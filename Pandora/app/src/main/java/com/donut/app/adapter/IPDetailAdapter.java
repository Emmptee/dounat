package com.donut.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.IpDetailMyDetail;
import com.donut.app.model.video.DensityUtil;

import java.util.List;

/**
 * Created by wujiaojiao on 2016/6/3.
 */
public class IPDetailAdapter extends BaseAdapter
{
    private List<IpDetailMyDetail> list;

    private Context mContext;

    public IPDetailAdapter(Context context, List<IpDetailMyDetail>
            contentData)
    {
        this.mContext=context;
        this.list = contentData;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //定义一个ImageView,显示在GridView里
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.image_item, null);
            holder.img = (ImageView) convertView
                    .findViewById(R.id.item_img);
            float scale = mContext.getResources().getDisplayMetrics().density;
            int width = (int)(DensityUtil.getWidthInPx(mContext)-(40 * scale + 0.5f))/4;
            LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(width, width);
            holder.img.setLayoutParams(param0);
            convertView.setTag(holder);

        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext)
                .load(list.get(position).getImgUrl())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(holder.img);
        return convertView;
    }

    public final class ViewHolder
    {
        public ImageView img;
    }
}
