package com.has.humidity;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.has.data.ClientSocket;
import com.has.data.Regions;

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
    public String getResult() {
        return sResult;
    }

    public void setEnterData() {
        sResult = ClientSocket.getInstance().getServerRequest("data");
        if(sResult!=null) {
            Log.i("TAG", sResult);
            ObjectMapper mapper = new ObjectMapper();
            try {
                Regions.getInstance().setInstance(mapper.readValue(sResult.getBytes(),Regions.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
