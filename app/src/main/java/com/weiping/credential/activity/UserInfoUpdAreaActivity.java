package com.weiping.credential.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.weiping.common.Constants;
import com.weiping.credential.tasks.GetCityTask;
import com.weiping.credential.tasks.GetProvinceTask;
import com.weiping.credential.tasks.UdpUserInfoAreaTask;

import java.util.ArrayList;
import java.util.List;

import platform.tyk.weping.com.weipingplatform.R;

public class UserInfoUpdAreaActivity extends Activity {
    private boolean upBtn = false;
    private GetProvinceTask provinceTask;
    private GetCityTask cityTask;
    private UdpUserInfoAreaTask udpUserInfoAreaTask;
    private int province_chose = 0;
    private int city_chose = 0;
    private int[] city_index;

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
                    NavUtils.navigateUpFromSameTask(UserInfoUpdAreaActivity.this);
                else
                    onBackPressed();
            }
        });
        setContentView(R.layout.activity_user_info_upd_area);
        loadSpinner();
    }

    public void loadSpinner(){
        Spinner provinceSpinner = (Spinner)findViewById(R.id.sp_province);
        List<String> array_province = new ArrayList<>();
        array_province.add("请选择省份");
        ArrayAdapter<String> arrayAdapter_p = new ArrayAdapter<String>(this, R.layout.spinner_item1, array_province);
        provinceSpinner.setAdapter(arrayAdapter_p);
        provinceTask = new GetProvinceTask(this, provinceSpinner);
        provinceTask.execute(Constants.URL_GET_PROVINCE);
        Spinner citySpinner = (Spinner) findViewById(R.id.sp_city);
        List<String> array_city = new ArrayList<>();
        array_city.add("请选择城市");
        ArrayAdapter<String> arrayAdapter_c = new ArrayAdapter<String>(this, R.layout.spinner_item1, array_city);
        citySpinner.setAdapter(arrayAdapter_c);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("province position", String.valueOf(position));
                province_chose = position;
                if (position > 0) {
                    cityTask = new GetCityTask(UserInfoUpdAreaActivity.this, (Spinner) findViewById(R.id.sp_city), position);
                    cityTask.execute(Constants.URL_GET_CITY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("city position", String.valueOf(position));
                city_chose = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClickUpdInfoField(View view){
        udpUserInfoAreaTask = new UdpUserInfoAreaTask(this, province_chose, city_index[city_chose-1]);
        udpUserInfoAreaTask.execute(Constants.URL_UPD_USER_INFO_AREA);
        upBtn = true;
    }

    @Override
    public void onBackPressed(){
        if (provinceTask != null){
            provinceTask.cancel(true);
        }
        if (cityTask != null){
            cityTask.cancel(true);
        }
        super.onBackPressed();
    }

    public int[] getCity_index() {
        return city_index;
    }

    public void setCity_index(int[] city_index) {
        this.city_index = city_index;
    }
}
