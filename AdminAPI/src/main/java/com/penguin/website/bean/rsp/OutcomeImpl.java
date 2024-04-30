package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 返回数据封装实现类.
 *
 * @param <T> 特别说明：不指定泛型类型时，表示data无返回值.
 * @author breggor
 */
@ApiModel(description = "业务层-返回数据封装")
public class OutcomeImpl<T> implements Outcome<T>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码.
     */
    @ApiModelProperty(value = "状态码", required = true)
    private int code;

    /**
     * 提示消息.
     */
    @ApiModelProperty(value = "提示消息", required = true)
    private String message;

    /**
     * 业务数据.
     */
    @ApiModelProperty(value = "业务数据")
    private T data;

    public OutcomeImpl(final Status status) {
        this(status.getCode(), status.getMessage());
    }

    public OutcomeImpl(final Status status, final String message) {
        this(status.getCode(), message, null);
    }

    public OutcomeImpl(final Status status, final String message, final T data) {
        this(status.getCode(), message, data);
    }

    public OutcomeImpl(final Status status, final T data) {
        this(status.getCode(), status.getMessage(), data);
    }

    public OutcomeImpl(final int code, final String message) {
        this(code, message, null);
    }

    public OutcomeImpl(final int code, final String message, final T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
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
