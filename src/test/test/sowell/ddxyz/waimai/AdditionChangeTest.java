package test.sowell.ddxyz.waimai;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.ddxyz.model.waimai.service.OrderManageService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
public class AdditionChangeTest {
	
	@Resource
	OrderManageService oService;
	
	@Test
	public void test() throws Exception {
		oService.additionChange();
	}
}
