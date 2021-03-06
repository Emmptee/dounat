package com.donut.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
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
import com.donut.app.http.message.SubComments;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by wujiaojiao on 2016/7/20.
 */
public class ReplyListAdapter extends RecyclerView.Adapter<ViewHolder>
{

    public static interface OnRecyclerViewListener
    {
        void onItemClick(int position);
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

    private List<SubComments> replyList;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;

    private static final int TYPE_TOP = 2;

    private boolean noMoreData;

    private Context mContext;

    private String currentUserId;

    private String mainCommentorId;

    public ReplyListAdapter(Context context, List<SubComments> contentData, View topView, View footView,String userId)
    {
        this.mContext = context;
        this.replyList = contentData;
        this.footView = footView;
        this.topView=topView;
        this.currentUserId=userId;
    }

    @Override
    public int getItemCount()
    {
        // 总条数+ 1（footerView）
        return replyList.size() + 2;
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
            final SubComments comment = replyList.get(position-1);
            Glide.with(mContext)
                    .load(comment.getRepliedUserImg())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.mHeader);
            holder.mName.setText(CommonUtils.setName(mContext,comment.getRepliedUserName()));
            float scale = mContext.getResources().getDisplayMetrics().density;
            int width1 = (int) (37* scale + 0.5f);
            int width2 = (int) (45 * scale + 0.5f);
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
            param1.addRule(RelativeLayout.CENTER_IN_PARENT);
            //param2.gravity= Gravity.CENTER;
            holder.mHeadBg.setLayoutParams(param2);
            holder.mHeader.setLayoutParams(param1);
            int width3 = (int) (45 / 3 * scale + 0.5f);
            RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(width3, width3);
            param3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            param3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param3.setMargins(0,0,5,3);
            holder.mHeaderVip.setLayoutParams(param3);
            if(comment.getIsMember()!=null&&comment.getIsMember()==1){
                holder.mHeaderVip.setVisibility(View.VISIBLE);
            }else{
                holder.mHeaderVip.setVisibility(View.GONE);
            }
            String myColor="'#56C0E7'";
            boolean replyCur,beReplyCur;
            if(comment.getBeRepliedUserId()!=null&&!"".equals(comment.getBeRepliedUserId())){
                if(currentUserId!=null&&currentUserId.equals(comment.getBeRepliedUserId())){
                    beReplyCur=true;
                }else{
                    beReplyCur=false;
                }
            }else{
                if(currentUserId!=null&&currentUserId.equals(mainCommentorId)){
                    beReplyCur=true;
                }else{
                    beReplyCur=false;
                }
            }

            if(currentUserId!=null&&currentUserId.equals(comment.getRepliedUserId())){
                replyCur=true;
            }else{
                replyCur=false;
            }
            if(comment.getRepliedUserType()!=null){
                if (Integer.parseInt(comment.getRepliedUserType()) == 1)
                {
                    holder.mHeadBg.setBackgroundResource(R.drawable.icon_star_bg);
                    holder.mName.setTextColor(mContext.getResources().getColor(R.color.gold));
                    holder.mHeadBg.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent=new Intent(mContext,StarDetailActivity.class);
                            intent.putExtra(StarDetailActivity.FKA01_ID,comment.getRepliedUserId());
                            mContext.startActivity(intent);
                        }
                    });
                } else
                {
                    holder.mHeadBg.setBackgroundResource(R.drawable.icon_head_bg);
                    if(replyCur){
                        holder.mName.setTextColor(mContext.getResources().getColor(R.color.light_blue));
                    }else{
                        holder.mName.setTextColor(mContext.getResources().getColor(R.color.text_gray6));
                    }

                }
            }
            holder.mMoney.setVisibility(View.GONE);
            holder.mAddr.setVisibility(View.GONE);
            holder.mTime.setText(comment.getCreateTime());
            String commonColor="'#81d8d0'";
            String starColor="'#ffb432'";
            String beReplyColor,contentColor;
            if(comment.getBeRepliedUserType()!=null&&!"".equals(comment.getBeRepliedUserType())&&Integer.parseInt(comment.getBeRepliedUserType())==1){
                beReplyColor=starColor;
            }else{
                if(beReplyCur){
                    beReplyColor=myColor;
                }else{
                    beReplyColor=commonColor;
                }
            }

            if(replyCur||beReplyCur){
                contentColor=myColor;
            }else{
                contentColor="'#666666'";
            }
            if (comment.getBeRepliedUserId() != null&&!"".equals(comment.getBeRepliedUserId()))
            {
                holder.mContent.setText(Html.fromHtml(
                        "<font color='#666666'> 回复 </font><font color="+beReplyColor+">"
                        + CommonUtils.setName(mContext,comment.getBeRepliedUserName())
                        + "  :  </font><font color="+contentColor+">" + comment.getContent() + "</font>"));
            } else
            {
                holder.mContent.setText(Html.fromHtml("<font color="+contentColor+">" + comment.getContent() + "</font>"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onItemClick(position-1);
                }
            });

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
                    R.layout.inner_item_comment, null);
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
            mHeadBg = (AutoRelativeLayout)view.findViewById(R.id.inner_comment_item_header_bg);
            mHeader = (ImageView) view.findViewById(R.id.inner_comment_item_header);
            mName = (TextView) view.findViewById(R.id.inner_comment_item_name);
            mMoney = (TextView) view.findViewById(R.id.inner_comment_item_money);
            mTime = (TextView) view.findViewById(R.id.inner_comment_item_creattime);
            mContent = (TextView) view.findViewById(R.id.inner_comment_item_content);
            mLine=(TextView) view.findViewById(R.id.inner_item_line);
            mHeaderVip= (ImageView) view.findViewById(R.id.inner_comment_item_header_v);
            mAddr=(TextView) view.findViewById(R.id.inner_comment_item_addr);
        }
        public AutoRelativeLayout mHeadBg;
        public ImageView mHeader,mHeaderVip;
        public TextView mName,mMoney, mTime, mContent;
        public TextView mLine,mAddr;

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

    public String getMainCommentorId()
    {
        return mainCommentorId;
    }

    public void setMainCommentorId(String mainCommentorId)
    {
        this.mainCommentorId = mainCommentorId;
    }
}


