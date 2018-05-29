package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.MyAppreciatesResponse;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class StarChosenAdapter extends RecyclerView.Adapter<ViewHolder>
{

    public static interface OnRecyclerViewListener
    {
        void onItemClick(int position);
        void onCancle(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener)
    {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public OnRecyclerViewListener getOnRecyclerViewListener()
    {
        return onRecyclerViewListener;
    }


    private List<MyAppreciatesResponse.MyAppreciate> list;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;


    private boolean noMoreData;

    private Context mContext;

    public StarChosenAdapter(Context context, List<MyAppreciatesResponse.MyAppreciate> contentData)
    {
        this.mContext = context;
        this.list = contentData;
    }

    @Override
    public int getItemCount()
    {
        // 总条数+ 1（footerView）
        return list.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        // 最后一个item设置为footerView
            return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        if (viewHolder instanceof ItemViewHolder)
        {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final MyAppreciatesResponse.MyAppreciate item = list.get(position);
            holder.relaTitle.setText(item.getTitle());
            Glide.with(mContext)
                    .load(item.getThumbnailUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(holder.img);
            Glide.with(mContext)
                    .load(item.getUserHeadPic())
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.header);
            holder.name.setText(CommonUtils.setName(mContext,item.getUserName())+" | "+item.getChallengeTitle());
            holder.time.setText(item.getCreateTime());
            holder.content.setText(item.getDescription());
            if(item.getIsMember()!=null&&item.getIsMember()==1){
                holder.headerVip.setVisibility(View.VISIBLE);
            }else{
                holder.headerVip.setVisibility(View.GONE);
            }
            holder.cancle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onCancle(position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onItemClick(position);
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.chosen_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ItemViewHolder(view);
        }

        return null;
    }

    class ItemViewHolder extends ViewHolder
    {
        public ItemViewHolder(View view)
        {
            super(view);
            relaTitle = (TextView) view.findViewById(R.id.chosen_item_subject_title);
            cancle = (TextView) view.findViewById(R.id.chosen_item_cancle);
            img= (ImageView) view.findViewById(R.id.chosen_item_img);
            header= (ImageView) view.findViewById(R.id.chosen_item_head);
            name = (TextView) view.findViewById(R.id.chosen_item_title);
            time=(TextView) view.findViewById(R.id.chosen_item_time);
            content=(TextView) view.findViewById(R.id.chosen_item_content);
            headerVip=(ImageView) view.findViewById(R.id.chosen_item_head_v);

        }

        public TextView relaTitle,cancle;
        public ImageView img,header,headerVip;
        public TextView name,time,content;
    }

    class FooterViewHolder extends ViewHolder
    {
        public FooterViewHolder(View view)
        {
            super(view);

            text = (TextView) view.findViewById(R.id.footer_loadmore_text);
            pb = (ProgressBar) view.findViewById(R.id.footer_load_progress);
        }

        public TextView text;

        public ProgressBar pb;
    }

    public void setNoMoreData(boolean noMoreData)
    {
        this.noMoreData = noMoreData;
    }


}


