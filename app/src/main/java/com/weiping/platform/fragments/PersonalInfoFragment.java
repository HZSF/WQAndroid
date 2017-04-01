package com.weiping.platform.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiping.platform.MainMemberActivity;
import com.weiping.servicecentre.Constants;

import platform.tyk.weping.com.weipingplatform.R;

public class PersonalInfoFragment extends MainMemberActivity.PlaceholderFragment {
    public View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.SHARE_PREFERENCE_USERNAME, "");
        ((TextView)rootView.findViewById(R.id.account_txtView_username_value)).setText(username);
        return rootView;
    }
}
