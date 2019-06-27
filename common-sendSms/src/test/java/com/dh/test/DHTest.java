package com.dh.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dh.common.restful.entity.DHRequest;
import com.dh.common.restful.entity.DHResponse;
import com.dh.common.utils.GsonUtil;
import com.dh.qf.sendSms.Application;
import com.dh.qf.sendSms.handle.SendSmsService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = Application.class) 
@WebAppConfiguration
public class DHTest {
	
	@Autowired
	private SendSmsService sendSmsService;
	
	
	@Test 
    public void testSendSms() throws Exception {  
		DHRequest request = new DHRequest("", "");
		Map<String, String> param = new HashMap<>();
		param.put("mobiles", "17602107290");
		param.put("content", "测试");
		request.setJsonParam(GsonUtil.toString(param));
		DHResponse response = sendSmsService.handle(request);
		System.out.println(response.getRetMsg());
	}
	
}
