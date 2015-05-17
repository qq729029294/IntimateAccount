package com.nan.ia.common.entities;

import java.util.Date;

public class AccountItem {
	private Integer accountItemId;
	private int accountBookId;
	private int accountCategoryId;
	private Integer waterValue;
	private String description;
	private int createUserId;
	private Date createTime;
	private Date updateTime;
	public Integer getAccountItemId() {
		return accountItemId;
	}
	public void setAccountItemId(Integer accountItemId) {
		this.accountItemId = accountItemId;
	}
	public int getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}
	public int getAccountCategoryId() {
		return accountCategoryId;
	}
	public void setAccountCategoryId(int accountCategoryId) {
		this.accountCategoryId = accountCategoryId;
	}
	public Integer getWaterValue() {
		return waterValue;
	}
	public void setWaterValue(Integer waterValue) {
		this.waterValue = waterValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
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
