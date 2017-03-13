package cn.sowell.copframe.utils;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.log4j.Logger;

public class TextUtils {
	static Logger logger = Logger.getLogger(TextUtils.class);
	
	
	/**
	 * 传入多个字符串对象，取第一个不为空的字符串
	 * @param texts
	 * @return
	 */
	public static String orText(String...texts){
		for (String text : texts) {
			if(text != null && !text.isEmpty()){
				return text;
			}
		}
		return null;
	}
	
	
	/**
	  * 去除字符串首尾的特定字符序列
	  * @param str 要修整的字符串
	  * @param regex 检查的字符序列
	  * @return 新字符串
	  */
	 public static String trim(String str, String...regex){
		if(str == null){
			return null;
		}
		String ret = new String(str);
		if(regex.length == 0){
			return ret.trim();
		}
		Boolean hasChanged = true;
		while(hasChanged){
			hasChanged = false;
			for (String r : regex) {
				if(ret.startsWith(r)){
					ret = ret.substring(r.length());
					hasChanged = true;
				}
				if(ret.endsWith(r)){
					ret = ret.substring(0, ret.lastIndexOf(r));
					hasChanged = true;
				}
			}
		}
		return ret;
	 }
	 
	 /**
	  * 二次解码，编码为UTF-8
	  * @param string
	  * @return
	  */
	 public static String dblDecode(String string){
		 return dblDecode(string, "UTF-8");
	 }
	 /**
	  * 二次解码，编码为code
	  * @param string
	  * @param code
	  * @return
	  */
	 public static String dblDecode(String string, String code){
		 try {
			return URLDecoder.decode(URLDecoder.decode(string, code), code);
		} catch (Exception e) {
			return string;
		}
	 }
	 
	 /**
	  * 判断某个字符串是否是数字，如果是整数或者小数，那么就返回true
	  * @param str
	  * @return
	  */
	 public static Boolean isNumeric(String str){
		 String regexInteger = "^-?\\d+$";
		 String regexDouble = "^(-?\\d+)(\\.\\d+)?$";
		 if(!str.matches(regexInteger)){
			 return str.matches(regexDouble);
		 }
		 return true;
	 }
	 
	 
	 private static String randomStr(int length, char[] scope){
		 if(length < 0){
			 throw new IllegalArgumentException("length参数必须>=0");
		 }
		 String ret = "";
		 char[] uuid = new char[length];
		 for(int i = 0 ; i < length; i++){
			 uuid[i] = scope[(int)(Math.random() * scope.length)];
		 }
		 ret = String.valueOf(uuid);
		 return ret;
	 }
	 /**
	  * 生成随机字符串，字符串的元素范围由scope确定
	  * @param length
	  * @param scope
	  * @return
	  */
	 public static String randomStr(int length, CharacterScope scope){
		 return randomStr(length, scope.toString().toCharArray());
	 }
	 /**
	  * 获取随机的uuid，长度为length，位数为radix<br/>
	  * @param length 最小值为0。为0的情况下返回一个空字符串
	  * @param radix 值域为[1,62]。如果是1~10间的值，那么随机串将从[0,radix-1]中随机获取。
	  * 如果值是16，那么随机串将从[0,9]|[a,f]中随机获取（就是十六进制）。
	  * 如果值是36，那么随机串将从[0,9]|[a,z]中随机获取
	  * 如果值是62，那么随机串将从[0,9]|[A,Z]|[a,z]中随机获取
	  * @return 随机串
	  */
	 public static String randomStr(int length, int radix){
		 switch (radix) {
		case 16:
			return randomStr(length, CharacterScope.Hex);
		case 36:
			return randomStr(length, CharacterScope.Word);
		case 62:
			return randomStr(length, CharacterScope.Regular);
		default:
			if(radix > 0 && radix < 10){
				char[] scope = new char[radix];
				for(int i=0; i < radix; i++){scope[i] = (char)(i + 48);}
				randomStr(length, scope);
			}
			return "";
		}
	 }
	 
	 
	 /**
	  * 生成唯一的UUID，并去除原始UUID中的“-”
	  * @return
	  * @see UUID
	  */
	 public static String uuid(){
		 return UUID.randomUUID().toString().replaceAll("-", "");
	 }
	 
	 
	 public static enum CharacterScope {
			Number("0123456789"),
			Majuscule("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
			Minuscule("abcdefghijklmnopqrstuvwxyz"),
			Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
			Regular("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
			Hex("0123456789abcdef"),
			Word("0123456789abcdefghijklmnopqrstuvwxyz")
			;
			private String scope;
			private CharacterScope(String scope){
				this.scope = scope;
			}
			@Override
			public String toString() {
				return this.scope;
			}
		}

	 
	/**
	 * md5加密
	 * @param string
	 * @return
	 */
	public static String md5Encode(String string) {
		;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	/**
	 * sha1加密
	 * @param upperCase
	 * @return
	 */
	public static String sha1Encode(String upperCase) {
		SHA1 sha1 = new SHA1();
		return sha1.Digest(upperCase);
	}
	
}
