package com.has.sprinkler;

import android.util.Log;

import com.has.data.ClientSocket;
import com.has.data.Regions;
import com.has.data.Sprinkler;
import com.has.data.Sprinklers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-03.
 */
public class SprinklerModel {
    URL url;
    HttpURLConnection conn;
    String sResult;

    SprinklerModel(){

    }
    public ArrayList<String> getRegionsData(){
        ArrayList<String> array =Regions.getInstance().getRegionsArray();
        return array;
    }
    public boolean setEnterData(){
        sResult = ClientSocket.getInstance().getServerRequest("sprinkler");
        Log.i("TAG", sResult);
        if(sResult.contains("controller is not")){
            return false;
        }else {
            Sprinklers.getInstance().initSprinklers();
            try {
                JSONObject obj = new JSONObject(sResult);
                JSONArray jsonArray = obj.getJSONArray("sprinkler");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String sprinkler_name = jsonArray.getJSONObject(i).getString("name");
                    Sprinkler sprinkler = new Sprinkler();
                    sprinkler.setName(sprinkler_name);
                    Sprinklers.getInstance().addItem(sprinkler);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
