package cn.sowell.ddxyz.admin.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sowell.ddxyz.model.main.AdminMainConstants;

@Controller
@RequestMapping("/admin")
public class AdminMainController {

	
	@RequestMapping("/login")
	public String login(@RequestParam(name="error",required=false) String error, Model model){
		model.addAttribute("error", error);
		model.addAttribute("errorMap", AdminMainConstants.ERROR_CODE_MAP);
		return "/admin/common/login.jsp";
	}
	
	@RequestMapping("/logout")
	public String logout(){
		return "/admin/common/logout.jsp";
	}
	
	@RequestMapping({"/", ""})
	public String index(){
		return "/admin/common/index.jsp";
	}
}
