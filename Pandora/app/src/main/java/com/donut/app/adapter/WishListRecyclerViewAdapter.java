package com.donut.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donut.app.R;
import com.donut.app.databinding.FragmentWishItemBinding;
import com.donut.app.http.message.wish.NotAchieveWishModel;
import com.donut.app.utils.BindingUtils;

import java.util.List;

public class WishListRecyclerViewAdapter extends RecyclerView.Adapter<WishListRecyclerViewAdapter.BindingHolder> {

    public interface OnAdapterInteractionListener {
        void onToLogin();

        void onLike(NotAchieveWishModel model, boolean like);

        void onToComment(NotAchieveWishModel model);

        void onToShare(NotAchieveWishModel model);

        void onToDetail(NotAchieveWishModel model);

        void onStarToReply(NotAchieveWishModel model);

        void onShowImg(NotAchieveWishModel model);

        void onPlayVideo(NotAchieveWishModel model);
    }

    private final List<NotAchieveWishModel> mWishList;

    private Context mContext;

    private OnAdapterInteractionListener mListener;

    private boolean login;

    private String starUserId;

    public WishListRecyclerViewAdapter(Context mContext, boolean login,
                                       List<NotAchieveWishModel> items,
                                       OnAdapterInteractionListener listener) {
        mWishList = items;
        this.mContext = mContext;
        this.login = login;
        this.mListener = listener;
        if (mListener == null) {
            throw new NullPointerException("OnAdapterInteractionListener can't null");
        }
    }

    public WishListRecyclerViewAdapter(Context mContext, String starId,
                                       List<NotAchieveWishModel> items,
                                       OnAdapterInteractionListener listener) {
        mWishList = items;
        this.mContext = mContext;
        this.starUserId = starId;
        this.login = true;
        this.mListener = listener;
        if (mListener == null) {
            throw new NullPointerException("OnAdapterInteractionListener can't null");
        }
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentWishItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_wish_item,
                parent,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        final NotAchieveWishModel wish = mWishList.get(position);

        BindingUtils.loadRoundImg(holder.binding.wishUserHeadImg, wish.getHeadPic());
        if (wish.getMediaType() == 1){
            BindingUtils.loadImg(holder.binding.ivWishVideoImg, wish.getImgUrl());
        } else if (wish.getMediaType() == 2){
            BindingUtils.loadImg(holder.binding.ivWishImg, wish.getImgUrl());
        }

        boolean star = (starUserId != null && starUserId.equals(wish.getStarUserId()));

        holder.binding.setHandler(this);
        holder.binding.setStar(star);
        holder.binding.setWish(wish);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mWishList.size();
    }

    public void onViewClick(View v, NotAchieveWishModel model) {
        switch (v.getId()) {
            case R.id.wish_like:
                if (!login) {
                    mListener.onToLogin();
                    break;
                }

                if (model.getPraiseStatus() == 0) {
                    model.setPraiseStatus(1);
                    model.setPraiseTimes(model.getPraiseTimes() + 1);
                    mListener.onLike(model, true);
                } else {
                    model.setPraiseStatus(0);
                    model.setPraiseTimes(model.getPraiseTimes() - 1);
                    mListener.onLike(model, false);
                }
                break;
            case R.id.wish_comment:
                if (!login) {
                    mListener.onToLogin();
                } else {
                    mListener.onToComment(model);
                }
                break;
            case R.id.wish_share:
                mListener.onToShare(model);
                break;
            case R.id.wish_star_reply:
                mListener.onStarToReply(model);
                break;
            case R.id.iv_wish_img:
                mListener.onShowImg(model);
                break;
            case R.id.tv_wish_video_play:
                mListener.onPlayVideo(model);
                break;
        }
    }

    public void onItemViewClick(NotAchieveWishModel model) {
        if (model.getAchieveStatus() == 1) {
            mListener.onToDetail(model);
        }
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setStarUserId(String starUserId) {
        this.starUserId = starUserId;
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private FragmentWishItemBinding binding;

        public BindingHolder(View view) {
            super(view);
        }

        public FragmentWishItemBinding getBinding() {
            return binding;
        }

        public void setBinding(FragmentWishItemBinding binding) {
            this.binding = binding;
        }
    }
}
