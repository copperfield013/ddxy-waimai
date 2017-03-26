package cn.sowell.ddxyz.model.waimai.pojo.item;

import java.util.Date;

public class OrderStatisticsListItem {
	private Date theDay;
	private Integer income;
	private Integer orderCount;
	private Integer cupCount;
	public Date getTheDay() {
		return theDay;
	}
	public void setTheDay(Date theDay) {
		this.theDay = theDay;
	}
	public Integer getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public Integer getCupCount() {
		return cupCount;
	}
	public void setCupCount(Integer cupCount) {
		this.cupCount = cupCount;
	}
}
