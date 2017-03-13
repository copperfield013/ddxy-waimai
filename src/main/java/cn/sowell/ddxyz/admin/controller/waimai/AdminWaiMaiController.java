package cn.sowell.ddxyz.admin.controller.waimai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/waimai")
public class AdminWaiMaiController {
	
	@RequestMapping({"", "/"})
	public String index(){
		return "/admin/waimai/index.jsp";
	}
}
