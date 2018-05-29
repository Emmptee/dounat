package com.donut.app.mvp.shakestar.search;

import com.donut.app.http.CommendAllRequest;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.AttentionRequest;
import com.donut.app.http.message.CommendContentRequest;
import com.donut.app.http.message.InformRequest;
import com.donut.app.http.message.ShakeStarCommendRequest;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarLikeResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.http.message.wish.CommendLikeRequest;
import com.donut.app.http.message.wish.ShakeStarSelectRequest;
import com.donut.app.mvp.shakestar.select.SelectPresenter;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */
//<V extends HomeContract.View> extends HomeContract.Presenter <V>
public class SearchPresenter<V extends SearchContract.View> extends SearchContract.Presenter<V> {

    private static final int NEW_COMMEND_CODE = 0000;
    private static final int NEW_LIKE_CODE = 00001;
    private static final int NEW_COMMENDCONTENT_CODE = 00004;
    private static final int NEW_COMMENDALL_CODE = 00003;
    private static final int NEW_USERINFORM_CODE = 00005;
    private static final int SHAKE_ATTEMTION = 00006;
    private List<ShakeStarCommendResponse.ShakingStarListBean> starCommendResponses=new ArrayList<>();
    private List<CommendAllResponse.CommentsListBean> commendlist=new ArrayList<>();

    private int page=0;
    public IsInfo isInfo;
    public void loadData(boolean showLoadingView,String searchStr,int sortType,int page,int rows) {
        this.page=page;
        ShakeStarSelectRequest request = new ShakeStarSelectRequest();
        request.setSearchStr(searchStr);
        request.setSortType(sortType);
        request.setPage(page);
        request.setRows(rows);

        super.loadData(request,
                HeaderRequest.SHAKESTAR_COMMEND,
                NEW_COMMEND_CODE,
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
    public void loadAttention(boolean showLoadingView,String starId,String operation){
        AttentionRequest request=new AttentionRequest();
        request.setStarId(starId);
        request.setOperation(operation);
        super.loadData(request,
                HeaderRequest.CONCERNED_ON_STAR,
                SHAKE_ATTEMTION,
                showLoadingView);
    }
    public void CommendContnet(boolean showLoadingView,String contentId,String operationType,String content) {
        CommendContentRequest request = new CommendContentRequest();
        request.setContentId(contentId);
        request.setOperationType(operationType);
        request.setContent(content);

        super.loadData(request,
                HeaderRequest.SUBJECT_COMMENT_SUBMIT,
                NEW_COMMENDCONTENT_CODE,
                showLoadingView);
    }
    public void loadAllCommend(boolean showLoadingView,String contentId,String currentUserId,int page,int rows,int subPage,int subRows) {
        CommendAllRequest request = new CommendAllRequest();
        request.setContentId(contentId);
        request.setCurrentUserId(currentUserId);
        request.setRows(rows);
        request.setPage(page);
        request.setSubPage(subPage);
        request.setSubRows(subRows);

        super.loadData(request,
                HeaderRequest.ALL_COMMENT,
                NEW_COMMENDALL_CODE,
                showLoadingView);
    }
    public void Like(boolean showLoadingView,String contentId,int praiseType) {
        CommendLikeRequest request = new CommendLikeRequest();
        request.setContentId(contentId);
        request.setPraiseType(praiseType);

        super.loadData(request,
                HeaderRequest.SUBJECT_PRAISE,
                NEW_LIKE_CODE,
                showLoadingView);
    }
    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId){
            case NEW_COMMEND_CODE:
                L.e("tag",responseJson);
                ShakeStarCommendResponse commendResponse=JsonUtils.fromJson(responseJson, ShakeStarCommendResponse.class);

                if(COMMON_SUCCESS.equals(commendResponse.getCode())){

                    if(commendResponse.getShakingStarList()!=null){
                        if(page==0){
                            starCommendResponses.clear();
                        }
//                        for (int i = 0; i < commendResponse.getShakingStarList().size(); i++) {
//                            Log.e("TAG",commendResponse.getShakingStarList().get(i).getContentDesc());
//                            starCommendResponses.add(commendResponse.getShakingStarList().get(i));
//                        }
                        starCommendResponses.addAll(commendResponse.getShakingStarList());
                        mView.showView(starCommendResponses);
                        isInfo.Info(0);
                    }else{

                    }
                }else{
                    showToast(commendResponse.getMsg());
                }
                break;
            case NEW_LIKE_CODE:
                ShakeStarLikeResponse likeResponse=JsonUtils.fromJson(responseJson,ShakeStarLikeResponse.class);
                if(COMMON_SUCCESS.equals(likeResponse.getCode())){
                   L.e("TAG","+1");
                }
                break;
            case NEW_COMMENDALL_CODE:
                //获取所有评论
                CommendAllResponse commendAllResponse=JsonUtils.fromJson(responseJson, CommendAllResponse.class);

                if(COMMON_SUCCESS.equals(commendAllResponse.getCode())){

                    if(commendAllResponse.getCommentsList()!=null){
                        if(page==0){
                            commendlist.clear();
                        }
//                        for (int i = 0; i < commendAllResponse.getCommentsList().size(); i++) {
//                            L.e("TAG",commendAllResponse.getCommentsList().size());
//                            commendlist.add(commendAllResponse.getCommentsList().get(i));
//                        }
                        commendlist.addAll(commendAllResponse.getCommentsList());
                        mView.showCommend(commendlist,commendAllResponse);
                    }else{

                    }
                }else{
                    showToast(commendAllResponse.getMsg());
                }
                break;
            case NEW_COMMENDCONTENT_CODE:
                //发表评论
                break;
            case NEW_USERINFORM_CODE:
                //举报
                break;
            case  SHAKE_ATTEMTION:
                //关注
                L.e("TAG",responseJson);
                break;

        }
    }
    public interface IsInfo{
        void Info(int k);
    }
    public void clear(){
        commendlist.clear();
    }
    public void setIsInfo(IsInfo isInfo) {
        this.isInfo = isInfo;
    }
}
