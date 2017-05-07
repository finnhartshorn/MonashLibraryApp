package finnhartshorn.monashlibrary.Books;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private ArrayList<Book> mBookList;

    public BookAdapter(ArrayList<Book> bookList) {
        mBookList = new ArrayList<Book>(bookList);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_view, parent, false);

        BookViewHolder bookViewHolder = new BookViewHolder(v);

        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Log.d("BookAdapter", "Element " + position + " set.");
        Book mBook = mBookList.get(position);
        holder.getTitleTextView().setText(mBook.getTitle());
        holder.getImageTextView().setText(mBook.getThumbnail());

    }
    public void updateDataset(ArrayList<Book> newBookList) {
        mBookList = newBookList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTitleTextView;
        protected TextView mImageTextView;

        public BookViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.card_title_text);
            mImageTextView = (TextView) itemView.findViewById(R.id.card_image_text);
        }

        public TextView getTitleTextView() {
            return mTitleTextView;
        }

        public TextView getImageTextView() {
            return mImageTextView;
        }
    }
}
