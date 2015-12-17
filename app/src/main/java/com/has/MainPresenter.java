package com.has;

import android.app.Fragment;
import android.util.Log;

import com.has.data.ClientSocket;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by YoungWon on 2015-11-30.
 */
public class MainPresenter {
    MainView view;
    MainModel model;

    MainPresenter(MainView view){
        this.view = view;
        model = new MainModel();
    }
    public void enteredActivity(){
        Fragment fragment = model.getFragment(MainModel.ID_HUMIDITY);
        view.setContainer(fragment);
    }
    public void humiditySelectorTouched(){
        Fragment fragment = model.getFragment(MainModel.ID_HUMIDITY);
        view.setContainer(fragment);
    }
    public void sprinklerSelectorTouched(){
        Fragment fragment = model.getFragment(MainModel.ID_SPRINKLER);
        view.setContainer(fragment);
    }
    public void autoSelectorTouched(){
        Fragment fragment = model.getFragment(MainModel.ID_AUTO);
        view.setContainer(fragment);
    }
    public void settingSelectorTouched(){
        Fragment fragment = model.getFragment(MainModel.ID_SETTING);
        view.setContainer(fragment);
    }
}
