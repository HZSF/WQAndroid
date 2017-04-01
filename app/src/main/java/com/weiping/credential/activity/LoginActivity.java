package com.weiping.credential.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weiping.credential.tasks.AuthTask;

import platform.tyk.weping.com.weipingplatform.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_login));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_login);
    }

    public void onClickLogin(View view){
        String username_submitted = ((EditText)findViewById(R.id.et_login_username)).getText().toString();
        String password_submitted = ((EditText)findViewById(R.id.et_login_password)).getText().toString();
        if(null == username_submitted || "".equalsIgnoreCase(username_submitted)){
            return;
        }
        if(null == password_submitted || "".equalsIgnoreCase(password_submitted)){
            return;
        }
        AuthTask authTask = new AuthTask(this, username_submitted, password_submitted);
        authTask.startTask();
    }

    public void onClickSignUpPage(View view){
        Intent intent_sign_up = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(intent_sign_up);
    }

    public void onClickActionBarBack(View view){
        Intent intent = new Intent();
        intent.putExtra("login", "back");
        setResult(Activity.RESULT_CANCELED, intent);
        onBackPressed();
    }


}
