package com.penguin.website.model.domain;

import java.util.List;

/**
 * @ClassName : Departments  //类名
 * @Description : 部门  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 16:05  //时间
 */
public class Departments {
    private Integer did;
    private String departmentName;
    private Integer parentId;
    private Integer disable;

    private List<Departments> children;

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public List<Departments> getChildren() {
        return children;
    }

    public void setChildren(List<Departments> children) {
        this.children = children;
    }
}
