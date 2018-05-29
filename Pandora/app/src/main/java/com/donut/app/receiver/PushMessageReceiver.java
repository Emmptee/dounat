package com.donut.app.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.manager.RequestManager;
import com.donut.app.AppManager;
import com.donut.app.SysApplication;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.CommentDetailActivity;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.HomeActivity;
import com.donut.app.activity.OrderDetailActivity;
import com.donut.app.activity.SystemNoticeActivity;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.PushRequest;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.mvp.subject.finalpk.SubjectFinalPkActivity;
import com.donut.app.mvp.subject.snap.SubjectSnapActivity;
import com.donut.app.mvp.subject.special.SubjectSpecialActivity;
import com.donut.app.mvp.welcome.WelcomeActivity;
import com.donut.app.mvp.wish.reply.WishReplyActivity;
import com.donut.app.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class PushMessageReceiver extends com.baidu.android.pushservice.PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = "wjj";

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        L.i(TAG, responseString);
        if (errorCode == 0) {
            // 绑定成功
            bindSuccess(context, userId, channelId);
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        //updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 onMessage=\"" + message
                + "\" customContentString=" + customContentString;
        L.i(TAG, messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("notice_type")) {
                    myvalue = customJson.getString("notice_type");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        //updateContent(context, messageString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "通知到达 onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        L.i(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
        context.getSharedPreferences(Constant.SP_INFO, Context.MODE_PRIVATE)
                .edit().putBoolean(Constant.HAS_NEW_MSG, true).apply();
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 onNotificationClicked title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        L.i(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值

        try {
            updateContent(context, customContentString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param failTags  设置失败的tag
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        L.i(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        //updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param failTags  删除失败的tag
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        L.i(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        //updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        L.i(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        //updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        L.i(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
            L.i(TAG, "解绑成功");
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        //updateContent(context, responseString);
    }

    public static void updateContent(Context context, String customContent) throws JSONException {

        String noticeType = null, skipId = null;
        JSONObject customJson;

        if (!TextUtils.isEmpty(customContent)) {
            customJson = new JSONObject(customContent);
            if (!customJson.isNull("notice_type")) {
                noticeType = customJson.getString("notice_type");
            }
            if (!customJson.isNull("skipId")) {
                skipId = customJson.getString("skipId");
            }
        } else {
            return;
        }

        boolean isFinish = AppManager.getAppManager().allActivityFinish();

        if (isFinish) {
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.putExtra(WelcomeActivity.NOTICE_JSON_CONTENT, customContent);
            context.startActivity(intent);
        } else {
            //根据通知类型跳转页面
            Intent intent = null;
            if ("0001".equals(noticeType)) {
                //系统通知、意见反馈    跳转到 消息中心页面
                intent = new Intent(context, SystemNoticeActivity.class);
            } else if ("0501".equals(noticeType)) {
                //订单管理  跳转到  订单详情页面
                intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_ID, skipId);
            } else if ("0401".equals(noticeType)) {
                //审核通过  跳转到  ip列表页面
                intent = new Intent(context, HomeActivity.class);
                intent.putExtra(HomeActivity.NOTICE, true);
            } else if ("0201".equals(noticeType)) {
                // 商品详情
                int goodsType = -1;
                if (!customJson.isNull("goodsType")) {
                    goodsType = customJson.getInt("goodsType");
                }
                SharedPreferences sp_Info = context.getSharedPreferences(
                        Constant.SP_INFO, Context.MODE_PRIVATE);
                sp_Info.edit().putString("goodsId", skipId).apply();
                intent = new Intent(context, H5WebActivity.class);
                switch (goodsType) {
                    case 0:
                        intent.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods-details.html");
                        break;
                    case 1:
                        intent.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods_details_videos.html");
                        break;
                    case 2:
                        intent.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods_details_documents.html");
                        break;
                    default:
                        return;
                }
            } else if ("0".equals(noticeType)) {
                //有本事你来专题详情
                intent = new Intent(context, SubjectSpecialActivity.class);
                intent.putExtra(SubjectSpecialActivity.SUBJECT_ID, skipId);
            } else if ("5".equals(noticeType)) {
                //街拍街拍专题详情
                intent = new Intent(context, SubjectSnapActivity.class);
                intent.putExtra(SubjectSnapActivity.SUBJECT_ID, skipId);
            } else if ("0211".equals(noticeType)) {
                // 对专题等详情页的打赏点赞等
                JSONObject skipIdJson = new JSONObject(customJson.getString("skipId"));
                int channelType = skipIdJson.getInt("channelType");
                String subjectId = skipIdJson.getString("subjectId");
                switch (channelType) {
                    case 0:
                        // 有本事你来专题
                        GotoChannelUtils.GotoSubjectDetail(context, GotoChannelUtils.CHANNEL_TYPE_SUBJECT, subjectId);
                        return;
                    case 1:
                        // 有本事你来专题历史（终极PK）
                        intent = new Intent(context, SubjectFinalPkActivity.class);
                        intent.putExtra(SubjectFinalPkActivity.CONTENT_ID, skipIdJson.getString("contentId"));
                        intent.putExtra(SubjectFinalPkActivity.SUBJECT_ID, subjectId);
                        break;
                    case 2:
                        // 挑战
                        intent = new Intent(context, SubjectChallengeActivity.class);
                        intent.putExtra(SubjectChallengeActivity.CONTENT_ID, skipIdJson.getString("contentId"));
                        intent.putExtra(SubjectChallengeActivity.SUBJECT_ID, subjectId);
                        break;
                    case 5:
                        // 街拍详情
                        GotoChannelUtils.GotoSubjectDetail(context, GotoChannelUtils.CHANNEL_TYPE_SNAP, subjectId);
                        return;
                    case 7:
                        // 大咖有通告
                        GotoChannelUtils.GotoSubjectDetail(context, GotoChannelUtils.CHANNEL_TYPE_NOTICE, subjectId);
                        return;
                }
            } else if ("0240".equals(noticeType)) {
                // 评论详情
                intent = new Intent(context, CommentDetailActivity.class);
                JSONObject skipIdJson = new JSONObject(customJson.getString("skipId"));
                String commentId = skipIdJson.getString("commentId");
                String contentId = skipIdJson.getString("contentId");
                intent.putExtra(CommentDetailActivity.COMMENTID, commentId);
                intent.putExtra(CommentDetailActivity.CONTENTID, contentId);
            } else if ("0250".equals(noticeType)) {
                // 全部评论
                intent = new Intent(context, CommentActivity.class);
                JSONObject skipIdJson = new JSONObject(customJson.getString("skipId"));
                String contentId = skipIdJson.getString("contentId");
                intent.putExtra(CommentActivity.CONTENTID, contentId);
            } else if ("0000".equals(noticeType)) {
                // 首页 点赞推送跳首页
                intent = new Intent(context, HomeActivity.class);
            } else if ("0706".equals(noticeType)) {
                // 心愿详情
                intent = new Intent(context, WishReplyActivity.class);
                JSONObject skipIdJson = new JSONObject(customJson.getString("skipId"));
                String contentId = skipIdJson.getString("contentId");
                intent.putExtra(WishReplyActivity.CONTENT_ID, contentId);
            }
            if (intent == null) {
                return;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private void bindSuccess(Context context, String userId, String channelId) {
        try {
            SharedPreferences sp_Info = context.getSharedPreferences(
                    Constant.SP_INFO, Context.MODE_PRIVATE);

            sp_Info.edit().putString(Constant.PUSH_USER_ID, userId)
                    .putString(Constant.PUSH_CHANNEL_ID, channelId)
                    .apply();

            UserInfo userInfo = SysApplication.getDb().findFirst(UserInfo.class);
            int type = sp_Info.getInt(Constant.USER_TYPE, -1);
            String loginUserId = null;
            if (userInfo != null && userInfo.getUserId() != null) {
                loginUserId = userInfo.getUserId();
            } else {
                return;
            }

            PushRequest request = new PushRequest();
            request.setPushUserid(userId);
            request.setPushChannelid(channelId);
            request.setOsType("0");
            if (loginUserId != null) {
                request.setUserId(loginUserId);
                if (type == 1) {
                    request.setUserType(String.valueOf(0));
                } else {
                    request.setUserType(String.valueOf(1));
                }
            }
            SendNetRequestManager sendNet = new SendNetRequestManager(
                    new RequestManager.RequestListener() {
                        @Override
                        public void onSuccess(String arg0,
                                              Map<String, String> arg1, String arg2, int arg3) {
                            L.i("wjj", "百度推送绑定========" + arg0);
                        }

                        @Override
                        public void onRequest() {
                        }

                        @Override
                        public void onError(String arg0, String arg1, int arg2) {
                        }

                        @Override
                        public void onLoading(long l, long l1, String s) {
                        }
                    });
            sendNet.sendNetRequest(request, HeaderRequest.PUSH_ADD, 0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
