package com.penguin.website.bean.rsp;

/**
 * 默认业务状态码.
 *
 * @author breggor
 */
public enum DefaultStatus implements Result.Status {

    FAILURE(0, "业务异常"),
    SUCCESS(1, "操作成功"),
    DATA_EXISTED(2, "业务数据已存在"),
    DATA_NOT_FOUND(3, "业务数据不存在"),
    DATA_INVALID(4, "业务数据无效");

    private int code;
    private String message;

    DefaultStatus(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
