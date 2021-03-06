package com.donut.app.activity;

import android.support.v7.app.AppCompatActivity;
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
import com.donut.app.http.RequestUrl;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import static com.donut.app.http.RequestUrl.IP_CLAUSE_URL;

public class H5ReadDocActivity extends BaseActivity {

    @ViewInject(R.id.activity_h5_read_doc_web)
    private WebView webView;

    @ViewInject(R.id.noData)
    private TextView noData;

    @ViewInject(R.id.webView_pb)
    private ProgressBar pb;

    public static final String DOC_URL = "DOC_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_read_doc);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle("文档阅读", true);

        ViewUtils.inject(this);

        initData();
    }

    private void initData() {

        String docUrl = getIntent().getStringExtra(DOC_URL);
        //优先使用缓存
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(newProgress);
                if (newProgress == 100)
                {
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
            {
//                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//                noData.setVisibility(View.VISIBLE);
                super.onReceivedHttpError(view, request, errorResponse);
            }

        });
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(docUrl);
    }

    @Override
     public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
