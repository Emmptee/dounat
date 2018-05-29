package com.donut.app.mvp.auction;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.databinding.ActivityAuctionItemBinding;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.message.auction.MyAuctionDetail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 */
public class MyAuctionAdapter extends RecyclerView.Adapter {

    private final List<MyAuctionDetail> mDetails;

    private MyAuctionContract.View mListener;

    private Context mContext;

    private View footerView;

    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    MyAuctionAdapter(Context context, List<MyAuctionDetail> items,
                     MyAuctionContract.View listener, View footerView) {
        mContext = context;
        mDetails = items;
        mListener = listener;
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

        ActivityAuctionItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.activity_auction_item, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder item = (BindingHolder) holder;
            final MyAuctionDetail detail = mDetails.get(position);
            item.binding.setDetail(detail);
            item.binding.setHandler(this);

            String strState;
            switch (detail.getAuctionLogsStatus()) {
                case 1:
                case -1:
                    strState = "竞拍成功";
                    break;
                case -2:
                    strState = "竞拍失败";
                    break;
                case 0:
                    strState = "竞拍中";
                    break;
                default:
                    strState = "";
                    break;
            }
            item.binding.auctionItemTvState.setText(strState);

            String srtFreight;
            UserInfo info = SysApplication.getUserInfo();
            if (info.getMemberStatus() == 1) {
                srtFreight = String.format(mContext.getString(R.string.auction_freight),
                        detail.getMemberFreight());
            } else {
                srtFreight = String.format(mContext.getString(R.string.auction_freight),
                        detail.getFreight());
            }
            item.binding.auctionItemTvFreight.setText(srtFreight);
            item.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(detail);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDetails.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDetails.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public void onToPayClick(MyAuctionDetail detail) {
        mListener.OnItemPayClick(detail);
    }

    public void onToCancelClick(final MyAuctionDetail detail) {
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setMessage("您确认放弃竞拍吗？")
                .setNegativeButton(mContext.getString(R.string.cancel), null)
                .setPositiveButton(mContext.getString(R.string.sure),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.OnItemCancelClick(detail);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    public void onToDeleteClick(final MyAuctionDetail detail) {
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setMessage("您确认删除吗？")
                .setNegativeButton(mContext.getString(R.string.cancel), null)
                .setPositiveButton(mContext.getString(R.string.sure),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.OnItemDeleteClick(detail);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    private static class BindingHolder extends RecyclerView.ViewHolder {
        private ActivityAuctionItemBinding binding;

        private BindingHolder(View view) {
            super(view);
        }

        public ActivityAuctionItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ActivityAuctionItemBinding binding) {
            this.binding = binding;
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}