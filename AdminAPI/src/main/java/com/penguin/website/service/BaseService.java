package com.penguin.website.service;

import com.penguin.website.mapper.penguin.PenguinsOrderMapper;
import com.penguin.website.mapper.penguin.PenguinsUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class BaseService {

    /** 日志 **/
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${penguin.url.userData}")
    protected String userUrl;

    @Value("${penguin.url.merchant}")
    protected String merchantUrl;

    @Value("${penguin.url.userTrade}")
    protected String userTradeUrl;

    @Value("${penguin.url.sendOtp}")
    protected String sendOtpUrl;

    @Value("${penguin.url.checkSendOtp}")
    protected String checkSendOtpUrl;

    @Value("${penguin.appId}")
    protected String appId;

    @Value("${penguin.appSecret}")
    protected String appSecret;

    @Autowired
    protected RestTemplate restTemplate;

    @Value("${penguin.url.userData}")
    protected String url;


    @Autowired
    protected PenguinsUserMapper penguinsUserMapper;


    @Autowired
    protected RestTemplateService restTemplateService;

    @Autowired
    protected PenguinsOrderMapper penguinsOrderMapper;

    @Autowired
    protected PenguinsUserService penguinsUserService;

}
