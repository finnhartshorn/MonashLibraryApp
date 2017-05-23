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

import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 7/05/2017.
 */

// This book adapter handles the recycler view that contains cards of books, each card contains another recycler view containing the individual books
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
        if (position < mDataset.size() && position < mDatasetTitles.size()) {       // TODO: Find out why this is happening
            holder.getInnerBookAdapter().updateDataset(mDataset.get(position));
            holder.getTitleTextView().setText(mDatasetTitles.get(position));
        } else {
            Log.e(TAG, "Error position: " + position + " dataset size: " + mDataset.size());
        }
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
        private InnerBookAdapter innerBookAdapter;
        private TextView titleTextView;

        public OuterBookViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            titleTextView = (TextView) itemView.findViewById(R.id.books_textView);
            innerBookRecyclerView = (RecyclerView)itemView.findViewById(R.id.outer_RecyclerView);
            innerBookRecyclerView.setHasFixedSize(true);

            innerBookAdapter = new InnerBookAdapter(context, new ArrayList<Book>());
            innerBookRecyclerView.setAdapter(innerBookAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            innerBookRecyclerView.setLayoutManager(linearLayoutManager);

        }

        public InnerBookAdapter getInnerBookAdapter() {
            return innerBookAdapter;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }
    }
}
