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
    HumidityModel() {
    }

    public ArrayList<String> getRegionsData() {
         ArrayList<String> array = Regions.getInstance().getRegionsArray();
        return array;
    }

    public void setEnterData() {
        Regions.getInstance().update();
    }
}
