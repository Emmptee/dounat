package com.donut.app.mvp.wish.user;

import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.http.message.wish.NotAchieveWishModel;
import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface WishUserContract {
    interface View extends BaseView {

        void onLike(WishDetailsResponse model);

        void onToComment();

        void onToShare(WishDetailsResponse model);

        void onShowImg(WishDetailsResponse model);

        void onPlayVideo(WishDetailsResponse model);

        void showView(WishDetailsResponse response);

        void clearCommentEdit();

        void refreshData();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
