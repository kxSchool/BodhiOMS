package com.penguin.website.service;

import com.penguin.website.bean.req.*;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.model.domain.PenguinsUser;

import java.util.List;

public interface PenguinOrderService {
    Outcome<String> createRechargeOrder(RspGameUser gameUser, ReqOrder reqOrder);

    Outcome<String> createSellOrder(RspGameUser gameUser, ReqOrder reqOrder);

    Outcome<Boolean> paymentRechargeOrder(String userId, ReqOrderPay reqOrder);

    Outcome<Boolean> merchantConfirmOrder(String userId, ReqOrderPay reqOrder);

    Outcome<Boolean> merchantCloseOrder(String userId, String token, ReqOrderPay reqOrder);

    Outcome<Boolean> merchantOperateOrder(String userId, String token, ReqOrderPay reqOrder);

    Outcome<RspOrderExtend> userGetOrderInfo(String userId, Integer id);

    Outcome<String> getOrderProof(Integer id);

    Outcome<RspOrderExtend> merchantGetOrderInfo(String userId, Integer id);

    /**
     * 用户获取订单
     * @param req
     * @return
     */
    Outcome<List<RspOrderExtend>> userOrderList(ReqUserOrderList req);

    /**
     * 商家获取订单
     * @param req
     * @return
     */
    Outcome<List<RspOrderInfo>> merchantOrderList(ReqUserOrderList req);

    Outcome<RspTransReport> getTransReport();

    Outcome<Page<RspOrderExtend>> getOrderList(REQPenguinsOrder req);

    int getUserWaitReleasedNum(String userId, Integer orderType, Integer orderStatus);

    int getMerchantWaitReleasedNum(String userId, Integer orderType, Integer orderStatus);

    Outcome<String> editOrderBySystem(ReqOperateOrder req);

}
