package com.weiping.platform;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.IntePropSearchActivity;
import com.weiping.common.BitmapWorkerTask;
import com.weiping.credential.activity.LoginActivity;
import com.weiping.membership.interactive.activity.InteractCommentActivity;
import com.weiping.platform.adapter.DashboardAdapter;
import com.weiping.platform.asyncTasks.AnnounceNotificationTask;
import com.weiping.common.Constants;
import com.weiping.servicecentre.announcement.activity.AnnounceCentreActivity;
import com.weiping.servicecentre.finance.activity.FinancialServiceActivity;
import com.weiping.servicecentre.healthy.activity.HealthCheckActivity;
import com.weiping.servicecentre.property.activity.PropertyActivity;
import com.weiping.servicecentre.quality.activity.QualityServiceActivity;
import com.weiping.servicecentre.training.activity.ManageTrainingActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class DashboardActivity extends Activity {

    private AnnounceNotificationTask announceNotificationTask;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_dashboard);
        ((TextView)actionBar.getCustomView().findViewById(R.id.txt_bar_title)).setText(getString(R.string.title_activity_dashboard));
        actionBar.getCustomView().findViewById(R.id.txt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        setContentView(R.layout.activity_dashboard);
        loadBitmap(R.drawable.dash_centre, (ImageView) findViewById(R.id.img_dash_centre));
        setGridView();
        TextView announce = (TextView)findViewById(R.id.txt_latest_announce);
        announceNotificationTask = new AnnounceNotificationTask(announce);
        announceNotificationTask.execute(Constants.URL_ANNOUNCE);
    }

    public void setGridView() {
        GridView gridview = (GridView) findViewById(R.id.gv_dashboard);
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
        gridview.setAdapter(new DashboardAdapter(this, dashStrings, dashDraws));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getBaseContext(), ManageTrainingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getBaseContext(), FinancialServiceActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getBaseContext(), IntePropSearchActivity.class);
                        intent.putExtra("page", 0);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getBaseContext(), IntePropSearchActivity.class);
                        intent.putExtra("page", 1);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getBaseContext(), PropertyActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        Intent intent_quality = new Intent(getBaseContext(), QualityServiceActivity.class);
                        startActivity(intent_quality);
                        break;
                    case 6:
                        Intent intent_healthy = new Intent(getBaseContext(), HealthCheckActivity.class);
                        startActivity(intent_healthy);
                        break;
                    case 7:
                        Intent intent_interactive = new Intent(getBaseContext(), InteractCommentActivity.class);
                        startActivity(intent_interactive);
                        break;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String refresh = data.getStringExtra("login");
                if("success".equalsIgnoreCase(refresh)){
                    Intent intent = new Intent(getBaseContext(), MainMemberActivity.class);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                }
            }
        }
    }

    public void onClickAnnounce(View view){
        Intent intent_announcement = new Intent(getBaseContext(), AnnounceCentreActivity.class);
        startActivity(intent_announcement);
    }

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, this);
        task.execute(resId);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.lbl_click_again_exit), Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }
}
