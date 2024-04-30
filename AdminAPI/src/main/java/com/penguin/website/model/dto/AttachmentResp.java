package com.penguin.website.model.dto;

import com.penguin.website.model.domain.Attachment;

public class AttachmentResp extends Attachment {
    //买家姓名
    private String userName;
    //买家账号
    private String realName;
    //订单编号
    private String orderNo;
    //下单时间
    private String orderTime;
    //总价
    private String payAmount;
    //单价
    private String orderUnitPrice;
    //数量
    private String saroNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getOrderUnitPrice() {
        return orderUnitPrice;
    }

    public void setOrderUnitPrice(String orderUnitPrice) {
        this.orderUnitPrice = orderUnitPrice;
    }

    public String getSaroNumber() {
        return saroNumber;
    }

    public void setSaroNumber(String saroNumber) {
        this.saroNumber = saroNumber;
    }

}
