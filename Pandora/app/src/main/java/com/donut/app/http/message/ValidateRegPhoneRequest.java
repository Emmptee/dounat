package com.donut.app.http.message;

public class ValidateRegPhoneRequest extends BaseRequest {
    private String phone;

    private String validateType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidateType() {
        return validateType;
    }

    public void setValidateType(String validateType) {
        this.validateType = validateType;
    }
}
