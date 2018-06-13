package com.donut.app.mvp.shakestar.commend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;

import com.donut.app.SysApplication;
import com.donut.app.adapter.ShakeStarSelectAdapter;
import com.donut.app.databinding.ActivityShakeSelectLayoutBinding;
import com.donut.app.entity.ConfigInfo;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.ItemDecoration;
import com.donut.app.mvp.shakestar.select.particulars.JointActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsActivity;
import com.donut.app.mvp.shakestar.select.SelectContract;
import com.donut.app.mvp.shakestar.select.SelectPresenter;
import com.donut.app.mvp.shakestar.utils.UserMessageDialog;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.socks.library.KLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hard on 2018/1/31.
 */

public class ShakeStarSelectActivity extends MVPBaseActivity<ActivityShakeSelectLayoutBinding, SelectPresenter> implements SelectContract.View,SwipeRefreshLayout.OnRefreshListener {

    public static final int REQUEST_FOR_PARTICULARS = 1000;

    private String name=null;
    private int page = 0, rows = 10, sortType = 0;
    private static final int ACTIONA=0;
    private static final int ACTIONB=1;
    private ShakeStarSelectAdapter recyclerViewAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shake_select_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.translucentStatusBar(this);
        mViewBinding.selectList.setLayoutManager(getLayoutManager());
        mViewBinding.backSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showKeyBoard();

        mViewBinding.userMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ConfigInfo info = SysApplication.getDb().findFirst(Selector.from(ConfigInfo.class).where("name", "=", "MATERIALTIP"));
                    String message = info.getValue();
                    UserMessageDialog.show(ShakeStarSelectActivity.this, message,null);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });

        //最新
        mViewBinding.ivShakeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.i("最新");
                sortType = 1;
                load();
            }
        });
        //最热
        mViewBinding.ivShakeHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.i("最热");
                sortType = 0;
                load();
            }
        });
        //同屏
        mViewBinding.ivShakeSamescreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.i("同屏");
                sortType = 2;
                load();
//                clickToRefresh();
            }
        });

        //拼接
        mViewBinding.ivShakeSplice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.i("拼接");
                sortType = 3;
                load();
            }
        });
    }

    @Override
    protected void loadData() {
        mViewBinding.selectSwip.setOnRefreshListener(this);

        ItemDecoration itemDecoration=new ItemDecoration(this);
        mViewBinding.selectList.addItemDecoration(itemDecoration);
        load();
        initEvent();

    }

    @Override
    public void showView(final List<ShakeStarSelectResponse.MaterialListBean > selectResponse) {
               mPresenter.setIsInfo(new SelectPresenter.IsInfo() {
                   @Override
                   public void Info(int k) {
                       if(k==1){
//                           mViewBinding.subjectSearchTvMsg.setVisibility(View.VISIBLE);
                       }else{
                           mViewBinding.subjectSearchTvMsg.setVisibility(View.GONE);
                       }
                   }
               });

               if(recyclerViewAdapter==null){
                   recyclerViewAdapter=new ShakeStarSelectAdapter(selectResponse,getApplicationContext());
                   mViewBinding.selectList.setAdapter(recyclerViewAdapter);
               }else{
                   recyclerViewAdapter.notifyDataSetChanged();
               }

        recyclerViewAdapter.setItemParticularsOnClickListener(new ShakeStarSelectAdapter.ItemParticularsOnClickListener() {
            @Override
            public void ParticularsClick(int position) {
                if(selectResponse.get(position).getType()==ACTIONA){//分屏
                    Intent itent=new Intent(getApplicationContext(), ParticularsActivity.class);
                    itent.putExtra("g03",selectResponse.get(position).getG03Id());
                    itent.putExtra("b02",selectResponse.get(position).getB02Id());
                    startActivityForResult(itent,REQUEST_FOR_PARTICULARS);
                }else if(selectResponse.get(position).getType()==ACTIONB){
                    Intent itent=new Intent(getApplicationContext(), JointActivity.class);
                    itent.putExtra("g03",selectResponse.get(position).getG03Id());
                    itent.putExtra("b02",selectResponse.get(position).getB02Id());
                    startActivityForResult(itent,REQUEST_FOR_PARTICULARS);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
//        page = 0;
//        if (!this.isAdded()) {
//            return;
//        }
        recyclerViewAdapter.notifyDataSetChanged();
        mViewBinding.selectSwip.setRefreshing(false);
    }
    private boolean isTop;
    private RecyclerView.LayoutManager getLayoutManager() {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.selectList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            page++;
                            load();
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

    private void showKeyBoard()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                InputMethodManager inputManager =
                        (InputMethodManager)  mViewBinding.shakeSearchIvEtClean.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput( mViewBinding.shakeSearchIvEtClean, 0);
            }

        }, 500);
    }

    public void initEvent()
    {
        mViewBinding.shakeSearchEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 0)
                {
                    mViewBinding.shakeSearchIvEtClean.setVisibility(View.GONE);
                } else
                {
                    mViewBinding.shakeSearchIvEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewBinding.shakeSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {

                    name=mViewBinding.shakeSearchEt.getText().toString().trim();
                    load();

                }
                return false;
            }
        });

        mViewBinding.shakeSearchIvEtClean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name=mViewBinding.shakeSearchEt.getText().toString().trim();
                if(!TextUtils.isEmpty(name)){
                    mViewBinding.shakeSearchEt.setText("");
                    load();
                }

            }
        });


    }
    public void load(){
        mPresenter.loadData(true,name,sortType,page,rows);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_PARTICULARS){
            if (resultCode == RESULT_OK){
                setResult(RESULT_OK);
                this.finish();
            }
        }
    }
}
