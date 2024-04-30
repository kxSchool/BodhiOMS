package com.penguin.website.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.penguin.website.annotation.NoVerificationToken;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.service.UserService;
import com.penguin.website.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * token拦截器
 */
@Component
public class SystemRequestTokenIntercept extends HandlerInterceptorAdapter {



    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!request.getRequestURI().startsWith("/api/system") || "OPTIONS".equals(request.getMethod())){
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

        // 校验token是否存在或已过期
        String token = JwtTokenUtil.getToken(request);
        boolean checkResult = JwtTokenUtil.isTokenExpired(token);
        if(!checkResult){
            resonseOut(response, Outcome.failure(("token不存在或已过期")));
            return false;
        }

        // 校验token中的用户是否存在
        if(!userService.checkUserNameIsExit(JwtTokenUtil.getUserName())){
            resonseOut(response, Outcome.failure(("token不存在或已过期")));
            return false;
        }

        return true;
    }

    /**
     * 回写给客户端
     * @param response
     * @param outcome
     * @throws IOException
     */
    private void resonseOut(HttpServletResponse response, Outcome outcome) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null ;
        String json = JSONObject.toJSON(outcome).toString();
        out = response.getWriter();
        out.append(json);
    }
}
