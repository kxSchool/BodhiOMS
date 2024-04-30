package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "用户扩展信息")
public class RspUserInfo extends RspUser implements Serializable {

	private static final long serialVersionUID = 4935545186174113864L;

	@ApiModelProperty(value = "用户昵称")
	private String nickname;

	@ApiModelProperty(value = "用户账号")
	private String userAccount;

	@ApiModelProperty(value = "用户saro数量")
	private BigDecimal userSaroNumber = BigDecimal.ZERO;

	@ApiModelProperty(value = "充值单价（每个saro的价格）")
	private BigDecimal rechargeUnitPrice;

	@ApiModelProperty(value = "回收单价（每个saro的价格）")
	private BigDecimal recoveryUnitPrice;

	@ApiModelProperty(value = "用户状态：1正常 0禁用")
	private Integer userStatus;

	@ApiModelProperty(value = "角色：1用户 2商家")
	private Integer userRole;

	@ApiModelProperty(value = "创建时间")
	private Long createTime;

	@ApiModelProperty(value = "最小充值数")
	private BigDecimal minRecharge;

	@ApiModelProperty(value = "最大充值数")
	private BigDecimal maxRecharge;

	@ApiModelProperty(value = "游戏token")
	private String userToken;

	@ApiModelProperty(value = "token状态 : 0 失效 1 正常")
	private Integer userTokenStatus;

	@ApiModelProperty(value = "密码出错次数")
	private Integer userTramsPwdErrorCount;

	@ApiModelProperty(value = "接口返回信息")
	private RspGameUser rspGameUser;

	public Integer getUserTokenStatus() {
		return userTokenStatus;
	}

	public void setUserTokenStatus(Integer userTokenStatus) {
		this.userTokenStatus = userTokenStatus;
	}

	public Integer getUserTramsPwdErrorCount() {
		return userTramsPwdErrorCount;
	}

	public void setUserTramsPwdErrorCount(Integer userTramsPwdErrorCount) {
		this.userTramsPwdErrorCount = userTramsPwdErrorCount;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public BigDecimal getMinRecharge() {
		return minRecharge;
	}

	public void setMinRecharge(BigDecimal minRecharge) {
		this.minRecharge = minRecharge;
	}

	public BigDecimal getMaxRecharge() {
		return maxRecharge;
	}

	public void setMaxRecharge(BigDecimal maxRecharge) {
		this.maxRecharge = maxRecharge;
	}

	public RspGameUser getRspGameUser() {
		return rspGameUser;
	}

	public void setRspGameUser(RspGameUser rspGameUser) {
		this.rspGameUser = rspGameUser;
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

	public BigDecimal getUserSaroNumber() {
		return userSaroNumber;
	}

	public void setUserSaroNumber(BigDecimal userSaroNumber) {
		this.userSaroNumber = userSaroNumber;
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

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
}
