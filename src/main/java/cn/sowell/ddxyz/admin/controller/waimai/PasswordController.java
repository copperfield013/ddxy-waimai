package cn.sowell.ddxyz.admin.controller.waimai;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.ddxyz.model.waimai.service.PasswordService;

@Controller
@RequestMapping("/admin/password")
public class PasswordController {
	
	@Resource
	PasswordService passwordService;
	
	@ResponseBody
	@RequestMapping(value="/validate", headers="Accept=application/json")
	public String check(@RequestParam("password") String password){
		boolean bool = passwordService.checkPassword(password);
		if(bool){
//			return JSON.toJSONString(JsonResponse.REFRESH_LOCAL("YES"), SerializerFeature.WriteEnumUsingToString);
			return "{\"status\": \"YES\"}";
		}else{
			//return JSON.toJSONString(JsonResponse.REFRESH_LOCAL("NO"), SerializerFeature.WriteEnumUsingToString);
			return "{\"status\":\"NO\"}";
		}
		
	}
}
