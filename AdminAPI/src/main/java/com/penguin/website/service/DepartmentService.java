package com.penguin.website.service;

import com.penguin.website.model.domain.Departments;
import com.penguin.website.model.domain.DepartmetsDesc;
import com.penguin.website.model.domain.President;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : DepartmentService  //类名
 * @Description : 部门service  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 16:26  //时间
 */
public interface DepartmentService {

    List<Departments> getAll();

    DepartmetsDesc getDepartmentId(Integer departmentId);

    List<President> getPresidentAll(Map<String,Object> map);

    President getPresidentById(Integer presidentId);
}
