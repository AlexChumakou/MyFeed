package com.aliaksei.guideapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFindActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<DataFeed> list;
    RecyclerView recyclerView;
    Button clear;
    AdapterFind adapter;
    FragmentBottomSheet bottomSheetDialogFragment;
    EditText editText;
    SearchView searchView;
    Firestore_Helper firestoreHelper;
    TextView mainText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_find);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestoreHelper = new Firestore_Helper();

        list = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerFind);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new AdapterFind(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                closeKeyboard();
            }
        });

        mainText = (TextView)findViewById(R.id.infoText);

        ReadFeedsFromDB();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment fragmentCreateFeed = new FragmentCreateFeed();
                fragmentCreateFeed.show(getFragmentManager(),"create");

            }
        });

    }

    // - DB READ/WRITE - //

    public void WriteNewFeedToDB(final String id){

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        final String user = sharedPreferences.getString("user","not_found");
        // Create a new Feed with a title

        db.collection("feeds").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DataFeed dataFeed = task.getResult().toObject(DataFeed.class);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user)
                        .collection("user_feeds").document(id)
                        .set(dataFeed);



            }
        });

        Snackbar.make(getCurrentFocus(), "New feed added to the Main Screen!", Snackbar.LENGTH_LONG)
                .show();

        // Add a new document with a generated ID

    }

    public void ReadFeedsFromDB(){
        mainText.setText("All Feeds");
        // - Initialize DB and list - //
        firestoreHelper.getAllFeeds(list,adapter);

    }


    // - SEARCH BUTTON / SEARCH METHOD - //

    @Override
    public void onClick(View v){

        if(v.getId() == R.id.findbtn1){

            String userInput = editText.getText().toString();

            if(!userInput.equals("")){

                dealwithSearch(userInput);

                closeKeyboard();


            }


        }


    }

    public void dealwithSearch(String input){
        mainText.setText("Search Results...");
        firestoreHelper.getSearchedFeeds(input,list,adapter,getCurrentFocus());

    }


    public void ItemClicked(DataFeed data){

        // show bottom sheet fragment with details

        Bundle bundle = new Bundle();
        bundle.putString("data",data.getTitle());
        bundle.putString("desc",data.getDescription());
        bundle.putString("id",data.getId());
        bundle.putString("user",data.getCreator());

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
        // Associate searchable configuration with the SearchView
        MenuItem searchViewMenuItem = menu.findItem(R.id.search2);
        searchView = (SearchView) searchViewMenuItem.getActionView();

        //searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do your search

                dealwithSearch(query);

                closeKeyboard();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) ReadFeedsFromDB();


                return false;
            }
        });


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
