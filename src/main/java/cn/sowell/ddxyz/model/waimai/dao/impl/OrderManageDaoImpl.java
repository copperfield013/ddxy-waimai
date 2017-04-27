package cn.sowell.ddxyz.model.waimai.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.sowell.copframe.dao.deferedQuery.ColumnMapResultTransformer;
import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.DeferedParamSnippet;
import cn.sowell.copframe.dao.deferedQuery.SimpleMapWrapper;
import cn.sowell.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.format.FormatUtils;
import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.copframe.utils.DateUtils;
import cn.sowell.ddxyz.model.waimai.AdminWaiMaiConstants;
import cn.sowell.ddxyz.model.waimai.dao.OrderManageDao;
import cn.sowell.ddxyz.model.waimai.pojo.OrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItemAddition;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderStatisticsListItem;

@Repository
public class OrderManageDaoImpl implements OrderManageDao{

	@Resource
	SessionFactory sFactory;
	
	
	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	public List<OrderListItem> getOrderList(OrderListCriteria criteria,
			CommonPageInfo pageInfo) {
		Session session = sFactory.getCurrentSession();
		String sql = 
				" SELECT" +
				"	o.id, o.c_code, o.c_receiver_name, o.c_receiver_contact, o.c_status, "
				+ "o.c_receiver_address, order_count.c_count, o.create_time" +
				" FROM" +
				"	t_waimai_order o" +
				" LEFT JOIN (" +
				"	SELECT" +
				"		item.order_id," +
				"		SUM(item.c_count) c_count" +
				"	FROM" +
				"		t_waimai_order_item item" +
				"	GROUP BY" +
				"		item.order_id" +
				") order_count" +
				" on o.id = order_count.order_id  @mainWhere" +
				" order by o.create_time desc";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(StringUtils.hasText(criteria.getOrderCode())){
			mainWhere.append("and o.c_code like :orderCode");
			dQuery.setParam("orderCode", "%" + criteria.getOrderCode() + "%");
		}
		if(StringUtils.hasText(criteria.getContactName())){
			mainWhere.append("and o.c_receiver_name like :receiverName");
			dQuery.setParam("receiverName", "%" + criteria.getContactName() + "%");
		}
		if(StringUtils.hasText(criteria.getContactNumber())){
			mainWhere.append("and o.c_receiver_contact like :receiverContact");
			dQuery.setParam("receiverContact", "%" + criteria.getContactNumber() + "%");
		}
		if(criteria.getStartTime() != null){
			mainWhere.append("and o.create_time >= :startTime");
			dQuery.setParam("startTime", criteria.getStartTime(), StandardBasicTypes.TIMESTAMP);
		}
		if(criteria.getEndTime() != null){
			mainWhere.append("and o.create_time <= :endTime");
			dQuery.setParam("endTime", criteria.getEndTime(), StandardBasicTypes.TIMESTAMP);
		}
		
		SQLQuery countQuery = dQuery.createSQLQuery(session, false, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			pageInfo.setCount(count);
			SQLQuery listQuery = dQuery.createSQLQuery(session, false, null);
			QueryUtils.setPagingParamWithCriteria(listQuery, pageInfo);
			listQuery.setResultTransformer(new ColumnMapResultTransformer<OrderListItem>() {

				@Override
				protected OrderListItem build(SimpleMapWrapper mapWrapper) {
					OrderListItem item = new OrderListItem();
					item.setOrderId(mapWrapper.getLong("id"));
					item.setCode(mapWrapper.getString("c_code"));
					item.setReceiverName(mapWrapper.getString("c_receiver_name"));
					item.setReceiverContact(mapWrapper.getString("c_receiver_contact"));
					item.setReceiverAddress(mapWrapper.getString("c_receiver_address"));
					item.setCupCount(mapWrapper.getInteger("c_count"));
					item.setStatus(mapWrapper.getString("c_status"));
					item.setCreateTime(mapWrapper.getDate("create_time"));
					return item;
				}
			});
			return listQuery.list();
		}
		return new ArrayList<OrderListItem>();
	}
	@Override
	public boolean setOrderStatus(Long orderId, String status) {
		String sql = "update t_waimai_order set c_status = :status where id = :orderId";
		Session session = sFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		int updated = 
				query.setString("status", status)
					.setLong("orderId", orderId)
					.executeUpdate()
				;
		if(updated > 0){
			return true;
		}
		return false;
		
	}
	
	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	public List<OrderStatisticsListItem> statisticOrder(
			OrderStatisticsCriteria criteria, CommonPageInfo pageInfo) {
		String sql = 
				" SELECT" +
				"	odr.order_date order_date," +
				"	sum(odr.c_total_income) total_income," +
				"	count(odr.id) order_count," +
				"	sum(odr.c_cup_count) cup_count" +
				" FROM" +
				"	(" +
				"		SELECT" +
				"			date(o.create_time) order_date," +
				"			o.*" +
				"		FROM" +
				"			t_waimai_order o" +
				"       @mainWhere" +
				"	) odr" +
				" GROUP BY odr.order_date" + 
				" order by odr.order_date desc";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(criteria.getStartDate() != null){
			mainWhere.append("and o.create_time >= :startTime ");
			dQuery.setParam("startTime", DateUtils.getTheDayZero(criteria.getStartDate()), StandardBasicTypes.DATE);
		}
		if(criteria.getEndDate() != null){
			mainWhere.append("and o.create_time <= :endTime ");
			dQuery.setParam("endTime", DateUtils.incDay(DateUtils.getTheDayZero(criteria.getEndDate()), 1), StandardBasicTypes.DATE);
		}
		mainWhere.append("and (o.c_status is null or o.c_status <> :canceledStatus)");
		dQuery.setParam("canceledStatus", AdminWaiMaiConstants.ORDER_STATUS_CANCELED);
		Session session = sFactory.getCurrentSession();
		SQLQuery countQuery = dQuery.createSQLQuery(session , false, new WrapForCountFunction());
		
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			SQLQuery query = dQuery.createSQLQuery(session, false, null);
			query.setResultTransformer(new ColumnMapResultTransformer<OrderStatisticsListItem>() {

				@Override
				protected OrderStatisticsListItem build(
						SimpleMapWrapper mapWrapper) {
					OrderStatisticsListItem item = new OrderStatisticsListItem();
					item.setTheDay(mapWrapper.getDate("order_date"));
					item.setIncome(mapWrapper.getInteger("total_income"));
					item.setOrderCount(mapWrapper.getInteger("order_count"));
					item.setCupCount(mapWrapper.getInteger("cup_count"));
					return item;
				}
			});
			return query.list();
		}
		return new ArrayList<OrderStatisticsListItem>();
	}
	
	@Override
	public List<WaiMaiOrderItem> getAllOrderItems() {
		String hql = "from WaiMaiOrderItem";
		return sFactory.getCurrentSession().createQuery(hql).list();
	}
	
	@Override
	public Map<Long, String> getAllAddionCnameMap() {
		String hql = "from WaiMaiAddition";
		List<WaiMaiAddition> list = sFactory.getCurrentSession().createQuery(hql).list();
		Map<Long, String> map = new LinkedHashMap<Long, String>();
		for (WaiMaiAddition addition : list) {
			map.put(addition.getId(), addition.getName());
		}
		return map;
	}
	
	
	@Override
	public void saveAddition(WaiMaiOrderItemAddition addition) {
		sFactory.getCurrentSession().save(addition);
	}

}
