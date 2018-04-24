package ss_3.iastate.edu.vendme;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;


/**
 * Displays data that is currently being held about the specific machine. Includes a picture,
 *    its contents, price, location, branc/type of machine, etc...
 * Created by Jared Danner on 3/16/2018.
 */

public class MachineDescription extends MachineSelection {

    //Holds all of the vending machines and their location/distance from user.
    private ListView MachineList;
    private TextView machine_Title;
    private TextView machine_Location;
    private TextView machine_Distance;
    private Location myLocation;
    private Button WalkNav;
    private Button BikeNav;

    //Holds the names (Building Location) of each vending machine.
    private List<String> MachineContents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_description);

        Intent Intent = getIntent();
        final int MachineID = Intent.getIntExtra("MachineID", 0);
        Bundle bundle = getIntent().getExtras();
        myLocation = new Location("");
        myLocation.setLatitude(bundle.getDouble("lat"));
        myLocation.setLongitude(bundle.getDouble("lng"));

        //Populate TextViews with data from that machine.
        machine_Title = (TextView)findViewById(R.id.SelectedInfo);
        machine_Title.setText(" Building Name: " + MainActivity.MachineResults[MachineID].getBuilding() + " ");

        machine_Location = (TextView)findViewById(R.id.Location);
        machine_Location.setText("Location Description: " + MainActivity.MachineResults[MachineID].getLocationDescription());

        machine_Distance = (TextView)findViewById(R.id.Distance);
        Location machineLoc = new Location("Chosen Machine Location");
        machineLoc.setLatitude(MainActivity.MachineResults[MachineID].getLocationLat());
        machineLoc.setLongitude(MainActivity.MachineResults[MachineID].getLocationLng());
        double distMiles = myLocation.distanceTo(machineLoc)/1609.34;
        double distFeet = distMiles * 5280.0;
        DecimalFormat dfMiles = new DecimalFormat("#.000");
        dfMiles.setRoundingMode(RoundingMode.CEILING);
        dfMiles.setMaximumFractionDigits(2);
        dfMiles.setMinimumIntegerDigits(0);
        NumberFormat nfFeet = DecimalFormat.getInstance();
        nfFeet.setMaximumFractionDigits(0);
        nfFeet.setRoundingMode(RoundingMode.CEILING);
        machine_Distance.setText("Distance from Device:\n" + dfMiles.format(distMiles) + " miles\n~ " + nfFeet.format(distFeet) + " feet");

        //List view filling. Only for show.
        MachineContents = new ArrayList<String>();
        for(int i = 0;i<MainActivity.MachineResults[MachineID].getMachineContents().size();i++){
            //Loop for proper spacing
            String spaceString = "";
            int contLength = MainActivity.MachineResults[MachineID].getMachineContents().get(i).length();
            int priceLength = MainActivity.MachineResults[MachineID].getMachinePrices().get(i).length();
            for(int j = 0;j<(55-(contLength + priceLength));j++){
                spaceString += " ";
            }
            MachineContents.add(MainActivity.MachineResults[MachineID].getMachineContents().get(i) + spaceString +
                    "$" + MainActivity.MachineResults[MachineID].getMachinePrices().get(i));
        }
        MachineList = (ListView) findViewById(R.id.Contents);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, MachineContents);

        MachineList.setAdapter(adapter);

        WalkNav = (Button) findViewById(R.id.WalkNavigate);
        BikeNav = (Button) findViewById(R.id.BikeNavigate);


        WalkNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                String walkTo = "google.navigation:q=" + Double.toString(MainActivity.MachineResults[MachineID].getLocationLat())
                        + "," + Double.toString(MainActivity.MachineResults[MachineID].getLocationLng()) + "&mode=w";

                Uri gmmIntentUri = Uri.parse(walkTo);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        BikeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                String bikeTo = "google.navigation:q=" + Double.toString(MainActivity.MachineResults[MachineID].getLocationLat())
                        + "," + Double.toString(MainActivity.MachineResults[MachineID].getLocationLng()) + "&mode=b";

                Uri gmmIntentUri = Uri.parse(bikeTo);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }
}
