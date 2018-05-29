package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.subjectStar.StarPlan;

import java.util.List;

public class StarDetailMomentsAdapter extends RecyclerView.Adapter {

    private final List<StarPlan> starMoments;

    public StarDetailMomentsAdapter(List<StarPlan> items) {
        starMoments = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.star_detail_moments_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder item = (ItemViewHolder) holder;
        final StarPlan detail = starMoments.get(position);
        item.tvTime.setText(detail.getCreateTime());
        item.tvContent.setText(detail.getContent());
    }

    @Override
    public int getItemCount() {
        return starMoments.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvTime, tvContent;

        public ItemViewHolder(View view) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.star_detail_moments_tv_time);
            tvContent = (TextView) view.findViewById(R.id.star_detail_moments_tv_content);
        }
    }
}
