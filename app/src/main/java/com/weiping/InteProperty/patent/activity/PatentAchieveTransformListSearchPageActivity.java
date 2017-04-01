package com.weiping.InteProperty.patent.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.weiping.InteProperty.patent.adapter.PatentAchieveTransformListAdapter;
import com.weiping.InteProperty.patent.model.PatentAchieveTransformListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentAchieveTransformListSearchPageActivity extends Activity {

    public EditText inputSearch;
    private ListView listView;
    private ArrayList<PatentAchieveTransformListItem> itemArrayList;
    private PatentAchieveTransformListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patent_achieve_transform_list_search_page);
        inputSearch = (EditText)findViewById(R.id.et_patent_achieve_transform_input_search);
        initListView();
        initInputSearch();
    }

    public void onClickPatentATCancelSearch(View view){
        onBackPressed();
    }

    private void initInputSearch(){
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }
        });
    }

    private void initListView() {
        itemArrayList = (ArrayList<PatentAchieveTransformListItem>) getIntent().getSerializableExtra("patent_list");
        adapter = new PatentAchieveTransformListAdapter(this, itemArrayList);
        listView = (ListView) findViewById(R.id.list_view_patent_achieve_transform_search);
        listView.setAdapter(adapter);
    }

}
