package com.donut.app.mvp.spinOff.boons;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donut.app.R;
import com.donut.app.customview.ninegrid.ImageInfo;
import com.donut.app.customview.ninegrid.NineGridView;
import com.donut.app.customview.ninegrid.preview.NineGridViewClickAdapter;
import com.donut.app.databinding.FragmentSpinOffBoonsItemBinding;
import com.donut.app.http.message.spinOff.ExpressionPics;
import com.donut.app.http.message.spinOff.WelfareZone;
import com.donut.app.http.message.wish.AchieveWishVoice;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.utils.BindingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qi on 2017/6/5.
 * Description :
 */
public class SpinOffBoonsListAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void onStarDetailClick(WelfareZone detail);

        void onDownloadClick(int position, WelfareZone detail);

        void onLikeClick(WelfareZone detail);

        void onCommentClick(WelfareZone detail);

        void onShareClick(WelfareZone detail);

        void onPlayAudio(WelfareZone detail);
    }

    private Context mContext;

    private final List<WelfareZone> mDetails;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    private String nowAudioPlayPath = "";

    private StorageManager storageManager;

    private SparseArray<String> downloadMsgArray = new SparseArray<>();

    public SpinOffBoonsListAdapter(Context mContext, List<WelfareZone> items,
                                   OnItemClickListener listener, View footerView) {
        this.mContext = mContext;
        mDetails = items;
        mListener = listener;
        this.footerView = footerView;

        storageManager = new StorageManager(mContext);

        NineGridView.setImageLoader(new GlideImageLoader());
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

        FragmentSpinOffBoonsItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_spin_off_boons_item, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            bindingHolder.binding.setHandler(this);
            final WelfareZone detail = mDetails.get(position);
            bindingHolder.binding.setDetail(detail);
            bindingHolder.binding.setPosition(position);

            Integer picType = 2, audioType = 1;
            if (picType.equals(detail.getType())) {
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                List<ExpressionPics> imageDetails = detail.getExpressionPics();
                if (imageDetails != null) {
                    for (ExpressionPics imageDetail : imageDetails) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(imageDetail.getPicUrl());
                        info.setBigImageUrl(imageDetail.getGifUrl());
                        info.setB02Id(detail.getB02Id());
                        imageInfo.add(info);
                    }
                }
                Context context = bindingHolder.binding.nineGrid.getContext();
                bindingHolder.binding.nineGrid.setAdapter(
                        new NineGridViewClickAdapter(context, imageInfo));
            } else if (audioType.equals(detail.getType())) {
                if (nowAudioPlayPath.equals(detail.getVoiceUrl())) {
                    bindingHolder.binding.audioPlayAnim.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable animationDrawable
                            = (AnimationDrawable) bindingHolder.binding.audioPlayAnim.getBackground();
                    animationDrawable.start();
                } else {
                    bindingHolder.binding.audioPlayAnim.setBackgroundResource(R.drawable.voice_gray);
                }
            }

            TextView tv = bindingHolder.binding.tvBoonsItemDownload;
            tv.setText(downloadMsgArray.get(position, mContext.getString(R.string.download_msg)));
        }
    }

    @Override
    public int getItemCount() {
        return mDetails.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDetails.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public void onStarDetailClick(WelfareZone detail) {
        mListener.onStarDetailClick(detail);
    }

    public void onDownloadClick(WelfareZone detail, int position) {
        mListener.onDownloadClick(position, detail);
        downloadMsgArray.put(position, mContext.getString(R.string.download_msg));
    }

    public void onLikeClick(WelfareZone detail) {
        mListener.onLikeClick(detail);
    }

    public void onCommentClick(WelfareZone detail) {
        mListener.onCommentClick(detail);
    }

    public void onShareClick(WelfareZone detail) {
        mListener.onShareClick(detail);
    }

    public void onPlayAudioClick(View v, WelfareZone model) {

        final View playAnim = v.findViewById(R.id.audio_play_anim);
        if (nowAudioPlayPath.equals(model.getVoiceUrl())) {
            if (MediaManager.isPlaying()) {
                final AnimationDrawable animationDrawable
                        = (AnimationDrawable) playAnim.getBackground();
                animationDrawable.stop();
                playAnim.setBackgroundResource(R.drawable.voice_gray);
                MediaManager.pause();
            } else {
                playAnim.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animationDrawable
                        = (AnimationDrawable) playAnim.getBackground();
                animationDrawable.start();
                MediaManager.resume();
            }
            return;
        }

        mListener.onPlayAudio(model);

        if (!"".equals(nowAudioPlayPath)) {
            notifyDataSetChanged();
        }
        nowAudioPlayPath = model.getVoiceUrl() != null ? model.getVoiceUrl() : "";

        storageManager.getInfo(nowAudioPlayPath, model.getLastTime(),
                new AudioRequestListener(playAnim));
    }

    private class BindingHolder extends RecyclerView.ViewHolder {
        private FragmentSpinOffBoonsItemBinding binding;

        private BindingHolder(View view) {
            super(view);
        }

        public FragmentSpinOffBoonsItemBinding getBinding() {
            return binding;
        }

        public void setBinding(FragmentSpinOffBoonsItemBinding binding) {
            this.binding = binding;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            BindingUtils.loadImg(imageView, url);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    private class AudioRequestListener implements RequestManager.RequestListener {

        private View playAnim;

        private AudioRequestListener(View playAnim) {
            this.playAnim = playAnim;
        }

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
    }

    void setTvDownLoadMsg(int position, String tvDownLoadMsg) {
        downloadMsgArray.put(position, tvDownLoadMsg);
    }

    public void setNowAudioPlayPath(String nowAudioPlayPath) {
        this.nowAudioPlayPath = nowAudioPlayPath;
    }
}
