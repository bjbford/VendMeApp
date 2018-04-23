package ss_3.iastate.edu.vendme;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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

    //Holds the names (Building Location) of each vending machine.
    private List<String> MachineContents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_description);

        Intent Intent = getIntent();
        int MachineID = Intent.getIntExtra("MachineID", 0);
        Bundle bundle = getIntent().getExtras();
        myLocation = new Location("");
        myLocation.setLatitude(bundle.getDouble("lat"));
        myLocation.setLongitude(bundle.getDouble("lng"));

        //Populate TextViews with data from that machine.
        machine_Title = (TextView)findViewById(R.id.SelectedInfo);
        machine_Title.setText("Building Name: " + MainActivity.MachineResults[MachineID].getBuilding());

        machine_Location = (TextView)findViewById(R.id.Location);
        machine_Location.setText("Location Description: " + MainActivity.MachineResults[MachineID].getLocationDescription());

        machine_Distance = (TextView)findViewById(R.id.Distance);
        Location machineLoc = new Location("Chosen Machine Location");
        machineLoc.setLatitude(MainActivity.MachineResults[MachineID].getLocationLat());
        machineLoc.setLongitude(MainActivity.MachineResults[MachineID].getLocationLng());
        double distMiles = myLocation.distanceTo(machineLoc)/1609.34;
        double distFeet = distMiles * 5280.0;
        DecimalFormat dfmiles = new DecimalFormat("#.000");
        dfmiles.setRoundingMode(RoundingMode.CEILING);
        dfmiles.setMaximumFractionDigits(2);
        dfmiles.setMinimumIntegerDigits(0);
        NumberFormat nfFeet = DecimalFormat.getInstance();
        nfFeet.setMaximumFractionDigits(0);
        nfFeet.setRoundingMode(RoundingMode.CEILING);
        machine_Distance.setText("Distance from Device:\n" + dfmiles.format(distMiles) + " miles\n~ " + nfFeet.format(distFeet) + " feet");

        //List view filling. Only for show.
        MachineContents = new ArrayList<String>();
        for(int i = 0;i<MainActivity.MachineResults[MachineID].getMachineContents().size();i++){
            MachineContents.add(MainActivity.MachineResults[MachineID].getMachineContents().get(i) + "                                " +
                    MainActivity.MachineResults[MachineID].getMachinePrices().get(i));
        }
        MachineList = (ListView) findViewById(R.id.Contents);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, MachineContents);

        MachineList.setAdapter(adapter);
    }
}
