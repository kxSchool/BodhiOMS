package com.penguin.website.model.enums;


public enum OperateTypeEnum {
	 /**
	 * 1 关闭
	 */
	 CLOSE(1),

	/**
	 * 放行/确认付款
	 */
	FINISH(2);

	private int value;

	private OperateTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
