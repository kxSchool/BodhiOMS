package com.penguin.website.model.enums;

/***
 *  日志事件
 */
public enum PenguinLogEventEnum {

    /***
     * 校验游戏端用户合法性
     */
    CHECK_TOKEN("登录"),

    GET_TOKEN("获取Token"),
    /***
     * 获取商家电话、银行卡信息
     */
    GET_MERCHANT_INFO("商家信息"),

    /***
     *  商家关闭订单
     */
    CLOSE_ORDER("关闭订单"),

    /**
     * 操作订单
     */
    OPERATE_ORDER("操作订单"),

    SYSTEM_OPERATE_ORDER("系统操作订单"),

    /***
     *  商家放行订单
     */
    CONFIRM_ORDER("放行订单"),

    /***
     *  商家获取订单详情
     */
    ORDER_DETAIL("订单详情"),

    /***
     *  商家获取订单列表
     */
    ORDER_LIST("订单列表"),

    /***
     *  商家获取订单列表
     */
    ORDER_PROOF("订单交易凭证"),

    /***
     *  商家获取自身信息
     */
    GET_TOKEN_INFO("自身信息"),

    /**
     * 获取充值商家列表
     */
    RECHARGE_MERCHANT_LIST("充值商家列表"),

    /**
     * 获取卖商家列表
     */
    RECHARGE_SELL_MERCHANT_LIST("充值商家列表"),

    /***
     * 用户设置手机号
     */
    EDIT_PHONE_CARD("手机号/银行卡"),

    /**
     * 上传文件
     */
    USER_UPLOAD("上传文件"),

    /**
     * 创建订单
     */
    CREATE_ORDER("创建订单"),

    CHECK_USER_IS_PWD("校验用户是否有交易密码"),

    CHECK_USER_IS_TRUE_PWD("校验用户交易密码是否正确"),

    EDIT_USER_PWD("校验用户交易密码是否正确"),

    USER_SEND_MESSAGE("用户发送短信验证码"),

    CHECK_USER_SEND_MESSAGE("校验用户发送短信验证码"),

    /***
     * 支付订单
     */
    PAYMENT_ORDER("支付订单"),

    /**
     * 商家后台
     */
    SYSTEM_USER_REGISTER("后台用户注册"),

    SYSTEM_USER_LOGIN("后台用户登录"),

    SYSTEM_USER_LIST("后台用户列表"),

    SYSTEM_USER_UPDATE_PWD("后台用户修改密码"),

    SYSTEM_TRANS_DATA("pc获取交易数据"),

    SYSTEM_PC_ORDER_LIST("pc获取订单数据"),

    SYSTEM_GET_MERCHANT_LIST("获取用户列表"),

    SYSTEM_GET_MERCHANT_INFO("获取用户信息"),

    SYSTEM_EDIT_USER("pc编辑用户信息"),

    SYSTEM_EDIT_ORDER("pc编辑订单信息")
    ;



    private String event;

   PenguinLogEventEnum(String event){
        this.event = event;
   }
   public String getEvent(){
       return this.event;
   }


    /**
     * 格式化用户信息
     * @param userId        用户id
     * @param eventEnum     log事件
     * @param actionEnum    log请求
     * @return
     */
   public static String formatLogTitle(String userId, PenguinLogEventEnum eventEnum, PenguinLogEventActionEnum actionEnum){
        return  userId + "_" + actionEnum.getAction() + "_" + eventEnum.event;
   }

}
