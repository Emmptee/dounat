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
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WebTermsActivity extends BaseActivity {

    @ViewInject(R.id.webView)
    private WebView webView;

    @ViewInject(R.id.noData)
    private TextView noData;

    @ViewInject(R.id.webView_pb)
    private ProgressBar pb;

    public static final String IS_CHALLENGE = "IS_CHALLENGE", IS_STAR_SEND_NOTICE = "IS_STAR_SEND_NOTICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_terms);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.web_terms_title),true);

        ViewUtils.inject(this);

        initData();
    }

    private void initData() {
        //优先使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient(){
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
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                //webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                //noData.setVisibility(View.VISIBLE);
                super.onReceivedHttpError(view, request, errorResponse);
            }

        });

        String url = RequestUrl.IP_CLAUSE_URL;
        if (getIntent().getBooleanExtra(IS_CHALLENGE, false)) {
            url = RequestUrl.CHALLENGE_CLAUSE_URL;
        } else if (getIntent().getBooleanExtra(IS_STAR_SEND_NOTICE,false)){
            url = RequestUrl.STAR_SEND_NOTICE_LAW_URL;
        }
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("01");
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00");
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_CONTENT_TERM.getCode() + functionCode);
    }
}
