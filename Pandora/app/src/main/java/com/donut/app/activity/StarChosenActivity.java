package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.StarChosenAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.MyAppreciatesRequest;
import com.donut.app.http.message.MyAppreciatesResponse;
import com.donut.app.http.message.RecommandRequest;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class StarChosenActivity extends BaseActivity implements StarChosenAdapter.OnRecyclerViewListener
{
    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecycleview;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    private StarChosenAdapter mAdapter;

    private List<MyAppreciatesResponse.MyAppreciate> choiceList=new ArrayList<MyAppreciatesResponse.MyAppreciate>();

    private static final int CHOSEN_REQUEST=0,CHOSEN_CANCLE_REQUEST=1;

    private int pos=-1;

    public static final String SUBJECT_ID="subjectid";

    private String subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_chosen);
        StatusBarUtil.setColor(StarChosenActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.star_chosen), true);
        initView();
        subjectId=getIntent().getStringExtra(SUBJECT_ID);
    }

    @Override
    protected void onResume()
    {
        requestData();
        super.onResume();
    }

    private void initView()
    {
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(getLayoutManager());
        mAdapter = new StarChosenAdapter(this, choiceList);
        mRecycleview.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewListener(this);
    }


    private void requestData(){
        MyAppreciatesRequest request=new MyAppreciatesRequest();
        request.setSubjectId(subjectId);
        sendNetRequest(request, HeaderRequest.MY_APPRECIATE, CHOSEN_REQUEST,
                false);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_RECOMMAND.getCode()+"00",request,HeaderRequest.MY_APPRECIATE);
    }

    @Override
    public void onItemClick(int position)
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_RECOMMAND.getCode()+"01");
        Bundle bundle = new Bundle();
        bundle.putString(SubjectChallengeActivity.CONTENT_ID, choiceList.get(position).getContentId());
        bundle.putString(SubjectChallengeActivity.SUBJECT_ID, subjectId);
        launchActivity(SubjectChallengeActivity.class, bundle);
    }

    @Override
    public void onCancle(final int position)
    {
        Dialog dialog = new AlertDialog.Builder(StarChosenActivity.this)
                .setMessage(getString(R.string.cancle_chosen_tip))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        pos=position;
                        cancleRequest();

                    }
                }).create();
        dialog.show();
    }

    private void cancleRequest(){
        RecommandRequest request=new RecommandRequest();
        request.setContentId(choiceList.get(pos).getContentId());
        request.setOperation("1");
        sendNetRequest(request, HeaderRequest.RECOMMAND_OPERA, CHOSEN_CANCLE_REQUEST,
                false);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_RECOMMAND.getCode()+"02",request,HeaderRequest.RECOMMAND_OPERA);
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId){
            case CHOSEN_REQUEST:
                MyAppreciatesResponse addrRes = JsonUtils.fromJson(response,
                        MyAppreciatesResponse.class);
                if(COMMON_SUCCESS.equals(addrRes.getCode())){
                    showView(addrRes.getMyAppreciates());
                }
                break;
            case CHOSEN_CANCLE_REQUEST:
                BaseResponse defaultRes=JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(defaultRes.getCode())){
                    choiceList.remove(pos);
                    mAdapter.notifyDataSetChanged();
                    if(choiceList.size()==0){
                        mNoData.setVisibility(View.VISIBLE);
                    }
                }else{
                    ToastUtil.showShort(this,defaultRes.getMsg());
                }
                break;
        }
    }

    private void showView(List<MyAppreciatesResponse.MyAppreciate> list){

        choiceList.clear();

        if (list != null && list.size() > 0)
        {
            mNoData.setVisibility(View.GONE);
        } else
        {
          mNoData.setVisibility(View.VISIBLE);
        }
        choiceList.addAll(list);
        mAdapter.notifyDataSetChanged();

    }

    private ABaseLinearLayoutManager getLayoutManager()
    {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                this);
        return layoutManager;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_RECOMMAND.getCode()+"03");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.STAR_RECOMMAND.getCode() + "xx");
        super.onStop();
    }
}
