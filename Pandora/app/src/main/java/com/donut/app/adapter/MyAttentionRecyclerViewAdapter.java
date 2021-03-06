package com.donut.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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
import com.donut.app.http.message.MyFollowListResponse;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 */
public class MyAttentionRecyclerViewAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener{
        void OnItemClick(int position);
        void onCancle(int position);
    }

    private final List<MyFollowListResponse.FollowDetail> mList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private boolean noMoreData;

    Context context;


    public MyAttentionRecyclerViewAdapter(Context context, List<MyFollowListResponse.FollowDetail> items,
                                          OnItemClickListener listener,
                                          View footerView) {
        this.context=context;
        mList = items;
        mListener = listener;
        this.footerView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == BOTTOM_TYPE)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(lp);
            return new FooterViewHolder(footerView);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attention, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder) holder;
            final MyFollowListResponse.FollowDetail detail = mList.get(position);
            float scale = context.getResources().getDisplayMetrics().density;
            int width1 = (int) (40 * scale + 0.5f);
            int width2 = (int) (50 * scale + 0.5f);
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
            param1.addRule(RelativeLayout.CENTER_IN_PARENT);
            param2.gravity= Gravity.CENTER;
            item.mHeaderBg.setLayoutParams(param2);
            item.mHeader.setLayoutParams(param1);
            int width3 = (int) (40 / 3 * scale + 0.5f);
            RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(width3, width3);
            param3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            param3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param3.setMargins(0,0,5,3);
            item.mHeaderVip.setLayoutParams(param3);
            if(detail.getMemberStatus()!=null&&Integer.parseInt(detail.getMemberStatus())==1){
                item.mHeaderVip.setVisibility(View.VISIBLE);
            }else{
                item.mHeaderVip.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(detail.getHeadPic())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(context))
                    .into(item.mHeader);
            item.tvName.setText(CommonUtils.setTitle(context,detail.getName()));
            item.tvContent.setText(detail.getLastStarMoment());
            Drawable drawable;
            if(detail.getStatus()==0){
                drawable = context.getResources().getDrawable(R.drawable.icon_attention);
                item.tvStatus.setTextColor(context.getResources().getColor(R.color.text_gray6));
                item.tvStatus.setText("已关注");
            }else{
                drawable = context.getResources().getDrawable(R.drawable.icon_attention_add);
                item.tvStatus.setTextColor(context.getResources().getColor(R.color.gold));
                item.tvStatus.setText("加关注");
            }
            item.tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, drawable,null,null);
            item.tvStatus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (null != mListener) {
                        mListener.onCancle(position);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnItemClick(position);
                    }
                }
            });
        }else if(holder instanceof FooterViewHolder)
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

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public final AutoRelativeLayout mHeaderBg;
        public final ImageView mHeader,mHeaderVip;
        public final TextView tvName, tvContent,tvStatus;

        public ItemViewHolder(View view) {
            super(view);
            mHeaderBg=(AutoRelativeLayout) view.findViewById(R.id.attention_item_header_bg);
            mHeader = (ImageView) view.findViewById(R.id.attention_item_header);
            tvName = (TextView) view.findViewById(R.id.attention_name);
            tvContent = (TextView) view.findViewById(R.id.attention_content);
            tvStatus = (TextView) view.findViewById(R.id.attention_status);
            mHeaderVip = (ImageView) view.findViewById(R.id.attention_item_header_v);
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
