package com.donut.app.http.message;



public class RetrievePasswdResponse extends BaseResponse
{
    private String password;

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
