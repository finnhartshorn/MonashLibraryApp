package finnhartshorn.monashlibrary.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Finn Hartshorn on 10/06/2017.
 */

public class Loan {
    private static final String TAG = "Loan";

    private Book mBook;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);
    private Date mDueDate;
    private String mLocation;

    public Loan() {}

    public Loan(Book book, String date) {
        mBook = book;
        try {
            mDueDate = dateFormat.parse("0000-01-1");           // This should always succeed and acts as a default value
            mDueDate = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: ", e);
        }
    }

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book mBook) {
        this.mBook = mBook;
    }

    public String getDueDate() {
        return dateFormat.format(mDueDate);
    }

    public void setDueDate(String dueDate) {
        try {
            mDueDate = dateFormat.parse("0000-01-1");
            mDueDate = dateFormat.parse(dueDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: ", e);
        }
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
