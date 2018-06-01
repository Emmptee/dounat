package com.bis.android.plug.imagecyclelibrary;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 */
public class SubjectSnapImageCycleView extends ImageCycleView {

    private ArrayList<String> mImageUrlList;
    private ArrayList<String> mImagePeriodList;

    private ImageCycleViewListener imageCycleViewListener;

    public SubjectSnapImageCycleView(Context context) {
        super(context);
    }

    public SubjectSnapImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initOther() {
        mImageUrlList = new ArrayList<>();
        mImagePeriodList = new ArrayList<>();
        mAdvAdapter = new ImageCycleAdapter(this.getContext(), mImageUrlList, mImagePeriodList);
        super.setDotRes(R.drawable.shape_gray_dot);
    }

    /**
     * 装填图片数据
     */
    public void setImageResources(ArrayList<String> imagePeriodList, ArrayList<String> imageUrlList,
                                  ImageCycleViewListener imageCycleViewListener) {
        this.imageCycleViewListener = imageCycleViewListener;
        if (imageUrlList != null) {
            mImageUrlList.clear();
            mImageUrlList.addAll(imageUrlList);
        }
        if (imagePeriodList != null) {
            mImagePeriodList.clear();
            mImagePeriodList.addAll(imagePeriodList);
        }
        super.setImageResources(imageUrlList, imageCycleViewListener);
    }

    private class ImageCycleAdapter extends PagerAdapter {

        private final ArrayList<String> urlList;
        private final ArrayList<String> periodList;

        /**
         * 图片视图缓存列表
         */
        private ArrayList<View> mImageViewCacheList;

        private Context mContext;

        private ImageCycleAdapter(Context context, ArrayList<String> urlList,
                                  ArrayList<String> periodList) {
            this.mContext = context;
            mImageViewCacheList = new ArrayList<>();
            this.urlList = urlList;
            this.periodList = periodList;
        }

        @Override
        public int getCount() {
            if (urlList.size() > 1) {
                return Integer.MAX_VALUE;
            }
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            final int nowPosition = position % urlList.size();
            String imageUrl = urlList.get(nowPosition);
            View rootView;

            if (mImageViewCacheList.isEmpty()) {
                rootView = LayoutInflater.from(mContext).inflate(R.layout.subject_snap_img_view, container, false);
            } else {
                rootView = mImageViewCacheList.remove(0);
            }
            ViewHolder holder = null;
            if ((rootView != null) && (rootView.getTag() != null)) {
                Object tag = rootView.getTag();
                if (tag instanceof ViewHolder) {
                    holder = (ViewHolder) tag;
                }
            }

            if (holder == null) {
                holder = new ViewHolder();
                holder.mImageView = (ImageView) rootView.findViewById(R.id.ad_iv);
                holder.mPeriod = (TextView) rootView.findViewById(R.id.ad_period);
                rootView.setTag(holder);
            }
            holder.mPeriod.setText(periodList.get(nowPosition));

            // 设置点击监听
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageCycleViewListener != null) {
                        imageCycleViewListener.onImageClick(nowPosition, v);
                    }
                }
            });
            container.addView(rootView);
            if (imageCycleViewListener != null) {
                imageCycleViewListener.displayImage(imageUrl, holder.mImageView);
            }
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }

        private final class ViewHolder {
            private ImageView mImageView;
            private TextView mPeriod;
        }

    }
}
