package com.penguin.website.web.controller.penguin;

import com.penguin.website.annotation.NoVerificationToken;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.bean.req.ReqLoginVO;
import com.penguin.website.bean.req.ReqUserBaseVO;
import com.penguin.website.bean.req.ReqUserPasswordUpdateVO;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.model.enums.PenguinLogEventActionEnum;
import com.penguin.website.model.enums.PenguinLogEventEnum;
import com.penguin.website.service.LogService;
import com.penguin.website.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "用户账号")
@CrossOrigin
@RestController
@RequestMapping("/api/system")
public class SystemUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @NoVerificationToken
    @ApiOperation("添加用户")
    @PostMapping("/addUser")
    public Outcome addUser(HttpServletRequest request, @Valid @RequestBody ReqUserBaseVO userBaseVO)throws Exception{
        try {
            PenguinLogThreadLocal.setParam(userBaseVO);
            PenguinLogThreadLocal.setApiInfo("添加用户");
            Outcome outcome = userService.addUser(userBaseVO);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
            }
            PenguinLogThreadLocal.appendLog("result", outcome.getData());
            return outcome;
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_USER_REGISTER, PenguinLogEventActionEnum.CREATE, request);
        }

    }

    @NoVerificationToken
    @ApiOperation("登录")
    @PostMapping("/login")
    public Outcome login(HttpServletRequest request, @Valid @RequestBody ReqLoginVO loginVO){
        try {
            PenguinLogThreadLocal.setParam(loginVO);
            PenguinLogThreadLocal.setApiInfo("用户登录");
            Outcome outcome = userService.login(loginVO);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
            }
            PenguinLogThreadLocal.appendLog("result", outcome.getData());
            return outcome;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_USER_LOGIN, PenguinLogEventActionEnum.SELECT, request);
        }
        return Outcome.failure("");
    }

    @ApiOperation("用户列表")
    @GetMapping("/userList")
    public Outcome userList(HttpServletRequest request, @RequestHeader(value = "token") String token){
        PenguinLogThreadLocal.setToken(token);
        PenguinLogThreadLocal.setApiInfo("用户列表");
        try {
            Outcome outcome = userService.findUserList();
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
            }
            PenguinLogThreadLocal.appendLog("result", outcome.getData());
            return outcome;
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_USER_LIST, PenguinLogEventActionEnum.SELECT, request);
        }
    }

    /*@ApiOperation("/修改密码")
    @PostMapping("/updatePassword")
    public Outcome updatePassword(HttpServletRequest request, @RequestHeader(value = "token") String token, @Valid @RequestBody ReqUserPasswordUpdateVO passwordUpdateVO){

        try{
            PenguinLogThreadLocal.setToken(token);
            PenguinLogThreadLocal.setParam(passwordUpdateVO);
            PenguinLogThreadLocal.setApiInfo("用户列表");
            Outcome outcome = userService.updatePassword(passwordUpdateVO);
            if (!outcome.ok()) {
                PenguinLogThreadLocal.failure(outcome.getMessage());
            }
            PenguinLogThreadLocal.appendLog("result", outcome.getData());
            return outcome;
        } finally {
            logService.saveLog(PenguinLogEventEnum.SYSTEM_USER_UPDATE_PWD, PenguinLogEventActionEnum.EDIT, request);
        }

    }*/
}
