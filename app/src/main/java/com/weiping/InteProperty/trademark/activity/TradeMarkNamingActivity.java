package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiping.InteProperty.trademark.adapters.NamingAreaCompleteAdapter;
import com.weiping.InteProperty.trademark.asyncTasks.TrademarkNamingTask;

import org.w3c.dom.Text;

import java.util.List;

import platform.tyk.weping.com.weipingplatform.R;

import static android.widget.AdapterView.*;

public class TradeMarkNamingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_naming));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_naming);
        TextView note = (TextView)findViewById(R.id.tv_trademark_naming_notice);
        TextView state = (TextView)findViewById(R.id.tv_trademark_naming_state);
        TextView statement = (TextView)findViewById(R.id.tv_trademark_naming_statement);
        note.setText(Html.fromHtml(getResources().getString(R.string.trademark_naming_notice)));
        state.setText(getResources().getString(R.string.statement));
        statement.setText(getResources().getString(R.string.trademark_naming_statement));

        setAutoCompleteTextView();
    }

    public void setAutoCompleteTextView(){
        final AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_trademark_naming_area);
        textView.setAdapter(new NamingAreaCompleteAdapter(this));
        textView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    List<String> results = ((NamingAreaCompleteAdapter)textView.getAdapter()).getAllItems();
                    if(results.indexOf(textView.getText().toString()) == -1) {
                        textView.setError("输入错误");
                    }else{
                        textView.setError(null);
                    }
                }
            }
        });
    }

    public void onClickTrademarkNaming(View view){
        AutoCompleteTextView textView_area = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_trademark_naming_area);
        String searchArea = textView_area.getText().toString();
        String areaNum;
        List<String> searhchAreaList = ((NamingAreaCompleteAdapter)textView_area.getAdapter()).getAllItems();
        if(searhchAreaList.indexOf(textView_area.getText().toString()) == -1) {
            textView_area.setError("输入错误");
            return;
        }else{
            textView_area.setError(null);
            int index = searhchAreaList.indexOf(textView_area.getText().toString());
            areaNum = ((NamingAreaCompleteAdapter)textView_area.getAdapter()).getAreaNum(index);
            if(areaNum == null){
                return;
            }
        }
        String searchWord = ((EditText)findViewById(R.id.et_trademark_naming_search_key)).getText().toString();
        if(searchWord == null || searchWord.equalsIgnoreCase("")){
            return;
        }

        if(!networkConnect()){
            Toast.makeText(TradeMarkNamingActivity.this, getResources().getString(R.string.unconnected), Toast.LENGTH_SHORT).show();
            System.out.println("Unconnected!");
            return;
        }

        TrademarkNamingTask task = new TrademarkNamingTask(this);
        task.setSearchArea(searchArea);
        task.setAreaNum(areaNum);
        task.setSearchWord(searchWord);
        task.startTask();
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
