package com.nan.ia.app.entities;

public class AccountItem {
	int accountItemId;
	int accountBookId;
	String category;
	double waterValue;
	String description;
	int createUserId;
	int updateUserId;
	long craeteTime;
	long updateTime;
	
	public int getAccountItemId() {
		return accountItemId;
	}
	public void setAccountItemId(int accountItemId) {
		this.accountItemId = accountItemId;
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
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public long getCraeteTime() {
		return craeteTime;
	}
	public void setCraeteTime(long craeteTime) {
		this.craeteTime = craeteTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
