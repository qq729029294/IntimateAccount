package com.nan.ia.server.db.entities;

// Generated 2015-6-4 16:01:45 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UserTbl generated by hbm2java
 */
@Entity
@Table(name = "user_tbl", catalog = "intimate_account_db")
public class UserTbl implements java.io.Serializable {

	private Integer userId;
	private String nickname;
	private String avatar;
	private String description;
	private Date createTime;
	private Date updateTime;

	public UserTbl() {
	}

	public UserTbl(String nickname, Date createTime, Date updateTime) {
		this.nickname = nickname;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public UserTbl(String nickname, String avatar, String description,
			Date createTime, Date updateTime) {
		this.nickname = nickname;
		this.avatar = avatar;
		this.description = description;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "nickname", nullable = false, length = 45)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "avatar")
	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false, length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
