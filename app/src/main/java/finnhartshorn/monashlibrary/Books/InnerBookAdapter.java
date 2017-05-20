package finnhartshorn.monashlibrary.Books;

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

import finnhartshorn.monashlibrary.Model.Book;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class InnerBookAdapter extends RecyclerView.Adapter<InnerBookAdapter.BookViewHolder> {

    private final Context context;    // Needed for using Glide for image fetching and caching

    private static final String TAG = "InnerBookAdapter";

    protected ArrayList<Book> mBookList;
    private BookViewHolder bookViewHolder;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    public InnerBookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        mBookList = new ArrayList<>(bookList);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_view, parent, false);

        bookViewHolder = new BookViewHolder(v, this);

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


    protected static class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView mCoverImageView;
        private final InnerBookAdapter innerBookAdapter;

        public BookViewHolder(View itemView, InnerBookAdapter parent) {
            super(itemView);
            innerBookAdapter = parent;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book clickedBook = innerBookAdapter.mBookList.get(getAdapterPosition());
                    Log.d(TAG, clickedBook.getTitle() + " clicked");
                    Intent newIntent = new Intent(innerBookAdapter.context, BookDetailsActivity.class);
                    newIntent.putExtra("Book", clickedBook);

                    innerBookAdapter.context.startActivity(newIntent);
                }
            });
            mCoverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
        }
        protected ImageView getCoverImageView() { return mCoverImageView; }
    }
}
