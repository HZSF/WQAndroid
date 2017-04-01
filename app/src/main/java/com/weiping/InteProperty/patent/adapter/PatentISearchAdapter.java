package com.weiping.InteProperty.patent.adapter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiping.InteProperty.patent.activity.PtntISearchDetailActivity;
import com.weiping.InteProperty.patent.asyncTasks.AnnFeeMonitorSubmitTask;
import com.weiping.InteProperty.patent.asyncTasks.PtntAchTransSubmitTask;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentIntegratedItem;
import com.weiping.common.StringUtility;

import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class PatentISearchAdapter extends RecyclerView.Adapter<PatentISearchAdapter.ViewHolder> {
    private ArrayList<PatentIntegratedItem> itemArrayList;
    private int totalItems;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View view;
        public CardViewHolderClicks mListener;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }

        public ViewHolder(View v, CardViewHolderClicks listener) {
            this(v);
            view.setOnClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            if(v instanceof CardView){
                //mListener.onCardDetail(v, getLayoutPosition());
            }
        }

        public static interface CardViewHolderClicks{
            public void onCardDetail(View view, int position);
        }
    }

    public PatentISearchAdapter(ArrayList<PatentIntegratedItem> list) {
        itemArrayList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PatentISearchAdapter.ViewHolder vh = null;
        if(viewType == TYPE_ITEM) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.patent_isearch_card, parent, false);
            vh = new ViewHolder(v, new PatentISearchAdapter.ViewHolder.CardViewHolderClicks(){
                public void onCardDetail(View view, int position){
                    //PatentIntegratedItem item = itemArrayList.get(position-1);
                    //Intent intent = new Intent(view.getContext(), PtntISearchDetailActivity.class);
                    //intent.putExtra("patent_id", item.getPatentId());
                    //view.getContext().startActivity(intent);
                }
            });
        } else if(viewType == TYPE_HEADER){
            RelativeLayout rl = (RelativeLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.isearch_header, parent, false);
            vh = new ViewHolder(rl);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position_with_head) {
        final View cardView = holder.view;
        if(cardView instanceof CardView) {
            int position = position_with_head - 1;
            TextView titleText = (TextView) cardView.findViewById(R.id.txt_patent_title);
            ArrayList<String> patentTypeList = itemArrayList.get(position).getPatentTypeList();
            StringBuilder title_builder = new StringBuilder("");
            if(patentTypeList != null){
                for(int i=0; i<patentTypeList.size(); i++){
                    title_builder.append("[");
                    title_builder.append(patentTypeList.get(i));
                    title_builder.append("] ");
                }
            }
            title_builder.append(itemArrayList.get(position).getTitle());
            title_builder.append(" - ");
            title_builder.append(itemArrayList.get(position).getPatentId());
            titleText.setText(Html.fromHtml(title_builder.toString()));

            LinearLayout ll_subtitle = (LinearLayout)cardView.findViewById(R.id.ll_patent_subtitle);
            ll_subtitle.removeAllViews();
            ArrayList<String> patentStatusList = itemArrayList.get(position).getPatentStatusList();
            if(patentStatusList != null){
                boolean showMonitorTransform = true;
                for(int i=0; i<patentStatusList.size(); i++){
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(cardView.getContext());
                    lparams.setMargins(0, 0, 5, 0);
                    textView.setLayoutParams(lparams);
                    textView.setText(patentStatusList.get(i));
                    textView.setTextSize(11);
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    if (patentStatusList.get(i).equalsIgnoreCase("失效专利")){
                        textView.setBackgroundColor(Color.parseColor("#bfc0c1"));
                        showMonitorTransform = false;
                    } else if (patentStatusList.get(i).equalsIgnoreCase("实质审查")){
                        textView.setBackgroundColor(Color.parseColor("#6194b8"));
                        showMonitorTransform = false;
                    } else{
                        textView.setBackgroundColor(Color.parseColor("#6194b8"));
                    }
                    if (showMonitorTransform){
                        cardView.findViewById(R.id.patent_isearch_ann_fee_monitor).setVisibility(View.VISIBLE);
                        cardView.findViewById(R.id.patent_isearch_transform).setVisibility(View.VISIBLE);
                    } else {
                        cardView.findViewById(R.id.patent_isearch_ann_fee_monitor).setVisibility(View.GONE);
                        cardView.findViewById(R.id.patent_isearch_transform).setVisibility(View.GONE);
                    }

                    ll_subtitle.addView(textView);
                }
            }
            TextView applicantText = (TextView) cardView.findViewById(R.id.txt_patent_applicant);
            applicantText.setText(Html.fromHtml(itemArrayList.get(position).getApplicant()));
            applicantText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatentIntegratedItem item = itemArrayList.get(position_with_head - 1);
                    String applicant = item.getApplicant();
                    //((EditText)v.findViewById(R.id.et_patent_integrated)).setText(applicant);
                }
            });

            TextView summaryText = (TextView) cardView.findViewById(R.id.txt_patent_summary);
            summaryText.setText(Html.fromHtml(itemArrayList.get(position).getSummary()));
            TextView applyDateText = (TextView) cardView.findViewById(R.id.txt_patent_apply_date);
            applyDateText.setText("申请日期：" + itemArrayList.get(position).getApplyDate());

            cardView.findViewById(R.id.patent_isearch_ann_fee_monitor).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatentIntegratedItem item = itemArrayList.get(position_with_head - 1);
                    AnnFeeMonitorSubmitTask task = new AnnFeeMonitorSubmitTask(v.getContext(), item.getPatentId(), StringUtility.cleanEmTagHtml(item.getTitle()), item.getApplyDate(), StringUtility.cleanEmTagHtml(item.getApplicant()));
                    task.execute(Constants.URL_PATENT_ADD_ANN_FEE_MON);
                }
            });
            cardView.findViewById(R.id.patent_isearch_transform).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatentIntegratedItem item = itemArrayList.get(position_with_head - 1);
                    buildPriceDialog(v, item);
                }
            });
            cardView.findViewById(R.id.patent_isearch_read_detail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatentIntegratedItem item = itemArrayList.get(position_with_head - 1);
                    Intent intent = new Intent(v.getContext(), PtntISearchDetailActivity.class);
                    intent.putExtra("patent_id", item.getPatentId());
                    v.getContext().startActivity(intent);
                }
            });
        }else if(cardView instanceof RelativeLayout){
            TextView txt_total_result = (TextView)cardView.findViewById(R.id.txt_isearch_total_result);
            txt_total_result.setText("共有" + totalItems + "条结果");
            txt_total_result.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void clear(){
        itemArrayList = new ArrayList<>();
        totalItems = 0;
    }

    private void buildPriceDialog(final View view, final PatentIntegratedItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("输入价格");
        final EditText input = new EditText(view.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton(view.getResources().getString(R.string.dialog_btn_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String price = input.getText().toString();
                if(!StringUtility.isEmptyString(price)) {
                    PtntAchTransSubmitTask task = new PtntAchTransSubmitTask(view.getContext(), item.getPatentId(), item.getTitle(), price);
                    task.execute(Constants.URL_PATENT_ADD_ACHI_TRANS);
                }
            }
        });
        builder.setNegativeButton(view.getResources().getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return itemArrayList == null ? 1 : itemArrayList.size()+1;
    }

    @Override
    public int getItemViewType(int position){
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    public ArrayList<PatentIntegratedItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<PatentIntegratedItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
