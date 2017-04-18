package cn.sowell.ddxyz.model.waimai.pojo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import cn.sowell.copframe.dto.format.FormatUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class OrderRequest {
	private String code;
	private Integer totalIncome;
	private Integer originIncome;
	private WaiMaiReceiver receiver;
	private List<OrderItem> orderItemList = new ArrayList<OrderItem>();
	private TakeawayPlatform takeaway;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public WaiMaiReceiver getReceiver() {
		return receiver;
	}
	public void setReceiver(WaiMaiReceiver receiver) {
		this.receiver = receiver;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public void addOrderItem(OrderItem item) {
		this.orderItemList.add(item);
	}
	
	public TakeawayPlatform getTakeaway() {
		return takeaway;
	}
	public void setTakeaway(TakeawayPlatform takeaway) {
		this.takeaway = takeaway;
	}
	
	public static OrderRequest fromJSON(JSONObject json) {
		Assert.notNull(json);
		OrderRequest order = new OrderRequest();
		//订单号
		order.setCode(json.getString("code"));
		order.setTotalIncome(json.getInteger("totalIncome"));
		order.setOriginIncome(json.getInteger("originIncome"));
		//收货信息
		WaiMaiReceiver receiver = new WaiMaiReceiver();
		JSONObject rJson = json.getJSONObject("receiver");
		if(rJson != null){
			receiver.setId(FormatUtils.toLong(rJson.get("id")));
			receiver.setName(rJson.getString("name"));
			receiver.setAddress(rJson.getString("address"));
			receiver.setContact(rJson.getString("contact"));
			order.setReceiver(receiver);
		}
		//订单条目
		JSONArray iJson = json.getJSONArray("items");
		if(iJson != null){
			iJson.forEach(e -> {
				JSONObject iData = (JSONObject) e;
				OrderItem item = new OrderItem();
				List<WaiMaiOrderItemAddition> list = new ArrayList<WaiMaiOrderItemAddition>();
				item.setDrinkId(FormatUtils.toLong(iData.get("drinkId")));
				item.setCount(iData.getInteger("count"));
				item.setDrinkName(iData.getString("drinkName"));
				JSONArray iAdditions = iData.getJSONArray("additions");
				iAdditions.forEach(addi -> {
					JSONObject aData = (JSONObject) addi;
					WaiMaiOrderItemAddition itemAddition = new WaiMaiOrderItemAddition();
					itemAddition.setAdditionId(FormatUtils.toLong(aData.get("id")));
					itemAddition.setName(aData.getString("name"));
					list.add(itemAddition);
					item.setAdditions(list);
					//item.addAdditionId(FormatUtils.toLong(aData.get("id")));
				});
				item.setHeatKey(FormatUtils.toInteger(iData.get("heatKey")));
				item.setSweetnessKey(FormatUtils.toInteger(iData.get("sweetnessKey")));
				item.setIncome(iData.getInteger("income"));
				order.addOrderItem(item);
			});
		}
		
		//外卖平台
		JSONObject takeawayJson = json.getJSONObject("takeaway");
		if(takeawayJson != null){
			TakeawayPlatform takeaway = new TakeawayPlatform();
			takeaway.setKey(takeawayJson.getString("key"));
			order.setTakeaway(takeaway);
		}
		return order;
	}
	public Integer getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Integer totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Integer getOriginIncome() {
		return originIncome;
	}
	public void setOriginIncome(Integer originIncome) {
		this.originIncome = originIncome;
	}
}
