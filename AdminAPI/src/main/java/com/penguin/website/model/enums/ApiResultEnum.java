package com.penguin.website.model.enums;

public enum ApiResultEnum {

    SUCCESS("200","ok"),
    FAILED("400","请求失败"),
    UNAUTHORIZED("401","未授权"),
    FORBIDDEN("403","token不存在或已过期"),
    ERROR("500","不知名错误"),
    ERROR_NULL("501","空指针异常"),
    ERROR_CLASS_CAST("502","类型转换异常"),
    ERROR_RUNTION("503","运行时异常"),
    ERROR_IO("504","上传文件异常"),
    ERROR_MOTHOD_NOT_SUPPORT("505","请求方法错误"),


    USER_IS_EXIST("10003","账号已存在"),
    USER_INSERT_FAILURE("10004","账号保存失败"),
    ACCOUNT_LOGIN_FAILURE("10004","用户名或密码错误"),
    GOOGLE_ALREADY_BOUND("10005","当前账号已绑定"),
    GOOGLE_CODE_NOT_MATCH("10006","Google验证码不匹配");

    private String status;
    private String message;

    private ApiResultEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
