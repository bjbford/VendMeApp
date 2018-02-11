package com.example.jared.vendme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.example.jared.vendme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //This method is triggered by tapping the Send Button.
    public void sendMessage(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
