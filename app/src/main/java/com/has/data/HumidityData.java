package com.has.data;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.androidannotations.api.BackgroundExecutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-07.
 */
public class HumidityData{
    private float currentHumidity;
    private ArrayList<Entry> humidityGraphData;
    String sResult;
    ArrayList<Entry> yVals;

    public HumidityData() {

    }

    public ArrayList<Entry> getGraphData(final int index) {
        yVals = new ArrayList<Entry>();
        sResult = ClientSocket.getInstance().getServerRequest("Graph?sensor_name=sensor"
                + String.valueOf(index + 1));
        if(sResult!=null)
            Log.i("TAG", sResult);
        try {
            JSONObject obj = new JSONObject(sResult);
            JSONArray jsonArray = obj.getJSONArray("graph");
            int start = 0;
            int end = jsonArray.length();
            if (jsonArray.length() > 400) {
                start = jsonArray.length() - 400;
            }
            Log.i("start:", String.valueOf(start));
            Log.i("end:", String.valueOf(end));

            for (int i = start; i < end; i++) {
                String humidity = jsonArray.getJSONObject(i).getString("value");
                float val = Float.valueOf(humidity) * 100 / 900;
                yVals.add(new Entry(val, i - start));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return yVals;
    }
    public ArrayList<Entry> getHumidityGraphData() {
        return humidityGraphData;
    }

    public float getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(float currentHumidity) {
        this.currentHumidity = currentHumidity;
    }
    public void setHumidityGraphData(ArrayList<Entry> humidityGraphData) {
        this.humidityGraphData = humidityGraphData;
    }
}
