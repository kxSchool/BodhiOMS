package com.penguin.website.service.impl;

import com.alibaba.fastjson.JSON;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.REQPenguinsOrder;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.bean.rsp.RspGameUser;
import com.penguin.website.bean.rsp.RspGameResult;
import com.penguin.website.mapper.penguin.PenguinsOrderMapper;
import com.penguin.website.mapper.penguin.PenguinsUserMapper;
import com.penguin.website.model.domain.PenguinsOrder;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.enums.OrderStatusEnum;
import com.penguin.website.model.enums.OrderTypeEnum;
import com.penguin.website.model.enums.PenguinLogEventActionEnum;
import com.penguin.website.model.enums.PenguinLogEventEnum;
import com.penguin.website.service.LogService;
import com.penguin.website.service.PenguinsUserService;
import com.penguin.website.service.RestTemplateService;
import com.penguin.website.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Component
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ScheduledServiceImpl  {
	@Value("${order.overtime.user}")
	private Long userTime;

	@Value("${order.overtime.sell.user}")
	private Long userSellTime;

	@Value("${penguin.url.userTrade}")
	private String tradeUrl;

	@Autowired
	private PenguinsOrderMapper penguinsOrderMapper;

	@Autowired
	private PenguinsUserMapper penguinsUserMapper;

	@Autowired
	private PenguinsUserService penguinsUserService;

	@Autowired
	private RestTemplateService restTemplateService;

	@Autowired
	private LogService logService;

	@Scheduled(cron = "0 */1 * * * ?")
	public void scanOvertimeOrder() {
		REQPenguinsOrder param = new REQPenguinsOrder();
		param.setOrderStatus(OrderStatusEnum.PAYMENT_ORDER.getValue());
		List<PenguinsOrder> createList = penguinsOrderMapper.getOrderList(param);
		Date now = new Date();
		if (createList.size() > 0) {
			for (PenguinsOrder order : createList) {
				if (Objects.isNull(order.getOrderTime())) {
					continue;
				}
				Long subDate = DateUtil.getSubDate(order.getOrderTime(), now);
				if (Objects.equals(order.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
					if (subDate.longValue() < userTime.longValue()) {
						continue;
					}
					//下单用户超过30分钟未支付，系统取消订单
					String transId = order.getOrderTransactorId();
					this.updateOrder(now, order, transId, "用户超过" + userTime + "分钟未支付，系统自动取消订单");
				} else if (Objects.equals(order.getOrderType(), OrderTypeEnum.SELL.getValue())) {
					if (subDate.longValue() < userSellTime.longValue()) {
						continue;
					}
					//用户卖出订单超过2小时，商家未付款，订单自动取消
					String userId = order.getOrderUserId();
					this.updateOrder(now, order, userId, "商家超过" + userSellTime + "分钟未支付，系统自动取消订单");
				}
			}
		}
	}

	private void updateOrder(Date now, PenguinsOrder order, String userId, String remark) {
		if (StringUtils.isBlank(userId)) {
			return;
		}
		PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
		if (Objects.isNull(user) || StringUtils.isBlank(user.getUserToken())) {
			return;
		}
		Outcome<RspGameUser> outcome = penguinsUserService.checkUser(user.getUserToken());
		if (!outcome.ok()) {
			PenguinLogThreadLocal.failure(outcome.getMessage());
			logService.saveLog(PenguinLogEventEnum.SYSTEM_OPERATE_ORDER, PenguinLogEventActionEnum.EDIT, null);
			return;
		}
		PenguinLogThreadLocal.setApiInfo("定时任务取消订单");
		PenguinLogThreadLocal.setParam(order);
		Map<String, Object> params = new HashMap<>();
		params.put("order_no", order.getOrderNo() + "");
		params.put("saro_quantity", order.getOrderActualSaro().multiply(new BigDecimal(10000)).setScale(0));
		String json = JSON.toJSONString(params);
		RspGameResult rspGameResult = restTemplateService.doPost(tradeUrl, user.getUserToken(), json);
		if (rspGameResult.getCode() != 1) {
			PenguinLogThreadLocal.failure(rspGameResult.getMessage());
			logService.saveLog(PenguinLogEventEnum.SYSTEM_OPERATE_ORDER, PenguinLogEventActionEnum.EDIT, null);
			return;
		}
		PenguinsOrder update = new PenguinsOrder();
		update.setOrderStatus(OrderStatusEnum.CANCELLED_ORDER.getValue());
		update.setOrderRemark(remark);
		update.setBackMessage("系统取消订单成功");
		update.setUpdateTime(now);
		update.setId(order.getId());
		int result = penguinsOrderMapper.updateByPrimaryKeySelective(update);
		PenguinLogThreadLocal.appendLog("result", result);
		logService.saveLog(PenguinLogEventEnum.SYSTEM_OPERATE_ORDER, PenguinLogEventActionEnum.EDIT, null);
	}

}
