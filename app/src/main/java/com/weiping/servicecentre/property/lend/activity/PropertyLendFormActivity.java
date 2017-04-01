package com.weiping.servicecentre.property.lend.activity;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.common.Constants;
import com.weiping.common.StringUtility;
import com.weiping.servicecentre.property.lend.asyncTasks.PropertyLendFormUploadTask;

import platform.tyk.weping.com.weipingplatform.R;

public class PropertyLendFormActivity extends Activity {
    private String area = "";
    private String category = "";
    public PropertyLendFormUploadTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_property_lend_form));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_property_lend_form);
        setSpinnerView();
    }

    public void setSpinnerView(){
        Spinner spinner_area = (Spinner)findViewById(R.id.sp_address_area);
        final ArrayAdapter<CharSequence> arrayAdapter_area = ArrayAdapter.createFromResource(this, R.array.address_area, android.R.layout.simple_spinner_item);
        arrayAdapter_area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_area.setAdapter(arrayAdapter_area);
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = arrayAdapter_area.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner_category = (Spinner)findViewById(R.id.sp_category);
        final ArrayAdapter<CharSequence> arrayAdapter_category = ArrayAdapter.createFromResource(this, R.array.property_category, android.R.layout.simple_spinner_item);
        arrayAdapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(arrayAdapter_category);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = arrayAdapter_category.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClickSubmitForm(View view){
        if (StringUtility.isEmptyString(area)){
            return;
        }
        if (StringUtility.isEmptyString(category)){
            return;
        }
        String property_area = ((EditText)findViewById(R.id.et_area)).getText().toString();
        if (StringUtility.isEmptyString(property_area)){
            return;
        }
        String levels = ((EditText)findViewById(R.id.et_levels)).getText().toString();
        if (StringUtility.isEmptyString(levels)){
            return;
        }
        String ask_price = ((EditText)findViewById(R.id.et_ask_price)).getText().toString();
        if (StringUtility.isEmptyString(ask_price)){
            return;
        }
        String description = ((EditText)findViewById(R.id.et_description)).getText().toString();
        if(!networkConnect()){
            Toast.makeText(PropertyLendFormActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        task = new PropertyLendFormUploadTask(this, area, category, property_area, levels, ask_price, description);
        task.execute(Constants.URL_PROPERTY_ADD_LEND);
    }

    public boolean networkConnect() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed(){
        if (task != null){
            task.cancel(true);
        }
        super.onBackPressed();
    }

}
