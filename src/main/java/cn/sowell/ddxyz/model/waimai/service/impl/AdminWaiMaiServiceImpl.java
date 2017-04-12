package cn.sowell.ddxyz.model.waimai.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.sowell.ddxyz.model.waimai.dao.AdminWaiMaiDao;
import cn.sowell.ddxyz.model.waimai.pojo.OrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.OrderRequest;
import cn.sowell.ddxyz.model.waimai.pojo.SaveOrderResult;
import cn.sowell.ddxyz.model.waimai.pojo.TakeawayPlatform;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAdditionSize;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuGroup;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrder;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItemAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiReceiver;
import cn.sowell.ddxyz.model.waimai.service.AdminWaiMaiService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class AdminWaiMaiServiceImpl implements AdminWaiMaiService{
	
	@Resource
	AdminWaiMaiDao waimaiDao;
	
	@Override
	public Set<WaiMaiMenuGroup> getAvailableGroup() {
		return waimaiDao.getAvailableGroup();
	}

	@Override
	public List<WaiMaiAddition> getAllAdditions() {
		return waimaiDao.getAllAdditions();
	}

	@Override
	public Map<Long, WaiMaiMenuItem> getAllItem(Set<WaiMaiMenuGroup> groups) {
		Long[] groupIds = new Long[groups.size()];
		int i = 0;
		for (WaiMaiMenuGroup group : groups) {
			groupIds[i++] = group.getId();
		}
		List<WaiMaiMenuItem> items = waimaiDao.getItems(groupIds);
		Map<Long, WaiMaiMenuItem> result = new LinkedHashMap<Long, WaiMaiMenuItem>();
		items.forEach(item->{
			result.put(item.getId(), item);
		});
		return result;
	}

	@Override
	public Map<Long, List<WaiMaiMenuItem>> toGroupMap(
			Collection<WaiMaiMenuItem> items) {
		Map<Long, List<WaiMaiMenuItem>> result = new LinkedHashMap<Long, List<WaiMaiMenuItem>>();
		if(items != null){
			items.forEach(item->{
				Long groupId = item.getGroupId();
				List<WaiMaiMenuItem> groupItems = result.get(groupId);
				if(groupItems == null){
					groupItems = new ArrayList<WaiMaiMenuItem>();
					result.put(groupId, groupItems);
				}
				groupItems.add(item);
			});
		}
		return result;
	}
	
	@Override
	public Map<Long, Map<Integer, WaiMaiAdditionSize>> getAllAdditionSizeMap() {
		List<WaiMaiAdditionSize> list = waimaiDao.getAllAdditionSize();
		Map<Long, Map<Integer, WaiMaiAdditionSize>> result = new LinkedHashMap<Long, Map<Integer,WaiMaiAdditionSize>>();
		list.forEach(item -> {
			Map<Integer, WaiMaiAdditionSize> map = result.get(item.getAdditionId());
			if(map == null){
				map = new LinkedHashMap<Integer, WaiMaiAdditionSize>();
				map.put(item.getSize(), item);
				result.put(item.getAdditionId(), map);
			}else{
				map.put(item.getSize(), item);
			}
		});
		return result;
	}
	
	@Override
	public JSONObject serializeAdditionSizeJson(
			Map<Long, Map<Integer, WaiMaiAdditionSize>> additionSizes) {
		JSONObject json = new JSONObject();
		additionSizes.forEach((id, sizeMap) -> {
			JSONObject mapObj = new JSONObject();
			json.put("id-" + id, mapObj);
			sizeMap.forEach((size, sizeItem) -> {
				mapObj.put("size-" + size, JSON.toJSON(sizeItem));
			});
		});
		return json;
	}
	
	@Override
	public SaveOrderResult saveOrder(OrderRequest order) throws Exception {
		Assert.notNull(order);
		SaveOrderResult result = new SaveOrderResult();
		WaiMaiReceiver receiver = order.getReceiver();
		if(receiver != null){
			List<OrderItem> itemList = order.getOrderItemList();
			if(itemList != null && !itemList.isEmpty()){
				//根据传入的收件人的信息，合并数据库中收件人的信息
				receiver = waimaiDao.mergeReceiverInfo(receiver);
				
				//构建订单信息
				WaiMaiOrder wOrder = new WaiMaiOrder();
				result.setOrder(wOrder);
				//生成订单号并且放到对象中
				wOrder.setCode(generateOrderCode());
				wOrder.setCreateTime(new Date());
				wOrder.setReceiverId(receiver.getId());
				wOrder.setReceiverName(receiver.getName());
				wOrder.setReceiverContact(receiver.getContact());
				wOrder.setReceiverAddress(receiver.getAddress());
				//外卖平台
				TakeawayPlatform takeaway = order.getTakeaway();
				//订单总价
				wOrder.setTotalIncome(order.getTotalIncome());
				if(takeaway != null){
					wOrder.setTakeawayKey(takeaway.getKey());
				}
				
				int orderCupCount = 0;
				for (OrderItem item : itemList) {
					if(item.getDrinkId() != null && item.getCount() != null){
						orderCupCount += item.getCount();
					}
				}
				wOrder.setCupCount(orderCupCount);
				//执行保存当前订单
				Long orderId = waimaiDao.saveOrder(wOrder);
				
				//保存订单条目
				List<WaiMaiOrderItem> items = new ArrayList<WaiMaiOrderItem>();
				for (OrderItem item : itemList) {
					if(item.getDrinkId() != null){
						WaiMaiOrderItem wItem = new WaiMaiOrderItem();
						wItem.setDrinkId(item.getDrinkId());
						wItem.setCount(item.getCount());
						orderCupCount += wItem.getCount();
						wItem.setHeatKey(item.getHeatKey());
						wItem.setSweetnessKey(item.getSweetnessKey());
						wItem.setOrderId(orderId);
						wItem.setIncome(item.getIncome());
						wItem.setDrinkName(item.getDrinkName());
						//将所有加料的主键连成字符串
						//String additionsChain = CollectionUtils.toChain(item.getAdditionIds());
						//wItem.setAdditionIds(additionsChain);
						items.add(wItem);
					}else{
						throw new Exception("存在条目没有传入饮料的drinkId");
					}
				}
				//保存所有订单条目
				items.forEach(wItem -> {
					Long orderItemId = waimaiDao.saveOrderItem(wItem);
					for(OrderItem item : itemList){
						for(int i=0; i<item.getAdditions().size(); i++){
							item.getAdditions().get(i).setItemId(orderItemId);
							//TODO 保存itemAddition
							waimaiDao.saveWaiMaiOrderItemAddition(item.getAdditions().get(i));
						}
					}
				});
			}else{
				throw new Exception("没有传入订单明细");
			}
		}else{
			throw new Exception("保存失败，没有收件人信息");
		}
		return result;
	}
	
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	DecimalFormat noFormat = new DecimalFormat("00000");
	/**
	 * 生成订单号的方法
	 * @return
	 */
	private String generateOrderCode() {
		Integer orderNo = getOrderNoAndInc();
		if(orderNo != null){
			String code = dateFormat.format(new Date());
			code += noFormat.format(orderNo);
			return code;
		}
		return null;
	}

	@Override
	public WaiMaiReceiver getReceiverByContact(String contact) {
		return waimaiDao.getReceiverByContact(contact);
	}
	
	@Override
	public Integer getOrderNoAndInc() {
		return waimaiDao.getOrderNoAndInc(new Date());
	}

	@Override
	public WaiMaiOrder getOrderById(String id) {
		return waimaiDao.getOrderById(id);
	}

	@Override
	public List<WaiMaiOrderItem> getOrderItemsByOrderId(Long orderId) {
		return waimaiDao.getOrderItemsByOrderId(orderId);
	}

	@Override
	public List<WaiMaiOrderItemAddition> getWMOrderItemAdditionByItemId(Long itemId) {
		return waimaiDao.getWMOrderItemAdditionByItemId(itemId);
	}
	
}
