/**
 * @ClassName:     AccountRecordDailyStatistics.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月31日 
 */

package com.nan.ia.app.entities;

import java.util.Date;

public class AccountRecordMonthGroup {
	Date date;
	double expend;
	double income;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getExpend() {
		return expend;
	}
	public void setExpend(double expend) {
		this.expend = expend;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
}
