package com.weiping.servicecentre.quality.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.weiping.common.BitmapWorkerTask;
import com.weiping.servicecentre.quality.kids.activity.KidsWearInspectionActivity;
import com.weiping.servicecentre.quality.textile.activity.TextileInspectionActivity;
import com.weiping.servicecentre.quality.wood.activity.WoodInspectionActivity;

import platform.tyk.weping.com.weipingplatform.R;

public class QualityServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_service);
        loadBitmap(R.drawable.cs_quality_centre_top, (ImageView)findViewById(R.id.cs_quality_center_image));
    }

    public void onClickListenerWoodInspection(View view){
        Intent intent = new Intent(getBaseContext(), WoodInspectionActivity.class);
        startActivity(intent);
    }

    public void onClickListenerKidsWearInspection(View view){
        Intent intent = new Intent(getBaseContext(), KidsWearInspectionActivity.class);
        startActivity(intent);
    }

    public void onClickListenerTextileInspection(View view){
        Intent intent = new Intent(getBaseContext(), TextileInspectionActivity.class);
        startActivity(intent);
    }

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, QualityServiceActivity.this);
        task.execute(resId);
    }

}
