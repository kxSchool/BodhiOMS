package com.penguin.website.bean.rsp;

/**
 * 返回值接口.
 *
 * @param <T> 类型
 * @author breggor
 */
public interface Result<T> {

    /**
     * 返回数据.
     *
     * @return T
     */
    T getData();

    /**
     * 业务状态接口.
     * 不建议与http状态码重叠,按照模块编码规则定义
     */
    interface Status {

        /**
         * 业务状态码.
         *
         * @return String
         */
        int getCode();

        /**
         * 业务消息.
         *
         * @return String
         */
        String getMessage();
    }

}
