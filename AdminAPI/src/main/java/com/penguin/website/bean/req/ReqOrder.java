package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "创建订单信息")
public class ReqOrder extends ReqToken implements Serializable {

    private static final long serialVersionUID = 7067128096575849338L;

    @ApiModelProperty(value = "交易人", required = true)
    private String orderTransactorId;

    @ApiModelProperty(value = "下单saro数量，与下单金额互斥")
    private BigDecimal orderPriceSaro = BigDecimal.ZERO;

    @ApiModelProperty(value = "下单金额CNY，与下单saro数量互斥")
    private BigDecimal orderPriceCny = BigDecimal.ZERO;

    @ApiModelProperty(value = "下单单价", required = true)
    private BigDecimal orderUnitPrice;

    @ApiModelProperty(value = "订单实付金额")
    @DecimalMin(value = "0.0", message = "实付金额不能小于0")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单实付金额")
    @DecimalMin(value = "0.0", message = "实付金额不能小于0")
    private BigDecimal payAmou;

    @ApiModelProperty(value = "订单类型1充值2卖出", required = true)
    private Integer orderType;

    public String getOrderTransactorId() {
        return orderTransactorId;
    }

    public void setOrderTransactorId(String orderTransactorId) {
        this.orderTransactorId = orderTransactorId;
    }

    public BigDecimal getOrderPriceSaro() {
        return orderPriceSaro;
    }

    public void setOrderPriceSaro(BigDecimal orderPriceSaro) {
        this.orderPriceSaro = orderPriceSaro;
    }

    public BigDecimal getOrderPriceCny() {
        return orderPriceCny;
    }

    public void setOrderPriceCny(BigDecimal orderPriceCny) {
        this.orderPriceCny = orderPriceCny;
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

    public BigDecimal getPayAmou() { return payAmou; }

    public void setPayAmou(BigDecimal payAmou) { this.payAmou = payAmou; }

    public Integer getOrderType() { return orderType; }

    public void setOrderType(Integer orderType) { this.orderType = orderType; }
}
