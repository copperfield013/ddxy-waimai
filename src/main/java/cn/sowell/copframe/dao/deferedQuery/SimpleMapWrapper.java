package cn.sowell.copframe.dao.deferedQuery;

import java.util.Date;
import java.util.Map;

import cn.sowell.copframe.dto.format.FormatUtils;
/**
 * 封装map对象，并能通过{@link FormatUtils}的方法简单地将对象转换成指定的的类型
 * @author Copperfield
 * @date 2016年11月7日 上午11:15:27
 */
public class SimpleMapWrapper {
	Map<String, Object> map;
	
	public SimpleMapWrapper(Map<String, Object> map) {
		this.map = map;
	}
	
	public Object get(String column){
		return map.get(column);
	}
	
	public Map<String, Object> getMap(){
		return this.map;
	}
	
	public String getString(String column){
		return FormatUtils.toString(map.get(column));
	}
	
	public Long getLong(String column){
		return FormatUtils.toLong(map.get(column));
	}
	
	public Integer getInteger(String column){
		return FormatUtils.toInteger(map.get(column));
	}
	
	public Double toDouble(String column){
		return FormatUtils.toDouble(map.get(column));
	}
	
	public Date getDate(String column){
		return (Date) get(column);
	}
	
}
