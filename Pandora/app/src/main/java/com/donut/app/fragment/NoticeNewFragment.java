package com.donut.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.adapter.NoticeNewRecyclerViewAdapter;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.noticeBoard.NoticeBoardListModel;
import com.donut.app.http.message.noticeBoard.NoticeBoardListRequest;
import com.donut.app.http.message.noticeBoard.NoticeBoardListResponse;
import com.donut.app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class NoticeNewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<NoticeBoardListModel> mModelList = new ArrayList<>();

    private OnNoticeNewFragmentListener mListener;

    public static NoticeNewFragment newInstance() {
        return new NoticeNewFragment();
    }

    private int page = 0, rows = 5;

    private static final int NOTICE_REQUEST = 1;

    private RecyclerView mRecyclerView;

    private NoticeNewRecyclerViewAdapter mAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    private TextView no_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = new Listener();

        if (mModelList.size() <= 0) {
            page = 0;
            loadData(true);
        }
    }

    private void loadData(boolean showLoading) {
        NoticeBoardListRequest request = new NoticeBoardListRequest();
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.NOTICE_BOARD_CODE, NOTICE_REQUEST, showLoading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notice_list);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mAdapter = new NoticeNewRecyclerViewAdapter(getContext(), mModelList, mListener);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_notice_srl);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
        no_data = (TextView) view.findViewById(R.id.no_data);
        no_data.setText("暂时没有公告");
        return view;
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case NOTICE_REQUEST:
                NoticeBoardListResponse board
                        = JsonUtils.fromJson(response, NoticeBoardListResponse.class);
                if (COMMON_SUCCESS.equals(board.getCode())) {
                    showView(board.getNoticeBoardList());
                } else {
                    showToast(board.getMsg());
                }
                break;
        }
    }

    private void showView(List<NoticeBoardListModel> list) {
        if (page == 0) {
            mModelList.clear();
            mRefreshLayout.setRefreshing(false);
            try {
                mAdapter.setCheckItemUuid(list.get(0).getUuid());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (list != null && list.size() > 0) {
            no_data.setVisibility(View.GONE);
            mModelList.addAll(list);
        } else {
            if (page == 0) {
                no_data.setVisibility(View.VISIBLE);
            }
        }
        mAdapter.notifyDataSetChanged();
        page++;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData(false);
    }

    public interface OnNoticeNewFragmentListener {
        void onScrollTouch(WebView view, MotionEvent event);
    }

    private class Listener implements OnNoticeNewFragmentListener {
        @Override
        public void onScrollTouch(WebView view, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    recycleViewCanScroll = false;
                    break;
                case MotionEvent.ACTION_UP:
                    recycleViewCanScroll = true;
                    break;
            }
        }
    }

    private boolean recycleViewCanScroll = true;//, isTop;

    private LinearLayoutManager getLayoutManager() {
        ABaseLinearLayoutManager layoutManager = new MyLinearLayoutManager(getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mRecyclerView,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        //isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        //if (!isTop) {
                        loadData(false);
                        //}
                        //isTop = false;
                    }
                });
        return layoutManager;
    }

    class MyLinearLayoutManager extends ABaseLinearLayoutManager {

        MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return recycleViewCanScroll;
        }
    }
}
