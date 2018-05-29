package com.donut.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.PayRecoderDialog;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.message.SubjectHistoryPKStarComment;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.Map;

public class StarCommentRadioRecyclerViewAdapter extends RecyclerView.Adapter
        implements PayRecoderDialog.onPayListener{

    public interface OnUserPayListener{
        void userPaySucc(String uuid);
    }

    private final List<SubjectHistoryPKStarComment> commentList;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;
    private boolean noMoreData;

    private String nowPlayPath;

    private int nowPosition;

    private Context context;

    private String userId,challUserid;

    private int userType = 0;

    private OnUserPayListener userPayListener;

    public StarCommentRadioRecyclerViewAdapter(String challUserid, String userId, int userType,
                                               List<SubjectHistoryPKStarComment> items,
                                               View footerView, OnUserPayListener listener) {
        if (userId == null) {
            userId = "";
        }
        this.challUserid=challUserid;
        this.userType = userType;
        this.userId = userId;
        commentList = items;
        this.footerView = footerView;
        this.userPayListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if (viewType == BOTTOM_TYPE) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(lp);
            return new FooterViewHolder(footerView);
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_star_comment_radio_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            final ItemViewHolder item = (ItemViewHolder) holder;
            final SubjectHistoryPKStarComment comment = commentList.get(position);

            Glide.with(context)
                    .load(comment.getHeadPic())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(context))
                    .into(item.ivHeader);
            item.tvStarName.setText(comment.getName());
            item.tvTime.setText(comment.getCommentTime());
            item.lastTime.setText((comment.getLastTime() <= 0 ? 1 : comment.getLastTime()) + "''");

            if(comment.getListenTimes() == null || comment.getListenTimes() <= 0){
                item.tvListenTimes.setText("还没有被偷听");
            } else {
                item.tvListenTimes.setText(
                        Html.fromHtml(
                                "被偷听<font color='#81d8d0'>"
                                        + comment.getListenTimes()
                                        + "</font>次"));
            }

            if (comment.getAudioUrl().equals(nowPlayPath)) {
                item.playAnim.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) item.playAnim.getBackground();
                animationDrawable.start();
            } else {
                item.playAnim.setBackgroundResource(R.drawable.voice_gray);
            }
            if(userId.equals(comment.getFkA01())
                    || userId.equals(challUserid)
                    || userType == 1){
                comment.setStatus("1");
            }
            if("1".equals(comment.getStatus())){
                item.tvListen.setVisibility(View.GONE);
            } else {
                item.tvListen.setVisibility(View.VISIBLE);
            }

            item.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StarDetailActivity.class);
                    intent.putExtra(StarDetailActivity.STAR_ID , comment.getActorUuid());
                    intent.putExtra(StarDetailActivity.FKA01_ID , comment.getFkA01());
                    context.startActivity(intent);
                }
            });

            item.layoutRecordingPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordingListen(v.getContext(), comment, item);
                }
            });

            item.tvListen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 偷听支付
                    nowPosition = item.getAdapterPosition();
                    payAudio(v.getContext(), comment.getUuid());
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder hold = (FooterViewHolder) holder;
            if (noMoreData) {
                hold.pb.setVisibility(View.GONE);
                hold.text.setText("没有更多数据");
                setNoMoreData(false);
            } else {
                hold.pb.setVisibility(View.VISIBLE);
                hold.text.setText("加载中...");
            }
        }
    }

    private void recordingListen(Context context,
                                 SubjectHistoryPKStarComment comment,
                                 final ItemViewHolder item) {
        this.nowPosition = item.getAdapterPosition();
        if("1".equals(comment.getStatus())){
            // 播放语音
            nowPlayPath = comment.getAudioUrl();
            notifyDataSetChanged();
            new StorageManager(context).getInfo(nowPlayPath, comment.getLastTime(),
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
                            item.playAnim.setBackgroundResource(R.drawable.play_anim);
                            AnimationDrawable animationDrawable
                                    = (AnimationDrawable) item.playAnim.getBackground();
                            animationDrawable.start();
                            MediaManager.playSound(response, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    nowPlayPath = "";
                                    notifyItemChanged(nowPosition);
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMsg, String url, int actionId) {

                        }
                    });
        } else {
            //去支付...
            payAudio(context, comment.getUuid());
        }
    }

    private void payAudio(Context context, String starCommentId){
        SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        if(!sp_Info.getBoolean(Constant.IS_LOGIN, false)){
            ToastUtil.showShort(context, context.getString(R.string.no_login_msg));
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        PayRecoderDialog dialog = new PayRecoderDialog(context, this, starCommentId);
        UserInfo userInfo = SysApplication.getUserInfo();
        dialog.setContent(CommonUtils.setName(context,userInfo.getNickName()),
                userInfo.getImgUrl(),
                userInfo.getmBalance(),
                sp_Info.getFloat(Constant.COMMENT_PRICE, 0f));
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return commentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == commentList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    @Override
    public void onPaySuccess() {
        ToastUtil.showShort(context, "支付成功");
        SubjectHistoryPKStarComment comment = commentList.get(nowPosition);
        comment.setStatus("1");
        Integer times = comment.getListenTimes();
        if (times == null) {
            times = 0;
        } else {
            times++;
        }
        comment.setListenTimes(times);
        notifyItemChanged(nowPosition);
        userPayListener.userPaySucc(comment.getUuid());
    }

    @Override
    public void onPayFail(String code, String msg) {
        ToastUtil.showShort(context, msg);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivHeader;
        final TextView tvStarName, tvTime, tvListenTimes;

        final View layoutRecording, layoutRecordingPlay;

        final ImageView playAnim;
        final TextView lastTime, tvListen;

        public ItemViewHolder(View view) {
            super(view);
            ivHeader = (ImageView) view.findViewById(R.id.star_comment_radio_item_iv_header);
            tvStarName = (TextView) view.findViewById(R.id.star_comment_radio_item_tv_starName);
            tvTime = (TextView) view.findViewById(R.id.star_comment_radio_item_tv_time);
            tvListenTimes = (TextView) view.findViewById(R.id.star_comment_radio_item_recording_listenTimes);

            layoutRecording = view.findViewById(R.id.star_comment_radio_item_layout_recording);
            layoutRecordingPlay = view.findViewById(R.id.star_comment_radio_item_recording_play);
            playAnim = (ImageView) view.findViewById(R.id.star_comment_radio_item_recording_play_anim);
            lastTime = (TextView) view.findViewById(R.id.star_comment_radio_item_recording_last_time);
            tvListen = (TextView) view.findViewById(R.id.star_comment_radio_item_recording_listen);
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
