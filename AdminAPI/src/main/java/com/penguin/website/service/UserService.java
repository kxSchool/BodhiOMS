package com.penguin.website.service;

import java.util.Date;
import java.util.List;

import com.penguin.website.bean.req.ReqLoginVO;
import com.penguin.website.bean.req.ReqUserBaseVO;
import com.penguin.website.bean.req.ReqUserPasswordUpdateVO;
import com.penguin.website.bean.rsp.Outcome;
import com.penguin.website.bean.rsp.RspSystemUser;
import com.penguin.website.model.domain.User;


public interface UserService {
	/**
	 * 根据用户名和密码获取用户信息
	 * 
	 * @param name
	 *            用户名
	 * @param pwd
	 *            用户密码
	 * @return
	 */
	User getByNameAndPwd(String name, String pwd) throws Exception;

	/**
	 * 修改用户资料
	 * 
	 * @param user
	 */
	void updateDatum(User user) throws Exception;

	/**
	 * 用户id获取用户信息
	 * 
	 * @return
	 */
	User findUser();

	/**
	 * 查询原密码是否存在
	 * 
	 * @param userId
	 *            用户id
	 * @param formerlyPwd
	 *            原密码
	 * @return
	 */
	User findByUserIdAndUserPwd(Integer userId, String formerlyPwd) throws Exception;

	/**
	 * 修改最后登录时间
	 * 
	 * @param date
	 * @param userId
	 */
	void updateLoginLastTime(Date date, Integer userId) throws Exception;

	/**
	 * 修改用户状态为正常
	 * 
	 * @param userId
	 */
	void updateUserNormal(Integer userId) throws Exception;

	/**
	 * 更新登录失败次数
	 * 
	 * @return
	 */
	Integer updateError() throws Exception;

	/**
	 * 更新登录失败次数
	 *
	 * @return
	 */
	Integer updateError(Integer userId) throws Exception;

	/**
	 * 禁止登录
	 * 
	 * @param enable 状态
	 * @param error 错误次数
	 */
	void updateLoginEnable(String enable, Integer error) throws Exception;
	/**
	 * 保存用户
	 * @param user
	 */
	void save(User user);

	/**
	 * 添加账号
	 * @param userBaseVO
	 * @return
	 */
	Outcome addUser(ReqUserBaseVO userBaseVO) throws Exception;

	/**
	 * 登录
	 * @param loginVO
	 * @return
	 */
	Outcome login(ReqLoginVO loginVO) throws Exception;

	/**
	 * 用户列表
	 * @return
	 */
	Outcome<List<RspSystemUser>> findUserList();

	/**
	 * 修改用户密码
	 * @param passwordUpdateVO
	 * @return
	 */
	Outcome updatePassword(ReqUserPasswordUpdateVO passwordUpdateVO);

	/**
	 * 校验用户是否存在
	 * @param userName
	 * @return
	 */
	boolean checkUserNameIsExit(String userName);
}
