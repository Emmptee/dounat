package com.donut.app.mvp.shakestar.commend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.blog.www.guideview.Component;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.MyCommentAllAdapter;
import com.donut.app.adapter.ShakeStarRecyclerViewAdapter;
import com.donut.app.databinding.ShakeCommendFragmentBinding;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.shakestar.ItemDecoration;
import com.donut.app.mvp.shakestar.MDialog;
import com.donut.app.mvp.shakestar.mine.MyActivity;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.mvp.shakestar.utils.ScrollCalculatorHelper;
import com.donut.app.utils.L;
import com.donut.app.utils.ToastUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;


import java.util.List;

/**
 * 推荐页面
 * Created by hard on 2018/1/29.
 */

public class ShakeStarCommendFragment extends MVPBaseFragment<ShakeCommendFragmentBinding,CommendPresenter>
        implements CommendContract.View,SwipeRefreshLayout.OnRefreshListener{

    private static int position;
    private ShakeStarRecyclerViewAdapter recyclerViewAdapter;
    private static final int TYPEA=0;//同屏
    private static final int TYPEB=1;//拼接
    private int page = 0, rows = 10, sortType = 0;
    private int commendPage = 0, commendRows = 10;
    private Bitmap bitmap = null;
    private PopupWindow popup;
    private View view;
    private static  int height;
    private ImageView back;
    private RecyclerView commentRecyclerView;
    private EditText edit;
    private TextView sum;
    private  MyCommentAllAdapter myCommentAllAdapter;
    private List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses;
    private ScrollCalculatorHelper scrollCalculatorHelper;
    private  LinearLayoutManager linearLayoutManager;
    private boolean mFull = false;
    private int playTop,playBottom;
    private  LinearSnapHelper   snapHelper= new LinearSnapHelper();//recyclerView扩展类
    private  PagerSnapHelper   snapPagerHelper= new PagerSnapHelper();//recyclerView扩展类
    private  SharedPreferences isIf;
    private ImageView btn;
    private Guide guide;
    @Override
    protected int getLayoutId() {
        return R.layout.shake_commend_fragment;
    }

    @Override
    protected void initView() {

        mViewBinding.shakeCommendSwip.setOnRefreshListener(this);
                    WindowManager wm =(WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
                     height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recyclerViewAdapter!=null){
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initEvent() {


        view = View.inflate(getActivity(), R.layout.comment_pop,null);
        back=view.findViewById(R.id.pop_back);
        sum=view.findViewById(R.id.commend_sum);
        commentRecyclerView=view.findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
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

        ImageView imageView=new ImageView(getActivity());
        RelativeLayout layout=new RelativeLayout(getActivity());
        imageView.setImageResource(R.drawable.video_add);
        layout.addView(imageView);
        //限定范围为屏幕一半的上下偏移180
        playTop = CommonUtil.getScreenHeight(getContext()) / 2 - CommonUtil.dip2px(getContext(), 180);
        playBottom = CommonUtil.getScreenHeight(getContext()) / 2 + CommonUtil.dip2px(getContext(), 180);

        mPresenter.loadData(false,sortType,page,rows);

        mViewBinding.gzImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择素材
                UserInfo userInfo = SysApplication.getUserInfo();
                String userId = userInfo.getUserId();
                if(TextUtils.isEmpty(userId)||null==userId){
                    //去登陆
                    Intent it=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(it);
                }else{
                    Intent it=new Intent(getActivity(), ShakeStarSelectActivity.class);
                    startActivity(it);
                }

            }
        });
    }

    @Override
    public void showView( List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses) {
          this.starCommendResponses=starCommendResponses;

        linearLayoutManager = (LinearLayoutManager) getLayoutManager();
       linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直展示
        mViewBinding.shakeCommendRecycler.setLayoutManager(linearLayoutManager);
            recyclerViewAdapter=new ShakeStarRecyclerViewAdapter(starCommendResponses,null,getActivity());
            mViewBinding.shakeCommendRecycler.setAdapter(recyclerViewAdapter);
        snapPagerHelper.attachToRecyclerView( mViewBinding.shakeCommendRecycler);
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.tj_video, playTop, playBottom);
        mViewBinding.shakeCommendRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
    public void showCommend(final List<CommendAllResponse.CommentsListBean> commendAllResponse,CommendAllResponse response) {
        sum.setText(String.valueOf(response.getCommentTimes()));

        //获取全部评论
        if(myCommentAllAdapter==null){
            myCommentAllAdapter=new MyCommentAllAdapter(getActivity(),commendAllResponse);
            commentRecyclerView.setAdapter(myCommentAllAdapter);
        }else{
            myCommentAllAdapter.notifyDataSetChanged();
        }

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
       //关注
        recyclerViewAdapter.setItemAttentionOnClick(new ShakeStarRecyclerViewAdapter.ItemAttentionOnClick() {
            @Override
            public void Attention(String starId, String operation) {
                mPresenter.loadAttention(false,starId,0+"");
            }
        });

        recyclerViewAdapter.setItemInformOnClick(new ShakeStarRecyclerViewAdapter.ItemInformOnClick() {
            @Override
            public void onInformClick(final String fkB02) {//举报弹框！
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
                ShakeStarCommendFragment.position=position;
                UserInfo userInfo = SysApplication.getUserInfo();
                String userId = userInfo.getUserId();
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
                    }else{
                        popup.showAtLocation(img, Gravity.BOTTOM, 0, 0);
                        backgroundAlpha(0.5f);
                        mPresenter.loadAllCommend(false,starCommendResponses.get(position).getB02Id(),userId,commendPage,commendRows,0,10);
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
      recyclerViewAdapter.setItemSelectOnClickListener(new ShakeStarRecyclerViewAdapter.ItemSelectOnClickListener() {
          @Override
          public void SelectClick(int position) {
              Intent it = null;
              //跳转到素材详情
             if(starCommendResponses.get(position).getMaterialType()==TYPEA){
                 it=new Intent(getActivity(), ParticularsActivity.class);
                 it.putExtra("g03",starCommendResponses.get(position).getG03Id());
                 it.putExtra("b02",starCommendResponses.get(position).getB02Id());
                 it.putExtra("TYPEA",TYPEA);
             }else if(starCommendResponses.get(position).getMaterialType()==TYPEB){
                 it=new Intent(getActivity(), JointActivity.class);
                 it.putExtra("g03",starCommendResponses.get(position).getG03Id());
                 it.putExtra("b02",starCommendResponses.get(position).getB02Id());
                 it.putExtra("TYPEB",TYPEB);
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
          public void ShareClick(final int position) {

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
           builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
            @Override
            public void onSuccessRes() {
                L.e("tag","分享成功！");

            }
        });
        builder.setBitmap(bmp);
        builder.setLinkUrl(url);
        builder.create();
        mPresenter.Share(false,starCommendResponses.get(position).getB02Id(),null);
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
        mViewBinding.shakeCommendSwip.setRefreshing(false);
    }
    private boolean isTop;
    private RecyclerView.LayoutManager getLayoutManager() {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.shakeCommendRecycler,
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
    public void commend(final String b02id){}

    @Override
    public void onResume() {
        super.onResume();
//       if("first".equals(isIf.getString("first",""))&&!TextUtils.isEmpty(isIf.getString("first",""))){
//
//       }
        SharedPreferences isIf =getActivity().getSharedPreferences("isIf", Context.MODE_PRIVATE);
        if(!TextUtils.isEmpty(isIf.getString("first",""))){
            return;
        }
        getView().post(new Runnable() {
            @Override
            public void run() {
                guideBuidle();
            }
        });

//        guideBuidle();
    }
    public void guideBuidle(){
        final GuideBuilder builder=  new GuideBuilder();
        builder.setTargetView(mViewBinding.gzImg)
                .setAlpha(100)
                .setHighTargetCorner(360)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.addComponent(new GuidePublishIllnessVideoInfo());
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(getActivity());
        isIf =getActivity().getSharedPreferences("isIf", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = isIf.edit();
        edit.putString("first","first");
        edit.commit();
    }

    public class GuidePublishIllnessVideoInfo implements Component {

        @Override
        public View getView(LayoutInflater inflater) {
            LinearLayout ll=new LinearLayout(inflater.getContext());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setLayoutParams(params);
            ImageView imageView = new ImageView(inflater.getContext());
            imageView.setImageResource(R.drawable.guide_u);
            ll.removeAllViews();
            ll.addView(imageView);
            return ll;
        }

        @Override
        public int getAnchor() {
            return Component.ANCHOR_LEFT;
        }

        @Override
        public int getFitPosition() {
            return Component.FIT_END;
        }

        @Override
        public int getXOffset() {
            return 20;
        }

        @Override
        public int getYOffset() {
            return -70;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (GSYVideoManager.instance().listener() != null) {
            GSYVideoManager.instance().listener().onCompletion();
        }
        GSYVideoManager.instance().releaseMediaPlayer();
    }
}
