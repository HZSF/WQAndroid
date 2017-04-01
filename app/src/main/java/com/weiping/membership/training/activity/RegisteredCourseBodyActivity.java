package com.weiping.membership.training.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.credential.activity.LoginActivity;
import com.weiping.membership.common.Constants;
import com.weiping.membership.training.asyncTasks.SubmitCourseDeregistrationTask;
import com.weiping.servicecentre.training.course.CourseBodyFetch;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisteredCourseBodyActivity extends Activity {

    private CourseBodyFetch asyncFetch;
    private String search_course_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_course_body));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        search_course_id = intent.getExtras().getString("search_course_id");
        setContentView(R.layout.activity_registered_course_body);
        setCourseData(search_course_id);
    }

    public void setCourseData(String search_course_id){
        if(!networkConnect()){
            Toast.makeText(RegisteredCourseBodyActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        asyncFetch = new CourseBodyFetch();
        asyncFetch.setMActivity(this);
        asyncFetch.getResult(search_course_id);
    }

    public void onClickListenerCourseCancelAppointment(View view){
        if(!networkConnect()){
            Toast.makeText(RegisteredCourseBodyActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGIN, false);
        if(isLoggedIn){
            SubmitCourseDeregistrationTask submitCourseRegistrationTask = new SubmitCourseDeregistrationTask(this, search_course_id);
            submitCourseRegistrationTask.execute(Constants.URL_MEMBER_COURSES_DEREGISTER);
        }else{
            AlertDialog.Builder notLoginDialogBuilder = new AlertDialog.Builder(this);
            notLoginDialogBuilder
                    .setMessage("请登录！")
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog notLoginDialog = notLoginDialogBuilder.create();
            notLoginDialog.show();
        }
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}
