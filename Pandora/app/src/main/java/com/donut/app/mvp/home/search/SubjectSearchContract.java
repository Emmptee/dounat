package com.donut.app.mvp.home.search;

import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.home.ContentItem;
import com.donut.app.http.message.home.HomePageSearchResponse;
import com.donut.app.http.message.home.HomePageSearchV2Response;
import com.donut.app.http.message.home.WishItem;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public interface SubjectSearchContract {
    interface View extends BaseView {
        void showView(HomePageSearchResponse response);

        void onSubjectItemClick(SubjectListDetail detail);

        void onWishItemClick(WishItem detail);


    }

    interface ContextView extends  BaseView{
        void showSearchView(HomePageSearchV2Response response);

        void onContentItemClick(ContentItem contentItem);
    }


    abstract class Presenter extends BasePresenterImpl<ContextView> {

    }


}
