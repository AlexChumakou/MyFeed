package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/17/2018.
 */

public class DataComm {

    private String id,message,user,date;
    private int cheers;

    DataComm(){}

    DataComm(String id,String message,String user,String date,int cheers){
        this.id = id;
        this.message = message;
        this.user = user;
        this.date = date;
        this.cheers = cheers;
    }

    public String getMessage(){return this.message;}
    public String getUser(){return this.user;}
    public String getId(){return this.id;}
    public String getDate(){return this.date;}
    public int getCheers(){return this.cheers;}
    public void setCheers(int cheers){
        this.cheers = cheers;
    }


}
