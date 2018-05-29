package com.donut.app.mvp.shakestar.select.particulars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.adapter.ShakeStarParticularsAdapter;
import com.donut.app.databinding.ActivityParticularsLayoutBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.mvp.shakestar.commend.ShakeStarSelectActivity;
import com.donut.app.mvp.shakestar.select.ScrollInterceptScrollView;
import com.donut.app.mvp.shakestar.video.record.RecordActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.ToastUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import static com.donut.app.mvp.subject.snap.SubjectSnapActivity.STAR_DETAIL_CODE;

/**
 * Created by hard on 2018/2/1.
 */

public class ParticularsActivity extends MVPBaseActivity<ActivityParticularsLayoutBinding, ParticularsPresenter>implements ParticularsContract.View{

    private static final String TAG = "ParticularsActivity";

  private ShakeStarParticularsAdapter adapter;
  private Intent intent;
  private String g03 ;
  private String b02;
  boolean isCollection;
  private  int STATUSA=0;//未收藏
  private static final int STATUSB=1;//已收藏
  private int page = 0, rows = 10, sortType = 0;
  private int status;
  private ParticularsResponse shakingStarListBeans;
List<ParticularsResponse.ShakingStarListBean> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_particulars_layout;
    }

    @Override
    protected void initView() {
        mViewBinding.parentPanelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
        StatusBarCompat.translucentStatusBar(this);
         intent = getIntent();
         g03 = intent.getStringExtra("g03");
         b02 = intent.getStringExtra("b02");
        mPresenter.loadData(true,b02,g03,page,rows);
    }

    @Override
    public void showView( List<ParticularsResponse.ShakingStarListBean> list,ParticularsResponse shakingStarListBeans) {
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);//默认比例
        this.list=list;
//        mViewBinding.shakeCommendSwip.setEnabled(false);
        mViewBinding.xqRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        if(shakingStarListBeans.getShakingStarList().get(0).getVideoThumbnail()!=null){
            mViewBinding.xqMessage.setVisibility(View.GONE);
        }else{
            mViewBinding.xqMessage.setVisibility(View.VISIBLE);
        }
        BindingUtils.loadRoundImg(mViewBinding.particularsImg,shakingStarListBeans.getStarHeadPic());
        mViewBinding.particularsContent.setText(shakingStarListBeans.getTitle());
        mViewBinding.particularsUser.setText(shakingStarListBeans.getStarName());
        mViewBinding.particularsNumber.setText(shakingStarListBeans.getUseTimes()+"人使用");
        adapter=new ShakeStarParticularsAdapter(list,this);
        mViewBinding.xqRecyclerView.setAdapter(adapter);
        mViewBinding.xqRecyclerView.setNestedScrollingEnabled(false);
        if(shakingStarListBeans.getDisplay()==0){//素材在左
            mViewBinding.videoRight.setVisibility(View.GONE);
            mViewBinding.videoLeft.setVisibility(View.VISIBLE);
            mViewBinding.particularsPlayerLeft.setUp( shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl(),false,null);
            ImageView backButton = mViewBinding.particularsPlayerLeft.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            ImageView fullscreenButton = mViewBinding.particularsPlayerLeft.getFullscreenButton();
            fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
            status=shakingStarListBeans.getCollectionStatus();
            boolean wifi = NetUtils.isWifi(mContext);
            if(wifi){
                GSYBaseVideoPlayer player=mViewBinding.particularsPlayerLeft;
                //player.startPlayLogic();
            }
        }else if(shakingStarListBeans.getDisplay()==1){//素在在右
            ImageView backButton = mViewBinding.particularsPlayerRight.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            ImageView fullscreenButton = mViewBinding.particularsPlayerRight.getFullscreenButton();
            fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
            status=shakingStarListBeans.getCollectionStatus();
            mViewBinding.videoRight.setVisibility(View.VISIBLE);
            mViewBinding.videoLeft.setVisibility(View.GONE);
            mViewBinding.particularsPlayerRight.setUp( shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl(),false,null);
            boolean wifi = NetUtils.isWifi(mContext);
            if(wifi){
                GSYBaseVideoPlayer player=mViewBinding.particularsPlayerRight;
                //player.startPlayLogic();
            }
        }


        if(status==STATUSB){
            mViewBinding.particularsSc.setImageResource(R.drawable.sc2);
        }
//        HeaderScrollHelper helper=new HeaderScrollHelper();
//        helper.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer() {
//            @Override
//            public View getScrollableView() {
//                return mViewBinding.xqRecyclerView;
//            }
//        });
        onClick(shakingStarListBeans);
    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response) {

    }


    private RecyclerView.LayoutManager getLayoutManager() {

        final ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(this,3);
        layoutManager.setOnRecyclerViewScrollListener(mViewBinding.xqRecyclerView, new OnRecyclerViewScrollLocationListener() {
            @Override
            public void onTopWhenScrollIdle(RecyclerView recyclerView) {

            }

            @Override
            public void onBottomWhenScrollIdle(RecyclerView recyclerView) {

            }
        });
        return layoutManager;
    }
    public void onClick(final ParticularsResponse shakingStarListBeans){
        mViewBinding.xqUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle = new Bundle();
                bundle.putString(StarDetailActivity.STAR_ID,shakingStarListBeans.getFkB03());
                launchActivityForResult(StarDetailActivity.class, bundle, STAR_DETAIL_CODE);*/
                Bundle bundle = new Bundle();
                bundle.putString(BlooperDetailActivity.STAR_ID, shakingStarListBeans.getFkB03());
                launchActivity(BlooperDetailActivity.class, bundle);
            }
        });
        mViewBinding.scrollview.setScrollViewListener(new ScrollInterceptScrollView.IScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                     page++;
                    mPresenter.loadData(false,g03,b02,page,rows);
            }

            @Override
            public void onScrolledToTop() {

            }

            @Override
            public void onScrollChanged(ScrollInterceptScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
       adapter.setItemOnClickListener(new ShakeStarParticularsAdapter.ItemOnClickListener() {
           @Override
           public void onClick(int position) {
                Intent it=new Intent(ParticularsActivity.this,SelectVideoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("shakingStarListBeans", (ArrayList<? extends Parcelable>) list);
                bundle.putInt("position",position);
                bundle.putInt("TYPE",0);
                bundle.putString("g03",g03);
                bundle.putString("b02",b02);
                bundle.putInt("position",position);
                it.putExtra("bundle",bundle);
                startActivity(it);
           }
       });
        mViewBinding.particularsFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = RequestUrl.SHAKESTAR_URL + "header="
                        + HeaderRequest.SHAKESTAR_DETAIL + "&b02Id=" + b02;

                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_logo);
                ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
                builder.setTitle(shakingStarListBeans.getStarName());
                builder.setContent(shakingStarListBeans.getTitle());
                builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
                builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
                    @Override
                    public void onSuccessRes() {
                        L.e("tag","分享成功！");

                    }
                });
                builder.setBitmap(bmp);
                builder.setLinkUrl(url);
                builder.create();
                mPresenter.Share(false, shakingStarListBeans.getShakingStarList().get(0).getB02Id(),null);
            }
        });
        mViewBinding.particularsSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("status",status);
                if(STATUSA==0){
                   //收藏        type:99是没有用过的标识 ，后台没有操作
                    mViewBinding.particularsSc.setImageResource(R.drawable.sc2);
                        mPresenter.Collect(false,b02,99,1);
                    STATUSA=1;
                    }else if(STATUSA==1){//取消收藏
                    mViewBinding.particularsSc.setImageResource(R.drawable.sc1);
                        mPresenter.Collect(false,b02,99,0);
                    STATUSA=0;
                    }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    public void newImagePic(View view) {
        Intent it=new Intent(this.mContext, RecordActivity.class);
        startActivity(it);
        ToastUtil.showShort(getContext(),"精彩内容,敬请期待");

    }
}
