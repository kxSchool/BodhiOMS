package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "后台用户信息")
public class RspSystemUser implements Serializable {

	private static final long serialVersionUID = 7423047947642467516L;

	@ApiModelProperty(value = "")
	private Integer userId;

	@ApiModelProperty(value = "用户名称")
	private String userName;

	@ApiModelProperty(value = "登录失败次数")
	private Integer loginError;

	@ApiModelProperty(value = "最后登录时间")
	private Long loginLastTime;

	@ApiModelProperty(value = "创建时间")
	private Long createTime;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getLoginError() {
		return loginError;
	}

	public void setLoginError(Integer loginError) {
		this.loginError = loginError;
	}

	public Long getLoginLastTime() {
		return loginLastTime;
	}

	public void setLoginLastTime(Long loginLastTime) {
		this.loginLastTime = loginLastTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
}
