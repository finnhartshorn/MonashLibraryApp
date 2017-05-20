package finnhartshorn.monashlibrary.Model;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Finn Hartshorn on 20/05/2017.
 */

public class Availability {
    private static final String TAG = "Availability";

    Status mClayton;
    Status mCaulfield;
    Status mPeninsula;

    public Availability(Status clayton, Status caulfield, Status peninsula) {
        mClayton = clayton;
        mCaulfield = caulfield;
        mPeninsula = peninsula;
    }

    public Availability(HashMap<String, String> availability) {
        Log.d(TAG, "Clayton Availability: " + availability.get("Clayton"));
        mClayton = Status.valueOf(availability.get("Clayton"));
        mCaulfield = Status.valueOf(availability.get("Caulfield"));
        mPeninsula = Status.valueOf(availability.get("Peninsula"));
    }

    public Status getClayton() {
        return mClayton;
    }

    public void setClayton(Status mClayton) {
        this.mClayton = mClayton;
    }

    public Status getCaulfield() {
        return mCaulfield;
    }

    public void setCaulfield(Status mCaulfield) {
        this.mCaulfield = mCaulfield;
    }

    public Status getPeninsula() {
        return mPeninsula;
    }

    public void setPeninsula(Status mPeninsula) {
        this.mPeninsula = mPeninsula;
    }
}

