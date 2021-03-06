package com.donut.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.customview.CircleMenuLayout;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.AppChannel;
import com.donut.app.http.message.AppChannelResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/20.
 * ChannelFragment
 */
public class ChannelFragment extends BaseFragment {

    private CircleMenuLayout mCircleMenuLayout;

    @ViewInject(R.id.channel_one_img)
    private ImageView channelOneImg;

    @ViewInject(R.id.channel_one_name)
    private TextView channelOneText;

    @ViewInject(R.id.channel_two_img)
    private ImageView channelTwoImg;

    @ViewInject(R.id.channel_two_name)
    private TextView channelTwoText;

    @ViewInject(R.id.channel_three_img)
    private ImageView channelThreeImg;

    @ViewInject(R.id.channel_three_name)
    private TextView channelThreeText;

    private static List<AppChannel> recommendList = new ArrayList<>();

    private static List<AppChannel> generalList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mLayoutView = inflater.inflate(R.layout.fragment_channel_layout,
                container, false);
        ViewUtils.inject(this, mLayoutView);

        mCircleMenuLayout = (CircleMenuLayout) mLayoutView.findViewById(R.id.id_menulayout);

        loadData();
        initEvent();
        return mLayoutView;
    }

    private void loadData() {

        sendNetRequest(new Object(), HeaderRequest.CHANNEL_LIST);
    }

    private void initEvent() {
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                gotoChannelList(generalList.get(pos));
            }

            @Override
            public void itemCenterClick(View view) {
            }
        });
    }

    @OnClick({R.id.channel_one, R.id.channel_two,
            R.id.channel_three})
    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.channel_one:
                gotoChannelList(recommendList.get(0));
                break;
            case R.id.channel_two:
                gotoChannelList(recommendList.get(1));
                break;
            case R.id.channel_three:
                gotoChannelList(recommendList.get(2));
                break;
        }
    }

    private void gotoChannelList(AppChannel channel) {
        GotoChannelUtils.GotoList(this, channel, 0);

        if (channel.getType() == GotoChannelUtils.CHANNEL_TYPE_SUBJECT) {
            saveBehaviour("02");
        } else if (channel.getType() == GotoChannelUtils.CHANNEL_TYPE_SNAP) {
            saveBehaviour("03");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        AppChannelResponse channelResponse
                = JsonUtils.fromJson(response, AppChannelResponse.class);
        if (COMMON_SUCCESS.equals(channelResponse.getCode())) {
            recommendList.clear();
            generalList.clear();
            recommendList.addAll(channelResponse.getRecommendList());
            int recommendSize = recommendList.size();
            if (recommendSize > 0) {
                Glide.with(getContext())
                        .load(recommendList.get(0).getPicUrl())
//                        .centerCrop()
                        .placeholder(R.drawable.channel_loading)
                        .error(R.drawable.channel_loading)
                        .into(channelOneImg);
                channelOneText.setText(recommendList.get(0).getName());
            }
            if (recommendSize > 1) {
                Glide.with(getContext())
                        .load(recommendList.get(1).getPicUrl())
//                        .centerCrop()
                        .placeholder(R.drawable.channel_loading)
                        .error(R.drawable.channel_loading)
                        .into(channelTwoImg);
                channelTwoText.setText(recommendList.get(1).getName());
            }
            if (recommendSize > 2) {
                Glide.with(getContext())
                        .load(recommendList.get(2).getPicUrl())
//                        .centerCrop()
                        .placeholder(R.drawable.channel_loading)
                        .error(R.drawable.channel_loading)
                        .into(channelThreeImg);
                channelThreeText.setText(recommendList.get(2).getName());
            }

            generalList.addAll(channelResponse.getGeneralList());
            int generalSize = generalList.size();
            if (generalSize > 6) {
                generalSize = 6;
            }
            String[] mItemImgs = new String[generalSize];
            String[] mItemTexts = new String[generalSize];
            for (int i = 0; i < generalSize; i++) {
                mItemImgs[i] = generalList.get(i).getPicUrl();
                mItemTexts[i] = generalList.get(i).getName();
            }
            mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        } else {
            showToast(channelResponse.getMsg());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        saveBehaviour("01");
    }

    @Override
    public void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    public void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(getContext(),
                BehaviourEnum.CHANNEL_LIST.getCode() + functionCode);
    }
}
