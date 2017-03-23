package test.sowell.ddxyz.waimai;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
	private String id;
	private String name;
	private Integer price;
	private Integer size;
	private String content;
	private String data;
	private List<String> tagList = new ArrayList<String>();
	private boolean disabled;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	
	public void addTag(String tag){
		this.tagList.add(tag);
	}
	
	public String getTagsStr(){
		StringBuffer buffer = new StringBuffer();
		if(!tagList.isEmpty()){
			for (String tag : tagList) {
				buffer.append(tag + ",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}
	public boolean hasTag() {
		return !tagList.isEmpty();
	}
	
}
