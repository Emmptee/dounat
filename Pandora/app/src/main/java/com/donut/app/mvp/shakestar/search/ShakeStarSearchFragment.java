package com.donut.app.mvp.shakestar.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.donut.app.adapter.MyAttentionRecyclerViewAdapter;
import com.donut.app.adapter.MyCommentAllAdapter;
import com.donut.app.adapter.ShakeStarRecyclerViewAdapter;
import com.donut.app.adapter.ShakeStarSelectAdapter;
import com.donut.app.databinding.ShakeSearchFragmentBinding;
import com.donut.app.entity.UserInfo;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.MyFollowListResponse;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.shakestar.ItemDecoration;
import com.donut.app.mvp.shakestar.MDialog;
import com.donut.app.mvp.shakestar.commend.ShakeStarCommendFragment;
import com.donut.app.mvp.shakestar.commend.ShakeStarSelectActivity;
import com.donut.app.mvp.shakestar.mine.MyActivity;
import com.donut.app.mvp.shakestar.select.SelectPresenter;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.utils.L;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.umeng.qq.tencent.l;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 搜索页面
 * Created by hard on 2018/1/29.
 */

public class ShakeStarSearchFragment extends MVPBaseFragment<ShakeSearchFragmentBinding,SearchPresenter>
        implements SearchContract.View,SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "ShakeStarSearchFragment";

    private static int position;
    private String name=null;
    private int page = 0, rows = 10, sortType = 0;
    private static final int TYPEA=0;//同屏
    private static final int TYPEB=1;//拼接
    private ShakeStarRecyclerViewAdapter recyclerViewAdapter;
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
    @Override
    protected int getLayoutId() {
        return R.layout.shake_search_fragment;
    }


    @Override
    protected void initView() {
        linearLayoutManager= (LinearLayoutManager) getLayoutManager();
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mViewBinding.searchList.setLayoutManager(linearLayoutManager);
        WindowManager wm =(WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        showKeyBoard();
    }

    @Override
    protected void loadData() {

        ItemDecoration itemDecoration=new ItemDecoration(getActivity());
        mViewBinding.searchList.addItemDecoration(itemDecoration);
        mViewBinding.gzImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择素材
                Intent it=new Intent(getActivity(), ShakeStarSelectActivity.class);
                startActivity(it);
//                l.e("这个是tosi");
//                ToastUtil.showLong(getActivity(),"敬请期待");
            }
        });
        initEvent();
    }

    @Override
    public void showView(final List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses) {
        this.starCommendResponses=starCommendResponses;
         mPresenter.setIsInfo(new SearchPresenter.IsInfo() {
             @Override
             public void Info(int k) {
                 if(k==0){
                     mViewBinding.searchSwip.setVisibility(View.VISIBLE);
                     mViewBinding.gzImg.setVisibility(View.VISIBLE);
                     mViewBinding.shakeSearchTvMsg.setVisibility(View.GONE);
                 }else{
                     mViewBinding.searchSwip.setVisibility(View.GONE);
                     mViewBinding.gzImg.setVisibility(View.VISIBLE);
                     mViewBinding.shakeSearchTvMsg.setVisibility(View.VISIBLE);
                 }
             }
         });

        if(recyclerViewAdapter==null){
            mViewBinding.searchSwip.setOnRefreshListener(this);
            recyclerViewAdapter=new ShakeStarRecyclerViewAdapter(starCommendResponses,null,getActivity());
            mViewBinding.searchList.setAdapter(recyclerViewAdapter);
            snapPagerHelper.attachToRecyclerView(mViewBinding.searchList);
        }else{
            recyclerViewAdapter.notifyDataSetChanged();
        }
        //关注
        recyclerViewAdapter.setItemAttentionOnClick(new ShakeStarRecyclerViewAdapter.ItemAttentionOnClick() {
            @Override
            public void Attention(String starId, String operation) {
                mPresenter.loadAttention(false,starId,operation);
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
         if(starCommendResponses==null){
             mViewBinding.searchSwip.setRefreshing(false);
         }
        recyclerViewAdapter.setItemCommendOnClickListener(new ShakeStarRecyclerViewAdapter.ItemCommendOnClickListener() {
            @Override
            public void CommendClick(final int position, ImageView img) {
                ShakeStarSearchFragment.position=position;
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
    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse,CommendAllResponse response) {
        //获取全部评论
        sum.setText(String.valueOf(response.getCommentTimes()));
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

    @Override
    public void onRefresh() {
//        page = 0;
//        if (!this.isAdded()) {
//            return;
//        }
        if(recyclerViewAdapter!=null){
            recyclerViewAdapter.notifyDataSetChanged();
        }
        mViewBinding.searchSwip.setRefreshing(false);
    }
    private boolean isTop;
    private RecyclerView.LayoutManager getLayoutManager() {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.searchList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            page++;
                            load();
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

    private void showKeyBoard()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                InputMethodManager inputManager =
                        (InputMethodManager)  mViewBinding.shakeSearchIvEtClean.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput( mViewBinding.shakeSearchIvEtClean, 0);
            }

        }, 500);
    }

    public void initEvent()
    {
        view = View.inflate(getActivity(), R.layout.comment_pop,null);
        back=view.findViewById(R.id.pop_back);

        commentRecyclerView=view.findViewById(R.id.comment_recyclerview);
        sum=view.findViewById(R.id.commend_sum);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemDecoration itemDecoration=new ItemDecoration(getActivity());
        commentRecyclerView.addItemDecoration(itemDecoration);
        edit=view.findViewById(R.id.commend_edit);
        Log.i(TAG, "onEditorAction: 评论被调用");
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
        mViewBinding.shakeSearchEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 0)
                {
                    mViewBinding.shakeSearchIvEtClean.setVisibility(View.GONE);
                } else
                {
                    mViewBinding.shakeSearchIvEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewBinding.shakeSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {

                    name=mViewBinding.shakeSearchEt.getText().toString().trim();
                    load();
                }
                return false;
            }
        });
        mViewBinding.shakeSearchIvEtClean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name=mViewBinding.shakeSearchEt.getText().toString().trim();
                if(!TextUtils.isEmpty(name)){
                    mViewBinding.shakeSearchEt.setText("");
                }else{
                    showToast("请输入搜索内容");
                }

            }
        });

    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
    public void load(){
        mPresenter.loadData(false,name,sortType,page,rows);
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }
}
