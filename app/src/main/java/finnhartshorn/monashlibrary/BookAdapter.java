package finnhartshorn.monashlibrary;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> mBookList;

    public BookAdapter(List<Book> bookList) {
        mBookList = bookList;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_card_view, parent, false);

        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Log.d("BookAdapter", "Element " + position + " set.");
        Book mBook = mBookList.get(position);
        holder.getTitleTextView().setText(mBook.getTitle());
        holder.getImageTextView().setText(mBook.getThumbnail());

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