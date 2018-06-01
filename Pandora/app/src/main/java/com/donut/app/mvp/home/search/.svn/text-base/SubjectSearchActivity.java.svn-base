package com.donut.app.mvp.home.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivitySubjectSearchLayoutBinding;
import com.donut.app.http.message.StarListDetail;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.home.ChannelItem;
import com.donut.app.http.message.home.ContentCategory;
import com.donut.app.http.message.home.ContentItem;
import com.donut.app.http.message.home.HomePageSearchResponse;
import com.donut.app.http.message.home.HomePageSearchV2Response;
import com.donut.app.http.message.home.WishItem;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.blooper.BlooperAdapter;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.mvp.spinOff.SpinOffActivity;
import com.donut.app.mvp.wish.reply.WishReplyActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class SubjectSearchActivity extends MVPBaseActivity<ActivitySubjectSearchLayoutBinding, SubjectSearchPresenter>
        implements SubjectSearchContract.ContextView,
        TextView.OnEditorActionListener {

    private List<ChannelItem> channelItems = new ArrayList<>();

    private List<ContentCategory> contentCategories = new ArrayList<>();
    private List<ContentItem> contentItems = new ArrayList<>();


    private ContentItemSearchAdapter cAdapter;


    private StickySectionDecoration decoration;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subject_search_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        mViewBinding.setHandler(this);
        cAdapter = new ContentItemSearchAdapter(contentItems,this);
        mViewBinding.listSearch.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.listSearch.setAdapter(cAdapter);
        mViewBinding.tvInitMsg.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initEvent() {
        mViewBinding.etSearch.setOnEditorActionListener(this);
        mViewBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    mViewBinding.ivEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void loadData() {
    }

  /*  @Override
    public void showView(final HomePageSearchResponse detail) {
        mViewBinding.etSearch.setEnabled(true);
        subjectList.clear();
        wishList.clear();
        channelItems.clear();
        mViewBinding.tvInitMsg.setVisibility(View.GONE);

        if (detail.getChannelItems() != null
                && detail.getChannelItems().size() > 0) {
            channelItems.addAll(detail.getChannelItems());
            for (ChannelItem item : detail.getChannelItems()) {
                if (item.getSubjectList() != null) {
                    subjectList.addAll(item.getSubjectList());
                }
            }
        }

        if (detail.getWishItems() != null
                && detail.getWishItems().size() > 0) {
            wishList.addAll(detail.getWishItems());
        }

        StickySectionDecoration.GroupInfoCallback callback = new StickySectionDecoration.GroupInfoCallback() {

            @Override
            public StickySectionDecoration.GroupInfo getGroupInfo(int position) {
                if (position < subjectList.size()) {
                    int childCount = 0;
                    for (int groupId = 0; groupId < channelItems.size(); groupId++) {
                        if (channelItems.get(groupId).getSubjectList() != null) {
                            int index = position - childCount;
                            int itemSize = channelItems.get(groupId).getSubjectList().size();
                            childCount += itemSize;
                            if (index < itemSize) {
                                StickySectionDecoration.GroupInfo groupInfo
                                        = new StickySectionDecoration.GroupInfo(
                                        groupId, channelItems.get(groupId).getChannelName());
                                groupInfo.setGroupLength(itemSize);
                                groupInfo.setPosition(index);
                                return groupInfo;
                            }
                        }
                    }
                } else {
                    StickySectionDecoration.GroupInfo groupInfo
                            = new StickySectionDecoration.GroupInfo(
                            channelItems.size(), "心愿");
                    groupInfo.setGroupLength(wishList.size());
                    groupInfo.setPosition(position - subjectList.size());
                    return groupInfo;
                }
                return null;
            }
        };
        mViewBinding.listSearch.removeItemDecoration(decoration);
        decoration = new StickySectionDecoration(this, callback);
        mViewBinding.listSearch.addItemDecoration(decoration);

        mAdapter.notifyDataSetChanged();
        if (subjectList.size() + wishList.size() <= 0) {
            mViewBinding.tvMsg.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.tvMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSubjectItemClick(SubjectListDetail detail) {
        //跳转详情
        try {
            for (ChannelItem channelItem : channelItems) {
                for (SubjectListDetail subjectDetail : channelItem.getSubjectList()) {
                    if (detail.getSubjectId().equals(subjectDetail.getSubjectId())) {
                        GotoChannelUtils.GotoSubjectDetail(this,
                                channelItem.getChannelType(),
                                detail.getSubjectId(),
                                0);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWishItemClick(WishItem detail) {
        Bundle bundle = new Bundle();
        bundle.putString(WishReplyActivity.CONTENT_ID, detail.getB02Id());
        launchActivity(WishReplyActivity.class, bundle);
    }
*/
    @Override
    public void showSearchView(HomePageSearchV2Response response) {
        mViewBinding.etSearch.setEnabled(true);
        contentItems.clear();
        contentCategories.clear();
        mViewBinding.tvInitMsg.setVisibility(View.GONE);
        if (response.getCategoryList() != null
                && response.getCategoryList().size() > 0) {
            contentCategories.addAll(response.getCategoryList());
            for (ContentCategory item : response.getCategoryList()) {
                if (item.getItemList() != null) {
                    List<ContentItem> itemList = item.getItemList();
                    for(ContentItem contentItem :itemList){
                        contentItem.setCategoryType(item.getCategoryType());
                        contentItems.add(contentItem);
                    }
                }
            }
        }
        StickySectionDecoration.GroupInfoCallback callback = new StickySectionDecoration.GroupInfoCallback() {

            @Override
            public StickySectionDecoration.GroupInfo getGroupInfo(int position) {
                if (position < contentItems.size()) {
                    int childCount = 0;
                    for (int groupId = 0; groupId < contentCategories.size(); groupId++) {
                        if (contentCategories.get(groupId).getItemList() != null) {
                            int index = position - childCount;
                            int itemSize = contentCategories.get(groupId).getItemList().size();
                            childCount += itemSize;
                            if (index < itemSize) {
                                StickySectionDecoration.GroupInfo groupInfo
                                        = new StickySectionDecoration.GroupInfo(
                                        groupId, contentCategories.get(groupId).getCategoryName());
                                groupInfo.setGroupLength(itemSize);
                                groupInfo.setPosition(index);
                                return groupInfo;
                            }
                        }
                    }
                }
                return null;
            }
        };
        mViewBinding.listSearch.removeItemDecoration(decoration);
        decoration = new StickySectionDecoration(this, callback);
        mViewBinding.listSearch.addItemDecoration(decoration);
        cAdapter.notifyDataSetChanged();
        if (contentItems.size() <= 0) {
            mViewBinding.tvMsg.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.tvMsg.setVisibility(View.GONE);
            mViewBinding.tvInitMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onContentItemClick(ContentItem contentItem) {
        //跳转详情
        try {
            out:
            for (ContentCategory contentCategory:contentCategories){
                for(ContentItem itemList :contentCategory.getItemList()){
                    if(contentItem.getCategoryType()<3) {
                        GotoChannelUtils.GotoSubjectDetail(this,
                                contentCategory.getCategoryType(),
                                contentItem.getSubjectId(),
                                0);
                      return;
                    }
                }
            }
            if(!StringUtils.isEmpty(contentItem.getB02Id() )&& contentItem.getCategoryType() == 4) {
                Bundle bundle = new Bundle();
                bundle.putString(WishReplyActivity.CONTENT_ID, contentItem.getB02Id());
                launchActivity(WishReplyActivity.class, bundle);
            }else if(!StringUtils.isEmpty(contentItem.getStarId()) && contentItem.getCategoryType() == 6){
                Bundle bundle = new Bundle();
                bundle.putString(BlooperDetailActivity.STAR_ID, contentItem.getStarId());
                launchActivity(BlooperDetailActivity.class, bundle);
            }else if(contentItem.getCategoryType() == 89){
                Bundle bundle = new Bundle();
                bundle.putBoolean(SpinOffActivity.FROM_HOME_BOTTOM, true);
                bundle.putInt(SpinOffActivity.SPIN_OFF_TYPE, 4);
                launchActivity(SpinOffActivity.class, bundle);
            }else if(contentItem.getCategoryType() == 10){
                Bundle bundle = new Bundle();
                bundle.putBoolean(SpinOffActivity.FROM_HOME_BOTTOM, true);
                bundle.putInt(SpinOffActivity.SPIN_OFF_TYPE, 3);
                launchActivity(SpinOffActivity.class, bundle);
            }else if(contentItem.getCategoryType() == 11){
                Intent intent =null;
                if(contentItem.getMaterialType()==0){
                    intent = new Intent(this, ParticularsActivity.class);
                    intent.putExtra("g03",contentItem.getG03Id());
                    intent.putExtra("b02",contentItem.getB02Id());
                    startActivity(intent);
                }else if(contentItem.getMaterialType()==1){
                    intent=new Intent(this, JointActivity.class);
                    intent.putExtra("g03",contentItem.getG03Id());
                    intent.putExtra("b02",contentItem.getB02Id());
                    startActivity(intent);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mViewBinding.etSearch.setEnabled(false);
            mPresenter.searchContent = v.getText().toString();
            if (mPresenter.searchContent.length() <= 0) {
                showToast("请输入搜索内容");
                mViewBinding.etSearch.setEnabled(true);
                return false;
            }
            mPresenter.loadData(false);
            return true;
        }
        return false;
    }

    public void onClearEdit() {
        mViewBinding.etSearch.setText("");
        mPresenter.searchContent = "";
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!im.isActive()) {
            mPresenter.loadData(false);
        }
    }
}
