package com.donut.app.http.message;

import java.util.ArrayList;
import java.util.List;

public class StarSubjectsResponse extends BaseResponse
{
    
    private List<SubjectListDetail> starSubjects = new ArrayList<SubjectListDetail>();

    public List<SubjectListDetail> getStarSubjects()
    {
        return starSubjects;
    }

    public void setStarSubjects(List<SubjectListDetail> starSubjects)
    {
        this.starSubjects = starSubjects;
    }

    @Override
    public String toString()
    {
        return "StarSubjectsResponse [starSubjects=" + starSubjects + "]";
    }
}
