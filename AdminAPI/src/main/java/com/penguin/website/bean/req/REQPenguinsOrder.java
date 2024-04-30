package com.penguin.website.bean.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * penguins_order
 * @author 
 */
public class REQPenguinsOrder extends PageParam implements Serializable {

    private static final long serialVersionUID = 3371817225806084098L;
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private Long orderNo;

    @ApiModelProperty(value = "订单编号Str")
    private Long orderNoStr;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "商家账号")
    private String merchantAccount;

    @ApiModelProperty(value = "订单类型：1充值 2卖出")
    private Integer orderType;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "下单用户")
    private String orderUserId;

    @ApiModelProperty(value = "交易人")
    private String orderTransactorId;

    @ApiModelProperty(value = "下单开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startOrderTime;

    @ApiModelProperty(value = "下单结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endOrderTime;

    @ApiModelProperty(value = "成交开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startConfirmTime;

    @ApiModelProperty(value = "成交结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endConfirmTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderTransactorId() {
        return orderTransactorId;
    }

    public void setOrderTransactorId(String orderTransactorId) {
        this.orderTransactorId = orderTransactorId;
    }

    public Long getOrderNoStr() {
        return orderNoStr;
    }

    public void setOrderNoStr(Long orderNoStr) {
        this.orderNoStr = orderNoStr;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public Date getStartOrderTime() {
        return startOrderTime;
    }

    public void setStartOrderTime(Date startOrderTime) {
        this.startOrderTime = startOrderTime;
    }

    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    public Date getStartConfirmTime() {
        return startConfirmTime;
    }

    public void setStartConfirmTime(Date startConfirmTime) {
        this.startConfirmTime = startConfirmTime;
    }

    public Date getEndConfirmTime() {
        return endConfirmTime;
    }

    public void setEndConfirmTime(Date endConfirmTime) {
        this.endConfirmTime = endConfirmTime;
    }
}