package cn.sowell.ddxyz.model.main;

import java.util.HashMap;
import java.util.Map;

public interface AdminMainConstants {
	@SuppressWarnings("serial")
	Map<String, String> ERROR_CODE_MAP = new HashMap<String, String>(){
		{
			put("1", "用户名或密码错误");
		}
	};
}
