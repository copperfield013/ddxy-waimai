package cn.sowell.copframe.dto.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * <p>Title: FrameDateFormat</p>
 * <p>Description: </p><p>
 * 框架级的日期格式接口
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月13日 下午2:26:42
 */
public interface FrameDateFormat {

	/**
	 * 使用特定格式的字符串来格式化时间对象。
	 * 如果传入的pattern是一个错误的格式，那么最后会返回一个null值
	 * @param date 要进行格式化的日期对象
	 * @param pattern 日期格式
	 * @return 如果格式错误，那么就返回一个null值
	 * @see {@link #format(Date, DateFormat)}
	 */
	public String format(Date date, String pattern);

	/**
	 * 自定义日期格式化
	 * @param date 要进行格式化的日期对象
	 * @param format 日期格式对象,可以是{@link SimpleDateFormat}对象
	 * @return 如果传入的format对象为null,那么返回一个null
	 */
	public String format(Date date, DateFormat format);

	/**
	 * 将Date对象格式化按照默认日期格式来进行格式化
	 * @param date
	 * @return
	 */
	public String formatDate(Date date);

	/**
	 * 将Date对象按照默认时间格式来格式化
	 * @param date
	 * @return
	 */
	public String formatDateTime(Date date);

	/**
	 * 将Date对象按照默认日期时间格式来格式化
	 * @param date
	 * @return
	 */
	public String formatTime(Date date);

	/**
	 * 将字符串按照当前的日期时间格式转换为时间对象<br/>
	 * <h1>转化步骤为</h1>
	 * <ul>
	 * 	<li>按照默认时间格式来转换，如果无法解析，那么用时间格式来解析</li>
	 * 	<li>如果默认时间格式无法解析，那么按照日期时间格式来解析</li>
	 * 	<li>如果默认日期时间格式无法解析，那么最后返回null</li>
	 * <ul>
	 * <h1>
	 * 由此可见，无论这个字符串是日期还是时间或者是日期时间，只要是根据默认格式，
	 * 那么就可以将其解析为日期对象
	 * </h1>
	 * @param dateStr
	 * @return
	 */
	public Date parse(String dateStr);

	/**
	 * 将字符串按照特定日期格式化对象来解析成日期对象 
	 * 如果传入的日期对象为null，那么最后返回的对象也是null
	 * @param dateStr 要进行格式化的时间字符串
	 * @param format 日期格式化对象{@link SimpleDateFormat}
	 * @return
	 */
	public Date parse(String dateStr, DateFormat format);

	/**
	 * 将字符串按照特定日期格式来解析成日期对象
	 * 传入的日期格式有误，那么最后返回的对象也为null
	 * @param dateStr 要进行解析的时间字符串
	 * @param formatStr 解析格式字符串，会构造成{@link SimpleDateFormat}对象
	 * @return 
	 * @see {@link #parse(String, DateFormat)}
	 */
	public Date parse(String dateStr, String formatStr);

}