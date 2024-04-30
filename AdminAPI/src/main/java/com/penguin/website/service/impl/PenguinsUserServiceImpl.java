package com.penguin.website.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.*;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.dto.ExternalHttp;
import com.penguin.website.model.enums.OrderTypeEnum;
import com.penguin.website.model.enums.StatusEnum;
import com.penguin.website.model.enums.UserRoleEnum;
import com.penguin.website.service.BaseService;
import com.penguin.website.service.PenguinsUserService;
import com.penguin.website.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PenguinsUserServiceImpl extends BaseService implements PenguinsUserService {


    @Override
    public Outcome<List<RspUserPrice>> getMerchantList(String token) {

        RspGameResult rspGameResult = restTemplateService.doPost(merchantUrl, token, null);
        PenguinLogThreadLocal.appendLog("request", rspGameResult);
        if (rspGameResult.getCode() != 1) {
            return Outcome.failure("商家信息为空");
        }
        String data = rspGameResult.getData();
        if (StringUtils.isBlank(data)) {
            return Outcome.failure("暂无商户信息");
        }
        List<JSONObject> merchantList = new ArrayList<>();

        List<RspGameMerchant> rspGameMerchants = JSON.parseArray(data, RspGameMerchant.class);

        List<RspUserPrice> result = new ArrayList<>();
        for (RspGameMerchant merchant : rspGameMerchants) {
            JSONObject merchantJson = new JSONObject();
            RspGameResult rspMerchant = restTemplateService.doPost(url, merchant.getToken(), null);
            merchantJson.put(merchant.getToken(), rspMerchant);
            merchantList.add(merchantJson);
            if (rspMerchant.getCode() != 1) {
                continue;
            }
            //
            if (StringUtils.isBlank(rspMerchant.getData())) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(rspMerchant.getData());
            RspGameUser gameUser = JSON.toJavaObject(jsonObject, RspGameUser.class);
            if (Objects.isNull(gameUser.getUser_real_name_state()) || !Objects.equals(1, gameUser.getUser_real_name_state())) {
                log.error("getMerchantList:user_real_name_state is not 1, userId = " + gameUser.getUser_id());
                continue;
            }

            if (StringUtils.isBlank(gameUser.getUser_real_name())) {
                log.error("getMerchantList:user_real_name_state is null, userId = " + gameUser.getUser_id());
                continue;
            }
            if (Objects.isNull(gameUser.getSaro_quantity())) {
                continue;
            }

            BigDecimal userSaroNumber = gameUser.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);

            //update game merchant
            this.updateGameUser(merchant.getToken(), gameUser, userSaroNumber, null, 2);

            PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(gameUser.getUser_id());

            //校验商户合法性
            Outcome outcome = PenguinUtil.checkPenguinsMerchantMustPro(user);
            if (!outcome.ok()) {
                log.error("getMerchantList:" + outcome.getMessage());
                continue;
            }

            BigDecimal divide1 = user.getMinRecharge().divide(user.getRechargeUnitPrice(), 4, BigDecimal.ROUND_DOWN);

            if (userSaroNumber.compareTo(divide1) <= 0) {
                log.error("getMerchantList:user number is 0,userId = " + user.getUserId());
                continue;
            }
            RspUserPrice rsp = new RspUserPrice();
            BigDecimal rechargeUnitPrice = user.getRechargeUnitPrice();
            BigDecimal recoveryUnitPrice = user.getRecoveryUnitPrice();
            rsp.setUserId(user.getUserId());
            rsp.setUserName(user.getUserName());
            rsp.setNickname(user.getNickname());
            rsp.setSaroNumber(userSaroNumber);
            rsp.setRechargeUnitPrice(rechargeUnitPrice);
            rsp.setRecoveryUnitPrice(recoveryUnitPrice);
            DecimalFormat numFormat = new DecimalFormat("##,##0.00");
            if (Objects.nonNull(userSaroNumber) && Objects.nonNull(rechargeUnitPrice)) {
                if (userSaroNumber.compareTo(new BigDecimal(30)) > 0) {
                    userSaroNumber = new BigDecimal(30);
                }
                BigDecimal divide = userSaroNumber.multiply(rechargeUnitPrice);
                rsp.setMaxRecharge(numFormat.format(divide));
            }
            if (Objects.nonNull(user.getMinRecharge())) {
                rsp.setMinRecharge(numFormat.format(user.getMinRecharge()));
            }
            result.add(rsp);

        }
        //order by
        if (result.size() > 0) {
            result = result.stream().sorted(Comparator.comparing(RspUserPrice::getRechargeUnitPrice)).collect(Collectors.toList());//.thenComparing(RSPUserPrice::getRechargeUnitPrice)).collect(Collectors.toList());
        }
        PenguinLogThreadLocal.appendLog("merchantList", merchantList);
        return Outcome.success(result);
    }

    @Override
    public Outcome<List<RspUserPrice>> getRecoveryMerchantList(String token) {

        RspGameResult rspGameResult = restTemplateService.doPost(merchantUrl, token, null);
        PenguinLogThreadLocal.appendLog("request", rspGameResult);
        if (rspGameResult.getCode() != 1) {
            return Outcome.failure("获取回收商家信息异常");
        }
        String data = rspGameResult.getData();
        if (StringUtils.isBlank(data)) {
            return Outcome.failure("暂无回收商户信息");
        }
        List<RspGameMerchant> rspGameMerchants = JSON.parseArray(data, RspGameMerchant.class);
        List<JSONObject> recoveryMerchant = new ArrayList<>();
        List<RspUserPrice> result = new ArrayList<>();
        for (RspGameMerchant merchant : rspGameMerchants) {
            JSONObject object = new JSONObject();
            RspGameResult rspMerchant = restTemplateService.doPost(url, merchant.getToken(), null);
            object.put(merchant.getToken(), recoveryMerchant);
            recoveryMerchant.add(object);
            if (rspMerchant.getCode() != 1) {
                continue;
            }
            if (StringUtils.isBlank(rspMerchant.getData())) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(rspMerchant.getData());
            RspGameUser gameUser = JSON.toJavaObject(jsonObject, RspGameUser.class);
            if (Objects.isNull(gameUser.getUser_real_name_state()) || !Objects.equals(1, gameUser.getUser_real_name_state())) {
                log.error("getRecoveryMerchantList:user_real_name_state is not 1, userId = " + gameUser.getUser_id());
                continue;
            }

            if (StringUtils.isBlank(gameUser.getUser_real_name())) {
                log.error("getRecoveryMerchantList:user_real_name_state is null, userId = " + gameUser.getUser_id());
                continue;
            }
            PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(gameUser.getUser_id());

            Outcome outcome = PenguinUtil.checkPenguinsMerchantMustPro(user);
            if (!outcome.ok()) {
                log.error("getRecoveryMerchantList:" + outcome.getMessage());
                continue;
            }
            //update game merchant
            BigDecimal userSaroNumber = gameUser.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
            this.updateGameUser(merchant.getToken(), gameUser, userSaroNumber, null, 2);
            RspUserPrice rsp = new RspUserPrice();
            rsp.setUserId(user.getUserId());
            rsp.setUserName(user.getUserName());
            rsp.setNickname(user.getNickname());
            rsp.setRecoveryUnitPrice(user.getRecoveryUnitPrice());
            result.add(rsp);
        }
        //order by
        if (result.size() > 0) {
            result = result.stream().sorted(Comparator.comparing(RspUserPrice::getRecoveryUnitPrice).reversed()).collect(Collectors.toList());//.thenComparing(RSPUserPrice::getRechargeUnitPrice)).collect(Collectors.toList());
        }
        PenguinLogThreadLocal.appendLog("recoveryMerchant", recoveryMerchant);
        return Outcome.success(result);
    }

    @Override
    public Outcome<RspUserPrice> getMerchantInfo(ReqUser reqUser) {
        PenguinLogThreadLocal.appendLog("request", reqUser);
        PenguinsUser merchant = penguinsUserMapper.selectByPrimaryKey(reqUser.getUserId());
        if (Objects.isNull(merchant)) {
            return Outcome.failure("用户信息为空");
        }
        if (Objects.isNull(merchant.getMinRecharge()) || Objects.isNull(merchant.getRechargeUnitPrice())) {
            System.out.println("user MinRecharge or RechargeUnitPrice is null,userId = " + merchant.getUserId());
            return Outcome.failure("商家未设置最低限额或单价");
        }
        if (StringUtils.isBlank(merchant.getUserToken())) {
            return Outcome.failure("商家token为空");
        }
        if (!Objects.equals(merchant.getUserStatus(), StatusEnum.NORMAL.getValue())) {
            return Outcome.failure("商家未处于启用状态");
        }

        RspGameResult rspMerchant = restTemplateService.doPost(url, merchant.getUserToken(), null);
        if (rspMerchant.getCode() != 1) {
            return Outcome.failure("未获取到该商家信息");
        }
        if (StringUtils.isBlank(rspMerchant.getData())) {
            return Outcome.failure("未获取到该商家信息");
        }
        JSONObject jsonObject = JSONObject.parseObject(rspMerchant.getData());
        RspGameUser gameUser = JSON.toJavaObject(jsonObject, RspGameUser.class);
        System.out.println("merchant user：：：" + gameUser);
        if (Objects.isNull(gameUser.getUser_real_name_state()) || !Objects.equals(1, gameUser.getUser_real_name_state())) {
            System.out.println("user_real_name_state is not 1, userId = " + gameUser.getUser_id());
            return Outcome.failure("商家实名为空");
        }

        if (StringUtils.isBlank(gameUser.getUser_real_name())) {
            System.out.println("user_real_name_state is null, userId = " + gameUser.getUser_id());
            return Outcome.failure("商家实名未认证");
        }
        RspUserPrice rsp = new RspUserPrice();
        //update game merchant
        BigDecimal userSaroNumber = gameUser.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
        this.updateGameUser(merchant.getUserToken(), gameUser, userSaroNumber, null, 2);


        BigDecimal divide1 = merchant.getMinRecharge().divide(merchant.getRechargeUnitPrice(), 4, BigDecimal.ROUND_DOWN);

//        if (userSaroNumber.compareTo(divide1) <= 0) {
//            return Outcome.failure("商家余额不足");
//        }

        BigDecimal rechargeUnitPrice = merchant.getRechargeUnitPrice();
        BigDecimal recoveryUnitPrice = merchant.getRecoveryUnitPrice();
        rsp.setUserId(merchant.getUserId());
        rsp.setUserName(merchant.getUserName());
        rsp.setNickname(merchant.getNickname());
        rsp.setSaroNumber(userSaroNumber);
        rsp.setRechargeUnitPrice(rechargeUnitPrice);
        rsp.setRecoveryUnitPrice(recoveryUnitPrice);
        DecimalFormat numFormat = new DecimalFormat("##,##0.00");
        if (Objects.nonNull(userSaroNumber) && Objects.nonNull(rechargeUnitPrice)) {
            if (userSaroNumber.compareTo(new BigDecimal(30)) > 0) {
                userSaroNumber = new BigDecimal(30);
            }
            BigDecimal divide = userSaroNumber.multiply(rechargeUnitPrice);
            rsp.setMaxRecharge(numFormat.format(divide));
        }
        if (Objects.nonNull(merchant.getMinRecharge())) {
            rsp.setMinRecharge(numFormat.format(merchant.getMinRecharge()));
        }
        PenguinLogThreadLocal.appendLog("merchantInfo", rsp);

        if (userSaroNumber.compareTo(divide1) <= 0) {
            return Outcome.failure("商家余额不足",rsp);
        }

        return Outcome.success(rsp);
    }

    @Override
    public Outcome<String> checkMerchantByUserId(String userId) {
        PenguinLogThreadLocal.appendLog("request", userId);
        PenguinsUser penguinsUser = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(penguinsUser)) {
            return Outcome.failure("未获取到对应用户信息");
        }
        if (StringUtils.isBlank(penguinsUser.getUserPhone())) {
            return Outcome.failure("用户未填写手机信息");
        }
        if (StringUtils.isBlank(penguinsUser.getUserTransPwd())) {
            return Outcome.failure("用户未设置交易密码");
        }
        if (StringUtils.isBlank(penguinsUser.getUserBankCard()) || StringUtils.isBlank(penguinsUser.getUserBank())
                || StringUtils.isBlank(penguinsUser.getBankFavour())) {
            return Outcome.failure("用户未填写银行卡信息");
        }
        return Outcome.success("校验通过");
    }

    @Override
    public Outcome<RspUser> getMerchantByUserId(String userId) {
        PenguinLogThreadLocal.appendLog("request", userId);
        RspUser result = new RspUser();
        PenguinsUser penguinsUser = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(penguinsUser)) {
            return Outcome.failure("未获取到信息");
        }
        result.setUserId(penguinsUser.getUserId());
        result.setUserName(penguinsUser.getUserName());
        result.setBankFavour(penguinsUser.getBankFavour());
        result.setUserBank(penguinsUser.getUserBank());
        result.setUserPhone(penguinsUser.getUserPhone());
        result.setUserBankCard(penguinsUser.getUserBankCard());
        result.setUserBankBranch(penguinsUser.getUserBankBranch());
        result.setUserPhone2(penguinsUser.getUserPhone2());
        result.setUserPhone3(penguinsUser.getUserPhone3());
        result.setRechargeUnitPrice(penguinsUser.getRechargeUnitPrice());
        result.setRecoveryUnitPrice(penguinsUser.getRecoveryUnitPrice());

        PenguinLogThreadLocal.appendLog("result", result);
        return Outcome.success(result);
    }

    @Override
    public Outcome<Boolean> updatePenguinUser(ReqUser req) {
        PenguinLogThreadLocal.appendLog("request", req);

        if (req.getRechargeUnitPrice() == null || req.getRecoveryUnitPrice() == null) {
            return Outcome.failure("请补全充值和卖出单价");
        }

        PenguinsUser user = new PenguinsUser();
        user.setUserId(req.getUserId());
        PenguinsUser userKey = penguinsUserMapper.selectByPrimaryKey(req.getUserId());
        if (Objects.isNull(userKey)) {
            return Outcome.failure("用户信息为空");
        }
        //user.setBankFavour(req.getBankFavour());
        if (Objects.equals(userKey.getUserRole(), UserRoleEnum.GAME_USER.getValue())) {
            if (StringUtils.isNotBlank(req.getUserBank())) {
                if (StringUtils.isBlank(req.getUserBankCard())) {
                    return Outcome.failure("银行卡号为空");
                }
                if (StringUtils.isBlank(req.getUserBankBranch())) {
                    return Outcome.failure("开户支行为空");
                }
            }
            user.setUserBank(req.getUserBank());
            user.setUserBankCard(req.getUserBankCard());
            user.setUserBankBranch(req.getUserBankBranch());
        }
        if (req.getRechargeUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            user.setRechargeUnitPrice(req.getRechargeUnitPrice());
        }
        if (req.getRecoveryUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            user.setRecoveryUnitPrice(req.getRecoveryUnitPrice());
        }
        user.setUserPhone(req.getUserPhone());
        user.setUserPhone2(req.getUserPhone2());
        user.setUserPhone3(req.getUserPhone3());
        user.setUserTransPwd(req.getUserTransPwd());
        user.setUpdateTime(new Date());
        int result = penguinsUserMapper.updateByPrimaryKeySelective(user);
        if (result <= 0) {
            return Outcome.failure("更新失败");
        }
        PenguinLogThreadLocal.appendLog("result", "更新成功");
        return Outcome.success("更新成功");
    }

    @Override
    public Outcome<String> editPenguinUserPwd(String userId, ReqPwdEdit req) {
        if (Objects.isNull(req) || Objects.isNull(req.getConfirmPwd())
                || Objects.isNull(req.getNewPwd()) || Objects.isNull(req.getType())) {
            return Outcome.failure("参数为空");
        }
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return Outcome.failure("未获取到用户信息");
        }
        if (!Objects.equals(req.getNewPwd(), req.getConfirmPwd())) {
            return Outcome.failure("两次密码不一致");
        }
        if (Objects.equals(req.getNewPwd(), req.getFormerlyPwd())) {
            return Outcome.failure("与旧密码一致，无需修改");
        }
        if (Objects.equals(req.getType(), 2)) {
            //update
            if (Objects.isNull(req.getFormerlyPwd())) {
                return Outcome.failure("未输入原始密码");
            }
            //String formerlyPwd = SecureUtil.md5(req.getFormerlyPwd());
            String formerlyPwd = this.encryptPwd(req.getFormerlyPwd(), user.getUserSec());
            if (!Objects.equals(formerlyPwd, user.getUserTransPwd())) {
                this.updatePwdError(userId);
                return Outcome.failure("原密码输入有误");
            }
        }
        String random = RandomStringUtils.random(16, true, true);
        PenguinsUser updateParam = new PenguinsUser();
        updateParam.setUserId(userId);
        updateParam.setUpdateTime(new Date());
        updateParam.setUserTransPwd(this.encryptPwd(req.getNewPwd(), random));
        updateParam.setUserSec(random);
        int result = penguinsUserMapper.updateByPrimaryKeySelective(updateParam);
        if (result > 0) {
            return Outcome.success("资金密码编辑成功");
        }
        return Outcome.failure("资金密码编辑失败");
    }

    @Override
    public Outcome<RspGameUser> checkUserPost(String token) {
        ExternalHttp externalHttp = new ExternalHttp(token, appId, appSecret, null);
        Outcome<RspGameUser> outcome = CheckTokenUtils.checkExternalToken(url, externalHttp, restTemplate);
        if (!outcome.ok()) {
            PenguinLogThreadLocal.setErrMsg(outcome.getMessage());
            return outcome;
        }
        return PenguinUtil.checkRspGameUser(outcome.getData());
    }

    @Override
    public Outcome<RspGameUser> checkMerchantRoleInfoAndGamInfo(String userId) {
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        return PenguinUtil.checkMerchantRoleInfoAndGamInfo(user, url, appId, appSecret, restTemplate);
    }

    @Override
    public Outcome<RspGameUser> checkMerchantPost(String userId) {
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        //检查商户合法性
        Outcome<RspGameUser> stringOutcome = PenguinUtil.checkMerchantRoleInfoAndGamInfo(user, url, appId, appSecret, restTemplate);
        if (!stringOutcome.ok()) {
            return Outcome.failure(stringOutcome.getMessage());
        }
        return stringOutcome;
    }

    @Override
    public Outcome<PenguinsUser> checkLocalUser(String token) {
        return CheckTokenUtils.checklocalToken(token);
    }

    @Override
    public Outcome<RspGameUser> checkUser(String token) {
        //通过token获取用户信息
        RspGameResult rspGameResult = restTemplateService.doPost(url, token, null);
        if (rspGameResult.getCode() != 1) {
            System.out.println("jsonObject======");
            System.out.println(rspGameResult.toString());
            return Outcome.failure(rspGameResult.getMessage());
        }
        JSONObject jsonObject = JSONObject.parseObject(rspGameResult.getData());

        RspGameUser gameUser = JSON.toJavaObject(jsonObject, RspGameUser.class);
        PenguinLogThreadLocal.appendLog("tokenUser", gameUser);
        PenguinLogThreadLocal.setUserId(gameUser.getUser_id());
        if (StringUtils.isBlank(gameUser.getUser_real_name())) {
            return Outcome.failure("用户未实名");
        }
        if (Objects.isNull(gameUser.getUser_real_name_state()) || !Objects.equals(1, gameUser.getUser_real_name_state())) {
            return Outcome.failure("用户未完成实名认证");
        }

        return Outcome.success(gameUser);
    }

    @Override
    public Outcome<Boolean> checkUserByToken(String token) {

        Outcome<PenguinsUser> penguinsUserOutcome = CheckTokenUtils.checklocalToken(token);
        if (!penguinsUserOutcome.ok()) {
            return Outcome.failure(penguinsUserOutcome.getMessage());
        }

        Outcome<RspGameUser> outcome = this.checkUser(penguinsUserOutcome.getData().getUserToken());
        if (!outcome.ok()) {
            PenguinLogThreadLocal.failure("游戏token异常:" + outcome.getMessage());
            return Outcome.failure("游戏token异常");
        }

        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(penguinsUserOutcome.getData().getUserId());

        //根据RspGameUser校验当前用户状态
        Outcome check = PenguinUtil.checkUserLoginInfo(user);
        if (!check.ok()) {
            return Outcome.failure(check.getMessage());
        }

        //校验当前用户和数据库中角色状态是否一致
        if (!user.getUserRole().equals(outcome.getData().getUser_type())) {
            return Outcome.failure("游戏用户角色不一致");
        }

        PenguinLogThreadLocal.setToken(token);
        PenguinLogThreadLocal.setApiInfo("校验用户交易token合法性及时效性");
        return Outcome.success();
    }


    @Override
    public Outcome<PenguinsUser> editUser(RspGameUser gameUser, String token) {

        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(gameUser.getUser_id());
        PenguinsUser result = new PenguinsUser();
        BigDecimal userSaroNumber = gameUser.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
        String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
        result.setUserAccessToken(accessToken);
        if (Objects.isNull(user)) {
            //save game user
            this.saveGameUser(token, gameUser, userSaroNumber, 1, accessToken);
        } else {
            //update game user
            if (!Objects.equals(gameUser.getUser_type(), user.getUserRole())) {
                return Outcome.failure("用户角色发生改变，请联系客服变更角色");
            }
            this.updateGameUser(token, gameUser, userSaroNumber, accessToken, user.getUserRole());
            result.setUserRole(user.getUserRole());
        }
        return Outcome.success(result);
    }


    @Override
    public Outcome<PenguinsUser> getUserById(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Outcome.failure("参数为空");
        }
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return Outcome.failure("未获取到用户信息");
        }
        return Outcome.success(user);
    }

    @Override
    public Outcome<String> getUserByAccessToken(String accessToken) {
        PenguinLogThreadLocal.appendLog("request", accessToken);
        if (StringUtils.isBlank(accessToken)) {
            return Outcome.failure("临时token为空");
        }
        PenguinsUser user = penguinsUserMapper.selectByAccessToken(accessToken);
        if (Objects.isNull(user)) {
            return Outcome.failure("未获取到用户信息");
        }

        PenguinsUser update = new PenguinsUser();
        update.setUserId(user.getUserId());
        update.setUserAccessToken("");
        int i = penguinsUserMapper.updateByPrimaryKeySelective(update);
        update.setUserName(user.getUserName());
        update.setUserToken(user.getUserToken());
        String token = JwtTokenUtil.generateToken(update);
        if (i <= 0) {
            return Outcome.failure("未获取到用户信息");
        }
        return Outcome.success("获取成功", token);
    }

    @Override
    public Outcome<Page<RspUserInfo>> getUserList(ReqUserPage param) {
        Page<RspUserInfo> page = new Page<>();
        if (Objects.isNull(param.getPage())) {
            param.setPage(PageParam.DEFAULT_CURRENT_PAGE);
        }
        if (Objects.isNull(param.getSize())) {
            param.setSize(PageParam.DEFAULT_PAGE_SIZE);
        }
        page.setCurrent(param.getPage());
        page.setSize(param.getSize());
        param.setPage((param.getPage() - 1) * param.getSize());
        param.setSize(param.getSize());

        int count = penguinsUserMapper.selectUserCount(param);
        List<PenguinsUser> list = penguinsUserMapper.selectUserListByModel(param);
        page.setTotal(count);
        if (list.size() < 1 || count < 1) {
            page.setRecords(null);
            return Outcome.success(page);
        }

        List<RspUserInfo> result = new ArrayList<>();
        for (PenguinsUser user : list) {
            RspUserInfo rsp = new RspUserInfo();
            this.getUserInfo(user, rsp);
            if (StringUtils.isNotBlank(user.getUserToken())) {
                RspGameResult rspGameResult = restTemplateService.doPost(url, user.getUserToken(), null);
                if (rspGameResult.getCode() != 1) {
                    if (Objects.nonNull(user.getUserSaroNumber())) {
                        rsp.setUserSaroNumber(user.getUserSaroNumber());
                    }
                    rsp.setUserToken(user.getUserToken());
                    rsp.setUserTokenStatus(0);
                } else {
                    JSONObject jsonObject = JSONObject.parseObject(rspGameResult.getData());
                    RspGameUser gameUser = JSON.toJavaObject(jsonObject, RspGameUser.class);
                    PenguinLogThreadLocal.appendLog("tokenUser", gameUser);
                    PenguinLogThreadLocal.setUserId(gameUser.getUser_id());
                    BigDecimal userSaroNumber = gameUser.getSaro_quantity().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
                    rsp.setUserSaroNumber(userSaroNumber);
                    rsp.setRspGameUser(gameUser);
                    rsp.setUserToken(user.getUserToken());
                    rsp.setUserTokenStatus(1);
                }
            } else {
                if (Objects.nonNull(user.getUserSaroNumber())) {
                    rsp.setUserSaroNumber(user.getUserSaroNumber());
                }
                rsp.setUserTokenStatus(0);
            }

            if(user.getMinRecharge()!=null){
                rsp.setMinRecharge(user.getMinRecharge());
            }
            if(user.getMaxRecharge()!=null){
                rsp.setMaxRecharge(user.getMaxRecharge());
            }

            if(user.getUserTransPwdErrorCount()==null){
                rsp.setUserTramsPwdErrorCount(0);
            }else{
                rsp.setUserTramsPwdErrorCount(user.getUserTransPwdErrorCount());
            }

            if (Objects.nonNull(user.getCreateTime())) {
                rsp.setCreateTime(DateUtil.getTimestampOfDate(user.getCreateTime()));
            }
            rsp.setUserStatus(user.getUserStatus());
            result.add(rsp);
        }
        page.setRecords(result);
        return Outcome.success(page);
    }

    @Override
    public Outcome<RspUserInfo> getUserInfo(String userId) {
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return Outcome.success("暂无数据");
        }
        RspUserInfo rsp = new RspUserInfo();
        this.getUserInfo(user, rsp);

        rsp.setUserPhone2(user.getUserPhone2());
        rsp.setUserPhone3(user.getUserPhone3());
        return Outcome.success(rsp);
    }

    @Override
    public Outcome<RspUserInfo> getUserInfoByAccount(String account) {
        ReqUserPage reqUserPage = new ReqUserPage();
        reqUserPage.setUserName(account);
        List<PenguinsUser> penguinsUsers = penguinsUserMapper.selectUserListByModel(reqUserPage);
        if (penguinsUsers.size() <= 0) {
            return Outcome.success("未获取到对应的用户信息");
        }
        return this.getUserInfo(penguinsUsers.get(0).getUserId());
    }

    @Override
    public Outcome<String> checkUserRole(String userId) {
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        Outcome outcomeCheck = PenguinUtil.checkPenguinsUser(user);
        if (!outcomeCheck.ok()) {
            return outcomeCheck;
        }
        if (user.getUserRole().equals(UserRoleEnum.GAME_MERCHANT.getValue())) {
            return Outcome.failure("该用户已为商家！");
        }
        ExternalHttp externalHttp = new ExternalHttp(user.getUserToken(), appId, appSecret, null);
        Outcome<RspGameUser> outcome = CheckTokenUtils.checkExternalToken(url, externalHttp, restTemplate);
        return PenguinUtil.checkUserRole(user, outcome);
    }

    @Override
    public Outcome<PenguinsUser> checkUserStatus(String userId) {
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        if (!user.getUserStatus().equals(StatusEnum.NORMAL.getValue())) {
            return Outcome.failure("当前账号为非正常状态");
        }
        return Outcome.success("", user);
    }

    @Override
    public Outcome<PenguinsUser> upgradeUser(ReqUserExtend param) {
        PenguinsUser user = PenguinUtil.setReqUserExtendToPenguinsUser(param);
        if (!StringUtils.isBlank(param.getUserId())) {
            PenguinsUser penguinsUser = penguinsUserMapper.selectByPrimaryKey(param.getUserId().trim());
            Outcome outcomeCheck = PenguinUtil.checkPenguinsUser(penguinsUser);
            if (!outcomeCheck.ok()) {
                return outcomeCheck;
            }
            if (penguinsUser.getUserRole().equals(UserRoleEnum.GAME_MERCHANT.getValue())) {
                return Outcome.success("该用户已为商家！", penguinsUser);
            }
            ExternalHttp externalHttp = new ExternalHttp(penguinsUser.getUserToken(), appId, appSecret, null);
            Outcome<RspGameUser> outcome = CheckTokenUtils.checkExternalToken(url, externalHttp, restTemplate);
            Outcome outcome1 = PenguinUtil.checkUserRole(penguinsUser, outcome);
            if (!outcome1.ok()) {
                return outcome1;
            }
            //update
            user.setUpdateTime(new Date());
            user.setUserId(param.getUserId());
            user.setUserRole(UserRoleEnum.GAME_MERCHANT.getValue());
            user.setMinRecharge(new BigDecimal(10.00).setScale(2));
            int update = penguinsUserMapper.updateByPrimaryKeySelective(user);
            if (update > 0) {
                return Outcome.success("更新成功", penguinsUserMapper.selectByPrimaryKey(param.getUserId().trim()));
            }
            return Outcome.failure("更新失败");
        }
        return Outcome.failure("用户id不能为空！");
    }


    @Override
    public Outcome<String> editUserInfo(ReqUserExtend param) {
        PenguinsUser user = PenguinUtil.setReqUserExtendToPenguinsUser(param);
        if (!StringUtils.isBlank(param.getUserId())) {
            PenguinsUser penguinsUser = penguinsUserMapper.selectByPrimaryKey(param.getUserId().trim());
            if (penguinsUser == null) {
                return Outcome.failure("商家不存在");
            }
            //update
            user.setUpdateTime(new Date());
            user.setUserId(param.getUserId());
            user.setMinRecharge(new BigDecimal(10.00).setScale(2));
            int update = penguinsUserMapper.updateByPrimaryKeySelective(user);
            if (update > 0) {
                return Outcome.success("更新成功", param.getUserId());
            }
            return Outcome.failure("更新失败");
        }
        return Outcome.failure("用户id不能为空！");
    }

    @Override
    public Outcome<String> sendMessage(ReqUser param) {
        if (StringUtils.isBlank(param.getUserPhone())) {
            return Outcome.failure("手机号为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userName", param.getUserPhone());
        String json = JSON.toJSONString(params);
        RspMessageResult rsp = restTemplateService.doPost(sendOtpUrl, json);
        if (!Objects.equals(rsp.getResultCode(), "ok")) {
            return Outcome.failure(rsp.getResultMessage());
        }
        if (!rsp.getData()) {
            return Outcome.failure("验证码发生失败，请稍后再试");
        }
        PenguinLogThreadLocal.appendLog("result", rsp);
        return Outcome.success("发送成功", "发送成功");
    }

    @Override
    public Outcome<String> checkSendMessage(ReqUser req) {
        if (StringUtils.isBlank(req.getUserPhone())) {
            return Outcome.failure("手机号为空");
        }
        if (StringUtils.isBlank(req.getCode())) {
            return Outcome.failure("验证码为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userName", req.getUserPhone());
        params.put("otpCode", req.getCode());
        String json = JSON.toJSONString(params);

        RspMessageResult rsp = restTemplateService.doPost(checkSendOtpUrl, json);
        if (!Objects.equals(rsp.getResultCode(), "ok")) {
            return Outcome.failure(rsp.getResultMessage());
        }
        if (!rsp.getData()) {
            return Outcome.failure("验证码验证失败，请正确输入");
        }
        PenguinLogThreadLocal.appendLog("result", rsp);
        return Outcome.success("验证成功", "验证成功");
    }

    @Override
    public Outcome<String> checkMerchantPrice(String userId, ReqOrder param) {
        if (StringUtils.isBlank(userId)) {
            return Outcome.failure("参数用户id为空");
        }
        if (Objects.isNull(param.getOrderType())) {
            return Outcome.failure("请选择交易类型");
        }
        if (Objects.isNull(param.getOrderUnitPrice())) {
            return Outcome.failure("未获取到参数单价信息");
        }
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return Outcome.failure("未获取到商户信息");
        }
        if (Objects.equals(param.getOrderType(), OrderTypeEnum.SELL.getValue())) {
            if (!Objects.equals(user.getRecoveryUnitPrice(), param.getOrderUnitPrice().setScale(2))) {
                return Outcome.failure("当前单价有变动，请重新下单");
            }
        } else {
            if (!Objects.equals(user.getRechargeUnitPrice(), param.getOrderUnitPrice().setScale(2))) {
                return Outcome.failure("当前单价有变动，请重新下单");
            }
        }
        return Outcome.success();
    }

    @Override
    public String encryptPwd(String pwd, String sec) {
        if (StringUtils.isBlank(sec)) {
            return SecureUtil.md5(pwd);
        }
        String prefix = "penguin";
        String suffix = "shop";
        StringBuffer bf = new StringBuffer().append(prefix).append(sec).append(pwd).append(suffix);
        String encoder = Base64.getEncoder().encodeToString(bf.toString().getBytes());
        return SecureUtil.md5(encoder);
    }

    @Override
    public void updatePwdError(String userId) {
        PenguinsUser user = penguinsUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return;
        }
        PenguinsUser error = new PenguinsUser();
        error.setUserId(userId);
        error.setUserTransPwdErrorCount((user.getUserTransPwdErrorCount() == null ? 0 : user.getUserTransPwdErrorCount()) + 1);
        penguinsUserMapper.updateByPrimaryKeySelective(error);
    }


    private void updateGameUser(String token, RspGameUser gameUser, BigDecimal userSaroNumber, String accessToken, Integer role) {
        PenguinsUser updateParam = new PenguinsUser();
        updateParam.setUserId(gameUser.getUser_id());
        updateParam.setUserName(gameUser.getUser_name());
        if (Objects.equals(role, UserRoleEnum.GAME_USER.getValue())) {
            updateParam.setNickname(gameUser.getNickname());
            updateParam.setBankFavour(gameUser.getUser_real_name());
        }
        updateParam.setUserSaroNumber(userSaroNumber);
        updateParam.setUserToken(token);
        updateParam.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(accessToken)) {
            updateParam.setUserAccessToken(accessToken);
        }
        if (role == UserRoleEnum.GAME_MERCHANT.getValue()) {
            updateParam.setMinRecharge(new BigDecimal(Commons.getMINRECHARGE()).setScale(2));
        }
        PenguinLogThreadLocal.appendLog("updateGameUser", updateParam);
        penguinsUserMapper.updateByPrimaryKeySelective(updateParam);
    }


    private void saveGameUser(String token, RspGameUser gameUser, BigDecimal userSaroNumber, int i, String accessToken) {
        Date now = new Date();
        PenguinsUser saveParam = new PenguinsUser();
        saveParam.setUserId(gameUser.getUser_id());
        saveParam.setUserName(gameUser.getUser_name());
        saveParam.setNickname(gameUser.getNickname());
        saveParam.setBankFavour(gameUser.getUser_real_name());
        saveParam.setUserSaroNumber(userSaroNumber);
        saveParam.setUserToken(token);
        saveParam.setCreateTime(now);
        saveParam.setUpdateTime(now);
        saveParam.setUserStatus(StatusEnum.NORMAL.getValue());
        saveParam.setUserRole(i);

        if (i == UserRoleEnum.GAME_MERCHANT.getValue()) {
            saveParam.setMinRecharge(new BigDecimal(Commons.getMINRECHARGE()).setScale(2));
        }

        if (StringUtils.isNotBlank(accessToken)) {
            saveParam.setUserAccessToken(accessToken);
        }
        PenguinLogThreadLocal.appendLog("saveGameUser", saveParam);
        penguinsUserMapper.insert(saveParam);
    }

    private void getUserInfo(PenguinsUser user, RspUserInfo rsp) {
        rsp.setUserId(user.getUserId());
        rsp.setNickname(user.getNickname());
        rsp.setUserAccount(user.getUserName());
        rsp.setUserPhone(user.getUserPhone());
        rsp.setUserPhone2(user.getUserPhone2());
        rsp.setUserPhone3(user.getUserPhone3());
        rsp.setRechargeUnitPrice(user.getRechargeUnitPrice());
        rsp.setRecoveryUnitPrice(user.getRecoveryUnitPrice());
        rsp.setBankFavour(user.getBankFavour());
        rsp.setUserBank(user.getUserBank());
        rsp.setUserBankCard(user.getUserBankCard());
        rsp.setUserBankBranch(user.getUserBankBranch());
        rsp.setUserStatus(user.getUserStatus());
        rsp.setUserRole(user.getUserRole());
    }
}
