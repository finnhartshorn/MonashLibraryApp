package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class BookSearchAdapter extends GenericAdapter<Book> implements GenericAdapter.OnViewHolderClick, Filterable {

    private static final String TAG = "BookSearchAdapter";

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference thumbnailsReference = storageReference.child("cover-images");

    private ArrayList<Book> mUnfilteredBookList;
    private BookFilter mBookFilter = new BookFilter();

    private enum sortFields {
        Title, Author, Date
    }


    public BookSearchAdapter(Context context, ArrayList<Book> bookList) {
        super(context, null, bookList);
        setOnClickListener(this);
        mUnfilteredBookList = bookList;

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

    @Override
    public Filter getFilter() {
        return mBookFilter;
    }

    private class BookFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults filterResults = new FilterResults();

            // If there isn't a query, don't filter and just return the list
            if (filterString == null || filterString.length() == 0){
                filterResults.values = mUnfilteredBookList;                    // cast to ArrayList<Book> ?
                filterResults.count = mUnfilteredBookList.size();
            } else {
//                final ArrayList<Book> unfilteredList = (ArrayList<Book>) getDataset();

                final ArrayList<Book> tempFilteredBookList = new ArrayList<>();             // Temp list to hold subset of filtered list, don't want the view to update until filtering is complete

                // Iterate through original book list, if title or author contains the search query, add to filtered list
                for (Book book: mUnfilteredBookList) {
                    if (book.getTitle().toLowerCase().contains(filterString) || book.getAuthor().toLowerCase().contains(filterString)) {
                        tempFilteredBookList.add(book);
                    }
                }
                filterResults.values = tempFilteredBookList;
                filterResults.count = tempFilteredBookList.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            updateDataset((ArrayList<Book>) results.values);
        }
    }
}
