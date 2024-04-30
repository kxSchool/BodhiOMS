package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "用户信息参数")
public class ReqUser extends ReqToken implements Serializable {
	private static final long serialVersionUID = 1040819739649490910L;

	@ApiModelProperty(value = "用户id,为空则新增，不空则修改")
	private String userId;

	@ApiModelProperty(value = "用户电话")
	private String userPhone;

	@ApiModelProperty(value = "用户电话2")
	private String userPhone2;

	@ApiModelProperty(value = "用户电话3")
	private String userPhone3;

	@ApiModelProperty(value = "交易密码")
	private String userTransPwd;

	@ApiModelProperty(value = "收款人")
	private String bankFavour;

	@ApiModelProperty(value = "用户开户行")
	private String userBank;

	@ApiModelProperty(value = "用户银行卡号")
	private String userBankCard;

	@ApiModelProperty(value = "开户支行")
	private String userBankBranch;

	@ApiModelProperty(value = "验证码")
	private String code;

	@ApiModelProperty(value = "充值单价（每个saro的价格）")
	private BigDecimal rechargeUnitPrice = BigDecimal.ZERO;

	@ApiModelProperty(value = "回收单价（每个saro的价格）")
	private BigDecimal recoveryUnitPrice = BigDecimal.ZERO;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPhone2() {
		return userPhone2;
	}

	public void setUserPhone2(String userPhone2) {
		this.userPhone2 = userPhone2;
	}

	public String getUserPhone3() {
		return userPhone3;
	}

	public void setUserPhone3(String userPhone3) {
		this.userPhone3 = userPhone3;
	}

	public String getUserTransPwd() {
		return userTransPwd;
	}

	public void setUserTransPwd(String userTransPwd) {
		this.userTransPwd = userTransPwd;
	}

	public String getBankFavour() {
		return bankFavour;
	}

	public void setBankFavour(String bankFavour) {
		this.bankFavour = bankFavour;
	}

	public String getUserBank() {
		return userBank;
	}

	public void setUserBank(String userBank) {
		this.userBank = userBank;
	}

	public String getUserBankCard() {
		return userBankCard;
	}

	public void setUserBankCard(String userBankCard) {
		this.userBankCard = userBankCard;
	}

	public String getUserBankBranch() {
		return userBankBranch;
	}

	public void setUserBankBranch(String userBankBranch) {
		this.userBankBranch = userBankBranch;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
