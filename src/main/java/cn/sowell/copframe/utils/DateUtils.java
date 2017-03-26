package cn.sowell.copframe.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * <p>Title: DateUtils</p>
 * <p>Description: </p><p>
 * 时间处理工具类
 * </p>
 * @author Copperfield Zhang
 * @date 2017年3月25日 下午6:44:26
 */
public class DateUtils {
	final static DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 从字符串中分割时间范围
	 * @param dateRange 包含时间范围的字符串
	 * @param spliter 分割开始时间和结束时间的字符串
	 * @return 一个数组，包含两个元素，第一个是开始时间，第二个时间结束时间。
	 * 无论字符串是否包含两个时间，都会返回长度为2的数组，但是元素可能为null
	 */
	public static Date[] splitDateRange(String dateRange, String spliter){
		Date[] result = new Date[2];
		if(dateRange != null){
			String[] strs = dateRange.split(spliter, 2);
			try {
				result[0] = datetimeFormat.parse(strs[0]);
			} catch (ParseException e) {
				try {
					result[0] = dateFormat.parse(strs[0]);
				} catch (ParseException e1) {
				}
			}
			if(strs.length > 1){
				try {
					result[1] = datetimeFormat.parse(strs[1]);
				} catch (ParseException e) {
					try {
						result[1] = dateFormat.parse(strs[1]);
					} catch (ParseException e1) {
					}
				}
			}
		}
		return result;
	}
	/**
	 * 用符号“~”来分割开始时间和结束时间的字符串
	 * @param dateRange 时间范围字符串 
	 * @return 
	 * @see DateUtils#splitDateRange(String, String)
	 */
	public static Date[] splitDateRange(String dateRange){
		return splitDateRange(dateRange, "~");
	}
	
	/**
	 * 获得某天的零点时间对象
	 * @return
	 */
	public static Date getTheDayZero(Date theDay){
		if(theDay != null){
			Calendar c = Calendar.getInstance();
			c.setTime(theDay);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		}
		return null;
	}
	/**
	 * 根据日期，获得增加指定天数后的日期对象
	 * @param datetime 初始时间
	 * @param incDay 增加的天数，可以为负数
	 * @return 返回计算后的日期时间对象，注意只有日期改变，时间不变
	 */
	public static Date incDay(Date datetime, int incDay){
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.add(Calendar.DATE, incDay);
		return cal.getTime();
	}
	
	/**
	 * 将日期对象转换成yyyy-MM-dd HH:mm:ss的字符串
	 * @param date 日期对象
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return datetimeFormat.format(date);
	}
	
	
}
