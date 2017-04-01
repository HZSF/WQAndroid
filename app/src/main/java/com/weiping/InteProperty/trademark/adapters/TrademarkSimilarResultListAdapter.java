package com.weiping.InteProperty.trademark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiping.InteProperty.trademark.base.TrademarkSimilarResultItem;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class TrademarkSimilarResultListAdapter extends ArrayAdapter<TrademarkSimilarResultItem> {
    private Context context;
    private ArrayList<TrademarkSimilarResultItem> itemArrayList;
    private boolean downloading = false;

    public TrademarkSimilarResultListAdapter(Context context, ArrayList<TrademarkSimilarResultItem> itemArrayList){
        super(context, R.layout.trademark_similar_result_row, itemArrayList);
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.trademark_similar_result_row, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.textView_trademark_similar_result_name);
        TextView regNumView = (TextView)rowView.findViewById(R.id.textView_trademark_similar_result_regNum);
        TextView categoryNumView = (TextView)rowView.findViewById(R.id.textView_trademark_similar_result_categoryNum);
        nameView.setText(itemArrayList.get(position).getName());
        regNumView.setText("注册号："+itemArrayList.get(position).getRegNum());
        categoryNumView.setText("第"+itemArrayList.get(position).getCategoryNum()+"类");

        return rowView;
    }

    public void setItemArrayList(ArrayList<TrademarkSimilarResultItem> items){
        itemArrayList = items;
    }
    public ArrayList<TrademarkSimilarResultItem> getItemArrayList(){
        return itemArrayList;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}
