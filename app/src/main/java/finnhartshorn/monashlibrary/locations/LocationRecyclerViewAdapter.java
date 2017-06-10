package finnhartshorn.monashlibrary.locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.model.Location;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 4/05/2017.
 */

public class LocationRecyclerViewAdapter extends GenericAdapter<Location> implements GenericAdapter.OnViewHolderClick {

    private static final String TAG = "LocationRVAdapter";

    public LocationRecyclerViewAdapter(Context context, List<Location> dataset) {
        super(context, null, dataset);
        setOnClickListener(this);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.location_card_view, viewGroup, false);
    }

    @Override
    protected void bindView(Location location, GenericViewHolder viewHolder) {
        // Get views from location card
        TextView nameTextView = (TextView) viewHolder.getItemView().findViewById(R.id.location_name_textview);
        TextView streetTextView = (TextView) viewHolder.getItemView().findViewById(R.id.streetadress_textView);
        TextView suburbTextView = (TextView) viewHolder.getItemView().findViewById(R.id.suburb_textView);
        TextView stateTextView = (TextView) viewHolder.getItemView().findViewById(R.id.state_textView);
        TextView postcodeCountryTextView = (TextView) viewHolder.getItemView().findViewById(R.id.postcode_country_textView);
        ImageView imageView = (ImageView) viewHolder.getItemView().findViewById(R.id.location_imageView);

        nameTextView.setText(location.getName());
        streetTextView.setText(location.getStreet());
        suburbTextView.setText(location.getSuburb());
        stateTextView.setText(location.getState());
        postcodeCountryTextView.setText(location.getPostcode() + " " + location.getCountry());

        Glide.with(getContext()).load(location.getImageURL()).centerCrop().into(imageView);
    }

    @Override
    public void onClick(View view, int position) {
        Location location = getItem(position);
        Log.d(TAG, location.getName());
    }
}
