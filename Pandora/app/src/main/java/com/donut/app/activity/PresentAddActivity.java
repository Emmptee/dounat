package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PresentAddRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.FormatCheckUtil;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Locale;

public class PresentAddActivity extends BaseActivity {

    @ViewInject(R.id.head_right_tv)
    private TextView tvRight;

    @ViewInject(R.id.present_add_tv_chooseBankType)
    private TextView tvChooseBankType;

    @ViewInject(R.id.present_add_et_donutNum)
    private EditText etDonutNum;

    @ViewInject(R.id.present_add_tv_amount_des)
    private TextView tvAmountDes;

    @ViewInject(R.id.present_add_tv_amount)
    private TextView tvAmount;

    @ViewInject(R.id.present_add_et_creditCardNum)
    private EditText etCreditCardNum;

    @ViewInject(R.id.present_add_et_cardHolder)
    private EditText etCardHolder;

    public static final String DONUT_NUM = "DONUT_NUM";

    private String[] bankArray = {"中国银行", "中国农业银行", "中国工商银行", "中国建设银行", "交通银行", "招商银行"};

    private static final int PRESENT_GET = 1, PRESENT_ADD = 2;

    private float donutNum, inputNum = 0;;

    private double ratio,rechargeRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_add);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("创建提现", true);
        tvRight.setText(R.string.submit);

        loadData();
        initView();
    }

    private void loadData() {
        try {
            donutNum = Float.valueOf(getIntent().getStringExtra(DONUT_NUM));
        } catch (Exception e) {
            showToast(getString(R.string.system_error));
            finish();
            return;
        }
        inputNum = (int) donutNum;
        etDonutNum.setText(String.valueOf((int) inputNum));
//        sendNetRequest(new Object(), HeaderRequest.PRESENT_GET, PRESENT_GET);
        ratio = sp_Info.getFloat(Constant.RATIO, 0f);
        rechargeRatio = sp_Info.getFloat(Constant.RECHARGR_RATIO, 0f);
        showView();
    }

    private void initView() {
        etDonutNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    inputNum = Float.parseFloat(s.toString());
                } catch (Exception e) {
                    inputNum = 0;
                    e.printStackTrace();
                }
                tvAmount.setText(String.format(Locale.CHINA, "%.2f", inputNum / ratio * (1 - rechargeRatio)) + "元");
            }
        });
    }

    @OnClick({R.id.present_add_tv_chooseBankType,
            R.id.menu})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.present_add_tv_chooseBankType:
                showBankView();
                break;
            case R.id.menu:
                saveData();
                break;
        }
    }

    private void showBankView() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setItems(bankArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvChooseBankType.setText(bankArray[which]);
                        tvChooseBankType.setTextColor(
                                PresentAddActivity.this.getResources().getColor(R.color.text_gray6));
                    }
                })
                .create();
        dialog.show();
    }

    private void saveData() {
        if(inputNum > donutNum){
            showToast("请输入正确提现麦圈");
            return;
        } else if (getString(R.string.present_add_choose_bank).equals(tvChooseBankType.getText().toString())) {
            showToast("请选择银行卡类型");
            return;
        } else if (TextUtils.isEmpty(etCreditCardNum.getText())) {
            showToast("请输入银行卡号");
            return;
        } else if (TextUtils.isEmpty(etCardHolder.getText())) {
            showToast("请输入开户人姓名");
            return;
        }

        String bankNo = etCreditCardNum.getText().toString();
        if (bankNo.length() < 16 || bankNo.length() > 19) {
            showToast("银行卡号长度必须在16到19之间");
            return;
        } else if (!FormatCheckUtil.checkBankCard(bankNo)) {
            showToast("银行卡号输入错误!");
            return;
        }

        PresentAddRequest request = new PresentAddRequest();
        request.setDonutNum(inputNum);
        request.setAmount((float) (inputNum / ratio * (1 - rechargeRatio)));
        request.setCreditCardType(tvChooseBankType.getText().toString());
        request.setCreditCardNum(etCreditCardNum.getText().toString());
        request.setCardHolder(etCardHolder.getText().toString());
        sendNetRequest(request, HeaderRequest.PRESENT_ADD, PRESENT_ADD);
        saveBehaviour("01", request, HeaderRequest.PRESENT_ADD);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId){
//            case PRESENT_GET:
//                PresentGetResponse getResponse = JsonUtils.fromJson(response, PresentGetResponse.class);
//                if (COMMON_SUCCESS.equals(getResponse.getCode())){
//                    ratio = getResponse.getRatio();
//                    showView();
//                }else {
//                    showToast(getResponse.getMsg());
//                    finish();
//                }
//                break;
            case PRESENT_ADD:
                BaseResponse baseResponse = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode())){
                    UserInfo userInfo = getUserInfo();
                    userInfo.setmBalance(0);
                    setUserInfo(userInfo, true);
                    showToast("创建提现成功,请等待系统审核");
                    setResult(RESULT_OK);
                    finish();
                }else {
                    showToast(baseResponse.getMsg());
                }
                break;
        }
    }

    private void showView() {
        tvAmountDes.setText("(" + (int) ratio + "麦圈等于1元)");
         tvAmount.setText(String.format(Locale.CHINA, "%.2f", inputNum / ratio*(1-rechargeRatio)) + "元");
    }

    @Override
    public void onBackPressed()
    {
        saveBehaviour("02");
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.PRESENT_ADD.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.PRESENT_ADD.getCode() + functionCode, request, header);
    }
}
