package com.donut.app.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WebAdActivity extends BaseActivity {

    @ViewInject(R.id.webView)
    private WebView webView;

    @ViewInject(R.id.noData)
    private TextView noData;

    @ViewInject(R.id.webView_pb)
    private ProgressBar pb;

    public static final String TITLE = "TITLE", SKIP_ADDRESS = "SKIP_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_terms);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle(getIntent().getStringExtra(TITLE), true);

        ViewUtils.inject(this);

        initData();
    }

    private void initData() {
        WebSettings ws = webView.getSettings();
        //优先使用缓存
        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(true);
        ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        ws.setBuiltInZoomControls(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                //webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                //noData.setVisibility(View.VISIBLE);
                super.onReceivedHttpError(view, request, errorResponse);
            }

        });

        webView.loadUrl(getIntent().getStringExtra(SKIP_ADDRESS));
    }
}
