package com.aliaksei.guideapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainFeedActivity extends AppCompatActivity {


    private BottomSheetBehavior mBottomSheetBehavior1,mBottomSheetBehavior2;
    FloatingActionButton fab,fab2;

    RecyclerView recyclerView,recyclerViewbottom;
    AdapterFeed adapterFeed;
    AdapterComm adapterComm;

    ArrayList<DataPost> list,commentlist;
    String FeedId,PostId;
    TextView mainText;
    Button commentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(Color.parseColor("#060809"));

        // --- BUNDLE --- //
        String title,id;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title = bundle.getString("title","Title of Feed");
            id = bundle.getString("id");
            getSupportActionBar().setTitle(title);
        }else{
            id = "EMPTY";
        }

        FeedId = id;

        // Initiate and populate RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerFeed);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        list = new ArrayList<>();
        commentlist = new ArrayList<>();

        //readfrom database
        PopulateRecyclerView(id);

        // --- SET UP UI --- //
        //dealwithRecyclerView();
        //dealwithBottomSheet();

        // --- FAB --- //
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add new thing
                DialogFragment fragmentCreatePost = new FragmentCreatePost();
                Bundle bundle = new Bundle();
                bundle.putString("feedid",FeedId);
                fragmentCreatePost.setArguments(bundle);
                fragmentCreatePost.show(getFragmentManager(),"create");

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // - RECYCLER POPULATE/ITEM CLICKED - //

    public void PopulateRecyclerView(String id){

        adapterFeed = new AdapterFeed(list);
        recyclerView.setAdapter(adapterFeed);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);



            }
        });


        ReadFromDB(id);
    }

    public void ReadFromDB(String id){
        // - Initialize DB and list - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("feeds").document(id)
                .collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                list.add(document.toObject(DataPost.class));
                                adapterFeed.notifyDataSetChanged();
                            }

                        } else {

                        }
                    }
                });
    }


    FragmentBottomComm bottomSheetDialogFragment;
    public void ItemClicked(DataPost data){

        Bundle bundle = new Bundle();
        bundle.putString("data",data.getMessage());
        bundle.putString("postid",data.getId());
        bundle.putString("feedid",FeedId);



        bottomSheetDialogFragment = new FragmentBottomComm();
        bottomSheetDialogFragment.setArguments(bundle);

        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

        PostId = data.getId();

        /*


        if(mBottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fab.animate().scaleY(0).scaleX(0).setDuration(500).start();

        }

        mainText.setText(data.getMessage());

        PostId = data.getId();

        adapterComm = new AdapterComm(commentlist);
        recyclerViewbottom.setAdapter(adapterComm);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("feeds").document(FeedId)
                .collection("posts").document(data.getId())
                .collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentlist.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                commentlist.add(document.toObject(DataPost.class));
                                adapterComm.notifyDataSetChanged();
                            }

                        } else {

                        }
                    }
                });
          */

    }

    public void ItemLongClicked(String data){

        if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            fab.animate().scaleY(0).scaleX(0).setDuration(500).start();

        }


    }

    public void dealwithBottomSheet(){



        recyclerViewbottom = (RecyclerView)findViewById(R.id.recyclerComment);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerViewbottom.setLayoutManager(llm);

        View bottomSheet = findViewById(R.id.bottom_sheet1);
        //View bottomSheet2 = findViewById(R.id.bottom_sheet2);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        //mBottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2);

        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    fab.animate().scaleY(1).scaleX(1).setDuration(500).start();

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    public void dealwithBottomRecycler(String postId){

        adapterComm = new AdapterComm(commentlist);
        recyclerViewbottom.setAdapter(adapterComm);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("feeds").document(FeedId)
                .collection("posts").document(postId)
                .collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentlist.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                commentlist.add(document.toObject(DataPost.class));
                                adapterComm.notifyDataSetChanged();
                            }

                        } else {

                        }
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
