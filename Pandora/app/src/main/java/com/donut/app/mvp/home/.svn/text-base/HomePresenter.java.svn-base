package com.donut.app.mvp.home;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.SubjectHomePageResponse;
import com.donut.app.http.message.shakestar.Channel;
import com.donut.app.http.message.shakestar.SweetResponse;
import com.donut.app.http.message.subjectSnap.SubjectSnapDetailRequest;
import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;


/**
 * Created by Qi on 2017/3/16.
 * Description : 类为public是必须的,在反射中强转时非public会报错<br>
 */
public class HomePresenter<V extends HomeContract.View> extends HomeContract.Presenter <V>{

    private static final int NEW_HOME_CODE = 1;
    private static final int NEW_HOMELIKE_CODE = 2;
    private static final int NEW_CHANNEL_CODE = 3;

    public void loadData(boolean showLoadingView) {
        super.loadData(new Object(),
                HeaderRequest.NEW_HOME,
                NEW_HOME_CODE,
                showLoadingView);
    }

    public void loadDataList(boolean showLoadingView) {
        super.loadData(new Object(),
                HeaderRequest.NEW_HOME_LIST,
                NEW_HOMELIKE_CODE,
                showLoadingView);
    }
    public void loadChannelDataList(boolean showLoadingView) {
        super.loadData(new Object(),
                HeaderRequest.NEW_CHANNEL_LIST,
                NEW_CHANNEL_CODE,
                showLoadingView);
    }
    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case NEW_HOME_CODE:
                SubjectHomePageResponse response
                        = JsonUtils.fromJson(responseJson, SubjectHomePageResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
            case NEW_HOMELIKE_CODE:
                SweetResponse sweetResponse=JsonUtils.fromJson(responseJson,SweetResponse.class);
                if (COMMON_SUCCESS.equals(sweetResponse.getCode())) {
                    mView.showList(sweetResponse);
                } else {
                    showToast(sweetResponse.getMsg());
                }
                break;
            case NEW_CHANNEL_CODE:
                Channel channel=JsonUtils.fromJson(responseJson,Channel.class);
                if (COMMON_SUCCESS.equals(channel.getCode())) {
                    mView.showChannelList(channel);
                } else {
                    showToast(channel.getMsg());
                }
                break;
        }
    }
}
