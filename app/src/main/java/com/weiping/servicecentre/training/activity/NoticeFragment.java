package com.weiping.servicecentre.training.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import platform.tyk.weping.com.weipingplatform.R;

public class NoticeFragment extends Fragment {

    private ImageView centre_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frameLayout = (FrameLayout)inflater.inflate(R.layout.fragment_notice, container, false);
        //TextView textView = (TextView)V.findViewById(R.id.txt_notice_context);
        //textView.setText(Html.fromHtml(getResources().getString(R.string.training_notices)));
        centre_image = (ImageView)frameLayout.findViewById(R.id.centreImage);
        return frameLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(ManageTrainingActivity.class.isInstance(getActivity())){
            final int resId = R.drawable.training_notice_centre;
            ((ManageTrainingActivity)getActivity()).loadBitmap(resId,centre_image);
        }
    }

}
