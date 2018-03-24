package ss_3.iastate.edu.vendme;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;

/**
 * This activity controls the entire New Machine Submission Page, which allows the user to submit
 * a new machine into our database, upon entering the required information.
 * Created by bjbford on 2/10/18.
 *   - Integrated into main application by jdanner.
 */
public class SubmitNewMachineActivity extends AppCompatActivity implements View.OnClickListener {
//public class SubmitNewMachineActivity extends MainActivity, AppCompatActivity {

    static final int REQUEST_PICTURE = 1; //Request code of 1 for image capture
    private EditText editTextBuildingName, editTextMachineType, editTextLocationDesc;
    private EditText inItemName, inItemPrice;
    private Button submissionButton, machineLocation, machinePicture, buttonAdd;
    private LinearLayout newRow;
    private ProgressDialog progressDialog;
    private ImageView imgView;
    private Bitmap imgBitmap;
    private ArrayList<String> contentsList, priceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_machine_submission);

        // Required EditText's
        editTextBuildingName = (EditText) findViewById(R.id.editTextBuildingName);
        editTextMachineType = (EditText) findViewById(R.id.editTextMachineType);
        editTextLocationDesc = (EditText) findViewById(R.id.LocationDesc);

        // Page Buttons
        machineLocation = (Button) findViewById(R.id.MachineLocate);
        machinePicture = (Button) findViewById(R.id.MachinePicture);
        submissionButton = (Button) findViewById(R.id.Submission);

        progressDialog = new ProgressDialog(this);

        imgView = (ImageView) findViewById(R.id.machinePicView);
        submissionButton.setOnClickListener(this);
        machinePicture.setOnClickListener(this);
        machineLocation.setOnClickListener(this);

        // Add contents section
        inItemName = (EditText) findViewById(R.id.inItemName);
        inItemPrice = (EditText) findViewById(R.id.inItemPrice);
        buttonAdd = (Button) findViewById(R.id.add);
        newRow = (LinearLayout) findViewById(R.id.new_row);

        // ArrayLists to hold contents and prices of new machine.
        contentsList = new ArrayList<String>();
        priceList = new ArrayList<String>();

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.list_row, null);
                TextView itemName = (TextView) addView.findViewById(R.id.itemName);
                itemName.setText(inItemName.getText().toString());
                contentsList.add(inItemName.getText().toString());
                TextView itemPrice = (TextView) addView.findViewById(R.id.itemPrice);
                itemPrice.setText(inItemPrice.getText().toString());
                priceList.add(inItemPrice.getText().toString());
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                final View.OnClickListener myListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        TextView rmName = (TextView) v.findViewById(R.id.itemName);
                        contentsList.remove(rmName.getText().toString());
                        TextView rmPrice = (TextView) v.findViewById(R.id.itemPrice);
                        priceList.remove(rmPrice.getText().toString());
                    }
                };

                buttonRemove.setOnClickListener(myListener);
                newRow.addView(addView);
            }
        });
    }

    /**
     * Connect to Database and POST new machine.
     * - Created by jtbartz on 2/26/18
     */
    private void postNewMachine() {
        final String building = editTextBuildingName.getText().toString().trim();
        final String machineType = editTextMachineType.getText().toString().trim();
        final String description = editTextLocationDesc.getText().toString().trim();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("building", building);
                params.put("type", machineType);
                params.put("desc", description);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * Handles page button click listeners.
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == submissionButton) {
            postNewMachine();
            // Return to previous screen
            Intent settings = new Intent(SubmitNewMachineActivity.this,MachineSelection.class);
            //Launches new activity.
            SubmitNewMachineActivity.this.startActivity(settings);
        }
        else if(view == machinePicture){
            // When the user clicks button, prompt permission to use camera and open camera app
            // TODO: Handle retrieval of picture back into the app w/o crash.
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePicture.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePicture, REQUEST_PICTURE);
            }
        }
        else if(view == machineLocation){
            // When the user clicks button, prompt permission for location and open map to plot marker
            // TODO: merge Jonah's marker code for this and grab coordinates from plot
        }
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
            //imgView.setImageBitmap(imageBitmap);
            //imgView.setVisibility(View.VISIBLE);
        }
    }
}