package cn.sowell.ddxyz.model.waimai.pojo.criteria;

import java.util.Date;

import cn.sowell.copframe.utils.DateUtils;

public class OrderStatisticsCriteria {
	private String dateRange;
	private Date startDate;
	private Date endDate;
	
	public String getDateRange() {
		if(dateRange == null){
			if(startDate != null || endDate != null){
				StringBuffer buffer = new StringBuffer();
				if(startDate != null){
					buffer.append(DateUtils.formatDateTime(startDate));
				}
				if(endDate != null){
					buffer.append("~");
					buffer.append(DateUtils.formatDateTime(endDate));
				}
				return buffer.toString();
			}
		}
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
		Date[] array = DateUtils.splitDateRange(dateRange);
		this.setStartDate(array[0]);
		this.setEndDate(array[1]);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
