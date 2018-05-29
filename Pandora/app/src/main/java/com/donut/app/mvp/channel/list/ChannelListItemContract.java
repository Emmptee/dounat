package com.donut.app.mvp.channel.list;

import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public interface ChannelListItemContract {
    interface View extends BaseView {
        void showView(SubjectListDetail detail);

        void collectSuccess();
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }
}
