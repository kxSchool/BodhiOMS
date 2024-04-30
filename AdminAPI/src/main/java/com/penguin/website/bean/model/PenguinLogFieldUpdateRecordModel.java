package com.penguin.website.bean.model;

import java.io.Serializable;

/**
 * 字段修改前后
 */
public class PenguinLogFieldUpdateRecordModel implements Serializable {

    /***
     * 修改前
     */
    private Object before;

    /***
     * 修改后
     */
    private Object after;

    public PenguinLogFieldUpdateRecordModel(Object before, Object after){
        this.before = before;
        this.after = after;
    }


    public Object getBefore() {
        return before;
    }

    public void setBefore(Object before) {
        this.before = before;
    }

    public Object getAfter() {
        return after;
    }

    public void setAfter(Object after) {
        this.after = after;
    }
}
