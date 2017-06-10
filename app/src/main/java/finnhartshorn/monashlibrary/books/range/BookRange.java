package finnhartshorn.monashlibrary.books.range;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.model.Book;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 */

public abstract class BookRange implements ValueEventListener {

    private String mTitle;
    private Query mBookQuery;
    private OnBookDataChanged mChangeListener;
    private OnBookDataCancelled mCancelListener;


    public interface OnBookDataChanged {
        void onDataChange(ArrayList<Book> bookList);
    }
    public interface OnBookDataCancelled {
        void onDataCancelled(DatabaseError error);
    }

    public BookRange(String title, Query bookQuery, OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        mTitle = title;
        mBookQuery = bookQuery;
        mChangeListener = changeListener;
        mCancelListener = cancelListener;
        mBookQuery.limitToFirst(10).addValueEventListener(this);            // Book ranges only display the first 10, to see more the user must click the 'more' button
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<Book> mBooklist = new ArrayList<>();
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

    public Query getBookQuery() {
        return mBookQuery;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setListener(OnBookDataChanged listener) {
        mChangeListener = listener;
    }
}
