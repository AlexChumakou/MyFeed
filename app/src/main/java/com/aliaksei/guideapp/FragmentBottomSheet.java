package com.aliaksei.guideapp;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by alexc on 6/4/2018.
 */

public class FragmentBottomSheet extends BottomSheetDialogFragment {


    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(contentView);

        TextView textView = (TextView)dialog.findViewById(R.id.groupname);
        textView.setText(getArguments().getString("data"));



        Button btn = (Button) dialog.findViewById(R.id.btnaddgroup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add to user's main Feed

                String data = getArguments().get("data").toString();
                ((MainFindActivity)getActivity()).WriteNewFeedToDB(data);
                dialog.dismiss();



            }
        });


    }

}
