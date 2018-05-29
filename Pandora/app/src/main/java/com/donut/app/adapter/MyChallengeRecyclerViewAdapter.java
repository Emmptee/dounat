package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.MyChallengeResponse;
import com.donut.app.utils.CommonUtils;

import java.util.List;

/**
 */
public class MyChallengeRecyclerViewAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener{
        void OnClick(int position);
        void onDelete(int position);
    }

    private final List<MyChallengeResponse.MyChallenge> challengeList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private boolean noMoreData;

    Context context;


    public MyChallengeRecyclerViewAdapter(Context context, List<MyChallengeResponse.MyChallenge> items,
                                          OnItemClickListener listener,
                                          View footerView) {
        this.context=context;
        challengeList = items;
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
                .inflate(R.layout.my_challenge_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder) holder;
            final MyChallengeResponse.MyChallenge challenge = challengeList.get(position);

            Glide.with(context)
                    .load(challenge.getThumbnailUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(item.frontImg);

            item.tvName.setText(CommonUtils.setTitle(context,challenge.getTitle()));
            item.tvPeriod.setText("第"+challenge.getPeriod()+"期 | "+CommonUtils.setTitle(context,challenge.getSubjectName()));
            item.tvContent.setText(challenge.getDescription());
            item.tvTime.setText(challenge.getCreateTime());
            item.tvFavor.setText(challenge.getPraiseTimes()+"");
            item.tvReward.setText(challenge.getRewardAmount()+"");
            item.tvPlay.setText(challenge.getBrowseTimes()+"");
            item.tvDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (null != mListener) {
                        mListener.onDelete(position);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnClick(position);
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
        public final ImageView frontImg;
        public final TextView tvName, tvTime,tvDelete,tvPeriod;
        public final TextView tvContent;
        public final TextView tvPlay,tvFavor,tvReward;
        public ItemViewHolder(View view) {
            super(view);
            frontImg = (ImageView) view.findViewById(R.id.my_challenge_item_img);
            tvDelete = (TextView) view.findViewById(R.id.delete_btn);
            tvName = (TextView) view.findViewById(R.id.my_challenge_name);
            tvTime = (TextView) view.findViewById(R.id.my_challenge_time);
            tvPeriod = (TextView) view.findViewById(R.id.my_challenge_period);
            tvContent = (TextView) view.findViewById(R.id.my_challenge_content);
            tvPlay = (TextView) view.findViewById(R.id.play_num);
            tvFavor = (TextView) view.findViewById(R.id.favor_num);
            tvReward = (TextView) view.findViewById(R.id.reward_num);

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
