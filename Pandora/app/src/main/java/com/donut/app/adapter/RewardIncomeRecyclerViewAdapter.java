package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.RewardIncomeListDetail;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 */
public class RewardIncomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private final List<RewardIncomeListDetail> presentList;

    private View footerView;

    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public RewardIncomeRecyclerViewAdapter(List<RewardIncomeListDetail> items,
                                           View footerView) {
        presentList = items;
        this.footerView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BOTTOM_TYPE) {
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
            RewardIncomeListDetail detail = presentList.get(position);
            String strAmount = "";
            DecimalFormat fnum = new DecimalFormat("#0.00");
            if (detail.getAmount() > 0) {
                strAmount = "+" + fnum.format(detail.getAmount()) ;//+ "元";
                item.tvAmount.setTextColor(item.itemView.getResources().getColor(R.color.gold2));
            }
            else {
                strAmount = fnum.format(detail.getAmount()) ;//+ "元";
                item.tvAmount.setTextColor(item.itemView.getResources().getColor(R.color.text_gray6));
            }
            item.tvAmount.setText(strAmount);
            item.tvState.setText(detail.getContentName());
            item.tvTime.setText(detail.getCreateTime());
            //dateFormat()
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

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String dateFormat(String time) {
        String formatDate = "0000-00-00 00:00";
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date date = format.parse(time);
            DateFormat strFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            formatDate = strFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }
}
