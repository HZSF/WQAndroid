package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiping.common.StringUtility;
import com.weiping.credential.Common.Constants;
import com.weiping.credential.tasks.RegisterVerifyCodeSubmitTask;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisterVerifyCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_register_verify_code));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_register_verify_code);
        ((EditText)findViewById(R.id.et_signup_username_readonly)).setText(getIntent().getStringExtra(Constants.HEAD_REG_USERNAME));
        ((EditText)findViewById(R.id.et_signup_password_readonly)).setText(getIntent().getStringExtra(Constants.HEAD_REG_PASSWORD));
        ((EditText)findViewById(R.id.et_signup_password_again_readonly)).setText(getIntent().getStringExtra(Constants.HEAD_REG_PASSWORD));
        ((EditText)findViewById(R.id.et_signup_phone_number_readonly)).setText(getIntent().getStringExtra(Constants.HEAD_REG_PHONE));
        ((EditText)findViewById(R.id.et_signup_company_name_readonly)).setText(getIntent().getStringExtra(Constants.HEAD_REG_COMNAME));
        Button btn_submit = (Button)findViewById(R.id.btn_submit_register_verify_code);
        btn_submit.setText(getResources().getString(R.string.txt_btn_submit));
    }

    public void onClickSignUpSendVerifyCode(View view){
        EditText editText_verification_code = (EditText)findViewById(R.id.et_signup_verification_code);
        String submit_verification_code = editText_verification_code.getText().toString();
        if (StringUtility.isEmptyString(submit_verification_code)){
            return;
        }
        RegisterVerifyCodeSubmitTask task = new RegisterVerifyCodeSubmitTask(this, submit_verification_code);
        task.execute(Constants.URL_REGISTER_VERIFY_CODE);
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}
