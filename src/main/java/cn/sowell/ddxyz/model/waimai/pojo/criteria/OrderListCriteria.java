package cn.sowell.ddxyz.model.waimai.pojo.criteria;

import java.util.Date;

import cn.sowell.copframe.utils.DateUtils;

public class OrderListCriteria {
	private String keyword;
	/**
	 * 订单号
	 */
	private String orderCode;
	/**
	 * 联系人
	 */
	private String contactName;
	/**
	 * 联系号码
	 */
	private String contactNumber;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 时间范围字符串（用于从页面上传入，并且返回到页面中）
	 */
	private String timeRange;
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTimeRange() {
		if(timeRange == null){
			if(startTime != null || endTime != null){
				StringBuffer buffer = new StringBuffer();
				if(startTime != null){
					buffer.append(DateUtils.formatDateTime(startTime));
				}
				if(endTime != null){
					buffer.append("~");
					buffer.append(DateUtils.formatDateTime(endTime));
				}
				return buffer.toString();
			}
		}
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
		Date[] array = DateUtils.splitDateRange(timeRange);
		this.setStartTime(array[0]);
		this.setEndTime(array[1]);
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
}
