package com.donut.app.http.message;

public class CommentPraiseRequest extends BaseRequest
{
    private String commentId;
    
    private String operationType;

    public String getCommentId()
    {
        return commentId;
    }

    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }
    
}
