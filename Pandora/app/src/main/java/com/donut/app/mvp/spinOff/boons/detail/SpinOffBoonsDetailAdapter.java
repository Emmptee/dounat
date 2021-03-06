package com.donut.app.mvp.spinOff.boons.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.donut.app.R;
import com.donut.app.customview.ninegrid.NineGridViewWrapper;
import com.donut.app.http.message.spinOff.ExpressionPics;
import com.donut.app.utils.BindingUtils;

import java.util.List;

/**
 * Created by Qi on 2017/3/27.
 * Description :
 */
public class SpinOffBoonsDetailAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    private final List<ExpressionPics> pics;

    private OnItemClickListener mListener;

    public SpinOffBoonsDetailAdapter(List<ExpressionPics> items,
                                     OnItemClickListener listener) {
        pics = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NineGridViewWrapper view = new NineGridViewWrapper(parent.getContext());

        float mScale = parent.getContext().getResources().getDisplayMetrics().density;
        float w = parent.getContext().getResources().getDisplayMetrics().widthPixels;
        float w_h = (w - 50 * mScale) / 3;
        ViewGroup.LayoutParams params = new RecyclerView.LayoutParams((int) (w_h + 0.5f), (int) (w_h + 0.5f));
//        view.setForegroundGravity(Gravity.CENTER);
        view.setLayoutParams(params);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        final ExpressionPics detail = pics.get(position);
        String url = detail.getGifUrl();
        if (url != null && url.length() > 0
                && (url.endsWith("gif") || url.endsWith("GIF"))) {
            viewHolder.imageView.setAGif(true);
        } else {
            viewHolder.imageView.setAGif(false);
        }
        BindingUtils.loadImg(viewHolder.imageView, detail.getPicUrl());
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pics.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private NineGridViewWrapper imageView;

        public ItemViewHolder(NineGridViewWrapper view) {
            super(view);
            imageView = view;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.shape_half_rec_gray_f6);
        }
    }

}
