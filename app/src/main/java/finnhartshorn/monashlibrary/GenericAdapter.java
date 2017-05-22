package finnhartshorn.monashlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by Finn Hartshorn on 22/05/2017.
 */

// Based on https://gist.github.com/Plumillon/f85c6be94e2fdaf339b9 -- Apache license

// This is a generic Recycler view adapter and view holder, functions as a base class for all recycler views in the app

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder> {
    private List<T> mDataset= Collections.emptyList();
    private Context mContext;       // Context is needed for new intents and loading images via glide
    private OnViewHolderClick mListener;

    public interface OnViewHolderClick {
        void onClick(View view, int position);
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView (T item, GenericViewHolder viewHolder);

    public GenericAdapter(Context context, OnViewHolderClick listener, List<T> dataset) {
        super();
        mContext = context;
        mListener = listener;
        mDataset = dataset;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
        bindView(getItem(position), holder);
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GenericViewHolder(createView(mContext, parent, viewType), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public T getItem(int index) {
        if (mDataset != null && index < mDataset.size()) {
            return mDataset.get(index);
        } else {
            return null;
        }
    }

    public void updateDataset( List<T> nDataset) {
        setDataset(nDataset);
        notifyDataSetChanged();
    }


    public List<T> getDataset() {
        return mDataset;
    }

    public void setDataset(List<T> mDataset) {
        this.mDataset = mDataset;
    }

    public Context getContext() {
        return mContext;
    }

    public void setOnClickListener(OnViewHolderClick mListener) {
        this.mListener = mListener;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnViewHolderClick mListener;
        private View itemView;


        public GenericViewHolder(View itemView, OnViewHolderClick listener) {
            super(itemView);
            mListener = listener;

            this.itemView = itemView;

            if (mListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v, getAdapterPosition());
            }
        }

        public View getItemView() {
            return itemView;
        }
    }
}
