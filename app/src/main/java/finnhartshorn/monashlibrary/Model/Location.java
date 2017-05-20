package finnhartshorn.monashlibrary.Model;

/**
 * Created by Finn Hartshorn on 4/05/2017.
 */

public class Location {
    private String mName;
    private String mStreet;
    private String mSuburb;
    private String mState;
    private String mPostcode;
    private String mCountry;

    public Location() {
        // A blank constructor is required for firebase connectivity
    }


    // Most of this was generated based on the variables
    public Location(String name, String street, String suburb, String state, String postcode, String country) {
        mName = name;
        mStreet = street;
        mSuburb = suburb;
        mState = state;
        mPostcode = postcode;
        mCountry = country;
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
}
