package finnhartshorn.monashlibrary.Books.InnerBooks;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Activity activity;

    private static final String TAG = "BookAdapter";

    private ArrayList<Book> mBookList;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    public BookAdapter(Activity activity, ArrayList<Book> bookList) {
        this.activity = activity;
        mBookList = new ArrayList<Book>(bookList);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_view, parent, false);

        BookViewHolder bookViewHolder = new BookViewHolder(v);

        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Log.d("BookAdapter", "Element " + position + " set.");
        Book mBook = mBookList.get(position);
        StorageReference coverReference = thumbnailsReference.child(mBook.getThumbnail());          //TODO: This could fail, handle that

        Glide.with(activity)        // Glide caches images, which will reduce data footprint
                .using(new FirebaseImageLoader())
                .load(coverReference)
                .into(holder.getCoverImageView());

    }
    public void updateDataset(ArrayList<Book> newBookList) {
        mBookList = newBookList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mCoverImageView;

        public BookViewHolder(View itemView) {
            super(itemView);
            mCoverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
        }


        public ImageView getCoverImageView() { return mCoverImageView; }
    }
}
