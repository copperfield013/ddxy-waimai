package cn.sowell.ddxyz.model.waimai.dao;

import java.util.List;

import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
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


}
