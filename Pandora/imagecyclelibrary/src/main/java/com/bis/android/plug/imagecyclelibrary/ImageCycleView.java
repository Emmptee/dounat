package com.bis.android.plug.imagecyclelibrary;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 *
 */
public class ImageCycleView extends LinearLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;
    /**
     * 滚动图片视图适配
     */
    protected PagerAdapter mAdvAdapter;
    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 0;
    /**
     * 手机密度
     */
    private float mScale;

    private boolean isStop;

    private int dotSelectedRes, dotRes;

    private ArrayList<String> imageUrlList = new ArrayList<>();

    private ImageCycleViewListener imageCycleViewListener;

    public ImageCycleView(Context context) {
        super(context);
        init(context);
    }

    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
        // 滚动图片右下指示器视
        mGroup = (ViewGroup) findViewById(R.id.circles);
        initEvent();
        initOther();
    }

    protected void initOther() {
        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageCycleViewListener);
    }

    private void initEvent() {
        mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 装填图片数据
     */
    public void setImageResources(ArrayList<String> imageUrlList,
                                  ImageCycleViewListener imageCycleViewListener) {
        mAdvPager.setAdapter(mAdvAdapter);
        stopImageTimerTask();
        this.imageUrlList.clear();
        this.imageCycleViewListener = imageCycleViewListener;
        if (imageUrlList != null && imageUrlList.size() > 0) {
            this.setVisibility(View.VISIBLE);
            this.imageUrlList.addAll(imageUrlList);
        } else {
            this.setVisibility(View.GONE);
            return;
        }
        // 清除
        mGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = imageUrlList.size();
        if (imageCount > 1) {
            mImageViews = new ImageView[imageCount];
            for (int i = 0; i < imageCount; i++) {
                //图片轮播指示图
                ImageView mImageView = new ImageView(mContext);
                int imageParams = (int) (mScale * 5 + 0.5f);// XP与DP转换，适应应不同分辨率
                LayoutParams params = new LayoutParams(imageParams, imageParams);
                if (i != 0) {
                    params.leftMargin = 15;
                }
                mImageView.setScaleType(ScaleType.CENTER_CROP);
                mImageView.setLayoutParams(params);

                mImageViews[i] = mImageView;
                if (i == 0) {
                    mImageViews[i].setImageResource(getDotSelectedRes());//BackgroundResource(R.drawable.shape_white_dot);
                } else {
                    mImageViews[i].setImageResource(getDotRes());
                }
                mGroup.addView(mImageViews[i]);
            }
        }
        if (mAdvAdapter != null) {
            mAdvAdapter.notifyDataSetChanged();
        }
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        if (imageCount > 1) {
            mAdvPager.post(new Runnable() {
                @Override
                public void run() {
                    mAdvPager.setCurrentItem((mImageViews.length) * 100, false);
                }
            });
            startImageTimerTask();
        }
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播—用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        mHandler.postDelayed(mImageTimerTask, 3000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    mHandler.postDelayed(mImageTimerTask, 3000);
                }
            }
        }
    };

    /**
     * 轮播图片监听
     *
     * @author minking
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                startImageTimerTask();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            if (mImageViews != null) {
                index = index % mImageViews.length;
                // 设置当前显示的图片
                mImageIndex = index;
                // 设置图片滚动指示器背
                mImageViews[index].setImageResource(getDotSelectedRes());
                for (int i = 0; i < mImageViews.length; i++) {
                    if (index != i) {
                        mImageViews[i].setImageResource(getDotRes());
                    }
                }
            }
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ClickableImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private ArrayList<String> mAdList = new ArrayList<String>();

        /**
         * 广告图片点击监听
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, ArrayList<String> adList,
                                 ImageCycleViewListener imageCycleViewListener) {
            this.mContext = context;
            this.mAdList = adList;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<ClickableImageView>();
        }

        @Override
        public int getCount() {
            if (mAdList.size() > 1) {
                return Integer.MAX_VALUE;
            }
            return mAdList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = mAdList.get(position % mAdList.size());
            ClickableImageView imageView;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ClickableImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ScaleType.CENTER_CROP);
            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mImageCycleViewListener != null) {
                        mImageCycleViewListener.onImageClick(position % mAdList.size(), v);
                    }
                }
            });
//            imageView.setTag(imageUrl);
            container.addView(imageView);
            if (mImageCycleViewListener != null) {
                mImageCycleViewListener.displayImage(imageUrl, imageView);
            }
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ClickableImageView view = (ClickableImageView) object;
            mAdvPager.removeView(view);
            mImageViewCacheList.add(view);
        }

    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {
        /**
         * 加载图片资源
         *
         * @param imageURL
         * @param imageView
         */
        void displayImage(String imageURL, ImageView imageView);

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        void onImageClick(int position, View imageView);
    }

    public int getDotSelectedRes() {
        return dotSelectedRes == 0 ? R.drawable.shape_white_dot : dotSelectedRes;
    }

    public void setDotSelectedRes(int dotSelectedRes) {
        this.dotSelectedRes = dotSelectedRes;
    }

    public int getDotRes() {
        return dotRes == 0 ? R.drawable.shape_main_dot : dotRes;
    }

    public void setDotRes(int dotRes) {
        this.dotRes = dotRes;
    }
}
