package com.dh.common.restful;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.dh.common.restful.annotion.EnableDHRestful;

@ComponentScan(basePackages = { "com.dh.common.restful.controller" })
@EnableEurekaClient
@EnableDHRestful
public abstract class CommonApplication {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private SpringApplication application;
	
	public static String ENV_LOCAL_PATH = "/app/conf/";
	public static String ENV_LOCAL_WIN_PATH = "D:/app/conf/";
	protected static final String filename = "common.properties";

	public ConfigurableApplicationContext doStart() {
		try {
			String appName = this.applicationName();
			System.setProperty("logging.app.name", appName);
			application = new SpringApplication(this.getClass());
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
			return application.run();
		} catch (Exception e) {
			logger.error("服务启动失败：",e);
			return null;
		}
	}
	
	public abstract String applicationName() ;
}
