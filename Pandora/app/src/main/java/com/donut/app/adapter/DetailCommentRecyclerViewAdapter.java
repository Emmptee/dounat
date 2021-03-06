package com.donut.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.databinding.DetailCommentRecycleViewItemBinding;
import com.donut.app.http.message.ContentComments;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

public class DetailCommentRecyclerViewAdapter extends RecyclerView.Adapter<DetailCommentRecyclerViewAdapter.BindingHolder> {

    public interface OnItemClickListener {
        void OnCommentItemClick();
    }

    private final List<ContentComments> mCommentList;

    private Context mContext;

    private OnItemClickListener mListener;

    public DetailCommentRecyclerViewAdapter(Context mContext,
                                            List<ContentComments> items,
                                            OnItemClickListener mListener) {
        mCommentList = items;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DetailCommentRecycleViewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.detail_comment_recycle_view_item,
                parent,
                false);
        return new BindingHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        ContentComments comment = mCommentList.get(position);
        Glide.with(mContext)
                .load(comment.getCommentatorUrl())
                .centerCrop()
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(mContext))
                .into(holder.binding.wishCommentItemHeader);

        holder.bind(comment);
        holder.binding.executePendingBindings();

        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams) holder.binding.commentItemBottomLine.getLayoutParams();
        if (position != getItemCount() - 1) {
            params.addRule(RelativeLayout.RIGHT_OF, R.id.comment_item_header_layout);
        } else {
            params.addRule(RelativeLayout.RIGHT_OF, 0);
        }
        holder.binding.commentItemBottomLine.setLayoutParams(params);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnCommentItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }


    public class BindingHolder extends RecyclerView.ViewHolder {
        private DetailCommentRecycleViewItemBinding binding;

        public BindingHolder(View view, DetailCommentRecycleViewItemBinding binding) {
            super(view);
            this.binding = binding;
        }

        public void bind(ContentComments comment) {
            binding.setComment(comment);
        }
    }
}
