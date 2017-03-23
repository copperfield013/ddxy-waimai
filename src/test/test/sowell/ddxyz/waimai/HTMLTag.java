package test.sowell.ddxyz.waimai;

import java.util.LinkedHashMap;
import java.util.Map;

public class HTMLTag {
	private String tagName;
	private Map<String, String> attrMap = new LinkedHashMap<String, String>();
	private String content;
	
	public HTMLTag(String tagName) {
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void putAttr(String attrName, Object value){
		attrMap.put(attrName, String.valueOf(value));
	}
	public Map<String, String> getAttrMap() {
		return attrMap;
	}
	public void setAttrMap(Map<String, String> attrMap) {
		this.attrMap = attrMap;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + tagName);
		this.attrMap.forEach((attrName, value) ->{
			buffer.append(" " + attrName + "=\"" + value + "\"");
		});
		if(content != null & !content.isEmpty()){
			buffer.append(" >");
			buffer.append(content);
			buffer.append("</" + tagName + ">");
		}else{
			buffer.append(" />");
		}
		return buffer.toString();
	}
	
}
