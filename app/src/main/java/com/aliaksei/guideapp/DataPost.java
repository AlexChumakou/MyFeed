package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/20/2018.
 */

public class DataPost {

    private String id,message,user,date,hasupvoted;
    private int cheers,replies,hidenval;

    DataPost(){}

    DataPost(String id,String message,String user,String date,int cheers,int replies,String hasupvoted,int hidenval){
        this.id = id;
        this.message = message;
        this.user = user;
        this.date = date;
        this.cheers = cheers;
        this.replies = replies;
        this.hasupvoted = hasupvoted;
        this.hidenval = hidenval;
    }

    public String getMessage(){return this.message;}
    public String getUser(){return this.user;}
    public String getId(){return this.id;}
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}
    public int getCheers(){return this.cheers;}
    public void setCheers(int cheers){this.cheers = cheers;}
    public int getReplies(){return this.replies;}
    public void setReplies(int replies){this.replies = replies;}
    public String getHasupvoted(){return this.hasupvoted;}
    public void setHasupvoted(String hasupvoted){this.hasupvoted = hasupvoted;}
    public int getHidenval(){return this.hidenval;}
    public void setHidenval(int hidenval){this.hidenval = hidenval;}



}
