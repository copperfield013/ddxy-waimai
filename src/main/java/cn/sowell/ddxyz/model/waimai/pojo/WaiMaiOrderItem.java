package cn.sowell.ddxyz.model.waimai.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_waimai_order_item")
public class WaiMaiOrderItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="drink_id")
	private Long drinkId;
	
	@Column(name="c_count")
	private Integer count;
	
	@Column(name="c_addition_ids")
	private String additionIds;
	
	@Column(name="c_heat_key")
	private Integer heatKey;
	
	@Column(name="c_sweetness_key")
	private Integer sweetnessKey;
	
	@Column(name="order_id")
	private Long orderId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDrinkId() {
		return drinkId;
	}
	public void setDrinkId(Long drinkId) {
		this.drinkId = drinkId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getAdditionIds() {
		return additionIds;
	}
	public void setAdditionIds(String additionIds) {
		this.additionIds = additionIds;
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
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
