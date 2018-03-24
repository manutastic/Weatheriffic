package com.manutastic.weatheriffic;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.manutastic.weatheriffic.WeatherFunctions.mContext;

public class MainActivity extends AppCompatActivity {

    String units = "imperial";

    TextView current_temp_field, location_field, high_temp_field, low_temp_field, condition_field,
            sunrise_field, sunset_field, humidity_field, pressure_field, wind_field,
            wind_dir_field;
    ImageView condition_icon_image;

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appBar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        updateWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWeather();
    }

    public void updateWeather() {
        current_temp_field = (TextView) findViewById(R.id.current_temp);
        location_field = (TextView) findViewById(R.id.location);
        high_temp_field = (TextView) findViewById(R.id.high_temp);
        low_temp_field = (TextView) findViewById(R.id.low_temp);
        condition_field = (TextView) findViewById(R.id.condition);
        condition_icon_image = (ImageView) findViewById(R.id.condition_icon);
        sunrise_field = (TextView) findViewById(R.id.sunrise);
        sunset_field = (TextView) findViewById(R.id.sunset);
        humidity_field = (TextView) findViewById(R.id.humidity);
        pressure_field = (TextView) findViewById(R.id.pressure);
        wind_field = (TextView) findViewById(R.id.wind);
        wind_dir_field = (TextView) findViewById(R.id.wind_dir);
        condition_icon_image = (ImageView) findViewById(R.id.condition_icon);

        final WeatherFunctions.placeIdTask asyncTask = new WeatherFunctions.placeIdTask(new WeatherFunctions.AsyncResponse() {
            public void processFinish(String current_temp, String location, String high_temp, String low_temp, String condition,
                                      String sunrise, String sunset, String humidity, String pressure, String wind,
                                      String wind_dir, int weather_icon) {
                current_temp_field.setText(current_temp);
                location_field.setText(location);
                high_temp_field.setText(high_temp);
                low_temp_field.setText(low_temp);
                condition_field.setText(condition);
                sunrise_field.setText(sunrise);
                sunset_field.setText(sunset);
                humidity_field.setText(humidity);
                pressure_field.setText(pressure);
                wind_field.setText(wind);
                wind_dir_field.setText(wind_dir);
                condition_icon_image.setImageResource(weather_icon);
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                // Request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);

                updateWeather();

        } else {
            // Permission has been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                asyncTask.execute("" + location.getLatitude(), "" + location.getLongitude(), units); // Latitude N, Longitude E;
                            } else {
                                Toast.makeText(mContext, "Unable to get location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_units:
                // Change units
                if (units == "imperial") {
                    units = "metric";
                    item.setTitle(getResources().getString(R.string.c));
                    TextView wind_units = (TextView) findViewById(R.id.wind_units);
                    wind_units.setText(getResources().getString(R.string.wind_units_metric));
                    updateWeather();
                } else if (units == "metric") {
                    units = "imperial";
                    item.setTitle(getResources().getString(R.string.f));
                    TextView wind_units = (TextView) findViewById(R.id.wind_units);
                    wind_units.setText(getResources().getString(R.string.wind_units_imperial));
                    updateWeather();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void tempClick(View v) {
        Toast.makeText(mContext, "Updated!", Toast.LENGTH_SHORT).show();
        updateWeather();
    }

}
