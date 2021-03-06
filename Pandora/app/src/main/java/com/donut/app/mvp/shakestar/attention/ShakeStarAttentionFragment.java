package com.donut.app.mvp.shakestar.attention;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.MyCommentAllAdapter;
import com.donut.app.adapter.ShakeStarRecyclerViewAdapter;
import com.donut.app.databinding.ShakeAttentionFragmentBinding;
import com.donut.app.databinding.ShakeCommendFragmentBinding;
import com.donut.app.entity.UserInfo;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.shakestar.ItemDecoration;
import com.donut.app.mvp.shakestar.MDialog;
import com.donut.app.mvp.shakestar.commend.CommendPresenter;
import com.donut.app.mvp.shakestar.commend.ShakeStarCommendFragment;
import com.donut.app.mvp.shakestar.commend.ShakeStarSelectActivity;
import com.donut.app.mvp.shakestar.mine.MyActivity;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.mvp.shakestar.utils.ScrollCalculatorHelper;
import com.donut.app.utils.L;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * 关注页面
 * Created by hard on 2018/1/29.
 */

public class ShakeStarAttentionFragment extends MVPBaseFragment<ShakeAttentionFragmentBinding,AttentionPresenter>
        implements AttentionContract.View,SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "ShakeStarAttentionFragm";
    private static int position;
    private ShakeStarRecyclerViewAdapter recyclerViewAdapter;
    private int page = 0, rows = 10, sortType = 0;
    private static final int TYPEA=0;//同屏
    private static final int TYPEB=1;//拼接
    private int commendPage = 0, commendRows = 10;
    private PopupWindow popup;
    private View view;
    private static  int height;
    private ImageView back;
    private EditText edit;
    private MyCommentAllAdapter myCommentAllAdapter;
    private RecyclerView commentRecyclerView;
    private TextView sum;
    private List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses;
    private PagerSnapHelper snapPagerHelper= new PagerSnapHelper();//recyclerView扩展类
    private  LinearLayoutManager linearLayoutManager;
    private int playTop,playBottom;
    private ScrollCalculatorHelper scrollCalculatorHelper;
    @Override
    protected int getLayoutId() {
        return R.layout.shake_attention_fragment;
    }

    @Override
    protected void initView() {
        mViewBinding.shakeAttentionSwip.setOnRefreshListener(this);
        WindowManager wm =(WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
    }
    @Override
    protected void initEvent() {
        view = View.inflate(getActivity(), R.layout.comment_pop,null);
        back=view.findViewById(R.id.pop_back);
        //限定范围为屏幕一半的上下偏移180
        playTop = CommonUtil.getScreenHeight(getContext()) / 2 - CommonUtil.dip2px(getContext(), 180);
        playBottom = CommonUtil.getScreenHeight(getContext()) / 2 + CommonUtil.dip2px(getContext(), 180);
        commentRecyclerView=view.findViewById(R.id.comment_recyclerview);
         sum = view.findViewById(R.id.commend_sum);
         linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直展示
        commentRecyclerView.setLayoutManager(getLayoutManager());
        ItemDecoration itemDecoration=new ItemDecoration(getActivity());
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
        mViewBinding.shakeAttentionList.setLayoutManager(getLayoutManager());
        mPresenter.loadData(false,sortType,page,rows);

        mViewBinding.gzImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择素材
                Intent it=new Intent(getActivity(), ShakeStarSelectActivity.class);
                startActivity(it);
                ToastUtil.showLong(getActivity(),"精彩内容,敬请期待");
            }
        });
    }

    @Override
    public void showView( List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses) {
        this.starCommendResponses=starCommendResponses;
//        this.starCommendResponses.clear();
//        this. starCommendResponses.addAll(starCommendResponses);
        if(starCommendResponses.equals("")){
            mViewBinding.attentionLogo.setVisibility(View.VISIBLE);
        }else{
            mViewBinding.attentionLogo.setVisibility(View.GONE);
        }
        if(recyclerViewAdapter==null){
            mViewBinding.shakeAttentionList.setLayoutManager(linearLayoutManager);
            recyclerViewAdapter=new ShakeStarRecyclerViewAdapter(starCommendResponses,null,getActivity());
            mViewBinding.shakeAttentionList.setAdapter(recyclerViewAdapter);
            snapPagerHelper.attachToRecyclerView( mViewBinding.shakeAttentionList);
        }else{
            recyclerViewAdapter.notifyDataSetChanged();
        }
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.shake_attention_list, playTop, playBottom);
        mViewBinding.shakeAttentionList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //这是滑动自动播放的代码
                scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem);
            }
        });
        onClick(starCommendResponses);
    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse,CommendAllResponse response) {
        sum.setText(String.valueOf(response.getCommentTimes()));
        //获取全部评论
        myCommentAllAdapter=new MyCommentAllAdapter(getActivity(),commendAllResponse);
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
                        mPresenter.CommendContnet(false,starCommendResponses.get(position).getB02Id(),String.valueOf(0),content);
                        edit.setText("");
                        UserInfo userInfo = SysApplication.getUserInfo();
                        String userId = userInfo.getUserId();
                        if(TextUtils.isEmpty(userId)||null==userId){
                            //去登陆
                            Intent it=new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(it);
                        }else {
                            mPresenter.loadAllCommend(false, starCommendResponses.get(position).getB02Id(), userId, commendPage, commendRows, 0, 10);
                        }
                    }


                }
                return false;
            }
        });
    }

    public void onClick(final List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses){
        recyclerViewAdapter.setItemInformOnClick(new ShakeStarRecyclerViewAdapter.ItemInformOnClick() {
            @Override
            public void onInformClick(final String fkB02) {
                MDialog.show(getActivity(), "是否确认举报？", new MDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick() {
                        mPresenter.userInform(false,fkB02);
                        ToastUtil.show(getActivity(),"已举报！",0);
                    }
                });
            }
        });
        recyclerViewAdapter.setItemCommendOnClickListener(new ShakeStarRecyclerViewAdapter.ItemCommendOnClickListener() {
            @Override
            public void CommendClick(final int position, ImageView img) {
                ShakeStarAttentionFragment.position=position;
                UserInfo userInfo = SysApplication.getUserInfo();
                String userId = userInfo.getUserId();
                L.e("TAG",userId);
                if(TextUtils.isEmpty(userId)||null==userId){
                    //去登陆
                    Intent it=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(it);
                }else{

                    // 设置popWindow的显示和消失动画
                    popup.setAnimationStyle(R.style.showPopupAnimation);
                    if(popup.isShowing()){
                        //消失
                        popup.dismiss();
                        myCommentAllAdapter.notifyDataSetChanged();
                    }else{

                        popup.showAtLocation(img, Gravity.BOTTOM, 0, 0);
                        backgroundAlpha(0.5f);
                    }
                    mPresenter.loadAllCommend(false,starCommendResponses.get(position).getB02Id(),userId,commendPage,commendRows,0,10);
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
        recyclerViewAdapter.setItemSelectOnClickListener(new ShakeStarRecyclerViewAdapter.ItemSelectOnClickListener() {
            @Override
            public void SelectClick(int position) {
                Intent it = null;
                //跳转到素材详情
                if(starCommendResponses.get(position).getMaterialType()==TYPEA){
                    it=new Intent(getActivity(), ParticularsActivity.class);
                    it.putExtra("g03",starCommendResponses.get(position).getG03Id());
                    it.putExtra("b02",starCommendResponses.get(position).getB02Id());
                }else if(starCommendResponses.get(position).getMaterialType()==TYPEB){
                    it=new Intent(getActivity(), JointActivity.class);
                    it.putExtra("g03",starCommendResponses.get(position).getG03Id());
                    it.putExtra("b02",starCommendResponses.get(position).getB02Id());
                }
                getActivity().startActivity(it);
            }
        });

        recyclerViewAdapter.setItemLikeOnClickListener(new ShakeStarRecyclerViewAdapter.ItemLikeOnClickListener() {


            @Override
            public void LikeClick(ShakeStarCommendResponse.ShakingStarListBean model, boolean like) {
//             //点赞
//              if(type==1){
//              mPresenter.Like(false,contentId,type);
//              }else if(type==2){
//                  mPresenter.Like(false,contentId,type);
//              }
                mPresenter.Like(false,model.getB02Id(),like ? 1 : 2);
            }
        });
        recyclerViewAdapter.setItemShareOnClickListener(new ShakeStarRecyclerViewAdapter.ItemShareOnClickListener() {
            @Override
            public void ShareClick(int position) {

                String url = RequestUrl.SHAKESTAR_URL + "header="
                        + HeaderRequest.SHAKESTAR_DETAIL + "&b02Id=" + starCommendResponses.get(position).getB02Id();

                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_logo);
                String nickName = starCommendResponses.get(position).getNickName();
                String createTime = starCommendResponses.get(position).getCreateTime();
                ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getActivity());
                builder.setTitle(nickName);
                builder.setContent(starCommendResponses.get(position).getContentDesc());
//        L.e("TAG",starCommendResponses.get(position).getVideoThumbnail());
//        builder.setImgUrl(starCommendResponses.get(position).getVideoThumbnail());
                builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
                builder.setBitmap(bmp);
                builder.setLinkUrl(url);
                builder.create();
            }
        });
        recyclerViewAdapter.setItemMyOnClickListener(new ShakeStarRecyclerViewAdapter.ItemMyOnClickListener() {
            @Override
            public void MyOnClick(int position) {
                Intent it=new Intent(getContext(), MyActivity.class);
                it.putExtra("position",position);
                it.putExtra("b02Id",starCommendResponses.get(position).getB02Id());
                it.putExtra("g03Id",starCommendResponses.get(position).getG03Id());
                it.putExtra("type",starCommendResponses.get(position).getMaterialType());
                it.putExtra("fkA01",starCommendResponses.get(position).getFkA01());
                startActivity(it);
            }
        });
    }

    @Override
    public void onRefresh() {
//        page = 0;
//        if (!this.isAdded()) {
//            return;
//        }
        recyclerViewAdapter.notifyDataSetChanged();
        mViewBinding.shakeAttentionSwip.setRefreshing(false);
    }
    private boolean isTop;
    private RecyclerView.LayoutManager getLayoutManager() {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.shakeAttentionList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            page++;
                            mPresenter.loadData(false,sortType,page,rows);
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

}
