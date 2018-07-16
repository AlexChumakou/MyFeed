package com.aliaksei.guideapp;

/**
 * Created by alexc on 6/7/2018.
 */

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by alexc on 6/5/2018.
 */

public class AdapterComm  extends RecyclerView.Adapter<AdapterComm.ViewHolder> {

    public List<DataComm> classes;


    //Constructor
    AdapterComm(List<DataComm> classes){
        this.classes = classes;
    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cardName;
        TextView cardCat;
        TextView cardMessage;
        TextView cardCheer;
        TextView cardDate;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cardName = (TextView)itemView.findViewById(R.id.feedName);
            cardCat = (TextView) itemView.findViewById(R.id.feedCat);
            cardMessage = (TextView) itemView.findViewById(R.id.feedData);
            cardCheer = (TextView) itemView.findViewById(R.id.feedCheer);
            cardDate = (TextView) itemView.findViewById(R.id.feedDate);
        }
    }

    //onCreateViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewcomment, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }


    //onBindViewHolder
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.cardName.setText(classes.get(i).getUser());
        viewHolder.cardMessage.setText(classes.get(i).getMessage());
        viewHolder.cardCheer.setText("" + classes.get(i).getCheers());
        viewHolder.cardDate.setText(classes.get(i).getDate());



        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragmentBottomComm.onClick(classes.get(i),i);

                //((MainPostActivity)viewHolder.cv.getContext()).ItemClicked(classes.get(i));

                //((MainFeedActivity)viewHolder.cv.getContext()).ItemClicked(classes.get(i));

                //Intent i = new Intent(viewHolder.classTag.getContext(), MyClassActivity.class);
                //viewHolder.classProf.getContext().startActivity(i);

            }
        });

        viewHolder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //((MainFeedActivity)viewHolder.cv.getContext()).ItemLongClicked(classes.get(i).getUser());

                // Open Dialog with upvote and report option

                return false;
            }
        });


    }


    public void UpdateCommentCheer(DataComm dataComm){




    }


    //onAttachedToRecyclerView
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount(){
        return classes.size();
    }



}

