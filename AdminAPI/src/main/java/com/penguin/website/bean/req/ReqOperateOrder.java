package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(value = "后台操作订单")
public class ReqOperateOrder implements Serializable {
	private static final long serialVersionUID = 4532315177432875467L;

	@ApiModelProperty(value = "订单id")
	@NotBlank(message = "订单id为空")
	private Integer orderId;

	@ApiModelProperty(value = "订单类型：1充值,2卖出", required = true)
	@NotBlank(message = "订单类型为空")
	private Integer orderType;

	@ApiModelProperty(value = "操作类型 1 关闭 2 放行/确认付款")
	@NotBlank(message = "未选择操作类型")
	private Integer operateType;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
}
