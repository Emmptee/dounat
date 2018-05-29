package com.donut.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.shakestar.Channel;
import com.donut.app.http.message.shakestar.SweetResponse;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.L;
import com.umeng.qq.tencent.l;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by hard on 2018/2/28.
 */

public class HomeLeftAdapter  extends  RecyclerView.Adapter<HomeLeftAdapter.ViewHolder>{

    private Context mContext;
    private List<Channel.MenuListBean> list;
    private onItemClick onItemClick;
    public HomeLeftAdapter(Context mContext, List<Channel.MenuListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.left_item, parent, false);
        final HomeLeftAdapter.ViewHolder holder=new HomeLeftAdapter.ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onItemClick.OnClick( holder.getAdapterPosition(),list.get(holder.getAdapterPosition()));

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.title.setText(list.get(position).getTitle());
       holder.content.setText(list.get(position).getDescription());
       BindingUtils.loadImg(holder.image,list.get(position).getLogoUrl());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        private ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.left_item_title);
            content=itemView.findViewById(R.id.left_item_content);
            image = itemView.findViewById(R.id.left_item_image);
        }
    }

    public interface onItemClick{
     void OnClick(int position,Channel.MenuListBean list);
    }

    public void setOnItemClick(HomeLeftAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
