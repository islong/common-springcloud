package com.dh.common.eureka.server;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 
 * @author caisj
 *
 */
@EnableEurekaServer
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Application {

	public static String ENV_LOCAL_PATH = "/app/conf/";
	public static String ENV_LOCAL_WIN_PATH = "D:/app/conf/";
	protected static final String filename = "eurekaConf.properties";

	public static void main(String[] args) throws Exception {
		System.setProperty("logging.app.name", "common-eureka");
		SpringApplication application = new SpringApplication(Application.class);
		Properties prop = new Properties();
		String localPath = ENV_LOCAL_PATH;
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			localPath = ENV_LOCAL_WIN_PATH;
		}

		FileInputStream in = new FileInputStream(localPath + filename);
		prop.load(in);
		in.close();
		Map<String, Object> map = new HashMap<String, Object>((Map) prop);
		application.setDefaultProperties(map);
		application.run(args);
	}

}
