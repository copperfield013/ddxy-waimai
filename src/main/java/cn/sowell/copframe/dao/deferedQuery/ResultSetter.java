package cn.sowell.copframe.dao.deferedQuery;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * hibernate的SQLQuery查询的结果转换器
 * @author Copperfield
 * @date 2016年10月12日 上午8:54:40
 * @param <T>
 */
public class ResultSetter<T> {
	
	
	private Function<Map<String, Object>, T> setFunc;
	
	/**
	 * 创建一个查询结果映射器，参数是映射的函数对象。
	 * 函数对象可以用封装的{@link MapperFunction}对象，根据字段的别名来获得字段值
	 * @param func
	 * @see {@link MapperFunction}
	 */
	public ResultSetter(Function<Map<String, Object>, T> func){
		this.setFunc = func;
	}
	
	public ResultSetter(MapperFunction<T> func){
		this.setFunc = func;
	}
	
	public T transform(Object[] tuple, String[] aliases){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < aliases.length; i++) {
			map.put(aliases[i].toLowerCase(), tuple[i]);
		}
		return this.setFunc.apply(map);
	}
}
