package com.weiping.membership.quality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.membership.quality.model.AppointedInspectionListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppointedInspectionListAdapter extends ArrayAdapter<AppointedInspectionListItem> {
    private Context context;
    private ArrayList<AppointedInspectionListItem> itemArrayList;

    public AppointedInspectionListAdapter(Context context, ArrayList<AppointedInspectionListItem> itemArrayList){
        super(context, R.layout.membership_quality_appointed_inspection_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.membership_quality_appointed_inspection_list_row, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.member_appointed_inspection_list_title);
        titleView.setText(itemArrayList.get(position).getTitle());

        return rowView;
    }

    public void setItemArrayList(ArrayList<AppointedInspectionListItem> items){
        itemArrayList = items;
    }
    public ArrayList<AppointedInspectionListItem> getItemArrayList(){
        return itemArrayList;
    }
}
