package com.donut.app.model.galleypick.views;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.donut.app.R;
import com.donut.app.model.galleypick.adapter.FolderAdapter;
import com.donut.app.model.galleypick.entities.FolderEntity;

import java.util.ArrayList;
import java.util.List;

public class FolderDialog extends Dialog implements FolderAdapter.OnFolderItemClickListener {
    private RecyclerView rlFolder;
    private List<FolderEntity> mFolders;
    private FolderAdapter mAdapter;
    private OnFolderSelectedListener onFolderSelectedListener;

    public FolderDialog(Context context, List<FolderEntity> folders) {
        super(context, R.style.Theme_dialog2);
        setContentView(R.layout.gallery_folder);
        setImageFolders(folders);
        layoutDialog(context);
    }

    private void layoutDialog(Context context) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        getWindow().setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.x = 0;
        lp.y = (int) (96 * scale + 0.5f);
        lp.width = metrics.widthPixels;
        //这个高度包括了状态栏,所以还要多减去24dp
        lp.height = (int) (metrics.heightPixels - (168 * scale + 0.5f));
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlFolder = (RecyclerView) getWindow().findViewById(R.id.rl_folder);
        rlFolder.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FolderAdapter(getContext(), mFolders);
        rlFolder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    public void setOnFolderSelectedListener(OnFolderSelectedListener onFolderSelectedListener) {
        this.onFolderSelectedListener = onFolderSelectedListener;
    }

    public void setImageFolders(List<FolderEntity> folders) {
        if (mFolders == null) {
            mFolders = new ArrayList<>();
        } else {
            mFolders.clear();
        }
        mFolders.addAll(folders);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFolderItemClick(View v, String nowFolderName) {
        if (onFolderSelectedListener != null) {
            onFolderSelectedListener.onFolderSelected(v, nowFolderName);
        }
        mAdapter.notifyDataSetChanged();
        dismiss();
    }

    public interface OnFolderSelectedListener {
        void onFolderSelected(View v, String nowFolderName);
    }

}
