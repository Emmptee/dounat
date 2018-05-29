package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.utils.BindingUtils;

import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by hard on 2018/2/7.
 */

public class JointAdapter extends RecyclerView.Adapter<JointAdapter.ViewHolder> {

    private List<ParticularsResponse.MaterialVideoListBean> list;
    private Context mContext;
    private onSelectVideoItemOnClick onSelectVideoItemOnClick;
    public JointAdapter(List<ParticularsResponse.MaterialVideoListBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.joit_item, parent, false);
        final ViewHolder holder=new ViewHolder(view);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectVideoItemOnClick.onClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BindingUtils.loadImg(holder.img,list.get(position).getThumbnailUrl());
        holder.text.setText("PART "+(position+1));
        if(list.size()-1==position){
            holder.dian.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onSelectVideoItemOnClick{
        void onClick(int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private ImageView dian;
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.joint_video_img);
            dian=itemView.findViewById(R.id.dian);
            text=itemView.findViewById(R.id.joint_text);
        }
    }

    public void setOnSelectVideoItemOnClick(JointAdapter.onSelectVideoItemOnClick onSelectVideoItemOnClick) {
        this.onSelectVideoItemOnClick = onSelectVideoItemOnClick;
    }
}
