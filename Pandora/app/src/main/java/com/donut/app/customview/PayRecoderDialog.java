package com.donut.app.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.RechargeActivity;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PayRequest;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

/**
 * Created by Qi on 2016/8/16.
 */
public class PayRecoderDialog extends Dialog {

    @ViewInject(R.id.recoder_user_header_layout)
    private AutoRelativeLayout mRecoderUserHeaderLayout;

    @ViewInject(R.id.recoder_user_header)
    private ImageView mRecoderUserHeader;

    @ViewInject(R.id.recoder_nickname)
    private TextView mRecoderNickname;

    @ViewInject(R.id.recoder_balance)
    private static TextView mRecoderBalance;

    @ViewInject(R.id.recoder_pay)
    private TextView mRecoderPayTxt;

    private Context context;

    private float mBalance;

    private float mRecoderPay;

    private onPayListener payListener;

    private String starCommentId;

    public PayRecoderDialog(Context context, onPayListener listener, String starCommentId) {
        super(context, R.style.Theme_dialog);
        this.context = context;
        this.payListener = listener;
        this.starCommentId = starCommentId;
        View view = LayoutInflater.from(context).inflate(R.layout.pay_recoder_dialog, null);
        setContentView(view);
        ViewUtils.inject(this, view);
    }

    public void setContent(String nickName, String headUrl, float mBalance,
                           float mRecoderPay) {

        this.mBalance = mBalance;
        this.mRecoderPay = mRecoderPay;

        float scale = context.getResources().getDisplayMetrics().density;

        int width1 = (int) (45 * scale + 0.5f);
        int width2 = (int) (55 * scale + 0.5f);
        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(width1, width1);
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(width2, width2);
        param2.addRule(RelativeLayout.CENTER_IN_PARENT);
        mRecoderUserHeader.setLayoutParams(param2);
        mRecoderUserHeaderLayout.setLayoutParams(param3);
        mRecoderNickname.setText(CommonUtils.setName(context, nickName));
        Glide.with(context)
                .load(headUrl)
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(context))
                .into(mRecoderUserHeader);
        mRecoderBalance.setText(mBalance + " ");
        mRecoderPayTxt.setText(mRecoderPay + " ");
    }

    @OnClick({R.id.recoder_recharge, R.id.recoder_pay_cancle, R.id.recoder_pay_ok})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.recoder_recharge:
                Intent intent = new Intent(context, RechargeActivity.class);
                intent.putExtra(RechargeActivity.FROM_PAY_RECODE, true);
                context.startActivity(intent);
                break;
            case R.id.recoder_pay_cancle:
                this.dismiss();
                break;
            case R.id.recoder_pay_ok:
                //支付
                if (mBalance < mRecoderPay) {
                    ToastUtil.showShort(context, context.getString(R.string.reward_balance_error_msg));
                } else {

                    PayRequest request = new PayRequest();
                    request.setStarCommentId(starCommentId);
                    request.setPayMoney(String.valueOf(mRecoderPay));

                    SendNetRequestManager manager = new SendNetRequestManager(
                            requestListener);
                    mLoadController = manager.sendNetRequest(request, HeaderRequest.PAY_AUDIO);
                }
                break;
        }
    }

    private LoadController mLoadController = null;

    @Override
    public void dismiss() {
        if (mLoadController != null) {
            mLoadController.cancel();
            mLoadController = null;
        }
        super.dismiss();
    }

    private RequestManager.RequestListener requestListener = new RequestManager.RequestListener() {

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
            if ("0000".equals(res.getCode())) {
                mBalance -= mRecoderPay;

                try {
                    UserInfo info = SysApplication.getUserInfo();
                    info.setmBalance(mBalance);
                    SysApplication.getDb().saveOrUpdate(info);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                if (payListener != null) {
                    payListener.onPaySuccess();
                }
            } else if ("0002".equals(res.getCode())) {
                ToastUtil.showShort(context, context.getString(R.string.login_timeout_msg));
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                if (payListener != null) {
                    payListener.onPayFail(res.getCode(), res.getMsg());
                }
            }
            dismiss();
        }

        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long l, long l1, String s) {
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            if (payListener != null) {
                payListener.onPayFail("500", errorMsg);
            }
            dismiss();
        }
    };

    public interface onPayListener
    {
        void onPaySuccess();
        void onPayFail(String code, String msg);
    }

    static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                mRecoderBalance.setText(msg.obj.toString());
            }
            return false;
        }
    });

    public static void setBalanceView(float mBalance){
        Message msg = new Message();
        msg.what = 1;
        msg.obj = mBalance;
        handler.sendMessage(msg);
    }
}
