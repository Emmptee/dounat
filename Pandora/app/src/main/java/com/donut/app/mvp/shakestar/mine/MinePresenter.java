package com.donut.app.mvp.shakestar.mine;

import android.util.Log;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.AttentionRequest;
import com.donut.app.http.message.MaterialRequest;
import com.donut.app.http.message.MyProductionRequest;
import com.donut.app.http.message.ShakeStarCommendRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.MaterialResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarLikeResponse;
import com.donut.app.http.message.wish.CommendLikeRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */
//<V extends HomeContract.View> extends HomeContract.Presenter <V>
public class MinePresenter<V extends MineContract.View> extends MineContract.Presenter<V> {

    private static final String TAG = "MinePresenter";

    private static final int NEW_MYPRODUCTION = 0000;
    private static final int SHAKE_MYLIKE = 00001;
    private static final int SHAKE_MYMATERIAL = 00002;
    private static final int SHAKE_ATTEMTION = 00003;
    private static final int SHAKE_SHARE = 00004;
    private List<MyProductionResponse.MyShakingStarListBean> myShakingStarListBeans=new ArrayList<>();
    private List<MyLikeResponse.MyLikeShakingStarListBean> myLikelist=new ArrayList<>();
    private List<MaterialResponse.MyMaterialListBean> myMaterialListBeans=new ArrayList<>();
    private MyProductionResponse myProductionResponse;
    private int page;
    public void loadData(boolean showLoadingView,String selectedUserId,int page,int rows,int judge) {
        if(judge==0){//作品
            this.page=page;
            MyProductionRequest request = new MyProductionRequest();
            request.setSelectedUserId(selectedUserId);
            request.setPage(page);
            request.setRows(rows);
            super.loadData(request,
                    HeaderRequest.SHAKE_MYPRODUCTION,
                    NEW_MYPRODUCTION,
                    showLoadingView);
        }else if(judge==1){//喜欢
            this.page=page;
            MyProductionRequest request = new MyProductionRequest();
            request.setSelectedUserId(selectedUserId);
            request.setPage(page);
            request.setRows(rows);
            super.loadData(request,
                    HeaderRequest.SHAKE_MYLIKE,
                    SHAKE_MYLIKE,
                    showLoadingView);
        }else if(judge==2){//素材
            this.page=page;
            MaterialRequest request = new MaterialRequest();
            Log.i(TAG, "loadData: 素材的ID为:" + selectedUserId);
            request.setSelectedUserId(selectedUserId);
            request.setPage(page);
            request.setRows(rows);
            super.loadData(request,
                    HeaderRequest.SHAKE_MYMATERIAL,
                    SHAKE_MYMATERIAL,
                    showLoadingView);
        }

    }
    public void loadAttention(boolean showLoadingView,String starId,String operation){
        AttentionRequest request=new AttentionRequest();
        request.setStarId(starId);
        request.setOperation(operation);
        super.loadData(request,
                HeaderRequest.CONCERNED_ON_STAR,
                SHAKE_ATTEMTION,
                showLoadingView);
    }
    public void Share(boolean showLoadingView,String contentId,String subjectId) {
        ShareRequest request = new ShareRequest();
        request.setContentId(contentId);
        request.setSubjectId(subjectId);

        super.loadData(request,
                HeaderRequest.SHARE,
                SHAKE_SHARE,
                showLoadingView);
    }
    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId){
            case NEW_MYPRODUCTION:
                MyProductionResponse myProductionResponse=JsonUtils.fromJson(responseJson, MyProductionResponse.class);
                if(COMMON_SUCCESS.equals(myProductionResponse.getCode())){

                    if(myProductionResponse.getMyShakingStarList()!=null){
                        if(page==0){
                            myShakingStarListBeans.clear();
                        }
                        this.myProductionResponse=myProductionResponse;
                        myShakingStarListBeans.addAll(myProductionResponse.getMyShakingStarList());
                        mView.showView(myShakingStarListBeans,myProductionResponse);
                    }else{
                        mView.showView(myShakingStarListBeans,myProductionResponse);
                    }
                }else{
                    showToast(myProductionResponse.getMsg());
                }
                break;
            case  SHAKE_MYLIKE:
                L.e("json",responseJson);
                MyLikeResponse myLikeResponse=JsonUtils.fromJson(responseJson, MyLikeResponse.class);
                if(COMMON_SUCCESS.equals(myLikeResponse.getCode())){
                    if(myLikeResponse.getMyLikeShakingStarList()!=null){
                        if(page==0){
                            myLikelist.clear();
                        }
                        myLikelist.addAll(myLikeResponse.getMyLikeShakingStarList());
                        mView.showLikeView(myLikelist);
                    }else {
                        myMaterialListBeans.clear();
                        mView.showLikeView(myLikelist);
                    }
                }else{
                    showToast(myLikeResponse.getMsg());
                }
                break;
            case SHAKE_MYMATERIAL:
                 //素材
                L.e("json",responseJson);
                MaterialResponse materialResponse=JsonUtils.fromJson(responseJson, MaterialResponse.class);
                if(COMMON_SUCCESS.equals(materialResponse.getCode())){
                    if(materialResponse.getMyMaterialList()!=null){
                        if(page==0){
                            myMaterialListBeans.clear();
                        }
                        myMaterialListBeans.addAll(materialResponse.getMyMaterialList());
                        mView.showMaterialView(myMaterialListBeans);
                    }else {
                        myMaterialListBeans.clear();
                        mView.showMaterialView(myMaterialListBeans);
                    }
                }else{
                }
                break;
            case SHAKE_ATTEMTION:
                L.e("json",responseJson);
                //关注
                break;
            case SHAKE_SHARE:
                //分享
                break;
        }
    }
}
