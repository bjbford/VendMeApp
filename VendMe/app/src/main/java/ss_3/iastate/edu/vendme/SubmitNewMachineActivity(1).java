package ss_3.iastate.edu.vendme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.*;

/**
 * This activity controls the entire New Machine Submission Page, which allows the user to submit
 * a new machine into our database, upon entering the required information.
 * Created by bjbford on 2/10/18.
 *   - Integrated into main application by jdanner.
 */

public class SubmitNewMachineActivity extends Activity {

    static final int REQUEST_PICTURE = 1; //Request code of 1 for image capture
    private EditText buildingName, machineType, locationDesc;
    private Button submissionBut, machineLocation, machinePicture;
    private TextView displayResults;
    private ImageView imgView;
    private Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_machine_submission);

        // Required EditText's
        buildingName = (EditText) findViewById(R.id.BuildingName);
        machineType = (EditText) findViewById(R.id.MachineType);
        locationDesc = (EditText) findViewById(R.id.LocationDesc);

        // TextView only used for debugging
        //displayResults = (TextView) findViewById(R.id.DisplayResults);

        // Page Buttons
        machineLocation = (Button) findViewById(R.id.MachineLocate);
        machinePicture = (Button) findViewById(R.id.MachinePicture);
        submissionBut = (Button) findViewById(R.id.Submission);

        imgView = (ImageView) findViewById(R.id.machinePicView);

        submissionBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create json with fields
                JSONArray arr = new JSONArray();
                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("Building", buildingName.getText().toString());
                    jObj.put("Type_Machine", machineType.getText().toString());
                    jObj.put("LocationDescription", locationDesc.getText().toString());
                    arr.put(jObj);
                    // Display results to TextView to show that it was parsed: FOR DEBUGGING!
                    String str = " Building Name: " + buildingName.getText().toString() + "\n" +
                            "Machine Type: "+ machineType.getText().toString() + "\n" +
                            "Location Description: " + locationDesc.getText().toString();
                    displayResults.setText(str);
                    // TODO: Post json to mySQL database
                        // 1. Get a connection
                        String host = "jdbc:mysql://mysql.cs.iastate.edu/db309ss3";
                        String UName = "dbu309ss3";
                        String Pass = "aS!2DW2Q";
                        Connection myCon = DriverManager.getConnection(host, UName, Pass);
                        // 2. Create a statement
                        Statement myStmt = myCon.createStatement();
                        // 3. Execute SQL query
                        String sql =  "INSERT INTO info(Building, Type_Machine, LocationDescription) VALUES ('Carver','Coca-Cola', 'First Floor')";
                        myStmt.executeUpdate(sql);

                   // TODO: Return to settings page
                } catch (Exception e) {
                    Log.e("JSON Error: ", Log.getStackTraceString(e));
                }
            }
        });

        // When the user clicks button, prompt permission to use camera and open camera app
        machinePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: handle camera permission prompt and launch camera


                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePicture.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(takePicture, REQUEST_PICTURE);
                }
            }
        });

        // When the user clicks button, prompt permission for location and open map to plot marker
        machineLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: merge Jonah's marker code for this and grab coordinates from plot
            }
        });
    }

    /**
     * Called on exit of activity invoked by startActivityForResult.
     * This is used to return from using the phones camera app, and handling the picture.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_PICTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imageBitmap);
            imgView.setVisibility(View.VISIBLE);
        }
    }
}