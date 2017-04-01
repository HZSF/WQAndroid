package com.weiping.InteProperty.patent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.InteProperty.patent.activity.PtntConsultActivity;
import com.weiping.InteProperty.patent.activity.PtntISearchDetailActivity;
import com.weiping.InteProperty.patent.model.PatentAchieveTransformListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentAchieveTransformListAdapter extends PatentArrayAdapter<PatentAchieveTransformListItem> {
    private Context context;
    private ArrayList<PatentAchieveTransformListItem> itemArrayList;
    private boolean downloading = false;

    public PatentAchieveTransformListAdapter(Context context, ArrayList<PatentAchieveTransformListItem> itemArrayList) {
        super(context, R.layout.patent_achieve_transform_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.patent_achieve_transform_list_row, parent, false);
        TextView patentIdView = (TextView) rowView.findViewById(R.id.textView_patent_achieve_transform_patent_id);
        patentIdView.setText(getItem(position).getPatentId());
        TextView patentTitleView = (TextView) rowView.findViewById(R.id.textView_patent_achieve_transform_title);
        patentTitleView.setText(getItem(position).getPatentTitle());
        TextView applyDateView = (TextView) rowView.findViewById(R.id.textView_patent_achieve_transform_apply_date);
        applyDateView.setText(getItem(position).getApplyDate());
        TextView priceView = (TextView) rowView.findViewById(R.id.textView_patent_achieve_transform_price);
        priceView.setText(getItem(position).getPrice());
        rowView.findViewById(R.id.txt_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PtntISearchDetailActivity.class);
                intent.putExtra("patent_id", getItem(position).getPatentId());
                v.getContext().startActivity(intent);
            }
        });
        rowView.findViewById(R.id.patent_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PtntConsultActivity.class);
                intent.putExtra("patent_id", getItem(position).getPatentId());
                v.getContext().startActivity(intent);
            }
        });
        return rowView;
    }

    public ArrayList<PatentAchieveTransformListItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<PatentAchieveTransformListItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}
