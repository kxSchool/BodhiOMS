package com.penguin.website.web.controller.penguin;

import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.REQPenguinsOrder;
import com.penguin.website.bean.req.ReqOperateOrder;
import com.penguin.website.bean.req.ReqUserExtend;
import com.penguin.website.bean.req.ReqUserPage;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.dto.SystemUserDto;
import com.penguin.website.model.enums.PenguinLogEventActionEnum;
import com.penguin.website.model.enums.PenguinLogEventEnum;
import com.penguin.website.service.LogService;
import com.penguin.website.service.PenguinOrderService;
import com.penguin.website.service.PenguinsUserService;
import com.penguin.website.util.PenguinUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Api(tags = "商家PC后台", value = "商家PC后台")
@RestController
@RequestMapping(value = "/api/system/")
public class SystemController {

    @Autowired
    private PenguinOrderService penguinOrderService;

    @Autowired
    private PenguinsUserService penguinsUserService;

    @Autowired
    private LogService logService;

    @ApiOperation(value = "获取交易数据", notes = "获取交易数据")
    @GetMapping(value = "transReport")
    public Outcome<RspTransReport> getTransReport(@RequestHeader("token") String token, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取交易数据", token, null);
            return PenguinUtil.setErrorLog(penguinOrderService.getTransReport());
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_TRANS_DATA, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "获取订单列表数据", notes = "获取订单列表数据")
    @PostMapping(value = "getOrderList")
    public Outcome<Page<RspOrderExtend>> getOrderList(@RequestHeader("token") String token,
                                                      @RequestBody @ApiParam("pc获取订单数据") REQPenguinsOrder req, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取订单列表数据", token, req);
            return PenguinUtil.setErrorLog(penguinOrderService.getOrderList(req));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_PC_ORDER_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "获取用户列表数据", notes = "获取用户列表数据")
    @PostMapping(value = "getUserList")
    public Outcome<Page<RspUserInfo>> getUserList(@RequestHeader("token") String token, @RequestBody ReqUserPage param, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("获取用户列表数据", token, param);
            return PenguinUtil.setErrorLog(penguinsUserService.getUserList(param));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_GET_MERCHANT_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "根据id获取用户信息", notes = "根据id获取用户信息")
    @GetMapping(value = "getUserList/{userId}")
    public Outcome<RspUserInfo> getUserInfo(@RequestHeader("token") String token, @PathVariable String userId, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("根据id获取用户信息", token, userId);
            return PenguinUtil.setErrorLog(penguinsUserService.getUserInfo(userId));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_GET_MERCHANT_INFO, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "根据id判断用户是否可以升级商户", notes = "根据id判断用户是否可以升级商户")
    @PostMapping(value = "checkUserRole")
    public Outcome<String> checkUserRole(@RequestHeader("token") String token, @RequestBody SystemUserDto.UserId userId, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("根据id判断用户是否可以升级商户", token, userId.getUserId());
            return PenguinUtil.setErrorLog(penguinsUserService.checkUserRole(userId.getUserId()));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_GET_MERCHANT_INFO, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    @ApiOperation(value = "升级用户", notes = "升级用户")
    @PostMapping(value = "upgradeUser")
    public Outcome<PenguinsUser> upgradeUser(@RequestHeader("token") String token, @RequestBody ReqUserExtend param, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("升级用户", token, param);
            return PenguinUtil.setErrorLog(penguinsUserService.upgradeUser(param));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_EDIT_USER, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "编辑用户信息", notes = "编辑用户信息")
    @PostMapping(value = "editUserList")
    public Outcome<String> editUserList(@RequestHeader("token") String token, @RequestBody ReqUserExtend param, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("编辑用户信息", token, param);
            return PenguinUtil.setErrorLog(penguinsUserService.editUserInfo(param));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_EDIT_USER, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "根据账号获取用户信息", notes = "根据账号获取用户信息")
    @GetMapping(value = "getUserByAccount/{account}")
    public Outcome<RspUserInfo> getUserByAccount(@RequestHeader("token") String token, @PathVariable String account, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("根据账号获取用户信息", token, account);
            return PenguinUtil.setErrorLog(penguinsUserService.getUserInfoByAccount(account));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_GET_MERCHANT_INFO, PenguinLogEventActionEnum.EDIT, request);
        }
    }

    @ApiOperation(value = "用户编辑订单", notes = "用户编辑订单")
    @PutMapping(value = "editOrder")
    public Outcome<String> editOrder(@RequestHeader("token") String token, @RequestBody ReqOperateOrder param, HttpServletRequest request) {
        try {
            PenguinLogThreadLocal.setBaseInfo("用户编辑订单", token, param);
            return PenguinUtil.setErrorLog(penguinOrderService.editOrderBySystem(param));
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_EDIT_ORDER, PenguinLogEventActionEnum.EDIT, request);
        }
    }

}
