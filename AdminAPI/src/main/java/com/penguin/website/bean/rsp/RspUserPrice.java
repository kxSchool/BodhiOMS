package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "商家相关")
public class RspUserPrice implements Serializable {

	private static final long serialVersionUID = -8604115285508506300L;
	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "用户名称")
	private String userName;

	@ApiModelProperty(value = "用户昵称")
	private String nickname;

	@ApiModelProperty(value = "用户saro")
	private BigDecimal saroNumber;

	@ApiModelProperty(value = "最小额度")
	private String minRecharge;

	@ApiModelProperty(value = "最大额度")
	private String maxRecharge;

	@ApiModelProperty(value = "充值单价（每个saro的价格）")
	private BigDecimal rechargeUnitPrice;

	@ApiModelProperty(value = "回收单价（每个saro的价格）")
	private BigDecimal recoveryUnitPrice;

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

	public BigDecimal getSaroNumber() {
		return saroNumber;
	}

	public void setSaroNumber(BigDecimal saroNumber) {
		this.saroNumber = saroNumber;
	}

	public String getMinRecharge() {
		return minRecharge;
	}

	public void setMinRecharge(String minRecharge) {
		this.minRecharge = minRecharge;
	}

	public String getMaxRecharge() {
		return maxRecharge;
	}

	public void setMaxRecharge(String maxRecharge) {
		this.maxRecharge = maxRecharge;
	}

	public BigDecimal getRechargeUnitPrice() {
		return rechargeUnitPrice;
	}

	public void setRechargeUnitPrice(BigDecimal rechargeUnitPrice) {
		this.rechargeUnitPrice = rechargeUnitPrice;
	}

	public BigDecimal getRecoveryUnitPrice() {
		return recoveryUnitPrice;
	}

	public void setRecoveryUnitPrice(BigDecimal recoveryUnitPrice) {
		this.recoveryUnitPrice = recoveryUnitPrice;
	}
}
