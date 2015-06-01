package com.nan.ia.common.entities;

import java.util.Date;

public class AccountRecord {
	private Integer accountRecordId;
	private int accountBookId;
	private String category;
	private double waterValue;
	private String description;
	private Date recordTime;
	private int createUserId;
	private Date createTime;
	private Date updateTime;
	
	public Integer getAccountRecordId() {
		return accountRecordId;
	}
	public void setAccountRecordId(Integer accountRecordId) {
		this.accountRecordId = accountRecordId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}
	public double getWaterValue() {
		return waterValue;
	}
	public void setWaterValue(double waterValue) {
		this.waterValue = waterValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
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
