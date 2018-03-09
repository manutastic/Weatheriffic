package com.manutastic.weatheriffic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView current_temp_field, location_field, high_temp_field, low_temp_field, condition_field,
            sunrise_field, sunset_field, humidity_field, pressure_field, visibility_field, wind_field,
            wind_dir_field;
    ImageView condition_icon_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        current_temp_field = (TextView)findViewById(R.id.current_temp);
        location_field = (TextView)findViewById(R.id.location);
        high_temp_field = (TextView)findViewById(R.id.high_temp);
        low_temp_field = (TextView)findViewById(R.id.low_temp);
        condition_field = (TextView)findViewById(R.id.condition);
        condition_icon_image = (ImageView)findViewById(R.id.condition_icon);
        sunrise_field = (TextView)findViewById(R.id.sunrise);
        sunset_field = (TextView)findViewById(R.id.sunset);
        humidity_field = (TextView)findViewById(R.id.humidity);
        pressure_field = (TextView)findViewById(R.id.pressure);
        visibility_field = (TextView)findViewById(R.id.visibility);
        wind_field = (TextView)findViewById(R.id.wind);
        wind_dir_field = (TextView)findViewById(R.id.wind_dir);

        WeatherFunctions.placeIdTask asyncTask = new WeatherFunctions.placeIdTask(new WeatherFunctions.AsyncResponse(){
            public void processFinish(String current_temp, String location, String high_temp, String low_temp, String condition,
                                      String sunrise, String sunset, String humidity, String pressure, String visibility, String wind,
                                      String wind_dir) {
                current_temp_field.setText(current_temp);
                location_field.setText(location);
                high_temp_field.setText(high_temp);
                low_temp_field.setText(low_temp);
                condition_field.setText(condition);
                sunrise_field.setText(sunrise);
                sunset_field.setText(sunset);
                humidity_field.setText(humidity);
                pressure_field.setText(pressure);
                visibility_field.setText(visibility);
                wind_field.setText(wind);
                wind_dir_field.setText(wind_dir);
            }
        });
        asyncTask.execute("40.7306", "-73.9867"); // Latitude N, Longitude E;
    }

}
