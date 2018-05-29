package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/30.
 */
public class SubjectRequest
{
    String subjectId;

    private int commentRows = 5;

    public int getCommentRows() {
        return commentRows;
    }

    public void setCommentRows(int commentRows) {
        this.commentRows = commentRows;
    }

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }
}
