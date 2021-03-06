package com.donut.app.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.RewardIncomeRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PresentListRequest;
import com.donut.app.http.message.RewardIncomeListDetail;
import com.donut.app.http.message.RewardIncomeListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RewardIncomeActivity extends BaseActivity {

    @ViewInject(R.id.reward_income_tv_startTime)
    private TextView tvStartTime;

    @ViewInject(R.id.reward_income_tv_endTime)
    private TextView tvEndTime;

    @ViewInject(R.id.reward_income_list)
    private RecyclerView rvList;

    @ViewInject(R.id.reward_income_tv_allAmount)
    private TextView tvAllAmount;

    @ViewInject(R.id.reward_income_tv_already)
    private TextView tvAlready;

    @ViewInject(R.id.reward_income_tv_could)
    private TextView tvCould;

    @ViewInject(R.id.head_right_tv)
    private TextView tvRight;

    @ViewInject(R.id.reward_income_tv_msg)
    private View viewNoMsg;

    private int page = 0, rows = 20;

    private String startTime = "", endTime = "";

    private List<RewardIncomeListDetail> presentList = new ArrayList<>();

    private RewardIncomeRecyclerViewAdapter mAdapter;

    private View footerView;

    private Calendar calendar = Calendar.getInstance();

    private static final int PRESENT_REQUEST = 1, RECHARGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_income);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.income_tips), true);
        tvRight.setText(R.string.cash_present_title);

        loadData(true);
        initView();
    }

    private void initView() {
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new RewardIncomeRecyclerViewAdapter(presentList, footerView);
        rvList.setAdapter(mAdapter);
        rvList.setLayoutManager(getLayoutManager());

        int nowY = calendar.get(Calendar.YEAR);
        int nowM = calendar.get(Calendar.MONTH) + 1;
        int nowD = calendar.get(Calendar.DAY_OF_MONTH);
        String strM = nowM < 10 ? "0" + String.valueOf(nowM) : String.valueOf(nowM);
        String strD = nowD < 10 ? "0" + String.valueOf(nowD) : String.valueOf(nowD);
        tvEndTime.setText(nowY + "/" + strM + "/" + strD);
        endTime = nowY + "-" + strM + "-" + strD;
    }

    private boolean isTop;

    private RecyclerView.LayoutManager getLayoutManager() {
        ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(this);
        layoutManager.setOnRecyclerViewScrollLocationListener(rvList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            footerView.setVisibility(View.VISIBLE);
                            page++;
                            loadData(false);
                        }
                        isTop = false;
                    }
                });

        return layoutManager;
    }

    private PresentListRequest initRequest;

    private void loadData(boolean isShowDialog) {
        initRequest = new PresentListRequest();
        initRequest.setStartTime(startTime);
        initRequest.setEndTime(endTime);
        initRequest.setPage(page);
        initRequest.setRows(rows);
        sendNetRequest(initRequest, HeaderRequest.REWARD_INCOME, isShowDialog);
    }

    @OnClick({R.id.reward_income_tv_search,
            R.id.reward_income_tv_startTime,
            R.id.reward_income_tv_endTime,
            R.id.reward_income_tv_present,
            R.id.menu, R.id.reward_recharge})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.reward_income_tv_search:
                if (checkTime()) {
                    break;
                }
                page = 0;
                loadData(false);
                saveBehaviour("02", initRequest, HeaderRequest.REWARD_INCOME);
                break;
            case R.id.reward_income_tv_startTime:
                showDatePicker(tvStartTime);
                break;
            case R.id.reward_income_tv_endTime:
                showDatePicker(tvEndTime);
                break;
            case R.id.menu:
                if (getLoginStatus()) {
                    launchActivity(CashPresentActivity.class);
                    saveBehaviour("03");
                } else {
                    showToast(getString(R.string.no_login_msg));
                    launchActivity(LoginActivity.class);
                }
                break;
            case R.id.reward_income_tv_present:
                if (getLoginStatus()) {
                    saveBehaviour("01");
                    String balance = tvCould.getText().toString();
                    if ("0".equals(balance)) {
                        showToast("暂无可提现麦圈");
                        break;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(PresentAddActivity.DONUT_NUM, balance);
                    launchActivityForResult(PresentAddActivity.class, bundle, PRESENT_REQUEST);
                } else {
                    showToast(getString(R.string.no_login_msg));
                    launchActivity(LoginActivity.class);
                }
                break;
            case R.id.reward_recharge:
                launchActivityForResult(RechargeActivity.class, RECHARGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PRESENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    page = 0;
                    loadData(false);
                }
                break;
            case RECHARGE:
                if (resultCode == RESULT_OK) {
                    page = 0;
                    loadData(false);
                }
                break;
        }
    }

    private boolean checkTime() {
        if ("".equals(startTime) || "".equals(endTime)) {
            showToast("请选择查询时间");
            return true;
        }
        int startYear = Integer.valueOf(startTime.substring(0, 4));
        int startMonth = Integer.valueOf(startTime.substring(5, 7));
        int startDay = Integer.valueOf(startTime.substring(8, 10));

        int endYear = Integer.valueOf(endTime.substring(0, 4));
        int endMonth = Integer.valueOf(endTime.substring(5, 7));
        int endDay = Integer.valueOf(endTime.substring(8, 10));
        if (endYear < startYear
                || (endYear == startYear && endMonth < startMonth)
                || (endYear == startYear && endMonth == startMonth
                && endDay < startDay)) {
            showToast("结束时间不能小于开始时间!");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        footerView.setVisibility(View.GONE);
        RewardIncomeListResponse rewardIncomeListResponse = JsonUtils.fromJson(response, RewardIncomeListResponse.class);
        if (COMMON_SUCCESS.equals(rewardIncomeListResponse.getCode())) {
            DecimalFormat fnum = new DecimalFormat("#0.00");
            tvAllAmount.setText(fnum.format(rewardIncomeListResponse.getTotalIncome()));
            tvAlready.setText(String.valueOf((int) rewardIncomeListResponse.getTotalPresentNum()));
            tvCould.setText(fnum.format(rewardIncomeListResponse.getBalance()));
            showData(rewardIncomeListResponse.getRewardIncomeList());
        } else {
            showToast(rewardIncomeListResponse.getMsg());
        }
    }

    private void showData(List<RewardIncomeListDetail> list) {
        if (page == 0) {
            presentList.clear();
        }
        if (list != null && list.size() > 0) {
            presentList.addAll(list);
        }
        if (presentList.size() <= 0) {
            viewNoMsg.setVisibility(View.VISIBLE);
        } else {
            viewNoMsg.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showDatePicker(TextView view) {
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.Theme_Date_Dialog, new DateSetListener(view),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener {

        private TextView textView;

        Calendar calendar = Calendar.getInstance();

        public DateSetListener(TextView view) {
            textView = view;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            switch (textView.getId()) {
                case R.id.reward_income_tv_startTime:
                    if (year > calendar.get(Calendar.YEAR)
                            || (year == calendar.get(Calendar.YEAR) && monthOfYear > calendar.get(Calendar.MONTH))
                            || (year == calendar.get(Calendar.YEAR) && monthOfYear == calendar.get(Calendar.MONTH)
                            && dayOfMonth > calendar.get(Calendar.DAY_OF_MONTH))) {
                        showToast("开始时间不能大于当前时间!");
                        return;
                    } else {
                        int month = monthOfYear + 1;
                        String strM = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                        String strD = dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                        tvStartTime.setText(year + "/" + strM + "/" + strD);
                        startTime = year + "-" + strM + "-" + strD;
                    }
                    break;
                case R.id.reward_income_tv_endTime:
                    int startYear = 0, startMonth = 0, startDay = 0;
                    if (!"".equals(startTime)) {
                        startYear = Integer.valueOf(startTime.substring(0, 4));
                        startMonth = Integer.valueOf(startTime.substring(5, 7));
                        startDay = Integer.valueOf(startTime.substring(8, 10));
                    }
                    if (year < startYear
                            || (year == startYear && monthOfYear + 1 < startMonth)
                            || (year == startYear && monthOfYear + 1 == startMonth
                            && dayOfMonth < startDay)) {
                        showToast("结束时间不能小于开始时间!");
                        return;
                    } else {
                        int month = monthOfYear + 1;
                        String strM = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                        String strD = dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                        tvEndTime.setText(year + "/" + strM + "/" + strD);
                        endTime = year + "-" + strM + "-" + strD;
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("04");
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00", initRequest, HeaderRequest.REWARD_INCOME);
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REWARD_INCOME.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REWARD_INCOME.getCode() + functionCode, request, header);
    }
}
