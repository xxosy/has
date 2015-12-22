package com.has;

import android.widget.FrameLayout;

import com.rey.material.widget.ProgressView;

/**
 * Created by YoungWon on 2015-12-21.
 */
public class ProgressHandler {
    private FrameLayout progressLayout;
    private ProgressView progressView;

    ProgressHandler(FrameLayout progressLayout, ProgressView progressView){
        this.progressLayout = progressLayout;
        this.progressView = progressView;
    }
}
