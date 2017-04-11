package cn.sowell.ddxyz.model.waimai.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import cn.sowell.ddxyz.model.waimai.pojo.OrderRequest;
import cn.sowell.ddxyz.model.waimai.pojo.SaveOrderResult;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAdditionSize;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuGroup;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrder;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItemAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiReceiver;

public interface AdminWaiMaiService {
	/**
	 * 获得所有可用的菜单集合
	 * @return
	 */
	Set<WaiMaiMenuGroup> getAvailableGroup();

	/**
	 * 根据菜单集合获得所有菜单项，key是菜单项的主键
	 * @param groups
	 * @return
	 */
	Map<Long, WaiMaiMenuItem> getAllItem(Set<WaiMaiMenuGroup> groups);
	
	/**
	 * 将菜单选项按照集合进行分类
	 * @param itemMap
	 * @return
	 */
	Map<Long, List<WaiMaiMenuItem>> toGroupMap(Collection<WaiMaiMenuItem> items);
	
	
	/**
	 * 获得所有添加料选项
	 * @return
	 */
	List<WaiMaiAddition> getAllAdditions();

	/**
	 * 获得所有添加料时的尺寸选项
	 * @return
	 */
	Map<Long, Map<Integer, WaiMaiAdditionSize>> getAllAdditionSizeMap();
	
	/**
	 * 
	 * @param additionSizes
	 * @return
	 */
	JSONObject serializeAdditionSizeJson(Map<Long, Map<Integer, WaiMaiAdditionSize>> additionSizes);
	
	/**
	 * 保存订单到数据库
	 * @param order
	 * @throws Exception 
	 */
	SaveOrderResult saveOrder(OrderRequest order) throws Exception;

	/**
	 * 根据联系号码获得收货人的信息
	 * @param contact
	 * @return
	 */
	WaiMaiReceiver getReceiverByContact(String contact);

	/**
	 * 获得最新可用的订单号
	 * @return 
	 */
	Integer getOrderNoAndInc();
	
	/**
	 * 通过订单id获取外卖订单
	 * @param id
	 * @return
	 */
	WaiMaiOrder getOrderById(String id);
	
	/**
	 * 通过订单id获取订单内容
	 * @param orderId
	 * @return
	 */
	List<WaiMaiOrderItem> getOrderItemsByOrderId(Long orderId);
	
	List<WaiMaiOrderItemAddition> getWMOrderItemAdditionByItemId(Long itemId);
}
