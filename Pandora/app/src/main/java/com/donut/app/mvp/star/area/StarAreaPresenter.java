package com.donut.app.mvp.star.area;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.StarSubjectsResponse;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/3/16.
 * Description : 类为public是必须的,在反射中强转时非public会报错<br>
 */
public class StarAreaPresenter<V extends StarAreaContract.View> extends StarAreaContract.Presenter <V>{

    private static final int SUBJECT_LIST_REQUEST = 1;

    public void loadData(boolean showLoadingView) {
        super.loadData(new Object(),
                HeaderRequest.SUBJECT_STAR_LIST_REQUEST,
                SUBJECT_LIST_REQUEST,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SUBJECT_LIST_REQUEST:
                StarSubjectsResponse response
                        = JsonUtils.fromJson(responseJson, StarSubjectsResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }
}
