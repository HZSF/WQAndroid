package com.weiping.platform.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.weiping.membership.finance.activity.AppliedFinancialProductsListActivity;
import com.weiping.membership.healthy.activity.BookedHealthyPreActivity;
import com.weiping.membership.quality.activity.AppointedInspectionListActivity;
import com.weiping.membership.training.activity.RegisteredCoursesListActivity;
import com.weiping.platform.MainMemberActivity;
import com.weiping.platform.adapter.MyAppointAdapter;

import platform.tyk.weping.com.weipingplatform.R;

public class MyAppointFragment extends MainMemberActivity.PlaceholderFragment {
    public View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_appoint, container, false);
        setGridView();
        return rootView;
    }
    public void setGridView() {
        GridView gridview = (GridView) rootView.findViewById(R.id.gv_appointment);
        String[] dashStrings = {
                getResources().getString(R.string.lbl_appoint_course),
                getResources().getString(R.string.lbl_appoint_finance),
                getResources().getString(R.string.lbl_appoint_health),
                getResources().getString(R.string.lbl_appoint_quality),
                getResources().getString(R.string.lbl_appoint_property)
        };
        int[] dashDraws = {
                R.mipmap.cs_btn_icon_management,
                R.mipmap.cs_btn_icon_finance,
                R.mipmap.cs_btn_icon_health,
                R.mipmap.cs_btn_icon_quality,
                R.mipmap.cs_btn_icon_estate
        };
        gridview.setAdapter(new MyAppointAdapter(getActivity(), dashStrings, dashDraws));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        //intent = new Intent(getActivity().getBaseContext(), AppointedCourseListActivity.class);
                        intent = new Intent(getActivity().getBaseContext(), RegisteredCoursesListActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity().getBaseContext(), AppliedFinancialProductsListActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity().getBaseContext(), BookedHealthyPreActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity().getBaseContext(), AppointedInspectionListActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        break;
                }

            }
        });
    }

}
