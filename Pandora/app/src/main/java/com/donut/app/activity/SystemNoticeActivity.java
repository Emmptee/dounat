/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: StudyInfoActivity.java
 * @Package com.bis.sportedu.activity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Administrator
 * @date 2015-8-31 下午4:43:12
 * @version V1.0
 */
package com.donut.app.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.SystemAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PushMsgResponse;
import com.donut.app.http.message.PushRequest;
import com.donut.app.http.message.SystemNoticeRequest;
import com.donut.app.http.message.SystemNoticeResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujiaojiao
 * @version 1.0
 * @ClassName: SystemNoticeActivity
 * @Description:消息中心
 * @date 2016-7-15 下午4:43:12
 */
public class SystemNoticeActivity extends BaseActivity
{
    private View mTopView;

    @ViewInject(R.id.notice_setting_cb)
    private CheckBox mNoticeSetting;

    @ViewInject(R.id.swipe_layout)
    private SwipeRefreshLayout myRefreshView;

    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecycleview;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    private View mFooterView;


    private static final int SYSTEM_REQUEST = 0;

    private static final int SYSTEM_TAG_REQUEST = 1;

    private static final int SYSTEM_SET_TAG = 2;

    private int page = 0;

    private int rows = 20;

    private List<SystemNoticeResponse.Notice> noticeList = new ArrayList<SystemNoticeResponse.Notice>();

    private SystemAdapter mAdapter;

    protected SharedPreferences sp;

    private boolean isChoice;

    private SharedPreferences sharedPreferences;

    private Editor editor;

    private boolean isTop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_notice_layout);
        StatusBarUtil.setColor(SystemNoticeActivity.this, Constant.default_bar_color);
        sp = this.getSharedPreferences(Constant.SP_INFO, Context.MODE_PRIVATE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTopView = inflater.inflate(R.layout.system_notice_top_layout, null);
        mFooterView = inflater.inflate(R.layout.recycleview_footer, null, false);
        ViewUtils.inject(this, mTopView);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.message_center), true);
        if (getSysNoti())
        {
            mNoticeSetting.setChecked(true);
        } else
        {
            mNoticeSetting.setChecked(false);
        }
        initTag();
        initView();
        sp_Info.edit().putBoolean(Constant.HAS_NEW_MSG, false).apply();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        editor.putBoolean(Constant.NOTICE_FLAG, false);
        editor.commit();
        page = 0;
        getSystemList(true);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MESSAGE.getCode()+"00",request,HeaderRequest.SYSTEM_NOTICE);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MESSAGE.getCode()+"02");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MESSAGE.getCode() + "xx");
        super.onStop();
    }

    private void initView()
    {
        sharedPreferences = getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        myRefreshView.setColorSchemeResources(R.color.refresh_tiffany);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(getLayoutManager());
        mAdapter = new SystemAdapter(this, noticeList, mTopView, mFooterView);
        mRecycleview.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewListener(new SystemAdapter.OnRecyclerViewListener()
        {
            @Override
            public boolean onItemLongClick(int position)
            {
                return false;
            }

            @Override
            public void onItemClick(int position)
            {

            }
        });

        myRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                page = 0;
                getSystemList(false);
            }
        });
        mNoticeSetting.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked)
            {
                settag(isChecked);
                isChoice = isChecked;

            }
        });
    }

    private void initTag()
    {
        PushRequest request = new PushRequest();
        request.setPushUserid(sp.getString(Constant.PUSH_USER_ID, ""));
        request.setPushChannelid(sp.getString(Constant.PUSH_CHANNEL_ID, ""));
        sendNetRequest(request, HeaderRequest.QUERY_TAG, SYSTEM_TAG_REQUEST,
                false);
    }

    private void settag(boolean tag)
    {
        PushRequest request = new PushRequest();
        request.setPushUserid(sp.getString(Constant.PUSH_USER_ID, ""));
        request.setPushChannelid(sp.getString(Constant.PUSH_CHANNEL_ID, ""));
        request.setOsType("0");
        if(getUserInfo()!=null){
            request.setUserId(getUserInfo().getUserId());
        }
        if(tag){
            if(getLoginStatus()){
                request.setA01Status(2);
                if(getUserInfo().getUserType()==0){
                    request.setUserType(String.valueOf(1));
                }else{
                    request.setUserType(String.valueOf(0));
                }
            }else{
                request.setA01Status(1);
            }
            SaveBehaviourDataService.startAction(this, BehaviourEnum.MESSAGE.getCode()+"01",request,HeaderRequest.SET_TAG);
        }else{
            request.setA01Status(0);
        }

        //request.setPushStatus(tag ? "0" : "1");
        sendNetRequest(request, HeaderRequest.SET_TAG, SYSTEM_SET_TAG, false);
    }

    SystemNoticeRequest request;
    private void getSystemList(boolean isShowLoading)
    {
        request = new SystemNoticeRequest();
        request.setPage(page);
        request.setRows(rows);
        if(getLoginStatus()){
          request.setUserId(getUserInfo().getUserId());
        }
        sendNetRequest(request, HeaderRequest.SYSTEM_NOTICE, SYSTEM_REQUEST,
                isShowLoading);
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case SYSTEM_REQUEST:
                SystemNoticeResponse sysRes = JsonUtils.fromJson(response,
                        SystemNoticeResponse.class);
                if (sysRes != null && COMMON_SUCCESS.equals(sysRes.getCode()))
                {
                    if (page == 0)
                    {
                        mFooterView.setVisibility(View.GONE);
                        noticeList.clear();
                        myRefreshView.setRefreshing(false);
                    } else if (sysRes.getNoticeList() == null
                            || sysRes.getNoticeList().size() < rows)
                    {
                        mFooterView.setVisibility(View.VISIBLE);
                        mAdapter.setNoMoreData(true);
                    } else
                    {
                        mFooterView.setVisibility(View.GONE);
                    }
                    if (sysRes.getNoticeList() != null && sysRes.getNoticeList().size() > 0)
                    {
                        mNoData.setVisibility(View.GONE);
                    } else
                    {
                        if (page == 0)
                        {
                            mNoData.setVisibility(View.VISIBLE);
                        } else
                        {
                            mNoData.setVisibility(View.GONE);
                        }
                    }
                    noticeList.addAll(sysRes.getNoticeList());
                    mAdapter.notifyDataSetChanged();
                    page++;
                } else
                {

                }

                break;
            case SYSTEM_TAG_REQUEST:
                PushMsgResponse res = JsonUtils.fromJson(response,
                        PushMsgResponse.class);
                mNoticeSetting.setClickable(true);
                if (COMMON_SUCCESS.equals(res.getCode()))
                {
                    if ("0".equals(res.getPushStatus()))
                    {
                        mNoticeSetting.setChecked(false);
                    } else
                    {
                        mNoticeSetting.setChecked(true);
                    }

                } else
                {
                    mNoticeSetting.setChecked(false);
                }
                break;
            case SYSTEM_SET_TAG:
                BaseResponse setRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(setRes.getCode()))
                {
                    if (isChoice)
                    {
                        mNoticeSetting.setChecked(true);
                        setSysNoti(true);
                    } else
                    {
                        setSysNoti(false);
                        mNoticeSetting.setChecked(false);
                    }

                } else
                {
                    ToastUtil.showShort(SystemNoticeActivity.this, setRes.getMsg());
                }
                break;

            default:
                break;
        }
    }

    private ABaseLinearLayoutManager getLayoutManager()
    {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                this);
        layoutManager.setOnRecyclerViewScrollLocationListener(mRecycleview,
                new OnRecyclerViewScrollLocationListener()
                {

                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView)
                    {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView)
                    {
                        if (!isTop)
                        {
                            getSystemList(false);
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;

                    }
                });

        return layoutManager;
    }


    /**
     * 保存是否接受系统通知
     *
     * @return
     */
    public void setSysNoti(boolean state)
    {
        sp_Info.edit().putBoolean(Constant.NOTICE_STATE, state).commit();
    }

    public boolean getSysNoti()
    {
        return sp_Info.getBoolean(Constant.NOTICE_STATE, true);
    }
}
