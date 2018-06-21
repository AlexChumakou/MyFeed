package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/17/2018.
 */

public class DataFeed {

    // - FEED VARIABLES -//
    private String id,title,creator;


    // - CONSTRUCTOR - //
    DataFeed(){

    }

    DataFeed(String id,String title,String creator){
        this.id = id;
        this.title = title;
        this.creator = creator;
    }

    // - GETTERS - //
    public String getId(){ return this.id; }
    public String getTitle(){
        return this.title;
    }
    public String getCreator(){ return this.creator; }

}
