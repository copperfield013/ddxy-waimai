package test.sowell.ddxyz.waimai;

import java.util.ArrayList;
import java.util.List;

public class AdditionItem {
	private String id;
	private String name;
	private List<String> tags = new ArrayList<String>();
	private String content;
	private Integer price;
	private List<String> classes = new ArrayList<String>();
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public List<String> getClasses() {
		return classes;
	}
	public void setClasses(List<String> classes) {
		this.classes = classes;
	}
	
	public void addClass(String classs){
		this.classes.add(classs);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public void addTag(String tag){
		this.tags.add(tag);
	}
	
	public String getTagsStr(){
		StringBuffer buffer = new StringBuffer();
		if(!tags.isEmpty()){
			for (String tag : tags) {
				buffer.append(tag + ",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}
	public String getClassStr(){
		StringBuffer buffer = new StringBuffer();
		if(!classes.isEmpty()){
			for (String clazz : classes) {
				buffer.append(clazz + ",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}
	
	public boolean hasTag() {
		return !tags.isEmpty();
	}
}
