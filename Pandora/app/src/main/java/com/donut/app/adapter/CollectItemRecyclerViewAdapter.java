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

import com.donut.app.R;
import com.donut.app.http.message.MyCollectionResponse;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.CommonUtils;

import java.util.List;

/**
 */
public class CollectItemRecyclerViewAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void OnChallengeClick(String contentType, String subjectId, String contentId);

        void OnWishClick(String contentId);

        void OnSubjectItemClick(String subjectId, String contentType);
    }

    private final List<MyCollectionResponse.MyCollection> collectList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private int orderType;

    private Context context;

    private boolean noMoreData;

    public CollectItemRecyclerViewAdapter(Context context, List<MyCollectionResponse.MyCollection> items,
                                          int type,
                                          OnItemClickListener listener,
                                          View footerView) {
        this.context = context;
        collectList = items;
        orderType = type;
        mListener = listener;
        this.footerView = footerView;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BOTTOM_TYPE) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(lp);
            return new FooterViewHolder(footerView);
        }
        if (orderType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_subject_collect_item_layout, parent, false);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(lp);
            return new SubjectItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_ip_collect_item, parent, false);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(lp);
            return new ChallIPItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SubjectItemViewHolder) {
            SubjectItemViewHolder item = (SubjectItemViewHolder) holder;
            final MyCollectionResponse.MyCollection collection = collectList.get(position);
            BindingUtils.loadImg(item.ivPlaybill, collection.getImgUrl());
            if ("7".equals(collection.getContentType())) {
                item.tvName.setText(CommonUtils.setName(context, collection.getStarName()));
            } else {
                item.tvName.setText(CommonUtils.setName(context, collection.getStarName())
                        + " | "
                        + CommonUtils.setTitle(context, collection.getName()));
            }
            item.tvContent.setText(collection.getDescription());
            if (collection.getPeriod() > 0) {
                item.tvPeriod.setText(collection.getPeriod() + "");
                item.periodLayout.setVisibility(View.VISIBLE);
            } else {
                item.periodLayout.setVisibility(View.GONE);
            }
            item.tvChannelName.setText(collection.getChannelName());
//            if (collection.getStatus() != null)
//            {
//                if (collection.getStatus() == 1)
//                {
//                    item.tvStatus.setText("进行中");
//                } else if (collection.getStatus() == 2)
//                {
//                    item.tvStatus.setText("已结束");
//                }
//            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnSubjectItemClick(collection.getSubjectId(), collection.getContentType());
                    }
                }
            });
        } else if (holder instanceof ChallIPItemViewHolder) {
            ChallIPItemViewHolder item = (ChallIPItemViewHolder) holder;
            final MyCollectionResponse.MyCollection collect = collectList.get(position);
            BindingUtils.loadImg(item.ivPlaybill, collect.getImgUrl());
            BindingUtils.loadRoundImg(item.ivHeadPhoto, collect.getHeadPic());
            item.tvName.setText(CommonUtils.setName(context, collect.getStarName()) + " | " + CommonUtils.setTitle(context, collect.getName()));
            item.tvContent.setText(collect.getDescription());
            item.tvTime.setText(collect.getCreateTime());
            if (collect.getMemberStatus() != null && collect.getMemberStatus() == 1) {
                item.ivHeadVip.setVisibility(View.VISIBLE);
            } else {
                item.ivHeadVip.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        if (orderType == 2) {
                            mListener.OnChallengeClick(collect.getContentType(),
                                    collect.getSubjectId(),
                                    collect.getContentId());
                        } else {
                            mListener.OnWishClick(collect.getIpId());
                        }
                    }
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerholder = (FooterViewHolder) holder;
            if (noMoreData) {
                footerholder.pb.setVisibility(View.GONE);
                footerholder.text.setText("没有更多数据");
                setNoMoreData(false);
            } else {
                footerholder.pb.setVisibility(View.VISIBLE);
                footerholder.text.setText("加载中...");
            }
        }

    }


    @Override
    public int getItemCount() {
        return collectList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == collectList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    private static class SubjectItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPlaybill;
        private final TextView tvName, tvPeriod, tvChannelName;
        //tvStatus
        private final TextView tvContent;
        private final View periodLayout;

        private SubjectItemViewHolder(View view) {
            super(view);
            ivPlaybill = (ImageView) view.findViewById(R.id.subject_item_playbill);
            tvName = (TextView) view.findViewById(R.id.subject_item_name);
            tvPeriod = (TextView) view.findViewById(R.id.subject_item_period);
            periodLayout = view.findViewById(R.id.subject_item_period_layout);
//            tvStatus = (TextView) view.findViewById(R.id.subject_item_status);
            tvContent = (TextView) view.findViewById(R.id.subject_item_description);
            tvChannelName = (TextView) view.findViewById(R.id.subject_item_channel_name);
        }
    }

    private static class ChallIPItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPlaybill;
        private final ImageView ivHeadPhoto, ivHeadVip;
        private final TextView tvName, tvTime;
        public final TextView tvContent;

        private ChallIPItemViewHolder(View view) {
            super(view);
            ivPlaybill = (ImageView) view.findViewById(R.id.ip_item_playbill);
            ivHeadPhoto = (ImageView) view.findViewById(R.id.ip_item_head);
            tvName = (TextView) view.findViewById(R.id.ip_item_name);
            tvTime = (TextView) view.findViewById(R.id.ip_item_time);
            tvContent = (TextView) view.findViewById(R.id.ip_item_content);
            ivHeadVip = (ImageView) view.findViewById(R.id.ip_item_head_vip);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.footer_loadmore_text);
            pb = (ProgressBar) view.findViewById(R.id.footer_load_progress);
        }

        public TextView text;

        public ProgressBar pb;
    }

    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }

    public boolean getNoMoreData() {
        return noMoreData;
    }
}
