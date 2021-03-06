package com.donut.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.http.message.CommentPraise;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by wujiaojiao on 2016/7/20.
 */
public class PraiseListAdapter extends RecyclerView.Adapter<ViewHolder>
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

    private List<CommentPraise> praiseList;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;

    private static final int TYPE_TOP = 2;

    private boolean noMoreData;

    private Context mContext;

    public PraiseListAdapter(Context context, List<CommentPraise> contentData, View topView, View footView)
    {
        this.mContext = context;
        this.praiseList = contentData;
        this.footView = footView;
        this.topView=topView;
    }

    @Override
    public int getItemCount()
    {
        // 总条数+ 1（footerView）
        return praiseList.size() + 2;
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
            final CommentPraise item = praiseList.get(position-1);
            Glide.with(mContext)
                    .load(item.getPraisedUserUrl())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.mHeader);
            float scale = mContext.getResources().getDisplayMetrics().density;
            int width1 = (int) (37 * scale + 0.5f);
            int width2 = (int) (45 * scale + 0.5f);
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
            param1.addRule(RelativeLayout.CENTER_IN_PARENT);
            param2.gravity= Gravity.CENTER;
            holder.mHeadBg.setLayoutParams(param2);
            holder.mHeader.setLayoutParams(param1);
            int width3 = (int) (45 / 3 * scale + 0.5f);
            RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(width3, width3);
            param3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            param3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param3.setMargins(0,0,5,3);
            holder.mHeaderVip.setLayoutParams(param3);
            if(item.getIsMember()!=null&&item.getIsMember()==1){
                holder.mHeaderVip.setVisibility(View.VISIBLE);
            }else{
                holder.mHeaderVip.setVisibility(View.GONE);
            }
            holder.name.setText(CommonUtils.setName(mContext,item.getPraisedUserName()));
            if (item.getPraisedUserType() != null)
            {
                if (Integer.parseInt(item.getPraisedUserType()) == 1)
                {
                    holder.mHeadBg.setBackgroundResource(R.drawable.icon_star_bg);
                    holder.name.setTextColor(mContext.getResources().getColor(R.color.gold));
                    holder.mHeadBg.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent=new Intent(mContext,StarDetailActivity.class);
                            intent.putExtra(StarDetailActivity.FKA01_ID,item.getPraisedUserId());
                            mContext.startActivity(intent);
                        }
                    });

                } else
                {
                    holder.mHeadBg.setBackgroundResource(R.drawable.icon_head_bg);
                    holder.name.setTextColor(mContext.getResources().getColor(R.color.text_gray6));
                }
            }
            holder.time.setText(item.getCreateTime());
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
                    R.layout.item_detail_praise, null);
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
            mHeadBg=(AutoRelativeLayout) view.findViewById(R.id.praise_header_bg);
            mHeader=(ImageView) view.findViewById(R.id.praise_header);
            name = (TextView) view.findViewById(R.id.praise_name);
            time = (TextView) view.findViewById(R.id.praise_time);
            mHeaderVip=(ImageView) view.findViewById(R.id.praise_header_vip);
        }
        public AutoRelativeLayout mHeadBg;
        public ImageView mHeader,mHeaderVip;
        public TextView name;
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


