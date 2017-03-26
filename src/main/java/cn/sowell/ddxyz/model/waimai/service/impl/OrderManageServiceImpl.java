package cn.sowell.ddxyz.model.waimai.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.ddxyz.model.waimai.AdminWaiMaiConstants;
import cn.sowell.ddxyz.model.waimai.dao.OrderManageDao;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderStatisticsListItem;
import cn.sowell.ddxyz.model.waimai.service.OrderManageService;

@Service
public class OrderManageServiceImpl implements OrderManageService{

	@Resource
	OrderManageDao omDao;
	
	Logger logger = Logger.getLogger(OrderManageDao.class);
	
	@Override
	public List<OrderListItem> getOrderList(OrderListCriteria criteria,
			CommonPageInfo pageInfo) {
		return omDao.getOrderList(criteria, pageInfo);
	}

	@Override
	public boolean setOrderStatus(Long orderId, String status) {
		try {
			if(AdminWaiMaiConstants.ORDER_STATUS_CANCELED.equalsIgnoreCase(status)){
				return omDao.setOrderStatus(orderId, AdminWaiMaiConstants.ORDER_STATUS_CANCELED);
			}else if(status == null || status.isEmpty()){
				return omDao.setOrderStatus(orderId, null);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}
	
	@Override
	public List<OrderStatisticsListItem> statisticOrder(
			OrderStatisticsCriteria criteria, CommonPageInfo pageInfo) {
		return omDao.statisticOrder(criteria, pageInfo);
		
	}
	
}
