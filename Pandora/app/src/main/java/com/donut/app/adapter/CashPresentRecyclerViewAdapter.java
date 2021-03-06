package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.http.message.PresentListDetail;

import java.text.DecimalFormat;
import java.util.List;

/**
 */
public class CashPresentRecyclerViewAdapter extends RecyclerView.Adapter{

    private final List<PresentListDetail> presentList;

    private View footerView;

    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public CashPresentRecyclerViewAdapter(List<PresentListDetail> items,
                                          View footerView) {
        presentList = items;
        this.footerView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == BOTTOM_TYPE)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(lp);
            return new FooterViewHolder(footerView);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cash_present_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder) holder;
            PresentListDetail detail = presentList.get(position);
            DecimalFormat fnum = new DecimalFormat("#0.00");
            item.tvAmount.setText(fnum.format(detail.getAmount())+ "元");
            if (detail.getStatus() == 1) {
                item.tvState.setText(R.string.cash_present_ed);
                item.tvState.setTextColor(
                        SysApplication.getInstance().getResources().getColor(R.color.text_gray9));
                item.tvTime.setText(detail.getDealTime());
            } else {
                item.tvState.setText(R.string.cash_present_ing);
                item.tvState.setTextColor(
                        SysApplication.getInstance().getResources().getColor(R.color.text_tiffany));
                item.tvTime.setText(detail.getApplicateTime());
            }

        }
    }

    @Override
    public int getItemCount() {
        return presentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == presentList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvState, tvTime, tvAmount;

        public ItemViewHolder(View view) {
            super(view);
            tvState = (TextView) view.findViewById(R.id.cash_present_item_state);
            tvTime = (TextView) view.findViewById(R.id.cash_present_item_time);
            tvAmount = (TextView) view.findViewById(R.id.cash_present_item_amount);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder
    {
        public FooterViewHolder(View itemView)
        {
            super(itemView);
        }
    }

}
