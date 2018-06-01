package com.donut.app.mvp.channel.list;

import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.SubjectListRequest;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;


/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelListPresenter extends ChannelListContract.Presenter {

    private static final int SUBJECT_LIST_REQUEST_CODE = 1;

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

    void saveBehaviour(String functionCode, int channelType) {

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
