package com.donut.app.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.WebAdActivity;
import com.donut.app.adapter.HomeLeftAdapter;
import com.donut.app.adapter.HomeListRecyclerAdapter;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.WeetwheatcirclesFragmentBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.AppChannel;
import com.donut.app.http.message.SubjectHomePageResponse;
import com.donut.app.http.message.shakestar.Channel;
import com.donut.app.http.message.shakestar.SweetResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.blooper.BlooperActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.mvp.channel.list.ChanneListPageActivity;
import com.donut.app.mvp.channel.list2.ChannelList2Activity;
import com.donut.app.mvp.notice.NoticeActivity;
import com.donut.app.mvp.spinOff.SpinOffActivity;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by hard on 2018/2/27.
 */

public class SweetWheatCirclesFragment extends MVPBaseFragment<WeetwheatcirclesFragmentBinding, HomePresenter> implements HomeContract.View {

    public boolean searchVisible;


    private LinearLayoutManager linearLayoutManager;
    private PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
    private HomeListRecyclerAdapter homeListRecyclerAdapter;
    private static final int TYPEA = 0, TYPEB = 1, TYPEC = 2;
    private Bitmap bmp;
    private ShareBuilderCommonUtil.Builder builder;
    private PopupWindow popup;
    private View view;
    private RecyclerView leftRecycler;
    private HomeLeftAdapter homeLeftAdapter;

    @Override
    public void onToVideoClick() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.weetwheatcircles_fragment;
    }

    @Override
    protected void initView() {

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        view = View.inflate(getActivity(), R.layout.home_left_popup, null);
        popup = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, height);
        //设置背景
        popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置焦点
        popup.setFocusable(true);
        //可触摸
        popup.setTouchable(true);
        //窗口关闭事件
//        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mViewBinding.homeRecy.setLayoutManager(linearLayoutManager);
        mViewBinding.homeLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = NetUtils.isNetworkConnected(getActivity());
                if (b) {
                    mPresenter.loadChannelDataList(false);
                    popup.showAtLocation(mViewBinding.homeLeftImg, Gravity.TOP, 0, 0);
                } else {
                    ToastUtil.show(getActivity(), "当前网络不佳", 0);
                }
//                popup.showAsDropDown(mViewBinding.homeLeftImg,0,0);
            }
        });

        mViewBinding.homeSousuo.setVisibility(searchVisible ? View.VISIBLE : View.GONE);
        mViewBinding.homeSousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(BlooperActivity.class);
            }
        });
        view.findViewById(R.id.left_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        leftRecycler = view.findViewById(R.id.left_recycler);
        leftRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initEvent() {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        builder = new ShareBuilderCommonUtil.Builder(getActivity());
    }

    @Override
    protected void loadData() {
        mPresenter.loadDataList(false);
    }


    @Override
    public void showList(SweetResponse sweetResponse) {

        homeListRecyclerAdapter = new HomeListRecyclerAdapter(getActivity(), sweetResponse.getHpContentList());
        mViewBinding.homeRecy.setAdapter(homeListRecyclerAdapter);
        pagerSnapHelper.attachToRecyclerView(mViewBinding.homeRecy);
        onClick();
    }

    private void onClick() {
        homeListRecyclerAdapter.setOnItemOnClick(new HomeListRecyclerAdapter.OnItemOnClick() {
            @Override
            public void OnClick(int channelType, String subjectId) {//根据返回值不同，跳转的页面不同
                GotoChannelUtils.GotoSubjectDetail(getActivity(), channelType, subjectId);
            }
        });
        homeListRecyclerAdapter.setOnItenShareClick(new HomeListRecyclerAdapter.OnItenShareClick() {
            @Override
            public void OnShareClick(int type, String name, String subjectId, int browseTimes, String description) {//分享
                Share(type, name, subjectId, browseTimes, description);
            }
        });

    }

    public void Share(int type, String name, String subjectId, int browseTimes, String content) {
        String linkurl;
        switch (type) {
            case TYPEA:

                linkurl = RequestUrl.SUBJECT_DETAIL_SHARE_URL + "header="
                        + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + subjectId;

                builder.setTitle(name
                        + ",【小伙伴】已围观了" + browseTimes + "次");
                builder.setContent("【" + name + "】"
                        + content);
                builder.setLinkUrl(linkurl);
                builder.setBitmap(bmp);
                builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//            }
//        });
                builder.create();
                break;
            case TYPEB:
                linkurl = RequestUrl.SUBJECT_SNAP_SHARE_URL + "header="
                        + HeaderRequest.SUBJECT_SNAP_DETAIL + "&subjectId=" + subjectId;

                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);


                builder.setTitle(name
                        + ",【小伙伴】已围观了" + browseTimes + "次");
                builder.setContent(content);
                builder.setLinkUrl(linkurl);
                builder.setBitmap(bmp);
                builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//
//            }
//        });
                builder.create();
                break;
            case TYPEC:
                linkurl = RequestUrl.SUBJECT_NOTICE_SHARE_URL
                        + "header=" + HeaderRequest.SUBJECT_NOTICE_DETAIL
                        + "&subjectId=" + subjectId;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
                builder.setTitle(name
                        + ",【小伙伴】已围观了" + browseTimes + "次");
                builder.setContent(content);
                builder.setLinkUrl(linkurl);
                builder.setBitmap(bmp);
                builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//
//            }
//        });
                builder.create();
                break;
        }
    }

    @Override
    public void showChannelList(Channel channel) {
        homeLeftAdapter = new HomeLeftAdapter(getActivity(), channel.getMenuList());
        leftRecycler.setAdapter(homeLeftAdapter);
        skip();
    }

    public void skip() {
        homeLeftAdapter.setOnItemClick(new HomeLeftAdapter.onItemClick() {
            @Override
            public void OnClick(int position, Channel.MenuListBean bean) {
                AppChannel channel = new AppChannel();
                channel.setUuid(bean.getSkipId());
                channel.setName(bean.getTypeName());
                popup.dismiss();//隐藏pop
                if (bean.getTypeA() == 1) {
                    switch (bean.getTypeB()) {
                        case 0:
                            //有本事你来模式
                            Intent it = new Intent(getActivity(), ChanneListPageActivity.class);
                            it.putExtra("channel", bean.getTitle());
                            startActivity(it);
                            break;
                        case 1:
                            Intent it2 = new Intent(getActivity(), ChanneListPageActivity.class);
                            it2.putExtra("channel", bean.getTitle());
                            it2.putExtra("CHANNEL_ID", bean.getUuid());
                            it2.putExtra("CHANNEL_TYPE",bean.getTypeB());
                            startActivity(it2);
                            //GotoChannelUtils.GotoSubjectDetail(getActivity(), bean.getTypeB(), bean.getSkipId());

                            break;
                        case 2:
                            Intent it3 = new Intent(getActivity(), ChannelList2Activity.class);
                            it3.putExtra("CHANNEL_ID", bean.getUuid());
                            it3.putExtra("CHANNEL_NAME", bean.getTitle());
                            startActivity(it3);
                            break;
                    }

                } else {
                    switch (bean.getTypeB()) {
                        case 0:
                        case 1:
                            if (!TextUtils.isEmpty(bean.getSkipId())) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WebAdActivity.TITLE, bean.getTypeName());
                                bundle.putString(WebAdActivity.SKIP_ADDRESS, bean.getSkipId());
                                launchActivity(WebAdActivity.class, bundle);
                            }
                            break;
                        case 2:
                        case 3:
                        case 4:
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(SpinOffActivity.FROM_HOME_BOTTOM, true);
                            bundle.putInt(SpinOffActivity.SPIN_OFF_TYPE, bean.getTypeB());
                            launchActivity(SpinOffActivity.class, bundle);
                            break;
                        case 5:
                            launchActivity(BlooperActivity.class);
                            break;
                        case 6:
                            Bundle blooperBundle = new Bundle();
                            blooperBundle.putString(BlooperDetailActivity.STAR_ID, bean.getSkipId());
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
                            channel.setType(GotoChannelUtils.CHANNEL_TYPE_SUBJECT);
                            GotoChannelUtils.GotoList(SweetWheatCirclesFragment.this, channel, 0);
                            break;
                        case 10:
                            channel.setType(GotoChannelUtils.CHANNEL_TYPE_SNAP);
                            GotoChannelUtils.GotoList(SweetWheatCirclesFragment.this, channel, 0);
                            break;
                        case 13:
                            channel.setType(GotoChannelUtils.CHANNEL_TYPE_NOTICE);
                            GotoChannelUtils.GotoList(SweetWheatCirclesFragment.this, channel, 0);
                            break;
                        case 15:
                            GotoChannelUtils.GotoSubjectDetail(SweetWheatCirclesFragment.this, 0, bean.getSkipId(), 0);
                            break;
                        case 16:
                            GotoChannelUtils.GotoSubjectDetail(SweetWheatCirclesFragment.this, 1, bean.getSkipId(), 0);
                            break;
                        case 17:
                            GotoChannelUtils.GotoSubjectDetail(SweetWheatCirclesFragment.this, 2, bean.getSkipId(), 0);
                            break;
                        case 11:
                        case 12:
                        case 14:
                        default:
                            showToast(Constant.DEFAULT_TIPS_MSG);
                            break;
                    }

                }
            }
        });

    }

    @Override
    public void showView(SubjectHomePageResponse detailResponse) {
    }

    @Override
    public void gotoChannelDetail(int channelType, String subjectId) {
    }


}
