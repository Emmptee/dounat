package com.donut.app.http.message;

public class UserLoginRequest
{
    private String userName;

    private String passwd;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }
}
