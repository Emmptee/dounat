package com.donut.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.utils.BindingUtils;
import com.shuyu.gsyvideoplayer.video.CustomGSYVideoPlayer;

import java.util.List;

/**
 * Created by hard on 2018/1/29.
 */

public class ShakeStarSelectAdapter extends RecyclerView.Adapter<ShakeStarSelectAdapter.Shake_starViewHolder> {

    private List<ShakeStarSelectResponse.MaterialListBean> list;
    private Context mContext;
    private ItemParticularsOnClickListener itemParticularsOnClickListener;
    private static final int TYPEA=0;//同屏
    private static final int TYPEB=1;//分屏
    public ShakeStarSelectAdapter(List<ShakeStarSelectResponse.MaterialListBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }
    @Override
    public Shake_starViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_list_item, parent, false);
        final Shake_starViewHolder viewHolder = new Shake_starViewHolder(view);
        //选择素材按钮点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemParticularsOnClickListener.ParticularsClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Shake_starViewHolder holder, int position) {

        holder.uname.setText(list.get(position).getStarName());//名字
        if(list.get(position).getUseTimes()>=10000){
            holder.make_count.setText("制作"+list.get(position).getUseTimes()/10000+"W次");//制作次数
        }
        holder.make_count.setText("制作"+list.get(position).getUseTimes()+"次");//制作次数
        holder.title.setText(list.get(position).getMaterialTitle());//标题
        if(list.get(position).getMaterialThumbnail()!=null){
            Glide.with(mContext).load(list.get(position).getMaterialThumbnail()).into(holder.video_img);//视频图片
        }
        BindingUtils.loadRoundImg(holder.head_img,list.get(position).getMaterialThumbnail());//头像
        if(list.get(position).getType()==TYPEA){
           holder.type.setImageResource(R.drawable.tongping);
        }else if(list.get(position).getType()==TYPEB){
            holder.type.setImageResource(R.drawable.fenping);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Shake_starViewHolder extends RecyclerView.ViewHolder{
        private ImageView video_img ;
        private ImageView head_img ;
        private TextView uname;
        private TextView make_count;
        private TextView title;
        private ImageView type;

        public Shake_starViewHolder(View itemView) {
            super(itemView);
            video_img=itemView.findViewById(R.id.select_item_uimg);
            head_img=itemView.findViewById(R.id.user_img);
            uname=itemView.findViewById(R.id.uset_name);
            make_count= itemView.findViewById(R.id.make_count);
            type=itemView.findViewById(R.id.select_item_type);
            title=itemView.findViewById(R.id.select_item_title);
        }

    }

    public interface ItemParticularsOnClickListener{
        void ParticularsClick(int position);
    }

    public void setItemParticularsOnClickListener(ItemParticularsOnClickListener itemParticularsOnClickListener) {
        this.itemParticularsOnClickListener = itemParticularsOnClickListener;
    }
}
