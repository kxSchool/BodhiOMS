package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "获取用户信息page")
public class ReqUserPage extends PageParam implements Serializable {

	private static final long serialVersionUID = -1354011717006053622L;

	@ApiModelProperty(value = "用户角色1用户2商家")
	private Integer userRole;

	@ApiModelProperty(value = "用户名称")
	private String userName;

	@ApiModelProperty(value = "用户账号")
	private String userAccount;

	@ApiModelProperty(value = "用户电话")
	private String userPhone;

	@ApiModelProperty(value = "用户状态：1正常")
	private Integer userStatus;

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
}
