package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.IPListResponse;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 */
public class IPTopItemRecyclerViewAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener{
        void OnClick(String uuid);
    }

    private final List<IPListResponse.IpListDetail> ipList;

    private OnItemClickListener mListener;

    private static final int ITEM_TYPE = 2;

    private Context mContext;

    public IPTopItemRecyclerViewAdapter(Context context, List<IPListResponse.IpListDetail> items,
                                        OnItemClickListener listener) {
        this.mContext=context;
        ipList = items;
        mListener = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_ip_item, parent, false);
            return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder) holder;
            final IPListResponse.IpListDetail detail = ipList.get(position);

            Glide.with(mContext)
                    .load(detail.getImgUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(item.ivPlaybill);
            Glide.with(mContext)
                    .load(detail.getHeadPic())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(item.ivHeadPhoto);

            item.tvName.setText(CommonUtils.setName(mContext,detail.getStarName()));

                    item.tvTime.setText(detail.getCreateTime());

            item.tvContent.setText(CommonUtils.setTitle(mContext,detail.getName()));

            if(detail.getMemberStatus()!=null&&detail.getMemberStatus()==1){
                item.ivHeadVip.setVisibility(View.VISIBLE);
            }else{
                item.ivHeadVip.setVisibility(View.GONE);
            }
            ViewGroup.LayoutParams layoutParams = item.ivHeadBg.getLayoutParams();
            float scale = item.ivHeadBg.getResources().getDisplayMetrics().density;
            if (detail.getType() == 1) {
                layoutParams.height = (int) (30 * scale + 0.5f);
                layoutParams.width = (int) (30 * scale + 0.5f);
                item.ivHeadBg.setImageDrawable(item.ivHeadBg.getResources().getDrawable(R.drawable.icon_star_bg));
            } else {
                layoutParams.height = (int) (25 * scale + 0.5f);
                layoutParams.width = (int) (25 * scale + 0.5f);
                item.ivHeadBg.setImageDrawable(item.ivHeadBg.getResources().getDrawable(R.drawable.shape_ring_round_main));
            }
            item.ivHeadBg.setLayoutParams(layoutParams);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnClick(detail.getIpId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ipList.size();
    }

    @Override
    public int getItemViewType(int position) {

            return ITEM_TYPE;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivPlaybill;
        public final ImageView ivHeadPhoto;
        public final TextView tvName, tvTime;
        public final TextView tvContent;
        public final ImageView ivHeadVip, ivHeadBg;

        public ItemViewHolder(View view) {
            super(view);
            ivPlaybill = (ImageView) view.findViewById(R.id.ip_item_iv_playbill);
            ivHeadPhoto = (ImageView) view.findViewById(R.id.ip_item_iv_head);
            tvName = (TextView) view.findViewById(R.id.ip_item_tv_name);
            tvTime = (TextView) view.findViewById(R.id.ip_item_tv_time);
            tvContent = (TextView) view.findViewById(R.id.ip_item_tv_content);
            ivHeadVip = (ImageView) view.findViewById(R.id.ip_item_iv_head_vip);
            ivHeadBg = (ImageView) view.findViewById(R.id.ip_item_iv_head_bg);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder
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


}
