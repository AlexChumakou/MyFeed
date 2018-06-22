package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/20/2018.
 */

public class DataPost {

    private String id,message,user;

    DataPost(){}

    DataPost(String id,String message,String user){
        this.id = id;
        this.message = message;
        this.user = user;
    }

    public String getMessage(){return this.message;}
    public String getUser(){return this.user;}
    public String getId(){return this.id;}


}
