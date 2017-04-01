package com.weiping.membership.finance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.membership.finance.model.AppliedFinancialProductListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class AppliedFinancialProductListAdapter extends ArrayAdapter<AppliedFinancialProductListItem> {
    private Context context;
    private ArrayList<AppliedFinancialProductListItem> itemArrayList;

    public AppliedFinancialProductListAdapter(Context context, ArrayList<AppliedFinancialProductListItem> itemArrayList){
        super(context, R.layout.membership_finance_applied_products_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.membership_finance_applied_products_list_row, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.member_applied_financial_products_list_title);
        titleView.setText(itemArrayList.get(position).getTitle());

        return rowView;
    }

    public void setItemArrayList(ArrayList<AppliedFinancialProductListItem> items){
        itemArrayList = items;
    }
    public ArrayList<AppliedFinancialProductListItem> getItemArrayList(){
        return itemArrayList;
    }
}
