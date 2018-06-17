package com.aliaksei.guideapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainFeedActivity extends AppCompatActivity {


    private BottomSheetBehavior mBottomSheetBehavior1;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(Color.parseColor("#060809"));

        // --- BUNDLE --- //
        String data;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            data = bundle.getString("data");
            getSupportActionBar().setTitle(data);
        }else{
            data = "EMPTY";
        }

        // --- SET UP UI --- //
        dealwithRecyclerView();
        dealwithBottomSheet();

        // --- FAB --- //
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void dealwithRecyclerView(){

        ArrayList<String> list = new ArrayList<>();
        list.add("Alex C");
        list.add("Avi A");
        list.add("Varun K");
        list.add("Darya C");
        list.add("Avi A");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerFeed);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        AdapterFeed adapter = new AdapterFeed(list);
        recyclerView.setAdapter(adapter);
    }

    public void ItemClicked(String data){


    }
    public void ItemLongClicked(String data){

        if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            fab.animate().scaleY(0).scaleX(0).setDuration(500).start();

        }


    }

    public void dealwithBottomSheet(){

        View bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    fab.animate().scaleY(1).scaleX(1).setDuration(500).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
