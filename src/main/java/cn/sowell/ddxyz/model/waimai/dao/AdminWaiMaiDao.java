package cn.sowell.ddxyz.model.waimai.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAdditionSize;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuGroup;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrder;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiReceiver;

public interface AdminWaiMaiDao {
	/**
	 * 获得所有可用的菜单组
	 * @return
	 */
	Set<WaiMaiMenuGroup> getAvailableGroup();
	/**
	 * 查询菜单组范围内的所有菜单项选项
	 * @param groupIds
	 * @return
	 */
	List<WaiMaiMenuItem> getItems(Long[] groupIds);
	/**
	 * 获得所有添加料的选项
	 * @return
	 */
	List<WaiMaiAddition> getAllAdditions();
	/**
	 * 
	 * @return
	 */
	List<WaiMaiAdditionSize> getAllAdditionSize();
	
	/**
	 * 当收件人存在的时候，在数据库中更新收件人的信息<br/>
	 * 当没有传入收件人信息的id或者当收件人不存在的时候，在数据库中添加一条收件人的信息
	 * @param receiver
	 * @return 更新后的收件人信息（一般用于获得自生成主键）
	 */
	WaiMaiReceiver mergeReceiverInfo(WaiMaiReceiver receiver);
	
	/**
	 * 保存订单信息
	 * @param wOrder
	 * @return
	 */
	Long saveOrder(WaiMaiOrder wOrder);
	/**
	 * 保存一条订单条目信息
	 * @param wItem
	 */
	Long saveOrderItem(WaiMaiOrderItem wItem);
	
	/**
	 * 根据联系号码获得收件人的信息
	 * @param contact
	 * @return
	 */
	WaiMaiReceiver getReceiverByContact(String contact);
	/**
	 * 获得指定日期最新的订单序号
	 * @param date
	 * @return
	 */
	Integer getOrderNoAndInc(Date date);
	
	
	
	
}
