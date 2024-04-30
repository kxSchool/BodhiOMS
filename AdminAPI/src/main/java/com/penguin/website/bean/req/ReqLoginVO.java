package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

public class ReqLoginVO extends ReqUserBaseVO {

    @ApiModelProperty(value = "谷歌验证码", required = true)
    @NotBlank(message = "谷歌验证码不能为空")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
