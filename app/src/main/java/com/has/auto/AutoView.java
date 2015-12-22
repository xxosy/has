package com.has.auto;

/**
 * Created by YoungWon on 2015-12-03.
 */
public interface AutoView {
    void setTxtMaxValue(int value);
    void setTxtMinValue(int value);
    void startProgress();
    void stopProgress();
    void setSeekbarMax(int max);
    void setSeekbarMin(int min);
}
