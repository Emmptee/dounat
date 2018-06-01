package com.donut.app.mvp.spinOff;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.customview.SpinOffBoonsSelectTypePopupWindow;
import com.donut.app.databinding.ActivitySpinOffBinding;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.spinOff.boons.SpinOffBoonsFragment;
import com.donut.app.mvp.spinOff.goods.SpinOffGoodsFragment;
import com.donut.app.mvp.spinOff.plans.SpinOffPlansFragment;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public class SpinOffActivity extends MVPBaseActivity<ActivitySpinOffBinding, SpinOffPresenter>
        implements SpinOffContract.View {

    public static final String FROM_HOME_BOTTOM = "FROM_HOME_BOTTOM", SPIN_OFF_TYPE = "SPIN_OFF_TYPE";

    private static final int GOODS_TYPE = 0, BOONS_TYPE = 1, PLANS_TYPE = 2;

    private List<Fragment> fragmentList = new ArrayList<>();

    private SpinOffBoonsSelectTypePopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_spin_off;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle("明星衍生品", true);

        fragmentList.clear();
        fragmentList.add(SpinOffGoodsFragment.newInstance());
        fragmentList.add(SpinOffBoonsFragment.newInstance());
        fragmentList.add(SpinOffPlansFragment.newInstance());


        mViewBinding.viewpager.setAdapter(new SpinOffViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        mViewBinding.viewpager.addOnPageChangeListener(new ViewPageChangeListener());

        mViewBinding.viewpager.setCurrentItem(GOODS_TYPE);
        setViewText(GOODS_TYPE);

        initToolBar();
    }

    private void initToolBar() {
        mViewBinding.toolbar.setTitle("");
        setSupportActionBar(mViewBinding.toolbar);
        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        //使用v7的时候,需要使用MenuItemCompat
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchView.SearchAutoComplete textView
                = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        textView.setTextSize(15f);
//        textView.setBackgroundColor(Color.WHITE);
//        ImageView mCloseButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
//        mCloseButton.setImageDrawable(getResources().getDrawable(R.drawable.edit_close_icon));

        searchView.setQueryHint("请输入关键字进行搜索");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchData(null);
                return true;
            }
        });

//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //可以自己new ComponentName来指定Activity
        //ComponentName componentName = new ComponentName(this, SearchResultActivity.class);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void initEvent() {
        mViewBinding.tvGoods.setOnClickListener(this);
        mViewBinding.topBoonsView.setOnClickListener(this);
        mViewBinding.tvPlans.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(FROM_HOME_BOTTOM, false)) {
            int type = intent.getIntExtra(SPIN_OFF_TYPE, -1);
            switch (type) {
                case 2:
                    setViewText(GOODS_TYPE);
                    break;
                case 3:
                    setViewText(PLANS_TYPE);
                    break;
                case 4:
                    setViewText(BOONS_TYPE);
                    break;
            }
        }
    }

    private void searchData(String query) {
        SpinOffGoodsFragment goodsFragment = (SpinOffGoodsFragment) fragmentList.get(0);
        goodsFragment.setSearchStarName(query);

        SpinOffBoonsFragment boonsFragment = (SpinOffBoonsFragment) fragmentList.get(1);
        boonsFragment.setSearchStarName(query);

        SpinOffPlansFragment plansFragment = (SpinOffPlansFragment) fragmentList.get(2);
        plansFragment.setSearchStarName(query);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_goods:
                setClickViewText(GOODS_TYPE);
                break;
            case R.id.top_boons_view:
                setClickViewText(BOONS_TYPE);
                break;
            case R.id.tv_plans:
                setClickViewText(PLANS_TYPE);
                break;
        }
    }

    private void setClickViewText(int index) {
        switch (index) {
            case GOODS_TYPE:
                if (mViewBinding.viewpager.getCurrentItem() == GOODS_TYPE) {
                    break;
                }
                setViewText(GOODS_TYPE);
                break;
            case BOONS_TYPE:
                if (mViewBinding.viewpager.getCurrentItem() == BOONS_TYPE) {
                    showFilterView();
                    break;
                }
                setViewText(BOONS_TYPE);
                break;
            case PLANS_TYPE:
                if (mViewBinding.viewpager.getCurrentItem() == PLANS_TYPE) {
                    break;
                }
                setViewText(PLANS_TYPE);
                break;
        }
    }

    private void showFilterView() {
        final SpinOffBoonsFragment boonsFragment = (SpinOffBoonsFragment) fragmentList.get(1);

        if (mPopupWindow == null) {
            mPopupWindow = new SpinOffBoonsSelectTypePopupWindow(this, boonsFragment.getSearchType(),
                    new SpinOffBoonsSelectTypePopupWindow.OnClickListenerWithPosition() {
                        @Override
                        public void onItemClick(View v, int selectType) {
                            boonsFragment.setSearchType(selectType);
                            mPopupWindow.dismiss();
                        }
                    });
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mPopupWindow.showAsDropDown(mViewBinding.topLayout);
        } else {
            int result = 0;
            int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                result = getResources().getDimensionPixelOffset(resId);
            }
            result += mViewBinding.toolbar.getHeight();
            mPopupWindow.showAtLocation(mViewBinding.topLayout, Gravity.NO_GRAVITY, 0,
                    result + mViewBinding.topLayout.getHeight());
        }
    }


    private void setViewText(int index) {
        switch (index) {
            case GOODS_TYPE:
                mViewBinding.tvGoods.setSelected(true);
                mViewBinding.tvGoods.setTextSize(16);
                mViewBinding.tvBoons.setSelected(false);
                mViewBinding.tvBoons.setTextSize(14);
                mViewBinding.tvPlans.setSelected(false);
                mViewBinding.tvPlans.setTextSize(14);
                mViewBinding.viewpager.setCurrentItem(GOODS_TYPE);

                break;
            case BOONS_TYPE:
                mViewBinding.tvGoods.setSelected(false);
                mViewBinding.tvGoods.setTextSize(14);
                mViewBinding.tvBoons.setSelected(true);
                mViewBinding.tvBoons.setTextSize(16);
                mViewBinding.tvPlans.setSelected(false);
                mViewBinding.tvPlans.setTextSize(14);
                mViewBinding.viewpager.setCurrentItem(BOONS_TYPE);

                break;
            case PLANS_TYPE:
                mViewBinding.tvGoods.setSelected(false);
                mViewBinding.tvGoods.setTextSize(14);
                mViewBinding.tvBoons.setSelected(false);
                mViewBinding.tvBoons.setTextSize(14);
                mViewBinding.tvPlans.setSelected(true);
                mViewBinding.tvPlans.setTextSize(16);
                mViewBinding.viewpager.setCurrentItem(PLANS_TYPE);

                break;
        }
    }

    private class SpinOffViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        private SpinOffViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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

}