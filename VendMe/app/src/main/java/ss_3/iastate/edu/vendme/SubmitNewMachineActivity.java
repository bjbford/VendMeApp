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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
public class SubmitNewMachineActivity extends Activity implements View.OnClickListener {

    static final int REQUEST_PICTURE = 1; //Request code of 1 for image capture

    private EditText editTextBuildingName, editTextMachineType, editTextLocationDesc;
    private Button submissionButton, machineLocation, machinePicture, itemAdd;
    private ProgressDialog progressDialog;
    private ImageView imgView;
    private Bitmap imgBitmap;
    private ListView listview;
    private ArrayAdapter<String> arrAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_machine_submission);

        final ArrayList<String> contents = new ArrayList<String>();

        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(new customListAdapter(this,R.layout.list_view,contents));
//        arrAdapt = new ArrayAdapter<String>(this,R.layout.list_view,contents);


        // Required EditText's
        editTextBuildingName = (EditText) findViewById(R.id.editTextBuildingName);
        editTextMachineType = (EditText) findViewById(R.id.editTextMachineType);
        editTextLocationDesc = (EditText) findViewById(R.id.LocationDesc);

        // Page Buttons
        machineLocation = (Button) findViewById(R.id.MachineLocate);
        machinePicture = (Button) findViewById(R.id.MachinePicture);
        submissionButton = (Button) findViewById(R.id.Submission);
        itemAdd = (Button) findViewById(R.id.itemAdd);

        // TextView only used for debugging
        //displayResults = (TextView) findViewById(R.id.DisplayResults);

        progressDialog = new ProgressDialog(this);

        imgView = (ImageView) findViewById(R.id.machinePicView);
        submissionButton.setOnClickListener(this);
        machinePicture.setOnClickListener(this);
        machineLocation.setOnClickListener(this);

        itemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View newRow = li.inflate(R.layout.list_view, null);
                //RelativeLayout rl = (RelativeLayout) findViewById(R.id.list_view);
                //lv.addView(newRow);
                arrAdapt.notifyDataSetChanged();
            }
        });
        // textViewLogin.setOnClickListener(this);
    }

    /**
     * Custom list adapter to dynamically add edit texts to list.
     * - Created by bjbford on 3/19/18
     */
    private class customListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> contents;

        private customListAdapter(Context context, int resource, List<String> objects){
            super(context, resource, objects);
            contents = objects;
            layout = resource;
        }

        // TODO: need to figure out how to addView, can't find any documentation.
    }

    public class ViewHolder{
        EditText itemName;
        EditText itemPrice;
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