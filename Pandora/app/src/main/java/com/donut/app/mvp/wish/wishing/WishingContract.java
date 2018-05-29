package com.donut.app.mvp.wish.wishing;

import com.donut.app.http.message.wish.AddWishRequest;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface WishingContract {
    interface View extends BaseView {

        void showUploadingProgress(int progress);

        void dismissUploadingProgress();

        void finishView();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

        public abstract void saveData(AddWishRequest request);
    }
}
