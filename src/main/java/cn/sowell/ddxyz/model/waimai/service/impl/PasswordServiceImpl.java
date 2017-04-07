package cn.sowell.ddxyz.model.waimai.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.sowell.ddxyz.model.waimai.dao.PasswordDao;
import cn.sowell.ddxyz.model.waimai.service.PasswordService;

@Service
public class PasswordServiceImpl implements PasswordService {

	@Resource
	PasswordDao passwordDao;
	
	Logger logger = Logger.getLogger(PasswordService.class);
	
	@Override
	public boolean checkPassword(String password) {
		return passwordDao.checkPassword(password);
	}

}
