package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.weiping.InteProperty.trademark.adapters.TrademarkIntegratedResultListAdapter;
import com.weiping.InteProperty.trademark.base.TrademarkIntegratedResultItem;
import com.weiping.InteProperty.trademark.core.Constants;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkIntegratedResultListActivity extends Activity {

    private ArrayList<TrademarkIntegratedResultItem> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_integrated_result_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_integrated_result_list);

        itemArrayList = (ArrayList<TrademarkIntegratedResultItem>)getIntent().getSerializableExtra("result_list");
        final String sort_result_num = getIntent().getStringExtra("result_sort_num");
        final String search_by = getIntent().getStringExtra("search_by");
        TrademarkIntegratedResultListAdapter adapter = new TrademarkIntegratedResultListAdapter(this, itemArrayList);
        ListView listView = (ListView)findViewById(R.id.listView_trademark_integrated_result);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<TrademarkIntegratedResultItem> trademarkList = ((TrademarkIntegratedResultListAdapter)parent.getAdapter()).getItemArrayList();

                String categoryNum = trademarkList.get(position).getCategoryNum();
                String name = trademarkList.get(position).getName();
                String link = trademarkList.get(position).getLink();
                Intent intent = new Intent(getBaseContext(), TradeMarkIntegratedPrecisionResultListActivity.class);
                intent.putExtra(Constants.SUBMIT_DATA_CATEGORYNUM, categoryNum);
                intent.putExtra(Constants.SUBMIT_DATA_SEARCHCONTENT, name);
                intent.putExtra(Constants.SUBMIT_DATA_RESULT_SORT_NUM, sort_result_num);
                intent.putExtra(Constants.SUBMIT_DATA_SEARCHMETHOD, search_by);
                intent.putExtra(Constants.SUBMIT_DATA_LINK, link);
                startActivity(intent);
            }
        });

    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}
