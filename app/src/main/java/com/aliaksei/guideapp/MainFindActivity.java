package com.aliaksei.guideapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

public class MainFindActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_find);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(Color.parseColor("#060809"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dealwithRecycler();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.findbtn1).setOnClickListener(this);

    }

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

    public void dealwithRecycler(){

        ArrayList<String> list = new ArrayList<>();
        list.add("UC Davis");
        list.add("UC Santa Cruz");
        list.add("UC LA");
        list.add("UC Berkley");
        list.add("NBA");
        list.add("NFL");
        list.add("Davis Party");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerFind);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        AdapterFind adapter = new AdapterFind(list);
        recyclerView.setAdapter(adapter);


    }

    FragmentBottomSheet bottomSheetDialogFragment;
    public void ItemClicked(String data){

        // show bottom sheet fragment with details

        Bundle bundle = new Bundle();
        bundle.putString("data",data);
        bottomSheetDialogFragment = new FragmentBottomSheet();
        bottomSheetDialogFragment.setArguments(bundle);

        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());



    }

    public void dealwithSearch(String input){
        ArrayList<String> list = new ArrayList<>();
        list.add(input);


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerFind);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        AdapterFind adapter = new AdapterFind(list);
        recyclerView.setAdapter(adapter);


    }

    public void closeKeyboard(){

        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

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
