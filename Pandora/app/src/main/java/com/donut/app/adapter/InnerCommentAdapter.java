package com.donut.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.ContentComments;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

public class InnerCommentAdapter extends BaseAdapter
{
    private Context context;
    private List<ContentComments> commentList;


    public InnerCommentAdapter(final Context context, List<ContentComments> list)
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
                    R.layout.inner_item_comment, null);
            holder.mHeadBg = (AutoRelativeLayout) convertView.findViewById(R.id.inner_comment_item_header_bg);
            holder.mHeaderVip = (ImageView) convertView.findViewById(R.id.inner_comment_item_header_v);
            holder.mHeader = (ImageView) convertView.findViewById(R.id.inner_comment_item_header);
            holder.mName = (TextView) convertView.findViewById(R.id.inner_comment_item_name);
            holder.mMoney = (TextView) convertView.findViewById(R.id.inner_comment_item_money);
            holder.mTime = (TextView) convertView.findViewById(R.id.inner_comment_item_creattime);
            holder.mContent = (TextView) convertView.findViewById(R.id.inner_comment_item_content);
            holder.mLine=(TextView) convertView.findViewById(R.id.inner_item_line);
            holder.mAddr=(TextView) convertView.findViewById(R.id.inner_comment_item_addr);
            convertView.setTag(holder);
        }
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
        int width2 = (int) (45 * scale + 0.5f);
        int width3 = (int) (37/3 * scale + 0.5f);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
        RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(width3, width3);
        param1.addRule(RelativeLayout.CENTER_IN_PARENT);
        //param2.gravity= Gravity.CENTER;
        param3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param3.setMargins(0,0,5,5);
        holder.mHeaderVip.setLayoutParams(param3);
        holder.mHeadBg.setLayoutParams(param2);
        holder.mHeader.setLayoutParams(param1);
        holder.mAddr.setText(comment.getAddress());
        if(comment.getUserType()!=null){
            if (Integer.parseInt(comment.getUserType()) == 1)
            {
                holder.mHeadBg.setBackgroundResource(R.drawable.icon_star_bg);
                holder.mName.setTextColor(context.getResources().getColor(R.color.gold));
            } else
            {
                holder.mHeadBg.setBackgroundResource(R.drawable.icon_head_bg);
                holder.mName.setTextColor(context.getResources().getColor(R.color.text_gray6));
            }
        }
        if(comment.getIsMember()!=null&&comment.getIsMember()==1){
            holder.mHeaderVip.setVisibility(View.VISIBLE);
        }else{
            holder.mHeaderVip.setVisibility(View.GONE);
        }
        if (comment.getRewardNum() != null)
        {
            holder.mMoney.setVisibility(View.VISIBLE);
            holder.mMoney.setText("x" + comment.getRewardNum());
        } else
        {
            holder.mMoney.setVisibility(View.GONE);
        }

        holder.mTime.setText(comment.getCreateTime());
        holder.mContent.setText(comment.getContent());

        if(position==commentList.size()-1){
            holder.mLine.setVisibility(View.GONE);
        }else{
            holder.mLine.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public final class ViewHolder
    {
        public AutoRelativeLayout mHeadBg;
        public ImageView mHeader,mHeaderVip;
        public TextView mName, mMoney, mTime, mContent,mAddr;
        public TextView mLine;
    }

}
