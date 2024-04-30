package com.penguin.website.service;

import com.penguin.website.bean.req.*;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.model.domain.PenguinsUser;

import java.util.List;

public interface PenguinsUserService {

    Outcome<List<RspUserPrice>> getMerchantList(String token);

    Outcome<List<RspUserPrice>> getRecoveryMerchantList(String token);

    Outcome<RspUserPrice> getMerchantInfo(ReqUser reqUser);

    Outcome<String> checkMerchantByUserId(String userId);

    Outcome<RspUser> getMerchantByUserId(String userId);

    Outcome<Boolean> updatePenguinUser(ReqUser req);

    Outcome<String> editPenguinUserPwd(String userId, ReqPwdEdit req);

    Outcome<RspGameUser> checkUser(String token);

    Outcome<Boolean> checkUserByToken(String token);

    Outcome<PenguinsUser> editUser(RspGameUser gameUser, String token);

    Outcome<PenguinsUser> getUserById(String userId);

    Outcome<String> getUserByAccessToken(String token);

    Outcome<Page<RspUserInfo>> getUserList(ReqUserPage param);

    Outcome<RspUserInfo> getUserInfo(String userId);

    Outcome<RspUserInfo> getUserInfoByAccount(String account);

    Outcome<String> editUserInfo(ReqUserExtend param);

    Outcome<PenguinsUser> upgradeUser(ReqUserExtend param);

    Outcome<String> sendMessage(ReqUser param);

    Outcome<String> checkSendMessage(ReqUser param);

    Outcome<String> checkMerchantPrice(String userId, ReqOrder param);

    String encryptPwd(String pwd, String sec);

    void updatePwdError(String userId);

    Outcome<RspGameUser> checkUserPost(String token);

    Outcome<PenguinsUser> checkLocalUser(String token);

    Outcome<String> checkUserRole(String userId);

    Outcome<PenguinsUser> checkUserStatus(String userId);

    Outcome<RspGameUser> checkMerchantRoleInfoAndGamInfo(String userId);

    Outcome<RspGameUser> checkMerchantPost(String userId);
}
