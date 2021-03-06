package com.donut.app.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.donut.app.adapter.CashPresentRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PresentListDetail;
import com.donut.app.http.message.PresentListRequest;
import com.donut.app.http.message.PresentListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CashPresentActivity extends BaseActivity {

    @ViewInject(R.id.cash_present_tv_startTime)
    private TextView tvStartTime;

    @ViewInject(R.id.cash_present_tv_endTime)
    private TextView tvEndTime;

    @ViewInject(R.id.cash_present_list)
    private RecyclerView rvList;

    @ViewInject(R.id.cash_present_tv_msg)
    private View viewNoMsg;

    private int page = 0, rows = 20;

    private String startTime = "", endTime = "";

    private List<PresentListDetail> presentList = new ArrayList<>();

    private CashPresentRecyclerViewAdapter mAdapter;

    private View footerView;

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_present);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.cash_present_title),true);

        loadData(true);
        initView();
    }

    private void initView() {
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new CashPresentRecyclerViewAdapter(presentList ,footerView);
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
        sendNetRequest(initRequest, HeaderRequest.CASH_PRESENT, isShowDialog);
    }

    @OnClick({R.id.cash_present_tv_search,
            R.id.cash_present_tv_startTime,
            R.id.cash_present_tv_endTime})
    private void viewOnClick(View v){
        switch (v.getId()) {
            case R.id.cash_present_tv_search:
                if(checkTime()) {
                    break;
                }
                page = 0;
                loadData(false);
                saveBehaviour("01", initRequest, HeaderRequest.CASH_PRESENT);
                break;
            case R.id.cash_present_tv_startTime:
                showDatePicker(tvStartTime);
                break;
            case R.id.cash_present_tv_endTime:
                showDatePicker(tvEndTime);
                break;
        }
    }

    private boolean checkTime() {
        if("".equals(startTime)||"".equals(endTime)){
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
        PresentListResponse presentListResponse = JsonUtils.fromJson(response,PresentListResponse.class);
        if (COMMON_SUCCESS.equals(presentListResponse.getCode())){
            showData(presentListResponse.getPresentList());
        } else {
            showToast(presentListResponse.getMsg());
        }
    }

    private void showData(List<PresentListDetail> list) {
        viewNoMsg.setVisibility(View.GONE);
        if (page == 0) {
            presentList.clear();
        }
        if(list == null || list.size() <= 0){
            if (page == 0) {
                viewNoMsg.setVisibility(View.VISIBLE);
            }
        } else {
            presentList.addAll(list);
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

    private class DateSetListener implements DatePickerDialog.OnDateSetListener{

        private TextView textView;

        Calendar calendar = Calendar.getInstance();

        public DateSetListener(TextView view) {
            textView = view;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            switch (textView.getId()) {
                case R.id.cash_present_tv_startTime:
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
                case R.id.cash_present_tv_endTime:
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
    public void onBackPressed()
    {
        saveBehaviour("02");
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00", initRequest, HeaderRequest.CASH_PRESENT);
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.CASH_PRESENT.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.CASH_PRESENT.getCode() + functionCode, request, header);
    }
}
