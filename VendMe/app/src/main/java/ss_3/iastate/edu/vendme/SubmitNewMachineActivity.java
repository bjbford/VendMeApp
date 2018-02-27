package ss_3.iastate.edu.vendme;



/**
 * This activity controls the entire New Machine Submission Page, which allows the user to submit
 * a new machine into our database, upon entering the required information.
 * Created by bjbford on 2/10/18.
 *   - Integrated into main application by jdanner.
 */



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;





public class SubmitNewMachineActivity extends Activity implements View.OnClickListener {

    static final int REQUEST_PICTURE = 1; //Request code of 1 for image capture

    private EditText editTextBuilding, editTextBuildingType, editTextDescription;
    private Button buttonRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_machine_submission);




            editTextBuilding = (EditText) findViewById(R.id.editTextBuilding);
            editTextBuildingType = (EditText) findViewById(R.id.editTextBuildingType);
            editTextDescription = (EditText) findViewById(R.id.editTextDescription);



            buttonRegister = (Button) findViewById(R.id.buttonRegister);

            progressDialog = new ProgressDialog(this);

            buttonRegister.setOnClickListener(this);
            // textViewLogin.setOnClickListener(this);
        }

        private void newMachine() {
            final String building = editTextBuilding.getText().toString().trim();
            final String buildingType = editTextBuildingType.getText().toString().trim();
            final String description = editTextDescription.getText().toString().trim();

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
                    params.put("type", buildingType);
                    params.put("desc", description);
                    return params;
                }
            };


            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


        }
        // Required EditText's





        // When the user clicks button, prompt permission to use camera and open camera app



        // When the user clicks button, prompt permission for location and open map to plot marker



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

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            newMachine();
        }
    }
}
