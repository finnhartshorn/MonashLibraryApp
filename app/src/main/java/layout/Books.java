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

import java.util.List;

import finnhartshorn.monashlibrary.Book;
import finnhartshorn.monashlibrary.BookAdapter;
import finnhartshorn.monashlibrary.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Books.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Books#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Books extends Fragment {

    private static final String TAG = "Books";
    // Recycler view references
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    List<Book> mDataset;


    // Database references
    private DatabaseReference mRootRef;
//    private DatabaseReference mThumbnailRef;
    private DatabaseReference mBooksRef;

    private OnFragmentInteractionListener mListener;

    public Books() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Reference the database location were book thumbnails are located
     * @return A new instance of fragment Books.
     */
    // TODO: Rename and change types and number of parameters
    public static Books newInstance(String param1, String param2) {
        Books fragment = new Books();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initDataset();
        mAdapter = new BookAdapter(mDataset);
        Log.d(TAG, "Test");
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

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // get reference to recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        mRecyclerView.setHasFixedSize(true); // Layout size shouldn't change when the content of the view changes, so this should improve performance
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "Attached Adapter");
//        mRootRef = FirebaseDatabase.getInstance().getReference();
//        mBooksRef = mRootRef.child("books");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void initDataset() {
        Log.d(TAG, "Dataset initiated");
        mDataset.add(new Book("1","1","1"));
        mDataset.add(new Book("2","2","2"));
        mDataset.add(new Book("3","3","3"));
        mDataset.add(new Book("4","4","4"));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
