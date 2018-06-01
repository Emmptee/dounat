package com.donut.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.donut.app.config.Constant;
import com.donut.app.utils.PayUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{

    private IWXAPI api;

    public static final String ACTION = "com.donut.app.wxapi.WXPayEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        PayUtils.hasLogin = true;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            Intent intent = new Intent();
            intent.putExtra(PayUtils.WX_RESULT, resp.errCode);
            intent.setAction(ACTION);
            sendBroadcast(intent);
            finish();
        }
    }
}