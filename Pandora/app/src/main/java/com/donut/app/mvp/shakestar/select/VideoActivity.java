package com.donut.app.mvp.shakestar.select;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.manager.LoadController;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.MyCommentAllAdapter;
import com.donut.app.adapter.ShakeStarRecyclerViewAdapter;
import com.donut.app.adapter.ShakeStarVideoAdapter;
import com.donut.app.databinding.ActivityVideoBinding;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.MaterialResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.wish.CommendLikeRequest;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.ItemDecoration;
import com.donut.app.mvp.shakestar.MDialog;
import com.donut.app.mvp.shakestar.commend.ShakeStarSelectActivity;
import com.donut.app.mvp.shakestar.mine.MyActivity;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsContract;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
import com.donut.app.mvp.shakestar.select.particulars.SelectVideoActivity;
import com.donut.app.mvp.shakestar.utils.ScrollCalculatorHelper;
import com.donut.app.utils.L;
import com.donut.app.utils.ToastUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * Created by hard on 2018/2/22.
 */

public class VideoActivity extends MVPBaseActivity<ActivityVideoBinding,ParticularsPresenter>
        implements ParticularsContract.View,SwipeRefreshLayout.OnRefreshListener,ShakeStarVideoAdapter.ItemLikeOnClickListener {

        private ParticularsResponse shakingStarListBeans;
        private ShakeStarVideoAdapter recyclerViewAdapter;
        private List<MyProductionResponse.MyShakingStarListBean> list;
        private List<MyLikeResponse.MyLikeShakingStarListBean> list2;
        private List<MaterialResponse.MyMaterialListBean> list3;
        private  int TYPE;//同屏0,拼接1
        private  String g03;
        private  String b02;
        private  int position,type;
        private int page = 0, rows = 10;
        private PopupWindow popup;
        private View view;
        private int commendPage = 0, commendRows = 10;
        private static  int height;
        private ImageView back;
        private RecyclerView commentRecyclerView;
        private EditText edit;
        private TextView sum;
        private MyCommentAllAdapter myCommentAllAdapter;
        private static int position2;
        private String selectedUserId;
        private String b02Id,g03Id,name;
        private PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();//recyclerView扩展类
        private List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses;
    private int playTop,playBottom;
    private ScrollCalculatorHelper scrollCalculatorHelper;
    private  LinearLayoutManager layoutManager;
        @Override
        protected int getLayoutId() {
            return R.layout.activity_video;
        }

        @Override
        protected void initView() {
            StatusBarCompat.translucentStatusBar(this);
            selectedUserId=getIntent().getStringExtra("fkA01");
            b02Id=getIntent().getStringExtra("b02Id");
            g03Id=getIntent().getStringExtra("g03Id");
            type=getIntent().getIntExtra("TYPE",0);
            mViewBinding.adBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            height = wm.getDefaultDisplay().getHeight();
            mViewBinding.shakeCommendSwip.setOnRefreshListener(this);
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("bundle");

                list =  bundle.getParcelableArrayList("list");

                list2 =  bundle.getParcelableArrayList("list2");

                list3 =  bundle.getParcelableArrayList("list3");


            position = bundle.getInt("position",0);
            TYPE= bundle.getInt("TYPE",0);
            g03= bundle.getString("g03");
            b02= bundle.getString("b02");
//         shakingStarListBeans = (ParticularsResponse) bundle.getSerializable("shakingStarListBeans");
            view = View.inflate(this, R.layout.comment_pop,null);
            back=view.findViewById(R.id.pop_back);
            sum=view.findViewById(R.id.commend_sum);
            commentRecyclerView=view.findViewById(R.id.comment_recyclerview);

            commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ItemDecoration itemDecoration=new ItemDecoration(this);
            commentRecyclerView.addItemDecoration(itemDecoration);
            edit=view.findViewById(R.id.commend_edit);
            popup=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,height/3*2);
            //设置背景
            popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            //设置焦点
            popup.setFocusable(true);
            //可触摸
            popup.setTouchable(true);
            //窗口关闭事件
            popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        @Override
        protected void loadData() {
            //限定范围为屏幕一半的上下偏移180
            playTop = CommonUtil.getScreenHeight(getContext()) / 2 - CommonUtil.dip2px(getContext(), 180);
            playBottom = CommonUtil.getScreenHeight(getContext()) / 2 + CommonUtil.dip2px(getContext(), 180);
             layoutManager = (LinearLayoutManager) getLayoutManager();
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mViewBinding.selectVideo.setLayoutManager(layoutManager);
            if(list!=null){
                recyclerViewAdapter = new ShakeStarVideoAdapter(list,null,null,this);
            }else if(list2!=null){
                recyclerViewAdapter = new ShakeStarVideoAdapter(null,list2,null, this);
            }else if(list3!=null){
                recyclerViewAdapter = new ShakeStarVideoAdapter(null,null,list3, this);
            }
            mViewBinding.selectVideo.setAdapter(recyclerViewAdapter);
            MoveToPosition(layoutManager,mViewBinding.selectVideo,position);
            pagerSnapHelper.attachToRecyclerView(  mViewBinding.selectVideo);
            scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.tj_video, playTop, playBottom);
            mViewBinding.selectVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {

                int firstVisibleItem, lastVisibleItem;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    //这是滑动自动播放的代码
                    scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem);
                }
            });
            onClick();

            recyclerViewAdapter.setItemLikeOnClickListener(this);

        }

    public  void onClick(){

            if(list!=null){
                //关注
                recyclerViewAdapter.setItemAttentionOnClick(new ShakeStarVideoAdapter.ItemAttentionOnClick() {
                    @Override
                    public void Attention(String starId, String operation) {
                        mPresenter.loadAttention(false,starId,operation);
                    }
                });
            }else if(list2!=null){
                //关注
                recyclerViewAdapter.setItemAttentionOnClick2(new ShakeStarVideoAdapter.ItemAttentionOnClick2() {
                    @Override
                    public void Attention2(String starId, String operation) {
                        mPresenter.loadAttention(false,starId,operation);
                    }
                });
            }


        if(list!=null){
            recyclerViewAdapter.setItemInformOnClick(new ShakeStarVideoAdapter.ItemInformOnClick() {
                @Override
                public void onInformClick(final String fkB02) {
                    MDialog.show(VideoActivity.this, "是否确认举报？", new MDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                            mPresenter.userInform(false,fkB02);
                            ToastUtil.show(VideoActivity.this,"已举报！",0);
                        }
                    });
                }
            });
        }else if(list2!=null){
            recyclerViewAdapter.setItemInformOnClick2(new ShakeStarVideoAdapter.ItemInformOnClick2() {
                @Override
                public void onInformClick(final String fkB02) {
                    MDialog.show(VideoActivity.this, "是否确认举报？", new MDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                            mPresenter.userInform(false,fkB02);
                            ToastUtil.show(VideoActivity.this,"已举报！",0);
                        }
                    });
                }
            });
        }
        recyclerViewAdapter.setItemMyOnClickListener(new ShakeStarVideoAdapter.ItemMyOnClickListener() {
            @Override
            public void MyOnClick(int position) {
                Intent it=new Intent(getContext(), MyActivity.class);
                it.putExtra("position",position);
                it.putExtra("b02Id",b02Id);
                it.putExtra("g03Id",g03Id);
                it.putExtra("type",type);
                it.putExtra("fkA01",selectedUserId);
                startActivity(it);
            }
        });
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    String content = edit.getText().toString().trim();
                    if(!TextUtils.isEmpty(content)){

                        L.e("position2",position2);
                        edit.setText("");
                        UserInfo userInfo = SysApplication.getUserInfo();
                        String userId = userInfo.getUserId();
                        if(TextUtils.isEmpty(userId)||null==userId){
                            //去登陆
                            Intent it=new Intent(getContext(), LoginActivity.class);
                            getContext().startActivity(it);
                        }else {
                            if(list!=null){
                                mPresenter.loadAllCommend(false, list.get(position2).getB02Id(), userId, commendPage, commendRows, 0, 10);
                            }else if(list2!=null){
                                mPresenter.loadAllCommend(false, list2.get(position2).getB02Id(), userId, commendPage, commendRows, 0, 10);
                            }
                        }
                    }


                }
                return false;
            }
        });
        recyclerViewAdapter.setItemCommendOnClickListener(new ShakeStarVideoAdapter.ItemCommendOnClickListener() {
            @Override
            public void CommendClick(final int position, ImageView img) {
                VideoActivity.position2=position;
                UserInfo userInfo = SysApplication.getUserInfo();
                String userId = userInfo.getUserId();
                L.e("TAG",userId);
                if(TextUtils.isEmpty(userId)||null==userId){
                    //去登陆
                    Intent it=new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(it);
                }else{

                    // 设置popWindow的显示和消失动画
                    popup.setAnimationStyle(R.style.showPopupAnimation);
                    if(popup.isShowing()){
                        //消失
                        popup.dismiss();
                    }else{
                        mPresenter.loadAllCommend(false, list.get(position2).getB02Id(), userId, commendPage, commendRows, 0, 10);
                        popup.showAtLocation(img, Gravity.BOTTOM, 0, 0);
                        backgroundAlpha(0.5f);
                        mPresenter.loadAllCommend(false,list.get(position2).getB02Id(),userId,commendPage,commendRows,0,10);

                    }
                }

            }
        });
        recyclerViewAdapter.setItemCommendOnClickListener2(new ShakeStarVideoAdapter.ItemCommendOnClickListener2() {
            @Override
            public void CommendClick(final int position, ImageView img) {
                VideoActivity.position2=position;
                UserInfo userInfo = SysApplication.getUserInfo();
                String userId = userInfo.getUserId();
                L.e("TAG",userId);
                if(TextUtils.isEmpty(userId)||null==userId){
                    //去登陆
                    Intent it=new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(it);
                }else{

                    // 设置popWindow的显示和消失动画
                    popup.setAnimationStyle(R.style.showPopupAnimation);
                    if(popup.isShowing()){
                        //消失
                        popup.dismiss();
                    }else{
                        mPresenter.loadAllCommend(false, list2.get(position2).getB02Id(), userId, commendPage, commendRows, 0, 10);
                        popup.showAtLocation(img, Gravity.BOTTOM, 0, 0);
                        backgroundAlpha(0.5f);
                        mPresenter.loadAllCommend(false,list2.get(position2).getB02Id(),userId,commendPage,commendRows,0,10);

                    }
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //消失
                popup.dismiss();
                mPresenter.clear();
            }
        });
        mViewBinding.gzImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择素材
                Intent it=new Intent(getContext(), ShakeStarSelectActivity.class);
                startActivity(it);
            }
        });
        mViewBinding.adBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        recyclerViewAdapter.setItemLikeOnClickListener2(new ShakeStarRecyclerViewAdapter.ItemLikeOnClickListener2() {
//
//            @Override
//            public void LikeClick(ParticularsResponse.ShakingStarListBean model2, boolean like) {
//                mPresenter.Like(false,model2.getB02Id(),like ? 1 : 2);
//            }
//        });
        recyclerViewAdapter.setItemShareOnClickListener(new ShakeStarVideoAdapter.ItemShareOnClickListener() {
            @Override
            public void ShareClick(final int position) {

                String url = RequestUrl.SHAKESTAR_URL + "header="
                        + HeaderRequest.SHAKESTAR_DETAIL + "&b02Id=" + list.get(position).getB02Id();

                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_logo);
                String nickName = list.get(position).getNickName();
                String createTime = list.get(position).getCreateTime();
                ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
                builder.setTitle(nickName);
                builder.setContent(list.get(position).getContentDesc());
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
                mPresenter.Share(false,list.get(position).getB02Id(),null);
            }
        });
        recyclerViewAdapter.setItemShareOnClickListener2(new ShakeStarVideoAdapter.ItemShareOnClickListener2() {
            @Override
            public void ShareClick(final int position) {

                String url = RequestUrl.SHAKESTAR_URL + "header="
                        + HeaderRequest.SHAKESTAR_DETAIL + "&b02Id=" + list2.get(position).getB02Id();

                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_logo);
                String nickName = list2.get(position).getNickName();
                String createTime = list2.get(position).getCreateTime();
                ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
                builder.setTitle(nickName);
                builder.setContent(list2.get(position).getContentDesc());
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
                mPresenter.Share(false,list2.get(position).getB02Id(),null);
            }
        });
        recyclerViewAdapter.setItemSelectOnClickListener(new ShakeStarVideoAdapter.ItemSelectOnClickListener() {
            @Override
            public void SelectClick(int position) {
                Intent it = null;
                //跳转到素材详情
                if(TYPE==0){
                    it=new Intent(getContext(), ParticularsActivity.class);
                    it.putExtra("g03",g03);
                    it.putExtra("b02",b02);
                }else if(TYPE==1){
                    it=new Intent(getContext(), JointActivity.class);
                    it.putExtra("g03",g03);
                    it.putExtra("b02",b02);
                }
                startActivity(it);
            }
        });
    }

    private boolean isTop;
    private RecyclerView.LayoutManager getLayoutManager() {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(this);
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.selectVideo,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            mPresenter.clear();
                            page++;
                            mPresenter.loadData(false,g03,b02,page,rows);
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

    //根据下标指定位置
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }
    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> list,ParticularsResponse shakingStarListBeans) {
//        this.list=list;
//        L.e("TAG","showView");
//        recyclerViewAdapter = new ShakeStarRecyclerViewAdapter(null,list, this);
//        mViewBinding.selectVideo.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response) {
        //获取全部评论
        sum.setText(String.valueOf(Response.getCommentTimes()));
        myCommentAllAdapter=new MyCommentAllAdapter(this,commendAllResponse);
        commentRecyclerView.setAdapter(myCommentAllAdapter);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    String content = edit.getText().toString().trim();
                    if(!TextUtils.isEmpty(content)){
                        L.e("position",position);
                        if(list!=null){
                            mPresenter.CommendContnet(false,list.get(position2).getB02Id(),String.valueOf(0),content);
                        }else if(list2!=null){
                            mPresenter.CommendContnet(false,list2.get(position2).getB02Id(),String.valueOf(0),content);
                        }

                        edit.setText("");
                        UserInfo userInfo = SysApplication.getUserInfo();
                        String userId = userInfo.getUserId();
                        if(TextUtils.isEmpty(userId)||null==userId){
                            //去登陆
                            Intent it=new Intent(getContext(), LoginActivity.class);
                            getContext().startActivity(it);
                        }else {
                            if(list!=null){
                                mPresenter.loadAllCommend(false, list.get(position2).getB02Id(), userId, commendPage, commendRows, 0, 10);
                            }else if(list2!=null){
                                mPresenter.loadAllCommend(false, list2.get(position2).getB02Id(), userId, commendPage, commendRows, 0, 10);
                            }
                        }
                    }


                }
                return false;
            }
        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    @Override
    public void onRefresh() {
        page = 0;
        mPresenter.clear();
        recyclerViewAdapter.notifyDataSetChanged();
        mViewBinding.shakeCommendSwip.setRefreshing(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void LikeClick(MyLikeResponse.MyLikeShakingStarListBean myLikeShakingStarListBean, boolean like) {
        int NEW_LIKE_CODE = 00001;
        CommendLikeRequest request = new CommendLikeRequest();
        request.setContentId(myLikeShakingStarListBean.getB02Id());
        request.setPraiseType(like ? 1 : 2);
        mPresenter.loadData(request,
                HeaderRequest.SUBJECT_PRAISE,
                NEW_LIKE_CODE,
                false);
    }

}
