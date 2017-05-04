package finnhartshorn.monashlibrary.Books;

/**
 * Created by Finn Hartshorn on 3/05/2017.
 */

public class Book {
    private String mISBN;
    private String mTitle;
    private String mThumbnail;

    public Book() {
        // This is needed for Firebase
    }

    public Book(String ISBN, String title, String thumbnail) {
        mISBN = ISBN;
        mTitle = title;
        mThumbnail = thumbnail;
    }

    // Generated getters and setters
    public String getISBN() {
        return mISBN;
    }

    public void setISBN(String mISBN
    ) {
        this.mISBN = mISBN;
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
}
