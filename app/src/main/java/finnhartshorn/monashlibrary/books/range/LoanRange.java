package finnhartshorn.monashlibrary.books.range;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.R;
import finnhartshorn.monashlibrary.books.LoanViewActivity;
import finnhartshorn.monashlibrary.model.Book;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 * Returns loans of the currently logged in user, if this is used with an anonymous user, it will return nothing as an anonymous user has no data associated with their account
 */

public class LoanRange extends BookRange {
    public LoanRange(Context context, OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        super(context, "Your Loans", FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), changeListener, cancelListener);
    }

    @Override           // Overrides the BookRange as loans have a different database structure than just books
    public void onDataChange(DataSnapshot dataSnapshot) {
        mBooklist = new ArrayList<>();
        for (DataSnapshot loanSnapShot : dataSnapshot.getChildren()) {
            Book book = loanSnapShot.child("book").getValue(Book.class);
            mBooklist.add(book);
        }
        if (mChangeListener != null) {
            mChangeListener.onDataChange(mBooklist);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_more) {
            Intent newIntent = new Intent(getContext(), LoanViewActivity.class);
            getContext().startActivity(newIntent);
        }
    }
}




