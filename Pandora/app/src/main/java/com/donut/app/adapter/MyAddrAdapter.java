package com.donut.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.DeliveryAddress;
import com.donut.app.utils.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class MyAddrAdapter extends RecyclerView.Adapter<ViewHolder>
{

    public static interface OnRecyclerViewListener
    {
        void onEdit(int position);
        void onDel(int position);
        void onDefaultSet(int position);

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

    private View footView;

    private List<DeliveryAddress> addressesList;

    private static final int TYPE_ITEM = 0;

    private static final int TYPE_FOOTER = 1;


    private boolean noMoreData;

    private Context mContext;

    public MyAddrAdapter(Context context, List<DeliveryAddress> contentData, View footView)
    {
        this.mContext = context;
        this.addressesList = contentData;
        this.footView = footView;
    }

    @Override
    public int getItemCount()
    {
        // 总条数+ 1（footerView）
        return addressesList.size() + 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount())
        {
            return TYPE_FOOTER;

        } else
        {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        if (viewHolder instanceof ItemViewHolder)
        {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final DeliveryAddress item = addressesList.get(position);
            holder.name.setText("收货人："+ CommonUtils.setName(mContext,item.getConsignee()));
            holder.phone.setText(item.getPhone());
            holder.addr.setText("收货地址："+item.getAddress());
            if(item.getIsDefault()!=null){
                if(item.getIsDefault()==0){
                    holder.defauleCb.setChecked(false);
                    holder.defauleCb.setText(mContext.getString(R.string.default_addr_set));
                }else{
                    holder.defauleCb.setChecked(true);
                    holder.defauleCb.setText("默认地址");
                }
            }else{
                holder.defauleCb.setChecked(false);
                holder.defauleCb.setText(mContext.getString(R.string.default_addr_set));
            }
            holder.defauleCb.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(item.getIsDefault()==0){
                        onRecyclerViewListener.onDefaultSet(position);
                    }else{
                        holder.defauleCb.setChecked(true);
                    }
                }
            });

            holder.addrEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onEdit(position);
                }
            });

            holder.addrDel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onRecyclerViewListener.onDel(position);
                }
            });
        }else if(viewHolder instanceof FooterViewHolder)
        {
            FooterViewHolder holder = (FooterViewHolder) viewHolder;
            if (noMoreData)
            {
                holder.pb.setVisibility(View.GONE);
                holder.text.setText("没有更多数据");
                setNoMoreData(false);
            } else
            {
                holder.pb.setVisibility(View.VISIBLE);
                holder.text.setText("加载中...");
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_my_address, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ItemViewHolder(view);
        }
        // type == TYPE_FOOTER 返回footerView
        else if (viewType == TYPE_FOOTER)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footView.setLayoutParams(lp);
            return new FooterViewHolder(footView);
        }
        return null;
    }

    class ItemViewHolder extends ViewHolder
    {
        public ItemViewHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.my_nickname);
            phone = (TextView) view.findViewById(R.id.my_phone);
            addr=(TextView) view.findViewById(R.id.my_address);
            defauleCb=(CheckBox) view.findViewById(R.id.addr_default);
            addrEdit=(TextView) view.findViewById(R.id.addr_edit);
            addrDel=(TextView) view.findViewById(R.id.addr_del);
        }

        public TextView name;
        public TextView phone,addr;
        public CheckBox defauleCb;
        public TextView addrEdit,addrDel;

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

    class HeaderViewHolder extends ViewHolder
    {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }


    public void setNoMoreData(boolean noMoreData)
    {
        this.noMoreData = noMoreData;
    }

    public boolean getNoMoreData()
    {
        return noMoreData;
    }

}


