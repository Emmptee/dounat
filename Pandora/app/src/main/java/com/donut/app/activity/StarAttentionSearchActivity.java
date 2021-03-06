package com.donut.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.MyAttentionRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.ConcernCanclePopupWindow;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ConcernedOnStarRequest;
import com.donut.app.http.message.MyFollowListRequest;
import com.donut.app.http.message.MyFollowListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StarAttentionSearchActivity extends BaseActivity implements
        MyAttentionRecyclerViewAdapter.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener
{

    @ViewInject(R.id.my_attention_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.my_attention_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.subject_search_tv_msg)
    private TextView mNoData;

    @ViewInject(R.id.subject_search_et)
    private EditText etSearch;

    @ViewInject(R.id.subject_search_iv_etClean)
    private View ivEtClean;

    private List<MyFollowListResponse.FollowDetail> mList;

    private View footerView;

    private MyAttentionRecyclerViewAdapter mAdapter;

    private static final int ATTENTION_REQUEST = 0;

    private static final int ATTENTION_ON_STAR_REQUEST = 1;

    private String searchName;

    private int page = 0, rows = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_search);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        initView();
        showKeyBoard();
    }

    private void showKeyBoard()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                 InputMethodManager inputManager =
                                       (InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                 inputManager.showSoftInput(etSearch, 0);
            }

         }, 500);
    }

    private void initView()
    {
        mList = new ArrayList<MyFollowListResponse.FollowDetail>();
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new MyAttentionRecyclerViewAdapter(this, mList, this,
                footerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 0)
                {
                    ivEtClean.setVisibility(View.GONE);
                } else
                {
                    ivEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    page = 0;
                    searchName = v.getText().toString();
                    requestData(true);
                    etSearch.setEnabled(false);
                }
                return false;
            }
        });
    }

    MyFollowListRequest request;

    private void requestData(boolean isShowDialog)
    {
        searchName = etSearch.getText().toString();
        request = new MyFollowListRequest();
        request.setSearchName(searchName);
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.MY_ATTENTION, ATTENTION_REQUEST, isShowDialog);
        saveBehaviour("01",request,HeaderRequest.MY_ATTENTION);
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        requestData(false);
    }

    @Override
    public void OnItemClick(int position)
    {
        Intent intent = new Intent(this, StarDetailActivity.class);
        intent.putExtra(StarDetailActivity.STAR_ID, mList.get(position).getStarId());
        startActivity(intent);
    }

    private int operaPos;
    ConcernCanclePopupWindow showWindow;
    @Override
    public void onCancle(int position)
    {
        operaPos=position;
        if(mList.get(position).getStatus()==0){
            showWindow = new ConcernCanclePopupWindow(this, itemsOnClick);
            //显示窗口
            showWindow.showAtLocation(findViewById(R.id.attention_search_main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            ConcernedOnStarRequest request = new ConcernedOnStarRequest();
            request.setStarId(mList.get(position).getStarId());
            request.setOperation("0");
            sendNetRequest(request, HeaderRequest.CONCERNED_ON_STAR, ATTENTION_ON_STAR_REQUEST);
            saveBehaviour("03",request,HeaderRequest.CONCERNED_ON_STAR);
        }
    }

    //为弹出窗口实现监听类
    private ConcernCanclePopupWindow.OnClickListenerWithPosition itemsOnClick = new ConcernCanclePopupWindow.OnClickListenerWithPosition()
    {
        @Override
        public void onClick(View v, int actionId)
        {
            if (showWindow != null && showWindow.isShowing())
            {
                showWindow.dismiss();
                switch (v.getId())
                {
                    case R.id.btn_click_three:
                        ConcernedOnStarRequest request = new ConcernedOnStarRequest();
                        request.setStarId(mList.get(operaPos).getStarId());
                        request.setOperation("1");
                        sendNetRequest(request, HeaderRequest.CONCERNED_ON_STAR, ATTENTION_ON_STAR_REQUEST);
                        saveBehaviour("02",request,HeaderRequest.CONCERNED_ON_STAR);
                        break;

                    default:
                        break;
                }
            }
        }
    };


    @OnClick({R.id.subject_search_tv_cancel,
            R.id.subject_search_iv_etClean})
    protected void viewOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.subject_search_tv_cancel:
                onBackPressed();
                break;
            case R.id.subject_search_iv_etClean:
                etSearch.setText("");
                searchName = "";
                break;
        }
    }

    private void showView(List<MyFollowListResponse.FollowDetail> dataList)
    {
        if (dataList != null && dataList.size() > 0)
        {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            mNoData.setVisibility(View.GONE);
            if (page == 0)
            {
                footerView.setVisibility(View.GONE);
                mList.clear();
                swipeRefreshLayout.setRefreshing(false);
            } else
            {
                if (dataList.size() >= rows)
                {
                    footerView.setVisibility(View.GONE);
                } else
                {
                    footerView.setVisibility(View.VISIBLE);
                    mAdapter.setNoMoreData(true);
                }
            }
            page++;
            mList.addAll(dataList);
        } else
        {
            if (page == 0)
            {
                footerView.setVisibility(View.GONE);
                Drawable drawable = getResources().getDrawable(R.drawable.no_msg_icon);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mNoData.setCompoundDrawables(null, drawable, null, null);
                mNoData.setText(R.string.subject_search_no_mag);
                mNoData.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                mList.clear();
            } else
            {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                mNoData.setVisibility(View.GONE);
                footerView.setVisibility(View.VISIBLE);
                mAdapter.setNoMoreData(true);
            }

        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case ATTENTION_REQUEST:
                swipeRefreshLayout.setRefreshing(false);
                footerView.setVisibility(View.GONE);
                etSearch.setEnabled(true);
                MyFollowListResponse followResponse
                        = JsonUtils.fromJson(response, MyFollowListResponse.class);
                if (COMMON_SUCCESS.equals(followResponse.getCode()))
                {
                    showView(followResponse.getFollowList());
                } else
                {
                    showToast(followResponse.getMsg());
                }
                break;
            case ATTENTION_ON_STAR_REQUEST:
                BaseResponse basResponse
                        = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(basResponse.getCode()))
                {
                    concernOperSuccess();
                } else
                {
                    ToastUtil.showShort(StarAttentionSearchActivity.this, basResponse.getMsg());
                }
                break;
        }
    }

    private void concernOperSuccess()
    {
        if (mList.get(operaPos).getStatus() == 0)
        {
            mList.get(operaPos).setStatus(1);
        } else
        {
            mList.get(operaPos).setStatus(0);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onError(String errorMsg, String url, int actionId)
    {
        super.onError(errorMsg, url, actionId);
        etSearch.setEnabled(true);
    }

    private boolean isTop;

    private ABaseLinearLayoutManager getLayoutManager()
    {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                StarAttentionSearchActivity.this);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView,
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
                            requestData(false);
                            footerView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("02");
        super.onBackPressed();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        saveBehaviour("00");
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SEARCH_ATTENTION.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SEARCH_ATTENTION.getCode() + functionCode, request, header);
    }
}
