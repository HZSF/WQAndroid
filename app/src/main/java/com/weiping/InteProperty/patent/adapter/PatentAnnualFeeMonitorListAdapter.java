package com.weiping.InteProperty.patent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.InteProperty.patent.model.PatentAnnualFeeMonitorListItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentAnnualFeeMonitorListAdapter extends ArrayAdapter<PatentAnnualFeeMonitorListItem> {
    private Context context;
    private ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList;

    public PatentAnnualFeeMonitorListAdapter(Context context, ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList) {
        super(context, R.layout.patent_annual_fee_monitor_list_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.patent_annual_fee_monitor_list_row, parent, false);
        TextView patentIdView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_patent_id);
        patentIdView.setText(itemArrayList.get(position).getPatentId());
        TextView patentTitleView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_title);
        patentTitleView.setText(itemArrayList.get(position).getPatentTitle());
        TextView applyDateView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_apply_date);
        applyDateView.setText(itemArrayList.get(position).getApplyDate());
        TextView expireDateView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_expire_date);
        expireDateView.setText(itemArrayList.get(position).getExpireDate());
        TextView expireStatusView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_expire_status);
        expireStatusView.setText(itemArrayList.get(position).getExpireStatus());
        TextView paymentStatusView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_payment_status);
        paymentStatusView.setText(itemArrayList.get(position).getPaymentStatus());

        return rowView;
    }

    public ArrayList<PatentAnnualFeeMonitorListItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }
}
