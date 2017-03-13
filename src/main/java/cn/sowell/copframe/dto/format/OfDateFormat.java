package cn.sowell.copframe.dto.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * 
 * <p>Title: OfDateFormat</p>
 * <p>Description: </p><p>
 * 框架默认的日期格式工具类
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月9日 上午9:20:45
 */
public class OfDateFormat extends AbstractFrameDateFormat {
	//默认日期格式
	private static final DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//默认日期时间格式
	private static final DateFormat defaultDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//默认时间格式
	private static final DateFormat defaultTimeFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * 系统默认构造函数，使用默认格式来格式化时间对象
	 */
	public OfDateFormat() {
		super(defaultDateFormat, defaultTimeFormat, defaultDateTimeFormat);
	}
	
	
}
