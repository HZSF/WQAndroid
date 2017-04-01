package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weiping.common.StringUtility;
import com.weiping.credential.Common.Constants;
import com.weiping.credential.tasks.UpdUserInfoTask;

import platform.tyk.weping.com.weipingplatform.R;

public class UserInfoUpdateActivity extends Activity {

    private boolean upBtn = false;
    private String field_name;
    private String field_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_user_info_update));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upBtn)
                    NavUtils.navigateUpFromSameTask(UserInfoUpdateActivity.this);
                else
                    onBackPressed();
            }
        });
        setContentView(R.layout.activity_user_info_update);
        field_name = getIntent().getStringExtra("field_name");
        field_value = getIntent().getStringExtra("field_value");
        EditText et = (EditText) findViewById(R.id.et_update_info_field);
        et.setText(field_value);
        et.setSelection(et.getText().length());
    }

    public void onClickUpdInfoField(View view){
        EditText et = (EditText) findViewById(R.id.et_update_info_field);
        String value = et.getText().toString();
        if(!StringUtility.isEmptyString(value) && !value.equalsIgnoreCase(field_value)) {
            UpdUserInfoTask task = new UpdUserInfoTask(this, field_name, value);
            task.execute(Constants.URL_UPD_USER_INFO);
            upBtn = true;
        }
    }
}
