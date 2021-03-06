package com.donut.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.http.message.ContentComments;
import com.donut.app.http.message.SubComments;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

public class HotCommentAdapter extends BaseAdapter
{

    public static interface OnRecyclerViewListener
    {
        void onHotItemClick(String uuid);

        void onHotFavor(int position);

        void onHotComment(int position,int subPos);
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

    private Context context;

    private List<ContentComments> commentList;


    public HotCommentAdapter(final Context context, List<ContentComments> list)
    {
        this.context = context;
        this.commentList = list;
    }

    @Override
    public int getCount()
    {
        return commentList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if ((convertView != null) && (convertView.getTag() != null))
        {
            Object tag = convertView.getTag();
            if (tag instanceof ViewHolder)
            {
                holder = (ViewHolder) tag;
            }
        }

        if (holder == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_comment, null);
            holder.mHeadBg = (AutoRelativeLayout) convertView.findViewById(R.id.comment_item_header_bg);
            holder.mHeader = (ImageView) convertView.findViewById(R.id.comment_item_header);
            holder.mName = (TextView) convertView.findViewById(R.id.comment_item_name);
            holder.mMoney = (TextView) convertView.findViewById(R.id.comment_item_money);
            holder.mTime = (TextView) convertView.findViewById(R.id.comment_item_creattime);
            holder.mContent = (TextView) convertView.findViewById(R.id.comment_item_content);
            holder.mFavorNum = (TextView) convertView.findViewById(R.id.comment_item_favor_num);
            holder.mCommentNum = (TextView) convertView.findViewById(R.id.comment_item_num);
            holder.mFavorList = (TextView) convertView.findViewById(R.id.comment_item_favor_tv);
            holder.mReplyLinear = (AutoLinearLayout) convertView.findViewById(R.id.comment_reply_linear);
            holder.mALL = (TextView) convertView.findViewById(R.id.comment_item_all);
            holder.mReplyZone = (AutoLinearLayout) convertView.findViewById(R.id.reply_zone);
            holder.mAddr = (TextView) convertView.findViewById(R.id.comment_item_city);
            holder.mLine=(TextView) convertView.findViewById(R.id.item_line);
            holder.mMidLinear=(AutoLinearLayout) convertView.findViewById(R.id.comment_item_mid_linear);
            holder.mHeaderVip= (ImageView)convertView.findViewById(R.id.comment_item_header_vip);
            convertView.setTag(holder);
        }
        boolean isPraRelay = false, isComReply = false;
        final ContentComments comment = commentList.get(position);
        Glide.with(context)
                .load(comment.getCommentatorUrl())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(context))
                .into(holder.mHeader);
        holder.mName.setText(CommonUtils.setName(context,comment.getCommentatorName()));
        float scale = context.getResources().getDisplayMetrics().density;
        int width1 = (int) (37* scale + 0.5f);
        int width2 = (int) (46 * scale + 0.5f);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
        param1.addRule(RelativeLayout.CENTER_IN_PARENT);
        param2.gravity= Gravity.CENTER;
        holder.mHeadBg.setLayoutParams(param2);
        holder.mHeader.setLayoutParams(param1);
        int width3 = (int) (46 / 3 * scale + 0.5f);
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
        if(comment.getUserType()!=null){
            if (Integer.parseInt(comment.getUserType()) == 1)
            {
                holder.mHeadBg.setBackgroundResource(R.drawable.icon_star_bg);
                holder.mName.setTextColor(context.getResources().getColor(R.color.gold));
                holder.mHeadBg.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent(context,StarDetailActivity.class);
                        intent.putExtra(StarDetailActivity.FKA01_ID,comment.getCommentatorId());
                        context.startActivity(intent);
                    }
                });
            } else
            {
                holder.mHeadBg.setBackgroundResource(R.drawable.icon_head_bg);
                holder.mName.setTextColor(context.getResources().getColor(R.color.text_gray6));
                holder.mHeadBg.setOnClickListener(null);
            }
        }
        if (comment.getRewardNum() != null)
        {
            holder.mMoney.setVisibility(View.VISIBLE);
            holder.mMoney.setText("x" + comment.getRewardNum());
        } else
        {
            holder.mMoney.setVisibility(View.GONE);
        }

        holder.mTime.setText(comment.getUpdateTime());
        holder.mContent.setText(comment.getContent());
        Drawable drawable=null;
        if(comment.getIsPraised()!=null){
            if(Integer.parseInt(comment.getIsPraised())==0){
                //赞过
                drawable = context.getResources().getDrawable(R.drawable.icon_comment_favor_main);
                holder.mFavorNum.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else{
                drawable = context.getResources().getDrawable(R.drawable.icon_comment_favor_gray);
                holder.mFavorNum.setTextColor(context.getResources().getColor(R.color.text_gray6));
            }
        }else{
            drawable = context.getResources().getDrawable(R.drawable.icon_comment_favor_gray);
            holder.mFavorNum.setTextColor(context.getResources().getColor(R.color.text_gray6));
        }
        holder.mFavorNum.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        if(comment.getPraiseList()!=null&&comment.getPraiseList().size()>0){
            holder.mFavorNum.setText(comment.getPraiseList().size() + "");
        }else{
            holder.mFavorNum.setText("");
        }
        holder.mAddr.setText(comment.getAddress());
        Drawable draw=null;
        if(comment.getIsReplied()!=null){
            if(Integer.parseInt(comment.getIsReplied())==0){
                //回复过
                draw = context.getResources().getDrawable(R.drawable.icon_comment_green);
                holder.mCommentNum.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else{
                draw = context.getResources().getDrawable(R.drawable.icon_comment_gray);
                holder.mCommentNum.setTextColor(context.getResources().getColor(R.color.text_gray6));
            }
        }else{
            draw = context.getResources().getDrawable(R.drawable.icon_comment_gray);
            holder.mCommentNum.setTextColor(context.getResources().getColor(R.color.text_gray6));
        }
        holder.mCommentNum.setCompoundDrawablesWithIntrinsicBounds(null,null,draw,null);
        if(comment.getCommentNum()>0){
            holder.mCommentNum.setText(comment.getCommentNum()+"");
        }else{
            holder.mCommentNum.setText("");
        }
        List<SubComments> subList = new ArrayList<SubComments>();
        if (comment.getCommentNum() > 5)
        {
            holder.mALL.setVisibility(View.VISIBLE);
            holder.mALL.setText("查看全部" + comment.getCommentNum() + "条评论");
            for (int i = 0; i < 5; i++)
            {
                subList.add(comment.getSubCommentsList().get(i));
            }
        } else
        {
            holder.mALL.setVisibility(View.GONE);
            subList.addAll(comment.getSubCommentsList());
        }
        if (comment.getPraiseList() != null && comment.getPraiseList().size() > 0)
        {
            holder.mFavorList.setVisibility(View.VISIBLE);
            isPraRelay = true;
            int praiseNum = comment.getPraiseList().size();
            StringBuffer buffer = new StringBuffer();
            if (praiseNum <= 3)
            {
                for (int i = 0; i < praiseNum - 1; i++)
                {
                    buffer.append(CommonUtils.setName(context,comment.getPraiseList().get(i).getPraisedUserName()) + "、");
                }
                buffer.append(CommonUtils.setName(context,comment.getPraiseList().get(praiseNum - 1).getPraisedUserName()));
                holder.mFavorList.setText(Html.fromHtml("<font color='#81d8d0'>" + buffer.toString()
                        + "</font>"));
            } else
            {
                for (int i = 0; i < 2; i++)
                {
                    buffer.append(CommonUtils.setName(context,comment.getPraiseList().get(i).getPraisedUserName()) + "、");
                }
                buffer.append(CommonUtils.setName(context,comment.getPraiseList().get(2).getPraisedUserName()));
                holder.mFavorList.setText(Html.fromHtml("<font color='#81d8d0'>" + buffer.toString()
                        + "</font>" + "<font color='#666666'>" + "等" + praiseNum + "人" + "</font>"));
            }
        } else
        {
            holder.mFavorList.setVisibility(View.GONE);
            isPraRelay = false;
        }

        if (subList != null && subList.size() > 0)
        {
            isComReply = true;
            holder.mReplyLinear.setVisibility(View.VISIBLE);
            holder.mReplyLinear.removeAllViews();
            for (int i = 0; i < subList.size(); i++)
            {
                final int subPos=i;
                final SubComments reply = subList.get(i);
                LayoutInflater inflater = LayoutInflater.from(context);
                LinearLayout ll = (LinearLayout) inflater
                        .inflate(R.layout.comment_reply_layout, null);
                TextView content = (TextView) ll.findViewById(R.id.comment_item_reply);
                String commonColor="'#81d8d0'";
                String starColor="'#ffb432'";
                String replyColor,beReplyColor;
                if(reply.getBeRepliedUserType()!=null&&!"".equals(reply.getBeRepliedUserType())){
                    if(Integer.parseInt(reply.getBeRepliedUserType())==0){
                        beReplyColor=commonColor;
                    }else{
                        beReplyColor=starColor;
                    }
                }else{
                    beReplyColor=commonColor;
                }

                if(reply.getRepliedUserType()!=null&&!"".equals(reply.getRepliedUserType())){
                    if(Integer.parseInt(reply.getRepliedUserType())==0){
                        replyColor=commonColor;
                    }else{
                        replyColor=starColor;
                    }
                }else{
                    replyColor=commonColor;
                }
                if(reply.getBeRepliedUserId()!=null&&!"".equals(reply.getBeRepliedUserId())){
                    content.setText(Html.fromHtml("<font color="+replyColor+">" + CommonUtils.setName(context,reply.getRepliedUserName()) + "</font>" +
                            "<font color='#666666'> 回复 </font><font color="+beReplyColor+">"
                            + CommonUtils.setName(context,reply.getBeRepliedUserName())
                            + "  :  </font><font color='#666666'>" + reply.getContent() + "</font>"));
                }else{
                    content.setText(Html.fromHtml("<font color="+replyColor+">"
                            + CommonUtils.setName(context,reply.getRepliedUserName())
                            + "  :  </font><font color='#666666'>" + CommonUtils.setName(context,reply.getContent()) + "</font>"));
                }

                ll.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        onRecyclerViewListener.onHotComment(position,subPos);
                    }
                });
                holder.mReplyLinear.addView(ll);
            }
        } else
        {
            holder.mReplyLinear.setVisibility(View.GONE);
            isComReply = false;
        }
        if (isPraRelay || isComReply)
        {
            holder.mReplyZone.setVisibility(View.VISIBLE);
        } else
        {
            holder.mReplyZone.setVisibility(View.GONE);
        }

        if(position==commentList.size()-1){
            holder.mLine.setVisibility(View.GONE);
        }else{
            holder.mLine.setVisibility(View.VISIBLE);
        }

        holder.mFavorNum.setOnClickListener(new View.OnClickListener()
        {
            long lastClick;
            @Override
            public void onClick(View v)
            {
                if (System.currentTimeMillis() - lastClick <= 1000) {
                    return;
                }
                lastClick = System.currentTimeMillis();
                onRecyclerViewListener.onHotFavor(position);
            }
        });
        holder.mCommentNum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onRecyclerViewListener.onHotComment(position,-1);
            }
        });
        holder.mMidLinear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onRecyclerViewListener.onHotItemClick(comment.getCommentId());
            }
        });

        return convertView;
    }

    public final class ViewHolder
    {
        public AutoRelativeLayout mHeadBg;
        public ImageView mHeader,mHeaderVip;
        public TextView mName, mMoney, mTime, mContent, mAddr;
        public TextView mFavorNum, mCommentNum;
        public TextView mFavorList;
        public AutoLinearLayout mReplyLinear, mReplyZone;
        public TextView mALL;
        public TextView mLine;
        public AutoLinearLayout mMidLinear;
    }

}
