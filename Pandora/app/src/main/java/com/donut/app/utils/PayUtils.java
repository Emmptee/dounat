/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: PayUtils.java 
 * @Package com.bis.sportedu.utils 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 亓振刚  
 * @date 2015-9-23 下午3:55:04 
 * @version V1.0   
 */
package com.donut.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.donut.app.R;
import com.donut.app.http.message.PayResponse;
import com.donut.app.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

//import com.unionpay.uppay.PayActivity;

/**
 * @ClassName: PayUtils
 * @Description: 支付
 * @author 亓振刚
 * @date 2015-9-23 下午3:55:04
 * @version 1.0
 */
public class PayUtils extends Activity
{
    private static final String TAG = "com.castar.app.utils.PayUtils";

    public static final int ALIPAY = 0;

    public static final int UPOMPPAY = 3;

    public static final int WXPAY = 1;

    private static final String ALIPAYINFO = "info";

    private static final String UPOMPPAY_TN = "tn";

    private static final String ALI_PAY = "AliPay";

    private static final String UPOMP_PAY = "UpompPay";

    private static final String WX_PAY = "WxPay";

    private static final int ALI_SDK_PAY_FLAG = 1;

    private static onPayListener resultListener;

    private static int PAYTYPE = 0;

    private IWXAPI msgApi;

    private static PayResponse payRes;

    public static final String WX_RESULT = "wx_result";

    public static void startUpompPay(Context context,
            onPayListener resultListener, String tn)
    {
        PayUtils.resultListener = resultListener;
        PayUtils.PAYTYPE = UPOMPPAY;
        Intent intent = new Intent(context, PayUtils.class);
        intent.setAction(UPOMP_PAY);
        intent.putExtra(UPOMPPAY_TN, tn);
        context.startActivity(intent);
    }

    public static void startAliPay(Context context,
            onPayListener resultListener, String payInfo)
    {
        PayUtils.resultListener = resultListener;
        PayUtils.PAYTYPE = ALIPAY;
        Intent intent = new Intent(context, PayUtils.class);
        intent.setAction(ALI_PAY);
        intent.putExtra(ALIPAYINFO, payInfo);
        context.startActivity(intent);
    }

    public static void startWxPay(Context context, onPayListener resultListener,
            PayResponse payRes)
    {
        PayUtils.resultListener = resultListener;
        PayUtils.PAYTYPE = WXPAY;
        PayUtils.payRes = payRes;
        Intent intent = new Intent(context, PayUtils.class);
        intent.setAction(WX_PAY);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (TextUtils.equals(UPOMP_PAY, getIntent().getAction()))
        {
            String tn = getIntent().getStringExtra(UPOMPPAY_TN);

            // “00” – 银联正式环境
            // “01” – 银联测试环境，该环境中不发生真实交易
            String serverMode = "01";

            this.startUpompPayByJAR(tn, serverMode);
        }
        else if (TextUtils.equals(ALI_PAY, getIntent().getAction()))
        {
            final String payInfo = getIntent().getStringExtra(ALIPAYINFO);

            Runnable payRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(PayUtils.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);

                    Message msg = new Message();
                    msg.what = ALI_SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
        else if (TextUtils.equals(WX_PAY, getIntent().getAction()))
        {
            msgApi = WXAPIFactory.createWXAPI(this, null);

            IntentFilter filter = new IntentFilter();
            filter.addAction(WXPayEntryActivity.class.getCanonicalName());
            this.registerReceiver(receiver, filter);
            // 生成预支付订单之后，调起微信支付
            sendPayReq();
        }

    }

    /**
     * 去支付
     */
    private boolean toPay;
    public static boolean hasLogin;
    @Override
    protected void onResume() {

        /**
         * 微信支付神经病!,当跳转到微信时,未登录状态返回,不会回调!!!!!!
         * 暂定修改方案,记录跳转状态,微信有返回时,设置hasLogin值返回时关闭当前页面!
         */
        if(toPay && !hasLogin && PayUtils.PAYTYPE == WXPAY)
        {
            ToastUtil.showShort(this, this.getString(R.string.pay_cancel));
            onBackPressed();
            if (resultListener != null) {
                resultListener.onPayFail(PAYTYPE, -2);
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        toPay = true;
        super.onPause();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case ALI_SDK_PAY_FLAG:
            {
                finish();

                PayResult payResult = new PayResult((String) msg.obj);

                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                // String resultInfo = payResult.getResult();

                String resultStatus = payResult.getResultStatus();

                // 判断resultStatus 为“9000”则代表支付成功
                if (TextUtils.equals(resultStatus, "9000"))
                {
                    if (resultListener != null) {
                        resultListener.onPaySuccess(PAYTYPE);
                    }
                }
                else
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000"))
                {
                    if (resultListener != null) {
                        resultListener.onPayFail(PAYTYPE, 8000);
                    }
                }
                else
                // 用户取消支付
                if (TextUtils.equals(resultStatus, "6001"))
                {
                    ToastUtil.showShort(PayUtils.this,
                            PayUtils.this.getString(R.string.pay_cancel));
                    if (resultListener != null) {
                        resultListener.onPayFail(PAYTYPE, 6001);
                    }
                }
                else
                // 网络连接错误
                if (TextUtils.equals(resultStatus, "6002"))
                {
                    ToastUtil.showShort(PayUtils.this, PayUtils.this
                            .getString(R.string.connect_failuer_toast));
                    if (resultListener != null) {
                        resultListener.onPayFail(PAYTYPE, 6002);
                    }
                }
                else
                {
                    // 其他值就可以判断为支付失败，系统返回的错误
                    if (resultListener != null) {
                        resultListener.onPayFail(PAYTYPE,
                                Integer.valueOf(resultStatus));
                    }
                }

            }
            }
        };
    };

    private void startUpompPayByJAR(String paydata, String serverMode)
    {
        Bundle localBundle = new Bundle();
        initBundle(paydata, localBundle, serverMode);
        localBundle.putString("SpId", null);
        localBundle.putString("SysProvide", null);
        localBundle.putString("paydata", paydata);
        localBundle.putInt("reqOriginalId", 2);
        Intent intent = new Intent();
        intent.putExtras(localBundle);
        // intent.setClass(this, PayActivity.class);
        // this.startActivityForResult(intent, 10);
    }

    private void initBundle(String paydata, Bundle localBundle,
            String serverMode)
    {
        if ((paydata == null) || (paydata.trim().length() <= 0))
        {
            return;
        }
        if (paydata.trim().charAt(0) == '<')
        {
            if ((serverMode != null)
                    && ("00".equalsIgnoreCase(serverMode.trim())))
            {
                localBundle.putBoolean("UseTestMode", false);
                return;
            }
            localBundle.putBoolean("UseTestMode", true);
            return;
        }
        localBundle.putString("ex_mode", serverMode);
    }

    private void sendPayReq()
    {
        PayReq req = new PayReq();
        req.appId = payRes.getAppId();
        req.partnerId = payRes.getPartnerId();
        req.prepayId = payRes.getPrepayId();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = payRes.getNonceStr();
        req.timeStamp = payRes.getTimeStamp();
        req.sign = payRes.getSign();

        msgApi.registerApp(payRes.getAppId());
        if (!msgApi.isWXAppInstalled()) {
            ToastUtil.showShort(this, "没有安装微信");
            finish();
            if (resultListener != null) {
                resultListener.onPayFail(PAYTYPE, -2);
            }
            return;
        }
        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {//msgApi.isWXAppSupportAPI()
            ToastUtil.showShort(this, "当前微信版本不支持支付功能");
            finish();
            if (resultListener != null) {
                resultListener.onPayFail(PAYTYPE, -2);
            }
            return;
        }

       msgApi.sendReq(req);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (TextUtils.equals(WX_PAY, getIntent().getAction()))
        {
            unregisterReceiver(receiver);
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            PayUtils.this.finish();
            int code = intent.getIntExtra(WX_RESULT, -2);
            switch (code)
            {
            case 0:
                // 成功
                if (resultListener != null) {
                    resultListener.onPaySuccess(PAYTYPE);
                }
                break;
            case -1:
                // 错误
                if (resultListener != null) {
                    resultListener.onPayFail(PAYTYPE, -1);
                }
                break;
            case -2:
                // 取消
                if (resultListener != null) {
                    resultListener.onPayFail(PAYTYPE, -2);
                }
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        finish();
        if (data == null)
        {
            return;
        }

        String R_SUCCESS = "success", R_FAIL = "fail", R_CANCEL = "cancel";

        String str = data.getExtras().getString("pay_result");

        if (str.equalsIgnoreCase(R_SUCCESS))
        {
            if (resultListener != null) {
                resultListener.onPaySuccess(PAYTYPE);
            }
        }
        else if (str.equalsIgnoreCase(R_FAIL))
        {
            if (resultListener != null) {
                resultListener.onPayFail(PAYTYPE, 0);
            }
        }
        else if (str.equalsIgnoreCase(R_CANCEL))
        {
            ToastUtil.showShort(this, this.getString(R.string.pay_cancel));
        }
    }

    public interface onPayListener
    {
        void onPaySuccess(int paytype);

        /**
         * 支付宝支付时 state 为8000 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
         * 最终交易是否成功以服务端异步通知为准（小概率状态）
         */
        void onPayFail(int paytype, int stateCode);
    }

    class PayResult
    {
        private String resultStatus;

        private String result;

        private String memo;

        public PayResult(String rawResult)
        {

            if (TextUtils.isEmpty(rawResult)) {
                return;
            }

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams)
            {
                if (resultParam.startsWith("resultStatus"))
                {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result"))
                {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo"))
                {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }

        @Override
        public String toString()
        {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        private String gatValue(String content, String key)
        {
            String prefix = key + "={";
            return content.substring(content.indexOf(prefix) + prefix.length(),
                    content.lastIndexOf("}"));
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus()
        {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo()
        {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult()
        {
            return result;
        }
    }

}
