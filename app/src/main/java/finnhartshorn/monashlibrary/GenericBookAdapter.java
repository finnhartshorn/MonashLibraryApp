package finnhartshorn.monashlibrary;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import finnhartshorn.monashlibrary.books.BookDetailsActivity;
import finnhartshorn.monashlibrary.books.range.BookRange;
import finnhartshorn.monashlibrary.model.Book;

/**
 * Created by Finn Hartshorn on 11/06/2017.
 */

public abstract class GenericBookAdapter extends GenericAdapter<Book> implements GenericAdapter.OnViewHolderClick, BookRange.OnBookDataChanged {


    public GenericBookAdapter(Context context, List<Book> dataset) {
        super(context, null, dataset);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        Book book = getItem(position);
        Intent newIntent = new Intent(getContext(), BookDetailsActivity.class);
        newIntent.putExtra("Book", book);

        getContext().startActivity(newIntent);
    }

    @Override
    public void onDataChange(ArrayList<Book> bookList) {
        updateDataset(bookList);
    }
}
