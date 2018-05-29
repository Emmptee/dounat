package com.donut.app.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.AddOrEditAddrActivity;
import com.donut.app.activity.H5ReadDocActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.MyOrderActivity;
import com.donut.app.activity.PersonalBandingPhoneActivity;
import com.donut.app.activity.VipActivity;
import com.donut.app.entity.H5UserBehaviour;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.message.PayResponse;
import com.donut.app.service.SaveH5BehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PayUtils;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Qi on 2016/6/7.
 */
public class DonutPlugin extends CordovaPlugin implements PayUtils.onPayListener {

    private CallbackContext callbackContext;

    private Context context;

    public static final int LOGIN_REQUEST_CODE = 1, VIP_REQUEST_CODE = 2, BANDING_REQUEST_CODE = 3;

    private boolean ALIPAY = false;

    private byte[] lock = new byte[0];

    @Override
    public boolean execute(String action, JSONArray args,
                           CallbackContext callbackContext) throws JSONException {

        this.context = this.webView.getContext();
        this.callbackContext = callbackContext;

        if ("backAction".equals(action)) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.finish();
            }
            return true;
        } else if ("userInfoAction".equals(action)) {
            UserInfo userInfo = SysApplication.getUserInfo();
            String userId = userInfo.getUserId();
            String token = userInfo.getToken();
            int memberStatus = userInfo.getMemberStatus();
            this.echo((userId == null ? "" : userId) + "," + (token == null ? "" : token) + "," + memberStatus, callbackContext);

            return true;
        } else if ("loginAction".equals(action)) {
            SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);

            String msg = args.get(0).toString();
            if ("0002".equals(msg)) {
                ToastUtil.showShort(context, context.getString(R.string.login_timeout_msg));

                sp_Info.edit()
                        .putBoolean(Constant.IS_LOGIN, false).apply();

                Activity activity = (Activity) context;
                activity.startActivityForResult(new Intent(activity, LoginActivity.class), LOGIN_REQUEST_CODE);
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                ToastUtil.showShort(context, msg);

                UserInfo userInfo = SysApplication.getUserInfo();

                if (sp_Info.getBoolean(Constant.IS_LOGIN, false)) {
                    String userId = userInfo.getUserId();
                    String token = userInfo.getToken();
                    int memberStatus = userInfo.getMemberStatus();
                    this.echo((userId == null ? "" : userId) + "," + (token == null ? "" : token) + "," + memberStatus, callbackContext);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        activity.startActivityForResult(new Intent(activity, LoginActivity.class), LOGIN_REQUEST_CODE);
                        synchronized (lock) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                return true;
            }
        } else if ("payAction".equals(action)) {
            PayResponse payResponse = JsonUtils.fromJson(args.getJSONObject(0).toString(), PayResponse.class);
            if (payResponse.getLanchPay() != null) {
                //支付宝支付
                PayUtils.startAliPay(context, this, payResponse.getLanchPay());
                ALIPAY = true;
            } else {
                //微信支付5=
                PayUtils.startWxPay(context, this, payResponse);
                ALIPAY = false;
            }
            return true;
        } else if ("orderAction".equals(action)) {

            if (context instanceof Activity) {
                context.startActivity(new Intent(context, MyOrderActivity.class));
            }
        } else if ("behaviourAction".equals(action)) {
//            Log.e("====", args.get(0).toString());
            H5UserBehaviour behaviour = JsonUtils.fromJson(args.get(0).toString(), H5UserBehaviour.class);
            SaveH5BehaviourDataService.startH5Action(context, behaviour.getH5Id(), behaviour.getData());
        } else if ("showMsgAction".equals(action)) {
            //Log.e("====", args.toString());
            ToastUtil.showShort(context, args.get(0).toString());
        } else if ("addAddressAction".equals(action)) {

            JSONObject object = new JSONObject(args.get(0).toString());

            Intent intent = new Intent(context, AddOrEditAddrActivity.class);
            intent.putExtra(AddOrEditAddrActivity.HASADDR, object.getBoolean("hasAddress"));
            context.startActivity(intent);
        } else if ("vipAction".equals(action)) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.startActivityForResult(new Intent(activity, VipActivity.class), VIP_REQUEST_CODE);
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if ("goodsAction".equals(action)) {
            SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            callbackContext.success(sp_Info.getString("goodsId", ""));
        } else if ("goodsListAction".equals(action)) {
            SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            callbackContext.success(sp_Info.getString("subjectId", ""));
        } else if ("readDocAction".equals(action)) {
            Intent intent = new Intent(context, H5ReadDocActivity.class);
            intent.putExtra(H5ReadDocActivity.DOC_URL, args.get(0).toString());
            context.startActivity(intent);
        } else if ("getDocUrlAction".equals(action)) {
            SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            callbackContext.success(sp_Info.getString("docUrl", ""));
        } else if ("promotionOrderAction".equals(action)) {
            SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            callbackContext.success(sp_Info.getString("orderIds", ""));
        } else if ("AuctionRecordAction".equals(action)) {
            SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            callbackContext.success(sp_Info.getString("auctionId", ""));
        } else if ("bindingPhone".equals(action)) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.startActivityForResult(
                        new Intent(activity, PersonalBandingPhoneActivity.class),
                        BANDING_REQUEST_CODE);
//                synchronized (lock) {
//                    try {
//                        lock.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

            }
        }

        return true;
    }

    private void echo(String message, CallbackContext callbackContext) {
        try {
            if (message != null && message.length() > 0) {
                callbackContext.success(message);
            } else {
                callbackContext.error("登录失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            UserInfo userInfo = SysApplication.getUserInfo();
            String userId = userInfo.getUserId();
            String token = userInfo.getToken();
            int memberStatus = userInfo.getMemberStatus();

            this.echo((userId == null ? "" : userId) + "," + (token == null ? "" : token) + "," + memberStatus, callbackContext);
        } else if (requestCode == VIP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            this.echo("1", callbackContext);
        } else if(requestCode == BANDING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            this.echo("2", callbackContext);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void onPaySuccess(int paytype) {
        ToastUtil.showShort(context, R.string.pay_success);
        callbackContext.success("0");

        if (context != null) {
            context.startActivity(new Intent(context, MyOrderActivity.class));
        }
    }

    @Override
    public void onPayFail(int paytype, int stateCode) {

        if (context != null) {
            context.startActivity(new Intent(context, MyOrderActivity.class));
        }

        if (paytype == 1) {
            if (stateCode == -1) {
                ToastUtil.showShort(context, R.string.pay_result_fail);
            }
            if (stateCode == -2) {
                ToastUtil.showShort(context, R.string.pay_result_cancle);
            }
        } else {
            ToastUtil.showShort(context, R.string.pay_fail);
//                showToast(stateCode + "");
        }

        if (!ALIPAY) {
            callbackContext.success("" + stateCode);
        } else {
            if (stateCode == 6001) {
                callbackContext.success("-2");
            } else {
                callbackContext.success("-1");
            }
        }
    }
}
