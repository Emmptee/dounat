package com.donut.app.mvp.auction;

import com.donut.app.http.message.auction.MyAuctionDetail;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

import java.util.List;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface MyAuctionContract {
    interface View extends BaseView {
        void showView(List<MyAuctionDetail> details);

        void OnItemClick(MyAuctionDetail detail);

        void OnItemCancelClick(MyAuctionDetail detail);

        void OnItemPayClick(MyAuctionDetail detail);

        void OnItemDeleteClick(MyAuctionDetail detail);

        void onRefreshView();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
