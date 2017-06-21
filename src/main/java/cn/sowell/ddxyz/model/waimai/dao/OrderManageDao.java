package cn.sowell.ddxyz.model.waimai.dao;

import java.util.List;
import java.util.Map;

import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItemAddition;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderMonthList;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderStatisticsListItem;

public interface OrderManageDao {
	/**
	 * 订单列表查询
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<OrderListItem> getOrderList(OrderListCriteria criteria,
			CommonPageInfo pageInfo);
	
	/**
	 * 更改订单状态
	 * @param orderId
	 * @param status
	 * @return
	 */
	boolean setOrderStatus(Long orderId, String status);

	/**
	 * 根据条件获得统计列表
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<OrderStatisticsListItem> statisticOrder(
			OrderStatisticsCriteria criteria, CommonPageInfo pageInfo);

	List<WaiMaiOrderItem> getAllOrderItems();

	Map<Long, String> getAllAddionCnameMap();

	void saveAddition(WaiMaiOrderItemAddition addition);

	/**
	 * 获取月统计列表
	 * @pageInfo
	 * @return
	 */
	List<OrderMonthList> OrderMonthList(CommonPageInfo pageInfo);


}
