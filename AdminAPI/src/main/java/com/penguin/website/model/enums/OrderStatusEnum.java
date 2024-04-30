package com.penguin.website.model.enums;


public enum OrderStatusEnum {

	/**
	 * 1 用户：创建 待付款，  商家： 待收款
	 */
	PAYMENT_ORDER(1),

	/**
	 * 2 用户： 待放行
	 */
	COMPLETE_PAYMENT_ORDER(2),

	/**
	 * 已完成
	 */
	COMPLETE_ORDER(3),

	/**
	 * 用户已取消
	 */
	CANCELLED_ORDER(4),

	/**
	 * 已关闭
	 */
	CLOSED_ORDER(5);

	private int value;

	private OrderStatusEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
