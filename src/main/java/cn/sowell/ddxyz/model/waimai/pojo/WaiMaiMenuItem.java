package cn.sowell.ddxyz.model.waimai.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_waimai_menu_item")
public class WaiMaiMenuItem {
	@Id
	private Long id;
	
	@Column(name="c_name")
	private String name;
	
	@Column(name="c_price")
	private Integer price;
	
	@Column(name="c_addition_price")
	private Integer additionPrice;
	
	@Column(name="c_size")
	private Integer size;
	
	@Column(name="c_view")
	private String view;
	
	@Column(name="c_data")
	private String data;
	
	@Column(name="c_tags")
	private String tags;
	
	@Column(name="c_order")
	private Integer order;
	
	@Column(name="group_id")
	private Long groupId;
	
	@Column(name="c_disabled")
	private Integer disabled;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
	private Date updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Integer getAdditionPrice() {
		return additionPrice;
	}
	public void setAdditionPrice(Integer additionPrice) {
		this.additionPrice = additionPrice;
	}
}
