package com.donut.app.mvp.blooper;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.donut.app.R;
import com.donut.app.databinding.BlooperItemLayoutBinding;
import com.donut.app.http.message.StarListDetail;

import java.util.List;

/**
 * Created by Qi on 2017/3/27.
 * Description :
 */
public class BlooperAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener{
        void OnClick(StarListDetail detail);
    }

    private final List<StarListDetail> starList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public BlooperAdapter(List<StarListDetail> items,
                          OnItemClickListener listener,
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

        BlooperItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.blooper_item_layout, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            final StarListDetail detail = starList.get(position);
            bindingHolder.binding.setDetail(detail);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnClick(detail);
                    }
                }
            });
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
        private BlooperItemLayoutBinding binding;

        public BindingHolder(View view) {
            super(view);
        }

        public BlooperItemLayoutBinding getBinding() {
            return binding;
        }

        public void setBinding(BlooperItemLayoutBinding binding) {
            this.binding = binding;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder
    {
        public FooterViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
