package finnhartshorn.monashlibrary.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.books.range.BookRange;
import finnhartshorn.monashlibrary.books.range.PopularRange;
import finnhartshorn.monashlibrary.books.range.RecentRange;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

public class BooksTabFragment extends Fragment implements BookRange.OnBookDataCancelled {

    private static final String TAG = "BooksTabFragment";

    // Recycler view references
    private RecyclerView mRecyclerView;
    private OuterBookAdapter mAdapter;

    ArrayList<BookRange> mDataset;

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

        mDataset = new ArrayList<>();
        mDataset.add(new RecentRange(null, this));
        mDataset.add(new PopularRange(null, this));

        // Get and configure recycler view

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.books_recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new OuterBookAdapter(getContext(), mDataset);
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onDataCancelled(DatabaseError error) {
        Toast.makeText(getContext(), "Error loading books from database", Toast.LENGTH_LONG).show();
    }
}
