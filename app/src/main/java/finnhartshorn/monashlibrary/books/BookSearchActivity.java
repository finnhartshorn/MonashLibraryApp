package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

public class BookSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "BookSearchActivity";
    // Recycler view references
    private RecyclerView mRecyclerView;
    private BookSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    // Items found textview reference
    private TextView mItemsFoundTextView;

    ArrayList<Book> mBooklist = new ArrayList<Book>();


    // Database references
    private DatabaseReference mRootRef;
//    private DatabaseReference mThum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        // Setup toolbar and add back button
        Toolbar tb = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get ref to items count text view
        mItemsFoundTextView = (TextView) findViewById(R.id.items_found);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        Query mBookQuery = mRootRef.child("books");
        mBookQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookSnapShot: dataSnapshot.getChildren()) {

                    Book book = bookSnapShot.getValue(Book.class);
                    mBooklist.add(book);
                    Log.d(TAG, "Added book: " + book.getTitle());
                }
                mAdapter.updateDataset(mBooklist);
                updateListCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed loading books", databaseError.toException());
            }
        });

        // Get and configure recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_search_recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new BookSearchAdapter(this, mBooklist);
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        // Inflate and init second toolbar          https://stackoverflow.com/questions/32808996/android-add-two-toolbars-in-the-same-activity
        Toolbar secondToolbar = (Toolbar) findViewById(R.id.search_refine_toolbar);
        Menu secondMenu = secondToolbar.getMenu();
        menuInfalter.inflate(R.menu.search_refine_menu, secondMenu);
        // For each menu item in the second toolbar add an on click listener that passes the click the same method that handles items for the first toolbar
        for (int i = 0; i < secondMenu.size(); i++) {
            secondMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Searching is done whenever the query text changes, this just closes the keyboard when the user presses enter/submit
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText, new Filter.FilterListener() {

            @Override
            public void onFilterComplete(int count) {
                updateListCount();
            }
        });
        return false;
    }

    private void updateListCount() {
        mItemsFoundTextView.setText(mAdapter.getItemCount() + " items found");
    }
}
