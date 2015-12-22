package com.has.auto;

import android.os.AsyncTask;
import android.util.Log;

import com.has.data.ClientSocket;
import com.has.humidity.HumidityModel;
import com.has.humidity.HumidityView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-03.
 */
public class AutoPresenter {
    AutoView view;
    AutoModel model;
    String sResult;

    int max_value;
    int min_value;
    AutoPresenter(AutoView view){
        this.view = view;
        this.model = new AutoModel();
    }
    public void fragmentEntered(){

    }
    public void maxSeekBarValueChanged(int value){
        this.max_value = value;
        view.setTxtMaxValue(value);
    }
    public void minSeekbarValueChanged(int value){
        this.min_value = value;
        view.setTxtMinValue(value);
    }
    public void setButtonClicked(){
        view.startProgress();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                sResult = ClientSocket.getInstance().getServerRequest("auto_value_setting="
                        + String.valueOf(max_value) + "=" + String.valueOf(min_value) + "=");
                if(sResult!=null)
                    Log.i("result", sResult);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                view.stopProgress();
            }
        }.execute();
    }
    public void startButtonClicked(){
        view.startProgress();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                sResult = ClientSocket.getInstance().getServerRequest("auto_start");
                if(sResult!=null)
                    Log.i("result", sResult);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                view.stopProgress();
            }
        }.execute();
    }
    public void stopButtonClicked(){
        view.startProgress();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                sResult = ClientSocket.getInstance().getServerRequest("auto_stop");
                if(sResult!=null)
                    Log.i("result", sResult);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                view.stopProgress();
            }
        }.execute();
    }
}
