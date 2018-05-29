package com.donut.app.activity;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.donut.app.AppManager;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.fragment.ShakeStarFragment;
import com.donut.app.mvp.home.SweetWheatCirclesFragment;
import com.donut.app.fragment.WishFragment;
import com.donut.app.mvp.home.HomeFragment;
import com.donut.app.mvp.home.IdolFragment;
import com.donut.app.mvp.mine.MineFragment;
import com.donut.app.mvp.star.area.StarAreaFragment;
import com.donut.app.mvp.welcome.WelcomeActivity;
import com.donut.app.receiver.PushMessageReceiver;
import com.donut.app.receiver.ScreenReceiver;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.AppConfigUtil;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;

import java.util.Map;
import java.util.UUID;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public static boolean isStar = true;

    private HomeFragment homeFragment = new HomeFragment();

    private StarAreaFragment starFragment = new StarAreaFragment();

//    private ChannelFragment channelFragment = new ChannelFragment();
    private ShakeStarFragment shakeStarFragment = new ShakeStarFragment();
    private SweetWheatCirclesFragment sweetWheatCirclesFragment=new SweetWheatCirclesFragment();
    private IdolFragment idolFragment=new IdolFragment();
    private WishFragment ipFragment = new WishFragment();

    private MineFragment mineFragment = new MineFragment();

    private FragmentManager fragmentManager;

    @ViewInject(R.id.tab_rg)
    private RadioGroup tabRg;

    @ViewInject(R.id.hot_rb)
    private RadioButton hotRb;

    @ViewInject(R.id.ip_rb)
    private RadioButton ipRb;

    @ViewInject(R.id.star_rb)
    private RadioButton starRb;
    @ViewInject(R.id.idol_rb)
    private RadioButton idol_rb;

    @ViewInject(R.id.channel_rb)
    private RadioButton channelRb;

    private ScreenReceiver screenReceiver;

    public static final String NOTICE = "notice";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        fragmentManager = getSupportFragmentManager();
        initTitle();

        screenReceiver = new ScreenReceiver(this);
        screenReceiver.requestScreenStateUpdate(new ScreenReceiver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                new AppConfigUtil(HomeActivity.this).setStarCode(UUID.randomUUID().toString());
                new AppConfigUtil(HomeActivity.this).setStarTime(System.currentTimeMillis());
                SaveBehaviourDataService.startAction(HomeActivity.this, AppConfigUtil.getBehaviourHeader() + "99999");
            }

            @Override
            public void onScreenOff() {
                SaveBehaviourDataService.startAction(HomeActivity.this, AppConfigUtil.getBehaviourHeader() + "88888");
            }
        });

        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限", Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void initTitle() {
        tabRg.setOnCheckedChangeListener(this);

        if (getIntent().getBooleanExtra(NOTICE, false)) {
            ipRb.setChecked(true);
        } else {
            hotRb.setChecked(true);
        }

        String noticeJson = getIntent().getStringExtra(WelcomeActivity.NOTICE_JSON_CONTENT);
        if (noticeJson != null && noticeJson.length() > 0) {
            try {
                PushMessageReceiver.updateContent(this, noticeJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private int choiceId;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        choiceId = checkedId;
        switch (checkedId) {
            case R.id.hot_rb:
                if (!homeFragment.isAdded()) {
//                    fragmentTransaction.replace(R.id.realtabcontent,
//                            homeFragment).commitAllowingStateLoss();
                    fragmentTransaction.replace(R.id.realtabcontent,
                            sweetWheatCirclesFragment).commitAllowingStateLoss();
                }
                StatusBarUtil.StatusBarDarkMode(HomeActivity.this, StatusBarUtil.StatusBarLightMode(HomeActivity.this));
                StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
                sweetWheatCirclesFragment.searchVisible  = false;
                break;
            case R.id.idol_rb:
                if (!idolFragment.isAdded()) {
                    fragmentTransaction.replace(R.id.realtabcontent,
                            idolFragment).commitAllowingStateLoss();
                }
                StatusBarUtil.StatusBarDarkMode(HomeActivity.this, StatusBarUtil.StatusBarLightMode(HomeActivity.this));
                StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
                break;
            case R.id.star_rb:
                if (!starFragment.isAdded()) {
//                    fragmentTransaction.replace(R.id.realtabcontent,
//                            channelFragment).commitAllowingStateLoss();
                    fragmentTransaction.replace(R.id.realtabcontent,
                            starFragment).commitAllowingStateLoss();
                }
                StatusBarUtil.StatusBarDarkMode(HomeActivity.this, StatusBarUtil.StatusBarLightMode(HomeActivity.this));
                StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
                break;
            case R.id.channel_rb:
                //拍拍
                if (!shakeStarFragment.isAdded()) {
//                    fragmentTransaction.replace(R.id.realtabcontent,
//                            channelFragment).commitAllowingStateLoss();
                    fragmentTransaction.replace(R.id.realtabcontent,
                            shakeStarFragment).commitAllowingStateLoss();
                }
                StatusBarUtil.StatusBarDarkMode(HomeActivity.this, StatusBarUtil.StatusBarLightMode(HomeActivity.this));
                StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
                break;
            case R.id.ip_rb:
                if (!ipFragment.isAdded()) {
                    fragmentTransaction.replace(R.id.realtabcontent,
                            ipFragment).commitAllowingStateLoss();
                }
                if (StatusBarUtil.StatusBarLightMode(HomeActivity.this, StatusBarUtil.StatusBarLightMode(HomeActivity.this))) {
                    StatusBarUtil.setColor(HomeActivity.this, Constant.white_bar_color);
                } else {
                    StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
                }
                break;
            case R.id.mine_rb:
                if (!mineFragment.isAdded()) {
                    fragmentTransaction.replace(R.id.realtabcontent,
                            mineFragment).commitAllowingStateLoss();
                }
                if (StatusBarUtil.StatusBarLightMode(HomeActivity.this, StatusBarUtil.StatusBarLightMode(HomeActivity.this))) {
                    StatusBarUtil.setColor(HomeActivity.this, Constant.white_bar_color);
                } else {
                    StatusBarUtil.setColor(HomeActivity.this, Constant.default_bar_color);
                }
                break;
        }

        disableRadioGroup(tabRg);

        group.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableRadioGroup(tabRg);
            }
        }, 500);
    }

    private void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStar = (getLoginStatus() && getUserInfo().getUserType() == 1);

        if (isStar) {
            starRb.setVisibility(View.VISIBLE);
            idol_rb.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(R.drawable.idou_selector);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(), (int) (drawable.getMinimumHeight()));
            channelRb.setCompoundDrawables(null,drawable, null, null);
//            channelRb.setVisibility(View.GONE);
        } else {
            //影藏
            starRb.setVisibility(View.GONE);
            idol_rb.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(R.drawable.channel_selector);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(), (int) (drawable.getMinimumHeight()));
            channelRb.setCompoundDrawables(null,drawable, null, null);
//            channelRb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(String response, Map<String, String> headers,
                          String url, int actionId) {
        super.onSuccess(response, headers, url, actionId);

        ToastUtil.showShort(this, headers.toString());
    }

    private long mExitTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            showToast(getString(R.string.exit_app));
            mExitTime = System.currentTimeMillis();
        } else {
            exitSaveBehaviour();
            AppManager.getAppManager().AppExit(this);
        }
    }

    private void exitSaveBehaviour() {
        switch (choiceId) {
            case R.id.hot_rb:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.HOT.getCode() + "03");
                break;
            case R.id.special_rb:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT.getCode() + "03");
                break;
            case R.id.star_rb:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_ZONE.getCode() + "03");
                break;
            case R.id.ip_rb:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_LIST.getCode() + "12");
                break;
            case R.id.mine_rb:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.MINE.getCode() + "13");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        screenReceiver.unregisterScreenStateUpdate();
        super.onDestroy();
    }

    public RadioGroup getTabRg() {
        return tabRg;
    }
}