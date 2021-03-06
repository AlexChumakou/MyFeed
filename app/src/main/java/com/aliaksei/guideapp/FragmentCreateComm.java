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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

                ((MainPostActivity)getActivity()).CommentAdded();

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

        String currDate = getCurrDate();

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("feeds").document(feedId)
                .collection("posts").document(postId)
                .collection("comments").document();

        documentReference.set(new DataComm(documentReference.getId(),message,user,currDate,0));


        final DocumentReference documentRef = db.collection("feeds").document(feedId)
                .collection("posts").document(postId);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DataPost dataPost1 = transaction.get(documentRef).toObject(DataPost.class);
                String upUsers = dataPost1.getHasupvoted();

                // Compute new number of ratings
                int cheers = dataPost1.getCheers() + 1;

                dataPost1.setCheers(cheers);
                // Update restaurant
                transaction.set(documentRef, dataPost1);

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


    public String getCurrDate(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        return sdf.format(Calendar.getInstance().getTime());
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
