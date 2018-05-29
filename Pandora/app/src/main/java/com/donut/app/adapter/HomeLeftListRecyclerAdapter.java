package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donut.app.R;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.shakestar.SweetResponse;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.L;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by hard on 2018/2/28.
 */

public class HomeLeftListRecyclerAdapter extends RecyclerView.Adapter<HomeLeftListRecyclerAdapter.ViewHolder> {


    private Context mContext;
    private List<SubjectListDetail> list;
    private OnLeftItemOnClick onItemOnClick;
    private OnLeftItenShareClick onItenShareClick;
    private Double aDouble;

    public HomeLeftListRecyclerAdapter(Context mContext, List<SubjectListDetail> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转
                int position = holder.getAdapterPosition();
                onItemOnClick.OnClick(list.get(position).getChannelType(), list.get(position).getSubjectId());
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                onItenShareClick.OnShareClick(list.get(position).getChannelType(),
                        list.get(position).getStarName(),
                        list.get(position).getSubjectId(),
                        (int) list.get(position).getBrowseTimes(),
                        list.get(position).getDescription());


            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BindingUtils.loadImg(holder.img, list.get(position).getPublicPic());//明星相片
        holder.time.setText("一一" + list.get(position).getPeriod() + "期");//当前期数
        holder.name.setText(list.get(position).getStarName() + " 丨 ");
        holder.content.setText(list.get(position).getName());
        if (list.get(position).getBrowseTimes() >= 10000) {
            BigDecimal bigDecimal = new BigDecimal(list.get(position).getBrowseTimes());
            BigDecimal bigDecimal2 = new BigDecimal(10000);
            holder.lookcount.setText(bigDecimal.divide(bigDecimal2, 2, RoundingMode.HALF_UP) + "W次观看");
        } else {
            holder.lookcount.setText(list.get(position).getBrowseTimes() + "次观看");
        }
//        Glide.with(mContext).load(R.drawable.list_guide).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.guide);
//        if(list.size()-1==position){
//            holder.guide.setVisibility(View.GONE);
//        }else{
//            holder.guide.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private ImageView guide;
        private TextView time;
        private TextView name;
        private TextView content;
        private TextView lookcount;
        private ImageView share;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.home_item_img);
            guide = itemView.findViewById(R.id.item_guide);
            time = itemView.findViewById(R.id.home_item_time);
            name = itemView.findViewById(R.id.home_item_name);
            content = itemView.findViewById(R.id.home_item_content);
            lookcount = itemView.findViewById(R.id.home_item_lookcount);
            share = itemView.findViewById(R.id.home_item_fx);
        }
    }

    public interface OnLeftItemOnClick {
        void OnClick(int channelType, String subjectId);
    }

    public interface OnLeftItenShareClick {
        void OnShareClick(int type, String name, String subjectId, int browseTimes, String description);
    }

    public void setOnItemOnClick(OnLeftItemOnClick onItemOnClick) {
        this.onItemOnClick = onItemOnClick;
    }

    public void setOnItenShareClick(OnLeftItenShareClick onItenShareClick) {
        this.onItenShareClick = onItenShareClick;
    }
}