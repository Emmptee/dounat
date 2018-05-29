package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.FullyLinearLayoutManager;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.VipRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.InitOpenMemberResponse;
import com.donut.app.http.message.PayRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VipActivity extends BaseActivity implements VipRecyclerViewAdapter.OnRecyclerViewListener
{

    @ViewInject(R.id.vip_iv_user_header)
    private ImageView ivHeader;

    @ViewInject(R.id.vip_iv_user_header_v)
    private ImageView ivVipIcon;

    @ViewInject(R.id.vip_tv_user_name)
    private TextView tvName;

    @ViewInject(R.id.vip_tv_balance)
    private TextView tvBalance;

    @ViewInject(R.id.vip_tv_state)
    private TextView tvState;

    @ViewInject(R.id.vip_tv_date)
    private TextView tvDate;

    @ViewInject(R.id.vip_rv_list)
    private RecyclerView rvList;

    @ViewInject(R.id.vip_tv_pay)
    private TextView tvPay;

    private List<InitOpenMemberResponse.MemberDetail> memberList = new ArrayList<>();

    private VipRecyclerViewAdapter mAdapter;

    private static final int INIT_MEMBER = 1, VIP_SIGN = 2;

    private static final int RECHARGE = 1;

    private int memberStatus = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.vip_title_sign), true);
        ViewUtils.inject(this);
        initView();
        loadData();
    }

    private void loadData()
    {
        sendNetRequest(new Object(), HeaderRequest.INIT_MEMBER, INIT_MEMBER);
    }

    private void initView()
    {
        mAdapter = new VipRecyclerViewAdapter(memberList, this);
        rvList.setAdapter(mAdapter);
        rvList.setLayoutManager(new FullyLinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RECHARGE:
                tvBalance.setText(String.valueOf(getUserInfo().getmBalance()));
                break;
        }
    }

    @OnClick({R.id.vip_tv_recharge_linear,
            R.id.vip_tv_pay})
    protected void viewOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.vip_tv_recharge_linear:
                //充值
                launchActivityForResult(RechargeActivity.class, RECHARGE);
                saveBehaviour("01");
                break;
            case R.id.vip_tv_pay:
                //支付
                payVip();
                break;
        }
    }

    private void payVip() {

        if (memberDetail == null) {
            showToast("请选择开通会员类型");
            return;
        } else {
            try {
                if (getUserInfo().getmBalance() < memberDetail.getPrice()) {
//                    showToast(getString(R.string.reward_balance_error_msg));
                    Dialog dialog = new AlertDialog.Builder(this)
                            .setMessage(R.string.reward_balance_error_msg)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    launchActivityForResult(RechargeActivity.class, RECHARGE);
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .create();
                    dialog.show();
                    return;
                }
            } catch (Exception e) {
                showToast(getString(R.string.system_error));
                return;
            }
        }

        String msg = "您确定开通会员?";
        if (memberStatus == 1) {
            msg = "您确定续费会员?";
        }
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendData();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();

    }

    private void sendData()
    {
        PayRequest request = new PayRequest();
        request.setReasonType("2");
        request.setPayMoney(String.valueOf(memberDetail.getPrice()));
        request.setPayType("3");
        request.setMemberDefinitionId(memberDetail.getUuid());
        request.setLastTime(memberDetail.getLastDays());
        sendNetRequest(request, HeaderRequest.VIP_SIGN, VIP_SIGN);
        saveBehaviour("02", request, HeaderRequest.VIP_SIGN);
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case INIT_MEMBER:
                InitOpenMemberResponse memberResponse
                        = JsonUtils.fromJson(response, InitOpenMemberResponse.class);
                if (COMMON_SUCCESS.equals(memberResponse.getCode()))
                {
                    showView(memberResponse);
                } else
                {
                    showToast(memberResponse.getMsg());
                }
                break;
            case VIP_SIGN:
                BaseResponse baseResponse = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode()))
                {

                    UserInfo userInfo = getUserInfo();
                    userInfo.setmBalance(userInfo.getmBalance() - memberDetail.getPrice());
                    userInfo.setMemberStatus(1);
                    setUserInfo(userInfo, true);

                    if (memberStatus == 1) {
                        showToast("续费会员成功");
                    } else {
                        showToast("开通会员成功");
                    }
                    setResult(RESULT_OK);
                    finish();
                } else
                {
                    showToast(baseResponse.getMsg());
                }
                break;
        }
    }

    private void showView(InitOpenMemberResponse member)
    {
        if (member.getMemberStatus() == null)
        {
            memberStatus = 0;
        } else
        {
            memberStatus = member.getMemberStatus();
        }
        if (memberStatus == 1)
        {
            ivVipIcon.setVisibility(View.VISIBLE);
            tvPay.setText(R.string.vip_title_renewal);
            updateHeadTitle(getString(R.string.vip_title_renewal), true);
            tvState.setText("VIP会员");
            tvState.setTextColor(getResources().getColor(R.color.text_tiffany));

            try
            {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = format.parse(member.getExpireTime());
                DateFormat strFormat = new SimpleDateFormat("yyyy-MM-dd到期", Locale.CHINA);
                tvDate.setText(strFormat.format(date));
                tvDate.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (getUserInfo().getUserType() == 1) {
            float scale = getResources().getDisplayMetrics().density;
            int width = (int) (66.5f * scale + 0.5f);
            int padding = (int) (6.5f * scale + 0.5f);
            ViewGroup.LayoutParams params = ivHeader.getLayoutParams();
            params.height = width;
            params.width = width;
            ivHeader.setLayoutParams(params);
            ivHeader.setPadding(padding, padding, padding, padding);
            ivHeader.setBackgroundResource(R.drawable.icon_star_bg);
        }

        Glide.with(this)
                .load(member.getHeadPic())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(ivHeader);
        tvName.setText(CommonUtils.setName(this, member.getNickName()));
        tvBalance.setText(String.valueOf(member.getBalance()));
        memberList.addAll(member.getMemberList());
        memberDetail = member.getMemberList().get(0);
        mAdapter.setSelectUuid(memberDetail.getUuid());
        mAdapter.notifyDataSetChanged();

        UserInfo userInfo = getUserInfo();
        userInfo.setmBalance(member.getBalance());
        setUserInfo(userInfo, true);
    }

    private InitOpenMemberResponse.MemberDetail memberDetail;

    @Override
    public void onItemClick(InitOpenMemberResponse.MemberDetail detail)
    {
        memberDetail = detail;
    }

    @Override
    public void onBackPressed()
    {
        saveBehaviour("03");
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.VIP.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.VIP.getCode() + functionCode, request, header);
    }
}
