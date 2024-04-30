package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "获取订单列表参数")
public class ReqUserOrderList extends PageParam implements Serializable {

	private static final long serialVersionUID = 2342670104665857576L;

	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "订单类型：1充值,2卖出", required = true)
	private Integer orderType;

	@ApiModelProperty(value = "订单状态 充值：空为全部,1 用户：待付款；商家：待收款,2  待放行,3  已完成,4  已取消,5  已关闭-->卖出:空为全部, 1 用户待收款 商家待付款,3  已完成,4  已取消")
	private Integer orderStatus;

	private String token;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
