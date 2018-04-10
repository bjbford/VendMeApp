package ss_3.iastate.edu.vendme;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

/**
 * Page that displays vending machine results as well as location on map from main page search area or search bar.
 * - Created by jdanner.
 */
public class MachineSelection extends MainActivity {

    //Holds all of the vending machines and their location/distance from user.
    private ListView MachineList;

    //Holds the names (Building Location) of each vending machine.
    private List<String> vendingMachines;


    @Override
    /**
     * This creates the layout for the Machine Selection Screen.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_selection);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_sel);
        mapFragment.getMapAsync(this);

        //TODO: Use Location.distanceBetween() method for approximate distances.
        //List view filling. Only for show.
        vendingMachines = new ArrayList<String>();
        vendingMachines.add("Iowa State University                           0.3 m");
        vendingMachines.add("The Hub                                                <0.1 m");
        vendingMachines.add("Coover                                                    0.1 m");

        MachineList = (ListView) findViewById(R.id.ML);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, vendingMachines);

        MachineList.setAdapter(adapter);

        // Set an item click listener for ListView
        MachineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Creation of intent object.
                Intent intent = new Intent(MachineSelection.this, MachineDescription.class);

                //Uses the position in the List View to tell which Vending Machine it is.
                //   Passes its machine ID to the new activity.
                intent.putExtra("MachineID", position);

                //Starts activity.
                MachineSelection.this.startActivity(intent);
            }
        });
    }


    /**
     * Assigns the Google Maps object with new data.
     * @param googleMap //Google maps object.
     */
    @Override
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
                .snippet("Contents: \n" + "- Coca-Cola\n" + "- Diet Coke\n" +
                        "- Cherry Coke\n" + "- Sprite\n" + "- Powerade"));
        MyInfoWindow customInfo = new MyInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfo);

        Location deviceLoc = startLocation(mMap);

        //Forces map to satellite view.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

}
