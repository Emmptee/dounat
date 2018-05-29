package com.donut.app.mvp.wish.reply;

import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface WishReplyContract {
    interface View extends BaseView {

        void onCollect(WishDetailsResponse model);

        void onLike(WishDetailsResponse model);

        void onToComment();

        void onToShare(WishDetailsResponse model);

        void onShowImg(WishDetailsResponse model);

        void onPlayAudio(android.view.View view, WishDetailsResponse model);

        void onPlayVideo(WishDetailsResponse model);

        void showView(WishDetailsResponse response);

        void clearCommentEdit();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
