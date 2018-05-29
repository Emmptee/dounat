package com.donut.app.mvp.shakestar.select;

import android.text.TextUtils;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.InformRequest;
import com.donut.app.http.message.ShakeStarCommendRequest;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.http.message.wish.ShakeStarSelectRequest;
import com.donut.app.mvp.shakestar.commend.CommendContract;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by hard on 2018/1/30.
 */
//<V extends HomeContract.View> extends HomeContract.Presenter <V>
public class SelectPresenter<V extends SelectContract.View> extends SelectContract.Presenter<V> {

    private static final int NEW_HOME_CODE = 0000;
    private static final int NEW_USERINFORM_CODE = 00001;
    private List<ShakeStarSelectResponse.MaterialListBean> selectList=new ArrayList<>();
    private int page=0;
    public IsInfo isInfo;
    public void loadData(boolean showLoadingView,String name,int sortType,int page,int rows) {
        this.page=page;
        ShakeStarSelectRequest request = new ShakeStarSelectRequest();
        request.setSearchStr(name);
        request.setSortType(sortType);
        request.setPage(page);
        request.setRows(rows);
        super.loadData(request,
                HeaderRequest.SHAKESTAR_SELECT,
                NEW_HOME_CODE,
                showLoadingView);
    }
    public void userInform(boolean showLoadingView,String fkB02) {
        InformRequest request = new InformRequest();
        request.setFkB02(fkB02);
        super.loadData(request,
                HeaderRequest.SHAKE_USERINFORM,
                NEW_USERINFORM_CODE,
                showLoadingView);
    }
    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId){
            case NEW_HOME_CODE:
                L.e("JSON",responseJson);
                ShakeStarSelectResponse selectResponse=JsonUtils.fromJson(responseJson, ShakeStarSelectResponse.class);

                if(COMMON_SUCCESS.equals(selectResponse.getCode())){

                    if(selectResponse.getMaterialList()==null){
                        isInfo.Info(1);
                    }
                      try{
                          if(page==0){
                              selectList.clear();
                          }
                          selectList.addAll(selectResponse.getMaterialList());
                          mView.showView(selectList);
                      }catch (Exception e){
                          e.printStackTrace();
                      }


                }else{
                    showToast(selectResponse.getMsg());
                }
                break;
            case NEW_USERINFORM_CODE:
                //举报
                break;
        }
    }
    public void clear(){
        selectList.clear();
    }
   public interface IsInfo{
        void Info(int k);
    }

    public void setIsInfo(IsInfo isInfo) {
        this.isInfo = isInfo;
    }
}
