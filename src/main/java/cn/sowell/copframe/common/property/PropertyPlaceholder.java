package cn.sowell.copframe.common.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import cn.sowell.copframe.exception.PropertyUndefinedException;

public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {

	private static Map<String, String> propertyMap;

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		propertyMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertyMap.put(keyStr, value);
		}
	}

	public static String getProperty(String name) {
		String value = propertyMap.get(name);
		if(value == null){
			throw new PropertyUndefinedException(name);
		}
		return value;
	}
}
