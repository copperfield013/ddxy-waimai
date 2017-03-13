package cn.sowell.copframe.dao.deferedQuery;

import java.util.Map;

public abstract class MapperFunction<T> implements Function<Map<String, Object>, T>{
	
	final public T apply(Map<String,Object> input) {
		SimpleMapWrapper mapWrapper = new SimpleMapWrapper(input);
		return build(mapWrapper);
	};
	public abstract T build(SimpleMapWrapper mapWrapper);
}
