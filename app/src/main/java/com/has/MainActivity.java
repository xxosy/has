package com.has;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.has.auto.AutoFragment;
import com.has.data.ClientSocket;
import com.has.humidity.HumidityFragment;
import com.has.sprinkler.SprinklerFragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

@EActivity
public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener,
        HumidityFragment.OnFragmentInteractionListener,
        SprinklerFragment.OnFragmentInteractionListener,
        AutoFragment.OnFragmentInteractionListener{
    MainPresenter presenter;
    private int thisFragment = R.id.humidity_selector;
    private int container = R.id.container;
    @ViewById(R.id.humidity_selector)
    FrameLayout humiditySelector;
    @ViewById(R.id.sprinkler_selector)
    FrameLayout sprinklerSelector;
    @ViewById(R.id.auto_selector)
    FrameLayout autoSelector;
    @ViewById(R.id.setting_selector)
    FrameLayout settingSelector;
    @ViewById
    ImageView icon_humidity;
    @ViewById
    ImageView icon_sprinkler;
    @ViewById
    ImageView icon_auto;
    @ViewById
    ImageView icon_setting;
    @ViewById
    RelativeLayout background;
    @ViewById
    CircularProgressBar progressbar;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @AfterInject
    void afterInject(){
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    @AfterViews
    void initObject(){
        presenter = new MainPresenter(this);
//        presenter.enteredActivity();
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_left, R.animator.slide_out_right);
        mFragmentTransaction.add(container, HumidityFragment.newInstance("", ""));
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        humiditySelector.setOnClickListener(this);
        sprinklerSelector.setOnClickListener(this);
        autoSelector.setOnClickListener(this);
        settingSelector.setOnClickListener(this);

        progressbar.setVisibility(View.VISIBLE);

        setSelector();
    }
    @Override
    public void setContainer(Fragment fragment) {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
        mFragmentTransaction.replace(container, fragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
        setSelector();
    }
    public void setSelector(){
        humiditySelector.setBackgroundColor(Color.argb(0x66, 0xff, 0xff, 0xff));
        sprinklerSelector.setBackgroundColor(Color.argb(0x66,0xff,0xff,0xff));
        autoSelector.setBackgroundColor(Color.argb(0x66,0xff,0xff,0xff));
        settingSelector.setBackgroundColor(Color.argb(0x66, 0xff, 0xff, 0xff));
        icon_auto.setAlpha(0.5f);
        icon_humidity.setAlpha(0.5f);
        icon_setting.setAlpha(0.5f);
        icon_sprinkler.setAlpha(0.5f);
        if(thisFragment == R.id.humidity_selector){
            icon_humidity.setAlpha(1f);
            background.setBackgroundResource(R.drawable.main_background);
            humiditySelector.setBackgroundColor(Color.argb(0x66, 0x00, 0x00, 0x00));
        }else if(thisFragment == R.id.sprinkler_selector){
            background.setBackgroundResource(R.drawable.sprinkler_background);
            icon_sprinkler.setAlpha(1f);
            sprinklerSelector.setBackgroundColor(Color.argb(0x66,0x00,0x00,0x00));
        }else if(thisFragment == R.id.auto_selector){
            background.setBackgroundResource(R.drawable.sprinkler_background);
            icon_auto.setAlpha(1f);
            autoSelector.setBackgroundColor(Color.argb(0x66,0x00,0x00,0x00));
        }else if(thisFragment == R.id.setting_selector){
            icon_setting.setAlpha(1f);
            settingSelector.setBackgroundColor(Color.argb(0x66,0x00,0x00,0x00));
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        ClientSocket.getInstance().onDestroy();
        Log.i("onDestroy","Destroyed");
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        thisFragment = v.getId();
        switch(thisFragment) {
            case R.id.humidity_selector :{
                presenter.humiditySelectorTouched();
                break;
            }case R.id.sprinkler_selector:{
                presenter.sprinklerSelectorTouched();
                break;
            }case R.id.auto_selector:{
                presenter.autoSelectorTouched();
                break;
            }case R.id.setting_selector:{
//                presenter.settingSelectorTouched();
                break;
            }
        }
    }
}
