package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/17/2018.
 */

public class DataFeed {

    // - FEED VARIABLES -//
    private String title;


    // - CONSTRUCTOR - //
    DataFeed(){

    }

    DataFeed(String data){
        this.title = data;
    }

    // - GETTERS - //
    public String getTitle(){
        return this.title;
    }

}
