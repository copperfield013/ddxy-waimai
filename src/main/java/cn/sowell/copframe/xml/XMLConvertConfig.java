package cn.sowell.copframe.xml;

import java.util.HashSet;
import java.util.Set;

public class XMLConvertConfig {
	//忽略所有必须字段
	boolean ignoreRequired = false;
	//忽略所有长度限定
	boolean ignoreExceedLength = false;
	
	private Set<String> requireIgnoredFieldNameSet = new HashSet<String>();
	
	/**
	 * 忽略某个必需项来进行转换
	 * @param fieldName 要忽略的字段名
	 * @return 返回自身
	 */
	public XMLConvertConfig addRequiredIgnored(String fieldName){
		requireIgnoredFieldNameSet.add(fieldName);
		return this;
	}
	/**
	 * 某个字段是否是必须的
	 * @param fieldName
	 * @return
	 */
	public boolean ignoreRequred(String fieldName){
		return ignoreRequired || requireIgnoredFieldNameSet.contains(fieldName);
	}

	public boolean isIgnoreRequired() {
		return ignoreRequired;
	}

	public void setIgnoreRequired(boolean ignoreRequired) {
		this.ignoreRequired = ignoreRequired;
	}

	public boolean isIgnoreExceedLength() {
		return ignoreExceedLength;
	}

	public void setIgnoreExceedLength(boolean ignoreExceedLength) {
		this.ignoreExceedLength = ignoreExceedLength;
	}
	
	
	
	
}
