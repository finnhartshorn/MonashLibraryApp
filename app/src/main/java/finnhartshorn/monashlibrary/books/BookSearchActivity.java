package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

public class BookSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ValueEventListener {

    private static final String TAG = "BookSearchActivity";
    // Recycler view references
    private RecyclerView mRecyclerView;
    private BookSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    // Items found textview reference
    private TextView mItemsFoundTextView;

    // A reference to the search view
    private SearchView mSearchView;

    // Stores current sort method, defaults to sort by newest added
    int selectedSortMethod = R.id.sort_added;

    // Stores reference to current query
    Query mBookQuery;

    // Database references
    private DatabaseReference mRootRef;

    // Stores the locations and genres that can be filtered
    private List<String> genres;
    private List<String> locations;

    ArrayList<Book> mBooklist = new ArrayList<>();

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mBooklist = new ArrayList<>();
        for (DataSnapshot bookSnapShot : dataSnapshot.getChildren()) {

            Book book = bookSnapShot.getValue(Book.class);
            mBooklist.add(book);
            Log.d(TAG, "Added book: " + book.getTitle());
        }
        mAdapter.updateBooklist(mBooklist);
        try {                   // TODO: Maybe change this?
            onQueryTextChange(mSearchView.getQuery().toString());           // After the data is changed, reapplies the search query
        } catch (NullPointerException e) {                                  // The search bar isn't created when this first runs,
            updateListCount();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "Failed loading books", databaseError.toException());
    }

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

        // Get reference to firebase database and set default query
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mBookQuery = mRootRef.child("books");
        mBookQuery.addValueEventListener(this);

        // Get and configure recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_search_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Create an adapter and add it to the recyclerview
        mAdapter = new BookSearchAdapter(this, mBooklist);
        recyclerView.setAdapter(mAdapter);

        // Make the recyclerview vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        genres = Arrays.asList(getResources().getStringArray(R.array.genres));
        locations = Arrays.asList(getResources().getStringArray(R.array.locations));
    }



    private void setSortQuery(Query query) {                    // Changes the firebase query and refreshes the recyclerview and count
        query.removeEventListener(this);
        mBookQuery = query;
        query.addValueEventListener(this);
//        mAdapter.updateDataset(mBooklist);
//        onQueryTextChange(mSearchView.getQuery().toString());           // After the data is changed, reapplies the search query
    }


    // Creates both toolbars
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
//        mSearchView.getQuery();

        // Inflate and init second toolbar          https://stackoverflow.com/questions/32808996/android-add-two-toolbars-in-the-same-activity
        Toolbar secondToolbar = (Toolbar) findViewById(R.id.search_refine_toolbar);

        final Menu secondMenu = secondToolbar.getMenu();

        menuInfalter.inflate(R.menu.search_refine_menu, secondMenu);
        // For each menu item in the second toolbar add an on click listener that passes the click the same method that handles items for the first toolbar
        // If the menu item has a submenu, add listeners to each item in the submenu
        for (int i = 0; i < secondMenu.size(); i++) {
            secondMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.hasSubMenu()) {
                        Menu sortby_menu = item.getSubMenu();
                        for (int j = 0; j < sortby_menu.size(); j++) {
                            MenuItem menuItem = sortby_menu.getItem(j);
                            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem innerItem) {
                                    return onOptionsItemSelected(innerItem);
                                }
                            });
                        }
                    }
                    checkMenuItem();                        // This makes sure only one item is checked
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
            case R.id.refine_text:
                createRefineDialogue();
            default:
//                Log.d(TAG, "ITEM CLICKED: " + item.getTitle());
//                Log.d(TAG, "Sort Method: " + sortMethod.name());
                break;
        }
        if (item.getGroupId() == R.id.sort_group) {
            if (!item.isChecked()) {
                switch (item.getItemId()) {
                    case R.id.sort_added:
                        selectedSortMethod = item.getItemId();                 // Changes the firebase query to get the items in a different order
                        setSortQuery(mRootRef.child("books"));
                        break;
                    case R.id.sort_author:
                        selectedSortMethod = item.getItemId();
                        setSortQuery(mRootRef.child("books").orderByChild("author"));
                        break;
                    case R.id.sort_title:
                        selectedSortMethod = item.getItemId();
                        setSortQuery(mRootRef.child("books").orderByChild("title"));
                        Log.d(TAG, "Sort by title");
                        break;
                    case R.id.sort_year:
                        selectedSortMethod = item.getItemId();
                        setSortQuery(mRootRef.child("books").orderByChild("publicationDate"));
                        break;
                }
                checkMenuItem();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void createRefineDialogue() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.refine_dialogue, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Refine Search");
        alertDialog.setView(view);

        final Spinner mGenreSpinner = (Spinner) view.findViewById(R.id.genre_spinner);
        final Spinner mLocationSpinner = (Spinner) view.findViewById(R.id.location_spinner);

        mGenreSpinner.setSelection(genres.indexOf(mAdapter.getGenreFilter()));
        mLocationSpinner.setSelection(locations.indexOf(mAdapter.getLocationFilter()));

        alertDialog.setPositiveButton("Refine", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String genre = mGenreSpinner.getSelectedItem().toString();
                String location = mLocationSpinner.getSelectedItem().toString();

                mAdapter.setGenreFilter(genre);
                mAdapter.setLocationFilter(location);

                onQueryTextChange(mSearchView.getQuery().toString());           // After the data is changed, reapplies the search query
                updateListCount();

                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void checkMenuItem() {

        Toolbar secondToolbar = (Toolbar) findViewById(R.id.search_refine_toolbar);

        final Menu secondMenu = secondToolbar.getMenu();

        // Sets the checked state of each sort menu item
        Menu sortMenu = secondMenu.findItem(R.id.sortby_action).getSubMenu();

        for (int i = 0; i < sortMenu.size(); i++) {             // Iterates over each item in the sort menu, if it is the currently selected one, make sure it is checked
            MenuItem menuItem = sortMenu.getItem(i);            //  otherwise uncheck it

            if (menuItem.getItemId() == selectedSortMethod) {
                menuItem.setChecked(true);
            } else {
                menuItem.setChecked(false);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Searching is done whenever the query text changes, this just closes the keyboard when the user presses enter/submit
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

