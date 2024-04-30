package com.penguin.website.model.domain;

/**
 * @ClassName : DepartmetsDesc  //类名
 * @Description : 部门描述  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 16:07  //时间
 */
public class DepartmetsDesc {

    private Integer did;
    private String departmentsconten;
    private String departmentsimg;

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getDepartmentsconten() {
        return departmentsconten;
    }

    public void setDepartmentsconten(String departmentsconten) {
        this.departmentsconten = departmentsconten;
    }

    public String getDepartmentsimg() {
        return departmentsimg;
    }

    public void setDepartmentsimg(String departmentsimg) {
        this.departmentsimg = departmentsimg;
    }

    @Override
    public String toString() {
        return "DepartmetsDesc{" +
                "did=" + did +
                ", departmentsconten='" + departmentsconten + '\'' +
                ", departmentsimg='" + departmentsimg + '\'' +
                '}';
    }
}
