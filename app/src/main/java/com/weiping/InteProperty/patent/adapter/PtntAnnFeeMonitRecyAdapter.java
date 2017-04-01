package com.weiping.InteProperty.patent.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiping.InteProperty.patent.asyncTasks.PtntAnnFeeDetailTask;
import com.weiping.InteProperty.patent.base.Constants;
import com.weiping.InteProperty.patent.model.PatentAnnualFeeMonitorListItem;
import com.weiping.common.StringUtility;

import java.util.ArrayList;
import java.util.Collections;

import platform.tyk.weping.com.weipingplatform.R;

public class PtntAnnFeeMonitRecyAdapter extends RecyclerView.Adapter<PtntAnnFeeMonitRecyAdapter.ViewHolder> {

    private ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList;
    private int position;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // each data item is just a string in this case
        public CardView view;

        public ViewHolder(CardView v) {
            super(v);
            view = v;
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, 0, Menu.NONE, "取消年费监测");//groupId, itemId, order, title
            menu.add(Menu.NONE, 1, Menu.NONE, "取消代缴");
        }
    }

    public PtntAnnFeeMonitRecyAdapter(ArrayList<PatentAnnualFeeMonitorListItem> list) {
        Collections.sort(list);
        itemArrayList = list;
    }

    @Override
    public PtntAnnFeeMonitRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.patent_ann_fee_m_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        View rowView = holder.view;
        TextView patentIdView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_patent_id);
        String patent_id = itemArrayList.get(position).getPatentId();
        if (!StringUtility.isEmptyString(patent_id)) {
            if (patent_id.length() > 6) {
                if (patent_id.charAt(6) == '1') {
                    patent_id += "（发明专利）";
                } else if (patent_id.charAt(6) == '2') {
                    patent_id += "（实用新型专利）";
                } else if (patent_id.charAt(6) == '3') {
                    patent_id += "（外观专利）";
                }
            }
        }
        patentIdView.setText(patent_id);
        TextView patentTitleView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_title);
        patentTitleView.setText(itemArrayList.get(position).getPatentTitle());
        TextView applicantView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_applicant);
        applicantView.setText("申请人：" + Html.fromHtml(itemArrayList.get(position).getApplicant()));
        TextView applyDateView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_apply_date);
        applyDateView.setText(itemArrayList.get(position).getApplyDate());
        TextView expireDateView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_expire_date);
        expireDateView.setText(itemArrayList.get(position).getExpireDate());
        TextView expireStatusView = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_expire_status);
        String patent_expire_status = "";
        String expiry_status_code = itemArrayList.get(position).getExpireStatus();
        if (!StringUtility.isEmptyString(expiry_status_code)) {
            if (expiry_status_code.equalsIgnoreCase("3")) {
                patent_expire_status += "（未到期）";
                expireDateView.setTextColor(Color.parseColor("#aeadad"));
                expireStatusView.setTextColor(Color.parseColor("#aeadad"));
            } else if (expiry_status_code.equalsIgnoreCase("2")) {
                patent_expire_status += "（即将到期）";
                expireDateView.setTextColor(Color.parseColor("#f6aa07"));
                expireStatusView.setTextColor(Color.parseColor("#f6aa07"));
            } else if (expiry_status_code.equalsIgnoreCase("1")) {
                patent_expire_status += "（即将到期）";
                expireDateView.setTextColor(Color.parseColor("#fe0505"));
                expireStatusView.setTextColor(Color.parseColor("#fe0505"));
            } else if (expiry_status_code.equalsIgnoreCase("0")) {
                patent_expire_status += "（缴费期已过）";
                expireDateView.setTextColor(Color.parseColor("#aeadad"));
                expireStatusView.setTextColor(Color.parseColor("#aeadad"));
            }
        }
        expireStatusView.setText(patent_expire_status);
        TextView annual_fee = (TextView) rowView.findViewById(R.id.textView_patent_annual_fee_monitor_amount);
        String annualFee = itemArrayList.get(position).getAnnualFee();
        annual_fee.setText(annualFee);
        TextView paymentStatusView = (TextView) rowView.findViewById(R.id.btn_patent_ann_fee_monitor_apply);
        if (!StringUtility.isEmptyString(itemArrayList.get(position).getPaymentStatus())) {
            if (itemArrayList.get(position).getPaymentStatus().equalsIgnoreCase("0")) {
                paymentStatusView.setText("申请代缴");
                paymentStatusView.setTextColor(Color.parseColor("#ffffff"));
                paymentStatusView.setBackgroundResource(R.drawable.btn5);
            } else if (itemArrayList.get(position).getPaymentStatus().equalsIgnoreCase("1")) {
                paymentStatusView.setText("已申请代缴");
                paymentStatusView.setTextColor(Color.parseColor("#000000"));
                paymentStatusView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        rowView.findViewById(R.id.btn_patent_ann_fee_monitor_apply).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PatentAnnualFeeMonitorListItem item = itemArrayList.get(position);
                String patentId = item.getPatentId();
                PtntAnnFeeDetailTask task = new PtntAnnFeeDetailTask(v.getContext(), patentId);
                task.execute(Constants.URL_PATENT_DELEGATE_ANN_FEE_MON);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemArrayList == null ? 0 : itemArrayList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder){
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public ArrayList<PatentAnnualFeeMonitorListItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<PatentAnnualFeeMonitorListItem> itemArrayList) {
        Collections.sort(itemArrayList);
        this.itemArrayList = itemArrayList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
