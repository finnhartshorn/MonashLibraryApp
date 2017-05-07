package finnhartshorn.monashlibrary.Books.InnerBooks;

import android.content.Context;
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

import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;    // Needed for using Glide for image fetching and caching

    private static final String TAG = "BookAdapter";

    private ArrayList<Book> mBookList;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
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
        Log.d(TAG, "Element " + position + " set.");
        Book mBook = mBookList.get(position);
        StorageReference coverReference = thumbnailsReference.child(mBook.getThumbnail());          //TODO: This could fail, handle that

        Glide.with(context)        // Glide caches images, which will reduce data footprint
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
