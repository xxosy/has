package com.has.humidity;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.has.data.ClientSocket;
import com.has.data.Regions;
import com.has.data.Sensors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
