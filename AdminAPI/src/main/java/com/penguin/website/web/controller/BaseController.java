package com.penguin.website.web.controller;

import com.penguin.website.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class BaseController {

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

    @Value("${penguin.url.vueHost}")
    protected String vueHost;

    @Value("${imageUrl}")
    protected String imageUrl;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected LogService logService;

    @Autowired
    protected PenguinsUserService penguinsUserService;

    @Autowired
    protected PenguinOrderService penguinOrderService;

    @Autowired
    protected AttachmentService attachmentService;

}
