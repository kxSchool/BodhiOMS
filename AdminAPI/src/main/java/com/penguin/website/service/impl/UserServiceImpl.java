package com.penguin.website.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.penguin.website.bean.req.ReqLoginVO;
import com.penguin.website.bean.req.ReqUserBaseVO;
import com.penguin.website.bean.req.ReqUserPasswordUpdateVO;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.bean.rsp.RspRegisterVO;
import com.penguin.website.bean.rsp.RspSystemUser;
import com.penguin.website.bean.rsp.RspTokenVO;
import com.penguin.website.model.domain.UserExample;
import com.penguin.website.model.enums.ApiResultEnum;
import com.penguin.website.util.DateUtil;
import com.penguin.website.util.GoogleAuthenticator;
import com.penguin.website.util.JwtTokenUtil;
import com.penguin.website.util.QRCodeUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.penguin.website.mapper.master.custom.UserMapperCustom;
import com.penguin.website.mapper.master.generator.UserMapper;
import com.penguin.website.model.domain.User;
import com.penguin.website.service.UserService;
import org.springframework.util.DigestUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapperCustom userMapperCustom;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public User getByNameAndPwd(String name, String pwd) throws Exception {
        return userMapperCustom.getByNameAndPwd(name, pwd);
    }

    @Override
    public void updateDatum(User user) throws Exception {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User findUser() {
        List<User> lists = userMapperCustom.findUser();
        if (lists != null && lists.size() > 0) {
            return lists.get(0);
        } else {
            return new User();
        }
    }

    @Override
    public User findByUserIdAndUserPwd(Integer userId, String formerlyPwd) throws Exception {
        return userMapperCustom.findByUserIdAndUserPwd(userId, formerlyPwd);
    }

    @Override
    public void updateLoginLastTime(Date date, Integer userId) throws Exception {
        User user = new User();
        user.setLoginLastTime(date);
        user.setUserId(userId);
        userMapper.updateByPrimaryKeySelective(user);

    }

    @Override
    public void updateUserNormal(Integer userId) throws Exception {
        User user = new User();
        user.setLoginError(0);
        user.setLoginEnable("false");
        user.setUserId(userId);
        user.setLoginLastTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer updateError() throws Exception {
        User user = this.findUser();
        user.setLoginError((user.getLoginError() == null ? 0 : user.getLoginError()) + 1);
        userMapper.updateByPrimaryKeySelective(user);
        return user.getLoginError();
    }

    @Override
    public Integer updateError(Integer userId) throws Exception {
        User user = userMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return null;
        }
        user.setLoginError((user.getLoginError() == null ? 0 : user.getLoginError()) + 1);
        userMapper.updateByPrimaryKeySelective(user);
        return user.getLoginError();
    }

    @Override
    public void updateLoginEnable(String enable, Integer error) throws Exception {
        User user = this.findUser();
        user.setLoginEnable(enable);
        user.setLoginError(error);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void save(User user) {
        userMapper.insert(user);
    }


    @Value("${googleAuthenticator.host: http://localhost}")
    private String host;


    /**
     * 根据账号查找
     *
     * @param userName
     * @return
     */
    public User findUserByName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        return userMapper.findUserByName(userName);
    }

    /**
     * 校验用户是否存在
     *
     * @param userName
     * @return
     */
    @Override
    public boolean checkUserNameIsExit(String userName) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        return userMapper.countUserByName(userName) > 0;
    }

    @Override
    public Outcome addUser(ReqUserBaseVO userBaseVO) throws Exception {
        if (checkUserNameIsExit(userBaseVO.getUserName())) {
            return Outcome.failure(ApiResultEnum.USER_IS_EXIST.getMessage());
        }
        User user = new User();
        user.setUserName(userBaseVO.getUserName());
        user.setUserPwd(DigestUtils.md5DigestAsHex(userBaseVO.getPassword().getBytes()));
        user.setGoogleSecret(GoogleAuthenticator.getRandomSecretKey());
        user.setLoginEnable("false");
        user.setLoginError(0);
        user.setCreateTime(new Date());
        int i = userMapper.insert(user);
        if (i > 0) {
            RspRegisterVO rspRegisterVO = new RspRegisterVO();
            rspRegisterVO.setGoogleSecret(user.getGoogleSecret());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            String secretQrCode = GoogleAuthenticator.getGoogleAuthenticatorBarCode(user.getGoogleSecret(), user.getUserName(), host);
            QRCodeUtil.encode(secretQrCode, stream);
            String base64QrCode = Base64.encodeBase64String(stream.toByteArray());
            stream.close();
            rspRegisterVO.setQrCode(base64QrCode);
            return Outcome.success(rspRegisterVO);
        }
        return Outcome.failure(ApiResultEnum.USER_INSERT_FAILURE.getMessage());
    }

    @Override
    public Outcome login(ReqLoginVO loginVO) throws Exception {
        User user = findUserByName(loginVO.getUserName());
        if (user == null || !user.getUserPwd().equals(DigestUtils.md5DigestAsHex(loginVO.getPassword().getBytes()))) {
            //
            if (Objects.nonNull(user)) {
                userService.updateError(user.getUserId());
            }
            return Outcome.failure(ApiResultEnum.ACCOUNT_LOGIN_FAILURE.getMessage());
        }
        long code = 0L;
        try {
            code = Long.parseLong(loginVO.getCode());
        } catch (Exception e) {
            userService.updateError(user.getUserId());
            return Outcome.failure(ApiResultEnum.GOOGLE_CODE_NOT_MATCH.getMessage());
        }
        boolean isTrue = GoogleAuthenticator.check_code(user.getGoogleSecret(), code, System.currentTimeMillis());
        if (!isTrue) {
            userService.updateError(user.getUserId());
            return Outcome.failure(ApiResultEnum.GOOGLE_CODE_NOT_MATCH.getMessage());
        }
        String token = JwtTokenUtil.generateToken(user);
        RspTokenVO tokenVO = new RspTokenVO();
        tokenVO.setToken(token);
        return Outcome.success("", tokenVO);
    }

    @Override
    public Outcome<List<RspSystemUser>> findUserList() {
        List<User> all = userMapper.findAll();
        if (all.size() <= 0) {
            return Outcome.success();
        }
        List<RspSystemUser> result = new ArrayList<>();
        for (User user : all) {
            if (StringUtils.isBlank(user.getGoogleSecret())) {
                continue;
            }
            RspSystemUser rsp = new RspSystemUser();
            rsp.setUserId(user.getUserId());
            rsp.setUserName(user.getUserName());
            rsp.setLoginError(user.getLoginError());
            if (Objects.nonNull(user.getLoginLastTime())) {
                rsp.setLoginLastTime(DateUtil.getTimestampOfDate(user.getLoginLastTime()));
            }
            if (Objects.nonNull(user.getCreateTime())) {
                rsp.setCreateTime(DateUtil.getTimestampOfDate(user.getCreateTime()));
            }
            result.add(rsp);
        }
        return Outcome.success(result);
    }

    @Override
    public Outcome updatePassword(ReqUserPasswordUpdateVO passwordUpdateVO) {
        User user = new User();
        user.setUserId(passwordUpdateVO.getId().intValue());
        if (StringUtils.isBlank(passwordUpdateVO.getPassword())) {
            // 默认密码 000000
            passwordUpdateVO.setPassword("000000");
        }
        user.setUserPwd(DigestUtils.md5DigestAsHex(passwordUpdateVO.getPassword().getBytes()));
        return userMapper.updateById(user) > 0 ? Outcome.success("修改成功") : Outcome.failure("密码修改失败");
    }
}
