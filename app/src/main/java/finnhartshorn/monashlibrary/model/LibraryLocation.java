package finnhartshorn.monashlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Finn Hartshorn on 4/05/2017.
 */

public class LibraryLocation implements Parcelable {

    private static final String TAG = "LibraryLocation";

    public static final LatLng CLAYTON_HAL = new LatLng(-37.909771, 145.132103);
    public static final LatLng CAULFIELD = new LatLng(-37.876999, 145.044236);
    public static final LatLng PENINSULA = new LatLng(-38.153160, 145.134976);

    private String mName;
    private String mStreet;
    private String mSuburb;
    private String mState;
    private String mPostcode;
    private String mCountry;
    private String mImageURL;

    public LibraryLocation() {
        // A blank constructor is required for firebase connectivity
    }


    // Most of this was generated based on the variables
    public LibraryLocation(String name, String street, String suburb, String state, String postcode, String country, String imageURL) {
        mName = name;
        mStreet = street;
        mSuburb = suburb;
        mState = state;
        mPostcode = postcode;
        mCountry = country;
        mImageURL = imageURL;
    }

    protected LibraryLocation(Parcel in) {
        mName = in.readString();
        mStreet = in.readString();
        mSuburb = in.readString();
        mState = in.readString();
        mPostcode = in.readString();
        mCountry = in.readString();
        mImageURL = in.readString();
    }

    public static final Creator<LibraryLocation> CREATOR = new Creator<LibraryLocation>() {
        @Override
        public LibraryLocation createFromParcel(Parcel in) {
            return new LibraryLocation(in);
        }

        @Override
        public LibraryLocation[] newArray(int size) {
            return new LibraryLocation[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mStreet);
        dest.writeString(mSuburb);
        dest.writeString(mState);
        dest.writeString(mPostcode);
        dest.writeString(mCountry);
        dest.writeString(mImageURL);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public String getSuburb() {
        return mSuburb;
    }

    public void setSuburb(String suburb) {
        mSuburb = suburb;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getPostcode() {
        return mPostcode;
    }

    public void setPostcode(String postcode) {
        mPostcode = postcode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
