package com.penguin.website.model.dto;

/**
 * create by: mlshao
 * description: 接口请求头信息实体
 */
public class ExternalHttp {

    private String token;
    private String appId;
    private String appSecret;
    private String param;

    public ExternalHttp(String token, String appId, String appSecret, String param) {
        this.token = token;
        this.appId = appId;
        this.appSecret = appSecret;
        this.param = param;
    }

    public ExternalHttp() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
