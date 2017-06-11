package finnhartshorn.monashlibrary.books.range;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.MainMenuActivity;
import finnhartshorn.monashlibrary.R;
import finnhartshorn.monashlibrary.books.BookSearchActivity;
import finnhartshorn.monashlibrary.model.Book;

import static java.security.AccessController.getContext;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 * Stores a firebase query and related data, is used for the nested recycler view on the book tab
 */

public abstract class BookRange implements ValueEventListener, View.OnClickListener {

    protected Context mContext;
    private String mTitle;
    private Query mBookQuery;
    protected OnBookDataChanged mChangeListener;
    protected OnBookDataCancelled mCancelListener;
    protected ArrayList<Book> mBooklist;
    protected String mOrderByChild = null;


    public interface OnBookDataChanged {
        void onDataChange(ArrayList<Book> bookList);
    }
    public interface OnBookDataCancelled {
        void onDataCancelled(DatabaseError error);
    }

    public BookRange(Context context, String title, Query bookQuery, OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        mContext = context;
        mTitle = title;
        mBookQuery = bookQuery;
        mChangeListener = changeListener;
        mCancelListener = cancelListener;
        mBookQuery.limitToFirst(10).addValueEventListener(this);            // Book ranges only display the first 10, to see more the user must click the 'more' button
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mBooklist = new ArrayList<>();
        for (DataSnapshot bookSnapShot : dataSnapshot.getChildren()) {
            Book book = bookSnapShot.getValue(Book.class);
            book.setFirebaseId(bookSnapShot.getKey());
            mBooklist.add(book);
        }
        mChangeListener.onDataChange(mBooklist);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        mCancelListener.onDataCancelled(databaseError);
    }

    public void refreshData() {
        if (mOrderByChild == null) {
            mBookQuery.limitToFirst(10).removeEventListener(this);
            mBookQuery.limitToFirst(10).addValueEventListener(this);
        } else {
            mBookQuery.orderByChild(mOrderByChild).limitToFirst(10).removeEventListener(this);
            mBookQuery.orderByChild(mOrderByChild).limitToFirst(10).addValueEventListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_more) {

            Intent newIntent = new Intent(mContext, BookSearchActivity.class);
            newIntent.putExtra("Query", mBookQuery.toString());
            if (mOrderByChild != null) {
                newIntent.putExtra("Order", mOrderByChild);
            }
            mContext.startActivity(newIntent);
        }
    }

    public Query getBookQuery() {
        return mBookQuery;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setListener(OnBookDataChanged listener) {
        mChangeListener = listener;
    }

    public Context getContext() {
        return mContext;
    }

    public String getOrderByChild() {
        return mOrderByChild;
    }

    public void setmOrderByChild(String OrderByChild) {
        mOrderByChild = OrderByChild;
        mBookQuery.removeEventListener(this);
        mBookQuery.orderByChild(mOrderByChild).limitToFirst(10).addValueEventListener(this);
    }
}
