package com.donut.app.mvp.blooper.detail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.donut.app.R;
import com.donut.app.databinding.BlooperDetailItemLayoutBinding;
import com.donut.app.http.message.StarBlooperDetailResponse;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Qi on 2017/3/27.
 * Description :
 */
public class BlooperDetailAdapter extends RecyclerView.Adapter {

    public interface OnItemPlayClickListener {
        void OnPlayClick(StarBlooperDetailResponse.BlooperItem detail);
        void OnShareClick(StarBlooperDetailResponse.BlooperItem detail);
    }

    private final List<StarBlooperDetailResponse.BlooperItem> starList;

    private OnItemPlayClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public BlooperDetailAdapter(List<StarBlooperDetailResponse.BlooperItem> items,
                                OnItemPlayClickListener listener,
                                View footerView) {
        starList = items;
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

        BlooperDetailItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.blooper_detail_item_layout, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            final StarBlooperDetailResponse.BlooperItem detail = starList.get(position);
            bindingHolder.binding.setDetail(detail);
            DecimalFormat df = new DecimalFormat("#.00");
            int palyNum = detail.getBrowseTimes() / 10000;
            if(detail.getBrowseTimes() > 1000){
                bindingHolder.binding.btnBlooperDetailItemPlay.setText("播放" + df.format(palyNum) + "万次");
            }else{
                bindingHolder.binding.btnBlooperDetailItemPlay.setText("播放" + detail.getBrowseTimes() + "次");
            }
            bindingHolder.binding.btnBlooperDetailItemPlay
                    .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnPlayClick(detail);
                }
            });
            bindingHolder.binding.btnBlooperDetailItemShare
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.OnShareClick(detail);
                        }
                    });
//R.id.btn_blooper_detail_item_play;
        }
    }

    @Override
    public int getItemCount() {
        return starList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == starList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private BlooperDetailItemLayoutBinding binding;

        public BindingHolder(View view) {
            super(view);
        }

        public BlooperDetailItemLayoutBinding getBinding() {
            return binding;
        }

        public void setBinding(BlooperDetailItemLayoutBinding binding) {
            this.binding = binding;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
