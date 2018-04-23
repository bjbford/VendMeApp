package ss_3.iastate.edu.vendme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;


/**
 * Main Application Class
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                GoogleMap.OnMyLocationButtonClickListener,
                                                                GoogleMap.OnMyLocationClickListener{
    // Local Database of Machines from MySQL server
    public static Machine[] MachineDatabase;
    public int machineCount=100;

    //Google Maps Object.
    public GoogleMap mMap;

    //Maps view object.
    public SupportMapFragment mapFragment;

    // Main map screen buttons
    private Button settings;
    private Button searchThisArea;

    private EditText searchBar;

    private Location deviceLocation;

    //Location of the vending machine inside the ISU Caribou Coffee cafe.
    static final LatLng hubMachine1 = new LatLng(42.027134,-93.648371);

    //Used to gather the permission needed for location.
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;



    private static final String REGISTER_URL = "http://proj-309-ss-3.cs.iastate.edu/android/v1/post.php";


    /**
     * Runs on application launch.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String  tag_string_req1 = "string_req1";

        String url1 = "http://proj-309-ss-3.cs.iastate.edu/post1.php";



        StringRequest strReq1 = new StringRequest(Request.Method.GET,
                url1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {






                machineCount = Integer.parseInt(response1.trim());


               // int temp = MachineDatabase.length;
               // Toast.makeText(getApplicationContext(),machineCount,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq1, tag_string_req1);



        // Obtain the SupportMapFragment. Allows use of onMapReady().
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchBar = (EditText) findViewById(R.id.SearchBar);

        // Initialize Machine Database

        MachineDatabase = new Machine[machineCount];
        for(int i =0; i< MachineDatabase.length; i++){
            MachineDatabase[i] = new Machine();
        }
        // TODO: Pull all Machines from MySQl to local Database with method call, using Machine setters.






//Machine m = new Machine(20,21,"blah","blah","blah","balh","blah");

//MachineDatabase[0]=m;


// Tag used to cancel the request
        String  tag_string_req = "string_req";

        String url = "http://proj-309-ss-3.cs.iastate.edu/post.php";



        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  ArrayList<String> sl = new ArrayList<String>();
                String s = response;
              //  sl.add(s);


                    String[] parts=  s.split("/");
              //  int c=0;
for(int i=1; i<parts.length; i++) {
   // String part1 = parts[0];
    String part2 = parts[i];

    String[] args = part2.split("#");

    String contents[] = args[5].split(",");
    ArrayList<String> contentsList = new ArrayList<>();

    for(int j =0; j<contents.length; j++){
        contentsList.add(contents[j]);
    }

    String prices[] = args[6].split(",");
    ArrayList<String> pricesList = new ArrayList<>();

    for(int k =0; k<prices.length; k++){
        pricesList.add(prices[k]);
    }


   // Machine m = new Machine(args[0], args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]), contentsList, pricesList);

   // MachineDatabase[i-1] = new Machine(args[0], args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]), contentsList, pricesList);

    MachineDatabase[i-1].setBuilding(args[0]);
    MachineDatabase[i-1].setLocationDescription(args[1]);
    MachineDatabase[i-1].setType(args[2]);
    MachineDatabase[i-1].setLocationLat(Double.parseDouble(args[3]));
    MachineDatabase[i-1].setLocationLng(Double.parseDouble(args[4]));
    MachineDatabase[i-1].setMachineContents(contentsList);
    MachineDatabase[i-1].setMachinePrices(pricesList);
}
              // Toast.makeText(getApplicationContext(),MachineDatabase[2].getBuilding(),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);






        //TEST








































        //Defines settings object to be that of button "settingsBtn".
        settings = (Button) findViewById(R.id.settingsBtn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creates intent with the new submissions page.
                Intent settings = new Intent(MainActivity.this,SubmitNewMachineActivity.class);
                //Launches new activity.
                MainActivity.this.startActivity(settings);
            }
        });

        //Search this area button
        searchThisArea = (Button) findViewById(R.id.searchAreaBtn);
        searchThisArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLngBounds screenBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                //Check which type of search
                boolean searchBarEmpty = TextUtils.isEmpty(searchBar.getText().toString());
                if(searchBarEmpty){
                    searchMachinesInArea(screenBounds);
                }
                else {
                    searchByItemInArea(searchBar.getText().toString(), screenBounds);
                }
                //Creation of intent object. This object (from my understanding) can peek into the class and see its requirements and variables.
                Intent intent = new Intent(MainActivity.this, MachineSelection.class);
                //Starts activity DisplayMessageActivity.
                MainActivity.this.startActivity(intent);
            }
        });
    }


    /**
     * Called when 'Search this Area' button is clicked, this method uses the bounds of the screen
     * to search local database for machines.
     * @param bounds Latitudinal/Longitudinal bounds for map on screen.
     * @return Array of machines in Area, ordered by closest to deviceLocation.
     */
    public Machine[] searchMachinesInArea(LatLngBounds bounds){
        Machine[] machinesInArea = new Machine[machineCount]; //Do I need this big of a size array or is it dynamic?
        int counter = 0;
        // Iterate over all machines in local database
        for(Machine i : MachineDatabase) {
            // Check if machine is in screen bounds.
            if(bounds.contains(new LatLng(i.getLocationLat(), i.getLocationLng()))) {
                // Add machine in bounds to new Array of Machines
                machinesInArea[counter++] = i;
            }
        }
        // Return the Array of machines ordered by distance from device.
        return orderMachinesByDistance(machinesInArea);
    }


    /**
     *  Called when 'Search this Area' button is clicked and the 'Search Bar' contains text of item
     * that the user is looking for. This will search the local database for machines that contain
     * the searched item.
     * @param item String of item to be searched for.
     * @param bounds Latitudinal/Longitudinal bounds for map on screen.
     * @return Array of machines in Area that contains the item searched for.
     */
    public Machine[] searchByItemInArea(String item, LatLngBounds bounds){
        Machine[] machinesWithItem = new Machine[machineCount]; //Do I need this big of a size array or is it dynamic?
        int counter = 0;
        // Iterate over all machines in local database
        for(Machine i : MachineDatabase){
            // Check if machine is in screen bounds and if it contains the item.
            if(bounds.contains(new LatLng(i.getLocationLat(),i.getLocationLng()))
                    && i.getMachineContents().contains(item)){
                // Add machine to new Array of Machines.
                machinesWithItem[counter++] = i;
            }
        }
        // Return the Array of machines ordered by distance from device.
        return orderMachinesByDistance(machinesWithItem);
    }


    /**
     * This method takes in an array of Machines and reorders them based on distance from the
     * devices location.
     * @param machines Array of Machines.
     * @return Machines order by distance from device's location.
     */
    public Machine[] orderMachinesByDistance(Machine[] machines){
        Machine[] ordered = new Machine[machines.length];
        //TODO: some sorting algorithm by distance.
//        for(int i = 0;i < input.length;i++){
//            Location.distanceBetween(deviceLocation.getLatitude(), deviceLocation.getLongitude(),
//                    input[i].getLocationLat(), input[i].getLocationLng());
//        }
        return ordered;
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
                .snippet("Contents: \n" + "- Coca-Cola\n" + "- Diet Coke\n" +
                        "- Cherry Coke\n" + "- Sprite\n" + "- Powerade"));
        MyInfoWindow customInfo = new MyInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfo);

        deviceLocation = startLocation(mMap);
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
                deviceLocation = startLocation(mMap);
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
    public Location startLocation(GoogleMap googleMap){
        Location location;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            //Request permission.
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            location = null;
        }
        else{
            //Permission granted, so enable location.
            googleMap.setMyLocationEnabled(true);
            // gather location of the device
            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
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
