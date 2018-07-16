package com.aliaksei.guideapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainPostActivity extends AppCompatActivity {


    String text,user,FeedID,PostID;
    ArrayList<DataComm> commentlist;
    AdapterComm adapterComm;
    Firestore_Helper firestoreHelper;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        // --- BUNDLE --- //

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            text = bundle.getString("text","Post Message");
            user = bundle.getString("user", "Post Author");
            FeedID = bundle.getString("feedid","Empty");
            PostID = bundle.getString("postid", "Empty");
        }else{
            text = "Post Message";
            user = "Post User";
            FeedID = "Empty";
            PostID = "Empty";
        }

        TextView mainText = (TextView)findViewById(R.id.mainText);
        TextView mainUser = (TextView)findViewById(R.id.mainUser);
        mainText.setText(text);
        mainUser.setText(user);

        commentlist = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerComment);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapterComm = new AdapterComm(commentlist);
        recyclerView.setAdapter(adapterComm);

        firestoreHelper = new Firestore_Helper();
        firestoreHelper.getPostComments(FeedID,PostID,commentlist, adapterComm);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment fragmentCreateComm = new FragmentCreateComm();
                Bundle bundle = new Bundle();
                bundle.putString("feedid",FeedID);
                bundle.putString("postid",PostID);
                fragmentCreateComm.setArguments(bundle);
                fragmentCreateComm.show(getFragmentManager(),"tag");

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // ----
    // Comment was just added
    // ----

    public void CommentAdded(){
        // repopulate list
        firestoreHelper.getPostComments(FeedID,PostID,commentlist, adapterComm);
    }

}
