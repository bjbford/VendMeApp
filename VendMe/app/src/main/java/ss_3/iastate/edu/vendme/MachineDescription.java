package ss_3.iastate.edu.vendme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Displays data that is currently being held about the specific machine. Includes a picture,
 *    its contents, price, location, branc/type of machine, etc...
 * Created by Jared Danner on 3/16/2018.
 */

public class MachineDescription extends MachineSelection {

    //Holds all of the vending machiens and their location/distance from user.
    private ListView MachineList;

    //Holds the names (Building Location) of each vending machine.
    private List<String> MachineContents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_description);

        Intent Intent = getIntent();
        int MachineID = Intent.getIntExtra("MachineID", 0);

        //Populate TextViews with data from that machine.
        TextView machine_Title = (TextView)findViewById(R.id.SelectedInfo);
        //machine_Title.setText(MachineDatabase.getMachineTitle);

        TextView machine_Location = (TextView)findViewById(R.id.Location);
        //machine_Title.setText(MachineDatabase[MachineID].getMachineBuilding);

        TextView machine_Distance = (TextView)findViewById(R.id.Distance);
        //machine_Title.setText(MachineDatabase[MachineID].getMachineDistance);

        ListView machine_Contents = (ListView)findViewById(R.id.Contents);
        //machine_Title.setText(MachineDatabase[MachineID].getContents);



        //List view filling. Only for show.
        MachineContents = new ArrayList<String>();
        MachineContents.add("Diet Pepsi              $1.50                  5");
        MachineContents.add("Chips                      $1.00                 15");
        MachineContents.add("Coke                       $1.25                 10");
        MachineContents.add("Kit Kat                    $1.00                 12");
        MachineContents.add("Pepsi                      $1.25                 7");
        MachineContents.add("Fanta                      $2.00                 15");
        MachineContents.add("Mt. Dew                  $2.00                 10");
        MachineContents.add("Mellow Yellow        $2.25                  6");
        MachineContents.add("Mr. Pibb                   $5.00                  1");

        MachineList = (ListView) findViewById(R.id.Contents);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, MachineContents);

        MachineList.setAdapter(adapter);

    }
}
