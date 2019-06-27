package com.dh.common.param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.dh.common.restful.CommonApplication;

/**
 * 
 * @author caisj
 *
 */
@SpringBootApplication
@MapperScan("com.dh.common.param.dao")
@EnableTransactionManagement
public class Application extends CommonApplication{

	public static void main(String[] args) {
		new Application().doStart();
	}
	
	@Override
	public String applicationName() {
		return "common-param";
	}
}
