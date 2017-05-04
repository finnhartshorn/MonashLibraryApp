package finnhartshorn.monashlibrary.Locations;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 4/05/2017.
 */

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder> {


    private ArrayList<Location> mLocationList;

    public LocationRecyclerViewAdapter(ArrayList<Location> LocationList) {
        mLocationList = new ArrayList<Location>(LocationList);
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card_view, parent, false);

        LocationViewHolder LocationViewHolder = new LocationViewHolder(v);

        return LocationViewHolder;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        Log.d("Location...Adapter", "Element " + position + " set.");
        Location mLocation = mLocationList.get(position);
        holder.getNameTextView().setText(mLocation.getName());
        holder.getStreetTextView().setText(mLocation.getStreet());
        holder.getSuburbTextView().setText(mLocation.getSuburb());
        holder.getStateTextView().setText(mLocation.getState());
        holder.getPostcodeCountryTextView().setText(mLocation.getPostcode() + " " + mLocation.getCountry());

    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mStreetTextView;
        private TextView mSuburbTextView;
        private TextView mStateTextView;
        private TextView mPostcodeCountryTextView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.location_name_textview);
            mStreetTextView = (TextView) itemView.findViewById(R.id.streetadress_textView);
            mSuburbTextView = (TextView) itemView.findViewById(R.id.suburb_textView);
            mStateTextView = (TextView) itemView.findViewById(R.id.state_textView);
            mPostcodeCountryTextView = (TextView) itemView.findViewById(R.id.postcode_country_textView);
        }

        public TextView getNameTextView() {
            return mNameTextView;
        }

        public TextView getStreetTextView() {
            return mStreetTextView;
        }

        public TextView getSuburbTextView() {
            return mSuburbTextView;
        }

        public TextView getStateTextView() {
            return mStateTextView;
        }

        public TextView getPostcodeCountryTextView() {
            return mPostcodeCountryTextView;
        }
    }

}
