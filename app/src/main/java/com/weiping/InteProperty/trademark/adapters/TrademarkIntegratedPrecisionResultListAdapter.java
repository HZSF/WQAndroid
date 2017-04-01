package com.weiping.InteProperty.trademark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.InteProperty.trademark.base.TrademarkIntegratedResultItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkIntegratedPrecisionResultListAdapter extends ArrayAdapter<TrademarkIntegratedResultItem> {
    private Context context;
    private ArrayList<TrademarkIntegratedResultItem> itemArrayList;

    public TrademarkIntegratedPrecisionResultListAdapter(Context context, ArrayList<TrademarkIntegratedResultItem> itemArrayList){
        super(context, R.layout.trademark_integrated_precision_result_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.trademark_integrated_precision_result_row, parent, false);

        TextView nameView = (TextView) rowView.findViewById(R.id.textView_trademark_integrated_precision_result_name);
        TextView applicantView = (TextView) rowView.findViewById(R.id.textView_trademark_integrated_precision_result_applicant);
        TextView regNumView = (TextView)rowView.findViewById(R.id.textView_trademark_integrated_precision_result_regNum);
        TextView categoryNumView = (TextView)rowView.findViewById(R.id.textView_trademark_integrated_precision_result_categoryNum);
        nameView.setText(itemArrayList.get(position).getName());
        applicantView.setText("申请人：" + itemArrayList.get(position).getApplicant());
        regNumView.setText("注册号："+itemArrayList.get(position).getRegNum());
        categoryNumView.setText("第"+itemArrayList.get(position).getCategoryNum()+"类");

        return rowView;
    }

    public void setItemArrayList(ArrayList<TrademarkIntegratedResultItem> items){
        itemArrayList = items;
    }
    public ArrayList<TrademarkIntegratedResultItem> getItemArrayList(){
        return itemArrayList;
    }
}
