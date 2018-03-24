package ss_3.iastate.edu.vendme;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MachineSelection extends MainActivity {

    //Holds all of the vending machiens and their location/distance from user.
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


        //List view filling.
        vendingMachines = new ArrayList<String>();
        vendingMachines.add("Iowa State University");
        vendingMachines.add("Caribou Coffee");

        MachineList = (ListView) findViewById(R.id.MachineList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, vendingMachines);

        MachineList.setAdapter(adapter);

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

        startLocation();

        //Forces map to satellite view.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

}
