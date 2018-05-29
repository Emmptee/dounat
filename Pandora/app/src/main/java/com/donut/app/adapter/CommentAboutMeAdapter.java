package com.donut.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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


public class CommentAboutMeAdapter extends RecyclerView.Adapter<ViewHolder>
{
    public static interface OnRecyclerViewListener
    {
        void onItemClick(String uuid,String contentid);

        void onFavor(int position);

        void onComment(int position, int subPos);

        void onRelate(ContentComments comment);
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

    private List<ContentComments> commentList;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;

    private boolean noMoreData;

    Context context;

    String currentUserId;

    public CommentAboutMeAdapter(Context context, List<ContentComments> contentData, View footView,String userId)
    {
        this.context = context;
        this.commentList = contentData;
        this.footView = footView;
        this.currentUserId=userId;
    }

    @Override
    public int getItemCount()
    {
        // 总条数+ 1（footerView）
        return commentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        // 最后一个item设置为footerView
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
            boolean isPraRelay = false, isComReply;
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final ContentComments comment = commentList.get(position);
            Glide.with(context)
                    .load(comment.getCommentatorUrl())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(context))
                    .into(holder.mHeader);
            float scale = context.getResources().getDisplayMetrics().density;
            int width1 = (int) (37 * scale + 0.5f);
            int width2 = (int) (46 * scale + 0.5f);
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
            param1.addRule(RelativeLayout.CENTER_IN_PARENT);
            param2.gravity= Gravity.CENTER;
            holder.mHeadBg.setLayoutParams(param2);
            holder.mHeader.setLayoutParams(param1);
            int width3 = (int) (40 / 3 * scale + 0.5f);
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
            holder.mName.setText(CommonUtils.setName(context,comment.getCommentatorName()));
            if (comment.getUserType() != null)
            {
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
            Glide.with(context)
                    .load(comment.getImgUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(holder.mRelaImg);
            if("7".equals(comment.getType())){
                holder.mRelaTitle.setText(comment.getUserName());
            } else {
                holder.mRelaTitle.setText(CommonUtils.setName(context, comment.getUserName()) + " | " + CommonUtils.setTitle(context, comment.getTitle()));
            }
            holder.mRelaContent.setText(comment.getDescription());
            List<SubComments> subList = new ArrayList<SubComments>();
            if(comment.getCommentNum()>0){

                holder.mCommentNum.setText(comment.getCommentNum()+"");
            }

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
                isPraRelay = false;
                holder.mFavorList.setVisibility(View.GONE);
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
                    String myColor="'#56C0E7'";
                    String replyColor,beReplyColor,contentColor;
                    boolean replyCur,beReplyCur;
                    if(reply.getBeRepliedUserId()!=null&&!"".equals(reply.getBeRepliedUserId())){
                        if(currentUserId!=null&&currentUserId.equals(reply.getBeRepliedUserId())){
                            beReplyCur=true;
                        }else{
                            beReplyCur=false;
                        }
                    }else{
                        if(currentUserId!=null&&currentUserId.equals(comment.getCommentatorId())){
                            beReplyCur=true;
                        }else{
                            beReplyCur=false;
                        }
                    }

                    if(currentUserId!=null&&currentUserId.equals(reply.getRepliedUserId())){
                        replyCur=true;
                    }else{
                        replyCur=false;
                    }
                    if(reply.getBeRepliedUserType()!=null&&!"".equals(reply.getBeRepliedUserType())){
                        if(Integer.parseInt(reply.getBeRepliedUserType())==0){
                            if(beReplyCur){
                                beReplyColor=myColor;
                            }else{
                                beReplyColor=commonColor;
                            }
                        }else{
                            beReplyColor=starColor;
                        }
                    }else{
                        if(beReplyCur){
                            beReplyColor=myColor;
                        }else{
                            beReplyColor=commonColor;
                        }
                    }

                    if(reply.getRepliedUserType()!=null&&!"".equals(reply.getRepliedUserType())){
                        if(Integer.parseInt(reply.getRepliedUserType())==0){
                            if(replyCur){
                                replyColor=myColor;
                            }else{
                                replyColor=commonColor;
                            }

                        }else{
                            replyColor=starColor;
                        }
                    }else{
                        if(replyCur){
                            replyColor=myColor;
                        }else{
                            replyColor=commonColor;
                        }
                    }

                    if(replyCur||beReplyCur){
                        contentColor=myColor;
                    }else{
                        contentColor="'#666666'";
                    }

                    if (reply.getBeRepliedUserId() != null&&!"".equals(reply.getBeRepliedUserId()))
                    {
                        content.setText(Html.fromHtml("<font color="+replyColor+">" + CommonUtils.setName(context, reply.getRepliedUserName()) + "</font>" +
                                "<font color='#666666'> 回复 </font><font color="+beReplyColor+">"
                                + CommonUtils.setName(context, reply.getBeRepliedUserName())
                                + "  :  </font><font color="+contentColor+">" + reply.getContent() + "</font>"));
                    } else
                    {
                        content.setText(Html.fromHtml("<font color="+replyColor+">"
                                + CommonUtils.setName(context,reply.getRepliedUserName())
                                + "  :  </font><font color="+contentColor+">" + reply.getContent() + "</font>"));
                    }
                    ll.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onRecyclerViewListener.onComment(position,subPos);
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
            holder.mFavorNum.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onFavor(position);
                }
            });
            holder.mCommentNum.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onComment(position, -1);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onItemClick(comment.getCommentId(),comment.getContentId());
                }
            });
            holder.mRelateLinear.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onRelate(comment);
                }
            });
        } else if (viewHolder instanceof FooterViewHolder)
        {
            FooterViewHolder hold = (FooterViewHolder) viewHolder;
            if (noMoreData)
            {
                hold.pb.setVisibility(View.GONE);
                hold.text.setText("没有更多数据");
                setNoMoreData(false);
            } else
            {
                hold.pb.setVisibility(View.VISIBLE);
                hold.text.setText("加载中...");
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
       if (viewType == TYPE_ITEM)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_comment_about_me, null);
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
            mHeadBg = (AutoRelativeLayout) view.findViewById(R.id.comment_item_header_bg);
            mHeader = (ImageView) view.findViewById(R.id.comment_item_header);
            mName = (TextView) view.findViewById(R.id.comment_item_name);
            mMoney = (TextView) view.findViewById(R.id.comment_item_money);
            mTime = (TextView) view.findViewById(R.id.comment_item_creattime);
            mContent = (TextView) view.findViewById(R.id.comment_item_content);
            mFavorNum = (TextView) view.findViewById(R.id.comment_item_favor_num);
            mCommentNum = (TextView) view.findViewById(R.id.comment_item_num);
            mFavorList = (TextView) view.findViewById(R.id.comment_item_favor_tv);
            mReplyLinear = (AutoLinearLayout) view.findViewById(R.id.comment_reply_linear);
            mALL = (TextView) view.findViewById(R.id.comment_item_all);
            mReplyZone = (AutoLinearLayout) view.findViewById(R.id.reply_zone);
            mAddr = (TextView) view.findViewById(R.id.comment_item_city);

            mRelateLinear=(AutoLinearLayout) view.findViewById(R.id.relate_linear);
            mRelaImg=(ImageView) view.findViewById(R.id.relate_img);
            mRelaTitle=(TextView) view.findViewById(R.id.rela_title);
            mRelaContent=(TextView) view.findViewById(R.id.rela_content);
            mHeaderVip = (ImageView) view.findViewById(R.id.comment_item_header_v);


        }

        public AutoRelativeLayout mHeadBg;
        public ImageView mHeader,mHeaderVip;
        public TextView mName, mMoney, mTime, mContent, mAddr;
        public TextView mFavorNum, mCommentNum;
        public TextView mFavorList;
        public AutoLinearLayout mReplyLinear, mReplyZone;
        public TextView mALL;

        public AutoLinearLayout mRelateLinear;
        public ImageView mRelaImg;
        public TextView mRelaTitle,mRelaContent;

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


    public void setNoMoreData(boolean noMoreData)
    {
        this.noMoreData = noMoreData;
    }

    public boolean getNoMoreData()
    {
        return noMoreData;
    }

}
