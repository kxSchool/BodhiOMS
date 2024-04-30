package com.penguin.website.service;

import com.github.pagehelper.PageInfo;
import com.penguin.website.model.domain.Log;
import com.penguin.website.model.enums.PenguinLogEventActionEnum;
import com.penguin.website.model.enums.PenguinLogEventEnum;

import javax.servlet.http.HttpServletRequest;


public interface LogService {
	/**
	 * 保存日志
	 * 
	 * @param log
	 */
	void save(Log log);

	void saveLog(PenguinLogEventEnum busOperation, PenguinLogEventActionEnum sqlOperation, HttpServletRequest request);

	/**
	 * 分页查询日志
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	PageInfo<Log> findLogs(int page, int limit);

	/**
	 * 删除所有日志
	 */
	void clear();
}
