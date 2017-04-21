package cn.sowell.ddxyz.admin.controller.waimai;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.NoticeType;
import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.copframe.utils.DateUtils;
import cn.sowell.ddxyz.model.waimai.AdminWaiMaiConstants;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrder;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiOrderItemAddition;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderListCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.criteria.OrderStatisticsCriteria;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderListItem;
import cn.sowell.ddxyz.model.waimai.pojo.item.OrderStatisticsListItem;
import cn.sowell.ddxyz.model.waimai.service.AdminWaiMaiService;
import cn.sowell.ddxyz.model.waimai.service.OrderManageService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Controller
@RequestMapping("/admin/order-manage")
public class OrderManageController {
	@Resource
	OrderManageService omService;
	
	@Resource
	AdminWaiMaiService adminWaiMaiService;
	
	@RequestMapping({"", "/"})
	public String index(){
		return AdminWaiMaiConstants.ROOT_ORDER_MANAGE + "/order_manage.jsp";
	}
	
	@RequestMapping("/order-list")
	public String orderList(OrderListCriteria criteria, CommonPageInfo pageInfo, Model model){
		if(criteria.getTimeRange() == null){
			criteria.setStartTime(DateUtils.getTheDayZero(new Date()));
			criteria.setEndTime(DateUtils.incDay(criteria.getStartTime(), 1));
		}
		List<OrderListItem> items = omService.getOrderList(criteria, pageInfo);
		model.addAttribute("items", items);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("criteria", criteria);
		return AdminWaiMaiConstants.ROOT_ORDER_MANAGE + "/order_list.jsp";
	}
	
	@RequestMapping("/order-statistics")
	public String orderStatistics(OrderStatisticsCriteria criteria, CommonPageInfo pageInfo, Model model){
		
		List<OrderStatisticsListItem> statisticsList = omService.statisticOrder(criteria, pageInfo);
		model.addAttribute("statisticsList", statisticsList);
		model.addAttribute("criteria", criteria);
		model.addAttribute("pageInfo", pageInfo);
		return AdminWaiMaiConstants.ROOT_ORDER_MANAGE + "/order_statistics.jsp";
	}
	
	@RequestMapping("/order-detail")
	public String orderDetail(String id, Model model){
		WaiMaiOrder order = adminWaiMaiService.getOrderById(id);
		if(order != null){
			List<WaiMaiOrderItem> list = adminWaiMaiService.getOrderItemsByOrderId(order.getId());
			if(list != null && list.size() > 0){
				Map<Long, String> additionsNameMap = new LinkedHashMap<Long, String>();
				for(WaiMaiOrderItem wmOrderItem : list){
					List<WaiMaiOrderItemAddition> additions = adminWaiMaiService.getWMOrderItemAdditionByItemId(wmOrderItem.getId());
					if(additions != null && additions.size() > 0){
						String additionsName = "";
						for(WaiMaiOrderItemAddition wmOrderItemAddition : additions){
							additionsName += wmOrderItemAddition.getName() + ",";
						}
						additionsName = additionsName.substring(0, additionsName.length()-1);
						additionsNameMap.put(wmOrderItem.getId(), additionsName);
					}
				}
				model.addAttribute("additionsNameMap", additionsNameMap);
			}
			model.addAttribute("orderItems", list);
		}
		model.addAttribute("order", order);
		model.addAttribute("heatMap", AdminWaiMaiConstants.HEAT_MAP);
		model.addAttribute("sweetnessMap", AdminWaiMaiConstants.SWEETNESS_MAP);
		return AdminWaiMaiConstants.ROOT_ORDER_MANAGE + "/order_detail.jsp";
	}
	
	
	
	@ResponseBody
	@RequestMapping("/setOrderStatus")
	public String setOrderStatus(
			@RequestParam("id") Long orderId,
			@RequestParam("status") String status){
		boolean cancelResult = omService.setOrderStatus(orderId, status);
		if(cancelResult){
			return JSON.toJSONString(AjaxPageResponse.REFRESH_LOCAL("操作成功"), SerializerFeature.WriteEnumUsingToString);
		}else{
			AjaxPageResponse response = new AjaxPageResponse();
			response.setNotice("操作失败");
			response.setNoticeType(NoticeType.ERROR);
			return JSON.toJSONString(response, SerializerFeature.WriteEnumUsingToString);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getPrintOrder")
	public String prePrintOrder(@RequestParam("id") String orderId){
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		WaiMaiOrder order = adminWaiMaiService.getOrderById(orderId);
		//String result = "";
		JSONObject jo = new JSONObject();
		if(order != null){
			jo.put("orderCode", order.getCode());
			jo.put("orderTime", df.format(order.getCreateTime()));
			jo.put("receiverName", order.getReceiverName());
			jo.put("receiverAddress", order.getReceiverAddress());
			jo.put("receiverContact", order.getReceiverContact());
			jo.put("totalCount", order.getCupCount());
			jo.put("totalIncome", order.getTotalIncome());
			jo.put("originIncome", order.getOriginIncome());
			List<WaiMaiOrderItem> list = adminWaiMaiService.getOrderItemsByOrderId(order.getId());
			if(list != null && list.size() > 0){
				JSONArray jDrinks = new JSONArray();
				jo.put("drinks", jDrinks);
				for(WaiMaiOrderItem wmOrderItem : list){
					JSONObject jItem = new JSONObject();
					jItem.put("name", wmOrderItem.getDrinkName());
					jItem.put("sweetness", AdminWaiMaiConstants.SWEETNESS_MAP.get(wmOrderItem.getSweetnessKey()));
					jItem.put("heat", AdminWaiMaiConstants.HEAT_MAP.get(wmOrderItem.getHeatKey()));
					jItem.put("price", wmOrderItem.getIncome());
					jItem.put("cupCount", wmOrderItem.getCount());
					jDrinks.add(jItem);
							
					List<WaiMaiOrderItemAddition> additions = adminWaiMaiService.getWMOrderItemAdditionByItemId(wmOrderItem.getId());
					if(additions != null && additions.size() > 0){
						String additionsName = "";
						for(WaiMaiOrderItemAddition wmOrderItemAddition : additions){
							additionsName += wmOrderItemAddition.getName() + "、";
						}
						additionsName = additionsName.substring(0, additionsName.length()-1);
						jItem.put("additions", additionsName);
					}
				}
			}
		}
		return jo.toString();
	}
	
}
