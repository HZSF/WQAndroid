package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiping.common.StringUtility;
import com.weiping.credential.Common.Constants;
import com.weiping.credential.tasks.GetRegisterImgTask;
import com.weiping.credential.tasks.RegisterSubmitTask;

import platform.tyk.weping.com.weipingplatform.R;

public class RegisterActivity extends Activity {

    private GetRegisterImgTask getImgTask;
    private RegisterSubmitTask task;
    private boolean agree = false;
    private String sessionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_register));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_register);
        getImgTask = new GetRegisterImgTask(this);
        getImgTask.execute(Constants.URL_REGISTER_CODE_GET);

        ImageView img_register_code = (ImageView)findViewById(R.id.img_register_code);
        img_register_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getImgTask != null)
                    getImgTask.cancel(true);
                getImgTask = new GetRegisterImgTask(RegisterActivity.this);
                getImgTask.execute(Constants.URL_REGISTER_CODE_GET);
            }
        });
    }

    public void onClickSignUp(View view){
        ImageView img = (ImageView)findViewById(R.id.img_register_code);
        sessionid = img.getTag().toString();
        EditText editText_code = (EditText)findViewById(R.id.txt_register_code);
        String register_code = editText_code.getText().toString();
        EditText editText_username = (EditText)findViewById(R.id.et_signup_username);
        String signUp_username = editText_username.getText().toString();
        EditText editText_phone_number = (EditText)findViewById(R.id.et_signup_phone_number);
        String phone_number = editText_phone_number.getText().toString();
        EditText editText_password = (EditText)findViewById(R.id.et_signup_password);
        String signUp_password = editText_password.getText().toString();
        EditText editText_password_again = (EditText)findViewById(R.id.et_signup_password_again);
        String signUp_password_again = editText_password_again.getText().toString();
        EditText editText_company_name = (EditText)findViewById(R.id.et_signup_company_name);
        String signUp_company_name = editText_company_name.getText().toString();

        if(StringUtility.isEmptyString(signUp_username) ||
                StringUtility.isEmptyString(phone_number) ||
                StringUtility.isEmptyString(signUp_password) ||
                StringUtility.isEmptyString(signUp_password_again) ||
                StringUtility.isEmptyString(register_code) ||
                StringUtility.isEmptyString(signUp_company_name)){
            return;
        }

        if(!agree){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder
                    .setMessage("请阅读并同意《会员注册协议》！")
                    .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        if (!signUp_password.equalsIgnoreCase(signUp_password_again)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder
                    .setMessage("密码错误！")
                    .setPositiveButton(getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        task = new RegisterSubmitTask(this, signUp_username, signUp_password, phone_number, signUp_company_name, sessionid+register_code);
        task.execute(Constants.URL_REGISTER);

    }

    public void onClickSignUpAgreement(View view){
        Intent intent = new Intent(getBaseContext(), MemberAgreementActivity.class);
        startActivityForResult(intent, 2);
    }

    public void onClickActionBarBack(View view){
        if (getImgTask != null)
            getImgTask.cancel(true);
        if (task != null)
            task.cancel(true);
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String refresh = data.getStringExtra("onBack");
                if("Y".equalsIgnoreCase(refresh)){
                    onBackPressed();
                }
            }
        }
        else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                agree = true;
            }
        }
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public GetRegisterImgTask getGetImgTask() {
        return getImgTask;
    }

    public void setGetImgTask(GetRegisterImgTask getImgTask) {
        this.getImgTask = getImgTask;
    }
}
