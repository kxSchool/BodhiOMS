package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModelProperty;

/**
 * 注册
 */
public class RspRegisterVO {


    @ApiModelProperty(value = "谷歌私钥")
    private String googleSecret;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    public String getGoogleSecret() {
        return googleSecret;
    }

    public void setGoogleSecret(String googleSecret) {
        this.googleSecret = googleSecret;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
