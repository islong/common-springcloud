package com.dh.qf.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import com.dh.common.restful.CommonApplication;

/**
 * 
 * @author caisj
 *
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class Application extends CommonApplication	{

	public static void main(String[] args) {
		new Application().doStart();
	}
	
	@Override
	public String applicationName() {
		return "qf-gateway";
	}
}
