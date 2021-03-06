package com.aliaksei.guideapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alexc on 6/8/2018.
 */

public class AdapterFind  extends RecyclerView.Adapter<AdapterFind.ViewHolder> {

    public List<DataFeed> classes;
    public Context context;

    //Constructor
    AdapterFind(List<DataFeed> classes){
        this.classes = classes;
    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cardName;
        TextView classProf;
        TextView classTime;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cardName = (TextView)itemView.findViewById(R.id.rectag);
        }
    }

    //onCreateViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewfind, viewGroup, false);
        context = viewGroup.getContext();
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }


    //onBindViewHolder
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.cardName.setText(classes.get(i).getTitle());


        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainFindActivity)context).ItemClicked(classes.get(i));
                //Intent i = new Intent(context, MainFeedActivity.class);
                //context.startActivity(i);

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

