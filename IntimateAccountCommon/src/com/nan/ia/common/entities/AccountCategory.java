package com.nan.ia.common.entities;

import java.util.Date;

public class AccountCategory {
	private int accountBookId;
	private String category;
	private String superCategory;
	private String icon;
	private Date createTime;
	private Date updateTime;
	
	public int getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSuperCategory() {
		return superCategory;
	}
	public void setSuperCategory(String superCategory) {
		this.superCategory = superCategory;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}