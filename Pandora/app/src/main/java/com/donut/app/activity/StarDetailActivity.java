package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.fragment.StarIndexFragment;
import com.donut.app.fragment.StarMomentsFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ConcernedOnStarRequest;
import com.donut.app.http.message.subjectStar.SubjectStarRequest;
import com.donut.app.http.message.subjectStar.SubjectStarResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class StarDetailActivity extends BaseActivity {

    @ViewInject(R.id.star_detail_iv_head)
    private ImageView ivHead;

    @ViewInject(R.id.star_detail_tv_name)
    private TextView tvName;

    @ViewInject(R.id.star_detail_tv_fans)
    private TextView tvFans;

    @ViewInject(R.id.star_detail_tv_personal_info)
    private TextView tvPersonalInfo;

    @ViewInject(R.id.star_detail_tv_index)
    private TextView tvIndex;

    @ViewInject(R.id.star_detail_tv_moments)
    private TextView tvMoments;

    @ViewInject(R.id.star_detail_cb_concerned)
    private CheckBox cbConcerned;

    @ViewInject(R.id.star_detail_iv_vip)
    private ImageView ivVip;

    public static final String STAR_ID = "STAR_ID", FKA01_ID = "FKA01_ID",
            FOLLOW_STATUS = "FOLLOW_STATUS";

    private String starId = "";

    public static final int INDEX_REQUEST = 1, CONCERNED_ON_STAR = 2;

    private FragmentManager fragmentManager;
    private Fragment starIndexFragment, starMomentsFragment;
    private static final String INDEX_TAG = "INDEX_TAG", MOMENTS_TAG = "MOMENTS_TAG";

    private long fansNum;

    private int operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_detail);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("明星空间", true);
        initView();
        loadData();
    }

    private SubjectStarRequest indexRequest;

    private void loadData() {
        starId = getIntent().getStringExtra(STAR_ID);
        String fka01Id = getIntent().getStringExtra(FKA01_ID);

        indexRequest = new SubjectStarRequest();
        indexRequest.setStarId(starId);
        indexRequest.setFkA01Id(fka01Id);
        indexRequest.setCurrentUserId(getUserInfo().getUserId());
        sendNetRequest(indexRequest, HeaderRequest.STAR_INDEX_REQUEST, INDEX_REQUEST);
    }

    private void initView() {
        tvIndex.setSelected(true);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case INDEX_REQUEST:
                SubjectStarResponse indexResponse = JsonUtils.fromJson(response, SubjectStarResponse.class);
                if (COMMON_SUCCESS.equals(indexResponse.getCode())) {
                    showView(indexResponse);
                } else {
                    showToast(indexResponse.getMsg());
                }
                break;
            case CONCERNED_ON_STAR:
                cbConcerned.setClickable(true);
                BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    if (0 == operation) {
                        cbConcerned.setText("已关注");
                        fansNum++;
                    } else if (1 == operation) {
                        cbConcerned.setText("+关注");
                        fansNum--;
                    }
                    if (fansNum <= 10000) {
                        tvFans.setText("粉丝 " + fansNum);
                    }
                } else {
                    showToast(res.getMsg());
                }
                break;
        }
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        super.onError(errorMsg, url, actionId);
        cbConcerned.setClickable(true);
    }

    private void showView(SubjectStarResponse indexResponse) {
        starId = indexResponse.getStarId();
        Glide.with(this)
                .load(indexResponse.getHeadPic())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(ivHead);
        if (indexResponse.getIsMember() == 1) {
            ivVip.setVisibility(View.VISIBLE);
        }
        tvName.setText(indexResponse.getName());
        String strFansNum = "0";
        fansNum = indexResponse.getFollowNum();
        strFansNum = String.valueOf(fansNum);
        if (indexResponse.getFollowNum() > 10000) {
            strFansNum = indexResponse.getFollowNum() / 10000 + "万";
        }
        tvFans.setText("粉丝 " + strFansNum);

        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(indexResponse.getRole())) {
            builder.append(indexResponse.getRole());
            builder.append(" | ");
        }
        if (!TextUtils.isEmpty(indexResponse.getBirth())) {
            builder.append(indexResponse.getBirth());
            builder.append(" | ");
        }
        builder.append(getStrBlood(indexResponse.getBlood()));
        builder.append(" | ");
        if (indexResponse.getHeight() > 0) {
            builder.append(indexResponse.getHeight());
            builder.append("CM");
            builder.append(" | ");
        }
        if (indexResponse.getWeight() > 0) {
            builder.append(indexResponse.getWeight());
            builder.append("KG");
            builder.append(" | ");
        }
        builder.deleteCharAt(builder.lastIndexOf("|"));
        tvPersonalInfo.setText(builder.toString());
        operation = indexResponse.getIsConcerned();
        if (operation == 0) {
            cbConcerned.setText("已关注");
            cbConcerned.setChecked(true);
        } else {
            cbConcerned.setText("+关注");
            cbConcerned.setChecked(false);
        }
        cbConcerned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!getLoginStatus()) {
                    showToast(getString(R.string.no_login_msg));
                    launchActivity(LoginActivity.class);
                    cbConcerned.setChecked(false);
                    return;
                }
                ConcernedOnStarRequest request = new ConcernedOnStarRequest();
                request.setStarId(starId);
                if (isChecked) {
                    operation = 0;
                } else {
                    operation = 1;
                }
                request.setOperation(String.valueOf(operation));
                sendNetRequest(request, HeaderRequest.CONCERNED_ON_STAR, CONCERNED_ON_STAR);
                cbConcerned.setClickable(false);
                saveBehaviour("03", request, HeaderRequest.CONCERNED_ON_STAR);
            }
        });

        starIndexFragment = StarIndexFragment.newInstance(indexResponse);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.star_detail_layout_main, starIndexFragment, INDEX_TAG);
        fragmentTransaction.commit();

        starMomentsFragment = StarMomentsFragment.newInstance(starId,
                indexResponse.getName() + "," + indexResponse.getHeadPic());
    }

    private String getStrBlood(Integer blood) {
        if (blood == null) {
            blood = -1;
        }
        String strBlood;
        switch (blood) {
            case 0:
                strBlood = "A型";
                break;
            case 1:
                strBlood = "B型";
                break;
            case 2:
                strBlood = "AB型";
                break;
            case 3:
                strBlood = "O型";
                break;
            default:
                strBlood = "其他";
                break;

        }
        return strBlood;
    }

    @OnClick({R.id.star_detail_tv_index, R.id.star_detail_tv_moments})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.star_detail_tv_index:
                tvIndex.setSelected(true);
                tvMoments.setSelected(false);
                if (starIndexFragment != null && fragmentManager.findFragmentByTag(INDEX_TAG) == null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.star_detail_layout_main, starIndexFragment, INDEX_TAG);
                    fragmentTransaction.commit();
                    saveBehaviour("01");
                }
                break;
            case R.id.star_detail_tv_moments:
                tvMoments.setSelected(true);
                tvIndex.setSelected(false);
                if (starMomentsFragment != null && fragmentManager.findFragmentByTag(MOMENTS_TAG) == null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.star_detail_layout_main, starMomentsFragment, MOMENTS_TAG);
                    fragmentTransaction.commit();
                    saveBehaviour("02");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("04");
        Intent intent = new Intent();
        intent.putExtra(FOLLOW_STATUS, Math.abs(operation - 1));
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00", indexRequest, HeaderRequest.STAR_INDEX_REQUEST);
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_DETAIL.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_DETAIL.getCode() + functionCode, request, header);
    }
}
