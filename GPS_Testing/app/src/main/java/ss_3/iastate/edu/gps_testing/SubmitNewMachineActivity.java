package ss_3.iastate.edu.gps_testing;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by bjbford on 2/10/18.
 */

public class SubmitNewMachineActivity extends Activity {

    private EditText buildingName, machineType, locationDesc;
    private Button submissionBut;
    private TextView displayResults;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_machine_submission);

        buildingName = (EditText) findViewById(R.id.BuildingName);
        machineType = (EditText) findViewById(R.id.MachineType);
        locationDesc = (EditText) findViewById(R.id.LocationDesc);
        submissionBut = (Button) findViewById(R.id.Submission);
        displayResults = (TextView) findViewById(R.id.DisplayResults);


        submissionBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create json with fields
                JSONArray arr = new JSONArray();
                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("Building_Name", buildingName.getText().toString());
                    jObj.put("Machine_Type", machineType.getText().toString());
                    jObj.put("Location_Description", locationDesc.getText().toString());
                    arr.put(jObj);
                    String str = buildingName + "\n" + machineType + "\n" + locationDesc;
                    displayResults.setText(str);
                } catch (Exception e) {
                    Log.e("JSON Error: ", Log.getStackTraceString(e));
                }
            }
        });
    }
}