package com.donut.app.http.message;

public class OrderOperationRequest
{
    private String orderId;
    
    private String operationType;

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }
    
}
