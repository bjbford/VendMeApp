package ss_3.iastate.edu.gps_testing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener{
        //GoogleMap.OnInfoWindowClickListener{

    private GoogleMap myMap;
    private static final int myLocationPermissionRequest = 1;
    //Vending machine #1 location: Hub
    static final LatLng hubMachine1 = new LatLng(42.027134,-93.648371);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        // Add a vending machine #1 position marker
        // This info will be fetched from database eventually
        myMap.addMarker(new MarkerOptions().position(hubMachine1)
                                .title("Hub: Coca-Cola Machine")
                                .snippet("Contents: \n" + "- Coca-Cola\n" + "- Diet Coke\n" +
                                         "- Cherry Coke\n" + "- Sprite\n" + "- Powerade"));
        MyInfoWindow customInfo = new MyInfoWindow(this);
        myMap.setInfoWindowAdapter(customInfo);
        //myMap.setOnInfoWindowClickListener();
        startLocation();
//        LatLng deviceLocation = myMap.getCameraPosition().target;
//        myMap.moveCamera(CameraUpdateFactory.newLatLng(deviceLocation));
        myMap.setOnMyLocationButtonClickListener(this);
        myMap.setOnMyLocationClickListener(this);
    }

    /**
     * Function to act on result of Permissions prompt.
     * @param requestCode: private integer constant for permission
     * @param permissions: string of permission to check
     * @param grantResults: result of permissionRequest callback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults){
        if(requestCode == myLocationPermissionRequest){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length>0){
                // Permission was granted by user
                startLocation();
            }
            else{
                //Exit application, because we need Location permission to operate.
                finishAffinity();
            }
        }
    }

    /**
     * Helper function to check fine location permission and request permission if not granted.
     */
    public void startLocation(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            //Request permission
            ActivityCompat.requestPermissions(this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},myLocationPermissionRequest);
        }
        else{
            //Permission granted, so enable location
            myMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Default Google location button click listener.
     * @return false in order to not consume button click event
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Location Button clicked",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Handles what to do when the device location icon is clicked.
     * @param location: current "fine" location of device.
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Your Device's Current Location:\n" + location,
                Toast.LENGTH_LONG).show();
    }

//    /**
//     * Show info windows when a Marker is clicked on.
//     * @param marker: Google Maps marker.
//     */
//    @Override
//    public void onInfoWindowClick(Marker marker){
//        marker.showInfoWindow();
//    }
}