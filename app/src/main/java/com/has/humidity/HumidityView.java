package com.has.humidity;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-01.
 */
public interface HumidityView {
    void setSpinner(ArrayList<String> spinnerDatas);
    void showDialog(String position,ArrayList<Entry> yVals);
    void setHexagonGroup(String current_region);
    void setHexagonGroupHumidity(float[] sensor_value);
    void setHumidityDisplayValue(int i, float humidity);
    void startProgress();
    void stopProgress();
}
