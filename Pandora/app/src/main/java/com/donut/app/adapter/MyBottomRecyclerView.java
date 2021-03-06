package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.utils.BindingUtils;

import java.util.List;

/**
 * Created by hard on 2018/2/12.
 */

public class MyBottomRecyclerView extends RecyclerView.Adapter<MyBottomRecyclerView.ViewHolder> {

    private static final String TAG = "MyBottomRecyclerView";
    private Context mContext;
    private List<MyProductionResponse.MyShakingStarListBean> list;
    private List<MyLikeResponse.MyLikeShakingStarListBean> list2;
    private BottomItemOnClick bottomItemOnClick;

    public MyBottomRecyclerView(Context mContext, List<MyProductionResponse.MyShakingStarListBean> list,List<MyLikeResponse.MyLikeShakingStarListBean> list2) {
        this.mContext = mContext;
        Log.i(TAG, "MyBottomRecyclerView: list获取到的数据为" + list);
        Log.i(TAG, "MyLikeShakingStarListBean: list2获取到的数据为" + list2);
        if(list2!=null){
            this.list2 = list2;
        }
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item, parent, false);
        final MyBottomRecyclerView.ViewHolder holder=new MyBottomRecyclerView.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomItemOnClick.OnClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(list2!=null){
            BindingUtils.loadImg(holder.img,list2.get(position).getVideoThumbnail());
            holder.title.setText(list2.get(position).getMaterialTitle());
            holder.content.setText(list2.get(position).getContentDesc());
            if(list2.get(position).getBrowseTimes()>=10000){
                holder.count.setText(list2.get(position).getBrowseTimes()/10000+"W次观看");
            }else{
                holder.count.setText(list2.get(position).getBrowseTimes()+"次观看");
            }
        }else{
            BindingUtils.loadImg(holder.img,list.get(position).getVideoThumbnail());
            holder.title.setText(list.get(position).getMaterialTitle());
            holder.content.setText(list.get(position).getContentDesc());
            if(list.get(position).getBrowseTimes()>=10000){
                holder.count.setText(list.get(position).getBrowseTimes()/10000+"W次观看");
            }else{
                holder.count.setText(list.get(position).getBrowseTimes()+"次观看");
            }
        }


    }

    @Override
    public int getItemCount() {
        if(list2!=null){
            return list2.size();
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       private ImageView img;
       private TextView title;
       private TextView content;
       private TextView count;
        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.my_img);
            title=itemView.findViewById(R.id.my_title);
            content=itemView.findViewById(R.id.my_content);
            count=itemView.findViewById(R.id.my_playcount);
        }
    }

    public interface BottomItemOnClick{
        void OnClick(int position);
    }

    public void setBottomItemOnClick(BottomItemOnClick bottomItemOnClick) {
        this.bottomItemOnClick = bottomItemOnClick;
    }
}
