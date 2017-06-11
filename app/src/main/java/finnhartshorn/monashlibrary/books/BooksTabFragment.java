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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.books.range.BookRange;
import finnhartshorn.monashlibrary.books.range.LoanRange;
import finnhartshorn.monashlibrary.books.range.PopularRange;
import finnhartshorn.monashlibrary.books.range.RecentRange;
import finnhartshorn.monashlibrary.R;

public class BooksTabFragment extends Fragment implements BookRange.OnBookDataCancelled {

    private static final String TAG = "BooksTabFragment";

    // Adapter references
    private OuterBookAdapter mAdapter;

    ArrayList<BookRange> mDataset = new ArrayList<>();

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

        mDataset = new ArrayList<>();                                           // The outer recycler view takes an array of BookRanges, each stores a query that returns multiple books
        mDataset.add(new RecentRange(getContext(), null, this));
        mDataset.add(new PopularRange(getContext(), null, this));
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && !currentUser.isAnonymous()) {                // Only displays loans if the user is logged in with a proper account
            mDataset.add(new LoanRange(getContext(), null, this));
        }

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

    public void addLoanRange() {
        Log.d(TAG, "Added loan");
        for (int i = 0; i < mDataset.size(); i++) {                     // If the user changes accounts, the previous loan display is taken down and a new one is created
            if (mDataset.get(i).getTitle().equals("Your Loans")) {
                mDataset.remove(i);
            }
        }
        mDataset.add(new LoanRange(getContext(), null, this));                        // Otherwise it just creates
        mAdapter.updateDataset(mDataset);
        for (BookRange bookRange: mDataset) {
            bookRange.refreshData();
        }
    }
}
