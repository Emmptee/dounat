package com.donut.app.mvp.channel.list;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.LoginActivity;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.FragmentChannelListItemLayoutBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.mvp.MVPBaseFragment;

import java.util.List;

public class ChannelListItemFragment extends MVPBaseFragment<FragmentChannelListItemLayoutBinding, ChannelListItemPresenter>
        implements ChannelListItemContract.View {

    private static final String SUBJECT_DETAIL = "SUBJECT_DETAIL", CHANNEL_TYPE = "CHANNEL_TYPE";

    private SubjectListDetail subjectDetail;

    private final static int REQUEST_CODE_LOGIN = 1, GOTO_DETAIL = 2;

    private OnFragmentInteractionListener mListener;

    private int channelType;

    public ChannelListItemFragment() {
    }

    public static ChannelListItemFragment newInstance(SubjectListDetail subjectDetail, int channelType) {
        ChannelListItemFragment fragment = new ChannelListItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUBJECT_DETAIL, subjectDetail);
        bundle.putInt(CHANNEL_TYPE, channelType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_channel_list_item_layout;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void loadData() {
        if (getArguments() != null) {
            subjectDetail = getArguments().getParcelable(SUBJECT_DETAIL);
            channelType = getArguments().getInt(CHANNEL_TYPE);
            if (subjectDetail != null) {
                showView(subjectDetail);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) getParentFragment();
        } else if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException((getParentFragment() != null ? getParentFragment() : context).toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showView(SubjectListDetail detail) {
        mViewBinding.setDetail(detail);
        mViewBinding.setHandler(this);
        mViewBinding.setChannelType(channelType);
        Glide.with(this)
                .load(detail.getPublicPic())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(mViewBinding.channelListItemPlaybill);
    }

    @Override
    public void collectSuccess() {
        mViewBinding.channelListItemCollect.setEnabled(true);
    }

    public void gotoDetail() {
        //跳转详情
        GotoChannelUtils.GotoSubjectDetail(this, channelType,
                subjectDetail.getSubjectId(), GOTO_DETAIL);
        mPresenter.saveBehaviour("01", channelType);
    }

    public void toShare() {
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void toCollect() {
        mViewBinding.channelListItemCollect.setEnabled(false);
        SharedPreferences sp_Info = getActivity().getSharedPreferences(
                Constant.SP_INFO, Context.MODE_PRIVATE);
        if (!sp_Info.getBoolean(Constant.IS_LOGIN, false)) {
            mViewBinding.channelListItemCollect.setEnabled(true);
            showToast("请先登录");
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        mPresenter.onToCollect(subjectDetail);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);

        mPresenter.onToShare(subjectDetail, channelType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GOTO_DETAIL:
                if (resultCode == Activity.RESULT_OK) {
                    int state = data.getIntExtra(Constant.COLLECT_STATUS, 0);
                    subjectDetail.setCollectionStatus(state);
                }
                break;
            case REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    mListener.onRefreshData();
                }
                break;
        }
    }

    interface OnFragmentInteractionListener {
        void onRefreshData();
    }
}
