package cn.sowell.copframe.dto.ajax;
/**
 * 
 * <p>Title: NoticeType</p>
 * <p>Description: </p><p>
 * 响应的通知类型
 * </p>
 * @author Copperfield Zhang
 * @date 2017年1月17日 上午9:26:44
 */
public enum NoticeType {
	ERROR("error"),
	WARNING("warning"),
	SUC("success"),
	INFO("info")
	;
	
	private String noticeType;
	NoticeType(String noticeType){
		this.noticeType = noticeType;
	}
	
	@Override
	public String toString() {
		return noticeType;
	}
}
