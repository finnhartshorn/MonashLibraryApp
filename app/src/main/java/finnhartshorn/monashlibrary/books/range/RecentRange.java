package finnhartshorn.monashlibrary.books.range;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 */

public class RecentRange extends BookRange {
    public RecentRange(OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        super("Recently Added", FirebaseDatabase.getInstance().getReference().child("books"), changeListener, cancelListener);
    }
}