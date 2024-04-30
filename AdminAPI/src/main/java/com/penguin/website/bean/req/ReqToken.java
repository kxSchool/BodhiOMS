package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "token信息")
public class ReqToken implements Serializable {

	private static final long serialVersionUID = -7482731385292096728L;

	@ApiModelProperty(value = "token", required = true)
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ReqToken() {
	}

	public ReqToken(String token) {
		this.token = token;
	}

}
