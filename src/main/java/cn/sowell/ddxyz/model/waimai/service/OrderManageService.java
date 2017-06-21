package cn.sowell.ddxyz.model.waimai.service;

import java.util.List;

import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderMonthList;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderStatisticsListItem;

public interface OrderManageService {

	/**
	 * 订单查询
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<OrderListItem> getOrderList(OrderListCriteria criteria,
			CommonPageInfo pageInfo);

	/**
	 * 取消订单
	 * @param orderId 订单id
	 * @param status 
	 * @return 取消成功，返回true，失败返回false
	 */
	boolean setOrderStatus(Long orderId, String status);

	/**
	 * 获得统计列表
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<OrderStatisticsListItem> statisticOrder(
			OrderStatisticsCriteria criteria, CommonPageInfo pageInfo);
	
	
	
	void additionChange();
	
	/*
	 * 获得月统计列表
	 * 
	 */
	List<OrderMonthList> monthList(
			CommonPageInfo pageInfo);

}
