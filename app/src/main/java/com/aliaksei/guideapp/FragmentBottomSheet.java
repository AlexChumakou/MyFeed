package com.aliaksei.guideapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by alexc on 6/4/2018.
 */

public class FragmentBottomSheet extends BottomSheetDialogFragment {

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(contentView);

        TextView textView = (TextView)dialog.findViewById(R.id.groupname);
        textView.setText(getArguments().getString("data"));
        TextView descText = (TextView)dialog.findViewById(R.id.groupdesc);
        descText.setText(getArguments().getString("desc","No Description Found"));

        Button btn = (Button) dialog.findViewById(R.id.btnaddgroup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add to user's main Feed

                String id = getArguments().getString("id");
                ((MainFindActivity)getActivity()).WriteNewFeedToDB(id);
                dialog.dismiss();



            }
        });



    }

}
