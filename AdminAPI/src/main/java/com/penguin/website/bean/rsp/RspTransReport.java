package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "交易数据信息")
public class RspTransReport implements Serializable {
	private static final long serialVersionUID = 7527857130569005282L;
	@ApiModelProperty(value = "今日成交量")
	private BigDecimal todaySaro = BigDecimal.ZERO;

	@ApiModelProperty(value = "今日成交额")
	private BigDecimal todayCny = BigDecimal.ZERO;

	@ApiModelProperty(value = "昨日成交量")
	private BigDecimal yesterdaySaro = BigDecimal.ZERO;

	@ApiModelProperty(value = "昨日成交额")
	private BigDecimal yesterdayCny = BigDecimal.ZERO;

	@ApiModelProperty(value = "平台总成交量")
	private BigDecimal totalSaro = BigDecimal.ZERO;

	@ApiModelProperty(value = "平台总成交额")
	private BigDecimal totalCny = BigDecimal.ZERO;

	public BigDecimal getTodaySaro() {
		return todaySaro;
	}

	public void setTodaySaro(BigDecimal todaySaro) {
		this.todaySaro = todaySaro;
	}

	public BigDecimal getTodayCny() {
		return todayCny;
	}

	public void setTodayCny(BigDecimal todayCny) {
		this.todayCny = todayCny;
	}

	public BigDecimal getYesterdaySaro() {
		return yesterdaySaro;
	}

	public void setYesterdaySaro(BigDecimal yesterdaySaro) {
		this.yesterdaySaro = yesterdaySaro;
	}

	public BigDecimal getYesterdayCny() {
		return yesterdayCny;
	}

	public void setYesterdayCny(BigDecimal yesterdayCny) {
		this.yesterdayCny = yesterdayCny;
	}

	public BigDecimal getTotalSaro() {
		return totalSaro;
	}

	public void setTotalSaro(BigDecimal totalSaro) {
		this.totalSaro = totalSaro;
	}

	public BigDecimal getTotalCny() {
		return totalCny;
	}

	public void setTotalCny(BigDecimal totalCny) {
		this.totalCny = totalCny;
	}
}
