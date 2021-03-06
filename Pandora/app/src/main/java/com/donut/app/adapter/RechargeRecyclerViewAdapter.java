package com.donut.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.RechargeEntriesResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 */
public class RechargeRecyclerViewAdapter extends RecyclerView.Adapter {

    private final List<RechargeEntriesResponse.RechargeEntries> entriesList;

    private double selectAmount = 0, inputAmount = 0, ratio=0, mqAmount = 0;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;

    private int checkPosition;

    public RechargeRecyclerViewAdapter(List<RechargeEntriesResponse.RechargeEntries> items,
                                       double ratio) {
        entriesList = items;
        this.ratio = ratio;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType ==  TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recharge_item_input_layout, parent, false);
            return new FooterViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recharge_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final RechargeEntriesResponse.RechargeEntries detail = entriesList.get(position);
            holder.tvAppMoney.setText((int)detail.getMqAmount() + "麦圈");
            holder.tvPrice.setText(
                    String.format(holder.itemView.getContext().getString(R.string.order_detail_money),
                            String.valueOf(detail.getRealAmount())));

            if (selectAmount == detail.getRealAmount()) {
                holder.cb.setChecked(true);
                this.checkPosition = position;
            } else {
                holder.cb.setChecked(false);
            }

            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectAmount = detail.getRealAmount();
                    mqAmount = detail.getMqAmount();
                    notifyDataSetChanged();
                }
            });
        } else if(viewHolder instanceof FooterViewHolder) {
            final FooterViewHolder holder = (FooterViewHolder) viewHolder;

            if (selectAmount != -1) {
                holder.cb.setChecked(false);
            }

            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.etAppMoney.requestFocus();
                }
            });

            holder.etAppMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        ((InputMethodManager) v.getContext()
                                .getSystemService(Activity.INPUT_METHOD_SERVICE)).showSoftInput(v, 0);
                        selectAmount = -1;
                        holder.cb.setChecked(true);
                        if (checkPosition >= 0) {
                            notifyItemChanged(checkPosition);
                        }
                    }
                }
            });

            holder.etAppMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            holder.etAppMoney.setText(s);
                            holder.etAppMoney.setSelection(s.length());
                        }
                    }
                    if (".".equals(s.toString().trim())) {
                        s = "0" + s;
                        holder.etAppMoney.setText(s);
                        holder.etAppMoney.setSelection(2);
                    }

                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!".".equals(s.toString().substring(1, 2))) {
                            holder.etAppMoney.setText(s.subSequence(0, 1));
                            holder.etAppMoney.setSelection(1);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (s.length() > 0) {
                            inputAmount = Double.valueOf(s.toString());
                            mqAmount = inputAmount;
                            BigDecimal b1 = new BigDecimal(inputAmount);
                            BigDecimal b2 = new BigDecimal(ratio);
                            inputAmount = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        } else {
                            inputAmount = 0;
                            mqAmount = 0;
                        }
                        holder.tvPrice.setText(String.format(
                                holder.itemView.getContext().getString(R.string.order_detail_money),
                                String.valueOf(inputAmount)));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return entriesList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == entriesList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox cb;
        public final TextView tvAppMoney, tvPrice;

        public ItemViewHolder(View view) {
            super(view);
            cb = (CheckBox) view.findViewById(R.id.recharge_item_cb);
            tvAppMoney = (TextView) view.findViewById(R.id.recharge_item_app_money);
            tvPrice = (TextView) view.findViewById(R.id.recharge_item_price);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox cb;
        public final EditText etAppMoney;
        public final TextView tvPrice;

        public FooterViewHolder(View view) {
            super(view);
            cb = (CheckBox) view.findViewById(R.id.recharge_item_input_cb);
            etAppMoney = (EditText) view.findViewById(R.id.recharge_item_input_app_money);
            tvPrice = (TextView) view.findViewById(R.id.recharge_item_input_price);
        }
    }

    public double getAmount() {
        if (selectAmount > 0) {
            return selectAmount;
        } else if (inputAmount > 0) {
            return inputAmount;
        } else {
            return 0;
        }
    }

    public void setSelectAmount(double selectAmount) {
        this.selectAmount = selectAmount;
    }

    public double getMqAmount() {
        return mqAmount;
    }
}
