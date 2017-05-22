package finnhartshorn.monashlibrary.books;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

public class BookSearchActivity extends AppCompatActivity {

    private static final String TAG = "BookSearchActivity";
    // Recycler view references
    private RecyclerView mRecyclerView;
    private BookSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    ArrayList<Book> mBooklist = new ArrayList<Book>();


    // Database references
    private DatabaseReference mRootRef;
//    private DatabaseReference mThum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);


        Toolbar tb = (Toolbar) findViewById(R.id.search_toolbar);

        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        Query mBookQuery = mRootRef.child("books").limitToFirst(10);       //TODO: Don't limit
        mBookQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookSnapShot: dataSnapshot.getChildren()) {

                    Book book = bookSnapShot.getValue(Book.class);
                    mBooklist.add(book);
                    Log.d(TAG, "Added book: " + book.getTitle());
                }
                mAdapter.updateDataset(mBooklist);
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
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
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
}
