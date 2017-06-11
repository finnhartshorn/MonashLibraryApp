package finnhartshorn.monashlibrary.books;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.books.range.BookRange;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * Created by Finn Hartshorn on 7/05/2017.
 */

// This book adapter handles the recycler view that contains ranges of books, each card contains another recycler view containing the individual books
public class OuterBookAdapter extends GenericAdapter<BookRange> {
    private static final String TAG = "OuterBookAdapter";


    public OuterBookAdapter(Context context, List<BookRange> dataset) {
        super(context, null, dataset);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.book_range_item, viewGroup, false);
    }

    @Override
    protected void bindView(BookRange bookRange, GenericViewHolder viewHolder) {
        TextView titleTextView = (TextView) viewHolder.getItemView().findViewById(R.id.book_range_title);
        titleTextView.setText(bookRange.getTitle());

        TextView moreTextView = (TextView) viewHolder.getItemView().findViewById(R.id.view_more);
        moreTextView.setOnClickListener(bookRange);

        // Uses Recyclerview-animators by Wasabeef for animating recycler views https://github.com/wasabeef/recyclerview-animators

        RecyclerView innerRecyclerView = (RecyclerView) viewHolder.getItemView().findViewById(R.id.outer_RecyclerView);
        innerRecyclerView.setItemAnimator(new SlideInRightAnimator());
        innerRecyclerView.getItemAnimator().setAddDuration(1000);

        InnerBookAdapter innerBookAdapter = new InnerBookAdapter(getContext(), new ArrayList<Book>());
        innerRecyclerView.setAdapter(innerBookAdapter);
        bookRange.setListener(innerBookAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        innerRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
