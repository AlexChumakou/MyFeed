package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/17/2018.
 */

public class DataFeed {

    // - FEED VARIABLES -//
    private String title,creator;
    private int numUsers;


    // - CONSTRUCTOR - //
    DataFeed(){

    }

    DataFeed(String data,String creator){
        this.title = data;
        this.creator = creator;
    }

    // - GETTERS - //
    public String getTitle(){
        return this.title;
    }
    public String getCreator(){ return this.creator; }
}
