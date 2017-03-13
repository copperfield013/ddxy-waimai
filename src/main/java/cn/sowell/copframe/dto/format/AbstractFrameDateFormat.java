package cn.sowell.copframe.dto.format;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * <p>Title: AbstractFrameFormat</p>
 * <p>Description: </p><p>
 * 框架默认日期时间格式工具类。
 * 通过集成这个抽象类可以实现不同格式的日期时间格式
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月13日 下午2:32:48
 */
public class AbstractFrameDateFormat implements FrameDateFormat{
	//默认日期格式化对象
	protected DateFormat dateFormat;
	//默认时间格式化对象
	protected DateFormat timeFormat;
	//默认日期时间格式化对象
	protected DateFormat dateTimeFormat;

	/**
	 * 自定义格式化对象
	 * @param dateFormat
	 * @param timeFormat
	 * @param dateTimeFormat
	 */
	protected AbstractFrameDateFormat(DateFormat dateFormat, DateFormat timeFormat,
			DateFormat dateTimeFormat) {
		super();
		this.dateFormat = dateFormat;
		this.timeFormat = timeFormat;
		this.dateTimeFormat = dateTimeFormat;
	}

	@Override
	public String format(Date date, String pattern) {
		//通过传入的字符串来构造一个format对象
		DateFormat format  = null;
		try {
			format = new SimpleDateFormat(pattern);
		} catch (Exception e) {
		}
		return this.format(date, format);
	}

	@Override
	public String format(Date date, DateFormat format) {
		if(date != null && format != null){
			try {
				return format.format(date);
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	public String formatDate(Date date) {
		return this.format(date, this.dateFormat);
	}

	@Override
	public String formatDateTime(Date date) {
		return this.format(date, this.dateTimeFormat);
	}

	@Override
	public String formatTime(Date date) {
		return this.format(date, this.timeFormat);
	}

	@Override
	public Date parse(String dateStr) {
		Date result = null;
		if(dateStr != null){
			try {
				result = this.dateTimeFormat.parse(dateStr);
			} catch (Exception e) {
				try {
					result = this.dateFormat.parse(dateStr);
				} catch (Exception e1) {
					try {
						result = this.timeFormat.parse(dateStr);
					} catch (ParseException e2) {
					}
				}
			}
		}
		return result;
	}

	@Override
	public Date parse(String dateStr, DateFormat format) {
		if(dateStr != null && format != null){
			try {
				return format.parse(dateStr);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	@Override
	public Date parse(String dateStr, String formatStr) {
		DateFormat format = null;
		try {
			format = new SimpleDateFormat(formatStr);
		} catch (Exception e) {
		}
		return this.parse(dateStr, format);
	}

}