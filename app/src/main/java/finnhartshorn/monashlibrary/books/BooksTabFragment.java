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

// Unsure of what this actually does
//    public static BooksTabFragment newInstance(String param1, String param2) {
//        BooksTabFragment fragment = new BooksTabFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initDataset();
//        mAdapter = new InnerBookAdapter(mBooklist);
//        Log.d(TAG, "Test");
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
        View view = inflater.inflate(R.layout.fragment_books, container, false);

//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
//        recyclerView.setHasFixedSize(true);
//
//        ArrayList<String> titles = new ArrayList();
//        titles.add("Recently Added");
//        mAdapter = new OuterBookAdapter(titles, mBooklist);
//        recyclerView.setAdapter(mAdapter);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        Query mBookQuery;
        mBookQuery = mRootRef.child("books").limitToFirst(10);

        mBookQuery.addValueEventListener(new ValueEventListener() {             // TODO: Seperate class, this class?
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mDataset.size() == 0){
                    mDataset.add(new ArrayList<Book>());
                } else {
                    mDataset.set(0, new ArrayList<Book>());
                }
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

        Log.d(TAG, "Initialised Dataset, length: " + Integer.toString(mDataset.size()));

        // Get and configure recycler view

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.books_recycler_view);
        recyclerView.setHasFixedSize(true);

        ArrayList<String> titles = new ArrayList();
        titles.add("Recently Added");
        mAdapter = new OuterBookAdapter(titles, mDataset);
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

}
