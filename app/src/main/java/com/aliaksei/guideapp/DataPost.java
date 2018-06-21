package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/20/2018.
 */

public class DataPost {

    private String message,user;

    DataPost(){}

    DataPost(String message,String user){
        this.message = message;
        this.user = user;
    }

    public String getMessage(){return this.message;}
    public String getUser(){return this.user;}


}
