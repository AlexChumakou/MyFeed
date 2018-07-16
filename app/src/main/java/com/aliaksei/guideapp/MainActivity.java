package com.aliaksei.guideapp;


import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    FragmentBottomSheet bottomSheetDialogFragment;
    private BottomSheetBehavior mBottomSheetBehavior1;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    AdapterMain adapter;
    ArrayList<DataFeed> list;
    TextView mainText;

    Firestore_Helper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firestoreHelper = new Firestore_Helper();
        list = new ArrayList<>();

        // Initiate RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerMain);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new AdapterMain(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab.animate().scaleY(1).scaleX(1).setDuration(300).start();

            }
        });

        mainText = (TextView)findViewById(R.id.mainTitle);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,MainFindActivity.class);
                startActivity(i);

                /*
                if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fab.animate().scaleY(0).scaleX(0).setDuration(500).start();

                }
                */

                //bottomSheetDialogFragment = new FragmentBottomSheet();
                //bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        dealwithBottomSheet();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        mainText.setText("Loading...");
        ReadFeedsFromDB();
    }


    // - DB READ/WRITE - //

    private void WriteNewFeedToDB(String data){

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new Feed with a title
        Map<String, Object> feed = new HashMap<>();
        feed.put("title", data);

        // Add a new document with a generated ID
        db.collection("feeds")
                .add(feed)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAAAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAAAG", "Error adding document", e);
                    }
                });



    }

    private void ReadFeedsFromDB(){

        // get userTag from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        String username = sharedPreferences.getString("user","not_created");

        // get list of DataFeed (user's saved feeds) PARAMS: userTag,list,adapter,infoTextView
        firestoreHelper.getSavedFeeds(username,list,adapter,mainText);

    }


    public void ItemClicked(DataFeed data,View view){



        Intent i = new Intent(MainActivity.this, MainFeedActivity.class);
        i.putExtra("id",data.getId());
        i.putExtra("title",data.getTitle());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)view, "title");
        startActivity(i, options.toBundle());
        //startActivity(i);


    }
    public void ItemLongClicked(DataFeed data){

        if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            fab.animate().scaleY(0).scaleX(0).setDuration(500).start();

        }

    }


    // - NOT USED -//

    public void dealwithBottomSheet(){

        View bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    fab.animate().scaleY(1).scaleX(1).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    public void onClick(View v){


    }


    // - MENU / NAV BAR / ON BACK - //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        }else if(id == R.id.action_new){

            Intent i = new Intent(MainActivity.this,MainFindActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myevents) {



            // Handle the camera action
        } else if (id == R.id.nav_findfeeds) {

            Intent i = new Intent(MainActivity.this,MainFindActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_signout){

            SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);

            sharedPreferences.edit().putString("user","").apply();

            Intent i = new Intent(this, SplashActivity.class);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed(){

    }

}
