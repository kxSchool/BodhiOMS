package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "订单信息")
public class RspOrderInfo implements Serializable {

	private static final long serialVersionUID = -5705587972626340445L;

	@ApiModelProperty("订单id")
	private Integer orderId;

	/**
	 * 订单编号
	 */
	@ApiModelProperty("订单编号")
	private Long orderNo;

	@ApiModelProperty("订单编号String")
	private String orderNoStr;

	@ApiModelProperty("用户id")
	private String userId;

	@ApiModelProperty("用户名称")
	private String userName;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "用户真实姓名")
	private String realName;

	@ApiModelProperty("订单状态,充值：空为全部,1 用户：待付款；商家：待收款,2  待放行,3  已完成,4  已取消,5  已关闭-->卖出:空为全部, 1 用户待收款 商家待付款,3  已完成,4  已取消")
	private Integer orderStatus;

	@ApiModelProperty("订单时间")
	private Date orderTime;

	@ApiModelProperty("订单时间时间戳")
	private Long orderTimeStr;

	@ApiModelProperty("放行时间戳")
	private Long confirmTimeStr;

	@ApiModelProperty("支付时间戳")
	private Long payTimeStr;

	@ApiModelProperty("订单saro数量")
	private BigDecimal saroNumber;

	@ApiModelProperty("实际支付金额")
	private BigDecimal payAmount;

	@ApiModelProperty("下单单价")
	private BigDecimal orderUnitPrice;

	@ApiModelProperty(value = "卖出单价")
	private BigDecimal sellUnitPrice;

	@ApiModelProperty("对方电话")
	private String phone;

	@ApiModelProperty(value = "有效时间(分钟)")
	private Integer validTime;

	@ApiModelProperty(value = "用户电话")
	private String userPhone;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public Long getOrderTimeStr() {
		return orderTimeStr;
	}

	public void setOrderTimeStr(Long orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}

	public BigDecimal getSaroNumber() {
		return saroNumber;
	}

	public void setSaroNumber(BigDecimal saroNumber) {
		this.saroNumber = saroNumber;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderNoStr() {
		return orderNoStr;
	}

	public void setOrderNoStr(String orderNoStr) {
		this.orderNoStr = orderNoStr;
	}

	public BigDecimal getOrderUnitPrice() {
		return orderUnitPrice;
	}

	public void setOrderUnitPrice(BigDecimal orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice;
	}

	public BigDecimal getSellUnitPrice() {
		return sellUnitPrice;
	}

	public void setSellUnitPrice(BigDecimal sellUnitPrice) {
		this.sellUnitPrice = sellUnitPrice;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getConfirmTimeStr() {
		return confirmTimeStr;
	}

	public void setConfirmTimeStr(Long confirmTimeStr) {
		this.confirmTimeStr = confirmTimeStr;
	}

	public Long getPayTimeStr() {
		return payTimeStr;
	}

	public void setPayTimeStr(Long payTimeStr) {
		this.payTimeStr = payTimeStr;
	}

	public Integer getValidTime() {
		return validTime;
	}

	public void setValidTime(Integer validTime) {
		this.validTime = validTime;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
}
