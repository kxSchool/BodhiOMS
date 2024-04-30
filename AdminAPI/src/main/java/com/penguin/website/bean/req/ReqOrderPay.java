package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "支付订单")
public class ReqOrderPay extends ReqToken implements Serializable {

	private static final long serialVersionUID = 5199183797752662092L;

	@ApiModelProperty(value = "订单编号", required = true)
	private Long orderNo;

	@ApiModelProperty(value = "支付凭证路径")
	private String transactorProofPath;

	@ApiModelProperty(value = "操作类型")
	private Integer operateType;

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getTransactorProofPath() {
		return transactorProofPath;
	}

	public void setTransactorProofPath(String transactorProofPath) {
		this.transactorProofPath = transactorProofPath;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
}
