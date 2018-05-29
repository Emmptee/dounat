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
import com.donut.app.http.message.shakestar.MaterialResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.utils.BindingUtils;

import java.util.List;

/**
 * Created by hard on 2018/2/12.
 */

public class MyBottomMaterialAdapter extends RecyclerView.Adapter<MyBottomMaterialAdapter.ViewHolder> {

    private static final String TAG = "MyBottomMaterialAdapter";
    private Context mContext;
    private List<MaterialResponse.MyMaterialListBean> list;
    private BottomItemOnClick bottomItemOnClick;

    public MyBottomMaterialAdapter(Context mContext, List<MaterialResponse.MyMaterialListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item, parent, false);
        final MyBottomMaterialAdapter.ViewHolder holder=new MyBottomMaterialAdapter.ViewHolder(view);
        //点击事件
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
        BindingUtils.loadImg(holder.img,list.get(position).getMaterialThumbnail());
//            holder.title.setText(list.get(position).getMaterialTitle());
        holder.content.setText(list.get(position).getMaterialTitle());
        if(list.get(position).getUseTimes()>=10000){
            holder.count.setText(list.get(position).getUseTimes()/10000+"W次使用");
        }else{
            holder.count.setText("制作" + list.get(position).getUseTimes()+"次");
        }
    }

    @Override
    public int getItemCount() {
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
