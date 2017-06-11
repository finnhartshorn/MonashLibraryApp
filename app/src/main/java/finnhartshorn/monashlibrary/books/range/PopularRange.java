package finnhartshorn.monashlibrary.books.range;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 */

public class PopularRange extends BookRange {
    public PopularRange(OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        super("Popular Books", FirebaseDatabase.getInstance().getReference().child("books").orderByChild("title"), changeListener, cancelListener);
    }
}
