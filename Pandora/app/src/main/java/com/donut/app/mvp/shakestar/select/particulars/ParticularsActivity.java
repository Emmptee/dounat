package com.donut.app.mvp.shakestar.select.particulars;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.adapter.ShakeStarParticularsAdapter;
import com.donut.app.databinding.ActivityParticularsLayoutBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.mvp.shakestar.select.ScrollInterceptScrollView;
import com.donut.app.mvp.shakestar.video.SourceVideoDownload.DownloadUtil;
import com.donut.app.mvp.shakestar.video.record.RecordActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.socks.library.KLog;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/2/1.
 */

public class ParticularsActivity extends MVPBaseActivity<ActivityParticularsLayoutBinding, ParticularsPresenter>implements ParticularsContract.View{

    private static final String TAG = "ParticularsActivity";
    public static final int REQUEST_FOR_RECORD = 1000;

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
    private String itemLastTime;

    private ProgressDialog mProgressDialog;
    private String videoPath;//下载路径
    private int max;
    private String sourceVideoUrl;
    private DownloadUtil mDownloadUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

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
        KLog.e("加载数据------");
        StatusBarCompat.translucentStatusBar(this);
        intent = getIntent();
        g03 = intent.getStringExtra("g03");
        b02 = intent.getStringExtra("b02");
        mPresenter.loadData(true,b02,g03,page,rows);
    }

    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> list,ParticularsResponse particularsResponse) {
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);//默认比例
        this.list=list;
//        mViewBinding.shakeCommendSwip.setEnabled(false);
        sourceVideoUrl = particularsResponse.getMaterialVideoList().get(0).getPlayUrl();
        videoPath = Environment.getExternalStorageDirectory().getPath() + "/DonutVideo/";
        mDownloadUtil = new DownloadUtil(4, videoPath, "download.mp4", sourceVideoUrl, this);

        if (particularsResponse.getShakingStarList() != null){

            mViewBinding.xqRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            if(particularsResponse.getShakingStarList().get(0).getVideoThumbnail()!=null){
                //不是首次拍摄
                KLog.e("不是首次拍摄");
                mViewBinding.xqMessage.setVisibility(View.GONE);
            }
            adapter=new ShakeStarParticularsAdapter(list,this);
            mViewBinding.xqRecyclerView.setAdapter(adapter);
            mViewBinding.xqRecyclerView.setNestedScrollingEnabled(false);
        }else {
            KLog.e("抖星列表为空");
            mViewBinding.xqMessage.setVisibility(View.VISIBLE);
            mViewBinding.xqRecyclerView.setVisibility(View.GONE);
        }
        BindingUtils.loadRoundImg(mViewBinding.particularsImg,particularsResponse.getStarHeadPic());
        mViewBinding.particularsContent.setText(particularsResponse.getTitle());
        mViewBinding.particularsUser.setText(particularsResponse.getStarName());
        mViewBinding.particularsNumber.setText(particularsResponse.getUseTimes()+"人使用");

        if(particularsResponse.getDisplay()==0){//素材在左
            KLog.e("素材在左边");
            mViewBinding.videoRight.setVisibility(View.GONE);
            mViewBinding.videoLeft.setVisibility(View.VISIBLE);
            mViewBinding.particularsPlayerLeft.setUp( particularsResponse.getMaterialVideoList().get(0).getPlayUrl(),false,null);
            ImageView backButton = mViewBinding.particularsPlayerLeft.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            ImageView fullscreenButton = mViewBinding.particularsPlayerLeft.getFullscreenButton();
            fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
            status=particularsResponse.getCollectionStatus();
            boolean wifi = NetUtils.isWifi(mContext);
            if(wifi){
                GSYBaseVideoPlayer player=mViewBinding.particularsPlayerLeft;
                //player.startPlayLogic();
            }
        }else if(particularsResponse.getDisplay()==1){//素材在右
            KLog.e("素材在右边");
            ImageView backButton = mViewBinding.particularsPlayerRight.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            ImageView fullscreenButton = mViewBinding.particularsPlayerRight.getFullscreenButton();
            fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
            status=particularsResponse.getCollectionStatus();
            mViewBinding.videoRight.setVisibility(View.VISIBLE);
            mViewBinding.videoLeft.setVisibility(View.GONE);
            mViewBinding.particularsPlayerRight.setUp( particularsResponse.getMaterialVideoList().get(0).getPlayUrl(),false,null);
            boolean wifi = NetUtils.isWifi(mContext);
            if(wifi){
                GSYBaseVideoPlayer player=mViewBinding.particularsPlayerRight;
                //player.startPlayLogic();
            }
            itemLastTime = getLastTime(particularsResponse.getMaterialVideoList().get(0).getPlayUrl());
            KLog.e("在particular中的时长" + itemLastTime);
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
        if (particularsResponse.getShakingStarList() != null){
            onClick(particularsResponse);
        }

        onClick2(particularsResponse);

    }

    private static String getLastTime(String url){
        String duration = null;
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(url);  //recordingFilePath（）为音频文件的路径
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取素材视频的时间
        duration = String.valueOf(player.getDuration());
        KLog.d("ACETEST", "### duration: " + duration);
        player.release();//释放资源
        return duration;
    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse
        Response) {

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
                     KLog.e("滑到底部");
//                    mPresenter.loadData(false,g03,b02,page,rows);
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



    }

    private void onClick2(final ParticularsResponse shakingStarListBeans){
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
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    public void newImagePic(View view) {
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        ToastUtil.showShort(getContext(),"精彩内容,敬请期待");
        initSourceVideo();
    }
    private void initSourceVideo(){
        mDownloadUtil.start();
        mDownloadUtil.setOnDownloadListener(new DownloadUtil.OnDownloadListener() {
            @Override
            public void downloadStart(int fileSize) {
                showProgressDialog();
                KLog.d("开始下载视频");
                max = fileSize;//文件总长度
            }

            @Override
            public void downloadProgress(int downloadedSize) {
                KLog.d("视频在下载中");
                mProgressDialog.setMessage("资源加载中..."+ downloadedSize * 100 / max + "%");

            }

            @Override
            public void downloadEnd() {
                KLog.e("视频下载结束");
                calcelProgressDialog();
                mDownloadUtil.pause();
                ParticularsEvent particularsEvent = new ParticularsEvent(itemLastTime);
                EventBus.getDefault().postSticky(particularsEvent);
                Intent it=new Intent(getContext(), RecordActivity.class);
                it.putExtra("g03",g03);
                it.putExtra("b02",b02);
                it.putExtra("mylasttime",itemLastTime);
                startActivityForResult(it,REQUEST_FOR_RECORD);
            }
        });
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Log.e("DAo","正在处理中");
        mProgressDialog.setMessage("资源加载中");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void calcelProgressDialog() {
        if( mProgressDialog !=null){
            mProgressDialog.cancel();
            mProgressDialog =null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_RECORD){
            if (resultCode == RESULT_OK){
                setResult(RESULT_OK);
                this.finish();
            }
        }
    }
}
