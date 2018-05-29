package com.donut.app.model.galleypick.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.model.galleypick.entities.MediaEntity;

import java.util.List;

public class GallerySelectorAdapter extends RecyclerView.Adapter {

    public interface OnGalleryItemClickListener {
        void onGalleryItemClick(View v, int position);
    }

    private final int imgHeight = Resources.getSystem().getDisplayMetrics().widthPixels / 3;

    private Context mContext;
    private List<MediaEntity> mItems;

    private OnGalleryItemClickListener onItemClickListener;

    public GallerySelectorAdapter(Context mContext, List<MediaEntity> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    public void setOnItemClickListener(OnGalleryItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_selector_item_layout, parent, false);

        return new PictureSelectorHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemClick(holder, position);

        ((PictureSelectorHolder) holder).setData(mItems.get(position));
    }

    private void setItemClick(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onGalleryItemClick(v, position);
                }
            }
        });
    }

    public class PictureSelectorHolder extends RecyclerView.ViewHolder {
        ImageView imgContent;
        View layout;
        TextView tvDuration;

        public PictureSelectorHolder(View itemView) {
            super(itemView);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
            layout = itemView.findViewById(R.id.rl_duration);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            ViewGroup.LayoutParams layoutParams = imgContent.getLayoutParams();
            layoutParams.height = imgHeight;
            imgContent.setLayoutParams(layoutParams);
        }

        public void setData(MediaEntity item) {
            Glide.with(mContext)
                    .load(item.getPath())
                    .placeholder(R.drawable.galley_img_loading)
                    .error(R.drawable.default_bg)
                    .into(imgContent);
            if (item.isVideo()) {
                tvDuration.setText(("时长：" + timeParse(item.getDuration())));
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 毫秒转时分秒
     *
     * @param duration
     * @return
     */
    public String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

}
