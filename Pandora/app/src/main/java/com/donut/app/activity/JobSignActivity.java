package com.donut.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.JobSignRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.EditUserInfoResponse;
import com.donut.app.http.message.JobCodeResponse;
import com.donut.app.http.message.PersonalModifyRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

public class JobSignActivity extends BaseActivity {

    @ViewInject(R.id.activity_job_recycler)
    private RecyclerView recyclerView;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    private JobSignRecyclerViewAdapter viewAdapter;

    private static final int JOB_CODE_REQUEST = 1, SAVE_CODE = 2;

    public static final String SELECTED_NAME = "SELECTED_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_sign);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.professional), true);
        mRight.setText(getString(R.string.save));
        loadData();
    }

    private void loadData() {
        sendNetRequest(new Object(), HeaderRequest.JOB_CODE, JOB_CODE_REQUEST);
    }

    @OnClick({R.id.menu})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                //保存
                saveData();
                break;
        }
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case JOB_CODE_REQUEST:
                JobCodeResponse jobResponse
                        = JsonUtils.fromJson(response, JobCodeResponse.class);
                if (COMMON_SUCCESS.equals(jobResponse.getCode())) {
                    showView(jobResponse.getJobList());
                } else {
                    showToast(jobResponse.getMsg());
                }
                break;
            case SAVE_CODE:
                EditUserInfoResponse res = JsonUtils.fromJson(response,
                        EditUserInfoResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    if (res.getIsFirst() == 1) {
                        saveFirstComplete();
                    }
                    onBack();
                } else {
                    showToast(res.getMsg());
                }
                break;
        }
    }

    private void showView(List<JobCodeResponse.JobCode> jobList) {

        viewAdapter = new JobSignRecyclerViewAdapter(jobList);
        String selectedName = getIntent().getStringExtra(SELECTED_NAME);
        viewAdapter.setSelectedName(selectedName);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void saveData() {
        PersonalModifyRequest request = new PersonalModifyRequest();
        request.setJob(viewAdapter.getSelectedName());
        sendNetRequest(request, HeaderRequest.MODIFY_PERSONAL_INFO, SAVE_CODE);
    }

    private void saveFirstComplete() {
        SharedPreferences.Editor editor = sp_Info.edit();
        editor.putLong(Constant.VIP_TIME, System.currentTimeMillis());
        editor.commit();
    }

    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_NAME, viewAdapter.getSelectedName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
