package com.donut.app.mvp.shakestar.video.record.preview;

import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

public interface ShakeStarPreviewContract {
    interface View extends BaseView {

        void showUploadingProgress(int progress);

        void dismissUploadingProgress();

        void finishView();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

        public abstract void saveData(ShakeStarPreviewRequest request);
    }
}
