package com.penguin.website.service;

import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.bean.rsp.RspGameResult;
import com.penguin.website.bean.rsp.RspMessageResult;

public interface RestTemplateService {
    RspGameResult doPost(String url, String token, String param);

    RspMessageResult doPost(String url, String param);
}
