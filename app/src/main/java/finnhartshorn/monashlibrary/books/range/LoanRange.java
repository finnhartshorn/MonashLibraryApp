package finnhartshorn.monashlibrary.books.range;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.model.Loan;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 */

public class LoanRange extends BookRange {
    public LoanRange(OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        super("Your Loans", FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), changeListener, cancelListener);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mBooklist = new ArrayList<>();
        for (DataSnapshot loanSnapShot : dataSnapshot.getChildren()) {
            Book book = loanSnapShot.child("book").getValue(Book.class);
            mBooklist.add(book);
        }

        mChangeListener.onDataChange(mBooklist);
    }
}




