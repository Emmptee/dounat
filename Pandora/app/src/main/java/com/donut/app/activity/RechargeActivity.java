package com.donut.app.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.FullyLinearLayoutManager;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.RechargeRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.PayRecoderDialog;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PayRequest;
import com.donut.app.http.message.PayResponse;
import com.donut.app.http.message.RechargeEntriesResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PayUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class RechargeActivity extends BaseActivity implements PayUtils.onPayListener {

    @ViewInject(R.id.recharge_iv_user_header)
    private ImageView ivHeader;

    @ViewInject(R.id.recharge_tv_user_name)
    private TextView tvUserName;

    @ViewInject(R.id.recharge_tv_balance)
    private TextView tvBalance;

    @ViewInject(R.id.recharge_rv_list)
    private RecyclerView rvList;

    @ViewInject(R.id.pay_rg_type)
    private RadioGroup rgPay;

    private int payType = -1;

    private RechargeRecyclerViewAdapter adapter;

    private static final int RECHARGE_ENTRIES = 1, PAY_REQUEST = 2;

    public static final String FROM_PAY_RECODE = "FROM_PAY_RECODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.recharge),true);

        loadData();
        initView();
    }

    private void initView() {

    }

    private void loadData() {
        sendNetRequest(new Object(), HeaderRequest.RECHARGE_ENTRIES, RECHARGE_ENTRIES);
    }

    @OnClick({R.id.recharge_tv_pay})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_tv_pay:
                double amount = adapter.getAmount();
                if (amount <= 0) {
                    showToast("请输入或选择充值麦圈");
                    break;
                }

                PayRequest request = new PayRequest();
                request.setReasonType("0");
                request.setPayMoney(String.valueOf(amount));
                request.setSubject(getString(R.string.app_name) + ":充值");
                request.setBody(getString(R.string.app_name) + ":充值");

                int checkedRadioButtonId = rgPay.getCheckedRadioButtonId();
                switch (checkedRadioButtonId) {
                    case R.id.pay_rb_weixin:
                        payType = 1;
                        break;
                    case R.id.pay_rb_alipay:
                        payType = 0;
                        break;
                    default:
                        showToast("请选择支付方式");
                        return;
                }
                request.setPayType(String.valueOf(payType));
                sendNetRequest(request, HeaderRequest.PAY_REQUEST, PAY_REQUEST);
                break;
        }
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case RECHARGE_ENTRIES:
                RechargeEntriesResponse entriesResponse
                        = JsonUtils.fromJson(response, RechargeEntriesResponse.class);
                if(COMMON_SUCCESS.equals(entriesResponse.getCode())) {
                    showView(entriesResponse);
                } else {
                    showToast(entriesResponse.getMsg());
                }
                break;
            case PAY_REQUEST:
                PayResponse payResponse = JsonUtils.fromJson(response, PayResponse.class);
                if (COMMON_SUCCESS.equals(payResponse.getCode())){
                    if (payType == 1) {
                        PayUtils.startWxPay(this, this, payResponse);
                    } else if (payType == 0) {
                        PayUtils.startAliPay(this, this, payResponse.getLanchPay());
                    }
                }else {
                    showToast(payResponse.getMsg());
                }
                break;
        }
    }

    private void showView(RechargeEntriesResponse entriesResponse) {
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
                .load(entriesResponse.getHeadPic())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(ivHeader);
        tvUserName.setText(CommonUtils.setName(this,entriesResponse.getNickName()));
        tvBalance.setText(String.valueOf(entriesResponse.getBalance()));

        UserInfo userInfo = getUserInfo();
        userInfo.setmBalance(entriesResponse.getBalance());
        setUserInfo(userInfo, true);

        adapter = new RechargeRecyclerViewAdapter(
                entriesResponse.getRechargeEntries(), entriesResponse.getRatio());
        adapter.setSelectAmount(entriesResponse.getRechargeEntries().get(0).getRealAmount());
        rvList.setAdapter(adapter);
        rvList.setLayoutManager(new FullyLinearLayoutManager(this));
    }

    boolean paySuccess = false;
    @Override
    public void onPaySuccess(int paytype)
    {
        showToast("充值成功!");
        paySuccess = true;
        UserInfo userInfo = getUserInfo();
        userInfo.setmBalance(userInfo.getmBalance() + (float) adapter.getMqAmount());
        setUserInfo(userInfo, true);
        tvBalance.setText(String.valueOf(userInfo.getmBalance()));

        if (getIntent().getBooleanExtra(FROM_PAY_RECODE, false)) {
            PayRecoderDialog.setBalanceView(userInfo.getmBalance());
        }
    }

    @Override
    public void onPayFail(int paytype, int stateCode)
    {
        paySuccess = false;
        if (paytype == 0)
        {
            if (stateCode == -1)
            {
                showToast(getString(R.string.pay_result_fail));
            }
            if (stateCode == -2)
            {
                showToast(getString(R.string.pay_result_cancle));
            }
        }
        else
        {
            // showToast(stateCode + "");
        }
    }

    @Override
    public void onBackPressed()
    {
        saveBehaviour("03");
        if(paySuccess) {
            setResult(RESULT_OK);
        }
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.RECHARGE.getCode() + functionCode);
    }

}
