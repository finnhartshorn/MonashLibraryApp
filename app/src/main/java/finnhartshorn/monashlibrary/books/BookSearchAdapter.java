package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.content.Intent;
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

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 22/05/2017.
 */

public class BookSearchAdapter extends GenericAdapter<Book> implements GenericAdapter.OnViewHolderClick{

    private static final String TAG = "BookSearchAdapter";

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");


    public BookSearchAdapter(Context context, ArrayList<Book> bookList) {
        super(context, null, bookList);
        setOnClickListener(this);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.search_card_view, viewGroup, false);
    }

    @Override
    protected void bindView(Book book, GenericViewHolder viewHolder) {
        if (book != null) {
            StorageReference coverReference = thumbnailsReference.child(book.getThumbnail());

            // Gets the image view and text views
            ImageView mCover = (ImageView) viewHolder.getItemView().findViewById(R.id.searchCoverImageView);
            TextView mTitle = (TextView) viewHolder.getItemView().findViewById(R.id.search_title_textView);
            TextView mAuthor = (TextView) viewHolder.getItemView().findViewById(R.id.search_author_textView);
            TextView mGenre = (TextView) viewHolder.getItemView().findViewById(R.id.search_genre_textView);
            TextView mISBN = (TextView) viewHolder.getItemView().findViewById(R.id.search_isbn_textView);
            TextView mClaytonAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_clayton_avail_textView);
            TextView mCaulfieldAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_caulfield_avail_textView);
            TextView mPeninsulaAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_peninsula_avail_textView);

            Glide.with(getContext())        // Glide caches images, which will reduce data footprint
                    .using(new FirebaseImageLoader())
                    .load(coverReference)
                    .into(mCover);

            mTitle.setText(book.getTitle());
            mAuthor.setText(book.getAuthor());
            mGenre.setText(book.getGenre());
            mISBN.setText(book.getISBN());

            // If the books is not available in a location, the textview is hidden
            if (book.claytonAvailability()) {
                mClaytonAvailability.setVisibility(View.VISIBLE);
            } else {
                mClaytonAvailability.setVisibility(View.INVISIBLE);
            }
            if (book.caulfieldAvailability()) {
                mCaulfieldAvailability.setVisibility(View.VISIBLE);
            } else {
                mCaulfieldAvailability.setVisibility(View.INVISIBLE);
            }
            if (book.peninsulaAvailability()) {
                mPeninsulaAvailability.setVisibility(View.VISIBLE);
            } else {
                mPeninsulaAvailability.setVisibility(View.INVISIBLE);
            }

        }


    }

    @Override
    public void onClick(View view, int position) {
        Book book = getItem(position);
        Intent newIntent = new Intent(getContext(), BookDetailsActivity.class);
        newIntent.putExtra("Book", book);

        getContext().startActivity(newIntent);
    }
}
