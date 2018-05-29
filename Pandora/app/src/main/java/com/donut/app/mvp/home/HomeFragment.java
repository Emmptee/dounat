package com.donut.app.mvp.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.WebAdActivity;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.FragmentHomeLayoutBinding;
import com.donut.app.databinding.HomeBottomImgViewBinding;
import com.donut.app.databinding.HomeSubjectItemLayoutBinding;
import com.donut.app.http.message.AppChannel;
import com.donut.app.http.message.SubjectHomePageResponse;
import com.donut.app.http.message.shakestar.Channel;
import com.donut.app.http.message.shakestar.SweetResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.blooper.BlooperActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.mvp.home.search.SubjectSearchActivity;
import com.donut.app.mvp.notice.NoticeActivity;
import com.donut.app.mvp.spinOff.SpinOffActivity;
import com.donut.app.utils.BindingUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class HomeFragment extends MVPBaseFragment<FragmentHomeLayoutBinding, HomePresenter>
        implements HomeContract.View {

    private ViewGroup mGroup;

    private ImageView[] mImageViews = null;

    private boolean isStop;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView() {
        mGroup = mViewBinding.homeCircles;
        mViewBinding.setHandler(this);

        int h = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams params = mViewBinding.homeBottomImgs.getLayoutParams();
        params.height = h * 200 / 1334;
        mViewBinding.homeBottomImgs.setLayoutParams(params);

    }

    @Override
    protected void initEvent() {
        mViewBinding.homeViewPager.addOnPageChangeListener(new GuidePageChangeListener());
        mViewBinding.homeViewPager.setOnTouchListener(new View.OnTouchListener() {
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
        mViewBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(SubjectSearchActivity.class);
            }
        });
    }

    @Override
    public void loadData() {
        mPresenter.loadData(true);
    }

    @Override
    public void showView(SubjectHomePageResponse detail) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(detail.getHpSubjectList());
        mViewBinding.homeViewPager.setAdapter(adapter);
        float mScale = getContext().getResources().getDisplayMetrics().density;
        if (detail.getHpSubjectList().size() > 1) {
            mImageViews = new ImageView[detail.getHpSubjectList().size()];
            for (int i = 0; i < detail.getHpSubjectList().size(); i++) {
                //图片轮播指示图
                ImageView mImageView = new ImageView(mContext);
                int imageParams = (int) (mScale * 5 + 0.5f);// XP与DP转换，适应应不同分辨率
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageParams, imageParams);
                if (i != 0) {
                    params.leftMargin = 15;
                }
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setLayoutParams(params);

                mImageViews[i] = mImageView;
                if (i == 0) {
                    mImageViews[i].setImageResource(R.drawable.shape_home_bottom_dot);
                } else {
                    mImageViews[i].setImageResource(R.drawable.shape_home_bottom_dot_normal);
                }
                mGroup.addView(mImageViews[i]);
            }
            mViewBinding.homeViewPager.setCurrentItem((mImageViews.length) * 100);
            startImageTimerTask();
        }

        BottomContentAdapter bottomContentAdapter
                = new BottomContentAdapter(detail.getMoreContentList(), new BottomContentAdapter.ItemListener() {
            @Override
            public void onItemClick(int position, SubjectHomePageResponse.MoreContent moreContent) {
                if (moreContent == null) {
                    return;
                }
                AppChannel channel = new AppChannel();
                channel.setUuid(moreContent.getSkipId());
                channel.setName(moreContent.getTypeName());

                switch (moreContent.getType()) {
                    case 0:
                    case 1:
                        if (!TextUtils.isEmpty(moreContent.getSkipId())) {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebAdActivity.TITLE, moreContent.getTypeName());
                            bundle.putString(WebAdActivity.SKIP_ADDRESS, moreContent.getSkipId());
                            launchActivity(WebAdActivity.class, bundle);
                        }
                        break;
                    case 2:
                    case 3:
                    case 4:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(SpinOffActivity.FROM_HOME_BOTTOM, true);
                        bundle.putInt(SpinOffActivity.SPIN_OFF_TYPE, moreContent.getType());
                        launchActivity(SpinOffActivity.class, bundle);
                        break;
                    case 5:
                        launchActivity(BlooperActivity.class);
                        break;
                    case 6:
                        Bundle blooperBundle = new Bundle();
                        blooperBundle.putString(BlooperDetailActivity.STAR_ID, moreContent.getSkipId());
                        launchActivity(BlooperDetailActivity.class, blooperBundle);
                        break;
                    case 7:
                        launchActivity(NoticeActivity.class);
                        break;
                    case 8:
                        Bundle reportBundle = new Bundle();
                        reportBundle.putString(H5WebActivity.URL, "file:///android_asset/www/my_report.html");
                        launchActivity(H5WebActivity.class, reportBundle);
                        break;
                    case 9:
                        channel.setType(0);
                        GotoChannelUtils.GotoList(HomeFragment.this, channel, 0);
                        break;
                    case 10:
                        channel.setType(1);
                        GotoChannelUtils.GotoList(HomeFragment.this, channel, 0);
                        break;
                    case 13:
                        channel.setType(2);
                        GotoChannelUtils.GotoList(HomeFragment.this, channel, 0);
                        break;
                    case 15:
                        GotoChannelUtils.GotoSubjectDetail(HomeFragment.this, 0, moreContent.getSkipId(), 0);
                        break;
                    case 16:
                        GotoChannelUtils.GotoSubjectDetail(HomeFragment.this, 1, moreContent.getSkipId(), 0);
                        break;
                    case 17:
                        GotoChannelUtils.GotoSubjectDetail(HomeFragment.this, 2, moreContent.getSkipId(), 0);
                        break;
                    case 11:
                    case 12:
                    case 14:
                    default:
                        showToast(Constant.DEFAULT_TIPS_MSG);
                        break;
                }
            }
        });
        RecyclerView view = mViewBinding.homeBottomImgs;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        view.setLayoutManager(manager);
        view.setNestedScrollingEnabled(false);
        view.setHasFixedSize(false);
        view.setAdapter(bottomContentAdapter);
    }

    @Override
    public void showList(SweetResponse sweetResponse) {

    }

    @Override
    public void gotoChannelDetail(int channelType, String subjectId) {
        GotoChannelUtils.GotoSubjectDetail(this, channelType, subjectId, 0);
    }

    @Override
    public void showChannelList(Channel channel) {}

    @Override
    public void onToVideoClick() {
//        Bundle bundle_video = new Bundle();
//        bundle_video.putString(H5WebActivity.URL, "file:///android_asset/www/my_video.html");
//        launchActivity(H5WebActivity.class, bundle_video);

        launchActivity(BlooperActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewBinding.homeViewPager.getCurrentItem() > 1) {
            startImageTimerTask();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopImageTimerTask();
    }

    private void startImageTimerTask() {
        if (mViewBinding.homeViewPager.getCurrentItem() <= 1) {
            return;
        }
        stopImageTimerTask();
        isStop = false;
        mHandler.postDelayed(mImageTimerTask, 3000);
    }

    private void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mViewBinding.homeViewPager.setCurrentItem(mViewBinding.homeViewPager.getCurrentItem() + 1);
                if (!isStop) {
                    mHandler.postDelayed(mImageTimerTask, 3000);
                }
            }
        }
    };

    private class ViewPagerAdapter extends PagerAdapter {

        private final List<HomeSubjectItemLayoutBinding> mViewCacheList;
        private final List<SubjectHomePageResponse.HpSubject> hpSubjectList;

        private ViewPagerAdapter(List<SubjectHomePageResponse.HpSubject> hpSubjectList) {
            mViewCacheList = new ArrayList<>();
            this.hpSubjectList = hpSubjectList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            HomeSubjectItemLayoutBinding binding;
            final int nowPosition = position % hpSubjectList.size();

            final SubjectHomePageResponse.HpSubject hpSubject = hpSubjectList.get(nowPosition);

            if (mViewCacheList.isEmpty()) {
                binding = DataBindingUtil.inflate(
                        LayoutInflater.from(mContext),
                        R.layout.home_subject_item_layout,
                        container, false);
                //LayoutInflater.from(mContext).inflate(R.layout.home_subject_item_layout, container, false);
            } else {
                binding = mViewCacheList.remove(0);
            }

            BindingUtils.loadImg(binding.subjectItemPlaybill, hpSubject.getPublicPic());
            binding.setHpSubject(hpSubject);

            // 设置点击监听
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoChannelDetail(hpSubject.getChannelType(), hpSubject.getSubjectId());
                }
            });
            container.addView(binding.getRoot());
            return binding.getRoot();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            View view = (View) object;
            container.removeView(view);
            HomeSubjectItemLayoutBinding binding = DataBindingUtil.bind(view);
            mViewCacheList.add(binding);
        }

        @Override
        public int getCount() {
            if (hpSubjectList.size() > 1) {
                return Integer.MAX_VALUE;
            }
            return hpSubjectList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
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
            index = index % mImageViews.length;
            mImageViews[index].setImageResource(R.drawable.shape_home_bottom_dot);
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i].setImageResource(R.drawable.shape_home_bottom_dot_normal);
                }
            }
        }
    }

    private static class BottomContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        interface ItemListener {
            void onItemClick(int position, SubjectHomePageResponse.MoreContent moreContent);
        }

        private ItemListener listener;

        private List<SubjectHomePageResponse.MoreContent> contentData;

        private BottomContentAdapter(List<SubjectHomePageResponse.MoreContent> contentData,
                                     ItemListener listener) {
            this.contentData = contentData;
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return contentData.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final SubjectHomePageResponse.MoreContent moreContent = contentData.get(position);

            Glide.with(holder.getBinding().getRoot().getContext())
                    .load(moreContent.getPicUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .into(holder.binding.adIv);
            holder.binding.imgTitle.setText(moreContent.getTitle());
            holder.binding.imgDescription.setText(moreContent.getDescription());
            holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(holder.getAdapterPosition(), moreContent);
                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeBottomImgViewBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.home_bottom_img_view, parent, false);
            ItemViewHolder holder = new ItemViewHolder(binding.getRoot());
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) binding.getRoot().getLayoutParams();
            lp.width = parent.getContext().getResources().getDisplayMetrics().widthPixels * 530 / 750;
            binding.getRoot().setLayoutParams(lp);
            holder.setBinding(binding);
            return holder;
        }

        static class ItemViewHolder extends RecyclerView.ViewHolder {
            private HomeBottomImgViewBinding binding;

            public ItemViewHolder(View view) {
                super(view);
            }

            public HomeBottomImgViewBinding getBinding() {
                return binding;
            }

            public void setBinding(HomeBottomImgViewBinding binding) {
                this.binding = binding;
            }
        }

    }

}
