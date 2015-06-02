package com.nan.ia.common.entities;

import java.io.Serializable;
import java.util.Date;

public class AccountRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int accountRecordId;
	private int accountBookId;
	private String category;
	private double waterValue;
	private String description;
	private Date recordTime;
	private int recordUserId;
	private Date createTime;
	private Date updateTime;
	public int getAccountRecordId() {
		return accountRecordId;
	}
	public void setAccountRecordId(int accountRecordId) {
		this.accountRecordId = accountRecordId;
	}
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
	public int getRecordUserId() {
		return recordUserId;
	}
	public void setRecordUserId(int recordUserId) {
		this.recordUserId = recordUserId;
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
