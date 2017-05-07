package finnhartshorn.monashlibrary.Books.InnerBooks;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class Book {
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
