/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: BaseFragment.java
 * @Package com.bis.sportedu.fragment
 * @author 亓振刚
 * @date 2015-7-24 下午5:27:01
 * @version V1.0
 */
package com.donut.app.fragment.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager.RequestListener;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.LoadingDialog;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PushRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author 亓振刚
 * @version 1.0
 * BaseFragment
 *  2015-7-24 下午5:27:01
 */
public class BaseFragment extends Fragment implements RequestListener,
        EasyPermissions.PermissionCallbacks
{

    private LoadingDialog progressDialog;

    protected LoadController mLoadController = null;

    // 默认请求码
    protected static final int BASEREQUEST = 0;

    protected static final String COMMON_SUCCESS = "0000";// 请求成功

    protected static final String COMMON_NOT_LOGIN = "0002";// 未登录

    protected static final int STARTACTLOGIN = 9999;// 未登录

    private boolean isShowLoading;

    private static final int RC_PERM = 1124;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected boolean getLoginStatus() {
        SharedPreferences sp_Info = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        return sp_Info.getBoolean(Constant.IS_LOGIN, false);
    }

    /**
     * 运行时权限申请
     */
    //@AfterPermissionGranted(RC_PERM)
    public void requestRuntimePermission(@NonNull String rationale,
                                         @NonNull String... permission) {
        if (EasyPermissions.hasPermissions(this.getContext(), permission)) {
            // Have permission, do the thing!
            onPermissionsGranted(RC_PERM, Arrays.asList(permission));
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, rationale,
                    RC_PERM, permission);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 发送网络请求的封装
     */
    public void sendNetRequest(Object obj, final String header,
                               final boolean isShowLoading)
    {
        this.sendNetRequest(obj, header, BASEREQUEST, isShowLoading);
    }

    public void sendNetRequest(Object obj, final String header)
    {
        this.sendNetRequest(obj, header, BASEREQUEST, true);
    }

    public void sendNetRequest(Object obj, final String header,
                               final int requestCode)
    {
        this.sendNetRequest(obj, header, requestCode, true);
    }

    public void sendNetRequest(Object obj, final String header,
                               final int requestCode, final boolean isShowLoading)
    {
        this.isShowLoading = isShowLoading;
        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.sendNetRequest(obj, header, requestCode);
    }

    protected void UpLoadNetRequest(String filePath, int type, int requestCode)
    {
        this.isShowLoading = true;
        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.uploadImg(filePath, type, requestCode);
    }

    @Override
    public void onRequest()
    {
        if (isShowLoading)
        {
            jumpDialog();
            isShowLoading = false;
        }
    }

    @Override
    public void onLoading(long total, long count, String filePath)
    {
    }

    @Override
    public void onSuccess(String response, Map<String, String> headers,
                          String url, int actionId)
    {
        BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
        if (res != null && COMMON_NOT_LOGIN.equals(res.getCode()))
        {
            ToastUtil.showShort(getContext(), "账户信息已过期，请重新登录");
            SharedPreferences sp_Info = getContext().getSharedPreferences(
                    Constant.SP_INFO, Context.MODE_PRIVATE);
            sp_Info.edit()
                    .putBoolean(Constant.IS_LOGIN, false).apply();
            try {
                SysApplication.getDb().deleteAll(UserInfo.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
            deleteBaiduPush();
            startActivityForResult(new Intent(getContext(),
                    LoginActivity.class), STARTACTLOGIN);
            dissDialog();
            return;
        }
        dissDialog();
        onSuccess(response, url, actionId);
    }

    private void deleteBaiduPush(){

        SharedPreferences sp_Info = getActivity().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        PushRequest request = new PushRequest();
        request.setPushUserid(sp_Info.getString(Constant.PUSH_USER_ID, ""));
        request.setPushChannelid(sp_Info.getString(Constant.PUSH_CHANNEL_ID, ""));
        request.setUserId(SysApplication.getUserInfo().getUserId());
        Integer type = SysApplication.getUserInfo().getUserType();
        if (type == 1) {
            request.setUserType(String.valueOf(0));
        } else {
            request.setUserType(String.valueOf(1));
        }
        request.setOsType("0");
        sendNetRequest(request, HeaderRequest.REMOVE_TAG, 3, false);

    }

    public void onSuccess(String response, String url, int actionId)
    {
    }

    @Override
    public void onError(String errorMsg, String url, int actionId)
    {
        dissDialog();
        showToast(getContext().getString(R.string.connect_failuer_toast));
    }

    protected void showToast(String message)
    {
        ToastUtil.showShort(getContext(), message);
    }
    /**
     * 弹起加载框
     */
    public void jumpDialog()
    {
        if (null == progressDialog)
        {
            progressDialog = new LoadingDialog(getContext());
            try
            {
                progressDialog.show();
            }
            catch (Exception e)
            {
                jumpDialog();
            }
        }
    }

    /**
     * 关闭弹出框
     */
    protected void dissDialog()
    {
        if (null != progressDialog)
        {
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLoadController != null)
        {
            mLoadController.cancel();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        dissDialog();
    }
}
