package com.penguin.website.web.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.penguin.website.model.dto.PenguinConst;
import com.penguin.website.service.LogService;
import com.penguin.website.util.MapCache;

import cn.hutool.core.text.StrBuilder;


public class BaseController {
	/** 日志 **/
	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	protected LogService logService;
	
	protected MapCache cache = MapCache.single();
	// 默认主题
	public static String THEME = "yiyuan";

	/**
	 * 根据主题名称渲染页面
	 *
	 * @param pageName
	 *            pageName
	 * @return 返回拼接好的模板路径
	 */
	public String render(String pageName) {
		//加载主题
		if(PenguinConst.THEME_NAME!=null) {
			THEME=PenguinConst.THEME_NAME;
		}
		StrBuilder themeStr = new StrBuilder("themes/");
		themeStr.append(THEME);
		themeStr.append("/");
		return themeStr.append(pageName).toString();
	}

	/**
	 * 404页面
	 * 
	 * @return
	 */
	public String render_404() {
		return "error/404";
	}
}
