package com.donut.app.http.message;

public class ConcernedOnStarRequest
{
    private String starId;
    
    private String operation;

    public String getStarId()
    {
        return starId;
    }

    public void setStarId(String starId)
    {
        this.starId = starId;
    }

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
}
