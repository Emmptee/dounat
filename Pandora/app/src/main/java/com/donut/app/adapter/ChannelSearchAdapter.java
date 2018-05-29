package com.donut.app.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donut.app.R;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivityChannelSearchItemBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.mvp.channel.search.ChannelSearchContract;

import java.util.List;

public class ChannelSearchAdapter extends RecyclerView.Adapter<ChannelSearchAdapter.BindingHolder> {

    private List<SubjectListDetail> mSubjectList;

    private ChannelSearchContract.View mListener;

    public ChannelSearchAdapter(List<SubjectListDetail> items,
                                ChannelSearchContract.View listener) {
        mSubjectList = items;
        this.mListener = listener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityChannelSearchItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.activity_channel_search_item,
                parent,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        final SubjectListDetail detail = mSubjectList.get(position);
//        Glide.with(mContext)
//                .load(wish.getHeadPic())
//                .centerCrop()
//                .error(R.drawable.default_header)
//                .transform(new GlideCircleTransform(mContext))
//                .into(holder.binding.wishUserHeadImg);

        holder.binding.setHandler(this);
        holder.binding.setDetail(detail);
//        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    public void onItemViewClick(SubjectListDetail detail) {
        mListener.onItemClick(detail);
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private ActivityChannelSearchItemBinding binding;

        public BindingHolder(View view) {
            super(view);
        }

        public ActivityChannelSearchItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ActivityChannelSearchItemBinding binding) {
            this.binding = binding;
        }
    }
}
