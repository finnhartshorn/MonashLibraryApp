package finnhartshorn.monashlibrary.Books;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.Model.Book;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 20/05/2017.
 */

//TODO: This is very similar to InnerBookAdapter, only real diff is onBindViewHolder -- REFACTOR

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.BookSearchViewHolder>{

    private static final String TAG = "InnerBookAdapter";

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    private BookSearchViewHolder bookSearchViewHolder;
    protected Context context;

    protected ArrayList<Book> mBookList;

    public BookSearchAdapter(ArrayList<Book> bookList, Context context) {
        mBookList = new ArrayList<>(bookList);
        this.context = context;
    }


    @Override
    public BookSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_view, parent, false);

        bookSearchViewHolder = new BookSearchViewHolder(v, this);
        return bookSearchViewHolder;
    }

    @Override
    public void onBindViewHolder(BookSearchViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");
        Book mBook = mBookList.get(position);
        StorageReference coverReference = thumbnailsReference.child(mBook.getThumbnail());
        Glide.with(context)        // Glide caches images, which will reduce data footprint
                .using(new FirebaseImageLoader())
                .load(coverReference)
                .into(holder.getCoverImageView());
        holder.getTitleTextView().setText(mBook.getTitle());
        holder.getAuthorTextView().setText(mBook.getAuthor());
        holder.getGenreTextView().setText(mBook.getGenre());
        holder.getISBNTextView().setText(mBook.getISBN());
        if (mBook.claytonAvailability()) {
            holder.getClaytonAvailabilityTextView().setVisibility(View.VISIBLE);
        } else {
            holder.getClaytonAvailabilityTextView().setVisibility(View.INVISIBLE);
        }
        if (mBook.caulfieldAvailability()) {
            holder.getCaulfieldAvailabilityTextView().setVisibility(View.VISIBLE);
        } else {
            holder.getCaulfieldAvailabilityTextView().setVisibility(View.INVISIBLE);
        }
        if (mBook.peninsulaAvailability()) {
            holder.getPeninsulaAvailabilityTextView().setVisibility(View.VISIBLE);
        } else {
            holder.getPeninsulaAvailabilityTextView().setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public void updateDataset( ArrayList<Book> newBooklist) {
        mBookList = newBooklist;
        notifyDataSetChanged();
    }

    public static class BookSearchViewHolder extends RecyclerView.ViewHolder {

        private BookSearchAdapter bookSearchAdapter;
        TextView mTitleTextView;
        TextView mAuthorTextView;
        TextView mGenreTextView;
        TextView mISBNTextView;
        TextView mClaytonAvailabilityTextView;
        TextView mCaulfieldAvailabilityTextView;
        TextView mPeninsulaAvailabilityTextView;
        private ImageView mCoverImageView;

        public BookSearchViewHolder(View itemView, BookSearchAdapter parent) {
            super(itemView);
            bookSearchAdapter = parent;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book clickedBook = bookSearchAdapter.mBookList.get(getAdapterPosition());
                    Log.d(TAG, clickedBook.getTitle() + " clicked");
                    Intent newIntent = new Intent(bookSearchAdapter.context, BookDetailsActivity.class);
                    newIntent.putExtra("Book", clickedBook);

                    bookSearchAdapter.context.startActivity(newIntent);
                }
            });
            mCoverImageView = (ImageView) itemView.findViewById(R.id.searchCoverImageView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.search_title_textView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.search_author_textView);
            mGenreTextView = (TextView) itemView.findViewById(R.id.search_genre_textView);
            mISBNTextView = (TextView) itemView.findViewById(R.id.search_isbn_textView);
            mClaytonAvailabilityTextView = (TextView) itemView.findViewById(R.id.search_clayton_avail_textView);
            mCaulfieldAvailabilityTextView = (TextView) itemView.findViewById(R.id.search_caulfield_avail_textView);
            mPeninsulaAvailabilityTextView = (TextView) itemView.findViewById(R.id.search_peninsula_avail_textView);

        }

        // Auto generated getters
        public ImageView getCoverImageView() { return mCoverImageView; }

        public TextView getTitleTextView() { return mTitleTextView; }

        public TextView getAuthorTextView() { return mAuthorTextView; }

        public TextView getGenreTextView() { return mGenreTextView; }

        public TextView getISBNTextView() { return mISBNTextView; }

        public TextView getClaytonAvailabilityTextView() { return mClaytonAvailabilityTextView; }

        public TextView getCaulfieldAvailabilityTextView() { return mCaulfieldAvailabilityTextView; }

        public TextView getPeninsulaAvailabilityTextView() { return mPeninsulaAvailabilityTextView; }
    }
}
