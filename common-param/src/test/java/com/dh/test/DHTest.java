package com.dh.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dh.common.param.Application;
import com.dh.common.param.handle.HandleService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = Application.class) 
@WebAppConfiguration
public class DHTest {
	@Autowired
	private HandleService handleService;
}
