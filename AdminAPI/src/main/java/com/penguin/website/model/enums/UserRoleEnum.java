package com.penguin.website.model.enums;


public enum UserRoleEnum {

	/**
	 * 1 用户
	 */
	GAME_USER(1),

	/**
	 * 2 商家
	 */
	GAME_MERCHANT(2);

	private int value;

	private UserRoleEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
