package com.penguin.website.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 管理员登录
 */
@ApiModel(value = "管理员登录")
public class REQAdminLogin implements Serializable {

    /**
     *  账号
     */
    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String userPwd;

    /**
     * 谷歌验证码
     */
    @ApiModelProperty(value = "谷歌验证码")
    private String googleCaptcha;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGoogleCaptcha() {
        return googleCaptcha;
    }

    public void setGoogleCaptcha(String googleCaptcha) {
        this.googleCaptcha = googleCaptcha;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
