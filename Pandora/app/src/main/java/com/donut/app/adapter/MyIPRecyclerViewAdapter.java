package com.donut.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.IPDetailActivity;
import com.donut.app.activity.IpContentActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.message.MyIp;
import com.donut.app.http.message.StarListDetail;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 */
public class MyIPRecyclerViewAdapter extends RecyclerView.Adapter
{

    public interface OnItemClickListener
    {
        void OnClick(String uuid);

        void onDelete(int position);

        void onEdit(int position);
    }

    private final List<MyIp> ipList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private boolean noMoreData;

    private Context mContext;

    public MyIPRecyclerViewAdapter(Context context, List<MyIp> items,
                                   OnItemClickListener listener,
                                   View footerView)
    {
        ipList = items;
        mListener = listener;
        this.mContext = context;
        this.footerView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        if (viewType == BOTTOM_TYPE)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(lp);
            return new FooterViewHolder(footerView);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_ip_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {

        if (holder instanceof ItemViewHolder)
        {
            ItemViewHolder item = (ItemViewHolder) holder;
            final MyIp ip = ipList.get(position);
            Glide.with(mContext)
                    .load(ip.getThumbnailUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(item.frontImg);
            item.tvName.setText(ip.getName());
            item.tvContent.setText(ip.getDescription());
            item.tvTime.setText(ip.getCreateTime());
            if(ip.getStatus()!=null){
                item.tvReviewState.setText(CommonUtils.getStatus(ip.getStatus()));
                switch (ip.getStatus()){
                    case 0:
                        item.tvEdit.setVisibility(View.GONE);
                        item.tvRefused.setVisibility(View.GONE);
                        item.tvReviewState.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        item.tvEdit.setVisibility(View.VISIBLE);
                        item.tvRefused.setVisibility(View.VISIBLE);
                        item.tvRefused.setText(ip.getApproveDesc());
                        item.tvReviewState.setTextColor(mContext.getResources().getColor(R.color.text_gray9));
                        break;
                    case 2:
                        item.tvEdit.setVisibility(View.VISIBLE);
                        item.tvRefused.setVisibility(View.GONE);
                        item.tvReviewState.setTextColor(mContext.getResources().getColor(R.color.text_gray9));
                        break;
                }

            }
            if(ip.getStarList()!=null&&ip.getStarList().size()>0){
                showStarList(item,ip.getStarList());
            }

            item.tvRule.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SaveBehaviourDataService.startAction(mContext, BehaviourEnum.MY_IP.getCode()+"02");
                    Intent intent=new Intent(mContext,IpContentActivity.class);
                    intent.putExtra(IpContentActivity.IP_ID, ip.getIpId());
                    mContext.startActivity(intent);
                }
            });

            item.tvEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mListener.onEdit(position);
                }
            });

            item.frontImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(ip.getStatus()!=null){
                        if(ip.getStatus()!=2){
                            Intent intent=new Intent(mContext, VideoActivity.class);
                            intent.putExtra(VideoActivity.VIDEONAME,ip.getName());
                            intent.putExtra(VideoActivity.VIDEOURL,ip.getPlayUrl());
                            mContext.startActivity(intent);
                        }else{
                            Intent intent=new Intent(mContext, IPDetailActivity.class);
                            intent.putExtra(IPDetailActivity.IPID,ip.getIpId());
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
            item.tvDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (null != mListener)
                    {
                        mListener.onDelete(position);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (null != mListener)
                    {
                        if(ip.getStatus()!=null){
                            if(ip.getStatus()==2){
                                Intent intent=new Intent(mContext, IPDetailActivity.class);
                                intent.putExtra(IPDetailActivity.IPID,ip.getIpId());
                                mContext.startActivity(intent);
                            }
                        }
                    }
                }
            });
        } else if (holder instanceof FooterViewHolder)
        {
            FooterViewHolder hold = (FooterViewHolder) holder;
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

    private void showStarList(ItemViewHolder holder, List<StarListDetail> starList)
    {
        holder.inviteLinear.removeAllViews();
        for (int i = 0; i < starList.size(); i++)
        {
            String url = starList.get(i).getHeadPic();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            LinearLayout ll = (LinearLayout) inflater
                    .inflate(R.layout.item_star, null);
            ImageView header = (ImageView) ll.findViewById(R.id.item_star_img);
            FrameLayout layout=(FrameLayout)ll.findViewById(R.id.frame_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i!=0){
                params.setMargins(15, 0, 0, 0);
            }
            ll.setLayoutParams(params);
            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(header);
            holder.inviteLinear.addView(ll);
        }
    }

    @Override
    public int getItemCount()
    {
        return ipList.size() + 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == ipList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        public final ImageView frontImg;
        public final TextView tvName, tvTime, tvDelete, tvRule;
        public final TextView tvContent;
        public final TextView tvReviewState, tvEdit, tvRefused;
        public final LinearLayout inviteLinear;

        public ItemViewHolder(View view)
        {
            super(view);
            frontImg = (ImageView) view.findViewById(R.id.my_ip_item_img);
            tvDelete = (TextView) view.findViewById(R.id.delete_btn);
            tvName = (TextView) view.findViewById(R.id.my_ip_name);
            tvTime = (TextView) view.findViewById(R.id.my_ip_time);
            tvRule = (TextView) view.findViewById(R.id.my_ip_rule);
            tvContent = (TextView) view.findViewById(R.id.my_ip_content);
            inviteLinear = (LinearLayout) view.findViewById(R.id.my_ip_star_invite);
            tvReviewState = (TextView) view.findViewById(R.id.review_status);
            tvEdit = (TextView) view.findViewById(R.id.my_ip_edit);
            tvRefused = (TextView) view.findViewById(R.id.refused_status);

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder
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
