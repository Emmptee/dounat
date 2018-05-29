package com.donut.app.http.message;

public class RetrievePasswdRequest
{
    private String phone;
    
    private String password;
    
    private String validateCode;
    
    private String deviceType;

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getValidateCode()
    {
        return validateCode;
    }

    public void setValidateCode(String validateCode)
    {
        this.validateCode = validateCode;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }
}
