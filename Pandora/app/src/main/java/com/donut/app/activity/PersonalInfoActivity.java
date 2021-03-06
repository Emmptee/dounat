package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseRequest;
import com.donut.app.http.message.UserInfoResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PersonalInfoActivity extends BaseActivity
{
    @ViewInject(R.id.per_header)
    private ImageView mHeader;

    @ViewInject(R.id.tv_name)
    private TextView mTvName;

    @ViewInject(R.id.tv_sex)
    private TextView mTvSex;

    //@ViewInject(R.id.tv_city)
   // private TextView mTvCity;

    @ViewInject(R.id.tv_phone)
    private TextView mTvPhone;

    @ViewInject(R.id.tv_age)
    private TextView mTvAge;

    @ViewInject(R.id.tv_star)
    private TextView mTvStar;

    @ViewInject(R.id.tv_professional)
    private TextView mTvProfes;

    @ViewInject(R.id.vip_tips)
    private TextView mTvipTips;

    private static final int PERSONAL_INFO_REQUEST = 0;

    private static final int MODIFY_HEAD_REQUEST_CODE = 1;
    private static final int MODIFY_NAME_REQUEST_CODE = 2;
    private static final int MODIFY_AGE_REQUEST_CODE = 3;
    private static final int MODIFY_SEX_REQUEST_CODE = 4;
    private static final int MODIFY_PRO_REQUEST_CODE = 5;
    private static final int MODIFY_STAR_REQUEST_CODE = 6;
    private static final int MODIFY_CITY_REQUEST_CODE = 7;
    private static final int MODIFY_PHONE_REQUEST_CODE = 8;

    private Integer flag, isBanding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle("个人信息", true);

        requestData();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.PERSONAL_INFO.getCode() + "00", request, HeaderRequest.GET_PERSONAL_INFO);
    }

    @Override
    protected void onStop()
    {
        saveBehaviour("xx");
        super.onStop();
    }

    @Override
    public void onBackPressed()
    {
        saveBehaviour("08");
        super.onBackPressed();
    }

    BaseRequest request;

    private void requestData()
    {
        request = new BaseRequest();
        sendNetRequest(request, HeaderRequest.GET_PERSONAL_INFO, PERSONAL_INFO_REQUEST,
                false);
    }

    @OnClick({R.id.header_layout, R.id.nickname_layout, R.id.sex_layout, R.id.city_layout, R.id.age_layout
            , R.id.star_sign_layout, R.id.professional_layout, R.id.vip_tips, R.id.phone_bind_layout})
    private void viewOnClick(View v)
    {

        switch (v.getId())
        {
            case R.id.header_layout:
                //头像
                saveBehaviour("01");
                Bundle head_bund = new Bundle();
                head_bund.putString(HeadModifyActivity.HEADURL, headurl);
                launchActivityForResult(HeadModifyActivity.class, head_bund, MODIFY_HEAD_REQUEST_CODE);
                break;
            case R.id.nickname_layout:
                //昵称
                saveBehaviour("02");
                Bundle name_bund = new Bundle();
                name_bund.putString(BasicInfoModifyActivity.CONTENT, nickname);
                name_bund.putInt(BasicInfoModifyActivity.TYPE, BasicInfoModifyActivity.TYPE_NICKNAME);
                launchActivityForResult(BasicInfoModifyActivity.class, name_bund, MODIFY_NAME_REQUEST_CODE);
                break;
            case R.id.sex_layout:
                //性别
                saveBehaviour("04");
                Bundle sex_bund = new Bundle();
                if (sex == null)
                {
                    sex = "0";
                }
                sex_bund.putInt(SexModifyActivity.SEX, Integer.parseInt(sex));
                launchActivityForResult(SexModifyActivity.class, sex_bund, MODIFY_SEX_REQUEST_CODE);
                break;
            case R.id.city_layout:
                //城市
//                launchActivityForResult(CityListActivity.class, MODIFY_CITY_REQUEST_CODE);
                break;
            case R.id.age_layout:
                //年龄
                saveBehaviour("05");
                Bundle age_bund = new Bundle();
                age_bund.putString(BasicInfoModifyActivity.CONTENT, age);
                age_bund.putInt(BasicInfoModifyActivity.TYPE, BasicInfoModifyActivity.TYPE_AGE);
                launchActivityForResult(BasicInfoModifyActivity.class, age_bund, MODIFY_AGE_REQUEST_CODE);
                break;
            case R.id.star_sign_layout:
                //星座
                saveBehaviour("06");
                Bundle sign_bund = new Bundle();
                if (star == null || star == 0)
                {
                    star = 1;
                }
                sign_bund.putInt(StarSignActivity.SIGN, star);
                launchActivityForResult(StarSignActivity.class, sign_bund, MODIFY_STAR_REQUEST_CODE);
                break;
            case R.id.professional_layout:
                //职业
                saveBehaviour("07");
                Bundle pro_bund = new Bundle();
                pro_bund.putString(JobSignActivity.SELECTED_NAME, professional);
                launchActivityForResult(JobSignActivity.class, pro_bund, MODIFY_PRO_REQUEST_CODE);
                break;
            case R.id.vip_tips:
                if (flag == 1)
                {
                    launchActivity(VipActivity.class);
                }
                break;
            case R.id.phone_bind_layout:
                saveBehaviour("03");
                if (getUserInfo().getThirdTag() == 1 && isBanding == 1)
                {
                    //三方登陆并且没有绑定过手机号
                    launchActivityForResult(PersonalBandingPhoneActivity.class, MODIFY_PHONE_REQUEST_CODE);
                } else
                {
                    launchActivity(ModifyBandingPhoneActivity.class);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case PERSONAL_INFO_REQUEST:
                UserInfoResponse res = JsonUtils.fromJson(response,
                        UserInfoResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode()))
                {
                    showView(res);
                } else
                {
                    ToastUtil.showShort(this, res.getMsg());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case MODIFY_HEAD_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        headurl = data.getStringExtra(HeadModifyActivity.HEADURL);
                        Glide.with(this)
                                .load(headurl)
                                .centerCrop()
                                .placeholder(R.drawable.default_header)
                                .error(R.drawable.default_header)
                                .transform(new GlideCircleTransform(this))
                                .into(mHeader);
                        requestData();
                    }
                }
                break;
            case MODIFY_NAME_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        nickname = data.getStringExtra(BasicInfoModifyActivity.CONTENT);
                        mTvName.setText(CommonUtils.setContent(this, nickname));
                        requestData();
                    }
                }
                break;
            case MODIFY_AGE_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        age = data.getStringExtra(BasicInfoModifyActivity.CONTENT);
                        mTvAge.setText(CommonUtils.setContent(this, age));
                        requestData();
                    }
                }
                break;
            case MODIFY_SEX_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        sex = data.getIntExtra(SexModifyActivity.SEX, -1) + "";
                        mTvSex.setText(CommonUtils.setSex(this, sex));
                        requestData();
                    }
                }
                break;
            case MODIFY_STAR_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        star = data.getIntExtra(StarSignActivity.SIGN, -1);
                        mTvStar.setText(CommonUtils.setStar(this, star));
                        requestData();
                    }
                }
                break;
//            case MODIFY_CITY_REQUEST_CODE:
//                if(resultCode==RESULT_OK){
//                    if(data!=null){
//                        city=data.getStringExtra(CityListActivity.CITY);
//                        mTvCity.setText(CommonUtils.setContent(this,city));
//                        checkCompleteState();
//                    }
//                }
//                break;
            case MODIFY_PRO_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        professional = data.getStringExtra(JobSignActivity.SELECTED_NAME);
                        mTvProfes.setText(CommonUtils.setContent(this, professional));
                        requestData();
                    }
                }
                break;
            case MODIFY_PHONE_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    mTvPhone.setText("已绑定");
                    requestData();
                }
                break;
        }
    }

    String headurl, nickname, age, professional, sex, days;
    Integer star;

    private void showView(UserInfoResponse res)
    {
        headurl = res.getHeadPic();
        nickname = res.getNickName();
        sex = res.getSex();
        age = res.getAge();
        star = res.getStar();
        professional = res.getJob();
//        jobCode = res.getJobCode();
        Glide.with(this)
                .load(headurl)
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(mHeader);
        mTvName.setText(CommonUtils.setContent(this, nickname));
        mTvSex.setText(CommonUtils.setSex(this, sex));
        mTvAge.setText(CommonUtils.setContent(this, age));
        mTvStar.setText(CommonUtils.setStar(this, star));
        mTvProfes.setText(CommonUtils.setContent(this, professional));
        days = res.getDays();
        flag = res.getFlg();
        isBanding = res.getIsBindingPhone();
        if (isBanding == 0)
        {
            mTvPhone.setText("已绑定");
        } else
        {
            mTvPhone.setText("未绑定");
        }
        if (res.getFlg() != null && res.getFlg() == 1)
        {
            showVipTips();
        } else
        {
            mTvipTips.setVisibility(View.VISIBLE);
            mTvipTips.setText("首次全部完善个人信息，我们将送您" + days + "天会员。");
        }
    }

    private void showVipTips()
    {
        long time = sp_Info.getLong(Constant.VIP_TIME, 0);
        if (time > 0 && System.currentTimeMillis() - time < 86400000)
        {
            mTvipTips.setVisibility(View.VISIBLE);
            mTvipTips.setText(Html.fromHtml("<font color='#666666'>"
                    + "已送您" + days + "天会员，请注意到期时间，"
                    + " </font><font color='#81D8D0'> 去看看吧！</font>"));
        } else
        {
            mTvipTips.setVisibility(View.GONE);
        }
    }


    private void saveBehaviour(String id)
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.PERSONAL_INFO.getCode() + id);
    }


}
