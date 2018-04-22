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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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


/**
 * Main Application Class
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                GoogleMap.OnMyLocationButtonClickListener,
                                                                GoogleMap.OnMyLocationClickListener{
    // Local Database of Machines from MySQL server
    public Machine[] MachineDatabase;
    public int machineCount;

    //Google Maps Object.
    public GoogleMap mMap;

    //Maps view object.
    public SupportMapFragment mapFragment;

    // Main map screen buttons
    private Button settings;
    private Button searchThisArea;

    private Location deviceLocation;

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

        // Initialize Machine Database
        machineCount = 0;
        MachineDatabase = new Machine[machineCount];
        // TODO: Pull all Machines from MySQl to local Database with method call, using Machine setters.

        //Defines settings object to be that of button "settingsBtn".
        settings = (Button) findViewById(R.id.settingsBtn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creates intent with the new submissions page.
                Intent settings = new Intent(MainActivity.this,SubmitNewMachineActivity.class);
                //Launches new activity.
//                settings.putExtra("DeviceLocation",deviceLocation);
                MainActivity.this.startActivity(settings);
            }
        });

        //Search this area button
        searchThisArea = (Button) findViewById(R.id.searchAreaBtn);
        searchThisArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: check if there is text in the search bar
                LatLngBounds screenBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

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
        //TODO: test mergesort on Machines.
        merge_sort(ordered,0,ordered.length-1);
        return ordered;
    }


    /**
     * Recursive merge sort algorithm to sort Machine by distance.
     * @param arr
     * @param left
     * @param right
     */
    void merge_sort(Machine arr[], int left, int right){
        if (left < right){
            // Find the middle point
            int mid = (left+right)/2;
            // Sort first and second sub arrays
            merge_sort(arr, left, mid);
            merge_sort(arr , mid+1, right);
            // Merge the sorted sub arrays
            merge(arr, left, mid, right);
        }
    }


    /**
     * Helper function for the merge sort algorithm to merge sub arrays.
     * @param arr
     * @param left
     * @param mid
     * @param right
     */
    private void merge(Machine arr[], int left, int mid, int right){
        // Find sizes of two subarrays to be merged
        int leftSize = mid - left + 1;
        int rightSize = right - mid;
        // Temp subarrays
        Machine subLeft[] = new Machine [leftSize];
        Machine subRight[] = new Machine [rightSize];

        //Copy data to left subarray
        for (int i=0; i<leftSize; ++i){
            subLeft[i] = arr[left + i];
        }
        //Copy data to right subarray
        for (int j=0; j<rightSize; ++j){
            subRight[j] = arr[mid + 1 + j];
        }
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
        // Initial index of merged subarray array
        int k = left;
        while (i < leftSize && j < rightSize){
            Location leftLoc = new Location("");
            leftLoc.setLatitude(subLeft[i].getLocationLat());
            leftLoc.setLongitude(subLeft[i].getLocationLng());
            Location rightLoc = new Location("");
            rightLoc.setLatitude(subRight[i].getLocationLat());
            rightLoc.setLongitude(subRight[i].getLocationLng());
            //Compare distances from device location to Machines
            if ((deviceLocation.distanceTo(leftLoc)) <= (deviceLocation.distanceTo(rightLoc))){
                arr[k++] = subLeft[i++];
            }
            else{
                arr[k++] = subRight[j++];
            }
        }
        // Copy remaining elements of left sub array
        while (i < leftSize){
            arr[k++] = subLeft[i++];
        }
        // Copy remaining elements of right sub array
        while (j < rightSize){
            arr[k++] = subRight[j++];
        }
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
        Location locationGPS;
        Location locationNet;
        Location location = new Location("");
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            //Request permission.
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else{
            //Permission granted, so enable location.
            googleMap.setMyLocationEnabled(true);
            // gather location of the device
            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationNet = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationGPS = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // check if we have location from the network
            if(locationNet != null){
                location = locationNet;
                // check if we have location from GPS (this is preferable)
                if(locationGPS != null){
                    location = locationGPS;
                }
            }
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


    private void register(String username, String password) {
        String urlSuffix = "?username="+username+"&password="+password;
        class RegisterUser extends AsyncTask<String, Void, String> {

         // ProgressDialog loading;


         /*   @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
            }
            */

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL("http://proj-309-ss-3.cs.iastate.edu/android/v1/"+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Check your Internet connection",Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

}
