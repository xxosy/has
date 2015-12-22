package com.has.humidity;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.has.data.ClientSocket;
import com.has.data.Region;
import com.has.data.Regions;
import com.has.data.Sensors;

import org.androidannotations.api.BackgroundExecutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-01.
 */
public class HumidityModel {
    String sResult;
    HumidityModel() {
    }

    public ArrayList<String> getRegionsData() {
        ArrayList<String> array = Regions.getInstance().getRegionsArray();
        return array;
    }
    public ArrayList<String> getSensorsData() {
        ArrayList<String> array = Sensors.getInstance().getSensorsArray();
        return array;
    }

    public String getResult() {
        return sResult;
    }

    public void setEnterData() {
        sResult = ClientSocket.getInstance().getServerRequest("data");
        if(sResult!=null) {
            Log.i("TAG", sResult);
            Regions.getInstance().initRegions();
            Sensors.getInstance().initSensors();
            try {
                JSONObject obj = new JSONObject(sResult);
                JSONArray jsonArray = obj.getJSONArray("region");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String region_name = jsonArray.getJSONObject(i).getString("name");
                    Regions.getInstance().setRegionsArray(region_name);
                    JSONArray jsonSensorArray = jsonArray.getJSONObject(i).getJSONArray("sensor");
                    for (int j = 0; j < jsonSensorArray.length(); j++) {
                        String sensor_name = jsonSensorArray.getJSONObject(j).getString("name");
                        Sensors.getInstance().addSensorsArray(region_name, sensor_name);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
