package cn.sowell.copframe.dto.page;
/**
 * 
 * <p>Title: PageInfo1</p>
 * <p>Description: </p><p>
 * 分页信息接口。用于存放分页信息以及计算一些简单的分页数据
 * </p>
 * @author Copperfield Zhang
 * @date 2016年1月26日 下午2:42:14
 */
public interface PageInfo {
	/**
	 * 获得当前的页码
	 * @return
	 */
	public Integer getPageNo();
	
	/**
	 * 设置当前的页码
	 * @param pageNo
	 */
	public void setPageNo(Integer pageNo);
	/**
	 * 获得总页数
	 * @return
	 */
	public Integer getPageSize();
	/**
	 * 设置总页数
	 * @param pageSize
	 */
	public void setPageSize(Integer pageSize);
	/**
	 * 获得数据总数
	 * @return
	 */
	public Integer getCount();
	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(Integer count);
	/**
	 * 当前是否进行分页
	 * @return
	 */
	public boolean getIsPaging();
	/**
	 * 设置当前是否分页，true为分页，false为不分页
	 * @param isPaging
	 */
	public void setIsPaging(boolean isPaging);

	/**
	 * 通过pageNo和pageSize计算分页条件下首条记录的索引(0-base)
	 * @return
	 */
	public Integer getFirstIndex();

	/**
	 * 通过pageNo和pageSize计算分页条件下最后一条记录的索引(0-base)
	 * @return
	 */
	public Integer getEndIndex();

}