package com.aliaksei.guideapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by alexc on 6/19/2018.
 */

public class FragmentCreateFeed extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layout = inflater.inflate(R.layout.fragment_create,null);

        final EditText editTitle = (EditText)layout.findViewById(R.id.titedit);
        final EditText editDesc = (EditText)layout.findViewById(R.id.descedit);

        final Button btnCreate = (Button)layout.findViewById(R.id.btncreate);
        final Button btnCancel = (Button)layout.findViewById(R.id.btncancel);

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 3){
                    editDesc.setVisibility(View.VISIBLE);
                }else{
                    editDesc.setVisibility(View.GONE);
                }
            }
        });

        editDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 5){
                    btnCreate.setVisibility(View.VISIBLE);
                }else{
                    btnCreate.setVisibility(View.GONE);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create new feed
                AddFeedToDB(editTitle.getText().toString(),editDesc.getText().toString());

                dismiss();

                ((MainFindActivity)getActivity()).dealwithRecycler();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });


        builder.setTitle("Create New Feed");

        builder.setView(layout);



        return builder.create();
    }

    public void AddFeedToDB(String title,String Description){

        // get currently signed in user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","not_found");

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference documentReference = db.collection("feeds").document();

        Map<String, Object> feed = new HashMap<>();
        feed.put("title", title);
        DataFeed dataFeed = new DataFeed(documentReference.getId(),title,user);

        // add DataFeed to FEEDS
        documentReference.set(dataFeed);

        documentReference.collection("posts").add(new DataPost("Welcome to This feed!",user));


        // add Feed ID to USER_FEEDS
        db.collection("users").document(user)
                .collection("user_feeds").document(documentReference.getId())
                .set(dataFeed);

    }

}
