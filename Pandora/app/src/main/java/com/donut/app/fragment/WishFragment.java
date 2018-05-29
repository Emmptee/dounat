package com.donut.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.mvp.wish.wishing.WishingActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.WishListSelectTypePopupWindow;
import com.donut.app.entity.ConfigInfo;
import com.donut.app.entity.UserInfo;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.service.SaveBehaviourDataService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class WishFragment extends BaseFragment {

    @ViewInject(R.id.wish_viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.wish_list_top_tv)
    private TextView tvWishList;

    @ViewInject(R.id.wish_fulfill_list_top_view)
    private TextView tvWishFulfill;

    @ViewInject(R.id.wish_mine_list_top_view)
    private TextView tvWishMine;

    @ViewInject(R.id.wishing)
    private View wishing;

    @ViewInject(R.id.wish_top_view)
    private View topView;

    private List<Fragment> fragmentList = new ArrayList<>();

    private static final int WISH_TYPE_LIST = 0, WISH_TYPE_FULFILL = 1, WISH_TYPE_MINE = 2;

    private static final int WISHING_RESULT_CODE = 0, LOGIN_RESULT_CODE = 1;

    public WishFragment() {
    }

    public static WishFragment newInstance() {
        return new WishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mLayoutView = inflater.inflate(R.layout.fragment_wish, container, false);
        ViewUtils.inject(this, mLayoutView);
        initView();
        return mLayoutView;
    }

    private void initView() {

        WishListFragment wishListFragment = WishListFragment.newInstance();
        WishMineFragment wishMineFragment = WishMineFragment.newInstance();
        fragmentList.clear();
        fragmentList.add(wishListFragment);
        fragmentList.add(WishFulfillFragment.newInstance());
        fragmentList.add(wishMineFragment);

        UserInfo userInfo = SysApplication.getUserInfo();
        if (userInfo.getUserType() == 1) {
            setStarState();
        }

        viewPager.setAdapter(new WishViewPagerAdapter(getChildFragmentManager(), fragmentList));
        viewPager.addOnPageChangeListener(new ViewPageChangeListener());

        viewPager.setCurrentItem(WISH_TYPE_LIST);
        setViewText(WISH_TYPE_LIST);
        try {
            ConfigInfo info = SysApplication.getDb().findFirst(
                    Selector.from(ConfigInfo.class).where("name", "=", "WISH_ORDER"));
            if (info != null && "1".equals(info.getValue())) {
                viewPager.setCurrentItem(WISH_TYPE_FULFILL);
                setViewText(WISH_TYPE_FULFILL);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.wish_list_top_view,
            R.id.wish_fulfill_list_top_view,
            R.id.wish_mine_list_top_view,
            R.id.wishing})
    protected void btnClick(View v) {
        switch (v.getId()) {
            case R.id.wish_list_top_view:
                setClickViewText(WISH_TYPE_LIST);
                break;
            case R.id.wish_fulfill_list_top_view:
                setClickViewText(WISH_TYPE_FULFILL);
                break;
            case R.id.wish_mine_list_top_view:
                setClickViewText(WISH_TYPE_MINE);
                break;
            case R.id.wishing:
                boolean login = getContext().getSharedPreferences(Constant.SP_INFO,
                        Context.MODE_PRIVATE).getBoolean(Constant.IS_LOGIN, false);
                if (!login) {
                    startActivityForResult(new Intent(getContext(), LoginActivity.class), LOGIN_RESULT_CODE);
                    break;
                }
                startActivityForResult(new Intent(getContext(), WishingActivity.class), WISHING_RESULT_CODE);
                saveBehaviour("05");
                break;
        }

    }

    private void setClickViewText(int index) {
        switch (index) {
            case WISH_TYPE_LIST:
                if (viewPager.getCurrentItem() == WISH_TYPE_LIST) {
                    showFilterView();
                    break;
                }
                setViewText(WISH_TYPE_LIST);
                break;
            case WISH_TYPE_FULFILL:
                if (viewPager.getCurrentItem() == WISH_TYPE_FULFILL) {
                    break;
                }
                setViewText(WISH_TYPE_FULFILL);
                break;
            case WISH_TYPE_MINE:
                if (viewPager.getCurrentItem() == WISH_TYPE_MINE) {
                    break;
                }
                setViewText(WISH_TYPE_MINE);
                break;
        }
    }

    private void setViewText(int index) {
        switch (index) {
            case WISH_TYPE_LIST:
                tvWishList.setSelected(true);
                tvWishList.setTextSize(16);
                tvWishFulfill.setSelected(false);
                tvWishFulfill.setTextSize(14);
                tvWishMine.setSelected(false);
                tvWishMine.setTextSize(14);
                viewPager.setCurrentItem(WISH_TYPE_LIST);

                WishListFragment wishListFragment = (WishListFragment) fragmentList.get(WISH_TYPE_LIST);
                wishListFragment.onRefresh();
                saveBehaviour("01");
                break;
            case WISH_TYPE_FULFILL:
                tvWishList.setSelected(false);
                tvWishList.setTextSize(14);
                tvWishFulfill.setSelected(true);
                tvWishFulfill.setTextSize(16);
                tvWishMine.setSelected(false);
                tvWishMine.setTextSize(14);
                viewPager.setCurrentItem(WISH_TYPE_FULFILL);
                WishFulfillFragment fulfillFragment = (WishFulfillFragment) fragmentList.get(WISH_TYPE_FULFILL);
                fulfillFragment.onRefresh();
                saveBehaviour("02");
                break;
            case WISH_TYPE_MINE:
                tvWishList.setSelected(false);
                tvWishList.setTextSize(14);
                tvWishFulfill.setSelected(false);
                tvWishFulfill.setTextSize(14);
                tvWishMine.setSelected(true);
                tvWishMine.setTextSize(16);
                viewPager.setCurrentItem(WISH_TYPE_MINE);
                WishMineFragment wishMineFragment = (WishMineFragment) fragmentList.get(WISH_TYPE_MINE);
                wishMineFragment.onResume();
                saveBehaviour("03");
                break;
        }
    }

    public void setStarState() {
        tvWishMine.setText("与我相关");
        wishing.setVisibility(View.GONE);
    }

    private void showFilterView() {
        final WishListFragment wishListFragment = (WishListFragment) fragmentList.get(WISH_TYPE_LIST);

        if (mPopupWindow == null) {
            mPopupWindow = new WishListSelectTypePopupWindow(getActivity(), wishListFragment.getSortType(),
                    new WishListSelectTypePopupWindow.OnClickListenerWithPosition() {
                        @Override
                        public void onItemClick(View v, int selectType) {
                            mPopupWindow.dismiss();
                            wishListFragment.refreshBySortType(selectType);
                        }
                    });
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mPopupWindow.showAsDropDown(topView);
        } else {
            int result = 0;
            int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                result = getResources().getDimensionPixelOffset(resId);
            }
            mPopupWindow.showAtLocation(topView, Gravity.NO_GRAVITY, 0, result + topView.getHeight());
        }
    }

    private WishListSelectTypePopupWindow mPopupWindow;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WishListFragment wishListFragment = (WishListFragment) fragmentList.get(WISH_TYPE_LIST);
        WishFulfillFragment wishFulfillFragment = (WishFulfillFragment) fragmentList.get(WISH_TYPE_FULFILL);
//        WishMineFragment wishMineFragment = (WishMineFragment) fragmentList.get(WISH_TYPE_MINE);
        switch (requestCode) {
            case WISHING_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    wishListFragment.refreshBySortType(wishListFragment.getSortType());
                }
//                else if (resultCode == Activity.RESULT_OK
//                        && viewPager.getCurrentItem() == WISH_TYPE_MINE) {
//                    wishMineFragment.onRefresh();
//                }
                break;
            case LOGIN_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    wishListFragment.setLoginState(true);
                    wishFulfillFragment.setLoginState(true);
                }
                break;
        }
    }

    private class WishViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        private WishViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setViewText(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        saveBehaviour("00");
    }

    @Override
    public void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(getContext(), BehaviourEnum.IP_LIST.getCode() + functionCode);
    }
}
