package com.penguin.website.bean.rsp;

import java.io.Serializable;

public class RspGameResult implements Serializable {
    private static final long serialVersionUID = -2439609069589153192L;
    private Integer code;

    private String message;

    private String data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
