package com.donut.app.mvp.shakestar.select.particulars;

import com.donut.app.http.CommendAllRequest;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.AttentionRequest;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.CommendContentRequest;
import com.donut.app.http.message.InformRequest;
import com.donut.app.http.message.ParticularsRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShakeStarCommendRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.StarCollectRequest;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.http.message.wish.CommendLikeRequest;
import com.donut.app.http.message.wish.ShakeStarSelectRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */
//<V extends HomeContract.View> extends HomeContract.Presenter <V>
public class ParticularsPresenter<V extends ParticularsContract.View> extends ParticularsContract.Presenter<V> {
    private static final int NEW_PARTICULARS_CODE = 0000;
    private static final int NEW_SHARE_CODE = 0001;
    private static final int NEW_COLLECT_CODE = 0002;
    private static final int NEW_LIKE_CODE = 00003;
    private static final int NEW_COMMENDALL_CODE = 00006;
    private static final int NEW_COMMENDCONTENT_CODE = 00005;
    private static final int NEW_USERINFORM_CODE = 00007;
    private static final int SHAKE_ATTEMTION = 00004;
    private List<ParticularsResponse.ShakingStarListBean> starCommendResponses=new ArrayList<>();
    private List<CommendAllResponse.CommentsListBean> commendlist=new ArrayList<>();

    private int page=0;

    public void loadData(boolean showLoadingView,String b02Id,String g03Id,int page,int rows) {
        this.page=page;
        ParticularsRequest request = new ParticularsRequest();
        request.setB02Id(b02Id);
        request.setG03Id(g03Id);
        request.setPage(page);
        request.setRows(rows);
        super.loadData(request,
                HeaderRequest.SHAKESTAR_PARTICULARS,
                NEW_PARTICULARS_CODE,
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
    public void loadAttention(boolean showLoadingView,String starId,String operation){
        AttentionRequest request=new AttentionRequest();
        request.setStarId(starId);
        request.setOperation(operation);
        super.loadData(request,
                HeaderRequest.CONCERNED_ON_STAR,
                SHAKE_ATTEMTION,
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
    public void Share(boolean showLoadingView,String contentId,String subjectId) {
        ShareRequest request = new ShareRequest();
        request.setContentId(contentId);
        request.setSubjectId(subjectId);

        super.loadData(request,
                HeaderRequest.SHARE,
                NEW_SHARE_CODE,
                showLoadingView);
    }
    public void Collect(boolean showLoadingView,String contentId,int type,int status) {
        CollectRequest request=new CollectRequest();
        request.setContentId(contentId);
        request.setType(type);
        request.setStatus(status);
        super.loadData(request,
                HeaderRequest. SUBJECT_COLLECT,
                NEW_COLLECT_CODE,
                showLoadingView);
    }
    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case NEW_PARTICULARS_CODE:
               L.e("json",responseJson);
                ParticularsResponse particularsResponse=JsonUtils.fromJson(responseJson, ParticularsResponse.class);
                if(COMMON_SUCCESS.equals(particularsResponse.getCode())){
                    if(particularsResponse.getShakingStarList()!=null){
                        if(page==0){
                            starCommendResponses.clear();
                        }
//                        for (int i = 0; i < commendResponse.getShakingStarList().size(); i++) {
//                            Log.e("TAG",commendResponse.getShakingStarList().get(i).getContentDesc());
//                            starCommendResponses.add(commendResponse.getShakingStarList().get(i));
//                        }
                        starCommendResponses.addAll(particularsResponse.getShakingStarList());
                        mView.showView(starCommendResponses,particularsResponse);
                    }

                }
                break;
            case NEW_SHARE_CODE:
                 //分享
                break;
            case NEW_COLLECT_CODE:
                //收藏||取消收藏
                L.e("json",responseJson);
                break;
            case NEW_LIKE_CODE:
                //赞
                break;
            case NEW_COMMENDCONTENT_CODE:
                //发表评论
                break;
            case NEW_COMMENDALL_CODE:
                //获取全部评论
                //获取所有评论
                L.e("JSON",responseJson);
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
            case NEW_USERINFORM_CODE:
                //举报
                break;
            case  SHAKE_ATTEMTION:
                //关注
                L.e("TAG",responseJson);
                break;
        }
    }
    public void clear(){
        commendlist.clear();
    }
}
