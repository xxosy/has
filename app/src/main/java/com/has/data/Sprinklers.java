package com.has.data;

import java.util.ArrayList;

/**
 * Created by YoungWon on 2015-12-11.
 */
public class Sprinklers {
    public static Sprinklers instance;
    ArrayList<Sprinkler> sprinklers;
    Sprinklers(){
        sprinklers = new ArrayList<>();
    }
    public static Sprinklers getInstance(){
        if(instance == null){
            instance = new Sprinklers();
        }
        return instance;
    }
    public void addItem(Sprinkler sprinkler){
        sprinklers.add(sprinkler);
    }
    public ArrayList<Sprinkler> getSprinklers(){
        return sprinklers;
    }
    public void initSprinklers(){
        sprinklers = new ArrayList<>();
    }
}
