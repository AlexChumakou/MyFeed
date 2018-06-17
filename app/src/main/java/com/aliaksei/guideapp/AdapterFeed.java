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

import java.util.List;

/**
 * Created by alexc on 6/5/2018.
 */

public class AdapterFeed  extends RecyclerView.Adapter<AdapterFeed.ViewHolder> {

    public List<String> classes;


    //Constructor
    AdapterFeed(List<String> classes){
        this.classes = classes;
    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cardName;
        TextView cardCat;
        TextView classTime;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cardName = (TextView)itemView.findViewById(R.id.feedName);
            cardCat = (TextView) itemView.findViewById(R.id.feedCat);
        }
    }

    //onCreateViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewfeed, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }


    //onBindViewHolder
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.cardName.setText(classes.get(i));

        if(classes.get(i).equals("Alex C"))
            viewHolder.cardCat.setText("Q");
        else if(classes.get(i).equals("Avi A"))
            viewHolder.cardCat.setText("E");
        else if(classes.get(i).equals("Varun K"))
            viewHolder.cardCat.setText("R");
        else
            viewHolder.cardCat.setText("N");
        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i = new Intent(viewHolder.classTag.getContext(), MyClassActivity.class);
                //viewHolder.classProf.getContext().startActivity(i);

            }
        });

        viewHolder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ((MainFeedActivity)viewHolder.cv.getContext()).ItemLongClicked(classes.get(i));

                // Open Dialog with upvote and report option

                return false;
            }
        });


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

