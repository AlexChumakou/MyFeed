package com.aliaksei.guideapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends Activity implements View.OnClickListener {

    EditText Euser,Epassword;
    Button signin,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(Color.parseColor("#060809"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        signin = (Button)findViewById(R.id.btnsignin);
        signup = (Button)findViewById(R.id.btnsignup);
        // - Get the fucking user list and find user name from shared preferences

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);

        String user_name = settings.getString("user","");
        if(user_name.equals("")){
            // create new account dialog

            signin.setVisibility(View.VISIBLE);

        }else{
            //signin.setVisibility(View.GONE);
            // sign in and open mainActivity
            Thread timer=new Thread()
            {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    finally
                    {
                        Intent i = new Intent(SplashActivity.this,MainActivity.class);
                        finish();
                        startActivity(i);
                    }
                }
            };
            timer.start();


        }

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);

    }


    public void onClick(View view){

        if(view.getId() == R.id.btnsignin){

            Euser = (EditText)findViewById(R.id.edituser);
            Epassword = (EditText)findViewById(R.id.editpassword);

            if(Euser.getVisibility() == View.GONE){
                Euser.setVisibility(View.VISIBLE);
                Epassword.setVisibility(View.VISIBLE);
                signup.setVisibility(View.VISIBLE);
            }else{
                if (!Euser.getText().toString().equals("")&& Epassword.getText().toString().length() >= 4 ){

                    SignInUser(Euser.getText().toString(),Epassword.getText().toString());

                }else{
                    Snackbar.make(getCurrentFocus(), "Password too short!", Snackbar.LENGTH_LONG)
                            .show();
                }
                // Create new user ()

            }
        }else if (view.getId() == R.id.btnsignup){

            Euser = (EditText)findViewById(R.id.edituser);
            Epassword = (EditText)findViewById(R.id.editpassword);

            if (!Euser.getText().toString().equals("")&& Epassword.getText().toString().length() >= 4){

                createNewUser(Euser.getText().toString(),Epassword.getText().toString());

            }

                // Create new user ()





        }

    }

    private void SignInUser(final String user, final String pass){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.getString("password").equals(pass)){

                            SharedPreferences settings = getSharedPreferences("settings",MODE_PRIVATE);
                            settings.edit().putString("user",user).commit();

                            Intent i = new Intent(getApplication(),MainActivity.class);
                            startActivity(i);
                            SplashActivity.this.finish();

                        }else{

                            Snackbar.make(getCurrentFocus(), "Wrong Password!", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {

                        Snackbar.make(getCurrentFocus(), "User not found, try again or Sign-up!", Snackbar.LENGTH_LONG)
                                .show();

                        //Log.d(TAG, "No such document");
                    }
                } else {

                    Snackbar.make(getCurrentFocus(), "Task is unsuccessful", Snackbar.LENGTH_LONG)
                            .show();
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




        //if user exists and password is correct
        //    open activity
        //else
        //    try again

    }

    private void createNewUser(String user,String pass){


        WriteNewFeedToDB(user,pass);



    }

    // - DB READ/WRITE - //

    private void WriteNewFeedToDB(final String user,String pass){

        // - Initialize DB - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new Feed with a title
        Map<String, Object> feed = new HashMap<>();
        feed.put("user_name", user);
        feed.put("password",pass);

        // Add a new document with a generated ID
        db.collection("users").document(user)
                .set(feed)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        SharedPreferences settings = getSharedPreferences("settings",MODE_PRIVATE);
                        settings.edit().putString("user",user).commit();

                        Intent i = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();

                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                    }
                });



    }

    private void ReadFeedsFromDB(){

        // - Initialize DB and list - //
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document("alex_c")
                .collection("user_feeds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //list.add(document.getString("title"));
                                //adapter.notifyDataSetChanged();
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });



    }
}
