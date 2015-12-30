package com.has.data;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-01.
 */
public class Regions {
    public Region[] region;
    public static Regions instance;

    public static Regions getInstance(){
        if(instance == null){
            instance = new Regions();
        }
        return instance;
    }
    public Region[] getRegion() {
        return region;
    }

    public void setRegion(Region[] region) {
        this.region = region;
    }
    public void setInstance(Regions regions){
        instance = regions;
    }
    public ArrayList<String> getRegionsArray(){
        ArrayList<String> result = new ArrayList<String>();
        for(Region region : this.region){
            result.add(region.getName());
        }
        return result;
    }
    public static void update(){
        String sResult = ClientSocket.getInstance().getServerRequest("data");
        if (sResult != null) {
            Log.i("TAG", sResult);
            ObjectMapper mapper = new ObjectMapper();
            try {
                instance = mapper.readValue(sResult.getBytes(), Regions.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
