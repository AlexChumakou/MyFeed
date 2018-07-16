package com.aliaksei.guideapp;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class MainFeedActivity extends AppCompatActivity  {


    private BottomSheetBehavior mBottomSheetBehavior1,mBottomSheetBehavior2;
    FloatingActionButton fab,fab2;

    RecyclerView recyclerView,recyclerViewbottom;
    AdapterFeed adapterFeed;
    AdapterComm adapterComm;

    ArrayList<DataPost> list,commentlist;
    String FeedId,Feedtitle;
    TextView mainText;
    Button commentBtn;
    FragmentBottomComm bottomSheetDialogFragment;
    Firestore_Helper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        TextView txt = getTextViewTitle(toolbar);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txt.setTransitionName("title");
        }

        firestoreHelper = new Firestore_Helper();

        // --- BUNDLE --- //
        String title,id;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Feedtitle = bundle.getString("title","Title of Feed");
            id = bundle.getString("id");
            getSupportActionBar().setTitle(Feedtitle);
        }else{
            id = getSharedPreferences("settings", MODE_PRIVATE).getString("feedid","EMPTY");
            Feedtitle = getSharedPreferences("settings", MODE_PRIVATE).getString("feedtitle","Error");
            getSupportActionBar().setTitle(Feedtitle);
        }

        FeedId = id;
        list = new ArrayList<>();

        // Initiate and populate RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerFeed);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapterFeed = new AdapterFeed(list);
        recyclerView.setAdapter(adapterFeed);




        //commentlist = new ArrayList<>();

        ReadFromDB(id);

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

    public void UpvoteClicked(final DataPost dataPost, final int pos){


        //final int cntreturn;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        String username = sharedPreferences.getString("user","not_created");

        final String id = db.collection("users").document(username).getId();

        String upUsers = dataPost.getHasupvoted();

        final DocumentReference documentReference = db.collection("feeds").document(FeedId)
                .collection("posts").document(dataPost.getId());

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                DataPost dataPost1 = transaction.get(documentReference).toObject(DataPost.class);
                //dataPost1.setDate(dataPost.getDate());
                String upUsers = dataPost1.getHasupvoted();
                int cheers = 0;
                if(upUsers.contains(id)) {
                    upUsers = upUsers.replace(id.concat(" "),"");
                    cheers = dataPost1.getCheers() - 1;

                }else {
                    upUsers = upUsers + id + " ";
                    cheers = dataPost1.getCheers() + 1;

                }

                dataPost1.setHasupvoted(upUsers);
                dataPost1.setCheers(cheers);

                //textView.setText(cheers + "");

                // Update restaurant
                transaction.set(documentReference, dataPost1);

                dataPost1.setDate(dataPost.getDate());
                datapostchanged(pos, dataPost1);
                return null;
            }


        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //readFromDB();
                //Log.d(TAG, "Transaction success!");

            }
        });



    }

    public void datapostchanged(int pos, DataPost dataPost){



        list.set(pos,dataPost);
        adapterFeed.notifyItemChanged(pos);
        recyclerView.smoothScrollToPosition(pos);
    }

    public static TextView getTextViewTitle(Toolbar toolbar){
        TextView textViewTitle = null;
        for(int i = 0; i<toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView) {
                textViewTitle = (TextView) view;
                break;
            }
        }
        return textViewTitle;
    }



    // - RECYCLER POPULATE/ITEM CLICKED - //



    public void ReadFromDB(String id){
        // - Initialize DB and list - //

        firestoreHelper.getFeedPosts(id,list,adapterFeed);

    }



    public void ItemClicked(DataPost data){

        // Start new Activity Post Activity
        Intent i = new Intent(this, MainPostActivity.class);
        i.putExtra("feedid",FeedId);
        i.putExtra("postid",data.getId());
        i.putExtra("text",data.getMessage());
        i.putExtra("user",data.getUser());
        startActivity(i);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        sharedPreferences.edit().putString("feedid",FeedId).apply();
        sharedPreferences.edit().putString("feedtitle",Feedtitle).apply();

        /*
        DO NOT DELETE
        USE FOR LONGCLICK TO SHOW TOP COMMENT
        Bundle bundle = new Bundle();
        bundle.putString("data",data.getMessage());
        bundle.putString("postid",data.getId());
        bundle.putString("feedid",FeedId);

        bottomSheetDialogFragment = new FragmentBottomComm();
        bottomSheetDialogFragment.setArguments(bundle);

        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

        */
    }

    public void ItemLongClicked(String data){

        // Show the top comment

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
