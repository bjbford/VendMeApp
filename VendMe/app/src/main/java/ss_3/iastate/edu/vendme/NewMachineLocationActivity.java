package ss_3.iastate.edu.vendme;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Activity used to drop a marker where the new machine is located then return to new machine
 * submission page.
 * Created by bjbford on 2/26/18.
 */

public class NewMachineLocationActivity extends MainActivity implements OnMapReadyCallback{
    private GoogleMap myMap;
    private Button addNewMarker, submitMarker;
    private Marker newMachine;
    private int markerCount;
    private Location myLocation;
//    private Location deviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_machine_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_newmachine);
        mapFragment.getMapAsync(this);

        //Device location passed from main activity/map
//        Bundle bundle = getIntent().getExtras();
//        myLocation = bundle.getParcelable("DeviceLocation");
    }

    /**
     * Assigns the Google Maps object with new data.
     * @param googleMap //Google maps object.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        markerCount = 0;
        //Creates basic (TEMPORARY) marker on ISU to show location.
        myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(42.0266, -93.6465)));
//        deviceLocation = startLocation(myMap);
        addNewMarker = (Button) findViewById(R.id.addMarkerBtn);
        submitMarker = (Button) findViewById(R.id.submitMarker);
        // gather location of the device
        myLocation = startLocation(myMap);
        final LatLng deviceLatLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
//        final LatLng deviceLatLng = new LatLng(deviceLocation.getLatitude(),deviceLocation.getLongitude());
        addNewMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markerCount < 1) {
                    // add new marker if there isn't one on the map already.
                    newMachine = myMap.addMarker(new MarkerOptions()
                            .title("NewMachine")
                            .position(deviceLatLng)
                            .draggable(true));
                    markerCount += 1;
                }
            }
        });
        submitMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return to submit new machine with marker location.
                Intent back = new Intent();
                back.putExtra("newMachineLocation",newMachine.getPosition());
                setResult(Activity.RESULT_OK,back);
                finish();
            }
        });
    }
}
