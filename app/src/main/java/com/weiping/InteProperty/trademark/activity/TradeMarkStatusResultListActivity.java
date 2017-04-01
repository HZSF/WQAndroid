package com.weiping.InteProperty.trademark.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.weiping.InteProperty.trademark.adapters.TrademarkStatusResultListAdapter;
import com.weiping.InteProperty.trademark.base.TrademarkStatusResultItem;
import com.weiping.InteProperty.trademark.core.Constants;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TradeMarkStatusResultListActivity extends Activity {

    private ArrayList<TrademarkStatusResultItem> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_common);
        ((TextView)actionBar.getCustomView().findViewById(R.id.textView_screen_title)).setText(getString(R.string.title_activity_trade_mark_status_result_list));
        actionBar.getCustomView().findViewById(R.id.linearLayout_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_trade_mark_status_result_list);

        itemArrayList = (ArrayList<TrademarkStatusResultItem>)getIntent().getSerializableExtra("result_list");

        final TrademarkStatusResultListAdapter adapter = new TrademarkStatusResultListAdapter(this, itemArrayList);
        final ListView listView = (ListView)findViewById(R.id.listView_trademark_status_result);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<TrademarkStatusResultItem> trademarkList = ((TrademarkStatusResultListAdapter)listView.getAdapter()).getItemArrayList();

                String categoryNum = trademarkList.get(position).getCategoryNum();
                String regNum = trademarkList.get(position).getRegNum();
                String name = trademarkList.get(position).getName();
                Intent intent = new Intent(getBaseContext(), TradeMarkStatusDetailActivity.class);
                intent.putExtra(Constants.JSON_NAME_CATEGORY_NUM, categoryNum);
                intent.putExtra(Constants.JSON_NAME_REG_NUM, regNum);
                intent.putExtra(Constants.JSON_NAME_NAME, name);
                startActivity(intent);
            }
        });
    }

    public void onClickActionBarBack(View view){
        onBackPressed();
    }

}
