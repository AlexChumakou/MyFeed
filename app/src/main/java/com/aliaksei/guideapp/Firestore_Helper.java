package com.aliaksei.guideapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by alexc on 7/4/2018.
 */

public class Firestore_Helper{


    private FirebaseFirestore db;

    //Constructor initialize database
    Firestore_Helper(){
        db = FirebaseFirestore.getInstance();
    }

    // ----
    // Get saved Feeds
    // ----
    public void getSavedFeeds(String userTag,
                        final ArrayList<DataFeed> dataFeedList,
                        final AdapterMain adapterMain,
                        final TextView infoText){


        db.collection("users").document(userTag)
                .collection("user_feeds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            dataFeedList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                dataFeedList.add(document.toObject(DataFeed.class));
                                adapterMain.notifyDataSetChanged();
                            }

                            if (dataFeedList.isEmpty()){
                                infoText.setText("Nothing to load!");
                            }else{
                                infoText.setText("My Feeds");
                            }

                        }
                    }
                });
    }

    // ----
    // Get all Feeds
    // ----
    public void getAllFeeds(final ArrayList<DataFeed> dataFeedList,
                            final AdapterFind adapterFind){

        db.collection("feeds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataFeedList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                dataFeedList.add(document.toObject(DataFeed.class));
                                adapterFind.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    // ----
    // Get searched Feeds
    // ----
    public void getSearchedFeeds(final String search,
                                 final ArrayList<DataFeed> dataFeedList,
                                 final AdapterFind adapterFind,
                                 final View focus){
        db.collection("feeds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataFeedList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.toObject(DataFeed.class).getTitle().toLowerCase().contains(search.toLowerCase())){
                                    dataFeedList.add(document.toObject(DataFeed.class));
                                    adapterFind.notifyDataSetChanged();
                                }
                            }
                            if(dataFeedList.isEmpty()){
                                adapterFind.notifyDataSetChanged();
                                Snackbar.make(focus, "None found! Create new feed or try again.", Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Log.w("TAAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    // ----
    // Get Posts from a Feed
    // ----
    public void getFeedPosts(String feedID, final ArrayList<DataPost> dataPostList, final AdapterFeed adapterFeed){

        //get todays date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        final String currTime =  sdf.format(Calendar.getInstance().getTime());



        db.collection("feeds").document(feedID)
                .collection("posts")
                .orderBy("cheers", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataPostList.clear();



                            for (DocumentSnapshot document : task.getResult()) {

                                DataPost dataPost = document.toObject(DataPost.class);

                                int cheers = dataPost.getCheers();
                                String postDate = dataPost.getDate();

                                int datediff = getDaysPassed(currTime,postDate);

                                int hidCheers = (cheers)/((datediff));
                                dataPost.setHidenval(hidCheers);
                                dataPost.setDate(datediff + "d");
                                dataPostList.add(dataPost);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //dataPostList.add(document.toObject(DataPost.class));

                            }

                            for(int j = 0; j < dataPostList.size();j++) {
                                for (int i = 1; i < dataPostList.size()-j; i++) {
                                    if (dataPostList.get(i).getHidenval() > dataPostList.get(i - 1).getHidenval()) {
                                        DataPost tpose = dataPostList.get(i);
                                        dataPostList.set(i, dataPostList.get(i - 1));
                                        dataPostList.set(i - 1, tpose);
                                    }
                                }
                            }

                            adapterFeed.notifyDataSetChanged();
                        }
                    }
                });

    }


    public int getDaysPassed(String currDate, String postDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            Date date1 = simpleDateFormat.parse(currDate);
            Date date2 = simpleDateFormat.parse(postDate);

            long different = date1.getTime() - date2.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;

            int iElap = (int)elapsedDays;

            if(iElap == 0)
                iElap = 1;

            return iElap;


            //obj.printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


    }

    // ----
    // Get Comments from a Post
    // ----
    public void getPostComments(String feedID, String postID, final ArrayList<DataComm> dataCommsList, final AdapterComm adapterComm){

        db.collection("feeds").document(feedID)
                .collection("posts").document(postID)
                .collection("comments")
                .orderBy("cheers", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataCommsList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                dataCommsList.add(document.toObject(DataComm.class));
                                adapterComm.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
