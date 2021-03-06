package com.donut.app.http.message;

public class UserPasswdChangeRequest
{

    private String passwd;

    private String newPasswd;

    private String confirmPasswd;

    /**
     * @return passwd
     */
    public String getPasswd()
    {
        return passwd;
    }

    /**
     * @param passwd
     *            the passwd to set
     */
    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    /**
     * @return content
     */
    public String getNewPasswd()
    {
        return newPasswd;
    }

    public void setNewPasswd(String newPasswd)
    {
        this.newPasswd = newPasswd;
    }

    /**
     * @return confirmPasswd
     */
    public String getConfirmPasswd()
    {
        return confirmPasswd;
    }

    /**
     * @param confirmPasswd
     *            the confirmPasswd to set
     */
    public void setConfirmPasswd(String confirmPasswd)
    {
        this.confirmPasswd = confirmPasswd;
    }

}
