package ss_3.iastate.edu.vendme;

import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;




/**
 * Main Application Class
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                GoogleMap.OnMyLocationButtonClickListener,
                                                                GoogleMap.OnMyLocationClickListener{

    //Google Maps Object.
    public GoogleMap mMap;

    //Maps view object.
    public SupportMapFragment mapFragment;

    //Location of the vending machine inside the ISU Caribou Coffee cafe.
    static final LatLng hubMachine1 = new LatLng(42.027134,-93.648371);

    //Used to gather the permission needed for location.
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;


    /**
     * Runs on application launch.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment. Allows use of onMapReady().
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * When "Search This Area" is pressed on the home screen, it generates
     *    a new activity view.
     * @param view //Access to the view the button resides in.
     */
    public void searchAreaButton(View view){

        //Creation of intent object. This object (from my understanding) can peek into the class and see its requirements and variables.
        Intent intent = new Intent(MainActivity.this, MachineSelection.class);

        //Starts activity DisplayMessageActivity.
        MainActivity.this.startActivity(intent);
    }


    /**
     * Assigns the Google Maps object with new data.
     * @param googleMap //Google maps object.
     */
    public void onMapReady(GoogleMap googleMap) {

        //Reloads the google maps object with a new map object.
        mMap = googleMap;

        //Creates basic (TEMPORARY) marker on ISU to show location.
        LatLng startLocation = new LatLng(42.0266, -93.6465);

        //Adds marker "startLocation" to the map. I used this location for two reasons.
        //   1) Center of United States so any user doesn't feel out of place on launch.
        //   2) It's where we are.
        mMap.addMarker(new MarkerOptions().position(startLocation).title("Iowa State University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));

        //Adds Google Maps Marker in the ISU Caribou Coffee cafe..
        mMap.addMarker(new MarkerOptions().position(hubMachine1)
                .title("Hub: Coca-Cola Machine")
                //Putting a new line after the title does not change anything. Its a different object than the snippet.
                //Putting the \n before contents lowers it, but does not return it to the left hand side.
                //I tried putting "\r" after \n but it does nothing as well.
                .snippet("\n\nContents: \n" + "- Coca-Cola\n" + "- Diet Coke\n" +
                        "- Cherry Coke\n" + "- Sprite\n" + "- Powerade"));
        MyInfoWindow customInfo = new MyInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfo);

//        Location deviceLoc = startLocation();
        startLocation();
        //Forces map to satellite view.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }


    /**
     * Function to act on result of Permissions prompt.
     * @param requestCode: private integer constant for permission
     * @param permissions: string of permission to check
     * @param grantResults: result of permissionRequest callback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length>0){
                // Permission was granted by user.
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
            //Request permission.
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else{
            //Permission granted, so enable location.
            mMap.setMyLocationEnabled(true);
            // gather location of the device
//            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////            Location deviceLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Location deviceLocation;
//            if((deviceLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER)) != null) {
//                return deviceLocation;
//            }
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

}
