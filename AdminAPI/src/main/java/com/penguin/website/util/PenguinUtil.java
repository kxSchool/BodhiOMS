package com.penguin.website.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.ReqOrder;
import com.penguin.website.bean.req.ReqUserExtend;
import com.penguin.website.bean.rsp.*;
import com.penguin.website.model.domain.PenguinsUser;
import com.penguin.website.model.dto.ExternalHttp;
import com.penguin.website.model.enums.StatusEnum;
import com.penguin.website.model.enums.UserRoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.devtools.restart.FailureHandler;
import org.springframework.util.Assert;

import com.penguin.website.model.domain.ArticleCustom;
import com.penguin.website.model.dto.PenguinConst;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import org.springframework.web.client.RestTemplate;


public class PenguinUtil {

    /**
     * 日志
     **/
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 推送百度
     *
     * @param blog_url 在搜索资源平台验证的站点
     * @param token    在搜索资源平台申请的推送用的准入密钥
     * @param urls     文章路径
     * @return
     */
    public static String baiduPost(String blogUrl, String token, String urls) {
        StringBuffer result = new StringBuffer();
        PrintWriter out = null;
        BufferedReader br = null;
        try {
            URL url = new URL("	http://data.zz.baidu.com/urls?site=" + blogUrl + "&token=" + token + "");
            // 打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("User-Agent", "curl/7.12.1");
            conn.setRequestProperty("Host", "data.zz.baidu.com*");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Content-Length", "83");
            // 设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            // 最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            // post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数即数据
            out.print(urls.trim());
            // 缓冲数据
            out.flush();
            // 获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            // 构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            // 关闭流
            is.close();
            // 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            // 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 转换文件大小
     *
     * @param size
     * @return
     */
    public static String parseSize(long size) {
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 获取文件长和宽
     *
     * @param file file
     * @return String
     */
    public static String getImageWh(File file) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(file));
            return image.getWidth() + "x" + image.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 生成rss
     *
     * @param articles
     * @return
     */
    public static String buildRss(List<ArticleCustom> articles) throws FeedException {
        Assert.notEmpty(articles, "posts must not be empty");
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        if (PenguinConst.OPTIONS.get("blog_name") == null) {
            channel.setTitle("");
        } else {
            channel.setTitle(PenguinConst.OPTIONS.get("blog_name"));
        }
        if (PenguinConst.OPTIONS.get("blog_url") == null) {
            channel.setLink("");
        } else {
            channel.setLink(PenguinConst.OPTIONS.get("blog_url"));
        }
        if (PenguinConst.OPTIONS.get("blog_url") == null) {
            channel.setUri("");
        } else {
            channel.setUri(PenguinConst.OPTIONS.get("blog_url"));
        }
        if (PenguinConst.OPTIONS.get("seo_describe") == null) {
            channel.setDescription("");
        } else {
            channel.setDescription(PenguinConst.OPTIONS.get("seo_describe"));
        }
        channel.setLanguage("zh-CN");
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            Item item = new Item();
            Description descr = new Description();
            item.setTitle(articles.get(i).getArticleTitle());
            item.setLink(PenguinConst.OPTIONS.get("blog_url") + "/post/" + articles.get(i).getArticleUrl());
            item.setPubDate(articles.get(i).getArticleNewstime());
            descr.setValue(articles.get(i).getArticleContent());
            item.setDescription(descr);
            items.add(item);
        }
        channel.setItems(items);
        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }

    /**
     * create by: mlshao
     * description:设置最新电话号码
     */
    public static void setUserPhoneToRspOrderExtendp(PenguinsUser user, RspOrderExtend rsp) {
        if (Objects.nonNull(user)) {
            if (StringUtils.isNotBlank(user.getUserPhone())) {
                String phone = StringLocalUtils.replaceSpace(user.getUserPhone());
                rsp.setPhone(phone);
                rsp.setUserPhone(phone);
            }
            if (StringUtils.isNotBlank(user.getUserPhone2())) {
                String phone = StringLocalUtils.replaceSpace(user.getUserPhone2());
                rsp.setUserPhone2(phone);
            } else {
                rsp.setUserPhone2("");
            }
            if (StringUtils.isNotBlank(user.getUserPhone3())) {
                String phone = StringLocalUtils.replaceSpace(user.getUserPhone3());
                rsp.setUserPhone3(phone);
            } else {
                rsp.setUserPhone3("");
            }
        }
    }

    public static void setMerchantPhoneToRspOrderExtendp(PenguinsUser user, RspOrderExtend rsp) {
        if (Objects.nonNull(user)) {
            if (StringUtils.isNotBlank(user.getUserPhone())) {
                String phone = StringLocalUtils.replaceSpace(user.getUserPhone());
                rsp.setMerchantPhone(phone);
            }
            if (StringUtils.isNotBlank(user.getUserPhone2())) {
                String phone = StringLocalUtils.replaceSpace(user.getUserPhone2());
                rsp.setMerchantPhone2(phone);
            } else {
                rsp.setMerchantPhone2("");
            }
            if (StringUtils.isNotBlank(user.getUserPhone3())) {
                String phone = StringLocalUtils.replaceSpace(user.getUserPhone3());
                rsp.setMerchantPhone3(phone);
            } else {
                rsp.setMerchantPhone3("");
            }
        }
    }

    /**
     * description:
     */
    public static <T> HashMap<String, T> setOrderList(List<T> list, Integer waitPayNum, Integer waitReleasedNum) {
        HashMap hashMap = new HashMap();
        hashMap.put("list", list);
        hashMap.put("waitPayNum", waitPayNum);
        hashMap.put("waitReleasedNum", waitReleasedNum);
        return hashMap;
    }

    public static Outcome<RspGameUser> rspResultToRspGameUser(RspGameResult rspGameResult) {
        if (rspGameResult.getCode() != 1) {
            System.out.println("jsonObject======");
            System.out.println(rspGameResult.toString());
            return Outcome.failure(rspGameResult.getMessage());
        }
        JSONObject jsonObject = JSONObject.parseObject(rspGameResult.getData());
        RspGameUser gameUser = JSON.toJavaObject(jsonObject, RspGameUser.class);
        return Outcome.success("", gameUser);
    }

    public static Outcome checkPenguinsUser(PenguinsUser user) {
        if (user == null) {
            return Outcome.failure("用户id不存在！");
        }
        if (StringUtils.isBlank(user.getUserToken())) {
            return Outcome.failure("游戏token为空!");
        }
        return Outcome.success();
    }

    public static PenguinsUser setReqUserExtendToPenguinsUser(ReqUserExtend param) {
        PenguinsUser user = new PenguinsUser();
        user.setUserName(param.getUserAccount());
        //user.setNickname(param.getNickname());
        user.setUserPhone(param.getUserPhone());
        user.setUserPhone2(param.getUserPhone2());
        user.setUserPhone3(param.getUserPhone3());
        if (param.getRechargeUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            user.setRechargeUnitPrice(param.getRechargeUnitPrice());
        }
        if (param.getRecoveryUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            user.setRecoveryUnitPrice(param.getRecoveryUnitPrice());
        }
        if (Objects.nonNull(param.getUserRole()) && param.getUserRole() > 0) {
            user.setUserRole(param.getUserRole());
        }
        user.setBankFavour(param.getBankFavour());
        user.setUserBank(param.getUserBank());
        user.setUserBankCard(param.getUserBankCard());
        user.setUserBankBranch(param.getUserBankBranch());
        user.setUserStatus(param.getUserStatus());
        user.setBankFavour(param.getBankFavour());
        user.setUserBank(param.getUserBank());
        user.setUserBankCard(param.getUserBankCard());
        user.setUserBankBranch(param.getUserBankBranch());
        user.setUserStatus(param.getUserStatus());
        return user;
    }

    public static Outcome<RspGameUser> checkRspGameUser(RspGameUser gameUser) {
        if (StringUtils.isBlank(gameUser.getUser_real_name())) {
            return Outcome.failure("用户未实名");
        }
        if (Objects.isNull(gameUser.getUser_real_name_state()) || !Objects.equals(1, gameUser.getUser_real_name_state())) {
            return Outcome.failure("用户未完成实名认证");
        }
        return Outcome.success("", gameUser);
    }

    public static Outcome<String> checkUserLoginInfo(PenguinsUser user) {
        if (Objects.isNull(user)) {
            return Outcome.failure("用户信息为空");
        }
        if (StringUtils.isBlank(user.getUserToken())) {
            return Outcome.failure("游戏token为空");
        }

        if (!user.getUserStatus().equals(StatusEnum.NORMAL.getValue())) {
            return Outcome.failure("该账号被禁用");
        }
        return Outcome.success("");
    }

    public static Outcome<String> checkMerchantUserBaseRoleInfo(PenguinsUser user) {
        if (Objects.isNull(user)) {
            return Outcome.failure("用户信息为空");
        }
        if (StringUtils.isBlank(user.getUserToken())) {
            return Outcome.failure("游戏token为空");
        }

        if (!user.getUserStatus().equals(StatusEnum.NORMAL.getValue())) {
            return Outcome.failure("商户账号禁用");
        }
        //校验商家状态
        if (!user.getUserRole().equals(UserRoleEnum.GAME_MERCHANT.getValue())) {
            return Outcome.failure("商家账号异常");
        }
        return Outcome.success("");
    }

    public static Outcome<RspGameUser> checkMerchantRoleInfoAndGamInfo(PenguinsUser user, String url, String appId, String appSecret, RestTemplate restTemplate) {
        Outcome<String> outcome = PenguinUtil.checkMerchantUserBaseRoleInfo(user);
        if (!outcome.ok()) {
            return Outcome.failure(outcome.getMessage());
        }
        Outcome<RspGameUser> rspGameUserOutcome = CheckTokenUtils.checkMerchantExternalTokenAndResp(url, user.getUserToken(), appId, appSecret, restTemplate);
        if (!outcome.ok()) {
            return rspGameUserOutcome;
        }
        return Outcome.success("");
    }

    public static Outcome setLogAndOutComeFailure(String failuerMes) {
        PenguinLogThreadLocal.failure(failuerMes);
        return Outcome.failure(failuerMes);
    }

    public static Outcome setLogAndOutComeFailure(String logMes, String failuerMes) {
        PenguinLogThreadLocal.failure(logMes);
        return Outcome.failure(failuerMes);
    }

    public static Outcome checkReqOrder(ReqOrder reqOrder) {
        String transId = reqOrder.getOrderTransactorId();
        if (StringUtils.isBlank(transId)) {
            return Outcome.failure("交易用户为空");
        }

        if (Objects.isNull(reqOrder.getOrderPriceCny()) && Objects.isNull(reqOrder.getOrderPriceSaro())) {
            return Outcome.failure("请选择按数量或按金额充值");
        }


        return Outcome.success();
    }

    public static Outcome checkUserBaseInfo(PenguinsUser user) {
        if (Objects.isNull(user) || StringUtils.isBlank(user.getUserToken())) {
            return Outcome.failure("用户信息为空");
        }

        if (StringUtils.isBlank(user.getUserPhone())) {
            return Outcome.failure("商家未填写手机信息");
        }

        if (Objects.isNull(user.getRechargeUnitPrice())) {
            return Outcome.failure("商家未设置交易单价");
        }

        if (StringUtils.isBlank(user.getUserBankCard()) || StringUtils.isBlank(user.getUserBank())
                || StringUtils.isBlank(user.getBankFavour())) {
            return Outcome.failure("商家未填写银行卡信息");
        }

        return Outcome.success();
    }

    public static Outcome<String> checkUserRole(PenguinsUser user, Outcome<RspGameUser> outcome) {
        if (!outcome.ok()) {
            return Outcome.failure(outcome.getMessage());
        }
        RspGameUser rspGameUser = outcome.getData();
        if (rspGameUser.getUser_type().equals(UserRoleEnum.GAME_MERCHANT.getValue())) {
            if (user.getUserRole().equals(UserRoleEnum.GAME_USER.getValue())) {
                return Outcome.success("半商家状态！");
            } else {
                return Outcome.failure("该用户已为商家！");
            }
        }
        return Outcome.failure("该用户为玩家用户！");
    }

    public static <T> Outcome<T> setErrorLog(Outcome<T> outcome) {
        if (!outcome.ok()) {
            PenguinLogThreadLocal.failure(outcome.getMessage());
        }
        return outcome;
    }

    public static Outcome checkPenguinsMerchantMustPro(PenguinsUser user) {

        if (Objects.isNull(user)) {
            return Outcome.failure(" user is null");
        }

        if (Objects.isNull(user.getUserRole()) || !user.getUserRole().equals(UserRoleEnum.GAME_MERCHANT.getValue())) {
            return Outcome.failure(user.getUserId() + " user role error");
        }

        if (StringUtils.isBlank(user.getUserPhone()) || StringUtils.isBlank(user.getUserPhone().trim())) {
            return Outcome.failure(user.getUserId() + " user phone is null");
        }
        if (user.getRechargeUnitPrice() == null) {
            return Outcome.failure(user.getUserId() + " user rechargeUnitPrice is null");
        }
        if (user.getRecoveryUnitPrice() == null) {
            return Outcome.failure(user.getUserId() + " user recoveryUnitPrice is null");
        }
        if (StringUtils.isBlank(user.getBankFavour()) || StringUtils.isBlank(user.getBankFavour().trim())) {
            return Outcome.failure(user.getUserId() + " user bankFavour is null");
        }
        if (StringUtils.isBlank(user.getUserBankCard()) || StringUtils.isBlank(user.getUserBankCard().trim())) {
            return Outcome.failure(user.getUserId() + " user userBankCard is null");
        }
        if (StringUtils.isBlank(user.getUserBank()) || StringUtils.isBlank(user.getUserBank().trim())) {
            return Outcome.failure(user.getUserId() + " user userBank is null");
        }
        if (StringUtils.isBlank(user.getUserBankBranch()) || StringUtils.isBlank(user.getUserBankBranch().trim())) {
            return Outcome.failure(user.getUserId() + " user userBankBranch is null");
        }

        if (Objects.isNull(user.getRecoveryUnitPrice())) {
            return Outcome.failure(user.getUserId() + " user RecoveryUnitPrice is null,userId = " + user.getUserId());
        }
        if (!Objects.equals(user.getUserStatus(), StatusEnum.NORMAL.getValue())) {
            return Outcome.failure(user.getUserId() + " user userStatus error");
        }

        if (Objects.isNull(user.getMinRecharge()) || Objects.isNull(user.getRechargeUnitPrice())) {
            return Outcome.failure(user.getUserId() + " user MinRecharge or RechargeUnitPrice is null,userId = " + user.getUserId());
        }

        return Outcome.success();
    }

}
