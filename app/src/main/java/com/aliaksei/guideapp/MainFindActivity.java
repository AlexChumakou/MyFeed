package com.aliaksei.guideapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFindActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> list;
    RecyclerView recyclerView;
    AdapterFind adapter;
    FragmentBottomSheet bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_find);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(Color.parseColor("#060809"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerFind);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        dealwithRecycler();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // replace with New Feed Activity
                WriteNewFeedToDB("Alex is awesome");

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.findbtn1).setOnClickListener(this);

    }

    // - DB READ/WRITE - //

    public void WriteNewFeedToDB(String data){

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","not_found");
        // Create a new Feed with a title
        Map<String, Object> feed = new HashMap<>();
        feed.put("title", data);

        // Add a new document with a generated ID
        db.collection("users").document(user)
                .collection("user_feeds").document(data)
                .set(feed);
    }

    private void ReadFeedsFromDB(){

        // - Initialize DB and list - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("feeds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                list.add(document.getString("title"));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    // - SEARCH BUTTON / SEARCH METHOD - //

    @Override
    public void onClick(View v){

        if(v.getId() == R.id.findbtn1){

            EditText editText = (EditText)findViewById(R.id.findedit1);
            String userInput = editText.getText().toString();

            if(!userInput.equals("")){

                dealwithSearch(userInput);

                closeKeyboard();


            }


        }

    }

    public void dealwithSearch(String input){
        ArrayList<String> list = new ArrayList<>();
        list.add(input);

        adapter = new AdapterFind(list);
        recyclerView.setAdapter(adapter);

    }


    // - POPULATE RECYCLER / ITEM CLICKED - //

    public void dealwithRecycler(){

        list = new ArrayList<>();


        adapter = new AdapterFind(list);
        recyclerView.setAdapter(adapter);

        ReadFeedsFromDB();
    }

    public void ItemClicked(String data){

        // show bottom sheet fragment with details

        Bundle bundle = new Bundle();
        bundle.putString("data",data);
        bottomSheetDialogFragment = new FragmentBottomSheet();
        bottomSheetDialogFragment.setArguments(bundle);

        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }


    // - OTHER - //

    public void closeKeyboard(){

        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    // - MENU - //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_find_activity, menu);
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
