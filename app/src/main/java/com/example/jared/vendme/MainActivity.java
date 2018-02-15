package com.example.jared.vendme;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.URL;


/**
 * Main Application Class
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //Google Maps Object.
    private GoogleMap mMap;

    //View button "Search This Area"
    View button;

    //Iowa State University Maps Marker.
    private Marker ISU;

    //String used for file linkage.
    public static final String EXTRA_MESSAGE = "com.example.jared.vendme";

    //Used to gather the permission needed for location.
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;



    /**
     * Runs on application launch.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Sets "Search This Area" to be invisible and therefore, does not let the user proceed through the app.
        button = findViewById(R.id.button);
        button.setVisibility(View.GONE);

    }


    /**
     * When "Search This Area" is pressed on the home screen, it generates
     *    a new activity view.
     * @param view //Access to the view the button resides in.
     */
    public void searchAreaButton(View view){

        //Creation of intent object. This object (from my understanding) can peek into the class and see its requirements and variables.
        Intent intent = new Intent(this, MachineSelection.class);

        Location currentLocation = new Location(mMap.getMyLocation());

        //Loads message to be displayed in next activity screen.
        intent.putExtra(EXTRA_MESSAGE, "");

        //Starts activity DisplayMessageActivity.
        startActivity(intent);
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

        //Adds newly created marker "startLocation" to the map. I used this location for two reasons.
        //   1) Center of United States so any user doesn't feel out of place on launch.
        //   2) It's where we are.
        ISU = mMap.addMarker(new MarkerOptions().position(startLocation).title("Iowa State University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));

        mMap.setOnMarkerClickListener(this);

        //Checks to see if user has previously given the app persmission to use the GPS location.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //Sets map to current position.
            mMap.setMyLocationEnabled(true);

            //Sets "Search This Area" button to visible. Allows user to now click this button and proceed to the next page.
            button.setVisibility(View.VISIBLE);
        }
        else{
            //Prompts user for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            while(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //Wait for user to hopefully say yes.
            }

            //Sets map to current position.
            mMap.setMyLocationEnabled(true);

            //Sets "Search This Area" button to visible. Allows user to now click this button and proceed to the next page.
            button.setVisibility(View.VISIBLE);

        }

        //Forces map to satellite view.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Shows floor plans of available buildings.
        mMap.setIndoorEnabled(true);
    }

    public boolean onMarkerClick(Marker ISU){

        //Toast.makeText(this, "Iowa State University\nCollege of Engineering", Toast.LENGTH_SHORT).show();

        WebView myWebView = (WebView) findViewById(R.id.webView);
        setContentView(myWebView);
        myWebView.loadUrl("http://www.google.com");

        Intent intent = new Intent();

        return false;
    }

}
