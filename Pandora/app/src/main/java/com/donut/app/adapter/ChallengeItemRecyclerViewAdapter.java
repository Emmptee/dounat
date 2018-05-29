package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.ChallengeListResponse;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 */
public class ChallengeItemRecyclerViewAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener{
        void OnClick(String uuid);
    }

    private final List<ChallengeListResponse.Challenge> challengeList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private int orderType;

    public ChallengeItemRecyclerViewAdapter(List<ChallengeListResponse.Challenge> items,
                                            int type,
                                            OnItemClickListener listener,
                                            View footerView) {
        challengeList = items;
        orderType = type;
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
                .inflate(R.layout.fragment_challengeitem, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder) holder;
            final ChallengeListResponse.Challenge challenge = challengeList.get(position);

            Glide.with(item.ivPlaybill.getContext())
                    .load(challenge.getThumbnailUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(item.ivPlaybill);
            Glide.with(item.ivPlaybill.getContext())
                    .load(challenge.getHeadPic())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(item.ivPlaybill.getContext()))
                    .into(item.ivHeadPhoto);
            if(challenge.getIsMember()!=null&&challenge.getIsMember()==1){
                item.ivHeadVip.setVisibility(View.VISIBLE);
            }else{
                item.ivHeadVip.setVisibility(View.GONE);
            }

            ViewGroup.LayoutParams layoutParams = item.ivHeadBg.getLayoutParams();
            float scale = item.ivHeadBg.getResources().getDisplayMetrics().density;
            if (challenge.getType() == 1) {
                layoutParams.height = (int) (30 * scale + 0.5f);
                layoutParams.width = (int) (30 * scale + 0.5f);
                item.ivHeadBg.setImageDrawable(item.ivHeadBg.getResources().getDrawable(R.drawable.icon_star_bg));
            } else {
                layoutParams.height = (int) (25 * scale + 0.5f);
                layoutParams.width = (int) (25 * scale + 0.5f);
                item.ivHeadBg.setImageDrawable(item.ivHeadBg.getResources().getDrawable(R.drawable.shape_ring_round_main));
            }
            item.ivHeadBg.setLayoutParams(layoutParams);

            item.tvName.setText(CommonUtils.setName(item.itemView.getContext(),
                    challenge.getNickName()));
            item.tvContent.setText(challenge.getTitle());
            switch (orderType)
            {
                case 0:
                    item.tvTime.setText(challenge.getCreateTime());
                    break;
                case 1:
                    item.tvTime.setText(challenge.getBrowseTimes()+"次");
                    break;
                case 2:
                    item.tvTime.setText(challenge.getPraiseTimes()+"票");
                    break;
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnClick(challenge.getUuid());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return challengeList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == challengeList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivPlaybill;
        public final ImageView ivHeadPhoto,ivHeadVip, ivHeadBg;
        public final TextView tvName, tvTime;
        public final TextView tvContent;

        public ItemViewHolder(View view) {
            super(view);
            ivPlaybill = (ImageView) view.findViewById(R.id.challenge_item_iv_playbill);
            ivHeadPhoto = (ImageView) view.findViewById(R.id.challenge_item_iv_head);
            ivHeadVip = (ImageView) view.findViewById(R.id.challenge_item_iv_head_v);
            ivHeadBg = (ImageView) view.findViewById(R.id.challenge_item_iv_head_bg);
            tvName = (TextView) view.findViewById(R.id.challenge_item_tv_name);
            tvTime = (TextView) view.findViewById(R.id.challenge_item_tv_time);
            tvContent = (TextView) view.findViewById(R.id.challenge_item_tv_content);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder
    {
        public FooterViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
