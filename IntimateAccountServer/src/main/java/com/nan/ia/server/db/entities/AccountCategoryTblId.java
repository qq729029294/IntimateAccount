package com.nan.ia.server.db.entities;

// Generated 2015-6-7 4:02:23 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AccountCategoryTblId generated by hbm2java
 */
@Embeddable
public class AccountCategoryTblId implements java.io.Serializable {

	private int accountBookId;
	private String category;

	public AccountCategoryTblId() {
	}

	public AccountCategoryTblId(int accountBookId, String category) {
		this.accountBookId = accountBookId;
		this.category = category;
	}

	@Column(name = "account_book_id", nullable = false)
	public int getAccountBookId() {
		return this.accountBookId;
	}

	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}

	@Column(name = "category", nullable = false, length = 45)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AccountCategoryTblId))
			return false;
		AccountCategoryTblId castOther = (AccountCategoryTblId) other;

		return (this.getAccountBookId() == castOther.getAccountBookId())
				&& ((this.getCategory() == castOther.getCategory()) || (this
						.getCategory() != null
						&& castOther.getCategory() != null && this
						.getCategory().equals(castOther.getCategory())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAccountBookId();
		result = 37 * result
				+ (getCategory() == null ? 0 : this.getCategory().hashCode());
		return result;
	}

}
