package ss_3.iastate.edu.vendme;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

import static android.text.TextUtils.isEmpty;

/**
 * This activity controls the entire New Machine Submission Page, which allows the user to submit
 * a new machine into our database, upon entering the required information.
 * Created by bjbford on 2/10/18.
 *   - Integrated into main application by jdanner.
 */
public class SubmitNewMachineActivity extends MainActivity implements View.OnClickListener {

    static final int REQUEST_PICTURE = 1; //Request code of 1 for image capture
    static final int REQUEST_MARKER = 2; //Request code of 2 for marker return
    private EditText editTextBuildingName, editTextMachineType, editTextLocationDesc;
    private EditText inItemName, inItemPrice;
    private Button submissionButton, machineLocation, machinePicture, buttonAdd;
    private LinearLayout newRow;
    private ProgressDialog progressDialog;
    private ImageView imgView;
    private Bitmap imgBitmap;
    private ArrayList<String> contentsList, priceList;
    private LatLng newMachineLocation;
    private double screenLat;
    private double screenLng;
    private float screenZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_machine_submission);

        Bundle bundle = getIntent().getExtras();
        screenLat = bundle.getDouble("centerLat");
        screenLng = bundle.getDouble("centerLng");
        screenZoom = bundle.getFloat("screenZoom");

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
                TextView itemPrice = (TextView) addView.findViewById(R.id.itemPrice);
                itemPrice.setText(inItemPrice.getText().toString());
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                final View.OnClickListener myListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                };

                buttonRemove.setOnClickListener(myListener);
                newRow.addView(addView);
            }
        });
    }

    /**
     * Add all contents and prices to proper ArrayLists to be posted to server upon submission.
     * - Created by bjbford on 3/26/18
     */
    private void addToArrayLists(LinearLayout l){
        // Iterate over all views in LinearLayout
        for(int i=0;i<l.getChildCount();i++){
            View v = l.getChildAt(i);
            TextView itemName = (TextView) v.findViewById(R.id.itemName);
            // add each content to ArrayList
            contentsList.add(itemName.getText().toString());
            TextView itemPrice = (TextView) v.findViewById(R.id.itemPrice);
            // add each price to ArrayList
            priceList.add(itemPrice.getText().toString());
        }
    }

    /**
     * Connect to Database and POST new machine. Iterates over contents and prices ArrayList.
     * - Created by jtbartz on 2/26/18
     */
    private void postNewMachine() {
        final String building = editTextBuildingName.getText().toString().trim();
        final String machineType = editTextMachineType.getText().toString().trim();
        final String description = editTextLocationDesc.getText().toString().trim();
        final String newMachineLat = Double.toString(newMachineLocation.latitude);
        final String newMachineLng = Double.toString(newMachineLocation.longitude);

        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        String contents = "";
        for(String i : contentsList){
            contents += i + ", ";
        }
        String prices = "";
        for(String j : priceList){
            prices += j + ", ";
        }
        final String contentsFinal = contents;
        final String pricesFinal = prices;

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
                params.put("lat",newMachineLat);
                params.put("lng",newMachineLng);
                params.put("contents",contentsFinal);
                params.put("prices",pricesFinal);
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
            // Double check they added a machine location and filled in fields!
            boolean buildNameEmpty = TextUtils.isEmpty(editTextBuildingName.getText().toString());
            boolean machineTypeEmpty = TextUtils.isEmpty(editTextMachineType.getText().toString());
            boolean locDescEmpty = TextUtils.isEmpty(editTextLocationDesc.getText().toString());
            if((newMachineLocation != null) && !buildNameEmpty && !machineTypeEmpty && !locDescEmpty && (newRow.getChildCount() > 0)) {
                addToArrayLists(newRow);
                postNewMachine();
                // Return to previous screen
                finish();
            }
            else{
                Toast.makeText(this,"You must fill in all information \n and provide a machine location! ",Toast.LENGTH_LONG).show();
            }
        }
        else if(view == machinePicture){
            // When the user clicks button, prompt permission to use camera and open camera app
            // TODO: Handle retrieval of picture back into the app
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePicture.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePicture, REQUEST_PICTURE);
            }
        }
        else if(view == machineLocation) {
            // When the user clicks button, prompt permission for location and open map to plot marker
            Intent intLocation = new Intent(SubmitNewMachineActivity.this,NewMachineLocationActivity.class);
            intLocation.putExtra("centerLat",screenLat);
            intLocation.putExtra("centerLng",screenLng);
            intLocation.putExtra("screenZoom",screenZoom);
            startActivityForResult(intLocation, REQUEST_MARKER);
        }
    }

    /**
     * Called on exit of activity invoked by startActivityForResult.
     * This is used to return from using the phones camera app, and handling the picture.
     * Also used to return from adding a marker indicating the new machine location.
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
        else if (requestCode == REQUEST_MARKER && resultCode == RESULT_OK){
            newMachineLocation = data.getExtras().getParcelable("newMachineLocation");
        }
    }
}