package finnhartshorn.monashlibrary.locations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.model.Location;
import finnhartshorn.monashlibrary.R;


public class LocationsTabFragment extends Fragment {

    private ArrayList<Location> mLocationList;

    public LocationsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        recyclerView.setHasFixedSize(true);
        initDataset();
        LocationRecyclerViewAdapter locationAdapter = new LocationRecyclerViewAdapter(mLocationList);
        recyclerView.setAdapter(locationAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void initDataset() {
        mLocationList = new ArrayList<Location>();
        mLocationList.add(new Location("Clayton", "13 College Walk Monash University", "Clayton", "VIC", "3168", "Australia"));
        mLocationList.add(new Location("Caulfield", "900 Dandenong Road", "Caulfield East", "VIC", "3145", "Australia"));
        mLocationList.add(new Location("Peninsula", "Building L McMahons Road", "Frankston", "VIC", "3199", "Australia"));
    }
}
