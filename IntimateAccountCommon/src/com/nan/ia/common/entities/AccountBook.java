package com.nan.ia.common.entities;

import java.io.Serializable;
import java.util.Date;

public class AccountBook implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer accountBookId;
	private String name;
	private String description;
	private int createUserId;
	private Date createTime;
	private Date updateTime;
	public Integer getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(Integer accountBookId) {
		this.accountBookId = accountBookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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