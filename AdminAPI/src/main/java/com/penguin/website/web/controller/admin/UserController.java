package com.penguin.website.web.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.penguin.website.model.domain.Attachment;
import com.penguin.website.model.domain.Log;
import com.penguin.website.model.domain.User;
import com.penguin.website.model.dto.JsonResult;
import com.penguin.website.model.dto.LogConstant;
import com.penguin.website.model.enums.PenguinEnums;
import com.penguin.website.service.AttachmentService;
import com.penguin.website.service.UserService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;

@RequestMapping(value = "/admin/profile")
@Controller
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 
	 * @param model
	 * @param session
	 * @return 返回个人资料页面
	 */
	@GetMapping
	public String profile(Model model, HttpSession session) {
		// 单用户
		User users = userService.findUser();
		model.addAttribute("user", users);
		return "admin/admin_user";
	}

	/**
	 * 所有附件
	 * 
	 * @param model
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping(value = "/openChoice/{id}")
	public String openChoice(Model model, @PathVariable String id,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "18") int limit) {
		PageInfo<Attachment> lists = attachmentService.getAttachment(page, limit);
		model.addAttribute("info", lists);
		model.addAttribute("id", id);
		return "admin/part/open_choice";
	}

	/**
	 * 
	 * @param user
	 * @return 修改个人资料结果
	 */
	@PostMapping(value = "updateProfile")
	@ResponseBody
	public JsonResult updateProfile(User user, HttpSession session) {
		try {
			userService.updateDatum(user);
			session.invalidate();
		} catch (Exception e) {
			log.error("修改资料", e);
			return new JsonResult(PenguinEnums.ERROR.isFlag(), PenguinEnums.ERROR.getMessage());
		}
		return new JsonResult(PenguinEnums.PRESERVE_SUCCESS.isFlag(), PenguinEnums.PRESERVE_SUCCESS.getMessage());
	}

	/**
	 * 修改密码
	 * 
	 * @param formerlyPwd
	 *            原密码
	 * @param nowPwd
	 *            新密码
	 * @param confirmPwd
	 *            确认新密码
	 * @param userId
	 *            用户id
	 * @return
	 */
	@PostMapping(value = "updatePwd")
	@ResponseBody
	public JsonResult updatePwd(@RequestParam(value = "formerlyPwd") String formerlyPwd,
			@RequestParam(value = "nowPwd") String nowPwd, @RequestParam(value = "confirmPwd") String confirmPwd,
			@RequestParam(value = "userId") Integer userId,HttpServletRequest request) {
		if (StringUtils.isBlank(formerlyPwd) || StringUtils.isBlank(formerlyPwd) || StringUtils.isBlank(confirmPwd)) {
			return new JsonResult(PenguinEnums.OPERATION_ERROR.isFlag(), "请填写完整信息");
		}
		if (!nowPwd.equals(confirmPwd)) {
			return new JsonResult(PenguinEnums.OPERATION_ERROR.isFlag(), "两次密码不一样");
		}
		try {
			User user = userService.findByUserIdAndUserPwd(userId, SecureUtil.md5(formerlyPwd));
			if (user != null) {
				user.setUserPwd(SecureUtil.md5(confirmPwd));
				userService.updateDatum(user);
				logService.save(new Log(LogConstant.UPDATE_PWD, LogConstant.LOGIN_SUCCESS, ServletUtil.getClientIP(request),
						DateUtil.date()));
			} else {
				return new JsonResult(PenguinEnums.OPERATION_ERROR.isFlag(), "原密码错误");
			}
		} catch (Exception e) {
			log.error("修改密码",e);
			return new JsonResult(PenguinEnums.ERROR.isFlag(), "修改密码失败");
		}
		return new JsonResult(PenguinEnums.OPERATION_SUCCESS.isFlag(), "修改密码成功");
	}
}
