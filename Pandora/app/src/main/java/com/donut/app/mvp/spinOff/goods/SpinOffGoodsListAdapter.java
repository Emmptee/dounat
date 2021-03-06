package com.donut.app.mvp.spinOff.goods;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.donut.app.R;
import com.donut.app.databinding.ActivityChannelList2ItemLayoutBinding;
import com.donut.app.databinding.FragmentSpinOffGoodsBinding;
import com.donut.app.databinding.FragmentSpinOffGoodsItemBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.spinOff.GoodsListDetail;

import java.util.List;

/**
 * Created by Qi on 2017/6/5.
 * Description :
 */
public class SpinOffGoodsListAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void OnItemClick(GoodsListDetail detail);
    }

    private final List<GoodsListDetail> mDetails;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public SpinOffGoodsListAdapter(List<GoodsListDetail> items,
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

        FragmentSpinOffGoodsItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_spin_off_goods_item, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            final GoodsListDetail detail = mDetails.get(position);
            bindingHolder.binding.setDetail(detail);
            bindingHolder.binding.getRoot()
                    .setOnClickListener(new View.OnClickListener() {
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

    private class BindingHolder extends RecyclerView.ViewHolder {
        private FragmentSpinOffGoodsItemBinding binding;

        private BindingHolder(View view) {
            super(view);
        }

        public FragmentSpinOffGoodsItemBinding getBinding() {
            return binding;
        }

        public void setBinding(FragmentSpinOffGoodsItemBinding binding) {
            this.binding = binding;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
