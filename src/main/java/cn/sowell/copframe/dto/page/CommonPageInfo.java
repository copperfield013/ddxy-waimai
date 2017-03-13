package cn.sowell.copframe.dto.page;

/**
 * 
 * <p>Title: PageInfo</p>
 * <p>Description: </p><p>
 * 分页信息类
 * </p>
 * @author Copperfield Zhang
 * @date 2015年12月8日 上午9:17:59
 */
public class CommonPageInfo implements PageInfo {
	private Integer pageNo;
	private Integer pageSize;
	private Integer count;
	private boolean isPaging;
	/**
	 * 默认构造函数
	 */
	public CommonPageInfo() {
		this(1, 10, null, true);
	}
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param count
	 */
	public CommonPageInfo(Integer pageNo, Integer pageSize, Integer count, boolean isPaging) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.count = count;
		this.isPaging = isPaging;
	}
	
	/**
	 * 构造分页对象,通过这个构造函数构造的分页对象的count属性为null,isPaging为true
	 * @param pageNo
	 * @param pageSize
	 */
	public CommonPageInfo(Integer pageNo, Integer pageSize) {
		this(pageNo, pageSize, null, true);
	}
	public CommonPageInfo(boolean isPaging) {
		this(null, null, null, isPaging);
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#getPageNo()
	 */
	@Override
	public Integer getPageNo() {
		return pageNo;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#setPageNo(java.lang.Integer)
	 */
	@Override
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#getPageSize()
	 */
	@Override
	public Integer getPageSize() {
		return pageSize;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#setPageSize(java.lang.Integer)
	 */
	@Override
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#getCount()
	 */
	@Override
	public Integer getCount() {
		return count;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#setCount(java.lang.Integer)
	 */
	@Override
	public void setCount(Integer count) {
		this.count = count;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#getIsPaging()
	 */
	@Override
	public boolean getIsPaging() {
		return isPaging;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#setIsPaging(boolean)
	 */
	@Override
	public void setIsPaging(boolean isPaging) {
		this.isPaging = isPaging;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#getFirstIndex()
	 */
	@Override
	public Integer getFirstIndex(){
		if(this.getPageNo() != null && this.getPageSize() != null){
			return (this.getPageNo() - 1) * this.getPageSize();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.sowell.of.dto.PageInfo1#getEndIndex()
	 */
	@Override
	public Integer getEndIndex(){
		Integer firstIndex = this.getFirstIndex();
		if(firstIndex != null){
			return firstIndex + this.getPageSize() - 1;
		}
		return null;
	}
	
}
