package ss_3.iastate.edu.vendme;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Page that displays vending machine results as well as location on map from main page search area or search bar.
 * - Created by jdanner.
 */
public class MachineSelection extends MainActivity {

    //Holds all of the vending machines and their location/distance from user.
    private ListView MachineList;
    private TextView MachinesFound;
    private double screenLat;
    private double screenLng;
    private Location myLocation;
    private float screenZoom;

    //Holds the names (Building Location) of each vending machine.
    private ArrayList<String> vendingMachines;


    @Override
    /**
     * This creates the layout for the Machine Selection Screen.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_selection);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_sel);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        myLocation = new Location("");
        myLocation.setLatitude(bundle.getDouble("lat"));
        myLocation.setLongitude(bundle.getDouble("lng"));
        screenLat = bundle.getDouble("centerLat");
        screenLng = bundle.getDouble("centerLng");
        screenZoom = bundle.getFloat("screenZoom");

        MachinesFound = (TextView) findViewById(R.id.MachinesFound);
        MachinesFound.setText("  Machines Found: " + MainActivity.MachineResults.length + "  ");

        vendingMachines = new ArrayList<String>();
        for(Machine i : MainActivity.MachineResults){
            Location machineLoc = new Location(i.getBuilding());
            machineLoc.setLatitude(i.getLocationLat());
            machineLoc.setLongitude(i.getLocationLng());
            double distMiles = myLocation.distanceTo(machineLoc)/1609.34;
            DecimalFormat dfMiles = new DecimalFormat("#.000");
            dfMiles.setRoundingMode(RoundingMode.CEILING);
            dfMiles.setMaximumFractionDigits(2);
            dfMiles.setMinimumIntegerDigits(0);
            String spaceString = "";
            //Loop for proper spacing
            for(int j = 0;j<(47-(i.getBuilding().length() + i.getType().length()));j++){
                spaceString += " ";
            }
            vendingMachines.add(i.getBuilding() + ": " + i.getType() + spaceString
                    + "~ " + dfMiles.format(distMiles) + " miles");
        }
        MachineList = (ListView) findViewById(R.id.ML);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, vendingMachines);

        MachineList.setAdapter(adapter);

        // Set an item click listener for ListView
        MachineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Creation of intent object.
                Intent intent = new Intent(MachineSelection.this, MachineDescription.class);

                //Uses the position in the List View to tell which Vending Machine it is.
                //   Passes its machine ID to the new activity.
                intent.putExtra("MachineID", position);
                intent.putExtra("lat",myLocation.getLatitude());
                intent.putExtra("lng",myLocation.getLongitude());

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(screenLat, screenLng),screenZoom));
        for(Machine i : MainActivity.MachineResults) {
            String snip = "Contents: \n";
            ArrayList<String> machineContents = i.getMachineContents();
            // Iterate over machine contents ArrayList for custom window popup.
            for (int j = 0; j < machineContents.size() - 1;j++){
                snip += ("- " + machineContents.get(j) + "\n");
            }
            // Last content so we don't want a new line after it.
            snip += "- " + machineContents.get(machineContents.size()-1);
            mMap.addMarker(new MarkerOptions().position(new LatLng(i.getLocationLat(),i.getLocationLng()))
                    .title(i.getBuilding() + ": " + i.getType())
                    .snippet(snip));
        }
        MyInfoWindow customInfo = new MyInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfo);

        //Forces map to satellite view.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
}
