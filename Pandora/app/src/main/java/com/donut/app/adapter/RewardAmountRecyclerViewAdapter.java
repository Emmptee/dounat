package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.donut.app.R;
import com.donut.app.SysApplication;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class RewardAmountRecyclerViewAdapter extends RecyclerView.Adapter {

    private final List<Integer> dataList;

    private int amount;

    private EditText etAmount;

    public RewardAmountRecyclerViewAdapter(EditText etAmount) {
        this.etAmount = etAmount;
        dataList = new ArrayList<>();
        setData();
    }

    private void setData() {
        dataList.add(66);
        dataList.add(88);
        dataList.add(168);
        dataList.add(188);
        dataList.add(520);
        dataList.add(1314);
        this.amount = 1314;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_amount_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder item = (ItemViewHolder) holder;

        final int amount = dataList.get(position);
        if (getAmount() == amount) {
            item.cb.setChecked(true);
        } else {
            item.cb.setChecked(false);
        }
        item.cb.setText(amount + SysApplication.getInstance().getString(R.string.app_money));
        item.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                if (!c.isChecked()) {
                    setAmount(0);
                } else {
                    setAmount(amount);
                    etAmount.setText("");
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox cb;

        public ItemViewHolder(View view) {
            super(view);
            cb = (CheckBox) view.findViewById(R.id.reward_amount_item_cb);
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
