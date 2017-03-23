package cn.sowell.ddxyz.admin.controller.waimai;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.ddxyz.model.waimai.AdminWaiMaiConstants;
import cn.sowell.ddxyz.model.waimai.pojo.OrderRequest;
import cn.sowell.ddxyz.model.waimai.pojo.SaveOrderResult;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAddition;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiAdditionSize;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuGroup;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiMenuItem;
import cn.sowell.ddxyz.model.waimai.pojo.WaiMaiReceiver;
import cn.sowell.ddxyz.model.waimai.service.AdminWaiMaiService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/admin/waimai")
public class AdminWaiMaiController {
	
	@Resource
	AdminWaiMaiService waimaiService;
	
	Logger logger = Logger.getLogger(AdminWaiMaiController.class);
	
	
	@RequestMapping({"", "/"})
	public String index(Model model){
		Set<WaiMaiMenuGroup> groups = waimaiService.getAvailableGroup();
		Map<Long, WaiMaiMenuItem> itemMap = waimaiService.getAllItem(groups);
		Map<Long, List<WaiMaiMenuItem>> groupItemMap = waimaiService.toGroupMap(itemMap.values());
		List<WaiMaiAddition> additions = waimaiService.getAllAdditions();
		Map<String, WaiMaiAddition> additionMap = CollectionUtils.toMap(additions, "id-");
		
		Map<Long, Map<Integer,WaiMaiAdditionSize>> additionSizes = waimaiService.getAllAdditionSizeMap();
		JSONObject additionSizeJson = waimaiService.serializeAdditionSizeJson(additionSizes);
		
		
		model.addAttribute("itemMapJson", JSON.toJSON(itemMap));
		model.addAttribute("additionMapJson", JSON.toJSON(additionMap));
		model.addAttribute("additionSizeJson", additionSizeJson);
		
		model.addAttribute("groups", groups);
		model.addAttribute("itemMap", groupItemMap);
		model.addAttribute("sizeMap", AdminWaiMaiConstants.SIZE_MAP);
		model.addAttribute("heatMap", AdminWaiMaiConstants.HEAT_MAP);
		model.addAttribute("sweetnessMap", AdminWaiMaiConstants.SWEETNESS_MAP);
		model.addAttribute("additions", additions);
		return "/admin/waimai/index.jsp";
	}
	
	@ResponseBody
	@RequestMapping(value="/submitOrder", headers="Accept=application/json")
	public String submitOrder(HttpServletRequest request){
		JSONObject jo = new JSONObject();
		try {
			BufferedReader reader = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line;
			while((line = reader.readLine()) != null){
				buffer.append(line);
			}
			JSONObject json = JSON.parseObject(buffer.toString());
			OrderRequest order = OrderRequest.fromJSON(json);
			try {
				SaveOrderResult result = waimaiService.saveOrder(order);
				if(result.getOrder() != null){
					jo.put("orderCode", result.getOrder().getCode());
				}
				jo.put("status", "suc");
			} catch (Exception e) {
				jo.put("status", "err");
				logger.error(e);
			}
		} catch (IOException e) {
			jo.put("status", "err");
			logger.error(e);
		}
		
		return jo.toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping("/loadReceiver")
	public String loadReceiver(String contact){
		JSONObject jo = new JSONObject();
		if(StringUtils.hasText(contact)){
			WaiMaiReceiver receiver = waimaiService.getReceiverByContact(contact);
			if(receiver != null){
				jo.put("status", "suc");
				jo.put("receiver", JSON.toJSON(receiver));
			}
		}else{
			jo.put("status", "err");
		}
		return jo.toJSONString();
	}
	
	@RequestMapping("/showPrint")
	public String showPrint(){
		return "/admin/waimai/print-area.jsp";
	}
	
	@ResponseBody
	@RequestMapping(value="/polling", headers="Accept=application/json")
	public String polling(){
		JSONObject json = new JSONObject();
		json.put("status", "suc");
		return json.toString();
	}
	
	
}
