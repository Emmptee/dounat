package com.donut.app.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.L;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

public class BrowseDetailActivity extends BaseActivity
{

    private WebView detailContent;

    public static final String webUrl = "webUrl";

    public static final String title = "title";

    public static final String share = "share";

    @ViewInject(R.id.head_right_tv)
    private TextView right_iv;

    private String mTitle, mUrl;

    private boolean isShare = false;

    @ViewInject(R.id.menu)
    private AutoLinearLayout mRight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brouse_detail_layout);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        mTitle = getIntent().getStringExtra(title);
        mUrl = getIntent().getStringExtra(webUrl);
        isShare = getIntent().getBooleanExtra(share, false);
        if (isShare)
        {
            mRight.setVisibility(View.VISIBLE);
            right_iv.setText("分享");
        }
        else
        {
            mRight.setVisibility(View.GONE);
        }
        updateHeadTitle(mTitle, true);
        detailContent = (WebView) findViewById(R.id.detail_content);
        detailContent.getSettings().setLoadWithOverviewMode(true);
        detailContent.getSettings().setJavaScriptEnabled(true);
        detailContent.setOverScrollMode(View.OVER_SCROLL_NEVER);
        detailContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        detailContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        detailContent.getSettings().setUseWideViewPort(true);
        detailContent.loadUrl(mUrl);
        jumpDialog();
        detailContent.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                dissDialog();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                detailContent.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl)
            {
                L.i("wjj", "errorCode=====" + errorCode + "===="
                        + description);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        collectData();
    }

    private void collectData(){

        if(getString(R.string.recommended).equals(title)){
            SaveBehaviourDataService.startAction(this, BehaviourEnum.RECOMMAND_FRIENDS.getCode()+"00");
        }
    }

    @OnClick(value = { R.id.back, R.id.menu })
    private void btnClick(View v)
    {
        switch (v.getId())
        {
        case R.id.menu:

            if (isShare)
            {
                requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限", Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            break;
        case R.id.back:
            finish();
            break;
        default:

            break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        //推荐给朋友
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);
        builder.setTitle("国内首款支持线上互动的明星&素人PK类平台。");
        builder.setContent("国内外当热当红明星大咖，每周一组生活艺术“神技秀”，喊你来PK！明星&素人，共同打造最佳艺能CP！");
        builder.setLinkUrl(mUrl);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
        builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if(getString(R.string.recommended).equals(title)){
            SaveBehaviourDataService.startAction(this, BehaviourEnum.RECOMMAND_FRIENDS.getCode()+"01");
        }
    }

}
