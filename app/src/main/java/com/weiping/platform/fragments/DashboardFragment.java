package com.weiping.platform.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.InteProperty.IntePropSearchActivity;
import com.weiping.common.BitmapWorkerTask;
import com.weiping.membership.interactive.activity.InteractCommentActivity;
import com.weiping.platform.MainMemberActivity;
import com.weiping.platform.adapter.DashboardAdapter;
import com.weiping.platform.asyncTasks.AnnounceNotificationTask;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.finance.activity.FinancialServiceActivity;
import com.weiping.servicecentre.healthy.activity.HealthCheckActivity;
import com.weiping.servicecentre.property.activity.PropertyActivity;
import com.weiping.servicecentre.quality.activity.QualityServiceActivity;
import com.weiping.servicecentre.training.activity.ManageTrainingActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class DashboardFragment extends MainMemberActivity.PlaceholderFragment {

    public View rootView;
    private AnnounceNotificationTask announceNotificationTask;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        loadBitmap(R.drawable.dash_centre, (ImageView) rootView.findViewById(R.id.img_dash_centre));
        setGridView();
        TextView announce = (TextView)rootView.findViewById(R.id.txt_latest_announce);

        announceNotificationTask = new AnnounceNotificationTask(announce);
        announceNotificationTask.execute(Constants.URL_ANNOUNCE);
        return rootView;
    }

    @Override
    public void onResume() {
        TextView announce = (TextView)rootView.findViewById(R.id.txt_latest_announce);

        announce.setSelected(true);
        super.onResume();
    }

    public void setGridView() {
        GridView gridview = (GridView) rootView.findViewById(R.id.gv_dashboard);
        String[] dashStrings = {
                getResources().getString(R.string.lbl_centre_service_manage_train),
                getResources().getString(R.string.lbl_centre_service_finance_service),
                getResources().getString(R.string.lbl_trademark_service),
                getResources().getString(R.string.lbl_patent_service),
                getResources().getString(R.string.lbl_centre_service_destined_property),
                getResources().getString(R.string.lbl_centre_service_quality_inspection),
                getResources().getString(R.string.lbl_centre_service_health_check),
                getResources().getString(R.string.title_activity_vipdiscuss)
        };
        int[] dashDraws = {
                R.mipmap.cs_btn_icon_management,
                R.mipmap.cs_btn_icon_finance,
                R.mipmap.ic_trademark_icon,
                R.mipmap.ic_patent_icon,
                R.mipmap.cs_btn_icon_estate,
                R.mipmap.cs_btn_icon_quality,
                R.mipmap.cs_btn_icon_health,
                R.mipmap.ic_vip_icon
        };
        gridview.setAdapter(new DashboardAdapter(getActivity(), dashStrings, dashDraws));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (announceNotificationTask != null){
                    announceNotificationTask.cancel(true);
                }
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity().getBaseContext(), ManageTrainingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity().getBaseContext(), FinancialServiceActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity().getBaseContext(), IntePropSearchActivity.class);
                        intent.putExtra("page", 0);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity().getBaseContext(), IntePropSearchActivity.class);
                        intent.putExtra("page", 1);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity().getBaseContext(), PropertyActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        Intent intent_quality = new Intent(getActivity().getBaseContext(), QualityServiceActivity.class);
                        startActivity(intent_quality);
                        break;
                    case 6:
                        Intent intent_healthy = new Intent(getActivity().getBaseContext(), HealthCheckActivity.class);
                        startActivity(intent_healthy);
                        break;
                    case 7:
                        Intent intent_interactive = new Intent(getActivity().getBaseContext(), InteractCommentActivity.class);
                        startActivity(intent_interactive);
                        break;
                }

            }
        });
    }

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, getActivity());
        task.execute(resId);
    }

}
