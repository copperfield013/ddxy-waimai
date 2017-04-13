package cn.sowell.ddxyz.model.waimai.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_waimai_order")
public class WaiMaiOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="c_code")
	private String code;
	
	@Column(name="c_receiver_id")
	private Long receiverId;
	
	@Column(name="c_receiver_name")
	private String receiverName;
	
	@Column(name="c_receiver_address")
	private String receiverAddress;
	
	@Column(name="c_receiver_contact")
	private String receiverContact;
	
	@Column(name="c_takeaway_key")
	private String takeawayKey;
	
	@Column(name="c_total_income")
	private Integer totalIncome;
	
	@Column(name="c_origin_income")
	private Integer originIncome;
	
	@Column(name="c_cup_count")
	private Integer cupCount;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Transient
	private List<WaiMaiOrderItem> items;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverContact() {
		return receiverContact;
	}
	public void setReceiverContact(String receiverContact) {
		this.receiverContact = receiverContact;
	}
	public String getTakeawayKey() {
		return takeawayKey;
	}
	public void setTakeawayKey(String takeawayKey) {
		this.takeawayKey = takeawayKey;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public List<WaiMaiOrderItem> getItems() {
		return items;
	}
	public void setItems(List<WaiMaiOrderItem> items) {
		this.items = items;
	}
	public Integer getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Integer totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Integer getCupCount() {
		return cupCount;
	}
	public void setCupCount(Integer cupCount) {
		this.cupCount = cupCount;
	}
	public Integer getOriginIncome() {
		return originIncome;
	}
	public void setOriginIncome(Integer originIncome) {
		this.originIncome = originIncome;
	}
}
