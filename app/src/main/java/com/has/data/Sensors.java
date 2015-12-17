package com.has.data;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-07.
 */
public class Sensors {
    ArrayList<Sensor> sensors;
    public static Sensors instance;
    public Sensors(){
        Sensor sensor = new Sensor();
        sensor.setName("region1","Grassplot");
        sensors = new ArrayList<>();
        sensors.add(sensor);
    }
    public static Sensors getInstance(){
        if(instance == null){
            instance = new Sensors();
        }
        return instance;
    }
    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<Sensor> sensors) {
        this.sensors = sensors;
    }

    public ArrayList<String> getSensorsArray(){
        ArrayList<String> result = new ArrayList<String>();
        for(Sensor sensor : getSensors()){
            result.add(sensor.getName());
        }
        return result;
    }
    public void initSensors(){
        sensors = new ArrayList<>();
    }
    public void addSensorsArray(String region_name,String sensor_name){
        Sensor sensor = new Sensor();
        sensor.setName(region_name,sensor_name);
        sensors.add(sensor);
    }
}
