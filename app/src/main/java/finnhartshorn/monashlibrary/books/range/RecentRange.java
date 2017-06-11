package finnhartshorn.monashlibrary.books.range;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 *
 * By default firebase lists everything by when it was added
 */

public class RecentRange extends BookRange {
    public RecentRange(Context context, OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        super(context, "Recently Added",FirebaseDatabase.getInstance().getReference().child("books"), changeListener, cancelListener);
    }
}