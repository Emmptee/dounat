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
import com.donut.app.activity.IPDetailActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.PayRecoderDialog;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.message.StarMomentsResponse;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.mvp.subject.finalpk.SubjectFinalPkActivity;
import com.donut.app.mvp.subject.notice.SubjectNoticeActivity;
import com.donut.app.mvp.subject.snap.SubjectSnapActivity;
import com.donut.app.mvp.subject.special.SubjectSpecialActivity;
import com.donut.app.mvp.wish.reply.WishReplyActivity;
import com.donut.app.mvp.wish.user.WishUserActivity;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.ToastUtil;

import java.util.List;
import java.util.Map;

public class StarMomentsRecyclerViewAdapter extends RecyclerView.Adapter
        implements PayRecoderDialog.onPayListener{

    public interface onLoginListener {
        void onLogin();
    }

    private final List<StarMomentsResponse.StarMoments> momentsList;

    private String nameAndImg;
    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;
    private boolean noMoreData;

    private String nowPlayPath;

    private int nowPosition;

    private Context context;

    private AnimationDrawable animationDrawable;

    private int userType;

    private onLoginListener mListener;

    public StarMomentsRecyclerViewAdapter(List<StarMomentsResponse.StarMoments> items,
                                          String nameAndImg,
                                          View footerView,
                                          onLoginListener listener) {
        momentsList = items;
        this.footerView = footerView;
        this.nameAndImg = nameAndImg;
        this.mListener = listener;
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
                .inflate(R.layout.fragment_star_moments_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            Class<?> aClass = null;
            String CONTENT_ID = "", SUBJECT_ID = "";

            final ItemViewHolder item = (ItemViewHolder) holder;
            final StarMomentsResponse.StarMoments moments = momentsList.get(position);
            Glide.with(context)
                    .load(nameAndImg.split(",")[1])
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(context))
                    .into(item.ivHeader);
            StringBuilder buffer = new StringBuilder();
            switch (moments.getOpType()) {
                case 0:
                    //评价
                    buffer.append("评价了");
                    break;
                case 1:
                    //回复
                    buffer.append("回复了");
                    break;
                case 2:
                    //点赞
                    String msg = "点赞了";
                    if (moments.getType() == 2 || moments.getType() == 3) {
                        msg = "投票了";
                    }
                    buffer.append(msg);
                    break;
                case 3:
                    //收藏
                    buffer.append("收藏了");
                    break;
                case 4:
                    //钦点
                    buffer.append("推荐了");
                    break;
                case 5:
                    //钦点
                    buffer.append("点评了");
                    break;
            }
            switch (moments.getType()) {
                case 0:
                    //专题
                    aClass = SubjectSpecialActivity.class;
                    CONTENT_ID = SubjectSpecialActivity.SUBJECT_ID;
                    break;
                case 1:
                    //专题历史
                    aClass = SubjectFinalPkActivity.class;
                    CONTENT_ID =  SubjectFinalPkActivity.CONTENT_ID;
                    SUBJECT_ID = SubjectFinalPkActivity.SUBJECT_ID;
                    break;
                case 2:
                    //挑战
                    aClass = SubjectChallengeActivity.class;
                    CONTENT_ID =  SubjectChallengeActivity.CONTENT_ID;
                    SUBJECT_ID = SubjectChallengeActivity.SUBJECT_ID;
                    break;
                case 3:
                    //IP征集
                    aClass = IPDetailActivity.class;
                    CONTENT_ID = IPDetailActivity.IPID;
                    break;
                case 4:
//                    aClass = WishDetailActivity.class;
//                    CONTENT_ID = WishDetailActivity.CONTENT_ID;
                    if (moments.getStatus() == 0) {
                        aClass = WishUserActivity.class;
                        CONTENT_ID = WishUserActivity.CONTENT_ID;
                    } else if (moments.getStatus() == 1) {
                        aClass = WishReplyActivity.class;
                        CONTENT_ID = WishReplyActivity.CONTENT_ID;
                    }
                    break;
                case 5:
                    //街拍
                    aClass = SubjectSnapActivity.class;
                    CONTENT_ID = SubjectSnapActivity.SUBJECT_ID;
                    break;
                case 7:
                    // 大咖有通告
                    aClass = SubjectNoticeActivity.class;
                    CONTENT_ID = SubjectNoticeActivity.SUBJECT_ID;
                    break;
            }
            buffer.append(moments.getTypeName());
            item.tvTitle.setText(nameAndImg.split(",")[0] + moments.getCreateTimeStyle() + buffer.toString());
            item.tvStarName.setText(nameAndImg.split(",")[0]);
            item.tvTime.setText(moments.getCreateTime());

            item.tvContent.setVisibility(View.GONE);
            item.layoutRecording.setVisibility(View.GONE);
            if (moments.getOpType() == 0) {
                item.tvContent.setText(moments.getContent());
                item.tvContent.setVisibility(View.VISIBLE);
            } else if (moments.getOpType() == 1) {
                item.tvContent.setText(Html.fromHtml(moments.getContent()
                        + "//<font color='#81D8D0'>"
                        + (moments.getBeRepliedUserName() == null ? context.getString(R.string.default_name) : moments.getBeRepliedUserName())
                        + "</font>:"
                        + moments.getUserContent()));
                item.tvContent.setVisibility(View.VISIBLE);
            } else if(moments.getOpType() == 5) {
                item.lastTime.setText((moments.getLastTime() <= 0 ? 1 : moments.getLastTime()) + "''");

                String msg = "还没有被偷听";
                if (moments.getListenTimes() > 0) {
                    msg = "被偷听" + moments.getListenTimes() + "次";
                }
                item.tvListenTimes.setText(msg);

                item.layoutRecording.setVisibility(View.VISIBLE);
                if (moments.getContent().equals(nowPlayPath)) {
                    item.playAnim.setBackgroundResource(R.drawable.play_anim);
                    animationDrawable = (AnimationDrawable) item.playAnim.getBackground();
                    animationDrawable.start();
                } else {
                    item.playAnim.setBackgroundResource(R.drawable.voice_gray);
                }

                if(moments.getIsBuyed() == 1 || getUserType() == 1){
                    item.tvListen.setVisibility(View.GONE);
                } else {
                    item.tvListen.setVisibility(View.VISIBLE);
                }
            }

            Glide.with(context)
                    .load(moments.getContentUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(item.ivSubjectImg);
            if(moments.getType() == 7){
                item.tvSubjectTitle.setText(moments.getUserName());
            } else {
                item.tvSubjectTitle.setText(
                        (moments.getUserName() == null || "".equals(moments.getUserName())
                                ? context.getString(R.string.default_name) : moments.getUserName())
                                + "|" + moments.getTitle());
            }
            item.tvSubjectDes.setText(moments.getDescription());

            final Class<?> finalCls = aClass;
            final String finalSUBJECT_ID = SUBJECT_ID;
            final String finalCONTENT_ID = CONTENT_ID;
            item.layoutSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalCls == null) {
                        ToastUtil.showShort(context, Constant.DEFAULT_TIPS_MSG);
                        return;
                    }
                    Intent intent = new Intent(v.getContext(), finalCls);
                    intent.putExtra(finalSUBJECT_ID,moments.getSubjectId());
                    intent.putExtra(finalCONTENT_ID,moments.getContentId());
                    v.getContext().startActivity(intent);
                }
            });

            item.layoutRecordingPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordingListen(v.getContext(), moments, item);
                }
            });

            item.tvListen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 偷听支付
                    nowPosition = item.getAdapterPosition();
                    payAudio(v.getContext(), moments.getStarCommentId());
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
                                 StarMomentsResponse.StarMoments moments,
                                 final ItemViewHolder item) {
        this.nowPosition = item.getAdapterPosition();
        if (moments.getIsBuyed() == 1 || getUserType() == 1) {
            // 播放语音
            nowPlayPath = moments.getContent();
            notifyDataSetChanged();
            new StorageManager(context).getInfo(nowPlayPath, moments.getLastTime(),
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
            payAudio(context, moments.getStarCommentId());
        }
    }

    private void payAudio(Context context, String starCommentId){
        SharedPreferences sp_Info = context.getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        if(!sp_Info.getBoolean(Constant.IS_LOGIN, false)){
            ToastUtil.showShort(context, context.getString(R.string.no_login_msg));
            mListener.onLogin();
            return;
        }

        PayRecoderDialog dialog = new PayRecoderDialog(context, this, starCommentId);
        UserInfo userInfo = SysApplication.getUserInfo();
        dialog.setContent(CommonUtils.setName(context, userInfo.getNickName()),
                userInfo.getImgUrl(),
                userInfo.getmBalance(),
                sp_Info.getFloat(Constant.COMMENT_PRICE, 0f));
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return momentsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == momentsList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    @Override
    public void onPaySuccess() {
        ToastUtil.showShort(context, "支付成功");
        momentsList.get(nowPosition).setIsBuyed(1);
        notifyItemChanged(nowPosition);
    }

    @Override
    public void onPayFail(String code, String msg) {
        ToastUtil.showShort(context, msg);
    }

    public void clearStyle() {
        nowPlayPath = null;
        notifyItemChanged(nowPosition);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivHeader, ivSubjectImg;
        final TextView tvTitle, tvStarName, tvTime, tvContent;
        final TextView tvSubjectTitle, tvSubjectDes;

        final View layoutSubject, layoutRecording, layoutRecordingPlay;

        final ImageView playAnim;
        final TextView lastTime, tvListen, tvListenTimes;

        public ItemViewHolder(View view) {
            super(view);
            ivHeader = (ImageView) view.findViewById(R.id.star_moment_iv_header);
            tvTitle = (TextView) view.findViewById(R.id.star_moment_tv_title);
            tvStarName = (TextView) view.findViewById(R.id.star_moment_tv_starName);
            tvTime = (TextView) view.findViewById(R.id.star_moment_tv_time);
            tvContent = (TextView) view.findViewById(R.id.star_moment_tv_content);

            layoutSubject = view.findViewById(R.id.star_moment_layout_subject);
            ivSubjectImg = (ImageView) view.findViewById(R.id.star_moment_iv_subject_img);
            tvSubjectTitle = (TextView) view.findViewById(R.id.star_moment_tv_subject_title);
            tvSubjectDes = (TextView) view.findViewById(R.id.star_moment_tv_subject_des);

            layoutRecording = view.findViewById(R.id.star_moment_layout_recording);
            layoutRecordingPlay = view.findViewById(R.id.star_moment_layout_recording_play);
            playAnim = (ImageView) view.findViewById(R.id.star_moment_recording_play_anim);
            lastTime = (TextView) view.findViewById(R.id.star_moment_recording_last_time);
            tvListen = (TextView) view.findViewById(R.id.star_moment_recording_listen);
            tvListenTimes = (TextView) view.findViewById(R.id.star_moment_recording_listen_times);
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
