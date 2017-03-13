package cn.sowell.copframe.dao.deferedQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.transform.ResultTransformer;


public abstract class ColumnMapResultTransformer<T> implements ResultTransformer {

	/**
	 * 
	 */
	static final long serialVersionUID = -4789768421017913745L;
	private MapperFunction<T> func ;
	
	public ColumnMapResultTransformer() {
		final ColumnMapResultTransformer<T> _this = this;
		this.func = new MapperFunction<T>() {

			@Override
			public T build(SimpleMapWrapper mapWrapper) {
				return _this.build(mapWrapper);
			}
		};
	}
	
	protected abstract T build(SimpleMapWrapper mapWrapper);
	
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < aliases.length; i++) {
			map.put(aliases[i].toLowerCase(), tuple[i]);
		}
		return this.func.apply(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List transformList(List collection) {
		return new ArrayList(collection);
	}
}
