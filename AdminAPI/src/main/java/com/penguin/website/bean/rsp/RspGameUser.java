package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "游戏用户信息")
public class RspGameUser implements Serializable {

	private static final long serialVersionUID = 6290332995072720984L;

	@ApiModelProperty(value = "")
	private String user_id;

	@ApiModelProperty(value = "用户昵称")
	private String nickname;

	@ApiModelProperty(value = "用户名")
	private String user_name;

	@ApiModelProperty(value = "saro")
	private BigDecimal saro_quantity;

	private Integer user_type;

	@ApiModelProperty(value = "0：认证未通过  1：认证通过 2：认证中 3：未认证")
	private Integer user_real_name_state;

	@ApiModelProperty(value = "用户实名名称")
	private String user_real_name;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public BigDecimal getSaro_quantity() {
		return saro_quantity;
	}

	public void setSaro_quantity(BigDecimal saro_quantity) {
		this.saro_quantity = saro_quantity;
	}

	public Integer getUser_type() {
		return user_type;
	}

	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}

	public Integer getUser_real_name_state() {
		return user_real_name_state;
	}

	public void setUser_real_name_state(Integer user_real_name_state) {
		this.user_real_name_state = user_real_name_state;
	}

	public String getUser_real_name() {
		return user_real_name;
	}

	public void setUser_real_name(String user_real_name) {
		this.user_real_name = user_real_name;
	}
}
