package com.has.data;

import java.util.HashMap;

/**
 * Created by YoungWon on 2015-12-07.
 */
public class Sensor {
    String name;
    String region_id;
    String id;
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

    public String getId() {
        return id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}
