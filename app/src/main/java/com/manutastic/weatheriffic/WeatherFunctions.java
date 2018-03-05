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

    public interface AsyncResponse {
        void processFinish(String output);
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
                    DateFormat format = DateFormat.getDateTimeInstance();

                    String temp = String.format("%.2f", main.getDouble("temp")) + "°";
                    delegate.processFinish(temp);
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
