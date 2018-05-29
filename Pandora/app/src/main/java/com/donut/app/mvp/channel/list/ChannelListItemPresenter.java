package com.donut.app.mvp.channel.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.SubjectHomePageResponse;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;


/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelListItemPresenter<V extends ChannelListItemContract.View>
        extends ChannelListItemContract.Presenter<V> {

    private final static int SUBJECT_COLLECT = 1, SUBJECT_SHARE = 2;

    public void loadData() {
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SUBJECT_COLLECT:
                BaseResponse collectRes = JsonUtils.fromJson(responseJson,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(collectRes.getCode())) {
                    mView.collectSuccess();
                } else {
                    showToast(collectRes.getMsg());
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
        if (channelType == GotoChannelUtils.CHANNEL_TYPE_SUBJECT) {
            if (subjectDetail.getStatus() == 1) {
                url = RequestUrl.SUBJECT_DETAIL_SHARE_URL + "header="
                        + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + subjectDetail.getSubjectId();
            } else {
                url = RequestUrl.SUBJECT_HISTORY_DETAIL_SHARE_URL + "header="
                        + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + subjectDetail.getSubjectId();
            }
            title = subjectDetail.getStarName() + " | " + subjectDetail.getName();
            content = subjectDetail.getDescription() == null ? "" : subjectDetail.getDescription();
        } else if (channelType == GotoChannelUtils.CHANNEL_TYPE_SNAP) {
            url = RequestUrl.SUBJECT_SNAP_SHARE_URL + "header="
                    + HeaderRequest.SUBJECT_SNAP_DETAIL + "&subjectId=" + subjectDetail.getSubjectId();
//            title = subjectDetail.getStarName()
//                    + ",【小伙伴】已围观了" + subjectDetail.getBrowseTimes();
            title = subjectDetail.getStarName() + " | 街拍街拍";
            content = subjectDetail.getName();
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

    public void saveBehaviour(String functionCode, int channelType) {

        String codeStart = null;
        switch (channelType) {
            case GotoChannelUtils.CHANNEL_TYPE_SUBJECT:
                codeStart = BehaviourEnum.SUBJECT.getCode();
                break;
            case GotoChannelUtils.CHANNEL_TYPE_SNAP:
                codeStart = BehaviourEnum.SUBJECT_SNAP_LIST.getCode();
                break;
        }

        SaveBehaviourDataService.startAction(mView.getContext(),
                codeStart + functionCode);
    }
}
