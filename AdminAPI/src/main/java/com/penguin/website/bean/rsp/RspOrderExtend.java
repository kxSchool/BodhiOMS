package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "订单信息扩展")
public class RspOrderExtend extends RspOrderInfo implements Serializable {

	private static final long serialVersionUID = -3874019411314383797L;

	@ApiModelProperty(value = "用户电话2")
	private String userPhone2;

	@ApiModelProperty(value = "用户电话3")
	private String userPhone3;

	@ApiModelProperty(value = "收款人")
	private String bankFavour;

	@ApiModelProperty(value = "用户开户行")
	private String userBank;

	@ApiModelProperty(value = "用户银行卡号")
	private String userBankCard;

	@ApiModelProperty(value = "开户支行")
	private String userBankBranch;

	@ApiModelProperty(value = "用户账号")
	private String userAccount;

	@ApiModelProperty(value = "商家账号")
	private String merchantAccount;

	@ApiModelProperty(value = "商家电话")
	private String merchantPhone;

	@ApiModelProperty(value = "商家电话2")
	private String merchantPhone2;

	@ApiModelProperty(value = "商家电话3")
	private String merchantPhone3;

	@ApiModelProperty(value = "商家昵称")
	private String merchantNickname;

	@ApiModelProperty(value = "凭证")
	private String transactorProof;

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

	public String getMerchantPhone() {
		return merchantPhone;
	}

	public String getMerchantPhone2() {
		return merchantPhone2;
	}

	public void setMerchantPhone2(String merchantPhone2) {
		this.merchantPhone2 = merchantPhone2;
	}

	public String getMerchantPhone3() {
		return merchantPhone3;
	}

	public void setMerchantPhone3(String merchantPhone3) {
		this.merchantPhone3 = merchantPhone3;
	}

	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}

	public String getMerchantNickname() {
		return merchantNickname;
	}

	public void setMerchantNickname(String merchantNickname) {
		this.merchantNickname = merchantNickname;
	}

	public String getTransactorProof() {
		return transactorProof;
	}

	public void setTransactorProof(String transactorProof) {
		this.transactorProof = transactorProof;
	}
}
