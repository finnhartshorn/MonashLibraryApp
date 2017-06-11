package finnhartshorn.monashlibrary.locations;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import finnhartshorn.monashlibrary.R;
import finnhartshorn.monashlibrary.model.LibraryLocation;

// A relatively simple map view used to show teh location of the three libraries and to link to google maps if users need directions

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;

    private LibraryLocation mLibraryLocation;
    private LatLng mLatLng;

    //
    private GoogleApiClient mAPIClient;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Library Locations");

        Intent intent = getIntent();
        mLibraryLocation = intent.getParcelableExtra("LibraryLocation");

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_maps);
        floatingActionButton.setOnClickListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Marks locations from the LibraryLocation class
        mMap.addMarker(new MarkerOptions().position(LibraryLocation.CLAYTON_HAL).title("Clayton Library"));
        mMap.addMarker(new MarkerOptions().position(LibraryLocation.CAULFIELD).title("Caulfield Library"));
        mMap.addMarker(new MarkerOptions().position(LibraryLocation.PENINSULA).title("Peninsula Library"));

        // The camera begins centred on the chosen location
        switch(mLibraryLocation.getName()) {
            case "Clayton Library":
                mLatLng = LibraryLocation.CLAYTON_HAL;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                break;
            case "Caulfield Library":
                mLatLng = LibraryLocation.CAULFIELD;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                break;
            case "Peninsula Library":
                mLatLng = LibraryLocation.PENINSULA;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                break;
        }

        // Test stuffs

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();                // Makes back arrow go back
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override                       // If the user clicks the floating button, open google maps with the selected library marked
    public void onClick(View v) {
        Uri gMapsIntentUri = Uri.parse(String.format(Locale.ENGLISH, "geo:0,0?q=%f,%f(%s)",
                mLatLng.latitude, mLatLng.longitude,
                mLibraryLocation.getName()));

        Intent newIntent = new Intent(Intent.ACTION_VIEW, gMapsIntentUri);
        newIntent.setPackage("com.google.android.apps.maps");
        if (newIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(newIntent);
        }
    }
}
