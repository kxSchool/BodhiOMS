package com.penguin.website.util;


import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.bean.rsp.RspGameResult;
import com.penguin.website.bean.rsp.RspGameUser;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.domain.User;
import com.penguin.website.model.dto.ExternalHttp;
import com.penguin.website.model.enums.UserRoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * description:
 */
public class CheckTokenUtils {

    /**
     * description: 检查本地token合法性
     */
    public static Outcome<PenguinsUser> checklocalToken(String token) {
        if (StringUtils.isBlank(token)) {
            return Outcome.failure("未获取到token");
        }
        Boolean tokenExpired = JwtTokenUtil.isTokenExpired(token);
        if (!tokenExpired) {
            return Outcome.failure("token失效");
        }

        String gameToken = JwtTokenUtil.getUserToken(token);
        if (StringUtils.isBlank(gameToken)) {
            return Outcome.failure("游戏token为空");
        }

        String userId=JwtTokenUtil.getUserId(token);
        if (StringUtils.isBlank(userId)) {
            return Outcome.failure("token失效");
        }

        PenguinsUser user=new PenguinsUser();
        user.setUserId(userId);
        user.setUserToken(gameToken);
        return  Outcome.success("",user);
    }

    /**
     * description:
     */
    public static Outcome<RspGameUser> checkExternalToken(String url, ExternalHttp externalHttp, RestTemplate restTemplate) {
        RspGameResult rspGameResult = ExternalDebugUtil.doPost(url, externalHttp, restTemplate);
        Outcome<RspGameUser> outcome = PenguinUtil.rspResultToRspGameUser(rspGameResult);
        if (!outcome.ok()) {
            return outcome;
        }
        RspGameUser gameUser = (RspGameUser) outcome.getData();
        PenguinLogThreadLocal.appendLog("tokenUser", gameUser);
        PenguinLogThreadLocal.setUserId(gameUser.getUser_id());
        return Outcome.success("",gameUser);
    }


    public static Outcome<RspGameUser> checkMerchantExternalTokenAndResp(String url,String userToken, String appId,String appSecret, RestTemplate restTemplate){
        ExternalHttp externalHttp = new ExternalHttp(userToken, appId, appSecret, null);
        Outcome<RspGameUser> outcome = CheckTokenUtils.checkExternalToken(url, externalHttp, restTemplate);
        if(!outcome.ok()){
            return Outcome.failure(outcome.getMessage());
        }
        RspGameUser rspMerchant=outcome.getData();
        //检验商家基本信息合法性
        outcome=PenguinUtil.checkRspGameUser(rspMerchant);
        if(!outcome.ok()){
            return Outcome.failure(outcome.getMessage());
        }
        //检查商家是否为商家
        if(!rspMerchant.getUser_type().equals(UserRoleEnum.GAME_MERCHANT.getValue())){
            return Outcome.failure("商家账号异常");
        }
        return Outcome.success("",outcome.getData());
    }


}