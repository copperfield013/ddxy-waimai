package cn.sowell.ddxyz.model.waimai.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_waimai_order_item_addition")
public class WaiMaiOrderItemAddition {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="addition_id")
	private Long additionId;
	
	@Column(name="c_name")
	private String name;
	
	@Column(name="item_id")
	private Long itemId;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdditionId() {
		return additionId;
	}

	public void setAdditionId(Long additionId) {
		this.additionId = additionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}
