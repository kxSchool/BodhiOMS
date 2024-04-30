package com.penguin.website.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.penguin.website.bean.rsp.RspGameResult;
import com.penguin.website.bean.rsp.RspMessageResult;
import com.penguin.website.service.RestTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class RestTemplateServiceImpl implements RestTemplateService {
    @Value("${penguin.appId}")
    private String appId;

    @Value("${penguin.appSecret}")
    private String appSecret;

    @Autowired
    private RestTemplate restTemplate;

    public RspGameResult doPost(String url, String token, String param) {
        HttpEntity requestEntity = null;
        long time = new Date().getTime() / 1000;
        StringBuffer bf = new StringBuffer(token).append("|").append(appId).append("|").append(appSecret).append("|");
        bf.append(time).append("|");
        if (StringUtils.isNotBlank(param)) {
            bf.append(param);
        }
        String encoder = Base64.getEncoder().encodeToString(bf.toString().getBytes());
        String result = SecureUtil.md5(encoder);

        //设置请求header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "Bearer " + token);
        headers.set("x-auth-timestamp", time + "");
        headers.set("x-auth-appid", appId);
        headers.set("x-auth-sign", result);
        //封装请求头和内容
        if (StringUtils.isBlank(param)) {
            requestEntity = new HttpEntity<>(null, headers);
        } else {
            requestEntity = new HttpEntity<>(param, headers);
        }
        /*System.out.println(bf);
        System.out.println(result);
        System.out.println(headers);*/
        RspGameResult rspGameResult = new RspGameResult();
        try {
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String post = restTemplate.postForObject(url, requestEntity, String.class);
            System.out.println("doPost");
            System.out.println(post);
            JSONObject jsonObject = JSONObject.parseObject(post);
            rspGameResult = JSON.toJavaObject(jsonObject, RspGameResult.class);

        } catch (Exception e) {
            rspGameResult.setCode(0);
            rspGameResult.setMessage(e.getMessage());
        }
        return rspGameResult;
    }

    @Override
    public RspMessageResult doPost(String url, String param) {
        //设置请求header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity requestEntity = new HttpEntity<>(param, headers);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        String post = restTemplate.postForObject(url, requestEntity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(post);
        RspMessageResult rspGameResult = JSON.toJavaObject(jsonObject, RspMessageResult.class);
        return rspGameResult;
    }
}
