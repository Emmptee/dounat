package com.donut.app.mvp.shakestar.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.MyBottomMaterialAdapter;
import com.donut.app.adapter.MyBottomRecyclerView;
import com.donut.app.databinding.ActivityMyBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.shakestar.MaterialResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.select.ScrollInterceptScrollView;
import com.donut.app.mvp.shakestar.select.VideoActivity;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.FastBlurUtil;
import com.donut.app.utils.L;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/2/9.
 */

public class MyActivity extends MVPBaseActivity<ActivityMyBinding,MinePresenter> implements MineContract.View{

    private static final String TAG = "MyActivity";

   private int page1=0,page2=0,page3=0,rows=10;
   private String selectedUserId;
   private String b02Id,g03Id,name;
   private  MyBottomRecyclerView myBottomRecyclerView;
   private  MyBottomMaterialAdapter myBottomMaterialAdapter;
   private int judge=0,type;
   private boolean isInfo;
   private int STATUSA=1,num;//已关注
   private static final int STATUSB=1;//已关注
   private List<MyProductionResponse.MyShakingStarListBean> list;
   private List<MyLikeResponse.MyLikeShakingStarListBean> list2;
   private List<MaterialResponse.MyMaterialListBean> list3;
   private  MyProductionResponse myProductionResponse;
   private int fans;
   @SuppressLint("HandlerLeak")
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if(msg.what==0){
               Bitmap obj = (Bitmap) msg.obj;
               mViewBinding.backgroundImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
               mViewBinding.backgroundImg.setImageBitmap(obj);
           }
       }
   };
    private int imageHeight;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    protected void initView() {
        StatusBarCompat.translucentStatusBar(this);
//        UserInfo userInfo = SysApplication.getUserInfo();
        selectedUserId=getIntent().getStringExtra("fkA01");
        b02Id=getIntent().getStringExtra("b02Id");
        g03Id=getIntent().getStringExtra("g03Id");
        type=getIntent().getIntExtra("type",0);
        mViewBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewBinding.textview.setTextColor(Color.argb( 0, 255, 255, 255));
    }

    @Override
    protected void loadData() {
        if(TextUtils.isEmpty(selectedUserId)||"".equals(selectedUserId)||null==selectedUserId){
            Intent it=new Intent(this, LoginActivity.class);
            startActivity(it);
        }else{
            mViewBinding.myRecycler.setLayoutManager(new GridLayoutManager(this,2 ));
            mPresenter.loadData(true,selectedUserId,page1,rows,judge);
        }

        mViewBinding.btnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mViewBinding.btnA.isChecked()){
                    judge=0;
                    mViewBinding.btnA.setTextColor(Color.parseColor("#81D8D0"));
                    mPresenter.loadData(false,selectedUserId,page1,rows,judge);
                }else{
                    mViewBinding.btnA.setTextColor(Color.parseColor("#000000"));
                }

                if(mViewBinding.btnB.isChecked()){
                    judge=1;
                    mViewBinding.btnB.setTextColor(Color.parseColor("#81D8D0"));
                    mPresenter.loadData(false,selectedUserId,page2,rows,judge);
                }else {
                    mViewBinding.btnB.setTextColor(Color.parseColor("#000000"));
                }
                if(mViewBinding.btnC.isChecked()){
                    judge=2;
                    mViewBinding.btnC.setTextColor(Color.parseColor("#81D8D0"));
                    mPresenter.loadData(false,selectedUserId,page3,rows,judge);
                }else{
                    mViewBinding.btnC.setTextColor(Color.parseColor("#000000"));
                }
                }

        });
    }

    @Override
    public void showView(List<MyProductionResponse.MyShakingStarListBean> myShakingStarListBeans, final MyProductionResponse myProductionResponse) {
    this.name=myProductionResponse.getUserNickName();
    this.list=myShakingStarListBeans;
    this.myProductionResponse=myProductionResponse;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int scaleRatio = 2;//不能小于2
                // 下面的这个方法必须在子线程中执行
                final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(myProductionResponse.getUserBGPic(), scaleRatio);
                Message message=new Message();
                message.what=0;
                message.obj=blurBitmap2;
                handler.sendMessage(message);
            }
        }).start();
        BindingUtils.loadRoundImg(mViewBinding.userImg,myProductionResponse.getUserHeadPic());//用户头像
        mViewBinding.userName.setText(myProductionResponse.getUserNickName());//用户名
        this.fans=myProductionResponse.getFollowCount();
        mViewBinding.attentionCount.setText("关注 "+(myProductionResponse.getAttentionCount()));//关注数量

        mViewBinding.fansCount.setText("粉丝 "+myProductionResponse.getFollowCount());//粉丝数量
        if(myProductionResponse.getFollowStatus()==STATUSB){
            mViewBinding.isAttention.setText("已关注");
            STATUSA=0;
            mViewBinding.isAttention.setChecked(true);
        }
        mViewBinding.challengeTvMsg.setVisibility(View.GONE);
        myBottomRecyclerView=new MyBottomRecyclerView(this,myShakingStarListBeans,null);
        mViewBinding.myRecycler.setAdapter(myBottomRecyclerView);
        onclick(myProductionResponse);
    }

    @Override
    public void showLikeView(List<MyLikeResponse.MyLikeShakingStarListBean> myLikelist) {
        this.list2=myLikelist;
        myBottomRecyclerView=new MyBottomRecyclerView(this,null,myLikelist);
        mViewBinding.myRecycler.setAdapter(myBottomRecyclerView);
        onclick(myProductionResponse);
        if(this.list2.size() == 0){
            mViewBinding.challengeTvMsg.setVisibility(View.VISIBLE);
        }else {
            mViewBinding.challengeTvMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMaterialView(List<MaterialResponse.MyMaterialListBean> myMaterialListBeans) {
        this.list3=myMaterialListBeans;
         myBottomMaterialAdapter = new MyBottomMaterialAdapter(this,myMaterialListBeans);
         mViewBinding.myRecycler.setAdapter(myBottomMaterialAdapter);
        onclick(myProductionResponse);
        if(this.list3.size() == 0){
           mViewBinding.challengeTvMsg.setVisibility(View.VISIBLE);
        }else {
            mViewBinding.challengeTvMsg.setVisibility(View.GONE);
        }
    }

    public void onclick(final MyProductionResponse myProductionResponse){
        mViewBinding.particularsFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("b02id",b02Id);
                String url = RequestUrl.SHAKESTAR_URL + "header="
                        + HeaderRequest.SHAKESTAR_DETAIL + "&b02Id=" + b02Id;

                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_logo);
                String nickName = myProductionResponse.getUserNickName();
//                String createTime = myProductionResponse.get(position).getCreateTime();
                ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
                builder.setTitle(nickName);
                builder.setContent("");
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
                mPresenter.Share(false,b02Id,null);
            }

        });
        myBottomRecyclerView.setBottomItemOnClick(new MyBottomRecyclerView.BottomItemOnClick() {
            @Override
            public void OnClick(int position) {
                if(judge==0){
                    Intent it=new Intent(getContext(),VideoActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",position);
                    bundle.putInt("TYPE",type);
                    bundle.putString("g03",g03Id);
                    bundle.putString("b02",b02Id);
                    bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                    it.putExtra("bundle",bundle);
                    it.putExtra("b02Id",b02Id);
                    it.putExtra("g03Id",g03Id);
                    it.putExtra("type",type);
                    it.putExtra("fkA01",selectedUserId);
                    startActivity(it);
                }else if(judge==1){
                    Intent it=new Intent(getContext(),VideoActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",position);
                    bundle.putInt("TYPE",type);
                    bundle.putString("g03",g03Id);
                    bundle.putString("b02",b02Id);
                    bundle.putParcelableArrayList("list2", (ArrayList<? extends Parcelable>) list2);
                    it.putExtra("bundle",bundle);
                    it.putExtra("b02Id",b02Id);
                    it.putExtra("g03Id",g03Id);
                    it.putExtra("type",type);
                    it.putExtra("fkA01",selectedUserId);
                    startActivity(it);
                }
            }
        });
        if(myBottomMaterialAdapter!=null){
            myBottomMaterialAdapter.setBottomItemOnClick(new MyBottomMaterialAdapter.BottomItemOnClick() {
                @Override
                public void OnClick(int position) {
                    //我的素材跳转
                    Log.i(TAG, "OnClick:getType " + list3.get(position).getType());
                    if(judge==2){
                        if(list3.get(position).getType()==0){
                            Intent itent=new Intent(getContext(), ParticularsActivity.class);
                            itent.putExtra("g03",list3.get(position).getG03Id());
                            itent.putExtra("b02",list3.get(position).getB02Id());
                            startActivity(itent);
                        }else if(list3.get(position).getType()==1){
                            Intent itent=new Intent(getContext(), JointActivity.class);
                            itent.putExtra("g03",list3.get(position).getG03Id());
                            itent.putExtra("b02",list3.get(position).getB02Id());
                            startActivity(itent);
                        }

                    }
                }
            });
        }
        mViewBinding.myScrollview.setScrollViewListener(new ScrollInterceptScrollView.IScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                if(judge==0){
                    page1++;
                    mPresenter.loadData(false,selectedUserId,page1,rows,judge);
                }else if(judge==1){
                    page2++;
                    mPresenter.loadData(false,selectedUserId,page2,rows,judge);
                }else if(judge==2){
                    page3++;
                    mPresenter.loadData(false,selectedUserId,page3,rows,judge);
                }

            }

            @Override
            public void onScrolledToTop() {

            }

            @Override
            public void onScrollChanged(ScrollInterceptScrollView scrollView, int x, int y, int oldx, int oldy) {
                imageHeight = mViewBinding.textview.getHeight();
                if (y <= 0) {
                    mViewBinding.textview.setText(name);
//                          设置文字背景颜色，白色
                    mViewBinding.textview.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得
//                          设置文字颜色，黑色
                    mViewBinding.textview.setTextColor(Color.argb( 0, 255, 255, 255));
                } else if (y > 0 && y <= imageHeight) {
                    mViewBinding.textview.setText(name);
                    float scale = (float) y / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)白色透明
                    mViewBinding.textview.setBackgroundColor(Color.argb((int) alpha, 129, 216, 208));
                    //  设置文字颜色，黑色，加透明度
                    mViewBinding.textview.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                } else {
                    //    白色不透明
                    mViewBinding.textview.setBackgroundColor(Color.argb((int) 255, 129, 216, 208));
                    //  设置文字颜色
                    //黑色
                    /*mViewBinding.textview.setTextColor(Color.argb((int) 255, 0, 0, 0));*/
                    mViewBinding.textview.setTextColor(Color.argb((int) 255, 255, 255, 255));

                }
            }
        });
        mViewBinding.isAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(STATUSA==1){
                   //关注
                    mViewBinding.isAttention.setText("已关注");
                    STATUSA=0;
                    mViewBinding.fansCount.setText("粉丝 "+(fans+=1));//粉丝数量
                    mViewBinding.isAttention.setChecked(true);
                }else if(STATUSA==0){//取消关注
                    mViewBinding.isAttention.setText("关注");
                    STATUSA=1;
                    mViewBinding.fansCount.setText("粉丝 "+(fans-=1));//粉丝数量
                    mViewBinding.isAttention.setChecked(false);
                }
                mPresenter.loadAttention(false,selectedUserId,String.valueOf(STATUSA));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
