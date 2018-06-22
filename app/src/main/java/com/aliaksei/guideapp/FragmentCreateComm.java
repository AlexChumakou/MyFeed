package com.aliaksei.guideapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

public class FragmentCreateComm extends DialogFragment {


    String feedId,postId;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        feedId = getArguments().getString("feedid","null");
        postId = getArguments().getString("postid","null");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layout = inflater.inflate(R.layout.fragment_create_post,null);

        final EditText editTitle = (EditText)layout.findViewById(R.id.titedit);
        //final EditText editDesc = (EditText)layout.findViewById(R.id.descedit);

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
                if(s.length() >= 1){
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
                AddPostToDB(editTitle.getText().toString());

                dismiss();

                //((MainFeedActivity)getActivity()).dealwithBottomRecycler(postId);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });


        builder.setTitle("New Comment");

        builder.setView(layout);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //showSoftKeyboard(editTitle);

        return dialog;
    }

    public void AddPostToDB(String message){

        // get currently signed in user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","not_found");

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("feeds").document(feedId)
                .collection("posts").document(postId)
                .collection("comments").document();

        documentReference.set(new DataPost(documentReference.getId(),message,user));


    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
