package cn.sowell.copframe.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

public class CommonRequestAttributeSetInterceptor implements WebRequestInterceptor {

	
	
	@Override
	public void preHandle(WebRequest req) throws Exception {
		if(req instanceof NativeWebRequest){
			HttpServletRequest request = ((NativeWebRequest) req).getNativeRequest(HttpServletRequest.class);
			String basePath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
			request.setAttribute("basePath", basePath);
		}
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
