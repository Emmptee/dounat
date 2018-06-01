package com.donut.app.model.galleypick.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.model.galleypick.entities.FolderEntity;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter {

    public interface OnFolderItemClickListener {
        void onFolderItemClick(View v, String nowFolderName);
    }

    private Context mContext;

    private List<FolderEntity> mItems;

    private OnFolderItemClickListener onItemClickListener;

    private String nowFolderName = "";

    public FolderAdapter(Context mContext, List<FolderEntity> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    public void setOnItemClickListener(OnFolderItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_folder_item, parent, false);

        return new FolderHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemClick(holder, position);
        ((FolderHolder) holder).setData(position);
    }

    private void setItemClick(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    nowFolderName = mItems.get(position).getFolderName();
                    onItemClickListener.onFolderItemClick(v, nowFolderName);
                }
            }
        });
    }

    public class FolderHolder extends RecyclerView.ViewHolder {
        private ImageView imgContent;
        private TextView tvFileName;
        private TextView tvFileCount;
        private ImageView imgSelected;

        public FolderHolder(View itemView) {
            super(itemView);
            imgContent = (ImageView) itemView.findViewById(R.id.img_first);
            imgSelected = (ImageView) itemView.findViewById(R.id.img_selected);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            tvFileCount = (TextView) itemView.findViewById(R.id.tv_file_count);
        }

        public void setData(int position) {
            FolderEntity item = mItems.get(position);
            Glide.with(mContext)
                    .load(item.getFirstPath())
                    .placeholder(R.drawable.galley_img_loading)
                    .error(R.drawable.default_bg)
                    .into(imgContent);
            tvFileName.setText(item.getFolderName());
            tvFileCount.setText(item.getCount() + "张");

            if (nowFolderName.equals(item.getFolderName())) {
                imgSelected.setVisibility(View.VISIBLE);
            } else {
                imgSelected.setVisibility(View.GONE);
            }
        }
    }

}
