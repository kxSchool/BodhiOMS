package com.penguin.website.model.enums;


public enum OrderTypeEnum {
	 /**
	 * 1 充值
	 */
	 RECHARGE(1),

	/**
	 * 出售
	 */
	SELL(2);

	private int value;

	private OrderTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
