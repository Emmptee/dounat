package com.donut.app.mvp.star.area;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.FragmentStarAreaItemLayoutBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.service.SaveBehaviourDataService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StarAreaItemFragment extends MVPBaseFragment<FragmentStarAreaItemLayoutBinding, StarAreaItemPresenter>
        implements StarAreaItemContract.View {

    private static final String SUBJECT_DETAIL = "SUBJECT_DETAIL";

    private final static int SUBJECT_DETAIL_REQUEST = 1;

    public StarAreaItemFragment() {
    }

    public static StarAreaItemFragment newInstance(SubjectListDetail subjectDetail) {
        StarAreaItemFragment fragment = new StarAreaItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUBJECT_DETAIL, subjectDetail);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star_area_item_layout;
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
            SubjectListDetail subjectDetail = getArguments().getParcelable(SUBJECT_DETAIL);
            if (subjectDetail != null) {
                showView(subjectDetail);
            }
        }
    }

    @Override
    public void showView(SubjectListDetail detail) {
        mViewBinding.setDetail(detail);
        mViewBinding.setHandler(this);

        if (detail.isEmptyData()) {
            mViewBinding.starAreaItemPlaybill.setImageDrawable(
                    getResources().getDrawable(R.drawable.star_area_empty_bg));
        } else {
            Glide.with(this)
                    .load(detail.getPublicPic())
                    .centerCrop()
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .into(mViewBinding.starAreaItemPlaybill);
        }

    }

    @Override
    public void gotoDetail() {
        //跳转详情
        if (mViewBinding.getDetail().isEmptyData()) {
            return;
        }
        int channelType = mViewBinding.getDetail().getChannelType();
        String subjectId = mViewBinding.getDetail().getSubjectId();
        GotoChannelUtils.GotoSubjectDetail(this, channelType, subjectId, 0);
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.STAR_ZONE.getCode() + "02");
    }

    private String createTimeFormat(String time) {
        if (time == null) {
            return "";
        }
        String formatTime = time;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(formatTime.substring(0, 10));
            formatTime = new SimpleDateFormat("yyyy MM dd", Locale.CHINA).format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatTime;
    }
}
