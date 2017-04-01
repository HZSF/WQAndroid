package com.weiping.InteProperty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkFragment extends Fragment {

    private ImageView centre_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frameLayout = (FrameLayout)inflater.inflate(R.layout.fragment_trademark, container, false);
        centre_image = (ImageView)frameLayout.findViewById(R.id.centreImage);
        return frameLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(IntePropSearchActivity.class.isInstance(getActivity())){
            final int resId = R.drawable.trademark_centre;
            ((IntePropSearchActivity)getActivity()).loadBitmap(resId,centre_image,525,525);
        }
    }

}
