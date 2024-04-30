package com.penguin.website.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.penguin.website.bean.model.PenguinLogFieldUpdateRecordModel;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.*;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.mapper.penguin.PenguinsOrderMapper;
import com.penguin.website.mapper.penguin.PenguinsUserMapper;
import com.penguin.website.model.domain.PenguinsOrder;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.dto.ExternalHttp;
import com.penguin.website.model.enums.*;
import com.penguin.website.service.BaseService;
import com.penguin.website.service.PenguinOrderService;
import com.penguin.website.service.PenguinsUserService;
import com.penguin.website.service.RestTemplateService;
import com.penguin.website.util.CheckTokenUtils;
import com.penguin.website.util.DateUtil;
import com.penguin.website.util.OrderNOUtil;
import com.penguin.website.util.PenguinUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PenguinsOrderServiceImpl extends BaseService implements PenguinOrderService {
    @Value("${penguin.url.userTrade}")
    private String tradeUrl;

    @Value("${order.overtime.user}")
    private Integer userTime;

    @Value("${order.overtime.sell.user}")
    private Integer userSellTime;

    /**
     * 最大充值数
     */
    @Value("${penguin.limit.recharge.max}")
    private Integer maxNumber;

    @Value("${penguin.limit.sell.max}")
    private Integer sellMax;

    @Value("${penguin.limit.sell.min}")
    private Integer sellMin;

    @Override
    public int getUserWaitReleasedNum(String userId, Integer orderType, Integer orderStatus) {
        REQPenguinsOrder order = new REQPenguinsOrder();
        order.setOrderUserId(userId);
        order.setOrderStatus(orderStatus);
        order.setOrderType(orderType);
        return penguinsOrderMapper.getOrderListCount(order);
    }

    @Override
    public int getMerchantWaitReleasedNum(String userId, Integer orderType, Integer orderStatus) {
        REQPenguinsOrder order = new REQPenguinsOrder();
        order.setOrderTransactorId(userId);
        order.setOrderStatus(orderStatus);
        order.setOrderType(orderType);
        return penguinsOrderMapper.getOrderListCount(order);
    }

    @Override
    public Outcome<String> createRechargeOrder(RspGameUser gameUser, ReqOrder reqOrder) {

        String transId = reqOrder.getOrderTransactorId();
        if (StringUtils.isBlank(transId)) {
            return Outcome.failure("交易用户为空");
        }

        if (Objects.isNull(reqOrder.getOrderPriceCny()) && Objects.isNull(reqOrder.getOrderPriceSaro())) {
            return Outcome.failure("请选择按数量或按金额充值");
        }
        PenguinsOrder order = new PenguinsOrder();
        //验证交易对象余额是否充足
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(transId);
        PenguinLogThreadLocal.appendLog("merchant", user);

        //验证商城和游戏端商户角色是否一致
        Outcome outcome=PenguinUtil.checkMerchantRoleInfoAndGamInfo(user,url, appId, appSecret, restTemplate);
        if (!outcome.ok()) {
            return outcome;
        }

        PenguinsUser orderUser = penguinsUserMapper.selectByPrimaryKey(gameUser.getUser_id());
        PenguinLogThreadLocal.appendLog("orderUser", orderUser);
        Outcome<String> checkOrderUser = penguinsUserService.checkMerchantByUserId(gameUser.getUser_id());
        if (!checkOrderUser.ok()) {
            return Outcome.failure(checkOrderUser.getMessage());
        }
        Outcome<RspGameUser> rspGameUserOutcome = penguinsUserService.checkUser(user.getUserToken());
        if (!rspGameUserOutcome.ok()) {
            return Outcome.failure("商家信息异常");
        }
        RspGameUser gameMerchant = rspGameUserOutcome.getData();

        if (Objects.isNull(gameMerchant.getSaro_quantity())) {
            return Outcome.failure("商家saro数量为空");
        }

        if (Objects.isNull(orderUser) || StringUtils.isBlank(orderUser.getUserToken())) {
            return Outcome.failure("下单用户信息为空");
        }

        if (reqOrder.getOrderPriceCny().compareTo(BigDecimal.ZERO) > 0 && reqOrder.getOrderPriceSaro().compareTo(BigDecimal.ZERO) > 0) {
            return Outcome.failure("数量或金额有误");
        }

        if (StringUtils.isBlank(user.getUserPhone())) {
            return Outcome.failure("商家未填写手机信息");
        }

        if (Objects.equals(transId, gameUser.getUser_id())) {
            return Outcome.failure("不能自己给自己下单");
        }

        if (Objects.isNull(user.getRechargeUnitPrice())) {
            return Outcome.failure("商家未设置交易单价");
        }

        if (reqOrder.getOrderUnitPrice().compareTo(user.getRechargeUnitPrice()) != 0) {
            return Outcome.failure("saro单价发生改变,原单价：" + reqOrder.getOrderUnitPrice() + "，现单价：" + user.getRechargeUnitPrice());
        }

        if (StringUtils.isBlank(user.getUserBankCard()) || StringUtils.isBlank(user.getUserBank())
                || StringUtils.isBlank(user.getBankFavour())) {
            return Outcome.failure("商家未填写银行卡信息");
        }
        REQPenguinsOrder exist = new REQPenguinsOrder();
        exist.setOrderUserId(gameUser.getUser_id());
        exist.setOrderType(OrderTypeEnum.RECHARGE.getValue());
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(exist);
        for (PenguinsOrder list : orderList) {
            if (Objects.equals(list.getOrderStatus(), OrderStatusEnum.PAYMENT_ORDER.getValue())) {
                return Outcome.failure("您有未完成订单，订单编号：" + list.getOrderNo());
            }
        }

        RspOrderInfo userSnapshot = new RspOrderExtend();
        RspOrderExtend merchantShot = new RspOrderExtend();
        BigDecimal merchantSaroNumber = gameMerchant.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
        if (Objects.nonNull(reqOrder.getOrderPriceSaro()) && reqOrder.getOrderPriceSaro().compareTo(new BigDecimal(0)) > 0) {
            if (reqOrder.getOrderPriceSaro().compareTo(merchantSaroNumber) > 0) {
                Outcome failure = Outcome.failure("充值数大于商家余额");
                PenguinLogThreadLocal.appendLog("remark", "订单量:" + reqOrder.getOrderPriceSaro() + ", 商家剩余量:" + user.getUserSaroNumber());
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            order.setOrderPriceSaro(reqOrder.getOrderPriceSaro());
            order.setOrderActualSaro(reqOrder.getOrderPriceSaro());
            BigDecimal payment = reqOrder.getOrderPriceSaro().multiply(user.getRechargeUnitPrice()).setScale(2, BigDecimal.ROUND_UP);
            order.setPayAmount(payment);
            userSnapshot.setSaroNumber(reqOrder.getOrderPriceSaro());
            merchantShot.setSaroNumber(reqOrder.getOrderPriceSaro());
        }
        if (Objects.nonNull(reqOrder.getOrderPriceCny()) && reqOrder.getOrderPriceCny().compareTo(new BigDecimal(0)) > 0) {
            BigDecimal actual = reqOrder.getOrderPriceCny().divide(user.getRechargeUnitPrice(), 4, BigDecimal.ROUND_DOWN);
            if (actual.compareTo(merchantSaroNumber) > 0) {
                Outcome failure = Outcome.failure("充值数大于商家余额");
                PenguinLogThreadLocal.appendLog("remark", "cny 订单量:" + reqOrder.getOrderPriceSaro() + ", 商家剩余量:" + user.getUserSaroNumber());
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            order.setOrderPriceCny(reqOrder.getOrderPriceCny().setScale(2, BigDecimal.ROUND_UP));
            order.setPayAmount(reqOrder.getOrderPriceCny().setScale(2, BigDecimal.ROUND_UP));
            order.setOrderActualSaro(actual);
            userSnapshot.setSaroNumber(actual);
            merchantShot.setSaroNumber(actual);
        }
        Date date = new Date();
        long uuid = OrderNOUtil.getUUID();
        order.setOrderNo(uuid);
        order.setOrderType(OrderTypeEnum.RECHARGE.getValue());
        order.setOrderStatus(OrderStatusEnum.PAYMENT_ORDER.getValue());
        order.setOrderTime(date);
        order.setOrderUserId(orderUser.getUserId());
        order.setOrderUserRealName(orderUser.getBankFavour());
        order.setOrderUserAccount(orderUser.getUserName());
        order.setOrderUserNickname(orderUser.getNickname());
        if (Objects.nonNull(reqOrder.getPayAmount()) && reqOrder.getPayAmount().compareTo(new BigDecimal(0)) > 0) {
            if (order.getPayAmount().compareTo(reqOrder.getPayAmount().setScale(2, BigDecimal.ROUND_UP)) != 0) {
                return Outcome.failure("实付金额有误");
            }
        }
        if (Objects.nonNull(reqOrder.getPayAmou()) && reqOrder.getPayAmou().compareTo(new BigDecimal(0)) > 0) {
            if (order.getPayAmount().compareTo(reqOrder.getPayAmou().setScale(2, BigDecimal.ROUND_UP)) != 0) {
                return Outcome.failure("实付金额有误");
            }
        }
        order.setOrderTransactorId(transId);
        order.setOrderTransactorAccount(user.getUserName());
        order.setUpdateTime(date);
        order.setOrderUnitPrice(reqOrder.getOrderUnitPrice());

        if (order.getPayAmount().compareTo(user.getMinRecharge()) < 0) {
            Outcome.failure("实付金额小于最低限额:" + user.getMinRecharge());
        }
        BigDecimal multiply = user.getRechargeUnitPrice().multiply(new BigDecimal(maxNumber));
        if (order.getPayAmount().compareTo(multiply) > 0) {
            Outcome.failure("超出最大限额:" + multiply);
        }
        userSnapshot.setUserName(orderUser.getUserName());
        userSnapshot.setNickname(orderUser.getNickname());
        userSnapshot.setRealName(orderUser.getBankFavour());
        userSnapshot.setUserId(orderUser.getUserId());
        userSnapshot.setPayAmount(order.getPayAmount());
        userSnapshot.setUserPhone(orderUser.getUserPhone());

        userSnapshot.setOrderTime(date);
        userSnapshot.setOrderNo(uuid);
        userSnapshot.setOrderUnitPrice(reqOrder.getOrderUnitPrice());
        order.setUserSnapshot(JSON.toJSONString(userSnapshot));

        merchantShot.setUserName(user.getUserName());
        merchantShot.setNickname(user.getNickname());
        //merchantShot.setRealName(user.getBankFavour());
        merchantShot.setUserId(orderUser.getUserId());
        merchantShot.setPayAmount(order.getPayAmount());
        merchantShot.setOrderTime(date);
        merchantShot.setOrderNo(uuid);
        merchantShot.setOrderUnitPrice(reqOrder.getOrderUnitPrice());
        merchantShot.setUserPhone(user.getUserPhone());
        merchantShot.setUserPhone2(user.getUserPhone2());
        merchantShot.setUserPhone3(user.getUserPhone3());
        merchantShot.setBankFavour(user.getBankFavour());
        merchantShot.setUserBank(user.getUserBank());
        merchantShot.setUserBankCard(user.getUserBankCard());

        merchantShot.setUserBankBranch(user.getUserBankBranch());
        order.setMerchantSnapshot(JSON.toJSONString(merchantShot));

        PenguinLogThreadLocal.appendLog("orderInfo", order);
        penguinsOrderMapper.insert(order);

        RspGameResult rspGameResult = this.operateBalance(order, -10000, user.getUserToken());
        if (rspGameResult.getCode() != 1) {
            penguinsOrderMapper.deleteByPrimaryKey(order.getId());
            return Outcome.failure("锁余额失败");
        }
        PenguinLogThreadLocal.appendLog("result", order.getId());

        return Outcome.success(order.getId() + "", order.getId() + "");
    }

    @Override
    public Outcome<String> createSellOrder(RspGameUser gameUser, ReqOrder reqOrder) {
        String transId = reqOrder.getOrderTransactorId();
        if (StringUtils.isBlank(transId)) {
            return Outcome.failure("交易用户为空");
        }
        if (Objects.isNull(reqOrder.getOrderPriceCny()) && Objects.isNull(reqOrder.getOrderPriceSaro())) {
            return Outcome.failure("请选择按数量或按金额出售");
        }

        //验证下单对象余额是否充足
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(transId);
        PenguinLogThreadLocal.appendLog("user", user);

        //验证商城和游戏端商户角色是否一致
        Outcome outcome=PenguinUtil.checkMerchantRoleInfoAndGamInfo(user,url, appId, appSecret, restTemplate);
        if (!outcome.ok()) {
            return outcome;
        }

        PenguinsUser orderUser = penguinsUserMapper.selectByPrimaryKey(gameUser.getUser_id());
        PenguinLogThreadLocal.appendLog("orderUser", orderUser);
        if (Objects.isNull(user) || StringUtils.isBlank(user.getUserToken())) {
            return Outcome.failure("商户信息为空");
        }

        if (Objects.isNull(orderUser) || StringUtils.isBlank(orderUser.getUserToken())) {
            return Outcome.failure("下单用户信息为空");
        }

        if (Objects.equals(transId, gameUser.getUser_id())) {
            return Outcome.failure("不能自己给自己下单");
        }

        if (reqOrder.getOrderPriceCny().compareTo(BigDecimal.ZERO) > 0 && reqOrder.getOrderPriceSaro().compareTo(BigDecimal.ZERO) > 0) {
            return Outcome.failure("数量或金额有误");
        }

        if (Objects.isNull(user.getRecoveryUnitPrice())) {
            return Outcome.failure("商家未设置交易单价");
        }

        if (reqOrder.getOrderUnitPrice().compareTo(user.getRecoveryUnitPrice()) != 0) {
            return Outcome.failure("saro单价发生改变, 原单价：" + reqOrder.getOrderUnitPrice() + "，现单价：" + user.getRecoveryUnitPrice());
        }
        REQPenguinsOrder exist = new REQPenguinsOrder();
        exist.setOrderUserId(gameUser.getUser_id());
        exist.setOrderType(OrderTypeEnum.SELL.getValue());
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(exist);
        for (PenguinsOrder list : orderList) {
            if (Objects.equals(list.getOrderStatus(), OrderStatusEnum.PAYMENT_ORDER.getValue())) {
                return Outcome.failure("您有未完成订单");
            }
        }

        PenguinsOrder order = new PenguinsOrder();
        RspOrderExtend userSnapshot = new RspOrderExtend();
        RspOrderExtend merchantShot = new RspOrderExtend();
        BigDecimal userSaroNumber = gameUser.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
        if (Objects.nonNull(reqOrder.getOrderPriceSaro()) && reqOrder.getOrderPriceSaro().compareTo(new BigDecimal(0)) > 0) {
            if (reqOrder.getOrderPriceSaro().compareTo(userSaroNumber) > 0) {
                Outcome failure = Outcome.failure("您没有足够的SARO");
                PenguinLogThreadLocal.appendLog("remark", "订单量:" + reqOrder.getOrderPriceSaro() + ", 用户剩余量:" + gameUser.getSaro_quantity());
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            order.setOrderPriceSaro(reqOrder.getOrderPriceSaro());
            order.setOrderActualSaro(reqOrder.getOrderPriceSaro());
            BigDecimal payment = reqOrder.getOrderPriceSaro().multiply(user.getRecoveryUnitPrice()).setScale(2, BigDecimal.ROUND_UP);
            order.setPayAmount(payment);
            userSnapshot.setSaroNumber(reqOrder.getOrderPriceSaro());
            merchantShot.setSaroNumber(reqOrder.getOrderPriceSaro());
        }
        if (Objects.nonNull(reqOrder.getOrderPriceCny()) && reqOrder.getOrderPriceCny().compareTo(new BigDecimal(0)) > 0) {
            BigDecimal actual = reqOrder.getOrderPriceCny().divide(user.getRecoveryUnitPrice(), 4, BigDecimal.ROUND_DOWN);
            if (actual.compareTo(userSaroNumber) > 0) {
                Outcome failure = Outcome.failure("您没有足够的SARO");
                PenguinLogThreadLocal.appendLog("remark", "订单量:" + reqOrder.getOrderPriceSaro() + ", 用户剩余量:" + gameUser.getSaro_quantity());
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            order.setOrderPriceCny(reqOrder.getOrderPriceCny().setScale(2, BigDecimal.ROUND_UP));
            order.setOrderActualSaro(actual);
            order.setPayAmount(reqOrder.getOrderPriceCny().setScale(2, BigDecimal.ROUND_UP));
            userSnapshot.setSaroNumber(actual);
            merchantShot.setSaroNumber(actual);
        }
        Date date = new Date();
        long uuid = OrderNOUtil.getUUID();
        order.setOrderNo(uuid);
        order.setOrderType(OrderTypeEnum.SELL.getValue());
        order.setOrderStatus(OrderStatusEnum.PAYMENT_ORDER.getValue());
        order.setOrderTime(date);
        order.setOrderUserId(orderUser.getUserId());
        order.setOrderUserRealName(orderUser.getBankFavour());
        order.setOrderUserAccount(orderUser.getUserName());
        order.setOrderUserNickname(orderUser.getNickname());

        if (Objects.nonNull(reqOrder.getPayAmount()) && reqOrder.getPayAmount().compareTo(new BigDecimal(0)) > 0) {
            if (order.getPayAmount().compareTo(reqOrder.getPayAmount().setScale(2, BigDecimal.ROUND_UP)) != 0) {
                return Outcome.failure("实付金额有误");
            }
        }
        if (Objects.nonNull(reqOrder.getPayAmou()) && reqOrder.getPayAmou().compareTo(new BigDecimal(0)) > 0) {
            if (order.getPayAmount().compareTo(reqOrder.getPayAmou().setScale(2, BigDecimal.ROUND_UP)) != 0) {
                return Outcome.failure("实付金额有误");
            }
        }

        if (order.getPayAmount().compareTo(new BigDecimal(sellMin)) < 0) {
            return Outcome.failure("卖出最小限额" + sellMin + "元");
        }
        if (order.getPayAmount().compareTo(new BigDecimal(sellMax)) > 0) {
            return Outcome.failure("单次卖出最大限额" + sellMax + "元");
        }

        order.setOrderTransactorId(transId);
        order.setOrderTransactorAccount(user.getUserName());
        order.setUpdateTime(date);
        order.setOrderUnitPrice(reqOrder.getOrderUnitPrice());

        merchantShot.setUserName(user.getUserName());
        merchantShot.setNickname(user.getNickname());
        merchantShot.setUserId(user.getUserId());
        merchantShot.setPayAmount(order.getPayAmount());
        merchantShot.setOrderTime(date);
        merchantShot.setOrderNo(uuid);
        merchantShot.setOrderUnitPrice(reqOrder.getOrderUnitPrice());
        merchantShot.setUserPhone(user.getUserPhone());
        merchantShot.setUserPhone2(user.getUserPhone2());
        merchantShot.setUserPhone3(user.getUserPhone3());
        order.setMerchantSnapshot(JSON.toJSONString(merchantShot));

        userSnapshot.setUserName(orderUser.getUserName());
        userSnapshot.setNickname(orderUser.getNickname());
        userSnapshot.setRealName(orderUser.getBankFavour());
        userSnapshot.setUserId(orderUser.getUserId());
        userSnapshot.setPayAmount(order.getPayAmount());
        userSnapshot.setOrderTime(date);
        userSnapshot.setOrderNo(uuid);
        userSnapshot.setOrderUnitPrice(reqOrder.getOrderUnitPrice());
        userSnapshot.setUserPhone(orderUser.getUserPhone());
        userSnapshot.setBankFavour(orderUser.getBankFavour());
        userSnapshot.setUserBank(orderUser.getUserBank());
        userSnapshot.setUserBankCard(orderUser.getUserBankCard());
        userSnapshot.setUserBankBranch(orderUser.getUserBankBranch());
        order.setUserSnapshot(JSON.toJSONString(userSnapshot));

        PenguinLogThreadLocal.appendLog("orderInfo", order);
        penguinsOrderMapper.insert(order);

        //通知锁余额
        RspGameResult rspGameResult = this.operateBalance(order, -10000, orderUser.getUserToken());
        if (rspGameResult.getCode() != 1) {
            penguinsOrderMapper.deleteByPrimaryKey(order.getId());
            return Outcome.failure("锁余额失败");
        }
        PenguinLogThreadLocal.appendLog("result", order.getId());
        return Outcome.success(order.getId() + "", order.getId() + "");
    }

    /**
     * 用户获取订单
     *
     * @param req
     * @return
     */
    @Override
    public Outcome<List<RspOrderExtend>> userOrderList(ReqUserOrderList req) {
        REQPenguinsOrder param = new REQPenguinsOrder();
        if (StringUtils.isBlank(req.getUserId())) {
            Outcome outcome = Outcome.success("暂无数据");
            return outcome;
        }
        if (Objects.isNull(req.getOrderType())) {
            Outcome outcome = Outcome.failure("选择交易类型");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;
        }
        param.setOrderStatus(req.getOrderStatus());
        param.setOrderUserId(req.getUserId());
        param.setOrderType(req.getOrderType());
        if (Objects.isNull(req.getPage())) {
            req.setPage(PageParam.DEFAULT_CURRENT_PAGE);
        }
        if (Objects.isNull(req.getSize())) {
            req.setSize(PageParam.DEFAULT_PAGE_SIZE);
        }
        param.setPage((req.getPage() - 1) * req.getSize());
        param.setSize(req.getSize());
        List<RspOrderExtend> result = this.getRspOrderInfos(param);
        PenguinLogThreadLocal.appendLog("result", result);
        return Outcome.success(result);
    }

    @Override
    public Outcome<RspOrderExtend> userGetOrderInfo(String userId, Integer id) {
        PenguinsOrder order = penguinsOrderMapper.selectByPrimaryKey(id);

        if (Objects.isNull(order)) {
            return Outcome.success();
        }
        if (!Objects.equals(userId, order.getOrderUserId())) {
            PenguinLogThreadLocal.failure("订单用户与登录用户不一致, orderUser:" + order.getOrderUserId() + ", gameUser:" + userId);
            return Outcome.failure("订单用户与登录用户不一致");
        }

        JSONObject jsonObject = JSONObject.parseObject(order.getMerchantSnapshot());
        RspOrderExtend rsp = JSON.toJavaObject(jsonObject, RspOrderExtend.class);
        if (Objects.nonNull(order.getOrderTime())) {

            rsp.setOrderTimeStr(DateUtil.getTimestampOfDate(order.getOrderTime()));
        }
        if (Objects.nonNull(order.getConfirmTime())) {
            rsp.setConfirmTimeStr(DateUtil.getTimestampOfDate(order.getConfirmTime()));
        }
        if (Objects.nonNull(order.getPayTime())) {
            rsp.setPayTimeStr(DateUtil.getTimestampOfDate(order.getPayTime()));
        }
        rsp.setOrderNoStr(order.getOrderNo() + "");
        rsp.setOrderId(order.getId());
        rsp.setOrderStatus(order.getOrderStatus());
        if (StringUtils.isNotBlank(order.getOrderTransactorId())) {
            PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(order.getOrderTransactorId());
            PenguinUtil.setUserPhoneToRspOrderExtendp(user, rsp); //设置返回最新号码
        }
        if (Objects.isNull(rsp.getPayAmount())) {
            rsp.setPayAmount(order.getPayAmount());
        }
        if (Objects.equals(order.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
            rsp.setValidTime(userTime);
        }
        if (Objects.equals(order.getOrderType(), OrderTypeEnum.SELL.getValue())) {
            rsp.setValidTime(userSellTime);
        }
        PenguinLogThreadLocal.appendLog("result", rsp);
        return Outcome.success(rsp);
    }

    @Override
    public Outcome<String> getOrderProof(Integer id) {
        PenguinsOrder order = penguinsOrderMapper.selectByPrimaryKey(id);
        if (Objects.isNull(order)) {
            Outcome outcome = Outcome.failure("未获取到订单信息");
            PenguinLogThreadLocal.failure(outcome.getMessage() + ", orderId:" + id);
            return outcome;
        }
        if (Objects.isNull(order.getTransactorProofPath())) {
            Outcome outcome = Outcome.failure("订单未上传凭证");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;

        }
        PenguinLogThreadLocal.appendLog("result", order.getTransactorProofPath());
        return Outcome.success(1, "获取成功", order.getTransactorProofPath());
    }

    @Override
    public Outcome<RspOrderExtend> merchantGetOrderInfo(String userId, Integer id) {
        PenguinsOrder order = penguinsOrderMapper.selectByPrimaryKey(id);

        if (Objects.isNull(order)) {
            return Outcome.success();
        }
        if (!Objects.equals(userId, order.getOrderTransactorId())) {
            PenguinLogThreadLocal.failure("订单用户与登录用户不一致, orderTransactorId:" + order.getOrderTransactorId() + ", gameUser:" + userId);
            return Outcome.failure("订单用户与登录用户不一致");
        }

        JSONObject jsonObject = JSONObject.parseObject(order.getUserSnapshot());
        RspOrderExtend rsp = JSON.toJavaObject(jsonObject, RspOrderExtend.class);
        rsp.setOrderId(order.getId());
        rsp.setOrderStatus(order.getOrderStatus());
        if (Objects.nonNull(order.getOrderTime())) {
            rsp.setOrderTimeStr(DateUtil.getTimestampOfDate(order.getOrderTime()));
        }
        if (Objects.nonNull(order.getConfirmTime())) {
            rsp.setConfirmTimeStr(DateUtil.getTimestampOfDate(order.getConfirmTime()));
        }
        if (Objects.nonNull(order.getPayTime())) {
            rsp.setPayTimeStr(DateUtil.getTimestampOfDate(order.getPayTime()));
        }
        if (StringUtils.isNotBlank(order.getOrderUserId())) {
            PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(order.getOrderUserId());
            if (Objects.nonNull(user) && StringUtils.isNotBlank(user.getUserPhone())) {
                rsp.setPhone(user.getUserPhone());
                if (StringUtils.isBlank(rsp.getRealName())) {
                    rsp.setRealName(user.getBankFavour());
                }
            }
        }
        if (Objects.isNull(rsp.getPayAmount())) {
            rsp.setPayAmount(order.getPayAmount());
        }
        if (Objects.equals(order.getOrderType(), OrderTypeEnum.SELL.getValue())) {
            rsp.setValidTime(userSellTime);
        }
        if (Objects.equals(order.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
            rsp.setValidTime(userTime);
        }
        rsp.setOrderNoStr(order.getOrderNo() + "");
        PenguinLogThreadLocal.appendLog("result", rsp);
        return Outcome.success(rsp);

    }

    @Override
    public Outcome<Boolean> paymentRechargeOrder(String useId, ReqOrderPay reqOrder) {
        if (Objects.isNull(reqOrder.getOrderNo()) || StringUtils.isBlank(reqOrder.getTransactorProofPath())) {
            return Outcome.failure("上传不成功 请重试");
        }
        REQPenguinsOrder param = new REQPenguinsOrder();
        param.setOrderNo(reqOrder.getOrderNo());
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(param);
        if (orderList.isEmpty()) {
            return Outcome.failure("未获取到订单信息");
        }
        PenguinsOrder order = orderList.get(0);
        PenguinLogThreadLocal.appendLog("order", order);
        if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.PAYMENT_ORDER.getValue())) {
            Outcome outcome = Outcome.failure("订单未处于待支付状态");
            PenguinLogThreadLocal.failure(outcome.getMessage() + JSON.toJSONString(order));
            return outcome;
        }
        if (!Objects.equals(order.getOrderUserId(), useId)) {
            Outcome outcome = Outcome.failure("登录用户与充值用户id不一致");
            PenguinLogThreadLocal.failure(outcome.getMessage() + "orderNo:" + order.getOrderNo() + ", gameUserId:" + useId + ", orderUserid:" + order.getOrderUserId());
            return outcome;
        }
        if (!Objects.equals(order.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
            return Outcome.failure("不需要上传交易凭证类型订单");
        }
        Date now = new Date();
        PenguinsOrder updateParam = new PenguinsOrder();
        JSONObject record = new JSONObject();
        record.put("transactorProofPath", new PenguinLogFieldUpdateRecordModel(order.getTransactorProofPath(), reqOrder.getTransactorProofPath()));
        updateParam.setTransactorProofPath(reqOrder.getTransactorProofPath());
        String pattern = "yyyy-MM-dd HH:mm:ss";
        record.put("updateTime", new PenguinLogFieldUpdateRecordModel(order.getUpdateTime() != null
                ? DateFormatUtils.format(order.getUpdateTime(), pattern) : null, DateFormatUtils.format(now, pattern)));
        updateParam.setUpdateTime(now);

        record.put("payTime", new PenguinLogFieldUpdateRecordModel(order.getPayTime() != null
                ? DateFormatUtils.format(order.getPayTime(), pattern) : null, DateFormatUtils.format(now, pattern)));
        updateParam.setPayTime(now);

        record.put("orderStatus", new PenguinLogFieldUpdateRecordModel(order.getOrderStatus(), OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue()));
        updateParam.setOrderStatus(OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue());
        updateParam.setId(order.getId());
        record.put("orderId", order.getId());
        record.put("orderNo", order.getOrderNo());

        PenguinLogThreadLocal.appendLog("record", record);
        int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
        if (i > 0) {
            return Outcome.success("提交凭证成功");
        }
        Outcome outcome = Outcome.failure("提交失败");
        PenguinLogThreadLocal.failure(outcome.getMessage());
        return outcome;
    }

    @Override
    public Outcome<Boolean> merchantConfirmOrder(String userId, ReqOrderPay reqOrder) {
        if (Objects.isNull(reqOrder.getOrderNo())) {
            Outcome outcome = Outcome.failure("参数为空");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;
        }
        REQPenguinsOrder param = new REQPenguinsOrder();
        param.setOrderNo(reqOrder.getOrderNo());
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(param);
        if (orderList.isEmpty()) {
            Outcome outcome = Outcome.failure("未获取到订单信息");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;
        }
        PenguinsOrder order = orderList.get(0);
        PenguinLogThreadLocal.appendLog("order", order);
        if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue())) {
            Outcome outcome = Outcome.failure("订单未处于待放行状态");
            PenguinLogThreadLocal.failure(outcome.getMessage() + ", orderNo:" + order.getOrderNo() + ", orderStatus:" + order.getOrderStatus());
            return outcome;
        }
        if (StringUtils.isBlank(order.getOrderUserId())) {
            Outcome outcome = Outcome.failure("订单用户为空");
            PenguinLogThreadLocal.failure(outcome.getMessage() + ", orderNo:" + order.getOrderNo());
            return outcome;
        }
        if (!Objects.equals(order.getOrderTransactorId(), userId)) {
            Outcome outcome = Outcome.failure("登录用户与订单交易用户id不一致");
            PenguinLogThreadLocal.failure(outcome.getMessage() + "orderNo:" + order.getOrderNo() + ", gameUserId:" + userId + ", orderUserid:" + order.getOrderUserId());
            return outcome;
        }
        PenguinsUser orderUser = penguinsUserMapper.selectByPrimaryKey(order.getOrderUserId());
        if (Objects.isNull(orderUser) || StringUtils.isBlank(orderUser.getUserToken())) {
            Outcome outcome = Outcome.failure("订单用户信息为空");
            PenguinLogThreadLocal.failure(outcome.getMessage() + ", orderNo:" + order.getOrderNo() + ", orderUserId:" + order.getOrderUserId());
            return outcome;
        }

        RspGameResult rspGameResult = this.operateBalance(order, 10000, orderUser.getUserToken());
        if (rspGameResult.getCode() != 1) {
            PenguinLogThreadLocal.failure(rspGameResult.getMessage() + ", 放行失败");
            return Outcome.failure("商家放行失败");
        }

        Date now = new Date();
        PenguinsOrder updateParam = new PenguinsOrder();
        JSONObject record = new JSONObject();
        String pattern = "yyyy-MM-dd HH:mm:ss";

        record.put("updateTime", new PenguinLogFieldUpdateRecordModel(order.getUpdateTime() != null
                ? DateFormatUtils.format(order.getUpdateTime(), pattern) : null, DateFormatUtils.format(now, pattern)));
        updateParam.setUpdateTime(now);

        record.put("orderStatus", new PenguinLogFieldUpdateRecordModel(order.getOrderStatus(), OrderStatusEnum.COMPLETE_ORDER.getValue()));
        updateParam.setOrderStatus(OrderStatusEnum.COMPLETE_ORDER.getValue());

        record.put("confirmTime", new PenguinLogFieldUpdateRecordModel(order.getConfirmTime() != null
                ? DateFormatUtils.format(order.getConfirmTime(), pattern) : null, DateFormatUtils.format(now, pattern)));
        updateParam.setConfirmTime(now);

        updateParam.setOrderRemark("商家确认放行订单，订单完成");
        updateParam.setId(order.getId());

        record.put("orderId", order.getId());
        record.put("orderNo", order.getOrderNo());

        PenguinLogThreadLocal.appendLog("record", record);
        int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
        if (i > 0) {
            return Outcome.success("放行成功");
        }
        Outcome outcome = Outcome.failure("放行失败");
        PenguinLogThreadLocal.failure(outcome.getMessage());
        return outcome;
    }

    @Override
    public Outcome<Boolean> merchantCloseOrder(String userId, String token, ReqOrderPay reqOrder) {
        if (Objects.isNull(reqOrder.getOrderNo())) {
            Outcome outcome = Outcome.failure("参数为空");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;
        }
        REQPenguinsOrder param = new REQPenguinsOrder();
        param.setOrderNo(reqOrder.getOrderNo());
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(param);
        if (orderList.isEmpty()) {
            Outcome outcome = Outcome.failure("未获取到订单信息");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;
        }
        PenguinsOrder order = orderList.get(0);
        PenguinLogThreadLocal.appendLog("order", order);
        if (Objects.equals(order.getOrderStatus(), OrderStatusEnum.CLOSED_ORDER.getValue())) {
            Outcome outcome = Outcome.failure("订单已关闭");
            PenguinLogThreadLocal.failure(outcome.getMessage());
            return outcome;
        }
        if (!Objects.equals(order.getOrderTransactorId(), userId)) {
            Outcome outcome = Outcome.failure("登录用户与交易用户id不一致");
            PenguinLogThreadLocal.failure(outcome.getMessage() + "orderNo:" + order.getOrderNo() + ", gameUserId:" + userId + ", orderUserid:" + order.getOrderUserId());
            return outcome;
        }
        PenguinsOrder updateParam = new PenguinsOrder();
        if (Objects.equals(order.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
            //充值
            if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue())) {
                Outcome outcome = Outcome.failure("订单未处于待放行状态");
                PenguinLogThreadLocal.failure(outcome.getMessage() + "orderNo:" + order.getOrderNo() + ", orderStatus:" + order.getOrderStatus());
                return outcome;
            }
            updateParam.setOrderRemark("商家关闭充值订单");
        } else {
            //卖出
            if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.PAYMENT_ORDER.getValue())) {
                Outcome outcome = Outcome.failure("回收订单未处于待付款状态");
                PenguinLogThreadLocal.failure(outcome.getMessage() + "orderNo:" + order.getOrderNo() + ", orderStatus:" + order.getOrderStatus());
                return outcome;
            }
            if (StringUtils.isBlank(order.getOrderUserId())) {
                return Outcome.failure("下单用户id为空");
            }
            PenguinsUser orderUser = penguinsUserMapper.selectByPrimaryKey(order.getOrderUserId());
            if (Objects.isNull(orderUser) || StringUtils.isBlank(orderUser.getUserToken())) {
                return Outcome.failure("下单用户信息为空");
            }
            token = orderUser.getUserToken();
            //验证用户token是否过期
            Outcome<RspGameUser> checkUser = penguinsUserService.checkUser(token);
            if (!checkUser.ok()) {
                return Outcome.failure("客户长时间未登录，请人工联系客户");
            }
            updateParam.setOrderRemark("商家关闭卖出订单");
        }
        RspGameResult rspGameResult = this.operateBalance(order, 10000, token);
        if (rspGameResult.getCode() != 1) {
            Outcome outcome = Outcome.failure("商家关闭订单失败");
            PenguinLogThreadLocal.failure(rspGameResult.getMessage() + ", " + outcome.getMessage());
            return outcome;
        }

        JSONObject record = new JSONObject();

        record.put("orderStatus", new PenguinLogFieldUpdateRecordModel(order.getOrderStatus(), OrderStatusEnum.CLOSED_ORDER.getValue()));
        updateParam.setOrderStatus(OrderStatusEnum.CLOSED_ORDER.getValue());

        updateParam.setBackMessage(updateParam.getOrderRemark() + "，资金返回成功");
        updateParam.setId(order.getId());

        record.put("orderNo", order.getOrderNo());
        record.put("orderId", order.getId());
        PenguinLogThreadLocal.appendLog("record", record);
        int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
        if (i > 0) {
            return Outcome.success("关闭成功");
        }
        Outcome outcome = Outcome.failure("关闭失败");
        PenguinLogThreadLocal.failure(outcome.getMessage());
        return outcome;
    }

    @Override
    public Outcome<Boolean> merchantOperateOrder(String userId, String token, ReqOrderPay reqOrder) {
        if (Objects.isNull(reqOrder.getOrderNo())) {
            return Outcome.failure("订单编号为空");
        }
        REQPenguinsOrder param = new REQPenguinsOrder();
        param.setOrderNo(reqOrder.getOrderNo());
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(param);
        if (orderList.isEmpty()) {
            return Outcome.failure("未获取到订单信息");
        }
        PenguinsOrder order = orderList.get(0);
        if (!Objects.equals(order.getOrderType(), OrderTypeEnum.SELL.getValue())) {
            return Outcome.failure("该订单不属于回收订单，请核对后再操作");
        }
        if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.PAYMENT_ORDER.getValue())) {
            return Outcome.failure("订单未处于待付款状态");
        }
        if (!Objects.equals(order.getOrderTransactorId(), userId)) {
            return Outcome.failure("登录用户与交易用户id不一致");
        }

        RspGameResult rspGameResult = this.operateBalance(order, 10000, token);
        if (rspGameResult.getCode() != 1) {
            return Outcome.failure("商家操作订单失败");
        }

        PenguinsOrder updateParam = new PenguinsOrder();
        updateParam.setUpdateTime(new Date());
        updateParam.setOrderStatus(OrderStatusEnum.COMPLETE_ORDER.getValue());
        updateParam.setConfirmTime(new Date());
        updateParam.setOrderRemark("商家付款回收订单");
        updateParam.setBackMessage("商家回收订单成功");
        updateParam.setId(order.getId());
        int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
        if (i > 0) {
            return Outcome.success("回收成功");
        }
        return Outcome.failure("回收失败");
    }

    /**
     * 商家获取订单
     *
     * @param req
     * @return
     */
    @Override
    public Outcome<List<RspOrderInfo>> merchantOrderList(ReqUserOrderList req) {
        REQPenguinsOrder param = new REQPenguinsOrder();
        if (StringUtils.isBlank(req.getUserId())) {
            return Outcome.failure("暂无数据");
        }
        if (Objects.isNull(req.getOrderType())) {
            return Outcome.failure("选择交易类型");
        }
        param.setOrderStatus(req.getOrderStatus());
        param.setOrderTransactorId(req.getUserId());
        param.setOrderType(req.getOrderType());
        if (Objects.isNull(req.getPage())) {
            req.setPage(PageParam.DEFAULT_CURRENT_PAGE);
        }
        if (Objects.isNull(req.getSize())) {
            req.setSize(PageParam.DEFAULT_PAGE_SIZE);
        }
        param.setPage((req.getPage() - 1) * req.getSize());
        param.setSize(req.getSize());
        List<RspOrderInfo> result = new ArrayList<>();
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(param);
        if (orderList.isEmpty()) {
            return Outcome.success(result);
        }
        orderList = orderList.stream().sorted(Comparator.comparing(PenguinsOrder::getOrderTime).reversed()).collect(Collectors.toList());
        for (PenguinsOrder order : orderList) {
            if (StringUtils.isBlank(order.getUserSnapshot())) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(order.getUserSnapshot());
            RspOrderInfo rsp = JSON.toJavaObject(jsonObject, RspOrderInfo.class);
            if (Objects.nonNull(order.getOrderTime())) {
                rsp.setOrderTimeStr(DateUtil.getTimestampOfDate(order.getOrderTime()));
            }
            rsp.setOrderNoStr(order.getOrderNo() + "");
            rsp.setOrderId(order.getId());
            rsp.setOrderStatus(order.getOrderStatus());
            if (StringUtils.isBlank(rsp.getRealName())) {
                PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(order.getOrderUserId());
                if (Objects.nonNull(user)) {
                    rsp.setRealName(user.getBankFavour());
                }
            }
            result.add(rsp);
        }
        return Outcome.success(result);
    }

    @Override
    public Outcome<RspTransReport> getTransReport() {
        //获取今日数据
        Date now = new Date();
        Date todayStart = DateUtil.minusDayFirst(now, 0);
        Date yesterday = DateUtil.minusDayFirst(now, -1);
        int status = OrderStatusEnum.COMPLETE_ORDER.getValue();
        RspTransReport todayData = penguinsOrderMapper.getTransReport(null, status, todayStart, now);
        RspTransReport yesterdayData = penguinsOrderMapper.getTransReport(null, status, yesterday, todayStart);
        RspTransReport totalData = penguinsOrderMapper.getTransReport(null, status, null, null);
        RspTransReport result = new RspTransReport();
        if (Objects.nonNull(todayData)) {
            result.setTodayCny(todayData.getTotalCny());
            result.setTodaySaro(todayData.getTotalSaro());
        }
        if (Objects.nonNull(yesterdayData)) {
            result.setYesterdayCny(yesterdayData.getTotalCny());
            result.setYesterdaySaro(yesterdayData.getTotalSaro());
        }
        if (Objects.nonNull(totalData)) {
            result.setTotalCny(totalData.getTotalCny());
            result.setTotalSaro(totalData.getTotalSaro());
        }
        return Outcome.success(result);
    }

    @Override
    public Outcome<Page<RspOrderExtend>> getOrderList(REQPenguinsOrder req) {
        Page<RspOrderExtend> page = new Page<>();
        if (Objects.isNull(req.getPage())) {
            req.setPage(PageParam.DEFAULT_CURRENT_PAGE);
        }
        if (Objects.isNull(req.getSize())) {
            req.setSize(PageParam.DEFAULT_PAGE_SIZE);
        }
        page.setCurrent(req.getPage());
        page.setSize(req.getSize());
        req.setPage((req.getPage() - 1) * req.getSize());
        req.setSize(req.getSize());
        //设置前端请求时间转换减八个小时
        try {
            DateUtil.formatTimeEight(req);
        } catch (Exception e) {
            PenguinLogThreadLocal.failure("前端日期格式转换异常！");
        }
        Integer count = penguinsOrderMapper.getOrderListCount(req);
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(req);
        page.setTotal(count);
        if (orderList.size() < 1) {
            page.setRecords(null);
            return Outcome.success(page);
        }
        List<RspOrderExtend> result = new ArrayList<>();
        orderList.forEach(order -> {
            RspOrderExtend rsp = new RspOrderExtend();
            rsp.setOrderId(order.getId());
            rsp.setOrderNoStr(order.getOrderNo() + "");
            rsp.setPayAmount(order.getPayAmount());
            rsp.setSellUnitPrice(order.getOrderUnitPrice());
            rsp.setOrderUnitPrice(order.getOrderUnitPrice());
            rsp.setSaroNumber(order.getOrderActualSaro());
            rsp.setNickname(order.getOrderUserNickname());
            rsp.setUserAccount(order.getOrderUserAccount());
            JSONObject jsonObject = JSONObject.parseObject(order.getUserSnapshot());
            RspOrderExtend user = JSON.toJavaObject(jsonObject, RspOrderExtend.class);
            rsp.setUserPhone(user.getUserPhone());
            if (StringUtils.isNotBlank(user.getUserId())) {
                PenguinsUser orderUser = penguinsUserMapper.selectByPrimaryKey(user.getUserId());
                if (Objects.nonNull(orderUser)) {
                    rsp.setUserPhone(orderUser.getUserPhone());
                }
                PenguinUtil.setUserPhoneToRspOrderExtendp(orderUser, rsp);
            }

            JSONObject json = JSONObject.parseObject(order.getMerchantSnapshot());
            RspOrderExtend merchant = JSON.toJavaObject(json, RspOrderExtend.class);
            rsp.setMerchantAccount(order.getOrderTransactorAccount());
            rsp.setMerchantNickname(merchant.getNickname());
            if (StringUtils.isNotBlank(merchant.getUserId())) {
                PenguinsUser merchantUser = penguinsUserMapper.selectByPrimaryKey(merchant.getUserId());
                if (Objects.nonNull(merchantUser)) {
                    rsp.setUserPhone(merchantUser.getUserPhone());
                }
                PenguinUtil.setMerchantPhoneToRspOrderExtendp(merchantUser, rsp);
            }
            if (Objects.nonNull(order.getOrderTime())) {
                rsp.setOrderTimeStr(DateUtil.getTimestampOfDate(order.getOrderTime()));
            }
            if (Objects.nonNull(order.getConfirmTime())) {
                rsp.setConfirmTimeStr(DateUtil.getTimestampOfDate(order.getConfirmTime()));
            }
            if (Objects.nonNull(order.getPayTime())) {
                rsp.setPayTimeStr(DateUtil.getTimestampOfDate(order.getPayTime()));
            }

            rsp.setTransactorProof(order.getTransactorProofPath());
            rsp.setOrderStatus(order.getOrderStatus());
            if (Objects.equals(OrderTypeEnum.SELL.getValue(), order.getOrderType())) {
                rsp.setBankFavour(user.getBankFavour());
                rsp.setUserBankBranch(user.getUserBankBranch());
                rsp.setUserBank(user.getUserBank());
                rsp.setUserBankCard(user.getUserBankCard());
            }
            result.add(rsp);
        });
        page.setRecords(result);
        return Outcome.success(page);
    }


    public int getWaitReleasedNum(String userId, Integer orderStatus) {
        REQPenguinsOrder order = new REQPenguinsOrder();
        order.setOrderUserId(userId);
        order.setOrderType(orderStatus);
        return penguinsOrderMapper.getOrderListCount(order);
    }

    @Override
    public Outcome<String> editOrderBySystem(ReqOperateOrder req) {
        if (Objects.isNull(req)) {
            return Outcome.failure("参数为空");
        }
        if (Objects.isNull(req.getOrderId())) {
            return Outcome.failure("订单id为空");
        }
        if (Objects.isNull(req.getOrderType())) {
            return Outcome.failure("订单类型为空");
        }
        if (Objects.isNull(req.getOperateType())) {
            return Outcome.failure("未选择操作类型");
        }
        PenguinsOrder order = penguinsOrderMapper.selectByPrimaryKey(req.getOrderId());
        if (Objects.isNull(order)) {
            return Outcome.failure("未获取到对应的订单");
        }
        if (Objects.isNull(order.getOrderTransactorId())) {
            return Outcome.failure("未获取到订单交易人");
        }
        PenguinsUser merchant = penguinsUserMapper.selectByPrimaryKey(order.getOrderTransactorId());
        if (Objects.isNull(merchant) || Objects.isNull(merchant.getUserToken())) {
            return Outcome.failure("商家token失效");
        }
        if (Objects.isNull(order.getOrderUserId())) {
            return Outcome.failure("未获取到订单下单人");
        }
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(order.getOrderUserId());
        if (Objects.isNull(user) || Objects.isNull(user.getUserToken())) {
            return Outcome.failure("用户token失效");
        }

        if (Objects.equals(req.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
            //充值订单

            if (!Objects.equals(order.getOrderType(), OrderTypeEnum.RECHARGE.getValue())) {
                return Outcome.failure("订单类型不一致");
            }
            if (Objects.equals(req.getOperateType(), OperateTypeEnum.CLOSE.getValue())) {
                //关闭订单
                if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue())) {
                    return Outcome.failure("订单未处于待放行状态");
                }
                RspGameResult rspGameResult = this.operateBalance(order, 10000, merchant.getUserToken());
                return closeOrder(order, rspGameResult);
            }
            //放行订单
            if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue())) {
                return Outcome.failure("订单未处于待放行");
            }

            RspGameResult result = this.operateBalance(order, 10000, user.getUserToken());
            if (result.getCode() != 1) {
                return Outcome.failure("商家放行失败");
            }

            PenguinsOrder updateParam = new PenguinsOrder();
            updateParam.setUpdateTime(new Date());
            updateParam.setOrderStatus(OrderStatusEnum.COMPLETE_ORDER.getValue());
            updateParam.setConfirmTime(new Date());
            updateParam.setOrderRemark("商家确认放行订单，订单完成");
            updateParam.setId(order.getId());

            int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
            if (i > 0) {
                return Outcome.success("放行成功");
            }

            return Outcome.failure("放行失败");
        }

        //卖出订单
        if (Objects.equals(req.getOperateType(), OperateTypeEnum.CLOSE.getValue())) {
            //关闭订单
            if (!Objects.equals(order.getOrderType(), OrderTypeEnum.SELL.getValue())) {
                return Outcome.failure("订单类型不一致");
            }
            if (!Objects.equals(OrderStatusEnum.PAYMENT_ORDER.getValue(), order.getOrderStatus())) {
                return Outcome.failure("订单未处于待收款");
            }
            RspGameResult result = this.operateBalance(order, 10000, user.getUserToken());
            return closeOrder(order, result);
        }
        //确认付款
        if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.PAYMENT_ORDER.getValue())) {
            return Outcome.failure("订单未处于待付款状态");
        }

        RspGameResult rspGameResult = this.operateBalance(order, 10000, merchant.getUserToken());
        if (rspGameResult.getCode() != 1) {
            return Outcome.failure("商家操作订单失败");
        }

        PenguinsOrder updateParam = new PenguinsOrder();
        updateParam.setUpdateTime(new Date());
        updateParam.setOrderStatus(OrderStatusEnum.COMPLETE_ORDER.getValue());
        updateParam.setConfirmTime(new Date());
        updateParam.setOrderRemark("后台付款回收订单");
        updateParam.setBackMessage("后台回收订单成功");
        updateParam.setId(order.getId());
        int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
        if (i > 0) {
            return Outcome.success("回收成功");
        }
        return Outcome.failure("回收失败");
    }



    private Outcome<String> closeOrder(PenguinsOrder order, RspGameResult rspGameResult) {
        if (rspGameResult.getCode() != 1) {
            return Outcome.failure("商家关闭订单失败");
        }
        PenguinsOrder updateParam = new PenguinsOrder();
        JSONObject record = new JSONObject();

        updateParam.setOrderRemark("后台关闭充值订单");
        updateParam.setOrderStatus(OrderStatusEnum.CLOSED_ORDER.getValue());
        updateParam.setBackMessage(updateParam.getOrderRemark() + "，资金返回成功");
        updateParam.setId(order.getId());

        record.put("orderStatus", new PenguinLogFieldUpdateRecordModel(order.getOrderStatus(), OrderStatusEnum.CLOSED_ORDER.getValue()));
        record.put("orderNo", order.getOrderNo());
        record.put("orderId", order.getId());
        PenguinLogThreadLocal.appendLog("record", record);
        int i = penguinsOrderMapper.updateByPrimaryKeySelective(updateParam);
        if (i > 0) {
            return Outcome.success("关闭成功");
        }
        return Outcome.failure("关闭失败");
    }

    private List<RspOrderExtend> getRspOrderInfos(REQPenguinsOrder param) {
        List<RspOrderExtend> result = new ArrayList<>();
        List<PenguinsOrder> orderList = penguinsOrderMapper.getOrderList(param);
        if (orderList.isEmpty()) {
            return result;
        }
        orderList = orderList.stream().sorted(Comparator.comparing(PenguinsOrder::getOrderTime).reversed()).collect(Collectors.toList());
        for (PenguinsOrder order : orderList) {
            if (StringUtils.isBlank(order.getOrderTransactorId())) {
                continue;
            }
            //PenguinsUser penguinsUser = penguinsUserMapper.selectByPrimaryKey(order.getOrderTransactorId());
            //RSPOrderExtend rsp = new RSPOrderExtend();
            //获取商家快照
            if (StringUtils.isNotBlank(order.getMerchantSnapshot())) {
                JSONObject jsonObject = JSONObject.parseObject(order.getMerchantSnapshot());
                RspOrderExtend rsp = JSON.toJavaObject(jsonObject, RspOrderExtend.class);
                if (Objects.nonNull(order.getOrderTime())) {
                    rsp.setOrderTimeStr(DateUtil.getTimestampOfDate(order.getOrderTime()));
                }
                rsp.setOrderNoStr(order.getOrderNo() + "");
                rsp.setOrderId(order.getId());
                rsp.setOrderStatus(order.getOrderStatus());
                result.add(rsp);
            }
        }
        return result;
    }

    /**
     * 操作用户用户余额
     *
     * @param order     订单信息
     * @param number    正负10000
     * @param userToken 操作的用户token
     * @return 操作结果
     */
    private RspGameResult operateBalance(PenguinsOrder order, int number, String userToken) {
        Outcome<RspGameUser> checkUser = penguinsUserService.checkUser(userToken);
        if (!checkUser.ok()) {
            RspGameResult result = new RspGameResult();
            result.setCode(0);
            return result;
        }
        JSONObject object = new JSONObject();
        Map<String, Object> params = new HashMap<>();
        params.put("order_no", order.getOrderNo() + "");
        params.put("saro_quantity", order.getOrderActualSaro().multiply(new BigDecimal(number)).setScale(0));
        object.put("requestData", params);
        String json = JSON.toJSONString(params);
        RspGameResult result = restTemplateService.doPost(tradeUrl, userToken, json);
        object.put("requestResult", result);
        object.put("url", tradeUrl);
        object.put("userToken", userToken);
        PenguinLogThreadLocal.appendLog("operateBalance", object);
        return result;
    }
}
