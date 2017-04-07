package cn.sowell.ddxyz.model.waimai.dao;

public interface PasswordDao {
	
	/**
	 * 判断password是否一致
	 * @param password
	 * @return
	 */
	boolean checkPassword(String password);

}
