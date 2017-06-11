package finnhartshorn.monashlibrary.books.range;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 * Returns books sorted by title as popularity isn't measured server side.
 */

public class PopularRange extends BookRange {
    public PopularRange(Context context, OnBookDataChanged changeListener, OnBookDataCancelled cancelListener) {
        super(context, "Popular Books", FirebaseDatabase.getInstance().getReference().child("books"), changeListener, cancelListener);
        setmOrderByChild("title");
    }
}
