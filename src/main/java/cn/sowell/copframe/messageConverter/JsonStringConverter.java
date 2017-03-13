package cn.sowell.copframe.messageConverter;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
/**
 * 
 * <p>Title: JsonStringConverter</p>
 * <p>Description: </p><p>
 * 用于将json请求时，Controller直接返回的字符的编码转换，
 * 默认是转换成utf-8
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月11日 下午3:06:45
 */
public class JsonStringConverter extends AbstractHttpMessageConverter<String>{

	private static Charset defaultCharset = Charset.forName("UTF-8");

	@Override
	protected boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}
	/**
	 * 获得要转换的内容编码
	 * @param contentType
	 * @return
	 */
	public static Charset getContentTypeCharset(MediaType contentType) {
		if (contentType != null && contentType.getCharset() != null) {
			return contentType.getCharset();
		}
		else {
			return defaultCharset ;
		}
	}
	
	/**
	 * 读取Controller返回的数据
	 */
	@Override
	protected String readInternal(Class<? extends String> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
		return StreamUtils.copyToString(inputMessage.getBody(), charset);
	}
	
	/**
	 * 
	 */
	@Override
	protected void writeInternal(String t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
		StreamUtils.copy(t, charset, outputMessage.getBody());
	}

}
