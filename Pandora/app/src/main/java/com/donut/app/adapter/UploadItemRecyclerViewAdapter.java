package com.donut.app.adapter;

import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.message.ChallengeRequest;
import com.donut.app.http.message.IpAddRequest;
import com.donut.app.http.message.StarNoticeAddRequest;
import com.donut.app.http.message.wish.WishCompletedRequest;
import com.donut.app.model.upload.UploadManager;
import com.donut.app.service.UploadService;
import com.donut.app.utils.FormatCheckUtil;
import com.donut.app.utils.JsonUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 */
public class UploadItemRecyclerViewAdapter extends RecyclerView.Adapter {

    public interface OnItemListener {
        void OnItemClick(boolean fail, String fileUrl);

        void OnRefresh(int adapterPosition);

        void OnRefreshLoading(ItemViewHolder itemViewHolder);

        void OnEmptyList();
    }

    private final List<UploadInfo> uploadInfoList = new ArrayList<>();

    private OnItemListener mListener;


    public UploadItemRecyclerViewAdapter(OnItemListener listener) {
        loadData();
        mListener = listener;
    }

    public void loadData() {
        uploadInfoList.clear();

        Map<String, UploadInfo> uploadInfoMap
                = UploadService.getUploadManager().getUploadInfoMap();

        for (String key : uploadInfoMap.keySet()) {
            uploadInfoList.add(uploadInfoMap.get(key));
        }
    }

    public void updateItemData(int adapterPosition) {
        UploadInfo uploadInfo = uploadInfoList.get(adapterPosition);
        if (uploadInfo == null) {
            return;
        }
        UploadInfo info = null;
        try {
            info = SysApplication.getDb().findById(UploadInfo.class, uploadInfo.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (info == null) {
            try {
                uploadInfoList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                if (uploadInfoList.size() <= 0) {
                    mListener.OnEmptyList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        uploadInfoList.set(adapterPosition, info);
        notifyItemChanged(adapterPosition);
    }

    public void updateItemDataForProgress(ItemViewHolder itemViewHolder) {

        int position = itemViewHolder.getAdapterPosition();
        UploadInfo uploadInfo = uploadInfoList.get(position);
        if (uploadInfo == null) {
            return;
        }
        UploadInfo info = null;
        try {
            info = SysApplication.getDb().findById(UploadInfo.class, uploadInfo.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (info == null) {
            try {
                uploadInfoList.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        uploadInfoList.set(position, info);

        itemViewHolder.refresh(info);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upload_manager_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ItemViewHolder item = (ItemViewHolder) holder;
        UploadInfo uploadInfo = uploadInfoList.get(position);

        String thumbnailUrl = null, title = null;
        final String outFilePath = uploadInfo.getOutFilePath();

        ChallengeRequest challengeRequest;
        IpAddRequest ipAddRequest;
        if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.IP_SEND.getType()) {
            // 创意征集
            ipAddRequest = JsonUtils.fromJson(uploadInfo.getSaveData(), IpAddRequest.class);
            if (ipAddRequest == null) {
                return;
            }
            thumbnailUrl = ipAddRequest.getImgUrl();
            title = "发起心愿/" + uploadInfo.getSaveTitle();
        } else if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.CHALLENGE.getType()) {
            // 发起挑战
            challengeRequest = JsonUtils.fromJson(uploadInfo.getSaveData(), ChallengeRequest.class);
            if (challengeRequest == null) {
                return;
            }
            thumbnailUrl = challengeRequest.getThumbnailUrl();
            title = "发起挑战/" + uploadInfo.getSaveTitle();
        } else if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.STAR_REPLY.getType()) {
            // 回复心愿
            WishCompletedRequest request
                    = JsonUtils.fromJson(uploadInfo.getSaveData(), WishCompletedRequest.class);
            if (request == null) {
                return;
            }
            thumbnailUrl = request.getAchievePicUrl();
            title = "回复心愿";
        } else if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.STAR_SEND_NOTICE.getType()) {
            // 回复心愿
            StarNoticeAddRequest request
                    = JsonUtils.fromJson(uploadInfo.getSaveData(), StarNoticeAddRequest.class);
            if (request == null) {
                return;
            }
            thumbnailUrl = request.getThumbnail();
            title = "发布通告";
        }

        Glide.with(item.ivThumbnail.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(item.ivThumbnail);

        item.tvTitle.setText(title);

        item.refresh(uploadInfo);

        final boolean fail = uploadInfo.getState() == 4 || uploadInfo.getState() == 5;
        item.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnItemClick(fail, outFilePath);
            }
        });

        initCallBack(uploadInfo, item);

    }

    private void initCallBack(UploadInfo uploadInfo, ItemViewHolder holder) {
//        if (uploadInfo.getBaseCallBack() == null)
        uploadInfo.setBaseCallBack(new PriRequestListener(holder));
        UploadService.getUploadManager().setUploadInfo(uploadInfo);
    }

    @Override
    public int getItemCount() {
        return uploadInfoList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivThumbnail;
        final TextView tvTitle, tvLength, tvProgress, tvMsg;
        final ProgressBar pb;
        final View msgLayout;

        public ItemViewHolder(View view) {
            super(view);
            ivThumbnail = (ImageView) view.findViewById(R.id.upload_item_iv_thumbnail);
            tvTitle = (TextView) view.findViewById(R.id.upload_item_tv_title);
            tvLength = (TextView) view.findViewById(R.id.upload_item_tv_length);
            pb = (ProgressBar) view.findViewById(R.id.upload_item_pb);
            tvProgress = (TextView) view.findViewById(R.id.upload_item_progress);
            msgLayout = view.findViewById(R.id.upload_item_layout_msg);
            tvMsg = (TextView) view.findViewById(R.id.upload_item_tv_msg);
        }

        void refresh(UploadInfo uploadInfo) {
            if (uploadInfo == null) {
                return;
            }

            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(uploadInfo.getFilePath());
                String strTime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                time = Integer.valueOf(strTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long fileLength = uploadInfo.getFileLength();
            mProgress = uploadInfo.getProgress();

            tvLength.setText(FormatCheckUtil.getDataSize(fileLength));

            if (uploadInfo.getState() == 2 && fileLength > 0) {
                pb.setProgress((int) mProgress);
                tvProgress.setText(mProgress + "%");
            } else if (uploadInfo.getState() == 1
                    && mProgress < 48) {
                pb.setProgress((int) mProgress);
                tvProgress.setText(mProgress + "%");

                Message message = new Message();
                message.obj = uploadInfo.getOutFilePath();
                mHandler.sendMessageDelayed(message, 3000);
            } else {
                pb.setProgress(0);
                tvProgress.setText("0%");
            }

            switch (uploadInfo.getState()) {
                case 0:
                    msgLayout.setVisibility(View.VISIBLE);
                    tvMsg.setText("正在排队...");
                    break;
                case 1:
                    msgLayout.setVisibility(View.VISIBLE);
                    tvMsg.setText("正在压缩...");
                    break;
                case 2:
                    msgLayout.setVisibility(View.VISIBLE);
                    tvMsg.setText("正在上传...");
                    tvLength.setText(setTvLength(mProgress, fileLength));
                    break;
                case 4:
                    msgLayout.setVisibility(View.VISIBLE);
                    tvMsg.setText("发起失败\n请重新发起");
                    break;
                case 5:
                    msgLayout.setVisibility(View.VISIBLE);
                    tvMsg.setText("已取消\n请重新发起");
                    break;
                default:
                    msgLayout.setVisibility(View.GONE);
                    tvLength.setText(setTvLength(mProgress, fileLength));
                    break;
            }

        }

        int time = 0;
        long mProgress = 0;
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (time == 0) {
                    time = 100000;
                }

                if (mProgress < 49) {
                    mProgress += 1;

                    try {
                        String outFilePath = (String) msg.obj;
                        UploadManager uploadManager = UploadService.getUploadManager();
                        UploadInfo uploadInfo = uploadManager.getUploadInfo(outFilePath);
                        if (uploadInfo == null) {
                            return;
                        }
                        uploadInfo.setProgress(mProgress);
                        uploadManager.setUploadInfo(uploadInfo);

                        SysApplication.getDb().update(uploadInfo);
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage(), e);
                    }

                    Message message = new Message();
                    message.obj = msg.obj;
                    mHandler.sendMessageDelayed(message,
                            new Random().nextInt(time / 50));
                }
                pb.setProgress((int) mProgress);
                tvProgress.setText(mProgress + "%");

                super.handleMessage(msg);
            }
        };
    }

    private static String setTvLength(long progress, long fileLength) {
        long nowLength = fileLength * progress / 100L;
        return FormatCheckUtil.getDataSize(nowLength)
                + "/"
                + FormatCheckUtil.getDataSize(fileLength);
    }

    private class PriRequestListener implements RequestManager.RequestListener {
        final ItemViewHolder itemViewHolder;

        private PriRequestListener(ItemViewHolder holder) {
            this.itemViewHolder = holder;
        }

        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long total, long count, String filePath) {
//            itemViewHolder.refresh(false);
            if (mListener != null) {
                mListener.OnRefreshLoading(itemViewHolder);
            }

        }

        @Override
        public void onSuccess(String s, Map<String, String> map, String s1, int i) {
            //itemViewHolder.refresh(false);
            try {
                UploadInfo uploadInfo = uploadInfoList.get(itemViewHolder.getAdapterPosition());
                uploadInfo.setState(3);
                if (mListener != null) {
                    mListener.OnRefresh(itemViewHolder.getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String s, String s1, int i) {
//            itemViewHolder.refresh(false);
            try {
                UploadInfo uploadInfo = uploadInfoList.get(itemViewHolder.getAdapterPosition());
                uploadInfo.setState(4);
                if (mListener != null) {
                    mListener.OnRefresh(itemViewHolder.getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}