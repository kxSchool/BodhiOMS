package com.penguin.website.model.enums;


public enum StatusEnum {
	 /*
	 * 1 正常
	 */
	NORMAL(1);

	private int value;

	private StatusEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
