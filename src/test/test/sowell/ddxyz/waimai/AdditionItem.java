package test.sowell.ddxyz.waimai;

import java.util.ArrayList;
import java.util.List;

public class AdditionItem {
	private String content;
	private Integer price;
	private String data;
	private List<String> classes = new ArrayList<String>();
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
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
}
