package ss_3.iastate.edu.vendme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by brianbradford on 2/26/18.
 */

public class NewMachineLocationActivity extends MainActivity implements OnMapReadyCallback{
    private GoogleMap myMap;
    private Button addNewMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_machine_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        Location deviceLocation = startLocation();
        addNewMarker = (Button) findViewById(R.id.addMarkerBtn);
        // gather location of the device
        final LatLng deviceLatLng = new LatLng(deviceLocation.getLatitude(),deviceLocation.getLongitude());

        addNewMarker.setOnClickListener((){
            Marker newMachine = myMap.addMarker(new MarkerOptions()
                    .title("NewMachine")
                    .position(deviceLatLng)
                    .draggable(true));
        });
        //TODO: Grab coordinates of marker when the 'OK' button is clicked

    }
}
