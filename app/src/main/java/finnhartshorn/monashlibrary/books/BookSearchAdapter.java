package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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
import java.util.Arrays;
import java.util.List;

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 22/05/2017.
 */

public class BookSearchAdapter extends BookCardAdapter implements Filterable {

    private static final String TAG = "BookSearchAdapter";

    private ArrayList<Book> mUnfilteredBookList;
    private BookFilter mBookFilter = new BookFilter();

    // Stores genre and location filter, defaults to all
    private String mGenreFilter = "All";
    private String mLocationFilter = "All";


    public BookSearchAdapter(Context context, ArrayList<Book> bookList) {
        super(context, bookList);
        mUnfilteredBookList = bookList;
    }

    public void updateBooklist(ArrayList<Book> nDataset) {
        mUnfilteredBookList =  nDataset;
        super.updateDataset(nDataset);
    }

    @Override
    protected void bindView(Book book, GenericViewHolder viewHolder) {
        if (book != null) {
            super.bindView(book, viewHolder);
            TextView mClaytonAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_clayton_avail_textView);
            TextView mCaulfieldAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_caulfield_avail_textView);
            TextView mPeninsulaAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_peninsula_avail_textView);

            int fadedColour = ContextCompat.getColor(getContext(), R.color.colorFaded);
            int fullColour = ContextCompat.getColor(getContext(), R.color.colorAccent);

            // If the books is not available in a location, the textview is faded
            if (book.getClaytonAvailability()) {
                mClaytonAvailability.setTextColor(fullColour);
            } else {
                mClaytonAvailability.setTextColor(fadedColour);
            }
            if (book.getCaulfieldAvailability()) {
                mCaulfieldAvailability.setTextColor(fullColour);
            } else {
                mCaulfieldAvailability.setTextColor(fadedColour);
            }
            if (book.getPeninsulaAvailability()) {
                mPeninsulaAvailability.setTextColor(fullColour);
            } else {
                mPeninsulaAvailability.setTextColor(fadedColour);
            }
        }
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
            if (filterString.length() == 0 && mGenreFilter.equals("All") && mLocationFilter.equals("All")) {
                filterResults.values = mUnfilteredBookList;                    // cast to ArrayList<Book> ?
                filterResults.count = mUnfilteredBookList.size();
            } else {
//                final ArrayList<Book> unfilteredList = (ArrayList<Book>) getDataset();

                final ArrayList<Book> tempFilteredBookList = new ArrayList<>();             // Temp list to hold subset of filtered list, don't want the view to update until filtering is complete

                // Iterate through original book list, if title or author contains the search query, add to filtered list
                for (Book book : mUnfilteredBookList) {
                    if ((book.getTitle().toLowerCase().contains(filterString) || book.getAuthor().toLowerCase().contains(filterString))
                            && (mGenreFilter.equals("All") || book.getGenre().contains(mGenreFilter))) {
                        switch (mLocationFilter) {
                            case "All":
                                tempFilteredBookList.add(book);
                                break;
                            case "Clayton":
                                if (book.getClaytonAvailability()) {
                                    tempFilteredBookList.add(book);
                                }
                                break;
                            case "Caulfield":
                                if (book.getCaulfieldAvailability()) {
                                    tempFilteredBookList.add(book);
                                }
                                break;
                            case "Peninsula":
                                if (book.getPeninsulaAvailability()) {
                                    tempFilteredBookList.add(book);
                                }
                                break;
                        }
//                        tempFilteredBookList.add(book);
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
    public void setGenreFilter(String genreFilter) { mGenreFilter = genreFilter; }

    public void setLocationFilter(String locationFilter) { mLocationFilter = locationFilter; }

    public String getGenreFilter() {
        return mGenreFilter;
    }

    public String getLocationFilter() {
        return mLocationFilter;
    }
}

