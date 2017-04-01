package com.weiping.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.weiping.common.BitmapWorkerBackgroundTask;
import com.weiping.common.Constants;
import com.weiping.platform.asyncTasks.CheckVersionNumTask;

import platform.tyk.weping.com.weipingplatform.R;

public class IntroPageActivity extends Activity {
    private BitmapWorkerBackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);
        loadBitmap(R.drawable.intro_background, (LinearLayout) findViewById(R.id.intro_page));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (task != null){
                    task.cancel(true);
                    Intent mInHome = new Intent(getBaseContext(), WelcomePageActivity.class);
                    startActivity(mInHome);
                    finish();
                    System.exit(0);
                }
            }
        }, 3000);
    }
    public void loadBitmap(int resId, ViewGroup layout){
        task = new BitmapWorkerBackgroundTask(layout, IntroPageActivity.this);
        task.setX(800);
        task.setY(800);
        task.execute(resId);
    }
}
