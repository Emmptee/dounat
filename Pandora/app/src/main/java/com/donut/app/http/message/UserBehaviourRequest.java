package com.donut.app.http.message;

import java.util.ArrayList;
import java.util.List;


public class UserBehaviourRequest
{
    private long systemTime;
    private List<UserBehaviour> userBehaviours = new ArrayList<UserBehaviour>();

    public List<UserBehaviour> getUserBehaviours()
    {
        return userBehaviours;
    }

    public void setUserBehaviours(List<UserBehaviour> userBehaviours)
    {
        this.userBehaviours = userBehaviours;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }
}
