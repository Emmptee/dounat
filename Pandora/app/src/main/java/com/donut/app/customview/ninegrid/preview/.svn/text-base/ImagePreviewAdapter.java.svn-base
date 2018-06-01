package com.donut.app.customview.ninegrid.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.donut.app.R;
import com.donut.app.customview.ninegrid.ImageInfo;
import com.donut.app.customview.ninegrid.NineGridView;

import java.util.List;


/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePreviewAdapter extends PagerAdapter {

    public interface OnItemLongClickListener {
        void onLongClick(View v);
    }

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;

    private OnItemLongClickListener longClickListener;

    public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_preview_layout, container, false);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
        final ImageView imageView = (ImageView) view.findViewById(R.id.pv);
        TextView tv = (TextView) view.findViewById(R.id.tv);

        ImageInfo info = this.imageInfo.get(position);

        showExcessPic(info, imageView);

        if (info.getDescription() != null && info.getDescription().length() > 0) {
            tv.setText(info.getDescription());
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }

        //如果需要加载的loading,需要自己改写,不能使用这个方法
//        NineGridView.getImageLoader().onDisplayImage(view.getContext(), imageView, info.bigImageUrl);

        pb.setVisibility(View.VISIBLE);

        String url = info.bigImageUrl;
        if (url != null && url.length() > 0
                && (url.endsWith("gif") || url.endsWith("GIF"))) {
            Glide.with(context).load(url)//
                    .asGif()
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .listener(new RequestListener<String, GifDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);
        } else {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);
        }


        container.addView(view);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onLongClick(v);
                }
                return true;
            }
        });

        return view;
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(ImageInfo imageInfo, ImageView imageView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null) {
            cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
        }
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.shape_half_rec_gray_f6);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    //    /** 单击屏幕关闭 */
//    @Override
//    public void onPhotoTap(View view, float x, float y) {
//        ((ImagePreviewActivity) context).finishActivityAnim();
//    }
}