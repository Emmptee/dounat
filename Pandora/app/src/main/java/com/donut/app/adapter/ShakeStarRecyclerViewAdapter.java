package com.donut.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.BR;
import com.donut.app.R;
import com.donut.app.databinding.ShakeCommendItemBinding;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.record.preview.DonutCameraVideo;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.NetUtils;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by hard on 2018/1/29.
 */

public class ShakeStarRecyclerViewAdapter extends RecyclerView.Adapter<ShakeStarRecyclerViewAdapter.Shake_starViewHolder> {

    private static final String TAG = "ShakeStarRecyclerViewAd";

    private List<ShakeStarCommendResponse.ShakingStarListBean> list;
    private List<ParticularsResponse.ShakingStarListBean> list2;
    private Context mContext;
    private ItemSelectOnClickListener itemSelectOnClickListener;
    private ItemLikeOnClickListener itemLikeOnClickListener;
    private ItemLikeOnClickListener2 itemLikeOnClickListener2;
    private ItemShareOnClickListener itemShareOnClickListener;
    private ItemMyOnClickListener itemMyOnClickListener;
    private ItemCommendOnClickListener itemCommendOnClickListener;
    private ItemInformOnClick itemInformOnClick;
    private ItemInformOnClick2 itemInformOnClick2;
    private ItemAttentionOnClick itemAttentionOnClick;
    private ItemAttentionOnClick2 itemAttentionOnClick2;
    private int position;
    private int videoPosition;
    private boolean login;
    private ShakeCommendItemBinding view;
    private int status = 0;

    // it is to check weather or not need to play the video (while change the page it must stop playing)

    public ShakeStarRecyclerViewAdapter(List<ShakeStarCommendResponse.ShakingStarListBean> list, List<ParticularsResponse.ShakingStarListBean> list2, Context mContext) {
        if (list2 != null) {
            this.list2 = list2;
        } else {
            this.list = list;
        }

        this.login = login;
        this.mContext = mContext;
    }

    @Override
    public Shake_starViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        ShakeCommendItemBinding view = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.shake_commend_item,
                parent,
                false);
        final Shake_starViewHolder viewHolder = new Shake_starViewHolder(view.getRoot(), view);

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.shake_commend_item,null);
        this.view = view;
//        final Shake_starViewHolder viewHolder = new Shake_starViewHolder(view);
        //点赞回调接口
        viewHolder.zan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 触发了点赞效果");

                position = viewHolder.getAdapterPosition();
                if (list2 != null) {
                    onLikeClick(list2.get(position));
                } else {
                    onLikeClick(list.get(position), viewHolder);
                }
            }
        });
        //分享接口回调
        viewHolder.fenxiang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = viewHolder.getAdapterPosition();
                if (list2 != null) {
                    viewHolder.fenxiang_count.setText(list2.get(0).getShareTimes() + 1 + "");
                    itemShareOnClickListener.ShareClick(position);
                } else {
                    viewHolder.fenxiang_count.setText(list.get(position).getShareTimes() + 1 + "");
                    itemShareOnClickListener.ShareClick(position);
                }

            }
        });
        //素材详情接口回调
        viewHolder.sc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewHolder.getAdapterPosition();
                itemSelectOnClickListener.SelectClick(position);
            }
        });
        viewHolder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewHolder.getAdapterPosition();
                itemMyOnClickListener.MyOnClick(position);
            }
        });
        viewHolder.commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewHolder.getAdapterPosition();
                itemCommendOnClickListener.CommendClick(position, viewHolder.commend);
            }
        });
        viewHolder.inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (list != null) {
                    itemInformOnClick.onInformClick(list.get(position).getB02Id());
                } else {
                    itemInformOnClick2.onInformClick(list2.get(position).getB02Id());
                }

            }
        });
        //关注
        viewHolder.attention_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = viewHolder.getAdapterPosition();
                if (list != null) {
                    itemAttentionOnClick.Attention(list.get(position).getFkA01(), "1");
                } else {
                    itemAttentionOnClick2.Attention2(list2.get(position).getFkA01(), "1");
                }
                viewHolder.attention_user.setVisibility(View.GONE);
            }
        });
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(Shake_starViewHolder holder, int position) {
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);//视频充满

        this.videoPosition = position;

        if (list2 != null) {
//                if(list2.get(position).getFollowStatus()!=1){
//                    holder.attention_user.setVisibility(View.VISIBLE);
//                }else{
//                    holder.attention_user.setVisibility(View.GONE);
//                }
//            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext).load(list2.get(position).getVideoThumbnail()).into(holder.video.thumbImageView);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            holder.video.setThumbImageView(imageView);
            if (TextUtils.isEmpty(list2.get(position).getNickName())) {
                holder.uname.setText("小麦穗");//用户昵称
            } else {
                holder.uname.setText(list2.get(position).getNickName());//用户昵称
            }

            holder.video.setUp(list2.get(position).getPlayUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, " ");//视频
            holder.video.performClick();
//            ImageView backButton = holder.video.getBackButton();
//            backButton.setVisibility(View.GONE);//隐藏返回键
//            ImageView fullscreenButton = holder.video.getFullscreenButton();
//            fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
            BindingUtils.loadRoundImg(holder.head_img, list2.get(position).getHeadPic());//用户头像
            holder.bz.setText(list2.get(position).getMaterialTitle());//素材标题
            holder.content.setText(list2.get(position).getContentDesc());//内容描述
            if (list2.get(position).getBrowseTimes() >= 10000) {
                BigDecimal bigDecimal = new BigDecimal(list2.get(position).getBrowseTimes());
                BigDecimal bigDecimal2 = new BigDecimal(10000);
                holder.player_count.setText(bigDecimal.divide(bigDecimal2, 2, RoundingMode.HALF_UP) + "万次观看");//观看次数
            } else {
                holder.player_count.setText(list2.get(position).getBrowseTimes() + "次观看");//观看次数
            }

            if (list2.get(position).getPraiseStatus() == 1) {
                holder.zan_img.setImageResource(R.drawable.dianzan2);
            }
            boolean wifi = NetUtils.isWifi(mContext);
            if (wifi) {
//                GSYBaseVideoPlayer player = holder.video;
                //player.startPlayLogic();
            }
//                BindingUtils.loadRoundImg(holder.sc_img, (String) list2.get(position).getMaterialThumbnail());
//                holder.zan_count.setText(list2.get(position).getPraiseTimes()+"");//点赞数量
            holder.pinglun_count.setText(list2.get(position).getCommentTimes() + "");//评论数量
            holder.fenxiang_count.setText(list2.get(position).getShareTimes() + "");//分享数量
        } else {

//                GSYVideoType
//                GSYBaseVideoPlayer
            if (list.get(position).getFollowStatus() != 1) {
                holder.attention_user.setVisibility(View.VISIBLE);
            } else {
                holder.attention_user.setVisibility(View.GONE);
            }
            ShakeStarCommendResponse.ShakingStarListBean commend = list.get(position);
//            ImageView imageView = new ImageView(mContext);
//            Glide.with(mContext).load(list.get(position).getVideoThumbnail()).into(imageView);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            holder.video.setThumbImageView(imageView);
            Glide.with(mContext).load(list.get(position).getVideoThumbnail()).into(holder.video.thumbImageView);


            if (TextUtils.isEmpty(list.get(position).getNickName())) {
                holder.uname.setText("小麦穗");//用户昵称
            } else {
                holder.uname.setText(list.get(position).getNickName());//用户昵称
            }
            holder.video.setUp(list.get(position).getPlayUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, " ");//视频
            holder.video.performClick();
            /*ImageView backButton = holder.video.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            ImageView fullscreenButton = holder.video.getFullscreenButton();
            fullscreenButton.setVisibility(View.GONE);//隐藏全屏按钮
            holder.video.setThumbPlay(true);//点击封面播放
            View startButton = holder.video.getStartButton();*/
//                GSYBaseVideoPlayer player=holder.video;
//                player.startPlayLogic();//播放视频
//                View startButton = holder.video.getStartButton();
//                startButton.setVisibility(View.GONE);
//                startButton.setBackgroundResource(R.drawable.ic_action);
            BindingUtils.loadRoundImg(holder.head_img, list.get(position).getHeadPic());//用户头像
            holder.bz.setText(list.get(position).getMaterialTitle());//素材标题
            holder.content.setText(list.get(position).getContentDesc());//内容描述
            if (list.get(position).getBrowseTimes() >= 10000) {
                BigDecimal bigDecimal = new BigDecimal(list.get(position).getBrowseTimes());
                BigDecimal bigDecimal2 = new BigDecimal(10000);
                holder.player_count.setText(bigDecimal.divide(bigDecimal2, 2, RoundingMode.HALF_UP) + "万次观看");//观看次数
            } else {
                holder.player_count.setText(list.get(position).getBrowseTimes() + "次观看");//观看次数
            }
//                BindingUtils.loadRoundImg(holder.sc_img,list.get(position).getMaterialThumbnail());
            if (list.get(position).getPraiseStatus() == 1) {
                holder.zan_img.setImageResource(R.drawable.dianzan2);
            }
            boolean wifi = NetUtils.isWifi(mContext);
                /*if(wifi && needToPlay){
                    GSYBaseVideoPlayer player=holder.video;
                    player.startPlayLogic();
                }*/
            if (wifi) {
//                GSYBaseVideoPlayer player = holder.video;
                //player.startPlayLogic();
            }
//              holder.zan_count.setText(list.get(position).getPraiseTimes()+"");//点赞数量
            holder.pinglun_count.setText(list.get(position).getCommentTimes() + "");//评论数量
            holder.fenxiang_count.setText(list.get(position).getShareTimes() + "");//分享数量
            holder.bind(commend);
            holder.binding.setHandler(this);
            holder.binding.setVariable(BR.commendFulfill, commend);
            holder.binding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        if (list2 != null) {
            return list2.size();
        } else {
            return list.size();
        }

    }

    public void onPlayClick(Shake_starViewHolder holder){
        holder.video.performClick();
    }


    public class Shake_starViewHolder extends RecyclerView.ViewHolder {
        private ShakeCommendItemBinding binding;
        private DonutCameraVideo video;
        private AutoRelativeLayout user;
        private TextView bz;
        private ImageView head_img;
        private ImageView attention_user;
        //        private ImageView action_start ;
        private ImageView inform;
        private ImageView zan_img;
        private ImageView commend;
        private ImageView fenxiang_img;
        private TextView uname;
        private TextView content;
        private ImageView sc_img;
        private TextView player_count;
        private TextView zan_count;
        private TextView pinglun_count;
        private TextView fenxiang_count;

        public Shake_starViewHolder(View itemView, ShakeCommendItemBinding binding) {
            super(itemView);
            this.binding = binding;
            video = itemView.findViewById(R.id.tj_video);
            bz = itemView.findViewById(R.id.bz);
            head_img = itemView.findViewById(R.id.head_img);
            attention_user = itemView.findViewById(R.id.attention_user);
//            action_start = itemView.findViewById(R.id.action_start);
            inform = itemView.findViewById(R.id.inform);
            uname = itemView.findViewById(R.id.uname);
            content = itemView.findViewById(R.id.content);
            sc_img = itemView.findViewById(R.id.material);
            player_count = itemView.findViewById(R.id.player_count);
            zan_count = itemView.findViewById(R.id.zan_count);
            commend = itemView.findViewById(R.id.pinglun_img);
            zan_img = itemView.findViewById(R.id.zan_img);
            pinglun_count = itemView.findViewById(R.id.pinglun_count);
            fenxiang_count = itemView.findViewById(R.id.fenxiang_count);
            fenxiang_img = itemView.findViewById(R.id.fenxiang_img);
            user = itemView.findViewById(R.id.user_message);
        }

        public void bind(ShakeStarCommendResponse.ShakingStarListBean model) {
            binding.setCommendFulfill(model);
        }
    }

    public void onLikeClick(ShakeStarCommendResponse.ShakingStarListBean model, Shake_starViewHolder holder) {
//        if (!login) {
//            mListener.onToLogin();
//            return;
//        }
        if (model.getPraiseStatus() == 0) {//点赞
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            itemLikeOnClickListener.LikeClick(model, true);
            holder.zan_img.setImageResource(R.drawable.dianzan2);
        } else { //取消
            model.setPraiseStatus(0);
            if (model.getPraiseTimes() > 0) {
                model.setPraiseTimes(model.getPraiseTimes() - 1);
            }
            holder.zan_img.setImageResource(R.drawable.dianzan1);
            itemLikeOnClickListener.LikeClick(model, false);
        }
//        if(model.getPraiseStatus()==0){
//            holder.zan_img.setImageResource(R.drawable.dianzan2);
//            itemLikeOnClickListener.LikeClick(model,true);
//            model.setPraiseTimes(model.getPraiseTimes() + 1);
//            holder.zan_count.setText(list.get(position).getPraiseTimes()+"");//点赞数量
//            status=1;
//        }else if(model.getPraiseStatus()==1){//取消收藏
//            holder.zan_img.setImageResource(R.drawable.dianzan1);
//            itemLikeOnClickListener.LikeClick(model,false);
//            model.setPraiseTimes(model.getPraiseTimes() - 1);
//            holder.zan_count.setText(list.get(position).getPraiseTimes()+"");//点赞数量
//            status=0;
//        }
    }
//    public void OnClickLike(ShakeStarCommendResponse.ShakingStarListBean model){
//        if (model.getPraiseStatus() == 0) {//点赞
//            model.setPraiseStatus(1);
//            model.setPraiseTimes(model.getPraiseTimes() + 1);
//            itemLikeOnClickListener.LikeClick(model,true);
////            holder.zan_img.setImageResource(R.drawable.dianzan2);
//        } else { //取消
//            model.setPraiseStatus(0);
//            if(model.getPraiseTimes()>0){
//                model.setPraiseTimes(model.getPraiseTimes() - 1);
//            }
////            holder.zan_img.setImageResource(R.drawable.dianzan1);
//            itemLikeOnClickListener.LikeClick(model,false);
//        }
//    }

    public void onLikeClick(ParticularsResponse.ShakingStarListBean model2) {
//        if (!login) {
//            mListener.onToLogin();
//            return;
//        }

        if (model2.getPraiseStatus() == 0) {
            model2.setPraiseStatus(1);
            model2.setPraiseTimes(model2.getPraiseTimes() + 1);

            itemLikeOnClickListener2.LikeClick(model2, true);
        } else {
            model2.setPraiseStatus(0);
            if (model2.getPraiseTimes() > 0) {
                model2.setPraiseTimes(model2.getPraiseTimes() - 1);
            }
            itemLikeOnClickListener2.LikeClick(model2, false);
        }
        notifyDataSetChanged();
    }

    public int getPosition() {
        return videoPosition;
    }

    public interface ItemAttentionOnClick {
        void Attention(String starId, String operation);
    }

    public interface ItemAttentionOnClick2 {
        void Attention2(String starId, String operation);
    }

    public interface ItemInformOnClick {
        void onInformClick(String fkB02);
    }

    public interface ItemInformOnClick2 {
        void onInformClick(String fkB02);
    }

    public interface ItemSelectOnClickListener {
        void SelectClick(int position);
    }

    public interface ItemMyOnClickListener {
        void MyOnClick(int position);
    }

    public interface ItemLikeOnClickListener {
        void LikeClick(ShakeStarCommendResponse.ShakingStarListBean model, boolean like);
    }

    public interface ItemLikeOnClickListener2 {
        void LikeClick(ParticularsResponse.ShakingStarListBean model2, boolean like);
    }

    public interface ItemShareOnClickListener {
        void ShareClick(int position);
    }

    public interface ItemCommendOnClickListener {
        void CommendClick(int position, ImageView img);
    }

    public void setItemInformOnClick2(ItemInformOnClick2 itemInformOnClick2) {
        this.itemInformOnClick2 = itemInformOnClick2;
    }

    public void setItemInformOnClick(ItemInformOnClick itemInformOnClick) {
        this.itemInformOnClick = itemInformOnClick;
    }

    public void setItemCommendOnClickListener(ItemCommendOnClickListener itemCommendOnClickListener) {
        this.itemCommendOnClickListener = itemCommendOnClickListener;
    }

    public void setItemSelectOnClickListener(ItemSelectOnClickListener itemSelectOnClickListener) {
        this.itemSelectOnClickListener = itemSelectOnClickListener;
    }

    public void setItemLikeOnClickListener(ItemLikeOnClickListener itemLikeOnClickListener) {
        this.itemLikeOnClickListener = itemLikeOnClickListener;
    }

    public void setItemLikeOnClickListener2(ItemLikeOnClickListener2 itemLikeOnClickListener2) {
        this.itemLikeOnClickListener2 = itemLikeOnClickListener2;
    }

    public void setItemShareOnClickListener(ItemShareOnClickListener itemShareOnClickListener) {
        this.itemShareOnClickListener = itemShareOnClickListener;
    }


    public void setItemMyOnClickListener(ItemMyOnClickListener itemMyOnClickListener) {
        this.itemMyOnClickListener = itemMyOnClickListener;
    }

    public void setItemAttentionOnClick(ItemAttentionOnClick itemAttentionOnClick) {
        this.itemAttentionOnClick = itemAttentionOnClick;
    }

    public void setItemAttentionOnClick2(ItemAttentionOnClick2 itemAttentionOnClick2) {
        this.itemAttentionOnClick2 = itemAttentionOnClick2;
    }

}
