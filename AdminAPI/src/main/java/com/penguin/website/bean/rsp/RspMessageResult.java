package com.penguin.website.bean.rsp;

import java.io.Serializable;

public class RspMessageResult implements Serializable {

    private static final long serialVersionUID = -6477744693449983423L;

    private String resultCode;//": "ok",

    private String resultMessage;

    private Boolean data;//": true,

    private Long timestamps;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public Long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Long timestamps) {
        this.timestamps = timestamps;
    }
}
