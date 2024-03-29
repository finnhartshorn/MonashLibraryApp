package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import finnhartshorn.monashlibrary.R;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.model.Loan;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 *
 * This adapter handles the loan recycler view
 */

public class LoanViewAdapter extends BookCardAdapter {

    private static final String TAG = "LoanViewAdapter";

    private HashMap<Book, Loan> mLoanMap;

    public LoanViewAdapter(Context context, ArrayList<Book> bookList, HashMap<Book, Loan> loanMap) {
        super(context, bookList);
        mLoanMap = loanMap;
    }

    public void updateBookData(ArrayList<Book> bookList, HashMap<Book, Loan> loanMap) {
        mLoanMap = loanMap;
        super.updateBooklist(bookList);
    }

    @Override
    protected void bindView(Book book, GenericViewHolder viewHolder) {
        super.bindView(book, viewHolder);
        TextView mClaytonAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_clayton_avail_textView);
        TextView mCaulfieldAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_caulfield_avail_textView);
        TextView mPeninsulaAvailability = (TextView) viewHolder.getItemView().findViewById(R.id.search_peninsula_avail_textView);

        String location = mLoanMap.get(book).getLocation();
        String dueDate = mLoanMap.get(book).getShortDueDate();

        // This is a bit messy as it uses the same card as the search view
        mClaytonAvailability.setText(location);
        mCaulfieldAvailability.setVisibility(View.INVISIBLE);
        mPeninsulaAvailability.setText(getContext().getString(R.string.loans_due_date, dueDate));
    }
}
