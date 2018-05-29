package com.donut.app.mvp.star.notice;

import com.donut.app.http.message.StarNoticeAddRequest;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface StarSendNoticeContract {
    interface View extends BaseView {

        void showUploadingProgress(String filePath, int progress);

        void dismissUploadingProgress(int actionId);

        void finishView();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

        public abstract void saveData(StarNoticeAddRequest request);
    }
}
