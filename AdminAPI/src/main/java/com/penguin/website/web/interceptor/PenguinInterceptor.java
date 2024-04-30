package com.penguin.website.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.penguin.website.annotation.NoVerificationToken;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.bean.rsp.RspGameUser;
import com.penguin.website.model.enums.PenguinLogEventActionEnum;
import com.penguin.website.model.enums.PenguinLogEventEnum;
import com.penguin.website.service.LogService;
import com.penguin.website.service.PenguinsUserService;
import com.penguin.website.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;


@Component
public class PenguinInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PenguinsUserService penguinsUserService;

    @Autowired
    private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        try {
            if (!request.getRequestURI().startsWith("/api/trans/") || "OPTIONS".equals(request.getMethod())) {
                return true;
            }

            // 获取方法上的注解
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if(method.isAnnotationPresent(NoVerificationToken.class)){
                return true;
            }
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("P3P", "CP=CAO PSA OUR");
            if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
                response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
                response.addHeader("Access-Control-Max-Age", "120");
            }

            String token = request.getHeader("token");
            if (StringUtils.isBlank(token)) {
                outMessage(response, "未获取到token");
                return false;
            }
            Outcome<Boolean> outcome = penguinsUserService.checkUserByToken(token);

            PenguinLogThreadLocal.setToken(token);
            PenguinLogThreadLocal.setApiInfo("校验用户交易token合法性及时效性");

            if (!outcome.ok()) {

                outMessage(response, outcome.getMessage());
                return false;
            }
            /*String userToken = JwtTokenUtil.getUserToken(token);
            Outcome<RspGameUser> outcome = penguinsUserService.checkUser(userToken);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
                outMessage(response, "token异常");
                return false;
            }*/
            PenguinLogThreadLocal.setToken(token);
            PenguinLogThreadLocal.setApiInfo("校验用户交易token合法性及时效性");
            Boolean tokenExpired = JwtTokenUtil.isTokenExpired(token);
            if (!tokenExpired) {
                outMessage(response, "token失效");
                return false;
            }
            return true;
        } finally {
            logService.saveLog(PenguinLogEventEnum.CHECK_TOKEN, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    private void outMessage(HttpServletResponse response, String message) throws IOException {
        PenguinLogThreadLocal.failure(message);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        String json = JSONObject.toJSON(Outcome.failure(message)).toString();
        out = response.getWriter();
        out.append(json);
        out.flush();
        out.close();
    }

}
