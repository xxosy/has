package com.has.data;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-01.
 */
public class Regions {
    ArrayList<Region> regions;
    public static Regions instance;

    public Regions(){
        Region region = new Region();
        region.setName("Grassplot");
        regions = new ArrayList<>();
        regions.add(region);
    }

    public static Regions getInstance(){
        if(instance == null){
            instance = new Regions();
        }
        return instance;
    }
    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public ArrayList<String> getRegionsArray(){
        ArrayList<String> result = new ArrayList<String>();
        for(Region region : getRegions()){
            result.add(region.getName());
        }
        return result;
    }
    public void initRegions(){
        regions = new ArrayList<>();
    }
    public void setRegionsArray(String strRegion){

        Region region = new Region();
        region.setName(strRegion);
        regions.add(region);
    }
}
