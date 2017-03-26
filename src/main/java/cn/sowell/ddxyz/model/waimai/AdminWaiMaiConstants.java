package cn.sowell.ddxyz.model.waimai;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public interface AdminWaiMaiConstants {
	/**
	 * 默认的未生成的订单号
	 */
	final String defaultOrderCode = "wait-for-generate";
	
	final Map<Integer, String> SIZE_MAP = new HashMap<Integer, String>(){
		{
			put(2, "中杯");
			put(3, "大杯");
		}
	};
	
	final Map<Integer, String> HEAT_MAP = new LinkedHashMap<Integer, String>(){
		{
			put(1, "少冰");
			put(2, "去冰");
			put(3, "常温");
			put(4, "温");
			put(5, "热");
		}
	};
	
	final Map<Integer, String> SWEETNESS_MAP = new LinkedHashMap<Integer, String>(){
		{
			put(1, "9分甜");
			put(2, "7分甜");
			put(3, "5分甜");
			put(4, "3分甜");
			put(5, "1分甜");
			put(6, "无糖");
		}
	};

	final String ROOT_ORDER_MANAGE = "/admin/order_manage";

	/**
	 * 取消订单状态
	 */
	final String ORDER_STATUS_CANCELED = "canceled";
	
	
	
}
