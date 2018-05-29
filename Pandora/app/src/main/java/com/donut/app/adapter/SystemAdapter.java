package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.donut.app.R;
import com.donut.app.http.message.SystemNoticeResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class SystemAdapter extends RecyclerView.Adapter<ViewHolder>
{

    public static interface OnRecyclerViewListener
    {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener)
    {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public OnRecyclerViewListener getOnRecyclerViewListener()
    {
        return onRecyclerViewListener;
    }

    private View footView;

    private View topView;

    private List<SystemNoticeResponse.Notice> noticeList;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;

    private static final int TYPE_TOP = 2;

    private boolean noMoreData;

    private Context mContext;

    public SystemAdapter(Context context, List<SystemNoticeResponse.Notice> contentData, View topView, View footView)
    {
        this.mContext = context;
        this.noticeList = contentData;
        this.footView = footView;
        this.topView=topView;
    }

    @Override
    public int getItemCount()
    {
        // 总条数+ 1（footerView）
        return noticeList.size() + 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        // 最后一个item设置为footerView
        if (position == 0)
        {
            return TYPE_TOP;
        }
        if (position + 1 == getItemCount())
        {
            return TYPE_FOOTER;

        } else
        {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        if (viewHolder instanceof ItemViewHolder)
        {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final SystemNoticeResponse.Notice notice = noticeList.get(position-1);
            holder.content.setText(notice.getContent());
            holder.time.setText(notice.getValidTime());
        }else if(viewHolder instanceof FooterViewHolder)
        {
            FooterViewHolder holder = (FooterViewHolder) viewHolder;
            if (noMoreData)
            {
                holder.pb.setVisibility(View.GONE);
                holder.text.setText("没有更多数据");
                setNoMoreData(false);
            } else
            {
                holder.pb.setVisibility(View.VISIBLE);
                holder.text.setText("加载中...");
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_TOP) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            topView.setLayoutParams(lp);
            return new HeaderViewHolder(topView);
        }else if (viewType == TYPE_ITEM)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.system_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ItemViewHolder(view);
        }
        // type == TYPE_FOOTER 返回footerView
        else if (viewType == TYPE_FOOTER)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footView.setLayoutParams(lp);
            return new FooterViewHolder(footView);
        }
        return null;
    }

    class ItemViewHolder extends ViewHolder
    {
        public ItemViewHolder(View view)
        {
            super(view);
            content = (TextView) view.findViewById(R.id.notice_content_item);
            time = (TextView) view.findViewById(R.id.notice_date_item);
        }

        public TextView content;
        public TextView time;

    }

    class FooterViewHolder extends ViewHolder
    {
        public FooterViewHolder(View view)
        {
            super(view);

            text = (TextView) view.findViewById(R.id.footer_loadmore_text);
            pb = (ProgressBar) view.findViewById(R.id.footer_load_progress);
        }

        public TextView text;

        public ProgressBar pb;
    }

    class HeaderViewHolder extends ViewHolder
    {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }


    public void setNoMoreData(boolean noMoreData)
    {
        this.noMoreData = noMoreData;
    }

    public boolean getNoMoreData()
    {
        return noMoreData;
    }

}


