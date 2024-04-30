package com.penguin.website.web.controller.penguin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.*;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.model.domain.Attachment;
import com.penguin.website.model.domain.Log;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.dto.LogConstant;
import com.penguin.website.model.enums.*;
import com.penguin.website.util.JwtTokenUtil;
import com.penguin.website.util.PenguinUtil;
import com.penguin.website.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@Api(tags = "交易", value = "交易相关操作")
@RestController
@RequestMapping(value = "/api/trans/")
public class TransController extends BaseController {

    @ApiOperation(value = "获取游戏用户token", notes = "获取游戏用户token")
    @GetMapping(value = "receive/userToken/{token}")
    public Outcome<String> getUserToken(HttpServletRequest request, HttpServletResponse response, @PathVariable String token) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取游戏用户token", token, null);
            Outcome<RspGameUser> outcome = penguinsUserService.checkUser(token);
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            if (!outcome.ok()) {
                response.setHeader("Location", vueHost + "/#/Error");
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            Outcome<PenguinsUser> stringOutcome = penguinsUserService.editUser(outcome.getData(), token);
            if (!stringOutcome.ok()) {
                response.setHeader("Location", vueHost + "/#/Error");
                return PenguinUtil.setLogAndOutComeFailure(stringOutcome.getMessage());
            }

            Outcome<PenguinsUser> penguinsUserOutcome=penguinsUserService.checkUserStatus(outcome.getData().getUser_id());
            if(!penguinsUserOutcome.ok()){
                response.setHeader("Location", vueHost + "/#/Accou");
                return PenguinUtil.setLogAndOutComeFailure(penguinsUserOutcome.getMessage());
            }

            PenguinsUser user = stringOutcome.getData();
            if (Objects.nonNull(user.getUserRole()) && Objects.equals(user.getUserRole(), UserRoleEnum.GAME_MERCHANT.getValue())) {
                response.setHeader("Location", vueHost + "/#/Merchan?t=" + user.getUserAccessToken());
            } else {
                response.setHeader("Location", vueHost + "/#/Home?t=" + user.getUserAccessToken());
            }
            response.setHeader("Connection", "close");
            return Outcome.success(user.getUserAccessToken(), user.getUserAccessToken());
        } finally {
            logService.saveLog(PenguinLogEventEnum.GET_TOKEN, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "通过临时token获取游戏用户token", notes = "通过临时token获取游戏用户token")
    @GetMapping(value = "receive/userAccessToken/{userAccessToken}")
    public Outcome<String> getUserAccessToken(HttpServletRequest request, @PathVariable("userAccessToken") String userAccessToken) {
        try {
            PenguinLogThreadLocal.setBaseInfo("通过临时token获取游戏用户token", userAccessToken, null);
            return PenguinUtil.setErrorLog(penguinsUserService.getUserByAccessToken(userAccessToken));
        } finally {
            logService.saveLog(PenguinLogEventEnum.GET_TOKEN, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "校验游戏端用户合法性", notes = "校验游戏端用户合法性")
    @PostMapping(value = "checkUser")
    public Outcome<RspGameUser> checkUser(HttpServletRequest request, @RequestHeader("token") String token) {
        try {
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            if (StringUtils.isBlank(userToken)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            return penguinsUserService.checkUser(userToken);
        } finally {
            logService.saveLog(PenguinLogEventEnum.CHECK_TOKEN, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "获取充值商家列表", notes = "获取充值商家列表")
    @PostMapping(value = "merchant/list")
    public Outcome<List<RspUserPrice>> getMerchantList(@RequestHeader(value = "token") String token, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取充值商家列表", token, null);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            return PenguinUtil.setErrorLog(penguinsUserService.getMerchantList(userToken));
        } finally {
            logService.saveLog(PenguinLogEventEnum.RECHARGE_MERCHANT_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "获取回收商家列表", notes = "获取回收商家列表")
    @GetMapping(value = "recovery/merchant/list")
    public Outcome<List<RspUserPrice>> getRecoveryMerchantList(@RequestHeader(value = "token") String token, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取回收商家列表", token, null);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            return PenguinUtil.setErrorLog(penguinsUserService.getRecoveryMerchantList(userToken));
        } finally {
            logService.saveLog(PenguinLogEventEnum.RECHARGE_SELL_MERCHANT_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "获取单个充值商家", notes = "获取单个充值商家")
    @PostMapping(value = "merchant/info")
    public Outcome<RspUserPrice> getMerchantList(@RequestHeader(value = "token") String token, @RequestBody ReqUser reqUser, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取单个充值商家", token, reqUser);
            if (StringUtils.isBlank(reqUser.getUserId())) {
                return PenguinUtil.setLogAndOutComeFailure("merchant id is null");
            }
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            return PenguinUtil.setErrorLog(penguinsUserService.getMerchantInfo(reqUser));
        } finally {
            logService.saveLog(PenguinLogEventEnum.GET_MERCHANT_INFO, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "获取用户商家个人信息", notes = "获取用户商家个人信息")
    @PostMapping(value = "merchant")
    public Outcome<RspUser> getMerchantByUserId(@RequestHeader(value = "token") String token, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取用户商家个人信息", token, null);
            if (StringUtils.isBlank(token)) {
                return PenguinUtil.setLogAndOutComeFailure("参数为空");
            }
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }
            return PenguinUtil.setErrorLog(penguinsUserService.getMerchantByUserId(userId));
        } finally {
            logService.saveLog(PenguinLogEventEnum.GET_MERCHANT_INFO, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "校验用户商家电话、银行卡信息", notes = "校验用户商家电话、银行卡信息")
    @PostMapping(value = "check/merchant")
    public Outcome<String> checkMerchantByUserId(@RequestHeader(value = "token") String token, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("校验用户电话、交易密码、银行卡信息", token, null);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }
            return PenguinUtil.setErrorLog(penguinsUserService.checkMerchantByUserId(userId));

        } finally {
            logService.saveLog(PenguinLogEventEnum.CHECK_TOKEN, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "用户商家设置个人信息", notes = "用户商家个人信息")
    @PutMapping(value = "updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<Boolean> updateUser(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("用户基本信息") ReqUser req) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户商家设置个人信息", token, req);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }
            req.setUserId(userId);
            return PenguinUtil.setErrorLog(penguinsUserService.updatePenguinUser(req));
        } finally {
            logService.saveLog(PenguinLogEventEnum.EDIT_PHONE_CARD, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "用户获取订单列表", notes = "用户获取订单列表")
    @PostMapping(value = "user/getOrderList", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<HashMap> userGetOrderList(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("获取订单列表参数") ReqUserOrderList req) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户获取订单列表", token, req);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }
            req.setUserId(userId);
            Outcome<List<RspOrderExtend>> result = penguinOrderService.userOrderList(req);
            if (!result.ok()) {
                PenguinLogThreadLocal.failure(result.getMessage());
            }

            Integer waitPayNum=penguinOrderService.getUserWaitReleasedNum(req.getUserId(),req.getOrderType(), OrderStatusEnum.PAYMENT_ORDER.getValue());
            Integer waitReleasedNum=penguinOrderService.getUserWaitReleasedNum(req.getUserId(),req.getOrderType(),OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue());
            //设置返回格式
            HashMap hashMap= PenguinUtil.setOrderList(result.getData(),waitPayNum,waitReleasedNum);
            return Outcome.success(hashMap);
        } finally {
            logService.saveLog(PenguinLogEventEnum.ORDER_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "用户下单", notes = "用户下单")
    @PostMapping(value = "user/createRechargeOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<String> createRechargeOrder(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam(value = "创建订单信息") ReqOrder reqOrder) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户下单", token, reqOrder);
            Outcome<Boolean> check = penguinsUserService.checkUserByToken(token);
            if (!check.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(check.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            if (StringUtils.isBlank(userToken)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }
            Outcome<RspGameUser> outcome = penguinsUserService.checkUser(userToken);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }

            if (Objects.nonNull(reqOrder.getOrderType()) && Objects.equals(reqOrder.getOrderType(), OrderTypeEnum.SELL.getValue())) {
                reqOrder.setToken(token);
                return PenguinUtil.setErrorLog(penguinOrderService.createSellOrder(outcome.getData(), reqOrder));
            }

            return PenguinUtil.setErrorLog(penguinOrderService.createRechargeOrder(outcome.getData(), reqOrder));

        } finally {
            logService.saveLog(PenguinLogEventEnum.CREATE_ORDER, PenguinLogEventActionEnum.CREATE, request);
        }
    }

    @ApiOperation(value = "检查商户单价是否有变动", notes = "检查商户单价是否有变动")
    @PostMapping(value = "check/price", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<String> checkPrice(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam(value = "创建订单信息") ReqOrder reqOrder) {
        try {

            PenguinLogThreadLocal.setBaseInfo("检查商户单价是否有变动", token, reqOrder);
            Outcome<Boolean> check = penguinsUserService.checkUserByToken(token);
            if (!check.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(check.getMessage());
            }
            return penguinsUserService.checkMerchantPrice(reqOrder.getOrderTransactorId(), reqOrder);
        } finally {
            logService.saveLog(PenguinLogEventEnum.CREATE_ORDER, PenguinLogEventActionEnum.CREATE, request);
        }
    }

    @ApiOperation(value = "用户获取订单详情", notes = "用户获取订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "订单id", required = true)
    @PostMapping(value = "user/getOrderInfo/{id}")
    public Outcome<RspOrderExtend> userGetOrderInfo(HttpServletRequest request, @RequestHeader(value = "token") String token, @PathVariable Integer id) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户获取订单详情", token, id);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            Outcome<RspOrderExtend> result = penguinOrderService.userGetOrderInfo(userId, id);
            if (!result.ok()) {
                PenguinLogThreadLocal.failure(result.getMessage());
            }
            return result;
        } finally {
            logService.saveLog(PenguinLogEventEnum.ORDER_DETAIL, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "支付订单", notes = "支付订单")
    @PutMapping(value = "user/paymentRechargeOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome paymentRechargeOrder(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("交易凭证") ReqOrderPay reqOrderPay) {
        try {
            PenguinLogThreadLocal.setBaseInfo("支付订单", token, reqOrderPay);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            return penguinOrderService.paymentRechargeOrder(userId, reqOrderPay);
        } finally {
            logService.saveLog(PenguinLogEventEnum.PAYMENT_ORDER, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "商家放行订单", notes = "商家放行订单")
    @PutMapping(value = "merchant/confirmOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome merchantConfirmOrder(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("交易信息") ReqOrderPay reqOrderPay) {
        try {

            PenguinLogThreadLocal.setBaseInfo("商家放行订单", token, reqOrderPay);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            return penguinOrderService.merchantConfirmOrder(userId, reqOrderPay);
        } finally {
            logService.saveLog(PenguinLogEventEnum.CONFIRM_ORDER, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "商家获取自身信息", notes = "商家获取自身信息")
    @PostMapping(value = "merchant/index", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<RspGameUser> merchantIndex(HttpServletRequest request, @RequestHeader(value = "token") String token) {

        try {
            PenguinLogThreadLocal.setBaseInfo("商家获取自身信息", token, null);

            Outcome<Boolean> check = penguinsUserService.checkUserByToken(token);
            if (!check.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(check.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            if (StringUtils.isBlank(userToken)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }

            Outcome<RspGameUser> outcome = penguinsUserService.checkUser(userToken);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            RspGameUser data = outcome.getData();
            Outcome<PenguinsUser> userById = penguinsUserService.getUserById(data.getUser_id());
            if (!userById.ok()) {
                PenguinLogThreadLocal.failure("数据库用户信息查询未空,  userId:" + data.getUser_id());
                return Outcome.failure("未获取到用户信息");
            }
            if (Objects.nonNull(userById.getData()) && Objects.equals(userById.getData().getUserRole(), 2)) {
                if (Objects.nonNull(data.getSaro_quantity())) {
                    data.setSaro_quantity(data.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN));
                }
                return Outcome.success(data);
            }
            Outcome failure = Outcome.failure("用户为非商家");
            PenguinLogThreadLocal.failure(failure.getMessage());
            return failure;
        } finally {
            logService.saveLog(PenguinLogEventEnum.GET_TOKEN_INFO, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "商家关闭订单", notes = "商家关闭订单")
    @PutMapping(value = "merchant/closeOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome merchantCloseOrder(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("交易信息") ReqOrderPay reqOrderPay) {
        try {

            PenguinLogThreadLocal.setBaseInfo("商家关闭订单", token, reqOrderPay);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            if (StringUtils.isBlank(userToken)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }

            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            return penguinOrderService.merchantCloseOrder(userId, userToken, reqOrderPay);
        } finally {
            logService.saveLog(PenguinLogEventEnum.CLOSE_ORDER, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "商家操作回收订单", notes = "商家操作回收订单")
    @PutMapping(value = "merchant/operateOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome merchantOperateOrder(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("交易信息") ReqOrderPay reqOrderPay) {
        try {
            PenguinLogThreadLocal.setBaseInfo("商家操作回收订单", token, reqOrderPay);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userToken = JwtTokenUtil.getUserToken(token);
            if (StringUtils.isBlank(userToken)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }

            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            Outcome<Boolean> result = penguinOrderService.merchantOperateOrder(userId, userToken, reqOrderPay);
            if (result.ok()) {
                PenguinLogThreadLocal.appendLog("result", result.getData());
            } else {
                PenguinLogThreadLocal.failure(result.getMessage());
            }
            return result;
        } finally {
            logService.saveLog(PenguinLogEventEnum.OPERATE_ORDER, PenguinLogEventActionEnum.EDIT, request);
        }

    }

    @ApiOperation(value = "商家获取订单列表", notes = "商家获取订单列表")
    @PostMapping(value = "merchant/getOrderList", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<HashMap> merchantGetOrderList(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("获取订单列表参数") ReqUserOrderList req) {
        try {
            PenguinLogThreadLocal.setBaseInfo("商家获取订单列表", token, req);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                return PenguinUtil.setLogAndOutComeFailure(outcome.getMessage());
            }
            if (Objects.isNull(req)) {
                return PenguinUtil.setLogAndOutComeFailure("参数为空");
            }

            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                return PenguinUtil.setLogAndOutComeFailure("token失效");
            }
            req.setUserId(userId);

            Outcome<List<RspOrderInfo>> result = penguinOrderService.merchantOrderList(req);
            if (result.ok()) {
                PenguinLogThreadLocal.appendLog("result", result.getData());
            } else {
                PenguinLogThreadLocal.failure(result.getMessage());
            }
            //查询待放行和待付款总的个数  waitReleasedNum waitPayNum
            Integer waitPayNum = penguinOrderService.getMerchantWaitReleasedNum(req.getUserId(), req.getOrderType(), OrderStatusEnum.PAYMENT_ORDER.getValue());
            Integer waitReleasedNum = penguinOrderService.getMerchantWaitReleasedNum(req.getUserId(), req.getOrderType(), OrderStatusEnum.COMPLETE_PAYMENT_ORDER.getValue());

            //设置返回格式
            HashMap hashMap = PenguinUtil.setOrderList(result.getData(), waitPayNum, waitReleasedNum);
            return Outcome.success(hashMap);
        } finally {
            logService.saveLog(PenguinLogEventEnum.ORDER_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "商家获取订单详情", notes = "商家获取订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "订单id", required = true)
    @PostMapping(value = "merchant/getOrderInfo/{id}")
    public Outcome<RspOrderExtend> merchantGetOrderInfo(HttpServletRequest request, @RequestHeader(value = "token") String token, @PathVariable Integer id) {
        try {

            PenguinLogThreadLocal.setBaseInfo("商家获取订单详情", token, id);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            if (Objects.isNull(id)) {
                Outcome failure = Outcome.failure("参数为空");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }

            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            return penguinOrderService.merchantGetOrderInfo(userId, id);
        } finally {
            logService.saveLog(PenguinLogEventEnum.ORDER_DETAIL, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "上传附件", notes = "上传附件")
    @PostMapping(value = "/upload")
    public Outcome<String> upload(@RequestHeader(value = "token") String token, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        log.info("file:" + file.toString());
        log.info("file:" + file);
        Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
        if (!outcome.ok()) {
            return Outcome.failure(outcome.getMessage());
        }

        String userId = JwtTokenUtil.getUserId(token);
        if (StringUtils.isBlank(userId)) {
            Outcome failure = Outcome.failure("token失效");
            return failure;
        }
        return uploadAttachment(userId, file, request);

    }

    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "订单id", required = true)
    @ApiOperation(value = "获取订单交易凭证", notes = "获取订单交易凭证")
    @PostMapping(value = "merchant/getOrderProof/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Outcome<String> getOrderProof(HttpServletRequest request, @RequestHeader(value = "token") String token, @PathVariable Integer id) {
        try {

            PenguinLogThreadLocal.setBaseInfo("获取订单交易凭证", token, id);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }

            if (Objects.isNull(id)) {
                Outcome failure = Outcome.failure("参数为空");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }

            return penguinOrderService.getOrderProof(id);
        } finally {
            logService.saveLog(PenguinLogEventEnum.ORDER_PROOF, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    public Outcome<String> uploadAttachment(String userId, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        if (!file.isEmpty()) {
            try {
                // 获取用户目录
                //String userPath = System.getProperties().getProperty("user.home") + "/penguin/";
                //String userPath = imageUrl;
                // 保存目录
                StringBuffer hold = new StringBuffer("upload/");

                System.out.println("file size :" + file.getSize());
                if (file.getSize() > 10485760L) {
                    return Outcome.failure("文件过大");
                }
                // 获取时间，以年月创建目录
                Date date = DateUtil.date();
                hold.append(DateUtil.thisYear()).append("/").append(DateUtil.thisMonth() + 1).append("/");
                File mediaPath = new File(imageUrl, hold.toString());
                // 如果没有该目录则创建
                if (!mediaPath.exists()) {
                    mediaPath.mkdirs();
                }
                System.out.println("路径++++++" + mediaPath);
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                // 生成文件名称
                String nameSuffix = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))
                        .replaceAll(" ", "_").replaceAll(",", "") + format.format(DateUtil.date())
                        + new Random().nextInt(1000);
                // 文件后缀
                String fileSuffix = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                // 上传文件名加后缀
                String fileName = nameSuffix + "." + fileSuffix;

                // 转存文件
                file.transferTo(new File(mediaPath.toString(), fileName));

                // 原图片路径
                StringBuffer originalPath = new StringBuffer();
                originalPath.append(mediaPath.getAbsolutePath()).append("/").append(fileName);
                // 压缩图片路径
                StringBuffer compressPath = new StringBuffer();
                compressPath.append(mediaPath.getAbsolutePath()).append("/").append(nameSuffix).append("_small.")
                        .append(fileSuffix);
                // 压缩图片
                Thumbnails.of(originalPath.toString()).size(256, 256).keepAspectRatio(false).toFile(compressPath.toString());
                // 原图数据库路径
                StringBuffer originalDataPath = new StringBuffer();
                originalDataPath.append("/").append(hold).append(fileName);
                // 压缩图数据库路径
                StringBuffer compressDataPath = new StringBuffer();
                compressDataPath.append("/").append(hold).append(nameSuffix).append("_small.").append(fileSuffix);
                // 添加数据库
                Attachment attachment = new Attachment();
                attachment.setPictureName(fileName);
                attachment.setPicturePath(originalDataPath.toString());
                attachment.setPictureType(file.getContentType());
                attachment.setPictureCreateDate(date);
                attachment.setPictureSuffix(new StringBuffer().append(".").append(fileSuffix).toString());
                attachment.setPictureSmallPath(compressDataPath.toString());
                attachment.setPictureWh(PenguinUtil.getImageWh(new File(mediaPath.toString() + "/" + fileName)));
                attachment.setPictureSize(PenguinUtil.parseSize(new File(mediaPath.toString() + "/" + fileName).length()));
                attachment.setUserId(userId);
                attachmentService.save(attachment);
                // 添加日志
                logService.save(new Log(LogConstant.UPLOAD_ATTACHMENT, LogConstant.UPLOAD_SUCCESS,
                        ServletUtil.getClientIP(request), DateUtil.date()));
                return Outcome.success(1, "上传成功", originalDataPath.toString());
            } catch (Exception e) {
                log.error("上传附件错误" + e.getMessage());
                logService.save(new Log(LogConstant.UPLOAD_ATTACHMENT, LogConstant.UPLOAD_ERROR + ":" + e.getMessage(),
                        ServletUtil.getClientIP(request), DateUtil.date()));
                return Outcome.failure("系统未知错误");
            }
        } else {
            return Outcome.failure("文件不能为空");
        }
    }

    @ApiOperation(value = "校验用户商家是否有交易密码", notes = "校验用户商家是否有交易密码")
    @GetMapping(value = "checkHas/transPwd")
    public Outcome checkHasTransPwd(HttpServletRequest request, @RequestHeader(value = "token") String token) {
        try {

            PenguinLogThreadLocal.setBaseInfo("校验用户是否有交易密码", token, null);

            if (StringUtils.isBlank(token)) {
                Outcome failure = Outcome.failure("参数为空");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            Outcome<PenguinsUser> userById = penguinsUserService.getUserById(userId);
            if (!userById.ok()) {
                PenguinLogThreadLocal.failure("数据库用户信息查询未空,  userId:" + userId);
                return Outcome.failure("未获取到用户信息");
            }
            if (Objects.isNull(userById.getData())) {
                PenguinLogThreadLocal.failure("数据库用户信息查询未空");
                return Outcome.failure("未获取到用户信息");
            }
            if (StringUtils.isBlank(userById.getData().getUserTransPwd())) {
                PenguinLogThreadLocal.failure("交易密码为空");
                return Outcome.failure("交易密码为空");
            }
            return Outcome.success();
        } finally {
            logService.saveLog(PenguinLogEventEnum.CHECK_USER_IS_PWD, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "校验用户商家交易密码是否正确", notes = "校验用户商家交易密码是否正确")
    @PostMapping(value = "check/transPwd")
    public Outcome checkTransPwd(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody ReqUserPasswordUpdateVO req) {
        try {
            PenguinLogThreadLocal.setBaseInfo("校验用户交易密码是否正确", token, null);
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            Outcome<PenguinsUser> userById = penguinsUserService.getUserById(userId);
            if (!userById.ok()) {
                PenguinLogThreadLocal.failure("数据库用户信息查询未空,  userId:" + userId);
                return Outcome.failure("未获取到用户信息");
            }
            if (Objects.isNull(userById.getData())) {
                PenguinLogThreadLocal.failure("数据库用户信息查询未空");
                return Outcome.failure("未获取到用户信息");
            }
            if (StringUtils.isBlank(userById.getData().getUserTransPwd())) {
                PenguinLogThreadLocal.failure("还未设置交易密码");
                return Outcome.failure("还未设置交易密码");
            }
            if (!Objects.equals(userById.getData().getUserTransPwd(), penguinsUserService.encryptPwd(req.getPassword(), userById.getData().getUserSec()))) {
                penguinsUserService.updatePwdError(userId);
                PenguinLogThreadLocal.failure("密码错误");
                return Outcome.failure("密码错误");
            }

            return Outcome.success("操作成功", "密码正确");
        } finally {
            logService.saveLog(PenguinLogEventEnum.CHECK_USER_IS_TRUE_PWD, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "用户商家编辑交易密码", notes = "用户商家编辑交易密码")
    @PostMapping(value = "edit/transPwd")
    public Outcome<String> editTransPwd(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody @ApiParam("交易密码表单") ReqPwdEdit req) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户编辑交易密码", token, req);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }
            Outcome<String> result = penguinsUserService.editPenguinUserPwd(userId, req);
            if (!result.ok()) {
                PenguinLogThreadLocal.failure(result.getMessage());
                return Outcome.failure(result.getMessage());
            }

            return Outcome.success(result);
        } finally {
            logService.saveLog(PenguinLogEventEnum.EDIT_USER_PWD, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @PostMapping(value = "user/message")
    public Outcome<String> sendMessage(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody ReqUser req) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户发送短信验证码", token, req);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }

            Outcome<String> result = penguinsUserService.sendMessage(req);
            if (!result.ok()) {
                PenguinLogThreadLocal.failure(result.getMessage());
                return Outcome.failure(result.getMessage());
            }
            return Outcome.success(result);
        } finally {
            logService.saveLog(PenguinLogEventEnum.USER_SEND_MESSAGE, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "校验发送短信验证码", notes = "校验发送短信验证码")
    @PostMapping(value = "check/message")
    public Outcome<String> checkMessage(HttpServletRequest request, @RequestHeader(value = "token") String token, @RequestBody ReqUser req) {
        try {

            PenguinLogThreadLocal.setBaseInfo("校验用户发送短信验证码", token, req);

            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                return Outcome.failure(outcome.getMessage());
            }
            String userId = JwtTokenUtil.getUserId(token);
            if (StringUtils.isBlank(userId)) {
                Outcome failure = Outcome.failure("token失效");
                PenguinLogThreadLocal.failure(failure.getMessage());
                return failure;
            }

            Outcome<String> result = penguinsUserService.checkSendMessage(req);
            if (!result.ok()) {
                PenguinLogThreadLocal.failure(result.getMessage());
                return Outcome.failure(result.getMessage());
            }
            return Outcome.success(result);
        } finally {
            logService.saveLog(PenguinLogEventEnum.CHECK_USER_SEND_MESSAGE, PenguinLogEventActionEnum.SELECT, request);
        }
    }

}
