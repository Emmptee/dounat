package com.donut.app.mvp.channel.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.view.View;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.adapter.HomeLeftListRecyclerAdapter;
import com.donut.app.adapter.HomeListRecyclerAdapter;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivityChannelListLayoutBinding;
import com.donut.app.databinding.ActivityChannelListPageBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.channel.search.ChannelSearchActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by hard on 2018/2/28.
 */

public class ChanneListPageActivity extends MVPBaseActivity<ActivityChannelListPageBinding, ChannelListPresenter>
        implements ChannelListContract.View{
    private  HomeLeftListRecyclerAdapter homeListRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
    private ShareBuilderCommonUtil.Builder builder;
    private Bitmap bmp;
    private static final int TYPEA=0,TYPEB=1,TYPEC=2;
    private int channel_id;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_list_page;
    }

    @Override
    protected void initView() {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        builder = new ShareBuilderCommonUtil.Builder(this);
        linearLayoutManager= new LinearLayoutManager(this);
      linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      mViewBinding.homeRecy.setLayoutManager(linearLayoutManager);
      mViewBinding.homeLeftBack.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
      mViewBinding.homePd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Bundle bundle = new Bundle();
              bundle.putString(ChannelSearchActivity.CHANNEL_ID,
                      getIntent().getStringExtra("CHANNEL_ID"));
              bundle.putString(ChannelSearchActivity.CHANNEL_NAME,
                      getIntent().getStringExtra("CHANNEL_NAME"));
              bundle.putInt(ChannelSearchActivity.CHANNEL_TYPE,channel_id);
              launchActivity(ChannelSearchActivity.class, bundle);
          }
      });
    }

    @Override
    protected void loadData() {
        channel_id=getIntent().getIntExtra("CHANNEL_TYPE",0);
        mViewBinding.homeTitle.setText(getIntent().getStringExtra("channel"));
      mPresenter.loadData(false,getIntent().getStringExtra("CHANNEL_ID"));
    }

    @Override
    public void showView(SubjectListResponse response) {

        homeListRecyclerAdapter=new HomeLeftListRecyclerAdapter(this,response.getSubjectList());
        mViewBinding.homeRecy.setAdapter(homeListRecyclerAdapter);
        pagerSnapHelper.attachToRecyclerView(mViewBinding.homeRecy);
        onClick();
    }
    public void onClick(){

        homeListRecyclerAdapter.setOnItemOnClick(new HomeLeftListRecyclerAdapter.OnLeftItemOnClick() {
            @Override
            public void OnClick(int channelType,String subjectId) {//根据返回值不同，跳转的页面不同
                GotoChannelUtils.GotoSubjectDetail(getContext(),channel_id,subjectId);
            }
        });
        homeListRecyclerAdapter.setOnItenShareClick(new HomeLeftListRecyclerAdapter.OnLeftItenShareClick() {
            @Override
            public void OnShareClick(int type,String name,String subjectId,int browseTimes,String description) {//分享
                Share(channel_id,name,subjectId,browseTimes,description);
            }
        });
    }
    public void Share(int type,String name,String subjectId,int browseTimes,String content){
        String linkurl;
        switch (type){
            case TYPEA:

                linkurl = RequestUrl.SUBJECT_DETAIL_SHARE_URL + "header="
                        + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + subjectId;

                builder.setTitle(name
                        + ",【小伙伴】已围观了" + browseTimes + "次");
                builder.setContent("【" + name + "】"
                        +content);
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
                        + "&subjectId=" +subjectId;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
                builder.setTitle(name
                        + ",【小伙伴】已围观了" + browseTimes+ "次");
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
}
