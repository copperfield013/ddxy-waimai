package cn.sowell.ddxyz.model.waimai.pojo;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {
	private Long drinkId;
	private Integer count;
	private List<WaiMaiOrderItemAddition> additions = new ArrayList<WaiMaiOrderItemAddition>();
	private Integer heatKey;
	private Integer sweetnessKey;
	private Integer income;
	
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	public List<WaiMaiOrderItemAddition> getAdditions() {
		return additions;
	}
	public void setAdditions(List<WaiMaiOrderItemAddition> additions) {
		this.additions = additions;
	}
	
	public Long getDrinkId() {
		return drinkId;
	}
	public void setDrinkId(Long drinkId) {
		this.drinkId = drinkId;
	}

	public Integer getHeatKey() {
		return heatKey;
	}
	public void setHeatKey(Integer heatKey) {
		this.heatKey = heatKey;
	}
	public Integer getSweetnessKey() {
		return sweetnessKey;
	}
	public void setSweetnessKey(Integer sweetnessKey) {
		this.sweetnessKey = sweetnessKey;
	}
	public Integer getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	
}
