package com.donut.app.mvp.spinOff.plans;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.donut.app.R;
import com.donut.app.databinding.FragmentSpinOffPlansItemBinding;
import com.donut.app.http.message.spinOff.ExclusivePlan;

import java.util.List;

/**
 * Created by Qi on 2017/6/5.
 * Description :
 */
public class SpinOffPlansListAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void onStarDetailClick(ExclusivePlan detail);

        void onPlayClick(ExclusivePlan detail);

        void onLikeClick(ExclusivePlan detail);

        void onCommentClick(ExclusivePlan detail);

        void onShareClick(ExclusivePlan detail);
    }

    private final List<ExclusivePlan> mDetails;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public SpinOffPlansListAdapter(List<ExclusivePlan> items,
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

        FragmentSpinOffPlansItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_spin_off_plans_item, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            bindingHolder.binding.setHandler(this);
            final ExclusivePlan detail = mDetails.get(position);
            bindingHolder.binding.setDetail(detail);
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

    public void onStarDetailClick(ExclusivePlan detail) {
        mListener.onStarDetailClick(detail);
    }

    public void onPlayClick(ExclusivePlan detail) {
        mListener.onPlayClick(detail);
    }

    public void onLikeClick(ExclusivePlan detail) {
        mListener.onLikeClick(detail);
    }

    public void onCommentClick(ExclusivePlan detail) {
        mListener.onCommentClick(detail);
    }

    public void onShareClick(ExclusivePlan detail) {
        mListener.onShareClick(detail);
    }

    private class BindingHolder extends RecyclerView.ViewHolder {
        private FragmentSpinOffPlansItemBinding binding;

        private BindingHolder(View view) {
            super(view);
        }

        public FragmentSpinOffPlansItemBinding getBinding() {
            return binding;
        }

        public void setBinding(FragmentSpinOffPlansItemBinding binding) {
            this.binding = binding;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
