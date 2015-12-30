package com.has.sprinkler;

import android.util.Log;

import com.has.data.ClientSocket;
import com.has.data.Regions;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-03.
 */
public class SprinklerModel {
    String sResult;

    SprinklerModel(){

    }
    public ArrayList<String> getRegionsData(){
        ArrayList<String> array =Regions.getInstance().getRegionsArray();
        return array;
    }
    public boolean setEnterData(){
        Regions.getInstance().update();
        sResult = ClientSocket.getInstance().getServerRequest("sprinkler");
        Log.i("TAG", sResult);
        if(sResult.contains("controller is not")){
            return false;
        }
        return true;

    }
}
