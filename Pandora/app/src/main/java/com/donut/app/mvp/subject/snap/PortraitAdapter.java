package com.donut.app.mvp.subject.snap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.utils.BindingUtils;

import java.util.List;

public class PortraitAdapter extends RecyclerView.Adapter<ViewHolder> {

    interface ItemListener {
        void onItemClick(int position);
    }

    private ItemListener listener;

    private List<SubjectSnapDetailResponse.Advertisement> contentData;

    public PortraitAdapter(List<SubjectSnapDetailResponse.Advertisement> contentData,
                           ItemListener listener) {
        this.contentData = contentData;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return contentData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) viewHolder;
        SubjectSnapDetailResponse.Advertisement advertisement = contentData.get(position);
        BindingUtils.loadImg(holder.iv, advertisement.getImgUrl());
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        ImageView iv = new ImageView(viewGroup.getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                dp2px(viewGroup.getContext(), 100),
                dp2px(viewGroup.getContext(), 100));
        lp.setMargins(dp2px(viewGroup.getContext(), 10), 0, 0, 0);
        iv.setLayoutParams(lp);

        return new ItemViewHolder(iv);
    }

    private int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    static class ItemViewHolder extends ViewHolder {
        ItemViewHolder(ImageView view) {
            super(view);
            iv = view;
        }

        final ImageView iv;
    }

}
