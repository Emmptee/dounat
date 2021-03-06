package com.donut.app.http.message;

import java.util.List;

public class CommentSearchResponse extends BaseResponse
{
    private List<ContentComments> hotComments;
    
    private List<ContentComments> commentsList;

    private ContentComments contentCommentsDetail;

    private List<ContentComments> myComments;

    public ContentComments getContentCommentsDetail()
    {
        return contentCommentsDetail;
    }

    public void setContentCommentsDetail(ContentComments contentCommentsDetail)
    {
        this.contentCommentsDetail = contentCommentsDetail;
    }

    public List<ContentComments> getMyComments()
    {
        return myComments;
    }

    public void setMyComments(List<ContentComments> myComments)
    {
        this.myComments = myComments;
    }

    public List<ContentComments> getHotComments()
    {
        return hotComments;
    }

    public void setHotComments(List<ContentComments> hotComments)
    {
        this.hotComments = hotComments;
    }

    public List<ContentComments> getCommentsList()
    {
        return commentsList;
    }

    public void setCommentsList(List<ContentComments> commentsList)
    {
        this.commentsList = commentsList;
    }

    
}
