package com.has;

import android.app.Fragment;

import com.has.auto.AutoFragment;
import com.has.humidity.HumidityFragment;
import com.has.sprinkler.SprinklerFragment;

/**
 * Created by YoungWon on 2015-11-30.
 */
public class MainModel {
    public static String ID_HUMIDITY = "humidity";
    public static String ID_SPRINKLER = "sprinkler";
    public static String ID_AUTO = "auto";
    public static String ID_SETTING = "setting";
    HumidityFragment humidityFragment;
    SprinklerFragment sprinklerFragment;
    AutoFragment autoFragment;
    MainModel(){
        humidityFragment = HumidityFragment.newInstance("","");
        sprinklerFragment = SprinklerFragment.newInstance("","");
        autoFragment = AutoFragment.newInstance("","");
    }
    public Fragment getFragment(String id){
        if(id.equals(ID_HUMIDITY)){
            return humidityFragment;
        }else if(id.equals(ID_SPRINKLER)){
            return sprinklerFragment;
        }else if(id.equals(ID_AUTO)){
            return autoFragment;
        }else if(id.equals(ID_SETTING)){

        }
        return null;
    }
}
