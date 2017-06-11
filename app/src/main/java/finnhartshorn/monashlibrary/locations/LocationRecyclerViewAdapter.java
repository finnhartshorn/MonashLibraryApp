package finnhartshorn.monashlibrary.locations;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import finnhartshorn.monashlibrary.GenericAdapter;
import finnhartshorn.monashlibrary.model.LibraryLocation;
import finnhartshorn.monashlibrary.R;

/**
 * Created by Finn Hartshorn on 4/05/2017.
 *
 * Adapter for the location recycler view
 */

public class LocationRecyclerViewAdapter extends GenericAdapter<LibraryLocation> implements GenericAdapter.OnViewHolderClick {

    private static final String TAG = "LocationRVAdapter";

    public LocationRecyclerViewAdapter(Context context, List<LibraryLocation> dataset) {
        super(context, null, dataset);
        setOnClickListener(this);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.location_card_view, viewGroup, false);
    }

    @Override
    protected void bindView(LibraryLocation libraryLocation, GenericViewHolder viewHolder) {
        // Get views from libraryLocation card
        TextView nameTextView = (TextView) viewHolder.getItemView().findViewById(R.id.location_name_textview);
        TextView streetTextView = (TextView) viewHolder.getItemView().findViewById(R.id.streetadress_textView);
        TextView suburbTextView = (TextView) viewHolder.getItemView().findViewById(R.id.suburb_textView);
        TextView stateTextView = (TextView) viewHolder.getItemView().findViewById(R.id.state_textView);
        TextView postcodeCountryTextView = (TextView) viewHolder.getItemView().findViewById(R.id.postcode_country_textView);
        ImageView imageView = (ImageView) viewHolder.getItemView().findViewById(R.id.location_imageView);

        nameTextView.setText(libraryLocation.getName());
        streetTextView.setText(libraryLocation.getStreet());
        suburbTextView.setText(libraryLocation.getSuburb());
        stateTextView.setText(libraryLocation.getState());
        postcodeCountryTextView.setText(libraryLocation.getPostcode() + " " + libraryLocation.getCountry());

        Glide.with(getContext()).load(libraryLocation.getImageURL()).centerCrop().into(imageView);
    }

    @Override
    public void onClick(View view, int position) {
        LibraryLocation libraryLocation = getItem(position);
        Intent newIntent = new Intent(getContext(), MapsActivity.class);
        newIntent.putExtra("LibraryLocation", libraryLocation);

        getContext().startActivity(newIntent);
    }
}
