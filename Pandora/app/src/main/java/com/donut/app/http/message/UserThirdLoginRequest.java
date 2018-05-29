package com.donut.app.http.message;

public class UserThirdLoginRequest
{
    private String thirdTag;
    
    private String nickName;
    
    /*private String sex;*/
    
    private String imgPath;
    
    private String regPlatform;

    private String phoneType;

    public String getPhoneType()
    {
        return phoneType;
    }

    public void setPhoneType(String phoneType)
    {
        this.phoneType = phoneType;
    }

    public String getThirdTag()
    {
        return thirdTag;
    }

    public void setThirdTag(String thirdTag)
    {
        this.thirdTag = thirdTag;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

   /* public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }
*/
    public String getImgPath()
    {
        return imgPath;
    }

    public void setImgPath(String imgPath)
    {
        this.imgPath = imgPath;
    }

    public String getRegPlatform()
    {
        return regPlatform;
    }

    public void setRegPlatform(String regPlatform)
    {
        this.regPlatform = regPlatform;
    }
    
}
