package finnhartshorn.monashlibrary.Books;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
;import java.util.ArrayList;

import finnhartshorn.monashlibrary.Books.InnerBooks.BookAdapter;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 7/05/2017.
 */

public class OuterBookAdapter extends RecyclerView.Adapter<OuterBookAdapter.OuterBookViewHolder> {
    private static final String TAG = "OuterBookAdapter";

//    protected final Activity activity;

//    private final Context mContext;
    private ArrayList<String> mDatasetTitles;
    private ArrayList<ArrayList<Book>> mDataset;

    private static RecyclerView innerBookRecyclerView;

    public OuterBookAdapter(ArrayList<String> datasetTitles, ArrayList<ArrayList<Book>> dataset) {
//        this.activity = activity
        mDataset = dataset;
        mDatasetTitles = datasetTitles;
    }

    @Override
    public OuterBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_range_item, parent, false);

        OuterBookViewHolder outerBookViewHolder = new OuterBookViewHolder(v);

        return outerBookViewHolder;
    }

    @Override
    public void onBindViewHolder(OuterBookViewHolder holder, int position) {
        holder.getBookAdapter().updateDataset(mDataset.get(position));
        holder.getTitleTextView().setText(mDatasetTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDataset( ArrayList<ArrayList<Book>> newDataset) {
        mDataset = newDataset;
        notifyDataSetChanged();
    }

    public class OuterBookViewHolder extends RecyclerView.ViewHolder {
        private BookAdapter bookAdapter;
        private TextView titleTextView;

        public OuterBookViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            titleTextView = (TextView) itemView.findViewById(R.id.books_textView);
            innerBookRecyclerView = (RecyclerView)itemView.findViewById(R.id.outer_RecyclerView);
            innerBookRecyclerView.setHasFixedSize(true);

            bookAdapter = new BookAdapter(context, new ArrayList<Book>());
            innerBookRecyclerView.setAdapter(bookAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            innerBookRecyclerView.setLayoutManager(linearLayoutManager);

        }

        public BookAdapter getBookAdapter() {
            return bookAdapter;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }
    }
}
