package com.manutastic.weatheriffic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView current_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_temp = (TextView)findViewById(R.id.current_temp);
        current_temp.setText("Loading...");

        WeatherFunctions.placeIdTask asyncTask = new WeatherFunctions.placeIdTask(new WeatherFunctions.AsyncResponse(){
            public void processFinish(String curr_temp) {
                current_temp.setText(curr_temp);
            }
        });
        asyncTask.execute("40.7306", "-73.9867"); // Latitude N, Longitude E;
    }
}
