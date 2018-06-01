package com.donut.app.mvp.home.search.ido;

import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.home.ContentCategory;
import com.donut.app.http.message.home.WishItem;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public interface IdoSearchContract {
    interface View extends BaseView {
        void showView(List<ContentCategory> categoryList);

        void onSubjectItemClick(SubjectListDetail detail);

        void onWishItemClick(WishItem detail);
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
