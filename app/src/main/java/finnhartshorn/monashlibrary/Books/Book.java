package finnhartshorn.monashlibrary.Books;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class Book implements Parcelable {
//    private String mISBN;
    private String mTitle;
    private String mThumbnail;
    private String mAuthor;
    private String mPubDate;
    private String mISBN;

    public Book() {
        // This is needed for Firebase
    }

    public Book(String Author, String pubDate, String thumbnail, String title) {
        mAuthor = Author;
        mTitle = title;
        mThumbnail = thumbnail;
        mPubDate = pubDate;
    }

    protected Book(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mAuthor = in.readString();
        mPubDate = in.readString();
        mISBN = in.readString();
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
        dest.writeString(mPubDate);
        dest.writeString(mISBN);
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

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getPublicationDate() { return mPubDate; }

    public void setPublicationDate(String mPubDate) {
        this.mPubDate = mPubDate;
    }

    public String getISBN() { return mISBN; }

    public void setISBN(String mISBN) { this.mISBN = mISBN; }

}
