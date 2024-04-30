package com.penguin.website.util;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.penguin.website.bean.rsp.RspGameResult;
import com.penguin.website.bean.rsp.RspMessageResult;
import com.penguin.website.model.dto.External;
import com.penguin.website.model.dto.ExternalHttp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExternalDebugUtil {
    /**
     * description:
     */
    public static HashMap doPostExternal(String url, ExternalHttp externalHttp, RestTemplate restTemplate) {
        //设置请求信息
        HttpEntity requestEntity = ExternalDebugUtil.setRequestEntity(externalHttp);

        RspGameResult rspGameResult = new RspGameResult();
        JSONObject jsonObject = null;
        try {
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            try {
                String post = baseDoPost(url, requestEntity, restTemplate);
                jsonObject = JSONObject.parseObject(post);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                rspGameResult.setCode(0);
                rspGameResult.setMessage("getInterfaceError:" + sw.getBuffer().toString());
                jsonObject = (JSONObject) JSON.toJSON(rspGameResult);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            rspGameResult.setCode(0);
            rspGameResult.setMessage(sw.getBuffer().toString());
            jsonObject = (JSONObject) JSON.toJSON(rspGameResult);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("httpReq", requestEntity);
        hashMap.put("httpRsp", jsonObject);
        return hashMap;
    }

    public static RspGameResult doPost(String url, ExternalHttp externalHttp, RestTemplate restTemplate) {
        //设置请求信息
        HttpEntity requestEntity = ExternalDebugUtil.setRequestEntity(externalHttp);
        RspGameResult rspGameResult = new RspGameResult();
        try {
            String postMs=baseDoPost(url, requestEntity, restTemplate);
            rspGameResult=postMsToRspGameResult(postMs);
        } catch (Exception e) {
            rspGameResult.setCode(0);
            rspGameResult.setMessage(e.getMessage());
        }
        return rspGameResult;
    }

    /**
     * description: 接口返回信息转换
     */
    public static RspGameResult postMsToRspGameResult(String postMs){
        RspGameResult rspGameResult = null;
        JSONObject jsonObject = JSONObject.parseObject(postMs);
        rspGameResult = JSON.toJavaObject(jsonObject, RspGameResult.class);
        return rspGameResult;
    }

    /**
     * description: 获取接口信息
     */
    public static String baseDoPost(String url, HttpEntity requestEntity, RestTemplate restTemplate) throws Exception {
        //设置请求信息
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String post = restTemplate.postForObject(url, requestEntity, String.class);
        return post;
    }


    public static HttpEntity setRequestEntity(ExternalHttp externalHttp) {
        HttpEntity requestEntity = null;
        long time = new Date().getTime() / 1000;

        String result = ExternalDebugUtil.getHttpMd5(ExternalDebugUtil.getOrignalHttpString(time, externalHttp));

        //设置请求header信息
        HttpHeaders headers = setHttpHeaders(result, time, externalHttp);
        //封装请求头和内容
        if (StringUtils.isBlank(externalHttp.getParam())) {
            requestEntity = new HttpEntity<>(null, headers);
        } else {
            requestEntity = new HttpEntity<>(externalHttp.getParam(), headers);
        }
        return requestEntity;
    }

    public static HttpHeaders setHttpHeaders(String result, long time, ExternalHttp externalHttp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "Bearer " + externalHttp.getToken());
        headers.set("x-auth-timestamp", time + "");
        headers.set("x-auth-appid", externalHttp.getAppId());
        headers.set("x-auth-sign", result);
        return headers;
    }

    public static StringBuffer getOrignalHttpString(long time, ExternalHttp externalHttp) {
        StringBuffer bf = new StringBuffer(externalHttp.getToken()).append("|").append(externalHttp.getAppId()).append("|").append(externalHttp.getAppSecret()).append("|");
        bf.append(time).append("|");
        if (StringUtils.isNotBlank(externalHttp.getParam())) {
            bf.append(externalHttp.getParam());
        }
        return bf;
    }

    public static String getHttpMd5(StringBuffer bf) {
        String encoder = Base64.getEncoder().encodeToString(bf.toString().getBytes());
        String result = SecureUtil.md5(encoder);
        return result;
    }

    public static HashMap sendOtp(String url, String phone, RestTemplate restTemplate) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", phone);
        String json = JSON.toJSONString(params);
        return doPost(url, json, restTemplate);
    }

    public static HashMap checkSendOtp(String url, String phone, String code, RestTemplate restTemplate) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", phone);
        params.put("otpCode", code);
        return doPost(url, JSON.toJSONString(params), restTemplate);
    }

    public static HashMap doPost(String url, String params, RestTemplate restTemplate) {

        //设置请求header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity requestEntity = new HttpEntity<>(params, headers);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        String post = restTemplate.postForObject(url, requestEntity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(post);
        HashMap hashMap = new HashMap();
        hashMap.put("httpReq", requestEntity);
        hashMap.put("httpRsp", jsonObject);

        return hashMap;
    }

    public static HashMap operateBalance(String url, ExternalHttp externalHttp, External.ExternalOrder externalOrder, int number, RestTemplate restTemplate) {
        Map<String, Object> params = new HashMap<>();
        params.put("order_no", externalOrder.getOrder_no());
        params.put("saro_quantity", externalOrder.getSaro_quantity().multiply(new BigDecimal(number)).setScale(0));
        return doPostExternal(url, externalHttp, restTemplate);
    }

}