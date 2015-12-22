package com.has.sprinkler;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.has.R;
import com.has.data.Sensor;
import com.has.data.Sensors;
import com.has.data.Sprinkler;
import com.has.data.Sprinklers;
import com.has.util.HexagonGroup;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SprinklerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SprinklerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SprinklerFragment extends Fragment implements SprinklerView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner spinner;
    private SprinklerPresenter presenter;
    private HexagonGroup humidityHexagon;
    Dialog dialog;
    View innerView;
    LayoutInflater mInflater;
    TextView txtDialogName;
    FButton dialogBtnOk;
    ImageView dialogbtnPower;
    ImageView connectState;
    int selectPposition;
    private FrameLayout progressLayout;
    private ProgressView progressView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SprinklerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SprinklerFragment newInstance(String param1, String param2) {
        SprinklerFragment fragment = new SprinklerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SprinklerFragment() {
        presenter = new SprinklerPresenter(this);
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mInflater = getActivity().getLayoutInflater();
    }
    @Override
    public void startProgress(){
        progressLayout.setVisibility(View.VISIBLE);
        progressView.start();
    }
    @Override
    public void stopProgress(){
        progressLayout.setVisibility(View.INVISIBLE);
        progressView.stop();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sprinkler, container, false);
        spinner = (Spinner)root.findViewById(R.id.sprinkler_spinner);
        humidityHexagon = (HexagonGroup)root.findViewById(R.id.hexagon_sprinkler);
        progressLayout = (FrameLayout)root.findViewById(R.id.progressback) ;
        progressView = (ProgressView) root.findViewById(R.id.progressview) ;
        connectState = (ImageView) root.findViewById(R.id.connect_state);
        stopProgress();
        humidityHexagon.setToggle(1);
        humidityHexagon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float x = event.getX();
                    float y = event.getY();
                    selectPposition = humidityHexagon.getTouchItem(x, y);
                    if(selectPposition<11) {
                        dialog.show();
                        presenter.hexagonItemTouched(selectPposition);
                    }
                }
                return false;
            }
        });
        presenter.fragmentEntered();
        initDialog();
        return root;
    }
    public void setConnectState(boolean bool){
        if(bool){
            connectState.setBackgroundResource(R.drawable.connected);
        }else{
            connectState.setBackgroundResource(R.drawable.notconnect);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void initDialog(){
        innerView = mInflater.inflate(R.layout.dialog_sprinkler,null);
        txtDialogName = (TextView)innerView.findViewById(R.id.sprinkler_name);
        dialog = new Dialog(getActivity(),R.style.FullHeightDialog);
        dialogBtnOk =(FButton)innerView.findViewById(R.id.dialog_sprinkler_btn_ok);
        dialogbtnPower = (ImageView)innerView.findViewById(R.id.btnpower);
        dialogbtnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnPowerClicked(selectPposition);
            }
        });
        dialog.setContentView(innerView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }
    private void dismissDialog() {
        if(dialog != null /*&& mDialog.isShowing()*/) {
            dialog.dismiss();
        }
    }
    @Override
    public void setPowerButtonState(Boolean btnState){
        if(btnState)
            dialogbtnPower.setImageResource(R.drawable.power);
        else
            dialogbtnPower.setImageResource(R.drawable.power_off);
    }
    public void setSpinner(final ArrayList<String> spinnerDatas){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item,spinnerDatas);
        spinner.setAdapter(adapter);
        presenter.setCurrentRegion((String) spinner.getSelectedItem());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setCurrentRegion((String) spinner.getSelectedItem());
                presenter.spinnerChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void showDialog(int position, ArrayList<Entry> yVals) {
        txtDialogName.setText("Sprinkler" + String.valueOf(position));
    }
    public void setHexagonGroup(String current_region){
        int size = 0;
        size = Sprinklers.getInstance().getSprinklers().size();
        humidityHexagon.setCurrentRegion(size);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
