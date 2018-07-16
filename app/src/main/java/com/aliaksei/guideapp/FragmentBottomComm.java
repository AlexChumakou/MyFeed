package com.aliaksei.guideapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by alexc on 6/4/2018.
 */

public class FragmentBottomComm extends BottomSheetDialogFragment {

    RecyclerView recyclerViewbottom;
    AdapterComm adapterComm;
    ArrayList<DataComm> commentlist;
    String feedid;
    String postid;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_comm, null);
        dialog.setContentView(contentView);

        feedid = getArguments().getString("feedid","null");
        postid = getArguments().getString("postid","null");

        commentlist = new ArrayList<>();

        recyclerViewbottom = (RecyclerView)dialog.findViewById(R.id.recyclerComment);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerViewbottom.setLayoutManager(llm);

        adapterComm = new AdapterComm(commentlist);
        recyclerViewbottom.setAdapter(adapterComm);

        readFromDB();

        TextView textView = (TextView)dialog.findViewById(R.id.mainText);
        textView.setText(getArguments().getString("data"));

        Button btn = (Button) dialog.findViewById(R.id.btncomment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add to user's main Feed

                //String id = getArguments().getString("id");
                //((MainFindActivity)getActivity()).WriteNewFeedToDB(id);

                DialogFragment fragmentCreateComm = new FragmentCreateComm();
                Bundle bundle = new Bundle();
                bundle.putString("feedid",feedid);
                bundle.putString("postid",postid);
                fragmentCreateComm.setArguments(bundle);
                fragmentCreateComm.show(getActivity().getFragmentManager(),"tag");


                dialog.dismiss();



            }
        });




    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }


    public void readFromDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("feeds").document(feedid)
                .collection("posts").document(postid)
                .collection("comments")
                .orderBy("cheers", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentlist.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                commentlist.add(document.toObject(DataComm.class));
                                adapterComm.notifyDataSetChanged();
                            }

                        } else {

                        }
                    }
                });
    }

    public void onClick(DataComm dataComm,final int pos){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("feeds").document(feedid)
                .collection("posts").document(postid)
                .collection("comments").document(dataComm.getId());

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DataComm datacomm = transaction.get(documentReference).toObject(DataComm.class);

                // Compute new number of ratings
                int cheers = datacomm.getCheers() + 1;

                datacomm.setCheers(cheers);


                // Update restaurant
                transaction.set(documentReference, datacomm);

                return null;
            }


        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                readFromDB();
                //Log.d(TAG, "Transaction success!");
            }
        });



    }



}
