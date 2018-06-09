package com.donut.app.mvp.shakestar.select.particulars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.adapter.JointAdapter;
import com.donut.app.adapter.ShakeStarParticularsAdapter;
import com.donut.app.databinding.ActivityJointBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.select.ScrollInterceptScrollView;
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
 * Created by hard on 2018/2/5.
 */

public class JointActivity extends MVPBaseActivity<ActivityJointBinding, ParticularsPresenter> implements ParticularsContract.View{

    private Intent intent;
    private String g03;
    private String b02;
    private JointAdapter jointAdapter;
    private ShakeStarParticularsAdapter adapter;
    private  int STATUSA=0;//未收藏
    private static final int STATUSB=1;//已收藏
    private int page = 0, rows = 10, sortType = 0;
    private int status;
    private List<ParticularsResponse.ShakingStarListBean> list;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_joint;
    }

    @Override
    protected void initView() {
        StatusBarCompat.translucentStatusBar(this);
         mViewBinding.jointBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
        intent= getIntent();
         g03 = intent.getStringExtra("g03");
         b02 = intent.getStringExtra("b02");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
         mViewBinding.selectRecyclerView.setLayoutManager(linearLayoutManager);
         mViewBinding.xqRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
    }

    @Override
    protected void loadData() {
        mPresenter.loadData(true,b02,g03,0,1);
    }

    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> list,ParticularsResponse shakingStarListBeans) {
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);//视频充满
        this.list=list;
         jointAdapter=new JointAdapter(shakingStarListBeans.getMaterialVideoList(),getApplicationContext());
         mViewBinding.selectRecyclerView.setAdapter(jointAdapter);
//         list=new ArrayList<>();
//        list.addAll(shakingStarListBeans.getShakingStarList());
        adapter=new ShakeStarParticularsAdapter(list,this);
        mViewBinding.xqRecyclerView.setAdapter(adapter);
        BindingUtils.loadRoundImg(mViewBinding.particularsImg,shakingStarListBeans.getStarHeadPic());
        mViewBinding.particularsContent.setText(shakingStarListBeans.getTitle());
        mViewBinding.particularsUser.setText(shakingStarListBeans.getStarName());
        mViewBinding.particularsNumber.setText(shakingStarListBeans.getUseTimes()+"人使用");
        ImageView imageView = new ImageView(this);
        Glide.with(this).load(shakingStarListBeans.getMaterialVideoList().get(0).getThumbnailUrl()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mViewBinding.userPjPlayer.setThumbImageView(imageView);
        mViewBinding.userPjPlayer.setUp( shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl(),false,null);
        ImageView backButton = mViewBinding.userPjPlayer.getBackButton();
        backButton.setVisibility(View.GONE);//隐藏返回键
        ImageView fullscreenButton = mViewBinding.userPjPlayer.getFullscreenButton();
        fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
        status=shakingStarListBeans.getCollectionStatus();
        boolean wifi = NetUtils.isWifi(mContext);
        if(wifi){
            GSYBaseVideoPlayer player=mViewBinding.userPjPlayer;
            //player.startPlayLogic();
        }
        if(status==STATUSB){
            mViewBinding.particularsSc.setImageResource(R.drawable.sc2);
        }
         onClick(shakingStarListBeans);
    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response) {

    }

    public void onClick(final ParticularsResponse shakingStarList){
        mViewBinding.xqUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(StarDetailActivity.STAR_ID,shakingStarList.getFkB03());
                launchActivityForResult(StarDetailActivity.class, bundle, STAR_DETAIL_CODE);
            }
        });
        mViewBinding.scrollview.setScrollViewListener(new ScrollInterceptScrollView.IScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                L.e("TAG","正在加载数据……");
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
        jointAdapter.setOnSelectVideoItemOnClick(new JointAdapter.onSelectVideoItemOnClick() {
            @Override
            public void onClick(int position) {
                ImageView imageView = new ImageView(getContext());
                Glide.with(getContext()).load(shakingStarList.getMaterialVideoList().get(position).getThumbnailUrl()).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mViewBinding.userPjPlayer.setThumbImageView(imageView);
                mViewBinding.userPjPlayer.setUp( shakingStarList.getMaterialVideoList().get(position).getPlayUrl(),false,null);
                /*jointAdapter.notifyDataSetChanged();*/
            }
        });
        adapter.setItemOnClickListener(new ShakeStarParticularsAdapter.ItemOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent it=new Intent(getContext(),SelectVideoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("shakingStarListBeans", (ArrayList<? extends Parcelable>) list);
                bundle.putInt("position",position);
                bundle.putInt("TYPE",1);
                bundle.putString("g03",g03);
                bundle.putString("b02",b02);
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
//                String nickName = name;
//                String createTime = shakingStarListBeans.get(position).getCreateTime();
                ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
                builder.setTitle(shakingStarList.getStarName());
                builder.setContent(shakingStarList.getTitle());
//        L.e("TAG",starCommendResponses.get(position).getVideoThumbnail());
//        builder.setImgUrl(starCommendResponses.get(position).getVideoThumbnail());
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
                mPresenter.Share(false, shakingStarList.getShakingStarList().get(0).getB02Id(),null);
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
//                mPresenter.loadData(false,b02,g03);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    public void showPhone(View view) {
        ToastUtil.showShort(getContext(),"精彩内容,敬请期待");
    }
}
