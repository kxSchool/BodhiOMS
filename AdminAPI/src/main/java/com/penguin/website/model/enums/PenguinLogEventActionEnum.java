package com.penguin.website.model.enums;

/**
 * 日志事件动作
 */
public enum PenguinLogEventActionEnum {

    /**
     * 查询
     */
    SELECT("查询"),

    /**
     * 修改编辑
     */
    EDIT("编辑"),

    /**
     * 创建
     */
    CREATE("创建"),

    /**
     * 删除
     */
    REMOVER("删除");

    private String action;

    PenguinLogEventActionEnum(String action){
        this.action = action;
    }

    public String getAction(){
        return this.action;
    }
}
