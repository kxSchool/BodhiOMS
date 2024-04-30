package com.penguin.website.mapper.master.custom;

import com.penguin.website.model.domain.Departments;
import com.penguin.website.model.domain.DepartmetsDesc;
import com.penguin.website.model.domain.President;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : DepartmentMapper  //类名
 * @Description : 部门mappper  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 16:13  //时间
 */
public interface DepartmentMapper {

    List<Departments> getAll();

    DepartmetsDesc getDepartmentId(@Param("departmentId") Integer departmentId);

    List<President> getPresidentAll(@Param("map") Map<String,Object> map);

    President getPresidentById(@Param("presidentId") Integer presidentId);
}
