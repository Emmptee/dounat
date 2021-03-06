package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.UploadItemRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.customview.SelectPicPopupWindow;
import com.donut.app.service.UploadService;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

public class UploadManagerActivity extends BaseActivity implements UploadItemRecyclerViewAdapter.OnItemListener {

    @ViewInject(R.id.upload_manager_list)
    private RecyclerView mRV;

    private UploadItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_manager);

        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.upload_title), true);

        initView();
    }

    private void initView() {
        mAdapter = new UploadItemRecyclerViewAdapter(this);
        mRV.setHasFixedSize(true);
        mRV.setAdapter(mAdapter);
        mRV.setLayoutManager(new ABaseLinearLayoutManager(this));
    }

    @Override
    public void OnItemClick(boolean fail, String fileUrl) {
        showPopupWindow(fail, fileUrl);
    }

    @Override
    public void OnRefresh(final int adapterPosition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adapterPosition==-1) {
                    return;
                }
                mAdapter.updateItemData(adapterPosition);
                UploadManagerActivity.this.sendBroadcast(new Intent("send_challenge_success"));
            }
        });
    }

    @Override
    public void OnRefreshLoading(final UploadItemRecyclerViewAdapter.ItemViewHolder itemViewHolder) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateItemDataForProgress(itemViewHolder);
            }
        });
    }

    @Override
    public void OnEmptyList() {
//        showToast("");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (UploadService.getUploadManager().getUploadInfoMapCount() > 0) {
            showToast("您的视频正在处理中，可在我的页面查看进度");
        }

    }

    private SelectPicPopupWindow showWindow;

    private void showPopupWindow(boolean fail, final String fileUrl) {

        String[] btnNames;
        if (fail) {
            btnNames = new String[]{"重新发起", "取消发起"};
        } else {
            btnNames = new String[]{"取消发起"};
        }

        showWindow = new SelectPicPopupWindow(this, new SelectPicPopupWindow.OnClickListenerWithPosition() {
            @Override
            public void onClick(View v, int actionId) {
                showWindow.dismiss();
                switch (v.getId()) {
                    case R.id.btn_click_one:
                        try {
                            UploadService.getUploadManager().resumeUpload(fileUrl);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.btn_click_three:
                        try {
                            UploadService.getUploadManager().removeUpload(fileUrl);
                            if (UploadService.getUploadManager().getUploadInfoMapCount() <= 0) {
                                OnEmptyList();
                                break;
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                mAdapter.loadData();
                mAdapter.notifyDataSetChanged();
            }
        }, btnNames);

        showWindow.showAtLocation(this.findViewById(R.id.activity_upload_manager),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
