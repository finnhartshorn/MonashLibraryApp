package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import finnhartshorn.monashlibrary.Book;
import finnhartshorn.monashlibrary.BookAdapter;
import finnhartshorn.monashlibrary.R;

public class BooksTabFragment extends Fragment {

    private static final String TAG = "BooksTabFragment";
    // Recycler view references
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    ArrayList<Book> mDataset;


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
//        mAdapter = new BookAdapter(mDataset);
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        recyclerView.setHasFixedSize(true);
        initDataset();
        BookAdapter bookAdapter = new BookAdapter(mDataset);
        recyclerView.setAdapter(bookAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
//        Log.d(TAG, "Attached Adapter");
//    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    private void initDataset() {
        Log.d(TAG, "Dataset initiated");
        mDataset = new ArrayList<Book>();
        mDataset.add(new Book("1","1","1"));
        mDataset.add(new Book("2","2","2"));
        mDataset.add(new Book("3","3","3"));
        mDataset.add(new Book("4","4","4"));
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        mAdapter.cleanup();
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
