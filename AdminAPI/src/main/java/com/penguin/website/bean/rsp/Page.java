
package com.penguin.website.bean.rsp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 简单分页模型
 *
 * @author hubin
 * @since 2018-06-09
 */
public class Page<T> implements Serializable {


    /**
     * 查询数据列表
     */
    @ApiModelProperty(value = "数据集")
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    @ApiModelProperty(value = "总条数")
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    @ApiModelProperty(value = "每页显示数")
    private long size = 10;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private long current = 1;



    public Page() {
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
