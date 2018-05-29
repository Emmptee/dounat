package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.RewardAmountRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.RewardAndCommentRequest;
import com.donut.app.http.message.RewardInfoResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class RewardActivity extends BaseActivity {

    @ViewInject(R.id.reward_iv_payer_header)
    private ImageView ivPayerHeader;

    @ViewInject(R.id.reward_tv_payer_name)
    private TextView tvPayerName;

    @ViewInject(R.id.reward_tv_balance)
    private TextView tvBalance;

    @ViewInject(R.id.reward_tv_payee_name)
    private TextView tvPayeeName;

    @ViewInject(R.id.reward_iv_payee_header)
    private ImageView ivPayeeHeader;

    @ViewInject(R.id.reward_list_select_amounts)
    private RecyclerView rvSelectAmounts;

    @ViewInject(R.id.reward_et_amount)
    private EditText etAmount;

    @ViewInject(R.id.reward_et_content)
    private EditText etContent;

    private RewardAmountRecyclerViewAdapter mAdapter;

    private static final int REWARD_INFO_GET = 1, REWARD_PAY = 2;

    private static final int RECHARGE = 1;

    public static final String PAYEE_NAME = "PAYEE_NAME", PAYEE_HEADER = "PAYEE_HEADER",
            CONTENT_ID = "CONTENT_ID", IS_STAR = "IS_STAR",
            REWARD_AMOUNT = "REWARD_AMOUNT", REWARD_CONTENT = "REWARD_CONTENT";

    private String contentId = "";

    private float mBalance = 0, mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        initView();
        loadData();
    }

    private void loadData() {
        sendNetRequest(new Object(), HeaderRequest.REWARD_INFO_GET, REWARD_INFO_GET);
    }

    private CharSequence oldSequence;

    private void initView() {
        updateHeadTitle(getString(R.string.reward), true);

        if (getIntent().getBooleanExtra(IS_STAR, false)) {
            float scale = getResources().getDisplayMetrics().density;
            int width = (int) (66.5f * scale + 0.5f);
            int padding = (int) (6.5f * scale + 0.5f);
            ViewGroup.LayoutParams params = ivPayeeHeader.getLayoutParams();
            params.height = width;
            params.width = width;
            ivPayeeHeader.setLayoutParams(params);
            ivPayeeHeader.setPadding(padding, padding, padding, padding);
            ivPayeeHeader.setBackgroundResource(R.drawable.icon_star_bg);
        }

        contentId = getIntent().getStringExtra(CONTENT_ID);
        tvPayeeName.setText(getIntent().getStringExtra(PAYEE_NAME));
        Glide.with(this)
                .load(getIntent().getStringExtra(PAYEE_HEADER))
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(ivPayeeHeader);

        mAdapter = new RewardAmountRecyclerViewAdapter(etAmount);
        rvSelectAmounts.setAdapter(mAdapter);
        rvSelectAmounts.setLayoutManager(new GridLayoutManager(this, 3));

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mAdapter.setAmount(0);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldSequence = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int editStart = etContent.getSelectionStart();
                int editEnd = etContent.getSelectionEnd();
                if (oldSequence.length() > Constant.LENGTH_512) {
                    showToast("评论内容限制512字以内");
                    s.delete(editStart - 1, editEnd);
                    etContent.setText(s);
                    if (editStart > Constant.LENGTH_512) {
                        editStart = Constant.LENGTH_512 + 1;
                    }
                    etContent.setSelection(editStart - 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECHARGE:
                tvBalance.setText(String.valueOf(getUserInfo().getmBalance()));
                break;
        }
    }

    @OnClick({R.id.reward_tv_recharge,
            R.id.reward_btn_pay})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.reward_tv_recharge:
                //充值
                launchActivityForResult(RechargeActivity.class, RECHARGE);
                saveBehaviour("01");
                break;
            case R.id.reward_btn_pay:
                //支付
                Dialog dialog = new AlertDialog.Builder(this)
                        .setMessage("确定打赏吗？")
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                sendData();
                            }
                        }).create();
                dialog.show();
                break;
        }
    }

    private long nowTime = 0;
    private void sendData() {

        int amount = mAdapter.getAmount();
        if (amount == 0 && TextUtils.isEmpty(etAmount.getText())) {
            showToast(getString(R.string.reward_error_msg));
            return;
        } else if (amount == 0) {
            amount = Integer.valueOf(etAmount.getText().toString());
        }
        if (amount == 0) {
            showToast(getString(R.string.reward_error_msg));
            return;
        }
        if (!TextUtils.isEmpty(tvBalance.getText())) {
            float balance = Float.valueOf(tvBalance.getText().toString());
            if (balance < amount) {
                showToast(getString(R.string.reward_balance_error_msg));
                return;
            }
            this.mBalance = balance - (float) amount;
            this.mAmount = amount;
        } else {
            showToast(getString(R.string.system_error));
            return;
        }

        long t = System.currentTimeMillis();
        if(t - nowTime <= 1000){
            nowTime = t;
            return;
        }

        RewardAndCommentRequest request = new RewardAndCommentRequest();
        request.setAmount(amount);
        request.setContentId(contentId);
        request.setContent(etContent.getText().toString());
        sendNetRequest(request, HeaderRequest.REWARD_PAY, REWARD_PAY);
        saveBehaviour("02", request, HeaderRequest.REWARD_PAY);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case REWARD_INFO_GET:
                RewardInfoResponse infoResponse = JsonUtils.fromJson(response, RewardInfoResponse.class);
                if (COMMON_SUCCESS.equals(infoResponse.getCode())) {
                    showTopView(infoResponse);
                } else {
                    showToast(infoResponse.getMsg());
                }
                break;
            case REWARD_PAY:
                BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    UserInfo userInfo = getUserInfo();
                    userInfo.setmBalance(mBalance);
                    setUserInfo(userInfo, true);
                    showToast("打赏成功");
                    Intent intent = new Intent();
                    intent.putExtra(REWARD_AMOUNT, this.mAmount);
                    intent.putExtra(REWARD_CONTENT, etContent.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showToast(res.getMsg());
                }
                break;
        }
    }

    private void showTopView(RewardInfoResponse infoResponse) {
        if (getUserInfo().getUserType() == 1) {
            float scale = getResources().getDisplayMetrics().density;
            int width = (int) (66.5f * scale + 0.5f);
            int padding = (int) (6.5f * scale + 0.5f);
            ViewGroup.LayoutParams params = ivPayerHeader.getLayoutParams();
            params.height = width;
            params.width = width;
            ivPayerHeader.setLayoutParams(params);
            ivPayerHeader.setPadding(padding, padding, padding, padding);
            ivPayerHeader.setBackgroundResource(R.drawable.icon_star_bg);
        }

        Glide.with(this)
                .load(infoResponse.getHeadPic())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(ivPayerHeader);
        tvPayerName.setText(CommonUtils.setName(this, infoResponse.getNickName()));
        tvBalance.setText(String.valueOf(infoResponse.getBalance()));

        UserInfo userInfo = getUserInfo();
        userInfo.setmBalance(infoResponse.getBalance());
        setUserInfo(userInfo, true);
    }

    @Override
    public void onBackPressed() {
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REWARD.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REWARD.getCode() + functionCode, request, header);
    }
}
