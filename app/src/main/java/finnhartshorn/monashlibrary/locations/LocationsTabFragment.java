package finnhartshorn.monashlibrary.locations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import finnhartshorn.monashlibrary.model.LibraryLocation;
import finnhartshorn.monashlibrary.R;


public class LocationsTabFragment extends Fragment {

    private ArrayList<LibraryLocation> mLibraryLocationList;

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
        LocationRecyclerViewAdapter locationAdapter = new LocationRecyclerViewAdapter(getContext(), mLibraryLocationList);
        recyclerView.setAdapter(locationAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void initDataset() {
        mLibraryLocationList = new ArrayList<>();
        mLibraryLocationList.add(new LibraryLocation("Clayton Library", "13 College Walk Monash University", "Clayton", "VIC", "3168", "Australia", "https://www.monash.edu/__data/assets/image/0003/157764/banner-title-hal.jpg"));
        mLibraryLocationList.add(new LibraryLocation("Caulfield Library", "900 Dandenong Road", "Caulfield East", "VIC", "3145", "Australia", "https://www.monash.edu/__data/assets/image/0006/918249/caulfield-B.jpg"));
        mLibraryLocationList.add(new LibraryLocation("Peninsula Library", "Building L McMahons Road", "Frankston", "VIC", "3199", "Australia", "https://www.monash.edu/__data/assets/image/0003/918192/Peninsula-Library-B.jpg"));
    }
}
