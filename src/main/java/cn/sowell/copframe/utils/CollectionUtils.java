package cn.sowell.copframe.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.util.Assert;

import cn.sowell.copframe.dto.format.FormatUtils;
import cn.sowell.copframe.dto.pojo.NormalPojo;

public class CollectionUtils {
	/**
	 * 将集合转换为map
	 * @param items 集合对象，不能为null
	 * @param keyGetter 每个元素对象获取其key的方法
	 * @return {@link LinkedHashMap}对象
	 */
	public static <V, T> Map<T, V> toMap(Collection<V> items, Function<V, T> keyGetter){
		Assert.notNull(items);
		Assert.notNull(keyGetter);
		Map<T, V> map = new LinkedHashMap<T, V>();
		items.forEach(item->{
			T key = keyGetter.apply(item);
			if(key != null){
				map.put(key, item);
			}
		});
		return map;
	}
	
	public static <T extends NormalPojo> Map<Long, T> toMap(Collection<T> items){
		Function<T, Long> func = item -> {
			return item.getId();
		};
		return toMap(items, func);
	}
	
	public static <T extends NormalPojo> Map<String, T> toMap(Collection<T> items, String keyPrefix){
		Function<T, String> func = item -> {
			return keyPrefix + item.getId();
		};
		return toMap(items, func);
	}

	public static String toChain(Collection<?> source, String spliter) {
		if(source == null){
			return null;
		}else{
			StringBuffer buffer = new StringBuffer();
			String _spliter = spliter == null? "": spliter;
			source.forEach(item -> {
				buffer.append(FormatUtils.toString(item) + _spliter);
			});
			if(buffer.length() > 0){
				buffer.delete(buffer.length() - spliter.length() - 1, buffer.length());
			}
			return buffer.toString();
		}
	}

	public static String toChain(Collection<?> source) {
		return toChain(source, ",");
	}
	
}
