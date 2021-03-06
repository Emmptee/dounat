package com.donut.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.IpRequest;
import com.donut.app.http.message.IpResponse;
import com.donut.app.http.message.StarListDetail;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 方案详情
 */
public class IpContentActivity extends BaseActivity {

    @ViewInject(R.id.ip_content_iv_playbill)
    private ImageView ivPlaybill;

    @ViewInject(R.id.ip_content_tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.ip_content_tv_content)
    private TextView tvContent;

    @ViewInject(R.id.ip_content_layout_star1)
    private FrameLayout layoutStar1;

    @ViewInject(R.id.ip_content_layout_star2)
    private FrameLayout layoutStar2;

    @ViewInject(R.id.ip_content_layout_star3)
    private FrameLayout layoutStar3;

    @ViewInject(R.id.ip_content_iv_head1)
    private ImageView ivHead1;

    @ViewInject(R.id.ip_content_iv_head2)
    private ImageView ivHead2;

    @ViewInject(R.id.ip_content_iv_head3)
    private ImageView ivHead3;

    @ViewInject(R.id.ip_content_tv_name1)
    private TextView tvName1;

    @ViewInject(R.id.ip_content_tv_name2)
    private TextView tvName2;

    @ViewInject(R.id.ip_content_tv_name3)
    private TextView tvName3;

    public static final String IP_ID = "IP_ID";

    private static final int IP_CONTENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_content);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("心愿详情", true);
        loadData();
    }

    private void loadData() {
        String ipId = getIntent().getStringExtra(IP_ID);
        IpRequest request = new IpRequest();
        request.setIpId(ipId);
        sendNetRequest(request, HeaderRequest.IP_CONTENT_REQUEST, IP_CONTENT_REQUEST);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case IP_CONTENT_REQUEST:

                IpResponse ipResponse = JsonUtils.fromJson(response, IpResponse.class);

                if (COMMON_SUCCESS.equals(ipResponse.getCode())) {
                    showView(ipResponse);
                } else {
                    showToast(ipResponse.getMsg());
                }
                break;
        }
    }

    private void showView(IpResponse ipResponse) {

        Glide.with(this)
                .load(ipResponse.getImgUrl())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(ivPlaybill);
        tvTitle.setText(ipResponse.getName());
        tvContent.setText(ipResponse.getDescription());

        List<StarListDetail> starList = ipResponse.getStarList();
        if (starList != null && starList.size() > 0) {

            for (int i = 0; i < starList.size(); i++) {
                StarListDetail detail = starList.get(i);
                switch (i)
                {
                    case 0:
                        layoutStar1.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(detail.getHeadPic())
                                .centerCrop()
                                .placeholder(R.drawable.default_header)
                                .error(R.drawable.default_header)
                                .transform(new GlideCircleTransform(this))
                                .into(ivHead1);
                        tvName1.setText(detail.getStarName());
                        break;
                    case 1:
                        layoutStar2.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(detail.getHeadPic())
                                .centerCrop()
                                .placeholder(R.drawable.default_header)
                                .error(R.drawable.default_header)
                                .transform(new GlideCircleTransform(this))
                                .into(ivHead2);
                        tvName2.setText(detail.getStarName());
                        break;
                    case 2:
                        layoutStar3.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(detail.getHeadPic())
                                .centerCrop()
                                .placeholder(R.drawable.default_header)
                                .error(R.drawable.default_header)
                                .transform(new GlideCircleTransform(this))
                                .into(ivHead3);
                        tvName3.setText(detail.getStarName());
                        break;
                }
            }
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
        }
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_CONTENT.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_CONTENT.getCode() + functionCode, request, header);
    }
}
