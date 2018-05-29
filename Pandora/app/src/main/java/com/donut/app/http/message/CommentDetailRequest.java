package com.donut.app.http.message;

public class CommentDetailRequest extends BaseRequest 
{
    private String commentId;
    
    private int subCommentPage;
    
    private int subCommentRows;
    
    private int praisePage;
    
    private int praiseRows;
    
    private String currentUserId;

    public String getCommentId()
    {
        return commentId;
    }

    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }

    public int getSubCommentPage()
    {
        return subCommentPage;
    }

    public void setSubCommentPage(int subCommentPage)
    {
        this.subCommentPage = subCommentPage;
    }

    public int getSubCommentRows()
    {
        return subCommentRows;
    }

    public void setSubCommentRows(int subCommentRows)
    {
        this.subCommentRows = subCommentRows;
    }

    public int getPraisePage()
    {
        return praisePage;
    }

    public void setPraisePage(int praisePage)
    {
        this.praisePage = praisePage;
    }

    public int getPraiseRows()
    {
        return praiseRows;
    }

    public void setPraiseRows(int praiseRows)
    {
        this.praiseRows = praiseRows;
    }

    public String getCurrentUserId()
    {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId)
    {
        this.currentUserId = currentUserId;
    }

    @Override
    public String toString()
    {
        return "CommentDetailRequest [commentId=" + commentId + ", subCommentPage=" + subCommentPage + ", subCommentRows=" + subCommentRows + ", praisePage=" + praisePage + ", praiseRows=" + praiseRows + ", currentUserId=" + currentUserId + "]";
    }
}
