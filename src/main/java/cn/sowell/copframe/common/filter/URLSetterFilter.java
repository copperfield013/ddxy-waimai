package cn.sowell.copframe.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.sowell.copframe.utils.HttpRequestUtils;

public class URLSetterFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(request instanceof HttpServletRequest){
			
			String requestType = ((HttpServletRequest) request).getHeader("X-Requested-With");
			if(requestType == null){
				request.setAttribute("__url", HttpRequestUtils.getCompleteURL((HttpServletRequest) request));
			}else if("XMLHttpRequest".equals(requestType)){
				
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
