package com.donut.app.http.message;

public class MyAppreciatesRequest extends BaseRequest
{
    private String subjectId;

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }
    
}
