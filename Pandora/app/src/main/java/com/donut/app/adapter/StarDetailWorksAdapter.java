package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.subjectStar.StarWorks;

import java.util.List;

public class StarDetailWorksAdapter extends RecyclerView.Adapter {

    private final List<StarWorks> starWorks;

    public StarDetailWorksAdapter(List<StarWorks> items) {
        starWorks = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.star_detail_works_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder item = (ItemViewHolder) holder;
        final StarWorks detail = starWorks.get(position);
        Glide.with(item.ivPic.getContext())
                .load(detail.getPublicPic())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(item.ivPic);
        item.tvName.setText(detail.getName());
    }

    @Override
    public int getItemCount() {
        return starWorks.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivPic;
        public final TextView tvName;

        public ItemViewHolder(View view) {
            super(view);
            ivPic = (ImageView) view.findViewById(R.id.star_detail_moments_iv_publicPic);
            tvName = (TextView) view.findViewById(R.id.star_detail_moments_tv_name);
        }
    }
}
