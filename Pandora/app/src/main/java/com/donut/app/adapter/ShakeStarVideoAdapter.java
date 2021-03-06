package com.donut.app.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.shakestar.MaterialResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.ToastUtil;
import com.shuyu.gsyvideoplayer.video.CustomGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by hard on 2018/1/29.
 */

public class ShakeStarVideoAdapter extends RecyclerView.Adapter<ShakeStarVideoAdapter.Shake_starViewHolder> {


    private static final String TAG = "ShakeStarVideoAdapter";

    private  List<MyProductionResponse.MyShakingStarListBean> list;
    private  List<MyLikeResponse.MyLikeShakingStarListBean> list2;
    private  List<MaterialResponse.MyMaterialListBean> list3;
    private Context mContext;
    private ItemSelectOnClickListener itemSelectOnClickListener;
    private ItemLikeOnClickListener itemLikeOnClickListener;
    private ItemLikeOnClickListener2 itemLikeOnClickListener2;
    private ItemShareOnClickListener itemShareOnClickListener;
    private ItemShareOnClickListener2 itemShareOnClickListener2;
    private ItemMyOnClickListener itemMyOnClickListener;
    private ItemCommendOnClickListener itemCommendOnClickListener;
    private ItemCommendOnClickListener2 itemCommendOnClickListener2;
    private ItemInformOnClick itemInformOnClick;
    private ItemInformOnClick2 itemInformOnClick2;
    private ItemAttentionOnClick itemAttentionOnClick;
    private ItemAttentionOnClick2 itemAttentionOnClick2;
    private int position;
    private int videoPosition;
    private boolean login;
    private View view;
    private int status=0;

    public ShakeStarVideoAdapter(List<MyProductionResponse.MyShakingStarListBean> list, List<MyLikeResponse.MyLikeShakingStarListBean> list2, List<MaterialResponse.MyMaterialListBean> list3, Context mContext) {
        if(list!=null){
            this.list = list;
        }else if(list2!=null){
            this.list2 = list2;
        }else if(list3!=null){
            this.list3 = list3;
        }

        this.mContext = mContext;
    }

    @Override
    public Shake_starViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shake_commend_item,parent,false);
        this.view=view;
        final Shake_starViewHolder viewHolder = new Shake_starViewHolder(view);
        //点赞回调接口
        viewHolder.zan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=viewHolder.getAdapterPosition();
                if(list2!=null){
                    //MyLikeResponse.MyLikeShakingStarListBean myLikeShakingStarListBean = list2.get(position);
                    onLikeClick(list2.get(position),viewHolder);
                }
            }
        });
        //分享接口回调
        viewHolder.fenxiang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=viewHolder.getAdapterPosition();
                if(list!=null){
                    viewHolder.fenxiang_count.setText(list.get(position).getShareTimes()+1+"");
                    itemShareOnClickListener.ShareClick(position);
                }else if(list2!=null){
                    viewHolder.fenxiang_count.setText(list2.get(position).getShareTimes()+1+"");
                    itemShareOnClickListener2.ShareClick(position);
                }else if(list3!=null){

                }

            }
        });
        //素材详情接口回调
        viewHolder.sc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=viewHolder.getAdapterPosition();
                itemSelectOnClickListener.SelectClick(position);
            }
        });
        viewHolder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=viewHolder.getAdapterPosition();
                itemMyOnClickListener.MyOnClick(position);
            }
        });
        viewHolder.commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=viewHolder.getAdapterPosition();
                if(list!=null){
                    itemCommendOnClickListener.CommendClick(position,viewHolder.commend);
                }else if(list2!=null){
                    itemCommendOnClickListener2.CommendClick(position,viewHolder.commend);
                }else if(list3!=null){

                }

            }
        });
        viewHolder.inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewHolder.getAdapterPosition();
                if(list!=null) {
                    itemInformOnClick.onInformClick(list.get(position).getB02Id());
                }else if(list2!=null){
                    itemInformOnClick2.onInformClick(list2.get(position).getB02Id());
                }else{

                }
            }
        });
        //关注
        viewHolder.attention_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 关注被调用了");
                int position = viewHolder.getAdapterPosition();
                if(list!=null){
                    itemAttentionOnClick.Attention(list.get(position).getFkA01(),"1");
                }else{
                    itemAttentionOnClick2.Attention2(list2.get(position).getFkA01(),"1");
                }
                viewHolder.attention_user.setVisibility(View.GONE);
            }
        });
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(Shake_starViewHolder holder, int position) {

        if(list!=null){
            this.videoPosition=position;
//            if(list.get(position).getFollowStatus()!=1){
//                holder.attention_user.setVisibility(View.VISIBLE);
//            }else{
//                holder.attention_user.setVisibility(View.GONE);
//            }
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext).load(list.get(position).getVideoThumbnail()).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.video.setThumbImageView(imageView);
            holder.uname.setText(list.get(position).getNickName());//用户昵称
            holder.video.setUp(list.get(position).getPlayUrl(),false,null,null);//视频
            ImageView backButton = holder.video.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            BindingUtils.loadRoundImg(holder.head_img,list.get(position).getHeadPic());//用户头像
            holder.bz.setText(list.get(position).getMaterialTitle());//素材标题
            holder.content.setText(list.get(position).getContentDesc());//内容描述
            if(list.get(position).getBrowseTimes()>=10000){
                BigDecimal bigDecimal = new BigDecimal(list.get(position).getBrowseTimes());
                BigDecimal bigDecimal2 = new BigDecimal(10000);
                holder.player_count.setText(bigDecimal.divide(bigDecimal2,2, RoundingMode.HALF_UP)+"万次观看");//观看次数
            }else{
                holder.player_count.setText(list.get(position).getBrowseTimes()+"次观看");//观看次数
            }
//            BindingUtils.loadRoundImg(holder.sc_img,list.get(position).getMaterialThumbnail());
            if(list.get(position).getPraiseStatus()==1){//已点赞
                holder.zan_img.setImageResource(R.drawable.dianzan2);
            }
            boolean wifi = NetUtils.isWifi(mContext);
            if(wifi){
                GSYBaseVideoPlayer player=holder.video;
                //player.startPlayLogic();
            }
            holder.zan_count.setText(list.get(position).getPraiseTimes()+"");//点赞数量
            holder.pinglun_count.setText(list.get(position).getCommentTimes()+"");//评论数量
            holder.fenxiang_count.setText(list.get(position).getShareTimes()+"");//分享数量

        }else if(list2!=null){
            this.videoPosition=position;
//            if(list2.get(position).getFollowStatus()!=1){
//                holder.attention_user.setVisibility(View.VISIBLE);
//            }else{
//                holder.attention_user.setVisibility(View.GONE);
//            }
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext).load(list2.get(position).getVideoThumbnail()).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.video.setThumbImageView(imageView);
            holder.uname.setText(list2.get(position).getNickName());//用户昵称
            holder.video.setUp(list2.get(position).getPlayUrl(),false,null,null);//视频
            ImageView backButton = holder.video.getBackButton();
            backButton.setVisibility(View.GONE);//隐藏返回键
            BindingUtils.loadRoundImg(holder.head_img,list2.get(position).getHeadPic());//用户头像
            holder.bz.setText(list2.get(position).getMaterialTitle());//素材标题
            holder.content.setText(list2.get(position).getContentDesc());//内容描述
            if(list2.get(position).getBrowseTimes()>=10000){
                BigDecimal bigDecimal = new BigDecimal(list2.get(position).getBrowseTimes());
                BigDecimal bigDecimal2 = new BigDecimal(10000);
                holder.player_count.setText(bigDecimal.divide(bigDecimal2,2, RoundingMode.HALF_UP)+"万次观看");//观看次数
            }else{
                holder.player_count.setText(list2.get(position).getBrowseTimes()+"次观看");//观看次数
            }
//            BindingUtils.loadRoundImg(holder.sc_img,list2.get(position).getMaterialThumbnail());
            if(list2.get(position).getPraiseStatus()==1){//已点赞
                holder.zan_img.setImageResource(R.drawable.dianzan2);
            }
            boolean wifi = NetUtils.isWifi(mContext);
            if(wifi){
                GSYBaseVideoPlayer player=holder.video;
                //player.startPlayLogic();
            }
            holder.player_count.setText(list2.get(position).getBrowseTimes()+"次观看");//观看次数
            holder.zan_count.setText(list2.get(position).getPraiseTimes()+"");//点赞数量
            holder.pinglun_count.setText(list2.get(position).getCommentTimes()+"");//评论数量
            holder.fenxiang_count.setText(list2.get(position).getShareTimes()+"");//分享数量

        }else if(list3!=null){

        }

 }




    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }else if(list2!=null){
            return list2.size();
        }else{
            return list3.size();
        }
    }


    public class Shake_starViewHolder extends RecyclerView.ViewHolder{
        private StandardGSYVideoPlayer video;
        private AutoRelativeLayout user;
        private TextView bz;
        private ImageView head_img ;
        private ImageView attention_user ;
        private ImageView zan_img ;
        private ImageView inform ;
        private ImageView commend ;
        private ImageView fenxiang_img ;
        private TextView uname;
        private TextView content;
        private ImageView sc_img;
        private TextView player_count;
        private TextView zan_count;
        private TextView pinglun_count;
        private TextView fenxiang_count;

        public Shake_starViewHolder(View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.tj_video);
            bz = itemView.findViewById(R.id.bz);
            head_img = itemView.findViewById(R.id.head_img);
            attention_user = itemView.findViewById(R.id.attention_user);
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

    }
    public void onLikeClick(MyLikeResponse.MyLikeShakingStarListBean model, Shake_starViewHolder holder) {

 /*      if (model.getPraiseStatus() != 0) {
           model.setPraiseStatus(1);
           model.setPraiseTimes(model.getPraiseTimes() + 1);
           itemLikeOnClickListener.LikeClick(model,true);
       } else {
           model.setPraiseStatus(0);
           if(model.getPraiseTimes()>0){
               model.setPraiseTimes(model.getPraiseTimes() - 1);
           }
           itemLikeOnClickListener.LikeClick(model,false);
       }*/
       if(model.getPraiseStatus()==0){
           holder.zan_img.setImageResource(R.drawable.dianzan2);
           itemLikeOnClickListener.LikeClick(model,true);
           model.setPraiseTimes(model.getPraiseTimes() + 1);
           holder.zan_count.setText(list2.get(position).getPraiseTimes()+"");//点赞数量
           status=1;
       }else if(model.getPraiseStatus()==1){//取消收藏
           holder.zan_img.setImageResource(R.drawable.dianzan1);
           itemLikeOnClickListener.LikeClick(model,false);
           model.setPraiseTimes(model.getPraiseTimes() - 1);
           holder.zan_count.setText(list2.get(position).getPraiseTimes()+"");//点赞数量
           status=0;
       }
        L.e("TAG","待续……");
    }
    public void onLikeClick(MyProductionResponse.MyShakingStarListBean model2) {
//        if (!login) {
//            mListener.onToLogin();
//            return;
//        }

        if (model2.getPraiseStatus() == 0) {
            model2.setPraiseStatus(1);
            model2.setPraiseTimes(model2.getPraiseTimes() + 1);

            itemLikeOnClickListener2.LikeClick(model2,true);
        } else {
            model2.setPraiseStatus(0);
            if(model2.getPraiseTimes()>0){
                model2.setPraiseTimes(model2.getPraiseTimes() - 1);
            }
            itemLikeOnClickListener2.LikeClick(model2,false);
        }
        notifyDataSetChanged();
    }
    public int getPosition(){
        return videoPosition;
    }
    public View view(){
        return view;
    }

    public interface ItemAttentionOnClick{
        void Attention(String starId,String operation);
    }
    public interface ItemAttentionOnClick2{
        void Attention2(String starId,String operation);
    }

    public interface ItemInformOnClick{
        void onInformClick(String fkB02);
    }
    public interface ItemInformOnClick2{
        void onInformClick(String fkB02);
    }

    public interface ItemSelectOnClickListener{
        void SelectClick(int position);
    }

    public interface ItemMyOnClickListener{
        void MyOnClick(int position);
    }


    public interface ItemLikeOnClickListener{
        void LikeClick(MyLikeResponse.MyLikeShakingStarListBean myLikeShakingStarListBean, boolean like);
    }

    public interface ItemLikeOnClickListener2{
        void LikeClick(MyProductionResponse.MyShakingStarListBean model2, boolean like);
    }
    public interface ItemShareOnClickListener{
        void ShareClick(int position);
    }
    public interface ItemShareOnClickListener2{
        void ShareClick(int position);
    }
    public interface ItemCommendOnClickListener{
        void CommendClick(int position, ImageView img);
    }
    public interface ItemCommendOnClickListener2{
        void CommendClick(int position, ImageView img);
    }

    public void setItemInformOnClick(ItemInformOnClick itemInformOnClick) {
        this.itemInformOnClick = itemInformOnClick;
    }

    public void setItemAttentionOnClick(ItemAttentionOnClick itemAttentionOnClick) {
        this.itemAttentionOnClick = itemAttentionOnClick;
    }

    public void setItemAttentionOnClick2(ItemAttentionOnClick2 itemAttentionOnClick2) {
        this.itemAttentionOnClick2 = itemAttentionOnClick2;
    }

    public void setItemInformOnClick2(ItemInformOnClick2 itemInformOnClick2) {
        this.itemInformOnClick2 = itemInformOnClick2;
    }

    public void setItemShareOnClickListener2(ItemShareOnClickListener2 itemShareOnClickListener2) {
        this.itemShareOnClickListener2 = itemShareOnClickListener2;
    }

    public void setItemCommendOnClickListener2(ItemCommendOnClickListener2 itemCommendOnClickListener2) {
        this.itemCommendOnClickListener2 = itemCommendOnClickListener2;
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

    public ItemLikeOnClickListener getItemLikeOnClickListener() {
        return itemLikeOnClickListener;
    }
}
