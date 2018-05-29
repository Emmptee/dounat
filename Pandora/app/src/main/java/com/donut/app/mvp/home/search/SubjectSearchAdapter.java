package com.donut.app.mvp.home.search;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donut.app.R;
import com.donut.app.databinding.ActivitySubjectSearchItemBinding;
import com.donut.app.databinding.ActivitySubjectSearchWishItemBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.home.WishItem;

import java.util.List;

public class SubjectSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SubjectListDetail> mSubjectList;

    private List<WishItem> mWishList;

    private SubjectSearchContract.View mListener;

    private static int SUBJECT_TYPE = 1, WISH_TYPE = 2;

    SubjectSearchAdapter(List<SubjectListDetail> subjectItems,
                         List<WishItem> wishItems,
                         SubjectSearchContract.View listener) {
        mSubjectList = subjectItems;
        mWishList = wishItems;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SUBJECT_TYPE) {
            ActivitySubjectSearchItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.activity_subject_search_item,
                    parent,
                    false);
            BindingSubjectHolder holder = new BindingSubjectHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        } else if (viewType == WISH_TYPE) {
            ActivitySubjectSearchWishItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.activity_subject_search_wish_item,
                    parent,
                    false);
            BindingWishHolder holder = new BindingWishHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BindingSubjectHolder) {
            BindingSubjectHolder subjectHolder = (BindingSubjectHolder) holder;
            final SubjectListDetail detail = mSubjectList.get(position);
//        Glide.with(mContext)
//                .load(wish.getHeadPic())
//                .centerCrop()
//                .error(R.drawable.default_header)
//                .transform(new GlideCircleTransform(mContext))
//                .into(holder.binding.wishUserHeadImg);
            subjectHolder.binding.setHandler(this);
            subjectHolder.binding.setDetail(detail);
//        holder.binding.executePendingBindings();
        } else if (holder instanceof BindingWishHolder) {
            BindingWishHolder wishHolder = (BindingWishHolder) holder;
            final WishItem detail = mWishList.get(position - mSubjectList.size());
            wishHolder.binding.setHandler(this);
            wishHolder.binding.setDetail(detail);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mSubjectList.size()) {
            return SUBJECT_TYPE;
        } else {
            return WISH_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size() + mWishList.size();
    }

    public void onSubjectItemViewClick(SubjectListDetail detail) {
        mListener.onSubjectItemClick(detail);
    }

    public void onWishItemViewClick(WishItem detail) {
        mListener.onWishItemClick(detail);
    }

    private static class BindingSubjectHolder extends RecyclerView.ViewHolder {
        private ActivitySubjectSearchItemBinding binding;

        private BindingSubjectHolder(View view) {
            super(view);
        }

        public ActivitySubjectSearchItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ActivitySubjectSearchItemBinding binding) {
            this.binding = binding;
        }
    }

    private static class BindingWishHolder extends RecyclerView.ViewHolder {
        private ActivitySubjectSearchWishItemBinding binding;

        private BindingWishHolder(View view) {
            super(view);
        }

        public ActivitySubjectSearchWishItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ActivitySubjectSearchWishItemBinding binding) {
            this.binding = binding;
        }
    }
}
