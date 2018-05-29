package com.donut.app.utils;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import com.donut.app.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * UpdateAppUtil
 * Created by Qi on 2017/3/8.
 */

public class UpdateAppUtil {

    public enum BtnType {
        /**
         * 下载,下次更新,去应用商店,后台下载,取消,安装
         */
        DOWNLOAD, DELAY, STORE, BACKGROUND, CANCEL, INSTALL
    }

    public interface OnBtnClickListener {
        void onBtnClick(boolean force, BtnType type);
    }

    private UpdateAppUtil.Builder builder;

    public static class Builder {

        private Context context;

        private String url;

        private String title;

        private String description;

        private boolean force;

        private String appName;

        private String fileName;

        private long fileSize;

        private String savePath;

        private String versionName;

        private OnBtnClickListener mListener;

        public Builder(final Context context, final String url) {
            this.context = context;
            this.url = url;
        }

        public UpdateAppUtil.Builder title(final String title) {
            this.title = title;
            return this;
        }

        public UpdateAppUtil.Builder description(final String description) {
            this.description = description;
            return this;
        }

        public UpdateAppUtil.Builder force(final boolean force) {
            this.force = force;
            return this;
        }

        public UpdateAppUtil.Builder appName(final String appName) {
            this.appName = appName;
            return this;
        }

        public UpdateAppUtil.Builder fileName(final String fileName) {
            this.fileName = fileName;
            return this;
        }

        public UpdateAppUtil.Builder fileSize(final long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public UpdateAppUtil.Builder savePath(final String savePath) {
            this.savePath = savePath;
            return this;
        }

        public UpdateAppUtil.Builder versionName(final String versionName) {
            this.versionName = versionName;
            return this;
        }

        public UpdateAppUtil.Builder setListener(final OnBtnClickListener mListener) {
            this.mListener = mListener;
            return this;
        }

        public UpdateAppUtil build() {
            return new UpdateAppUtil(this);
        }
    }

    private UpdateAppUtil(final Builder builder) {
        this.builder = builder;
    }

    public void update() {

        StringBuilder msgBuilder = new StringBuilder();
        if (builder.title != null) {
            msgBuilder.append(builder.title).append("\n").append("\n");
        } else {
            msgBuilder.append("检测到新版本").append("\n").append("\n");
        }
//        msgBuilder.append("当前版本号 V")
//                .append("1.0.1.1")
//                .append("\n");
//        if (builder.versionName != null) {
//            msgBuilder.append("最新版本号 V")
//                    .append(builder.versionName)
//                    .append("\n");
//        }
//        if (builder.fileSize > 0) {
//            msgBuilder.append("版本大小 ")
//                    .append(getDataSize(builder.fileSize))
//                    .append("\n");
//        }
        if (builder.description != null) {
            msgBuilder.append(builder.description);
        }

        AlertDialog.Builder pointDialogBuilder = new AlertDialog.Builder(builder.context, R.style.Theme_UpdateAppDialog)
                .setCancelable(false)
                .setMessage(msgBuilder.toString())
//                .setNeutralButton("下载安装",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (builder.mListener != null) {
//                                    builder.mListener.onBtnClick(builder.force, BtnType.DOWNLOAD);
//                                }
//                                chooseDialog();
//                            }
//                        })
                .setPositiveButton("去应用商店", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (builder.mListener != null) {
                            builder.mListener.onBtnClick(builder.force, BtnType.STORE);
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + builder.context.getPackageName()));
                        builder.context.startActivity(intent);
                    }
                });
        if (!builder.force) {
            pointDialogBuilder.setNegativeButton("下次提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (builder.mListener != null) {
                        builder.mListener.onBtnClick(builder.force, BtnType.DELAY);
                    }
                }
            });
        }

        pointDialogBuilder.create().show();
    }

    private DownloadManager downloadManager;

    private void chooseDialog() {
        boolean downSuccess = false;
        downloadManager
                = (DownloadManager) builder.context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = downloadManager.query(query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL));
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                if (url != null && url.equals(builder.url)) {
                    Uri apkUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                    showInstallDialog(apkUri);
                    downSuccess = true;
                    break;
                }
            }
            cursor.close();
        }
        if (!downSuccess) {
            downloadApp();
        }
    }

    private Dialog progressDialog;
    private ProgressBar progressBar;
    private Timer timer;
    private Handler handler;

    private void downloadApp() {
        final long id = downloadManager.enqueue(getRequest());

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int pro = bundle.getInt("pro");
                if (progressBar != null) {
                    progressBar.setProgress(pro);
                }
            }
        };

        showProgressDialog(id);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                long[] references = intent.getLongArrayExtra(extraID);
                for (long reference : references) {
                    if (reference == id) {
                        showProgressDialog(id);
                    }
                }
            }
        };
        builder.context.getApplicationContext().registerReceiver(receiver, filter);
    }

    private void showProgressDialog(final long id) {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    queryProgress(id);
                }
            }, 0, 1000);
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(builder.context, R.style.Theme_UpdateAppDialog)
                .setCancelable(false)
                .setView(R.layout.upload_progress_layout);
        if (!builder.force) {
            dialogBuilder
                    .setPositiveButton("后台下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (builder.mListener != null) {
                                builder.mListener.onBtnClick(builder.force, BtnType.BACKGROUND);
                            }
                        }
                    })
                    .setNegativeButton("取消下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (builder.mListener != null) {
                                builder.mListener.onBtnClick(builder.force, BtnType.CANCEL);
                            }
                            try {
                                downloadManager.remove(id);
                                timer.cancel();
                                timer = null;
                                builder.context = null;
                                handler = null;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        progressDialog = dialogBuilder.create();
        progressDialog.show();
        progressBar = (ProgressBar) progressDialog.findViewById(R.id.upload_progress);
    }

    private void queryProgress(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = downloadManager.query(query.setFilterById(id));
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    == DownloadManager.STATUS_SUCCESSFUL) {
                try {
                    timer.cancel();
                    progressDialog.dismiss();
                    timer = null;
                    builder.context = null;
                    handler = null;
                    cursor.close();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int pro = (bytes_downloaded * 100) / bytes_total;
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt("pro", pro);
            msg.setData(bundle);
            handler.sendMessage(msg);
            cursor.close();
        }
    }

    private DownloadManager.Request getRequest() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(builder.url));
        request.setTitle(builder.appName == null
                ? builder.context.getString(R.string.app_name) : builder.appName);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        request.setMimeType("application/vnd.android.package-archive");
        String savePath = builder.savePath;
        if (savePath == null || "".equals(savePath)) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            savePath = Environment.DIRECTORY_DOWNLOADS;
        }
        String fileName = builder.fileName;
        if (fileName == null || "".equals(fileName)) {
            fileName = builder.context.getPackageName() + ".apk";
        }
        request.setDestinationInExternalPublicDir(savePath, fileName);
        return request;
    }

    private void showInstallDialog(final Uri apkUri) {
        StringBuilder msgBuilder = new StringBuilder();
        if (builder.title != null) {
            msgBuilder.append(builder.title).append("\n").append("\n");
        } else {
            msgBuilder.append("检测到新版本").append("\n").append("\n");
        }
//        msgBuilder.append("当前版本号 V")
//                .append("1.0.1.1")
//                .append("\n");
//        if (builder.versionName != null) {
//            msgBuilder.append("最新版本号 V")
//                    .append(builder.versionName)
//                    .append("\n");
//        }
//        if (builder.fileSize > 0) {
//            msgBuilder.append("版本大小 ")
//                    .append(getDataSize(builder.fileSize))
//                    .append("\n");
//        }
        if (builder.description != null) {
            msgBuilder.append(builder.description);
        }

        AlertDialog.Builder installDialogBuilder = new AlertDialog.Builder(builder.context, R.style.Theme_UpdateAppDialog)
                .setCancelable(false)
                .setMessage(msgBuilder.toString())
                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (builder.mListener != null) {
                            builder.mListener.onBtnClick(builder.force, BtnType.INSTALL);
                        }
                        installApk(apkUri);
                    }
                });
        if (!builder.force) {
            installDialogBuilder
                    .setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (builder.mListener != null) {
                                builder.mListener.onBtnClick(builder.force, BtnType.DELAY);
                            }
                        }
                    });
        }
        installDialogBuilder.create().show();
    }

    private void installApk(final Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri apkUri = uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(builder.context, builder.context.getPackageName() + ".provider",
                    new File(uri.getPath()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        builder.context.startActivity(intent);
    }

}
