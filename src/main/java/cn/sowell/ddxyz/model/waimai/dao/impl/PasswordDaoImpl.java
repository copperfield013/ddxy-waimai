package cn.sowell.ddxyz.model.waimai.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.ddxyz.model.waimai.dao.PasswordDao;

@Repository
public class PasswordDaoImpl implements PasswordDao{
	
	@Resource
	SessionFactory sFactory;

	@SuppressWarnings("rawtypes")
	public boolean checkPassword(String password) {
		String sql = "select * from t_waimai_password t where t.c_password = :password";
		System.out.println("-------->sql:"+sql);
		Session session = sFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("password", password);
		List list = query.list();
		if(list.size() > 0){
			return true;
		}
		return false;
	}

}
