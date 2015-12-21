package com.has.sprinkler;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-03.
 */
public interface SprinklerView {
    void setSpinner(ArrayList<String> spinnerDatas);
    void showDialog(int position,ArrayList<Entry> yVals);
    void setHexagonGroup(String current_region);
    void setPowerButtonState(Boolean btnState);
    void startProgress();
    void stopProgress();
}
