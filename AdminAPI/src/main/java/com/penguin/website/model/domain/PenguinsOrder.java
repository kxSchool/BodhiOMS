package com.penguin.website.model.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * penguins_order
 * @author 
 */
public class PenguinsOrder implements Serializable {
    private Integer id;

    /**
     * 订单编号
     */
    private Long orderNo;

    /**
     * 订单类型：1充值 2卖出
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 下单用户
     */
    private String orderUserId;

    /**
     * 下单用实名
     */
    private String orderUserRealName;

    /**
     * 下单用户账号
     */
    private String orderUserAccount;

    /**
     * 下单用户昵称
     */
    private String orderUserNickname;

    /**
     * 下单金额CNY
     */
    private BigDecimal orderPriceCny;

    /**
     * 下单金额saro
     */
    private BigDecimal orderPriceSaro;

    /**
     * 下单单价
     */
    private BigDecimal orderUnitPrice;

    /**
     * 订单实付金额
     */
    private BigDecimal payAmount;

    /**
     * 交易人
     */
    private String orderTransactorId;

    /**
     * 交易用户账号
     */
    private String orderTransactorAccount;

    /**
     * 交易凭证路径
     */
    private String transactorProofPath;

    /**
     * 实际saro
     */
    private BigDecimal orderActualSaro;

    /**
     * 备注
     */
    private String orderRemark;

    /**
     * 用户快照(ps:json)
     */
    private String userSnapshot;

    /**
     * 商家快照(ps:json)
     */
    private String merchantSnapshot;

    /**
     * 放行时间
     */
    private Date confirmTime;

    /**
     * 支付时间
     */
    private Date payTime;

    private Date updateTime;

    /**
     * 退货情况信息
     */
    private String backMessage;

    private static final long serialVersionUID = 1L;

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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderUserRealName() {
        return orderUserRealName;
    }

    public void setOrderUserRealName(String orderUserRealName) {
        this.orderUserRealName = orderUserRealName;
    }

    public String getOrderUserAccount() {
        return orderUserAccount;
    }

    public void setOrderUserAccount(String orderUserAccount) {
        this.orderUserAccount = orderUserAccount;
    }

    public String getOrderUserNickname() {
        return orderUserNickname;
    }

    public void setOrderUserNickname(String orderUserNickname) {
        this.orderUserNickname = orderUserNickname;
    }

    public BigDecimal getOrderPriceCny() {
        return orderPriceCny;
    }

    public void setOrderPriceCny(BigDecimal orderPriceCny) {
        this.orderPriceCny = orderPriceCny;
    }

    public BigDecimal getOrderPriceSaro() {
        return orderPriceSaro;
    }

    public void setOrderPriceSaro(BigDecimal orderPriceSaro) {
        this.orderPriceSaro = orderPriceSaro;
    }

    public BigDecimal getOrderUnitPrice() {
        return orderUnitPrice;
    }

    public void setOrderUnitPrice(BigDecimal orderUnitPrice) {
        this.orderUnitPrice = orderUnitPrice;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getOrderTransactorId() {
        return orderTransactorId;
    }

    public void setOrderTransactorId(String orderTransactorId) {
        this.orderTransactorId = orderTransactorId;
    }

    public String getOrderTransactorAccount() {
        return orderTransactorAccount;
    }

    public void setOrderTransactorAccount(String orderTransactorAccount) {
        this.orderTransactorAccount = orderTransactorAccount;
    }

    public String getTransactorProofPath() {
        return transactorProofPath;
    }

    public void setTransactorProofPath(String transactorProofPath) {
        this.transactorProofPath = transactorProofPath;
    }

    public BigDecimal getOrderActualSaro() {
        return orderActualSaro;
    }

    public void setOrderActualSaro(BigDecimal orderActualSaro) {
        this.orderActualSaro = orderActualSaro;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getUserSnapshot() {
        return userSnapshot;
    }

    public void setUserSnapshot(String userSnapshot) {
        this.userSnapshot = userSnapshot;
    }

    public String getMerchantSnapshot() {
        return merchantSnapshot;
    }

    public void setMerchantSnapshot(String merchantSnapshot) {
        this.merchantSnapshot = merchantSnapshot;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getBackMessage() {
        return backMessage;
    }

    public void setBackMessage(String backMessage) {
        this.backMessage = backMessage;
    }
}