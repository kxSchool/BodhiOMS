package com.penguin.website.bean.model;

import com.penguin.website.bean.rsp.Outcome;

/**
 * 操作日志线程变量
 */
public class PenguinLogThreadLocal {

    final static ThreadLocal<PenguinLogContentModel> threadLocal = new ThreadLocal<PenguinLogContentModel>() {

        /**
         * 默认初始化
         * @return
         */
        @Override
        protected PenguinLogContentModel initialValue() {
            return new PenguinLogContentModel(false, null, true);
        }
    };


    public static void setBaseInfo(String apiInfo, String token) {
        PenguinLogThreadLocal.setToken(token);
        PenguinLogThreadLocal.setApiInfo(apiInfo);
    }

    public static void setBaseInfo(String apiInfo, String token, Object params) {
        setBaseInfo(apiInfo,token);
        PenguinLogThreadLocal.setParam(params);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void setParam(Object param) {
        PenguinLogContentModel model = threadLocal.get();
        model.setParam(param);
        threadLocal.set(model);
    }

    public static void setErrMsg(String errMsg) {
        PenguinLogContentModel model = threadLocal.get();
        model.setErrMsg(errMsg);
        threadLocal.set(model);
    }

    public static void setSuccessful(Boolean successful) {
        PenguinLogContentModel model = threadLocal.get();
        model.setSuccessful(successful);
        threadLocal.set(model);
    }

    public static void setToken(String token) {
        PenguinLogContentModel model = threadLocal.get();
        model.put("token", token);
        threadLocal.set(model);
    }

    public static void appendLog(String key, Object val) {
        PenguinLogContentModel model = threadLocal.get();
        model.put(key, val);
        threadLocal.set(model);
    }

    public static void appendLog(Object param, String errMsg, Boolean successful) {
        PenguinLogContentModel model = threadLocal.get();
        model.setParam(param);
        model.setErrMsg(errMsg);
        model.setSuccessful(successful);
        threadLocal.set(model);
    }

    public static PenguinLogContentModel setUserId(String userId) {
        PenguinLogContentModel model = threadLocal.get();
        model.setUserId(userId);
        return model;
    }

    public static void setApiInfo(String apiInfo) {
        PenguinLogContentModel model = threadLocal.get();
        model.setApiInfo(apiInfo);
        threadLocal.set(model);
    }

    public static PenguinLogContentModel get() {
        return threadLocal.get();
    }

    public static void set(PenguinLogContentModel model) {
        threadLocal.set(model);
    }

    public static String getLogContentAndRemove() {
        try {
            return threadLocal.get().toJSONString();
        } finally {
            remove();
        }
    }

    public static String getUserId() {
        return threadLocal.get().getUserId();
    }

    public static void failure(String errMsg) {
        PenguinLogContentModel model = threadLocal.get();
        model.setErrMsg(errMsg);
        model.setSuccessful(false);
        threadLocal.set(model);
    }
}
