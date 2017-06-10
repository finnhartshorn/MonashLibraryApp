package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.GenericBookAdapter;
import finnhartshorn.monashlibrary.books.range.BookRange;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class InnerBookAdapter extends GenericBookAdapter {

    private static final String TAG = "InnerBookAdapter";

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    public InnerBookAdapter(Context context, ArrayList<Book> bookList) {
        super(context, bookList);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.book_card_view, viewGroup, false);
    }

    @Override
    protected void bindView(Book book, GenericViewHolder viewHolder) {
        StorageReference coverReference = thumbnailsReference.child(book.getThumbnail());
        ImageView mCover = (ImageView) viewHolder.getItemView().findViewById(R.id.coverImageView);

        Glide.with(getContext())        // Glide caches images, which will reduce data footprint
                .using(new FirebaseImageLoader())
                .load(coverReference)
                .into(mCover);
    }
}
