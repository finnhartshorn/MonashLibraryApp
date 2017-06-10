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
import java.util.List;

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.R;
import finnhartshorn.monashlibrary.model.Book;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 */

// Both the search and loan view use the same card layout so this class holds common functionality between the two adapters
public abstract class BookCardAdapter extends GenericAdapter<Book> implements GenericAdapter.OnViewHolderClick {

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    public BookCardAdapter(Context context, List<Book> dataset) {
        super(context, null, dataset);
        setOnClickListener(this);
    }

    public void updateBooklist(ArrayList<Book> nDataset) {
        super.updateDataset(nDataset);
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

            Glide.with(getContext())        // Glide caches images, which will reduce data footprint
                    .using(new FirebaseImageLoader())
                    .load(coverReference)
                    .into(mCover);

            mTitle.setText(book.getTitle());
            mAuthor.setText(book.getAuthor());
            mGenre.setText(book.getGenre());
            mISBN.setText(book.getISBN());
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
