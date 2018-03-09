package com.manutastic.weatheriffic;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFunctions extends Application {
    public static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=imperial";
    public static Context mContext;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static String degreeToCardinal(int deg) {
        String[] compass = {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};
        int index = (int)Math.round((deg/22.5) + .5); // Change in direction every 22.5 degrees, +.5 for rounding
        return compass[index];
    }

    public interface AsyncResponse {
        void processFinish(String output01, String output02, String output03, String output04, String output05,
                           String output06, String output07, String output08, String output09, String output10,
                           String output11, String output12);
    }

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject>{

        public AsyncResponse delegate = null;
        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject weather = null;
            try {
                weather = getJSONData(params[0], params[1]);
            } catch (Exception e){
                Log.d("Error", "Cannot process JSON", e);
            }

            return weather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    SimpleDateFormat sdf =  new SimpleDateFormat("h:mm a" );

                    String current_temp = String.format("%d", main.getInt("temp")) + "°";
                    String location = json.getString("name");
                    String high_temp = String.format("%d", main.getInt("temp_max")) + "°";
                    String low_temp = String.format("%d", main.getInt("temp_min")) + "°";
                    String condition = TextUtilities.toTitleCase(details.getString("description"));
                    String sunrise = sdf.format(new Date(json.getJSONObject("sys").getLong("sunrise")*1000));
                    String sunset = sdf.format(new Date(json.getJSONObject("sys").getLong("sunset")*1000));
                    String humidity = String.format("%d", main.getInt("humidity")) + "%";
                    String pressure = String.format("%d", main.getInt("pressure")) + " hPa";
                    String visibility = String.format("%d", json.getInt("visibility")) + " mi";
                    String wind = String.format(json.getJSONObject("wind").getString("speed")) + " mph";
                    String wind_dir = degreeToCardinal(json.getJSONObject("wind").getInt("deg"));
                    delegate.processFinish(current_temp, location, high_temp, low_temp, condition,
                            sunrise, sunset, humidity, pressure, visibility, wind, wind_dir);
                }
            } catch (JSONException e) {
                Log.e("Error", "Cannot process JSON results", e);
            }
        }
    }

    public static JSONObject getJSONData(String lat, String lon) {
        try {
            URL url = new URL(String.format(API_URL, lat, lon));
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.addRequestProperty("x-api-key", WeatherFunctions.getContext().getResources().getString(R.string.api_key));

            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer data = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = bf.readLine()) != null) {
                data.append(tmp).append("\n");
            }
            bf.close();

            JSONObject json = new JSONObject(data.toString());

            if (json.getInt("cod") != 200) {
                System.out.print("JSON Error");
                return null;
            }

            return json;
        } catch (Exception e) {
            Log.d("Error", "Cannot get JSON Data", e);
            return null;
        }
    }
}
