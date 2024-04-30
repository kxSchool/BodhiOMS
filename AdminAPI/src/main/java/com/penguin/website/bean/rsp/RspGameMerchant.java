package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "游戏商家信息")
public class RspGameMerchant implements Serializable {

	private static final long serialVersionUID = 7608256052243954279L;
	@ApiModelProperty(value = "")
	private String id;

	@ApiModelProperty(value = "用户token")
	private String token;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
