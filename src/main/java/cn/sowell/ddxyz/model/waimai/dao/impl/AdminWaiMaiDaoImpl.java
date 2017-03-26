package cn.sowell.ddxyz.model.waimai.dao.impl;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dto.format.FormatUtils;
import cn.sowell.ddxyz.model.waimai.dao.AdminWaiMaiDao;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAdditionSize;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuGroup;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrder;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderNo;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiReceiver;

@Repository
public class AdminWaiMaiDaoImpl implements AdminWaiMaiDao {

	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<WaiMaiMenuGroup> getAvailableGroup() {
		String hql = "from WaiMaiMenuGroup g where g.disabled is null or g.disabled = 0 order by g.order asc";
		Session session = sFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		return new LinkedHashSet<WaiMaiMenuGroup>(query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WaiMaiMenuItem> getItems(Long[] groupIds) {
		String hql = "from WaiMaiMenuItem mi where mi.groupId in (:groupId) order by mi.order asc";
		Session session = sFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameterList("groupId", groupIds, LongType.INSTANCE);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WaiMaiAddition> getAllAdditions() {
		String hql = "from WaiMaiAddition a order by a.order asc";
		Session session = sFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WaiMaiAdditionSize> getAllAdditionSize() {
		String hql = "from WaiMaiAdditionSize s ";
		Session session = sFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	
	@Override
	public WaiMaiReceiver mergeReceiverInfo(WaiMaiReceiver receiver) {
		Session session = sFactory.getCurrentSession();
		String contactNumber = receiver.getContact();
		WaiMaiReceiver result = getReceiverByContact(contactNumber);
		if(result != null){
			result.setAddress(receiver.getAddress());
			result.setName(receiver.getName());
			result.setUpdateTime(new Date());
			session.update(result);
		}else{
			receiver.setCreateTime(new Date());
			session.save(receiver);
		}
		return receiver;
	}
	
	@Override
	public Long saveOrder(WaiMaiOrder wOrder) {
		Session session = sFactory.getCurrentSession();
		return (Long) session.save(wOrder);
	}
	
	@Override
	public Long saveOrderItem(WaiMaiOrderItem wItem) {
		Session session = sFactory.getCurrentSession();
		return (Long) session.save(wItem);
	}
	
	@Override
	public WaiMaiReceiver getReceiverByContact(String contact) {
		Session session = sFactory.getCurrentSession();
		String hql = "from WaiMaiReceiver w where w.contact = :contact";
		Query query = session.createQuery(hql);
		query.setString("contact", contact);
		return (WaiMaiReceiver) query.uniqueResult();
	}
	
	@Override
	public synchronized Integer getOrderNoAndInc(Date date) {
		String sql = "select n.c_order_no from t_waimai_order_no n where n.c_the_day = :theDay";
		Session session = sFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setDate("theDay", date);
		Date thisDate = new Date();
		Integer orderNo = FormatUtils.toInteger(query.uniqueResult());
		if(orderNo != null){
			String updateSQL = "update t_waimai_order_no set c_order_no = c_order_no + 1, update_time = :updateTime where c_the_day = :theDay";
			SQLQuery updateQuery = session.createSQLQuery(updateSQL);
			updateQuery.setDate("theDay", date);
			updateQuery.setTimestamp("updateTime", thisDate);
			updateQuery.executeUpdate();
		}else{
			WaiMaiOrderNo wOrderNo = new WaiMaiOrderNo();
			//因为创建之后，1就被占用了，因此从2开始
			wOrderNo.setOrderNo(2);
			wOrderNo.setTheDay(new Date());
			wOrderNo.setUpdateTime(thisDate);
			session.save(wOrderNo);
			return 1;
		}
		return orderNo;
	}
	
	@Override
	public void updateOrderCupCount(Long orderId, int orderCupCount) {
		String sql = "update t_waimai_order set c_cup_count = :cupCount";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger("cupCount", orderCupCount).executeUpdate();
	}
}
