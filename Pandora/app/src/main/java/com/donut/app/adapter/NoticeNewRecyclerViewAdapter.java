package com.donut.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.adapter.ABRecyclerViewHolder;
import com.bis.android.plug.refresh_recycler.adapter.ABRecyclerViewTypeAdapter;
import com.bis.android.plug.refresh_recycler.adapter.extra.ABRecyclerViewTypeExtraHolder;
import com.bis.android.plug.refresh_recycler.adapter.typeadapter.ABAdapterTypeRender;
import com.donut.app.R;
import com.donut.app.fragment.NoticeNewFragment;
import com.donut.app.http.message.noticeBoard.NoticeBoardListModel;

import java.util.List;

public class NoticeNewRecyclerViewAdapter extends ABRecyclerViewTypeAdapter {

    private Context mContext;
    private final List<NoticeBoardListModel> mModelList;
    private final NoticeNewFragment.OnNoticeNewFragmentListener mListener;

    private static String checkItemUuid;

    public NoticeNewRecyclerViewAdapter(Context context, List<NoticeBoardListModel> items,
                                        NoticeNewFragment.OnNoticeNewFragmentListener listener) {
        mContext = context;
        mModelList = items;
        mListener = listener;
    }

    @Override
    public ABAdapterTypeRender<ABRecyclerViewHolder> getAdapterTypeRender(int type) {
        return new ItemViewReader(mContext, this);
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }

    private class ItemViewReader implements ABAdapterTypeRender<ABRecyclerViewHolder> {
        private Context context;
        private NoticeNewRecyclerViewAdapter adapter;
        private ABRecyclerViewHolder holder;

        ItemViewReader(Context context, NoticeNewRecyclerViewAdapter adapter) {
            this.context = context;
            this.adapter = adapter;

            View view = LayoutInflater.from(context).inflate(R.layout.fragment_notice_new, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            holder = new ABRecyclerViewTypeExtraHolder(view);
        }

        @Override
        public ABRecyclerViewHolder getReusableComponent() {
            return holder;
        }

        @Override
        public void fitEvents() {
            holder.obtainView(R.id.fragment_notice_new_top_layout)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkItemUuid != null && checkItemUuid.equals(mModel.getUuid())) {
                                checkItemUuid = null;
                            } else {
                                checkItemUuid = mModel.getUuid();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
            holder.obtainView(R.id.fragment_notice_new_webView)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            if (null != mListener) {
                                mListener.onScrollTouch((WebView) view, event);
                            }
                            return false;
                        }
                    });
        }

        private NoticeBoardListModel mModel;

        @Override
        public void fitDatas(final int position) {
            mModel = mModelList.get(position);
            holder.obtainView(R.id.fragment_notice_new_top_name, TextView.class).setText(mModel.getTitle());
            TextView topState = holder.obtainView(R.id.fragment_notice_new_top_state, TextView.class);

            WebView view = holder.obtainView(R.id.fragment_notice_new_webView, WebView.class);

            view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            view.getSettings().setJavaScriptEnabled(true);
            view.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
            view.loadData(mModel.getMediaText(), "text/html; charset=UTF-8", null);

            if (checkItemUuid != null && checkItemUuid.equals(mModel.getUuid())) {
                view.setVisibility(View.VISIBLE);
                Drawable drawable = context.getResources().getDrawable(R.drawable.activity_notice_item_top_down_icon);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                topState.setCompoundDrawables(null, null, drawable, null);
                holder.obtainView(R.id.fragment_notice_new_bottom).setVisibility(View.GONE);
            } else {
                view.setVisibility(View.GONE);
                Drawable drawable = context.getResources().getDrawable(R.drawable.activity_notice_item_top_up_icon);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                topState.setCompoundDrawables(null, null, drawable, null);
                holder.obtainView(R.id.fragment_notice_new_bottom).setVisibility(View.VISIBLE);
            }
        }
    }

    public void setCheckItemUuid(String checkItemUuid) {
        NoticeNewRecyclerViewAdapter.checkItemUuid = checkItemUuid;
    }
}
