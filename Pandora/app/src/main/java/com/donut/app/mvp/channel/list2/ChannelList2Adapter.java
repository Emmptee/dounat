package com.donut.app.mvp.channel.list2;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.donut.app.R;
import com.donut.app.databinding.ActivityChannelList2ItemLayoutBinding;
import com.donut.app.http.message.SubjectListDetail;

import java.util.List;

/**
 * Created by Qi on 2017/3/27.
 * Description :
 */
public class ChannelList2Adapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void OnItemClick(SubjectListDetail detail);

        void OnItemCollectClick(SubjectListDetail detail);

        void OnItemShareClick(SubjectListDetail detail);
    }

    private final List<SubjectListDetail> mDetails;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private long collectClickTime;

    public ChannelList2Adapter(List<SubjectListDetail> items,
                               OnItemClickListener listener,
                               View footerView) {
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

        ActivityChannelList2ItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.activity_channel_list2_item_layout, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            final SubjectListDetail detail = mDetails.get(position);
            bindingHolder.binding.setDetail(detail);
            bindingHolder.binding.getRoot()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.OnItemClick(detail);
                        }
                    });
            bindingHolder.binding.channelList2ItemCollect
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (System.currentTimeMillis() - collectClickTime < 800) {
                                return;
                            }
                            collectClickTime = System.currentTimeMillis();
                            mListener.OnItemCollectClick(detail);
                        }
                    });
            bindingHolder.binding.channelList2ItemShare
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.OnItemShareClick(detail);
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

    public class BindingHolder extends RecyclerView.ViewHolder {
        private ActivityChannelList2ItemLayoutBinding binding;

        public BindingHolder(View view) {
            super(view);
        }

        public ActivityChannelList2ItemLayoutBinding getBinding() {
            return binding;
        }

        public void setBinding(ActivityChannelList2ItemLayoutBinding binding) {
            this.binding = binding;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
