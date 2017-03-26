package cn.sowell.ddxyz.model.waimai.pojo;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {
	private Long drinkId;
	private Integer count;
	private List<Long> additionIds = new ArrayList<Long>();
	private Integer heatKey;
	private Integer sweetnessKey;
	private Integer income;
	
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<Long> getAdditionIds() {
		return additionIds;
	}
	public void setAdditionIds(List<Long> additionIds) {
		this.additionIds = additionIds;
	}
	public Long getDrinkId() {
		return drinkId;
	}
	public void setDrinkId(Long drinkId) {
		this.drinkId = drinkId;
	}
	public void addAdditionId(Long additionId) {
		this.additionIds.add(additionId);
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
