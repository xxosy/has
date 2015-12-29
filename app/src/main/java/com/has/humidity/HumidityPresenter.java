package com.has.humidity;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.has.data.ClientSocket;
import com.has.data.HumidityData;
import com.has.data.Region;
import com.has.data.Regions;

import org.androidannotations.api.BackgroundExecutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-01.
 */
public class HumidityPresenter {
    public static int STATE_STOP = 0;
    public static int STATE_START = 1;
    int state;
    HumidityView view;
    HumidityModel model;
    ArrayList<Entry> yVals;

    int position;
    String sResult;

    HumidityData mHumidityData;
    String sensor_name;
    String currentRegion;
    float sensor_value[];
    int sensor_value_size[];
    int sensor_value_count[];
    HumidityPresenter(HumidityView view){
        this.view = view;
        this.model = new HumidityModel();
        mHumidityData = new HumidityData();
        sensor_value = new float[10];
        sensor_value_count = new int[10];
        sensor_value_size = new int[10];
    }
    public void fragmentEntered(){
        view.startProgress();
        new AsyncTask<Integer,Integer,Integer>(){
            ArrayList<String> spinnerDatas;
            @Override
            protected Integer doInBackground(Integer... params) {
                ClientSocket.getInstance();
                model.setEnterData();
                return null;
            }
            @Override
            protected void onPostExecute(Integer result) {
                spinnerDatas= model.getRegionsData();
                view.setSpinner(spinnerDatas);
                view.setHexagonGroup(currentRegion);
                view.stopProgress();
            }
        }.execute();
        state = STATE_START;
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                while (true) {
                    if (state == STATE_STOP) {
                        break;
                    }
                    sResult = ClientSocket.getInstance().getServerRequest("recent_value");
                    if (sResult!=null) {
                        Log.i("TAG", sResult);
                        try {
                            JSONObject obj = new JSONObject(sResult);
                            JSONArray jsonArray = obj.getJSONArray("humid");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                sensor_value[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("humidity")) / 900 * 100;
                                int temp_size = Integer.valueOf(jsonArray.getJSONObject(i).getString("size"));
                                if(sensor_value_size[i]==temp_size) {
                                    sensor_value_count[i]++;
                                } else if(sensor_value_size[i]>temp_size) {
                                    sensor_value_count[i] = 0;
                                }
                                sensor_value_size[i] = temp_size;
                                if(sensor_value_count[i]>2){
                                    sensor_value[i] = 1000;
                                }
                                if(position == i)
                                    view.setHumidityDisplayValue(0,sensor_value[position]);
                            }
                            view.setHexagonGroupHumidity(sensor_value);
                            Thread.sleep(1000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }.execute();
    }
    public void hexagonItemTouched(final int position){
        yVals = new ArrayList<Entry>();
        this.position = position;
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
            @Override
            public void execute() {
                int posCnt = 0;
                sensor_name = "Loading";
                for (Region region : Regions.getInstance().getRegion()) {
                    if (region.getName().equals(currentRegion)) {
                        sensor_name = region.getSensor()[position].getName();
                    }
                }
                BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
                    @Override
                    public void execute() {
                        yVals = mHumidityData.getGraphData(position);
                        view.showDialog(sensor_name, yVals);
                    }
                });

            }
        });
    }
    public void btnOkClicked(){
        state = STATE_STOP;
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
