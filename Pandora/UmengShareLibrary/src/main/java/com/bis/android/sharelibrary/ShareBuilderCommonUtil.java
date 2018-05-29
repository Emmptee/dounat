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
 * @Title: ShareCommon.java
 * @Package com.bis.sportedu.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Administrator
 * @date 2015-8-21 下午1:54:56
 * @version V1.0
 */
package com.bis.android.sharelibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * @version 1.0
 * @ClassName: ShareBuilderCommonUtil
 * @Description: 分享公共类
 */
public class ShareBuilderCommonUtil {

    private ShareParams mParams;

    public ShareBuilderCommonUtil(ShareParams shareParams) {
        super();
        this.mParams = shareParams;
    }

    /**
     * 分享内容</br>
     */
    private void setShareContent() {
        UMImage image = null;
        if (mParams.mImgUrl != null) {
            image = new UMImage(mParams.mContext, mParams.mImgUrl);
        } else if (mParams.mBitmap != null) {
            image = new UMImage(mParams.mContext, mParams.mBitmap);
        }

        ShareContent[] contents = new ShareContent[mParams.mShareMedia.length];

        for (int i = 0; i < mParams.mShareMedia.length; i++) {
            ShareContent shareContent = new ShareContent();

            SHARE_MEDIA media = mParams.mShareMedia[i];
            switch (media) {
                case QQ:
                    mParams.mLinkUrl += "&ch=qq";
                    break;
                case QZONE:
                    mParams.mLinkUrl += "&ch=qqkj";
                    break;
                case WEIXIN:
                    mParams.mLinkUrl += "&ch=wx";
                    break;
                case WEIXIN_CIRCLE:
                    mParams.mLinkUrl += "&ch=pyq";
                    break;
                case SINA:
                    mParams.mLinkUrl += "&ch=wb";
                    break;
                default:
                    break;
            }

            UMWeb web = new UMWeb(mParams.mLinkUrl);
            web.setTitle(mParams.mTitle);
            web.setThumb(image);
            web.setDescription(mParams.mContent);
            shareContent.mMedia = web;
            contents[i] = shareContent;
        }

        new ShareAction((Activity) mParams.mContext)
                .setDisplayList(mParams.mShareMedia)
                .setContentList(contents)
//                .withText(mParams.mContent)
//                .withTitle(mParams.mTitle)
//                .withTargetUrl(mParams.mLinkUrl)
//                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mParams.mContext, "分享成功", Toast.LENGTH_SHORT).show();

            if (mParams != null && mParams.mListener != null) {
                mParams.mListener.onSuccessRes();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mParams.mContext, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mParams.mContext, "取消分享", Toast.LENGTH_SHORT).show();
        }
    };

    public interface OnResponseListener {
        void onSuccessRes();
    }

    public static class Builder {
        private final ShareParams ShareParams;

        public Builder(Context context) {
            ShareParams = new ShareParams(context);
        }

        public Builder setListener(OnResponseListener listener) {
            ShareParams.mListener = listener;
            return this;
        }

        public Builder setTitle(String title) {
            ShareParams.mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            ShareParams.mContent = content.length() > 120 ? content.substring(0, 120) : content;
            return this;
        }

        public Builder setLinkUrl(String linkUrl) {
            ShareParams.mLinkUrl = linkUrl;
            return this;
        }

        public Builder setImgUrl(String imgUrl) {
            ShareParams.mImgUrl = imgUrl;
            return this;
        }

        public Builder setBitmap(Bitmap bitmap) {
            ShareParams.mBitmap = bitmap;
            return this;
        }

        public Builder setEmailPlat(boolean emailPlat) {
            ShareParams.mEmailPlat = emailPlat;
            if(emailPlat)
            {
                setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.EMAIL, SHARE_MEDIA.MORE});
            }
            return this;
        }

        public Builder setShareMedia(SHARE_MEDIA[] shareMedia) {
            ShareParams.mShareMedia = shareMedia;
            return this;
        }

        public ShareBuilderCommonUtil create() {
            final ShareBuilderCommonUtil shareCommonUtil = new ShareBuilderCommonUtil(ShareParams);
            shareCommonUtil.setShareContent();
            return shareCommonUtil;
        }
    }

    public static class ShareParams {

        public ShareParams(Context context) {
            mContext = context;
        }

        public OnResponseListener mListener;

        public Context mContext;

        public String mTitle;

        public String mContent;

        public String mImgUrl;

        public String mLinkUrl;

        public Bitmap mBitmap;

        public boolean mEmailPlat;

        public SHARE_MEDIA[] mShareMedia = new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.MORE};
    }
}
