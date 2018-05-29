package com.donut.app.mvp.spinOff.boons;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.manager.RequestManager;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.donut.app.R;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.databinding.FragmentSpinOffBoonsBinding;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.spinOff.ExpressionPics;
import com.donut.app.http.message.spinOff.WelfareZone;
import com.donut.app.http.message.spinOff.WelfareZoneResponse;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.utils.FileUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Qi on 2017/6/5.
 * Description : 衍生品-福利区 <br>
 */
public class SpinOffBoonsFragment extends MVPBaseFragment<FragmentSpinOffBoonsBinding, SpinOffBoonsPresenter>
        implements SpinOffBoonsContract.View, SpinOffBoonsListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SpinOffBoonsListAdapter mAdapter;

    private List<WelfareZone> boonsList = new ArrayList<>();

    private View footerView;

    private int searchType = 0;

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2;

    public static final int PERMISSION_CODE_DOWNLOAD = 1, PERMISSION_CODE_SHARE = 2;

    private WelfareZone model;

    private int nowDownloadPosition;

    private String searchStarName;

    public static SpinOffBoonsFragment newInstance() {
        return new SpinOffBoonsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spin_off_boons;
    }

    @Override
    protected void initView() {

        mViewBinding.spinOffBoonsSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.spinOffBoonsSrl.setOnRefreshListener(this);

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new SpinOffBoonsListAdapter(getContext(), boonsList, this, footerView);
        mViewBinding.spinOffBoonsList.setAdapter(mAdapter);
        mViewBinding.spinOffBoonsList.setLayoutManager(getLayoutManager());


    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        mPresenter.searchStarName = this.searchStarName;
        mPresenter.loadData(true, searchType);
    }

    public void setSearchStarName(String name) {
        this.searchStarName = name;
        if (isResumed()) {
            mPresenter.searchStarName = name;
            onRefresh();
        }
    }

    @Override
    public void showView(WelfareZoneResponse detail) {

        mViewBinding.spinOffBoonsSrl.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            boonsList.clear();
        }

        if (detail.getWelfareZone() != null && detail.getWelfareZone().size() > 0) {
            boonsList.addAll(detail.getWelfareZone());
        }
        mAdapter.notifyDataSetChanged();

        if (boonsList.size() <= 0) {
            mViewBinding.noData.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false, searchType);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        MediaManager.resume();
//    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.setNowAudioPlayPath("");
        mAdapter.notifyDataSetChanged();
        MediaManager.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.spinOffBoonsList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {

                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (boonsList.size() % mPresenter.rows == 0) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mPresenter.loadData(false, searchType);
                        }
                    }
                });

        return layoutManager;
    }

    @Override
    public void onStarDetailClick(WelfareZone detail) {
        Bundle bundle = new Bundle();
        bundle.putString(StarDetailActivity.STAR_ID, detail.getActorId());
        launchActivity(StarDetailActivity.class, bundle);
    }

    @Override
    public void onDownloadClick(int position, WelfareZone detail) {
        this.model = detail;
        this.nowDownloadPosition = position;

        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getContext(), permission)) {
            onPermissionsGranted(PERMISSION_CODE_DOWNLOAD, Arrays.asList(permission));
        } else {
            EasyPermissions.requestPermissions(this, "为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                    PERMISSION_CODE_DOWNLOAD, permission);
        }
    }

    @Override
    public void onLikeClick(WelfareZone model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (model.getPraiseStatus() == 0) {
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(model.getB02Id(), true);
        } else {
            model.setPraiseStatus(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            mPresenter.onLike(model.getB02Id(), false);
        }
    }

    @Override
    public void onCommentClick(WelfareZone detail) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommentActivity.CONTENTID, detail.getB02Id());
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);
    }

    @Override
    public void onShareClick(WelfareZone detail) {
        this.model = detail;

        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getContext(), permission)) {
            onPermissionsGranted(PERMISSION_CODE_SHARE, Arrays.asList(permission));
        } else {
            EasyPermissions.requestPermissions(this, "为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                    PERMISSION_CODE_SHARE, permission);
        }
    }

    @Override
    public void onPlayAudio(WelfareZone detail) {
        mPresenter.onPlayAudio(detail.getB02Id());
        detail.setBrowseTimes(detail.getBrowseTimes() + 1);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PERMISSION_CODE_DOWNLOAD) {

            Integer picType = 2, audioType = 1;
            if (picType.equals(model.getType())) {

                List<ExpressionPics> picList = model.getExpressionPics();
                ExpressionPics[] pics = picList.toArray(new ExpressionPics[picList.size()]);

                DownloadPicTask task = new DownloadPicTask(nowDownloadPosition);
                task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, pics);
            } else if (audioType.equals(model.getType())) {
                StorageManager storageManager = new StorageManager(mContext);
                storageManager.getInfo(model.getVoiceUrl(), model.getLastTime(),
                        new AudioRequestListener(nowDownloadPosition));
            }
        } else if (requestCode == PERMISSION_CODE_SHARE) {
            String linkUrl = "";
            Integer picType = 2, audioType = 1;
            if (picType.equals(model.getType())) {
                linkUrl = RequestUrl.SPIN_OFF_BOONS_PIC_SHARE_URL
                        + "header=00010323&b02Id="
                        + model.getB02Id();
            } else if (audioType.equals(model.getType())) {
                linkUrl = RequestUrl.SPIN_OFF_BOONS_AUDIO_SHARE_URL
                        + "header=00010325&b02Id="
                        + model.getB02Id();
            }

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
            ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
            builder.setTitle(model.getActorName() + "|" + model.getName());
            builder.setContent("爱豆给你的特别福利，快去甜麦圈围观吧！");
            builder.setLinkUrl(linkUrl);
            builder.setBitmap(bmp);
            builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                    SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});

            builder.create();
            mPresenter.shareRequest(model.getB02Id());
            model.setShareTimes(model.getShareTimes() + 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
            case COMMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mViewBinding.spinOffBoonsSrl.setRefreshing(true);
                    onRefresh();
                }
                break;
        }
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
        if (isResumed()) {
            onRefresh();
        }
    }

    private class AudioRequestListener implements RequestManager.RequestListener {

        private int position;

        private AudioRequestListener(int nowDownloadPosition) {
            this.position = nowDownloadPosition;
        }

        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long l, long l1, String s) {
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {

            DownloadAudioTask task = new DownloadAudioTask(position);
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, response);
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
        }
    }

    private class DownloadAudioTask extends AsyncTask<String, Integer, Boolean> {

        private int position;

        private DownloadAudioTask(int nowDownloadPosition) {
            this.position = nowDownloadPosition;
        }

        private String audioFilePath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showToast(getString(R.string.download_start));
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String filePath = params[0];
            boolean createStatus;

            String path = FileUtils.getSDDir(getString(R.string.app_name)
                            + File.separator
                            + getString(R.string.audio),
                    mContext);
            File oldFile = new File(filePath);
            File newFile = new File(path, oldFile.getName());
            if (!newFile.exists()) {
                createStatus = FileUtils.copyFile(oldFile, newFile);
            } else {
                createStatus = true;
            }
            audioFilePath = newFile.getAbsolutePath();
            return createStatus;
        }

        @Override
        protected void onPostExecute(Boolean createStatus) {
            super.onPostExecute(createStatus);
            if (createStatus) {
                showToast(String.format(getString(R.string.download_success), audioFilePath));
                mAdapter.setTvDownLoadMsg(position, getString(R.string.download_finish));
            } else {
                showToast(getString(R.string.download_fail));
                mAdapter.setTvDownLoadMsg(position, getString(R.string.download_retry));
            }


            mAdapter.notifyItemChanged(position);


        }
    }

    private class DownloadPicTask extends AsyncTask<ExpressionPics, Integer, Boolean> {

        private int position;

        private DownloadPicTask(int nowDownloadPosition) {
            this.position = nowDownloadPosition;
        }

        private String path = FileUtils.getSDDir(getString(R.string.app_name)
                        + File.separator
                        + getString(R.string.image),
                mContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showToast(getString(R.string.download_start));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mAdapter.setTvDownLoadMsg(position,
                    String.format(getString(R.string.downloading), values[0], values[1]));
            mAdapter.notifyItemChanged(position);
        }

        @Override
        protected Boolean doInBackground(ExpressionPics... params) {

            boolean createStatus = false;

            for (int i = 0; i < params.length; i++) {
                try {
                    ExpressionPics pic = params[i];
                    File file = Glide
                            .with(SpinOffBoonsFragment.this)
                            .load(pic.getGifUrl())
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    File newFile = new File(path,
                            pic.getGifUrl().substring(pic.getGifUrl().lastIndexOf(File.separator)));
                    if (!newFile.exists()) {
                        createStatus = FileUtils.copyFile(file, newFile);

                        if (!createStatus) {
                            break;
                        }

                        Uri localUri = Uri.fromFile(newFile);
                        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                        getContext().sendBroadcast(localIntent);
                    } else {
                        createStatus = true;
                    }
                    publishProgress(i, params.length);
                } catch (InterruptedException e) {
                    createStatus = false;
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    createStatus = false;
                    e.printStackTrace();
                }
            }
            return createStatus;
        }

        @Override
        protected void onPostExecute(Boolean createStatus) {
            super.onPostExecute(createStatus);
            if (createStatus) {
                mAdapter.setTvDownLoadMsg(position, getString(R.string.download_finish));
                showToast(String.format(getString(R.string.download_success), path));
            } else {
                mAdapter.setTvDownLoadMsg(position, getString(R.string.download_retry));
                showToast(getString(R.string.download_fail));
            }
            mAdapter.notifyItemChanged(position);
        }
    }

}
