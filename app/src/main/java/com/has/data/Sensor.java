package com.has.data;

import java.util.HashMap;

/**
 * Created by YoungWon on 2015-12-07.
 */
public class Sensor {
    String name;
    String region_name;
    public void setName(String region_name,String name) {
        this.name = name;
        this.region_name = region_name;
    }

    public String getName() {
        return name;
    }
    public String getRegion_name() {
        return region_name;
    }
}
