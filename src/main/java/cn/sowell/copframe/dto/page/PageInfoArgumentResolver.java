package cn.sowell.copframe.dto.page;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import cn.sowell.copframe.dto.format.FormatUtils;


public class PageInfoArgumentResolver implements HandlerMethodArgumentResolver{
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramClass = parameter.getParameterType();
		if(PageInfo.class.isAssignableFrom(paramClass)){
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		PageInfo page = new CommonPageInfo();
		WebDataBinder binder = binderFactory.createBinder(webRequest, page, parameter.getParameterName());
		String prefix = "";
		ServletRequest request = webRequest.getNativeRequest(ServletRequest.class);
		binder.bind(new MutablePropertyValues(WebUtils.getParametersStartingWith(request , prefix )));
		if(page.getPageNo() == null){
			Integer pageNum = FormatUtils.toInteger(request.getParameter("pageNum"));
			if(pageNum != null){
				page.setPageNo(pageNum);
			}
		}
		return page;
	}

}
