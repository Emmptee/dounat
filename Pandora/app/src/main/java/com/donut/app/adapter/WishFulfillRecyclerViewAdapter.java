package com.donut.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.manager.RequestManager;
import com.bumptech.glide.Glide;
import com.donut.app.BR;
import com.donut.app.R;
import com.donut.app.databinding.FragmentWishFulfillBinding;
import com.donut.app.http.message.wish.AchieveWish;
import com.donut.app.http.message.wish.AchieveWishVoice;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;
import java.util.Map;

public class WishFulfillRecyclerViewAdapter extends RecyclerView.Adapter<WishFulfillRecyclerViewAdapter.BindingHolder> {

    public interface OnAdapterInteractionListener {
        void onToLogin();

        void onLike(AchieveWish model, boolean like);

        void onToComment(AchieveWish model);

        void onToShare(AchieveWish model);

        void onToCollect(AchieveWish model, boolean collect);

        void onPlayVideo(AchieveWish model);

        void onPlayAudio(AchieveWish model);

        void onShowWish(AchieveWish model);
    }

    private final List<AchieveWish> mWishList;

    private Context mContext;

    private OnAdapterInteractionListener mListener;

    private boolean login;

    private String nowAudioPlayPath = "";

    public WishFulfillRecyclerViewAdapter(Context mContext, boolean login,
                                          List<AchieveWish> items,
                                          OnAdapterInteractionListener listener) {
        mWishList = items;
        this.mContext = mContext;
        this.login = login;
        this.mListener = listener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentWishFulfillBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_wish_fulfill,
                parent,
                false);
        return new BindingHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        AchieveWish wish = mWishList.get(position);

        if (wish.getWishType() == 0) {
            String starHeadPic = wish.getStarHeadPic();
            Glide.with(mContext)
                    .load(starHeadPic)
                    .centerCrop()
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.binding.wishStarHeadImg);

            String showImgUrl = wish.getStarHeadPic();
            if (wish.getAchievePicUrl() != null
                    && wish.getAchievePicUrl().length() > 0) {
                showImgUrl = wish.getAchievePicUrl();
            }
            Glide.with(mContext)
                    .load(showImgUrl)
                    .centerCrop()
                    .into(holder.binding.wishShowImg);

            Glide.with(mContext)
                    .load(wish.getStarHeadPic())
                    .centerCrop()
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.binding.wishStarHeadImg2);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.icon_logo)
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.binding.wishStarHeadImg);

            Glide.with(mContext)
                    .load(wish.getAchievePicUrl())
                    .error(R.drawable.wish_fulfill_top_bg)
                    .centerCrop()
                    .into(holder.binding.wishShowImg);

            Glide.with(mContext)
                    .load(R.drawable.icon_logo)
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.binding.wishStarHeadImg2);
        }

//        Glide.with(mContext)
//                .load(wish.getHeadPic())
//                .centerCrop()
//                .error(R.drawable.default_header)
//                .transform(new GlideCircleTransform(mContext))
//                .into(holder.binding.wishUserHeadImg);

        if (wish.getAchieveVoiceList() != null && wish.getAchieveVoiceList().size() > 0
                && !TextUtils.isEmpty(nowAudioPlayPath)
                && nowAudioPlayPath.equals(wish.getAchieveVoiceList().get(0).getAchieveVoiceUrl())) {
            holder.binding.wishFulfillAudioPlayAnim
                    .setBackgroundResource(R.drawable.play_anim);
            final AnimationDrawable animationDrawable
                    = (AnimationDrawable) holder.binding.wishFulfillAudioPlayAnim.getBackground();

            holder.binding.wishFulfillAudioPlayAnim.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationDrawable.start();
                }
            }, 10);
        } else {
            holder.binding.wishFulfillAudioPlayAnim.setBackgroundResource(R.drawable.voice_gray);
        }

        holder.bind(wish);

        holder.binding.setHandler(this);
        holder.binding.setVariable(BR.wishFulfill, wish);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mWishList.size();
    }

    public void onPlayClick(AchieveWish model) {
        mListener.onPlayVideo(model);
        model.setVideoPlayTimes(model.getVideoPlayTimes() + 1);
    }

    public void onCollectClick(AchieveWish model) {
        if (!login) {
            mListener.onToLogin();
        } else {
            if (model.getCollectionStatus() == 0) {
                model.setCollectionStatus(1);
                model.setCollectTimes(model.getCollectTimes() + 1);
                mListener.onToCollect(model, true);
            } else {
                model.setCollectionStatus(0);
                model.setCollectTimes(model.getCollectTimes() - 1);
                mListener.onToCollect(model, false);
            }
        }
    }

    public void onLikeClick(AchieveWish model) {
        if (!login) {
            mListener.onToLogin();
            return;
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
    }

    public void onShareClick(AchieveWish model) {
        mListener.onToShare(model);
    }

    public void onCommentClick(AchieveWish model) {
        if (!login) {
            mListener.onToLogin();
        } else {
            mListener.onToComment(model);
        }
    }

    public void onPlayAudioClick(View v, AchieveWish model) {
        mListener.onPlayAudio(model);

        if (!"".equals(nowAudioPlayPath)) {
            notifyDataSetChanged();
        }

        final View playAnim = v.findViewById(R.id.wish_fulfill_audio_play_anim);

        AchieveWishVoice voice = model.getAchieveVoiceList().get(0);
        nowAudioPlayPath = voice.getAchieveVoiceUrl();

        new StorageManager(mContext).getInfo(nowAudioPlayPath, voice.getLastTime(),
                new RequestManager.RequestListener() {
                    @Override
                    public void onRequest() {
                    }

                    @Override
                    public void onLoading(long l, long l1, String s) {
                    }

                    @Override
                    public void onSuccess(String response, Map<String, String> headers,
                                          String url, int actionId) {
                        playAnim.setBackgroundResource(R.drawable.play_anim);
                        final AnimationDrawable animationDrawable
                                = (AnimationDrawable) playAnim.getBackground();
                        animationDrawable.start();

                        MediaManager.playSound(response, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                nowAudioPlayPath = "";
                                animationDrawable.stop();
                                notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                    }
                });

        voice.setListenTimes(voice.getListenTimes() + 1);
    }

    public void onShowWishClick(AchieveWish model) {
        mListener.onShowWish(model);
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private FragmentWishFulfillBinding binding;

        public BindingHolder(View view, FragmentWishFulfillBinding binding) {
            super(view);
            this.binding = binding;
        }

        public void bind(AchieveWish wish) {
            binding.setWishFulfill(wish);
        }
    }
}
