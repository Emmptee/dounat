package com.donut.app.http.message.subjectStar;

public class SubjectStarRequest
{
    private String starId;

    private String fkA01Id;
    
    private String currentUserId;

    public String getStarId()
    {
        return starId;
    }

    public void setStarId(String starId)
    {
        this.starId = starId;
    }

    public String getFkA01Id() {
        return fkA01Id;
    }

    public void setFkA01Id(String fkA01Id) {
        this.fkA01Id = fkA01Id;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
