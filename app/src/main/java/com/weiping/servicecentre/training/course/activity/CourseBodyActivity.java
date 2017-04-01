package com.weiping.servicecentre.training.course.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.BitmapWorkerTask;
import com.weiping.common.Constants;
import com.weiping.common.StringUtility;
import com.weiping.credential.activity.LoginActivity;
import com.weiping.servicecentre.training.course.CourseBodyFetch;
import com.weiping.servicecentre.training.course.SubmitCourseRegistrationTask;

import platform.tyk.weping.com.weipingplatform.R;

public class CourseBodyActivity extends Activity {

    private CourseBodyFetch asyncFetch;
    private String search_course_id = "";
    private int categoryId;

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
        String title = intent.getExtras().getString("course_title");
        categoryId = intent.getExtras().getInt("category_id");

        setContentView(R.layout.activity_course_body);
        loadBitmap(getResources().getIdentifier("course" + categoryId, "drawable", getPackageName()), (ImageView) findViewById(R.id.image_course));
        TextView tv_course_title = (TextView)findViewById(R.id.txt_course_title);
        setCourseData(search_course_id);

        tv_course_title.setText(title);
    }

    public void setCourseData(String search_course_id){
        if(!networkConnect()){
            Toast.makeText(CourseBodyActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        asyncFetch = new CourseBodyFetch();
        asyncFetch.setMActivity(this);
        asyncFetch.getResult(search_course_id);
    }

    public void onClickListenerCourseAppoint(View view){
        EditText editText_ppl = (EditText)findViewById(R.id.et_course_appoint_ppl);
        String ppl = editText_ppl.getText().toString();
        if(StringUtility.isEmptyString(ppl)){
            return;
        }

        if(!networkConnect()){
            Toast.makeText(CourseBodyActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(Constants.IS_USER_LOGIN, false);
        if(isLoggedIn){
            SubmitCourseRegistrationTask submitCourseRegistrationTask = new SubmitCourseRegistrationTask(this, search_course_id, Integer.valueOf(ppl));
            submitCourseRegistrationTask.execute(Constants.URL_CS_TRAIN_COURSES_REGISTER);
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

    @Override
    public void onBackPressed(){
        if (asyncFetch != null)
            asyncFetch.cancel(true);

        super.onBackPressed();
    }

    public void loadBitmap(int resId, ImageView imageView){
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, CourseBodyActivity.this);
        task.execute(resId);
    }

}
