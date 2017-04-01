package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weiping.credential.Common.Constants;
import com.weiping.credential.tasks.GetUserInfoTask;

import platform.tyk.weping.com.weipingplatform.R;

public class UserInfoFormActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_user_info_form));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_user_info_form);
        GetUserInfoTask task = new GetUserInfoTask(this);
        task.execute(Constants.URL_GET_USER_INFO);
    }

    public void onClickUpdImg(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoUpdPortraitActivity.class);
        startActivity(intent);
    }

    public void onClickUpdArea(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoUpdAreaActivity.class);
        startActivity(intent);
    }

    public void onClickUpdContactName(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoUpdateActivity.class);
        String contact_name = ((TextView)findViewById(R.id.member_contact_name)).getText().toString();
        intent.putExtra("field_name", Constants.JSON_CONTACT_NAME);
        intent.putExtra("field_value", contact_name);
        startActivity(intent);
    }

    public void onClickUpdEmail(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoUpdateActivity.class);
        String email = ((TextView)findViewById(R.id.member_email)).getText().toString();
        intent.putExtra("field_name", Constants.JSON_EMAIL);
        intent.putExtra("field_value", email);
        startActivity(intent);
    }

    public void onClickUpdCompanyName(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoUpdateActivity.class);
        String company_name = ((TextView)findViewById(R.id.member_company_name)).getText().toString();
        intent.putExtra("field_name", Constants.JSON_COMPANY_NAME);
        intent.putExtra("field_value", company_name);
        startActivity(intent);
    }

    public void onClickUpdCompanyAddr(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoUpdateActivity.class);
        String company_addr = ((TextView)findViewById(R.id.member_company_address)).getText().toString();
        intent.putExtra("field_name", Constants.JSON_COMPANY_ADDR);
        intent.putExtra("field_value", company_addr);
        startActivity(intent);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}
