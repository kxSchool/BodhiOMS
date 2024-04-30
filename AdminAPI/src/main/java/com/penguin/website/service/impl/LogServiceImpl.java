package com.penguin.website.service.impl;

import java.util.List;
import java.util.Objects;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.penguin.website.bean.model.PenguinLogThreadLocal;
import com.penguin.website.model.enums.PenguinLogEventActionEnum;
import com.penguin.website.model.enums.PenguinLogEventEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.penguin.website.mapper.master.custom.LogMapperCustom;
import com.penguin.website.mapper.master.generator.LogMapper;
import com.penguin.website.model.domain.Log;
import com.penguin.website.model.domain.LogExample;
import com.penguin.website.service.LogService;

import javax.servlet.http.HttpServletRequest;


@Service
@Transactional(rollbackFor = RuntimeException.class)
public class LogServiceImpl implements LogService {

    private static final String LOGS_CACHE_NAME = "logs";

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private LogMapperCustom logMapperCustom;

    @Override
    @CacheEvict(value = LOGS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void save(Log log) {
        logMapper.insert(log);
    }

    @Override
    @Cacheable(value = LOGS_CACHE_NAME, key = "'findLogs'+#page+#limit")
    public PageInfo<Log> findLogs(int page, int limit) {
        PageHelper.startPage(page, limit);
        LogExample example = new LogExample();
        example.setOrderByClause("log_id DESC ");
        List<Log> logs = logMapper.selectByExample(example);
        return new PageInfo<>(logs);
    }

    @Override
    public void saveLog(PenguinLogEventEnum busOperation, PenguinLogEventActionEnum sqlOperation, HttpServletRequest request) {
        String ip = "";
        String title = "";
        if (Objects.isNull(request)) {
            ip = "127.0.0.1";
            title = PenguinLogEventEnum.formatLogTitle("system", busOperation, sqlOperation);
        } else {
            title = PenguinLogEventEnum.formatLogTitle(PenguinLogThreadLocal.getUserId(), busOperation, sqlOperation);
            ip = ServletUtil.getClientIP(request);
        }
        this.save(new Log(title, PenguinLogThreadLocal.getLogContentAndRemove(), ip, DateUtil.date()));
    }

    @Override
    @CacheEvict(value = LOGS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void clear() {
        logMapperCustom.clear();
    }

}
