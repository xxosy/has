package com.has.auto;

import android.app.Activity;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.has.R;

import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AutoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutoFragment extends Fragment implements AutoView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    AutoPresenter presenter;
    private TextView txtMaxValue;
    private TextView txtMinValue;
    private TextView txtTime;
    private SeekBar seekbarMax;
    private SeekBar seekbarMin;
    private FrameLayout btnTimePicker;
    private String operateTime;
    private FButton btn_start;
    private FButton btn_stop;
    private FButton btn_set;
    private FrameLayout progressLayout;
    private ProgressBarCircularIndeterminate progressView;
    TimePickerDialog.OnTimeSetListener t;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AutoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AutoFragment newInstance(String param1, String param2) {
        AutoFragment fragment = new AutoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AutoFragment() {
        // Required empty public constructor
        presenter = new AutoPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void startProgress(){
        progressLayout.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.VISIBLE);
    }
    @Override
    public void stopProgress(){
        progressLayout.setVisibility(View.INVISIBLE);
        progressView.setVisibility(View.INVISIBLE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_auto, container, false);
        txtMaxValue = (TextView)root.findViewById(R.id.txt_max_value);
        txtMinValue = (TextView)root.findViewById(R.id.txt_min_value);
        txtTime =(TextView)root.findViewById(R.id.txt_operate_time);
        seekbarMax = (SeekBar) root.findViewById(R.id.seekbar_max);
        seekbarMin = (SeekBar) root.findViewById(R.id.seekbar_min);
        btnTimePicker =(FrameLayout) root.findViewById(R.id.btn_time_picker);
        btn_set = (FButton)root.findViewById(R.id.auto_btn_set);
        btn_start = (FButton)root.findViewById(R.id.auto_btn_start);
        btn_stop = (FButton)root.findViewById(R.id.auto_btn_stop);
        progressLayout = (FrameLayout)root.findViewById(R.id.progressback) ;
        progressView = (ProgressBarCircularIndeterminate) root.findViewById(R.id.progressBar) ;
        seekbarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.maxSeekBarValueChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarMin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.minSeekbarValueChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), t, 0, 0,
                        true).show();
            }
        });
        t = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                operateTime =String.valueOf(hourOfDay)+String.valueOf(minute);
                txtTime.setText(hourOfDay + " : " + minute);
            }
        };
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setButtonClicked();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startButtonClicked();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.stopButtonClicked();
            }
        });
        presenter.fragmentEntered();
        return root;
    }
    public void setSeekbarMax(int max){
        seekbarMax.setProgress(max);
    }
    public void setSeekbarMin(int min){
        seekbarMin.setProgress(min);
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
    public void setTxtMaxValue(int value){
        String str = Integer.toString(value)+"%";
        txtMaxValue.setText(str);
    }
    public void setTxtMinValue(int value){
        String str = Integer.toString(value)+"%";
        txtMinValue.setText(str);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
