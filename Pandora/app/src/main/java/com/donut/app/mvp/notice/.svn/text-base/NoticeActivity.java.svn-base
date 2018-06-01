package com.donut.app.mvp.notice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityNoticeBinding;
import com.donut.app.fragment.NoticeHotFragment;
import com.donut.app.fragment.NoticeNewFragment;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class NoticeActivity extends MVPBaseActivity<ActivityNoticeBinding, NoticePresenter>
        implements NoticeContract.View {

    private Fragment hotFragment, newFragment;

    private static final String HOT_TAG = "HOT_TAG", NEW_TAG = "NEW_TAG";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle("活动/公告", true);

        hotFragment = NoticeHotFragment.newInstance();
        newFragment = NoticeNewFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.notice_frame, hotFragment, HOT_TAG).commit();
        mPresenter.saveBehaviour("05");

        mViewBinding.noticeTopIvHot.setSelected(true);
    }

    @Override
    protected void initEvent() {
        mViewBinding.noticeTopIvHot.setOnClickListener(this);
        mViewBinding.noticeTopIvNew.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.notice_top_iv_hot:
                mViewBinding.noticeTopIvHot.setSelected(true);
                mViewBinding.noticeTopIvNew.setSelected(false);
                if (hotFragment != null
                        && getSupportFragmentManager().findFragmentByTag(HOT_TAG) == null
                        && !hotFragment.isAdded()) {
                    FragmentTransaction fragmentTransaction
                            = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.notice_frame, hotFragment, HOT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                    mPresenter.saveBehaviour("05");
                }
                break;
            case R.id.notice_top_iv_new:
                mViewBinding.noticeTopIvNew.setSelected(true);
                mViewBinding.noticeTopIvHot.setSelected(false);
                if (newFragment != null
                        && getSupportFragmentManager().findFragmentByTag(NEW_TAG) == null
                        && !newFragment.isAdded()) {
                    FragmentTransaction fragmentTransaction
                            = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.notice_frame, newFragment, NEW_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                    mPresenter.saveBehaviour("06");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.saveBehaviour("07");
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00");
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        super.onStop();
    }
}