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
 * @Title: SendNetRequestManager.java
 * @author 亓振刚
 * @date 2015-8-21 下午3:53:48
 * @version V1.0
 */
package com.donut.app.http;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.manager.RequestManager.RequestListener;
import com.android.volley.model.FormFile;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.message.BaseRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 亓振刚
 * @version 1.0
 * @ClassName: SendNetRequestManager
 * @Description: 发送网络请求的封装
 * @date 2015-8-21 下午3:53:48
 */
public class SendNetRequestManager {

    // 默认请求码
    private static final int BASEREQUEST = 0;

    private RequestListener requestListener;

    private static final boolean SHOULD_CACHE = true;

    private static final int TIMEOUT_COUNT = 10 * 1000;

    private static final int RETRY_TIMES = 1;

    public SendNetRequestManager(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public LoadController sendNetRequest(Object obj, final String header) {
        return this.sendNetRequest(obj, header, BASEREQUEST);
    }

    public LoadController sendNetRequest(Object obj, final String header,
                                         final int requestCode) {
        if (!NetUtils.isNetworkConnected(SysApplication.getInstance())) {
            ToastUtil.showShort(SysApplication.getInstance(),
                    SysApplication.getInstance().getString(R.string.connect_no));
            return null;
        }
        UserInfo userInfo = SysApplication.getUserInfo();
        BaseRequest request = new BaseRequest();
        request.setHeader(header);
        request.setUserId(userInfo.getUserId());
        request.setToken(userInfo.getToken());
        request.setData(JsonUtils.toJson(obj, obj.getClass()));

        JSONObject jsonObject = null;
        try {
            L.i("====", JsonUtils.toJson(request, BaseRequest.class));
            jsonObject = new JSONObject(JsonUtils.toJson(request,
                    BaseRequest.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        char c = header.charAt(2);
        String requestUrl;
        if (c == '0') {
            requestUrl = RequestUrl.REQUEST_URL_R;
        } else {
            requestUrl = RequestUrl.REQUEST_URL_W;
        }
        return RequestManager.getInstance().jsonObjectRequest(
                requestUrl, jsonObject, requestListener,
                SHOULD_CACHE, TIMEOUT_COUNT, RETRY_TIMES, requestCode);
    }

    /**
     * 上传图片
     */

    public LoadController uploadImg(String filePath, int type, int requestCode) {

        if (!NetUtils.isNetworkConnected(SysApplication.getInstance())) {
            ToastUtil.showShort(SysApplication.getInstance(),
                    SysApplication.getInstance().getString(R.string.connect_no));
            return null;
        }

        // URI uri = URI.create(MediaStore.Images.Media.insertImage(
        // context.getContentResolver(), photo, null, null));

//        File file = new File(filePath);
//        // try
//        // {
//        // BufferedOutputStream bos = new BufferedOutputStream(
//        // new FileOutputStream(file));
//        // photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        // bos.flush();
//        // bos.close();
//        // }
//        // catch(IOException e)
//        // {
//        // e.printStackTrace();
//        // }
//
//        RequestMap params = new RequestMap();
//        params.put("file", file);
//        params.put("uploadType", type + "");
//        return RequestManager.getInstance().post(RequestUrl.REQUEST_UPLOAD_URL,
//                params, requestListener, requestCode);

        String fileName;
        if (type == 1) {
            fileName = "xxx.mp4";
        } else {
            fileName = "xxx.jpg";
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("uploadType", String.valueOf(type));
        params.put("fileName", fileName);

        File file = new File(filePath);

        FormFile[] files = new FormFile[1];
        files[0] = new FormFile(fileName, file, "file", null);

        return RequestManager.getInstance().upLoadRequest(RequestUrl.REQUEST_UPLOAD_URL,
                params, requestListener, files, requestCode);
    }


    public LoadController sendLocationRequest(final String url, final int requestCode) {
        if (!NetUtils.isNetworkConnected(SysApplication.getInstance())) {
            ToastUtil.showShort(SysApplication.getInstance(),
                    SysApplication.getInstance().getString(R.string.connect_no));
            return null;
        }

        return RequestManager.getInstance().jsonObjectRequest(
                url, null, requestListener,
                SHOULD_CACHE, TIMEOUT_COUNT, RETRY_TIMES, requestCode);
    }


}
