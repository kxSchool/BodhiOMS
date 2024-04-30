package com.penguin.website.service.impl;

import com.penguin.website.mapper.master.custom.DepartmentMapper;
import com.penguin.website.model.domain.Departments;
import com.penguin.website.model.domain.DepartmetsDesc;
import com.penguin.website.model.domain.President;
import com.penguin.website.service.DepartmentService;
import com.penguin.website.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : DepartmentServiceImpl  //类名
 * @Description : 部门service实现类  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 16:27  //时间
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Departments> getAll() {

        List<Departments> lists = departmentMapper.getAll();
        List<Departments> departments = TreeUtil.listToTree(lists);
        return departments;
    }

    @Override
    public DepartmetsDesc getDepartmentId(Integer departmentId) {
        return departmentMapper.getDepartmentId(departmentId);
    }

    @Override
    public List<President> getPresidentAll(Map<String, Object> map) {
        return departmentMapper.getPresidentAll(map);
    }

    @Override
    public President getPresidentById(Integer presidentId) {
        return departmentMapper.getPresidentById(presidentId);
    }
}
