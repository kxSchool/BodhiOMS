package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "用户编辑密码信息")
public class ReqPwdEdit implements Serializable {
	private static final long serialVersionUID = -5163999390229237976L;

	@ApiModelProperty(value = "原密码")
	private String formerlyPwd;

	@ApiModelProperty(value = "新密码")
	private String newPwd;

	@ApiModelProperty(value = "确认密码")
	private String confirmPwd;

	@ApiModelProperty(value = "类型：1新增 2修改")
	private Integer type;

	public String getFormerlyPwd() {
		return formerlyPwd;
	}

	public void setFormerlyPwd(String formerlyPwd) {
		this.formerlyPwd = formerlyPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}

