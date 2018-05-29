package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.utils.BindingUtils;

import java.util.List;

/**
 * Created by hard on 2018/2/9.
 */

public class MyCommentAllAdapter extends RecyclerView.Adapter<MyCommentAllAdapter.ViewHolder> {


    private Context mContext;
    private List<CommendAllResponse.CommentsListBean> list;

    public MyCommentAllAdapter(Context mContext, List<CommendAllResponse.CommentsListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext)
                .inflate(R.layout.commend_all_item, parent, false);
        final MyCommentAllAdapter.ViewHolder holder=new MyCommentAllAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BindingUtils.loadRoundImg(holder.img,list.get(position).getCommentatorUrl());
        holder.name.setText(list.get(position).getCommentatorName());
        holder.content.setText(list.get(position).getContent());
        holder.time.setText(list.get(position).getCreateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       private ImageView img;
       private TextView name;
       private TextView content;
       private TextView time;
        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.com_img);
            name=itemView.findViewById(R.id.com_name);
            content=itemView.findViewById(R.id.com_content);
            time=itemView.findViewById(R.id.com_time);
        }
    }
}
