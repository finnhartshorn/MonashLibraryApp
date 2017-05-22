package finnhartshorn.monashlibrary.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

public class BooksTabFragment extends Fragment {

    private static final String TAG = "BooksTabFragment";
    // Recycler view references
    private RecyclerView mRecyclerView;
    private OuterBookAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    ArrayList<ArrayList<Book>> mDataset = new ArrayList<ArrayList<Book>>();


    // Database references
    private DatabaseReference mRootRef;
//    private DatabaseReference mThumbnailRef;
    private DatabaseReference mBooksRef;

//    private OnFragmentInteractionListener mListener;

    public BooksTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add event listener to react to changes in book references

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        Query mBookQuery, mBookQuery2;
        mBookQuery = mRootRef.child("books").limitToFirst(10);
        mBookQuery2 = mRootRef.child("books").orderByChild("title");

        for (int i = 0; i < 2; i++) {
            mDataset.add(new ArrayList<Book>());
        }

        mBookQuery.addValueEventListener(new ValueEventListener() {             // TODO: Seperate class, this class?
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataset.set(0, new ArrayList<Book>());

                for (DataSnapshot bookSnapshot: dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    mDataset.get(0).add(book);
                }
                mAdapter.updateDataset(mDataset);
                Log.d(TAG, "New dataset, length: " + Integer.toString(mDataset.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed loading books", databaseError.toException());
            }
        });

        mBookQuery2.addValueEventListener(new ValueEventListener() {             // TODO: FIX THIS, IT IS BAD
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataset.set(1, new ArrayList<Book>());

                for (DataSnapshot bookSnapshot: dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    mDataset.get(1).add(book);
                }
                mAdapter.updateDataset(mDataset);
                Log.d(TAG, "New dataset, length: " + Integer.toString(mDataset.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed loading books", databaseError.toException());
            }
        });


        Log.d(TAG, "Initialised Dataset, length: " + Integer.toString(mDataset.size()));

        // Get and configure recycler view

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.books_recycler_view);
        recyclerView.setHasFixedSize(true);

        ArrayList<String> titles = new ArrayList();
        titles.add("Recently Added");
        titles.add("Popular Books");
        mAdapter = new OuterBookAdapter(titles, mDataset);
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

}
