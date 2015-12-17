package com.has.sprinkler;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.has.data.ClientSocket;
import com.has.data.Sprinklers;

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
 * Created by YoungWon on 2015-12-03.
 */
public class SprinklerPresenter {
    SprinklerView view;
    SprinklerModel model;
    ArrayList<Entry> yVals;
    String sResult;

    boolean powerBtnState[];

    String currentRegion;
    SprinklerPresenter(SprinklerView view){
        this.view = view;
        model = new SprinklerModel();

    }
    public void fragmentEntered(){
        new AsyncTask<Integer,Integer,Integer>(){
            ArrayList<String> spinnerDatas;
            @Override
            protected Integer doInBackground(Integer... params) {
                model.setEnterData();
                return null;
            }
            @Override
            protected void onPostExecute(Integer result) {
                powerBtnState = new boolean[Sprinklers.getInstance().getSprinklers().size()];
                for(int i = 0;i<powerBtnState.length;i++)
                    powerBtnState[i] = true;
                spinnerDatas= model.getRegionsData();
                view.setSpinner(spinnerDatas);
                view.setHexagonGroup(currentRegion);
            }
        }.execute();
    }
    public void hexagonItemTouched(final int position){
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
