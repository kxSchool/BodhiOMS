package com.penguin.website.model.dto;

import com.penguin.website.model.domain.Article;

import java.util.List;

public class JsonResult {
	private boolean flag;
	private String msg;
	private Object data;

	public JsonResult(boolean flag, List<Article> adServices) {
		super();
	}

	public JsonResult(boolean flag, String msg) {
		super();
		this.flag = flag;
		this.msg = msg;
	}

	public JsonResult(boolean flag, String msg, Object data) {
		super();
		this.flag = flag;
		this.msg = msg;
		this.data = data;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
