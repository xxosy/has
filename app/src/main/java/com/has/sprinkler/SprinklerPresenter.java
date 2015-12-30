package com.has.sprinkler;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.has.data.ClientSocket;
import com.has.data.Region;
import com.has.data.Regions;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-03.
 */
public class SprinklerPresenter {
    SprinklerView view;
    SprinklerModel model;
    ArrayList<Entry> yVals;
    String sResult;
    boolean controllerConnectState = false;
    boolean powerBtnState[];

    String currentRegion;
    SprinklerPresenter(SprinklerView view){
        this.view = view;
        model = new SprinklerModel();

    }
    public void fragmentEntered(){
        view.startProgress();
        new AsyncTask<Integer,Integer,Integer>(){
            ArrayList<String> spinnerDatas;
            @Override
            protected Integer doInBackground(Integer... params) {
                controllerConnectState = model.setEnterData();
                return null;
            }
            @Override
            protected void onPostExecute(Integer result) {
                spinnerDatas = model.getRegionsData();
                view.setSpinner(spinnerDatas);
                int size = 0;
                for(Region region:Regions.getInstance().getRegion()) {
                    Log.i("region.getName()",region.getName());
                    Log.i("currentRegion",currentRegion);
                    if (region.getName().equals(currentRegion))
                        size = region.getSprinkler().length;
                }
                Log.i("zzzzzzz",String.valueOf(size));
                powerBtnState = new boolean[size];
                for (int i = 0; i < powerBtnState.length; i++)
                    powerBtnState[i] = true;
                view.setHexagonGroup(currentRegion);
                view.stopProgress();
                view.setConnectState(controllerConnectState);
            }
        }.execute();
    }
    public void hexagonItemTouched(final int position){
        view.setPowerButtonState(powerBtnState[position]);
        view.showDialog(position, yVals);
    }
    public void btnPowerClicked(final int position) {
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                if (powerBtnState[position]) {
                    sResult = ClientSocket.getInstance().getServerRequest("power_off" + String.valueOf(position));
                } else {
                    sResult = ClientSocket.getInstance().getServerRequest("power_on" + String.valueOf(position));
                }
                powerBtnState[position] = !powerBtnState[position];
                if(sResult!=null)
                    Log.i("TAG", sResult);

                return null;
            }
            @Override
            protected void onPostExecute(Integer result) {
                view.setPowerButtonState(powerBtnState[position]);
            }
        }.execute();
    }
    public void spinnerChanged(){
        view.setHexagonGroup(currentRegion);
    }

    public String getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentRegion(String currentRegion) {
        this.currentRegion = currentRegion;
    }
}
