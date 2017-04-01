package com.weiping.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.weiping.common.BitmapWorkerBackgroundTask;
import com.weiping.common.Constants;
import com.weiping.credential.activity.LoginActivity;
import com.weiping.membership.asyncTasks.TokenPingTask;
import com.weiping.platform.asyncTasks.CheckVersionNumTask;

import platform.tyk.weping.com.weipingplatform.R;

public class WelcomePageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        loadBitmap(R.drawable.background, (LinearLayout) findViewById(R.id.layout_welcome));
        CheckVersionNumTask task = new CheckVersionNumTask(this);
        task.execute(Constants.URL_VERSION);

        SharedPreferences sharedPreferences = getSharedPreferences(com.weiping.membership.common.Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(com.weiping.membership.common.Constants.IS_USER_LOGIN, false);
        if(isLoggedIn){
            String AUTH_SESSION_TOKEN = sharedPreferences.getString(com.weiping.membership.common.Constants.HEAD_AUTH_TOKEN, "");
            TokenPingTask tokenPingTask = new TokenPingTask(WelcomePageActivity.this, AUTH_SESSION_TOKEN);
            tokenPingTask.setIsWelcomePage(true);
            tokenPingTask.execute(com.weiping.membership.common.Constants.URL_TOKEN_VALIDATION_PING);
        }
    }

    public void onClickLogin(View view){
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onClickTryNow(View view){
        Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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

    public void loadBitmap(int resId, ViewGroup layout){
        BitmapWorkerBackgroundTask task = new BitmapWorkerBackgroundTask(layout, WelcomePageActivity.this);
        task.setX(576);
        task.setY(576);
        task.execute(resId);
    }

}
