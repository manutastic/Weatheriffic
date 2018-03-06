package com.manutastic.weatheriffic;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView current_temp, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        current_temp = (TextView)findViewById(R.id.current_temp);
        location = (TextView)findViewById(R.id.location);

        current_temp.setText("Loading...");
        location.setText("City, State");

        WeatherFunctions.placeIdTask asyncTask = new WeatherFunctions.placeIdTask(new WeatherFunctions.AsyncResponse(){
            public void processFinish(String curr_temp) {
                current_temp.setText(curr_temp);
            }
        });
        asyncTask.execute("40.7306", "-73.9867"); // Latitude N, Longitude E;
    }
}
