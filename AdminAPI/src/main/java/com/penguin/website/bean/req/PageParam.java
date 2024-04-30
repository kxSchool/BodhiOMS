package com.penguin.website.bean.req;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class PageParam implements Serializable {
    private static final long serialVersionUID = 3521503870509395289L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_CURRENT_PAGE = 1;

    @ApiModelProperty(value = "页数")
    private Integer page;

    @ApiModelProperty(value = "条数")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
