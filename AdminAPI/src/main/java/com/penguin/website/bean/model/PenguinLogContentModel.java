package com.penguin.website.bean.model;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 日志内容
 */
public class PenguinLogContentModel extends JSONObject implements Serializable {

    @JsonIgnore
    private String userId;

    /**
     * key值
     */
    public static class ConstantKey {
        /**
         * 请求参数
         */
        public static final String PARAMS = "params";
        /**
         * 异常信息
         */
        public static final String ERR_MSG = "errMsg";
        /**
         * 请求状态
         */
        public static final String SUCCESSFUL = "successful";
        /**
         * api信息
         */
        public static final String API_INFO = "apiInfo";
    }

    public PenguinLogContentModel(Object param, String errMsg, Boolean successful){
        setParam(param);
        setErrMsg(errMsg);
        setSuccessful(successful);
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setParam(Object param){
        this.put(ConstantKey.PARAMS, param);
    }

    public Object getParam(){
        return this.containsKey(ConstantKey.PARAMS) ? this.get(ConstantKey.PARAMS) : null;
    }

    public void setErrMsg(String errMsg){
        this.put(ConstantKey.ERR_MSG, errMsg);
    }

    public String getErrMsg(){
        return this.containsKey(ConstantKey.ERR_MSG) ? this.getString(ConstantKey.ERR_MSG) : null;
    }

    public void setSuccessful(Boolean successful){
        this.put(ConstantKey.SUCCESSFUL, successful);
    }

    public Boolean getSuccessful(){
        return this.containsKey(ConstantKey.SUCCESSFUL) ? this.getBoolean(ConstantKey.SUCCESSFUL) : null;
    }

    public void setApiInfo(String apiInfo){
        this.put(ConstantKey.API_INFO, apiInfo);
    }

    public String getApiInfo(){
        return this.containsKey(ConstantKey.API_INFO) ? this.getString(ConstantKey.API_INFO) : null;
    }
}
