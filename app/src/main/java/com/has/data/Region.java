package com.has.data;

/**
 * Created by YoungWon on 2015-12-01.
 */
public class Region {
    String name;
    String id;
    Sensor[] sensor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Sensor[] getSensor() {
        return sensor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSensor(Sensor[] sensor) {
        this.sensor = sensor;
    }
}
