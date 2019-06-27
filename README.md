# common-springcloud

基于2.1.5.RELEASE，springlcoud Greenwich.RELEASE版本。目前实现比较初级的网关common-gateway，通过RestTemplate调取相应的微服务。
微服务之间调用通过反射方式进行统一调用，只需要给应用名、服务名、方法名、参数（json封装）。
使用说明：

1、mysql数据库服务器，创建test数据执行test-mysql.sql脚本，创建数据库。

2、windows系统在D盘创建app/conf（linux系统创建 /app/conf/），将common.properties、eurekaConf.properties、logback-spring.xml放在建好的目录下（需配置文件数据库连接地址需要按实际情况修改）。

3、集成开发环境先导入项目。

4、先启动common-eureka，在启动common-gateway,然后启动common-param，common-sendSms。

5、可以postman进行测试

![image](https://github.com/islong/common-springcloud/blob/master/images/test.png)

![image](https://github.com/islong/common-springcloud/blob/master/images/test1.png)

![image](https://github.com/islong/common-springcloud/blob/master/images/test2.png)