package com.penguin.website.bean.rsp;

import java.util.Optional;

/**
 * 返回值接口：默认功能.
 *
 * @param <T> 特别说明：不指定泛型类型时，表示data无返回值
 * @author breggor
 */
public interface Outcome<T> extends Result<T>, Result.Status {

    /**
     * 返回值.
     *
     * @return Optional
     */
    default Optional<T> value() {
        return Optional.<T>ofNullable(this.getData());
    }

    /**
     * 是否成功.
     *
     * @return boolean
     */
    default boolean ok() {
        return DefaultStatus.SUCCESS.getCode() == this.getCode();
    }

    /**
     * 默认成功.
     *
     * @return Outcome
     */
    static Outcome success() {
        return new OutcomeImpl(DefaultStatus.SUCCESS);
    }

    /**
     * 成功返回结果.
     *
     * @param message 消息
     * @return Outcome
     */
    static Outcome success(final String message) {
        return new OutcomeImpl(DefaultStatus.SUCCESS, message);
    }

    /**
     * 成功返回结果.
     *
     * @param status 状态
     * @return Outcome
     */
    static Outcome success(final Status status) {
        return new OutcomeImpl(status);
    }

    /**
     * 成功返回结果.
     *
     * @param status  状态
     * @param message 消息
     * @return Outcome
     */
    static Outcome success(final Status status, final String message) {
        return new OutcomeImpl(status, message);
    }

    /**
     * 成功返回结果.
     *
     * @param <E>  类型
     * @param data 数据
     * @return Outcome
     */
    static <E> Outcome<E> success(final E data) {
        return new OutcomeImpl<E>(DefaultStatus.SUCCESS, data);
    }

    /**
     * 成功返回结果.
     *
     * @param <E>     类型
     * @param message 消息
     * @param data    数据
     * @return Outcome
     */
    static <E> Outcome<E> success(final String message, final E data) {
        return new OutcomeImpl<E>(DefaultStatus.SUCCESS, message, data);
    }

    /**
     * 成功返回结果.
     *
     * @param <E>     类型
     * @param code    编码
     * @param message 消息
     * @param data    数据
     * @return Outcome
     */
    static <E> Outcome<E> success(final int code, final String message, final E data) {
        return new OutcomeImpl<E>(code, message, data);
    }

    /**
     * 默认失败.
     *
     * @return Outcome
     */
    static Outcome failure() {
        return new OutcomeImpl(DefaultStatus.FAILURE);
    }

    /**
     * 根据错误消息构建.
     *
     * @param message 消息
     * @return Outcome
     */
    static Outcome failure(final String message) {
        return new OutcomeImpl(DefaultStatus.FAILURE, message);
    }

    /**
     * 根据错误消息构建.
     *
     * @param code    编码
     * @param message 消息
     * @return Outcome
     */
    static Outcome failure(final int code, final String message) {
        return new OutcomeImpl(code, message);
    }

    /**
     * 根据状态码.
     *
     * @param status 状态
     * @return Outcome
     */
    static Outcome failure(final Status status) {
        return new OutcomeImpl(status);
    }

    /**
     * 根据错误消息构建.
     *
     * @param status  状态
     * @param message 消息
     * @return Outcome
     */
    static Outcome failure(final Status status, final String message) {
        return new OutcomeImpl(status.getCode(), message, null);
    }

    /**
     * 根据错误信息构建.
     *
     * @param <E>     类型
     * @param message 消息
     * @param data    数据
     * @return Outcome
     */
    static <E> Outcome<E> failure(final String message, final E data) {
        return new OutcomeImpl(DefaultStatus.FAILURE, message, data);
    }

    /**
     * 根据错误信息构建.
     *
     * @param <E>     类型
     * @param status  状态
     * @param message 消息
     * @param data    数据
     * @return Outcome
     */
    static <E> Outcome<E> failure(final Status status, final String message, final E data) {
        return new OutcomeImpl(status, message, data);
    }

    /**
     * 根据状态判断是否成功.
     *
     * @param status 状态
     * @return Outcome
     */
    static Outcome status(final boolean status) {
        return status ? success() : failure();
    }
}
