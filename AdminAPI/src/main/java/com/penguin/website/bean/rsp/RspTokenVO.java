package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModelProperty;

public class RspTokenVO {

    @ApiModelProperty(value = "jwt token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
