package cn.sowell.ddxyz.model.waimai.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.ddxyz.model.waimai.AdminWaiMaiConstants;
import cn.sowell.ddxyz.model.waimai.dao.OrderManageDao;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItemAddition;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderMonthList;
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
	
	@Override
	public void additionChange() {
		List<WaiMaiOrderItem> orderItems = omDao.getAllOrderItems();
		Map<Long, String> additionCname = omDao.getAllAddionCnameMap();
		for (WaiMaiOrderItem item : orderItems) {
			String additionStr = item.getAdditionIds();
			if(additionStr != null){
				String[] split = additionStr.split(",");
				for (String additionId : split) {
					if(!additionId.isEmpty()){
						Long aid = Long.valueOf(additionId);
						WaiMaiOrderItemAddition addition = new WaiMaiOrderItemAddition();
						addition.setItemId(item.getId());
						addition.setAdditionId(aid);
						addition.setName(additionCname.get(aid));
						omDao.saveAddition(addition);
					}
				}
			}
		}
		
	}

	@Override
	public List<OrderMonthList> monthList( CommonPageInfo pageInfo) {
		return omDao.OrderMonthList( pageInfo);
		
	}
	
}
