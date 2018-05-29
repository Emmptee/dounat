package com.donut.app.model.galleypick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.decoration.DividerGridItemDecoration;
import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.model.galleypick.adapter.GallerySelectorAdapter;
import com.donut.app.model.galleypick.entities.FolderEntity;
import com.donut.app.model.galleypick.entities.MediaEntity;
import com.donut.app.model.galleypick.utils.FindAllUtil;
import com.donut.app.model.galleypick.views.FolderDialog;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class GallerySelectorActivity extends AppCompatActivity
        implements GallerySelectorAdapter.OnGalleryItemClickListener,
        FindAllUtil.OnLoadListener,
        FolderDialog.OnFolderSelectedListener {

    @ViewInject(R.id.tv_selected_image_folder_name)
    private TextView tvFolderName;

    @ViewInject(R.id.rl_images)
    private RecyclerView rlImages;

    @ViewInject(R.id.progressBar)
    private ProgressBar mProgressBar;

    private GallerySelectorAdapter mAdapter;

    private LinkedHashMap<String, List<MediaEntity>> mediaFoldersMap;

    private List<FolderEntity> folders;

    private List<MediaEntity> mItems;

    private String keyName = "图片和视频";

    private FolderDialog folderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_selector);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);

        initRlImages();
    }

    private void initRlImages() {
        mediaFoldersMap = new LinkedHashMap<>();
        mItems = new ArrayList<>();

        rlImages.setLayoutManager(new GridLayoutManager(this, 3));
        rlImages.addItemDecoration(new DividerGridItemDecoration(this));
        mAdapter = new GallerySelectorAdapter(this, mItems);
        mAdapter.setOnItemClickListener(this);
        rlImages.setAdapter(mAdapter);

        tvFolderName.setText(keyName);

        FindAllUtil findAllUtil = new FindAllUtil(this, mediaFoldersMap);
        findAllUtil.execute(this);

    }

    @OnClick({R.id.back, R.id.tv_selected_image_folder_name})
    protected void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_selected_image_folder_name:
                showImageFolder();
                break;
        }
    }

    private void showImageFolder() {

        if (folderDialog == null) {
            folderDialog = new FolderDialog(this, folders);
            folderDialog.setOnFolderSelectedListener(this);
        } else {
            folderDialog.setImageFolders(folders);
        }
        folderDialog.show();
    }

    @Override
    public void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute() {
        mProgressBar.setVisibility(View.GONE);
        mItems.addAll(mediaFoldersMap.get(keyName));
        mAdapter.notifyDataSetChanged();

        if (mediaFoldersMap.size() > 0) {
            folders = new ArrayList<>();
            for (String key : mediaFoldersMap.keySet()) {
                List<MediaEntity> entities = mediaFoldersMap.get(key);
                if (entities != null && entities.size() > 0) {
                    String firstPath = entities.get(0).getPath();
                    FolderEntity entity = new FolderEntity(key, entities.size(), firstPath);
                    folders.add(entity);
                }
            }
        }
    }

    @Override
    public void onGalleryItemClick(View v, int position) {
        MediaEntity entity = mediaFoldersMap.get(keyName).get(position);
        Intent data = new Intent();
        data.putExtra("MEDIA_ENTITY", entity);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onFolderSelected(View v, String nowFolderName) {
        if (keyName.equals(nowFolderName)) {
            return;
        }
        keyName = nowFolderName;
        mItems.clear();
        mItems.addAll(mediaFoldersMap.get(keyName));
        mAdapter.notifyDataSetChanged();
        tvFolderName.setText(keyName);
    }
}
