package com.wz.modules.kk.entity;

import java.io.Serializable;
import java.util.List;

public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String total;
	private String page;
	private String totalPage;
	private List<T> dataList;
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	
}
