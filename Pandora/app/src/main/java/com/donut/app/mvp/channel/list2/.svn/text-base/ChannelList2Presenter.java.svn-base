package com.donut.app.mvp.channel.list2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListRequest;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;


/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelList2Presenter extends ChannelList2Contract.Presenter {

    private static final int SUBJECT_LIST_REQUEST_CODE = 1, SUBJECT_COLLECT = 2, SUBJECT_SHARE = 3;

    public static final int rows = 10;

    public int page = 0;

    public String searchName;

    public void loadData(boolean showLoadingView,
                         String channelId) {
        SubjectListRequest request = new SubjectListRequest();
        request.setChannelId(channelId);
        request.setSearchName(searchName);
        request.setPage(page);
        request.setRows(rows);
        super.loadData(request,
                HeaderRequest.SUBJECT_LIST_REQUEST,
                SUBJECT_LIST_REQUEST_CODE,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SUBJECT_LIST_REQUEST_CODE:
                SubjectListResponse response
                        = JsonUtils.fromJson(responseJson, SubjectListResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }

    void onToCollect(SubjectListDetail subjectDetail) {

        int collectionStatus = subjectDetail.getCollectionStatus() == null ? 0 : subjectDetail.getCollectionStatus();
        int afterStatus = Math.abs(collectionStatus - 1);
        subjectDetail.setCollectionStatus(afterStatus);

        CollectRequest request = new CollectRequest();
        request.setContentId(subjectDetail.getB02Id());
        request.setType(0);
        request.setStatus(afterStatus);
        super.loadData(request,
                HeaderRequest.SUBJECT_COLLECT,
                SUBJECT_COLLECT,
                false);
    }

    public void onToShare(final SubjectListDetail subjectDetail, int channelType) {

        String url = "", title = "", content = "";
        if (channelType == GotoChannelUtils.CHANNEL_TYPE_NOTICE) {
            url = RequestUrl.SUBJECT_NOTICE_SHARE_URL + "header="
                    + HeaderRequest.SUBJECT_NOTICE_DETAIL + "&subjectId=" + subjectDetail.getSubjectId();
//            title = subjectDetail.getStarName()
//                    + ",【小伙伴】已围观了" + subjectDetail.getBrowseTimes();
            title = subjectDetail.getStarName() + " | " + subjectDetail.getName();
            content = subjectDetail.getDescription();
        } else {
            return;
        }

        Bitmap bmp = BitmapFactory.decodeResource(mView.getContext().getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(mView.getContext());
        builder.setTitle(title);
        builder.setContent(content);
        builder.setLinkUrl(url);
        //builder.setImgUrl(subjectDetail.getPublicPic());
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//
//            }
//        });
        builder.create();

        ShareRequest request = new ShareRequest();
        request.setSubjectId(subjectDetail.getSubjectId());
        request.setContentId(subjectDetail.getB02Id());
        super.loadData(request,
                HeaderRequest.SHARE,
                SUBJECT_SHARE,
                false);
    }

    void saveBehaviour(String functionCode, int channelType) {

        String codeStart = null;
        switch (channelType) {
            case GotoChannelUtils.CHANNEL_TYPE_NOTICE:
                codeStart = BehaviourEnum.SUBJECT_NOTICE_LIST.getCode();
                break;
        }

        SaveBehaviourDataService.startAction(mView.getContext(),
                codeStart + functionCode);
    }
}
