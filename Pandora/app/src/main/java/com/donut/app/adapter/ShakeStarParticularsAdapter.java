package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.ParticularsRequest;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.utils.L;

import java.util.List;

/**
 * Created by hard on 2018/1/29.
 */

public class ShakeStarParticularsAdapter extends RecyclerView.Adapter<ShakeStarParticularsAdapter.Shake_starViewHolder> {

    private List<ParticularsResponse.ShakingStarListBean> list;
    private Context mContext;
    private ItemOnClickListener itemOnClickListener;
    public ShakeStarParticularsAdapter(List<ParticularsResponse.ShakingStarListBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public Shake_starViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.particulars_item, parent, false);
        final Shake_starViewHolder viewHolder = new Shake_starViewHolder(view);
        viewHolder.video_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                itemOnClickListener.onClick(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Shake_starViewHolder holder, int position) {
        L.e("TAG",list.get(position).getVideoThumbnail());
        Glide.with(mContext).load(list.get(position).getVideoThumbnail()).into(holder.video_img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Shake_starViewHolder extends RecyclerView.ViewHolder{
        private ImageView video_img ;


        public Shake_starViewHolder(View itemView) {
            super(itemView);
            video_img=itemView.findViewById(R.id.xq_img);

        }

    }

    public interface ItemOnClickListener{
        void onClick(int position);
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
