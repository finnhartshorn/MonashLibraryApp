package finnhartshorn.monashlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class Book implements Parcelable {

    private static final String TAG = "Book";
//    private String mISBN;
    private String mTitle;
    private String mThumbnail;
    private String mAuthor;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);
    private Date mPubDate;
    private String mISBN;
    private String mGenre;
    private Availability mAvailability;

    public Book() {
        // This is needed for Firebase
    }

    public Book(String Author, String pubDate, String thumbnail, String title, String genre, Availability availability){

        mAuthor = Author;
        mTitle = title;
        mThumbnail = thumbnail;
        try {
            mPubDate = dateFormat.parse("0000-01-1");           // This should always succeed and acts as a default value
            mPubDate = dateFormat.parse(pubDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: ", e);
        }
        mGenre = genre;
        mAvailability = availability;
    }

    protected Book(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mAuthor = in.readString();
        try {
            mPubDate = dateFormat.parse("0000-01-1");
            mPubDate = dateFormat.parse(in.readString());
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: ", e);
        }
        mGenre = in.readString();
        mISBN = in.readString();
        mAvailability = new Availability(
                Status.valueOf(in.readString()),
                Status.valueOf(in.readString()),
                Status.valueOf(in.readString()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mThumbnail);
        dest.writeString(mAuthor);
        String temp = dateFormat.format(mPubDate);
        Log.d(TAG, temp);
        dest.writeString(temp);
        dest.writeString(mGenre);
        dest.writeString(mISBN);
        dest.writeString(mAvailability.getClayton().name());
        dest.writeString(mAvailability.getCaulfield().name());
        dest.writeString(mAvailability.getPeninsula().name());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // Generated getters and setters
    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor
    ) {
        this.mAuthor = mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        mTitle = Title;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String Thumbnail) {
        this.mThumbnail = Thumbnail;
    }

    public String getPublicationDate() { return dateFormat.format(mPubDate); }

    public void setPublicationDate(String pubDate) {
        try {
            mPubDate = dateFormat.parse("0000-01-1");
            mPubDate = dateFormat.parse(pubDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: ", e);
        }
    }

    public String getISBN() { return mISBN; }

    public void setISBN(String ISBN) { mISBN = ISBN; }

    public String getGenre() { return mGenre; }

    public void setGenre(String Genre) { mGenre = Genre; }

    public Date getPublicationDateObject() {
        return mPubDate;
    }

    public int getPublicationYear() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mPubDate);
        return calendar.get(calendar.YEAR);
    }

    public Availability getAvailability() {
        return mAvailability;
    }

//    public void setAvailability(Availability availability) {
//        mAvailability = availability;
//    }

    public void setAvailability(HashMap<String, String> availability) {     // Firebase stores nested json as a hashmap of strings to strings
        Log.d(TAG, "Availability set");
        mAvailability = new Availability(availability);
    }

    public boolean claytonAvailability() {
        return mAvailability.getClayton() == Status.Available;
    }

    public boolean caulfieldAvailability() {
        return mAvailability.getCaulfield() == Status.Available;
    }

    public boolean peninsulaAvailability() {
        return mAvailability.getPeninsula() == Status.Available;
    }

}
