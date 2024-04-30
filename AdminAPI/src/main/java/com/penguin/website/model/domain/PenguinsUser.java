package com.penguin.website.model.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * penguins_user
 * @author 
 */
public class PenguinsUser implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    private String userToken;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户电话2
     */
    private String userPhone2;

    /**
     * 用户电话3
     */
    private String userPhone3;

    /**
     * 用户状态：1正常
     */
    private Integer userStatus;

    /**
     * 用户角色： 1用户 2商家
     */
    private Integer userRole;

    /**
     * 收款人
     */
    private String bankFavour;

    /**
     * 用户开户行
     */
    private String userBank;

    /**
     * 用户银行卡号
     */
    private String userBankCard;

    /**
     * 开户支行
     */
    private String userBankBranch;

    /**
     * 用户saro数量
     */
    private BigDecimal userSaroNumber;

    /**
     * 充值单价（每个saro的价格）
     */
    private BigDecimal rechargeUnitPrice;

    /**
     * 回收单价（每个saro的价格）
     */
    private BigDecimal recoveryUnitPrice;

    /**
     * 最小充值数，单位CNY
     */
    private BigDecimal minRecharge;

    /**
     * 最大充值数，单位CNY
     */
    private BigDecimal maxRecharge;

    /**
     * 临时令牌
     */
    private String userAccessToken;

    /**
     * 交易密码
     */
    private String userTransPwd;

    /**
     * 正割
     */
    private String userSec;

    /**
     * 交易密码错误次数
     */
    private Integer userTransPwdErrorCount;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone2() {
        return userPhone2;
    }

    public void setUserPhone2(String userPhone2) {
        this.userPhone2 = userPhone2;
    }

    public String getUserPhone3() {
        return userPhone3;
    }

    public void setUserPhone3(String userPhone3) {
        this.userPhone3 = userPhone3;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getBankFavour() {
        return bankFavour;
    }

    public void setBankFavour(String bankFavour) {
        this.bankFavour = bankFavour;
    }

    public String getUserBank() {
        return userBank;
    }

    public void setUserBank(String userBank) {
        this.userBank = userBank;
    }

    public String getUserBankCard() {
        return userBankCard;
    }

    public void setUserBankCard(String userBankCard) {
        this.userBankCard = userBankCard;
    }

    public String getUserBankBranch() {
        return userBankBranch;
    }

    public void setUserBankBranch(String userBankBranch) {
        this.userBankBranch = userBankBranch;
    }

    public BigDecimal getUserSaroNumber() {
        return userSaroNumber;
    }

    public void setUserSaroNumber(BigDecimal userSaroNumber) {
        this.userSaroNumber = userSaroNumber;
    }

    public BigDecimal getRechargeUnitPrice() {
        return rechargeUnitPrice;
    }

    public void setRechargeUnitPrice(BigDecimal rechargeUnitPrice) {
        this.rechargeUnitPrice = rechargeUnitPrice;
    }

    public BigDecimal getRecoveryUnitPrice() {
        return recoveryUnitPrice;
    }

    public void setRecoveryUnitPrice(BigDecimal recoveryUnitPrice) {
        this.recoveryUnitPrice = recoveryUnitPrice;
    }

    public BigDecimal getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(BigDecimal minRecharge) {
        this.minRecharge = minRecharge;
    }

    public BigDecimal getMaxRecharge() {
        return maxRecharge;
    }

    public void setMaxRecharge(BigDecimal maxRecharge) {
        this.maxRecharge = maxRecharge;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public String getUserTransPwd() {
        return userTransPwd;
    }

    public void setUserTransPwd(String userTransPwd) {
        this.userTransPwd = userTransPwd;
    }

    public String getUserSec() {
        return userSec;
    }

    public void setUserSec(String userSec) {
        this.userSec = userSec;
    }

    public Integer getUserTransPwdErrorCount() {
        return userTransPwdErrorCount;
    }

    public void setUserTransPwdErrorCount(Integer userTransPwdErrorCount) {
        this.userTransPwdErrorCount = userTransPwdErrorCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}