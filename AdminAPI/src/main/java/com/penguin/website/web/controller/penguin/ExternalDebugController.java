package com.penguin.website.web.controller.penguin;

import com.alibaba.fastjson.JSON;

import com.penguin.website.model.dto.External;
import com.penguin.website.model.dto.ExternalHttp;
import com.penguin.website.util.ExternalDebugUtil;
import com.penguin.website.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@Api(tags = "外部接口调试", value = "外部接口调试")
@RestController
@RequestMapping(value = "/external/debug/")
public class ExternalDebugController extends BaseController {

    @ApiOperation(value = "游戏用户-获取", notes = "获取游戏用户信息")
    @GetMapping(value = "user_data")
    @ResponseBody
    public Object getUserToken(@RequestParam String token) {
        ExternalHttp externalHttp = new ExternalHttp(token, appId, appSecret, null);
        return ExternalDebugUtil.doPostExternal(userUrl, externalHttp, restTemplate);
    }

    @ApiOperation(value = "商户信息", notes = "获取商户信息")
    @GetMapping(value = "merchants")
    @ResponseBody
    public Object getMerchants(@RequestParam String token) {
        ExternalHttp externalHttp = new ExternalHttp(token, appId, appSecret, null);
        return ExternalDebugUtil.doPostExternal(merchantUrl, externalHttp, restTemplate);
    }

    @ApiOperation(value = "订单-提交", notes = "订单调试接口")
    @PostMapping(value = "userTrade")
    @ResponseBody
    public Object userTrade( @RequestBody External.ExternalOrder externalOrder) {
        ExternalHttp externalHttp = new ExternalHttp(externalOrder.getUser_token(), appId, appSecret, JSON.toJSONString(externalOrder));
        return ExternalDebugUtil.operateBalance(userTradeUrl, externalHttp, externalOrder, -10000, restTemplate);
    }

    @ApiOperation(value = "用户、商户、订单接口验证", notes = "订单调试接口")
    @PostMapping(value = "interface3")
    @ResponseBody
    public Object interface3(@RequestBody External.ExternalOrder externalOrder) {
        HashMap hashMap = new HashMap();
        hashMap.put("user_data", getUserToken(externalOrder.getUser_token()));
        hashMap.put("merchants", getMerchants(externalOrder.getUser_token()));
        hashMap.put("userTrade", userTrade(externalOrder));
        return hashMap;
    }

    @ApiOperation(value = "短信--获取", notes = "获取短信")
    @PostMapping(value = "sendOtp")
    @ResponseBody
    public Object getOtp(@RequestBody External.Phone phone) {
        return ExternalDebugUtil.sendOtp(sendOtpUrl, phone.getPhone(), restTemplate);
    }

    @ApiOperation(value = "短信--校验", notes = "校验短信")
    @PostMapping(value = "checkSendOtp")
    @ResponseBody
    public Object checkSendOtp(@RequestBody External.PhoneCode phoneCode) {
        return ExternalDebugUtil.checkSendOtp(checkSendOtpUrl, phoneCode.getPhone(), phoneCode.getCode(), restTemplate);
    }

}