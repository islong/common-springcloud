package com.dh.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 使用http连接池 复用http连接,省去了tcp的3次握手和4次挥手的时间自动管理tcp连接,并发量很高不合适
 * @author caisj
 *
 */
public class HttpUtil {
	    private static final int CONNECT_TIMEOUT = 60000;//链接超时时间60s
	    private static final int REQUEST_CONNECT_TIMEOUT = 10;//从connect Manager获取Connection 超时时间
	    private static final int SOCKET_TIMEOUT = 60000;//获取数据的超时时间60s
	    private static final int MAX_CONN = 100;
	    private static final int Max_PRE_ROUTE = 100;
	    private static final int MAX_ROUTE = 100;
	    private static CloseableHttpClient httpClient; // 发送请求的客户端单例
	    private static PoolingHttpClientConnectionManager manager;
	    private static ScheduledExecutorService monitorExecutor;
	 
	    private final static Object syncLock = new Object();
	 
	    /**
	     * 对http请求进行基本设置
	     * @param httpRequestBase http请求
	     */
	    private static void setRequestConfig(HttpRequestBase httpRequestBase){
	        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(REQUEST_CONNECT_TIMEOUT)
	                .setConnectTimeout(CONNECT_TIMEOUT)
	                .setSocketTimeout(SOCKET_TIMEOUT).build();
	        httpRequestBase.setConfig(requestConfig);
	    }
	 
	    public static CloseableHttpClient getHttpClient(String url){
	        String hostName = url.split("/")[2];
	        System.out.println(hostName);
	        int port = 80;
	        if (hostName.contains(":")){
	            String[] args = hostName.split(":");
	            hostName = args[0];
	            port = Integer.parseInt(args[1]);
	        }
	 
	        if (httpClient == null){
	            synchronized (syncLock){
	                if (httpClient == null){
	                    httpClient = createHttpClient(hostName, port);
	                    //开启监控线程,对异常和空闲线程进行关闭
	                    monitorExecutor = Executors.newScheduledThreadPool(1);
	                    monitorExecutor.scheduleAtFixedRate(new TimerTask() {
	                        @Override
	                        public void run() {
	                            //关闭异常连接
	                            manager.closeExpiredConnections();
	                            //关闭5s空闲的连接
	                            manager.closeIdleConnections(Config.httpIdelTimeout, TimeUnit.MILLISECONDS);
	                        }
	                    }, Config.httpMonitorInterval, Config.httpMonitorInterval, TimeUnit.MILLISECONDS);
	                }
	            }
	        }
	        return httpClient;
	    }
	 
	    /**
	     * 根据host和port构建httpclient实例
	     * @param host 要访问的域名
	     * @param port 要访问的端口
	     * @return
	     */
	    public static CloseableHttpClient createHttpClient(String host, int port){
	        ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
	        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
	        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainSocketFactory)
	                .register("https", sslSocketFactory).build();
	 
	        manager = new PoolingHttpClientConnectionManager(registry);
	        //设置连接参数
	        manager.setMaxTotal(MAX_CONN); // 最大连接数
	        manager.setDefaultMaxPerRoute(Max_PRE_ROUTE); // 路由最大连接数
	 
	        HttpHost httpHost = new HttpHost(host, port);
	        manager.setMaxPerRoute(new HttpRoute(httpHost), MAX_ROUTE);
	 
	        //请求失败时,进行请求重试
	        HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
	            @Override
	            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
	                if (i > 3){
	                    //重试超过3次,放弃请求
	                    System.err.println("retry has more than 3 time, give up request");
	                    return false;
	                }
	                if (e instanceof NoHttpResponseException){
	                    //服务器没有响应,可能是服务器断开了连接,应该重试
	                    System.err.println("receive no response from server, retry");
	                    return true;
	                }
	                if (e instanceof SSLHandshakeException){
	                    // SSL握手异常
	                    System.err.println("SSL hand shake exception");
	                    return false;
	                }
	                if (e instanceof InterruptedIOException){
	                    //超时
	                    System.err.println("InterruptedIOException");
	                    return false;
	                }
	                if (e instanceof UnknownHostException){
	                    // 服务器不可达
	                    System.err.println("server host unknown");
	                    return false;
	                }
	                if (e instanceof ConnectTimeoutException){
	                    // 连接超时
	                    System.err.println("Connection Time out");
	                    return false;
	                }
	                if (e instanceof SSLException){
	                    System.err.println("SSLException");
	                    return false;
	                }
	 
	                HttpClientContext context = HttpClientContext.adapt(httpContext);
	                HttpRequest request = context.getRequest();
	                if (!(request instanceof HttpEntityEnclosingRequest)){
	                    //如果请求不是关闭连接的请求
	                    return true;
	                }
	                return false;
	            }
	        };
	 
	        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).setRetryHandler(handler).build();
	        return client;
	    }
	 
	    /**
	     * 设置post请求的参数
	     * @param httpPost
	     * @param params
	     */
	    private static void setPostParams(HttpPost httpPost, Map<String, Object> params){
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        Set<String> keys = params.keySet();
	        for (String key: keys){
	            nvps.add(new BasicNameValuePair(key, (String)params.get(key)));
	        }
	        try {
	            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * 设置post请求的json数据
	     * @param httpPost
	     * @param json
	     */
	    private static void setPostJson(HttpPost httpPost, String json){
	        	StringEntity entity = new StringEntity(json.toString(),"utf-8");    
	        	entity.setContentEncoding("UTF-8");
	            entity.setContentType("application/json");
	            httpPost.setEntity(entity);
	    }    
	 
	    public static JSONObject post(String url, Map<String, Object> params,String json){
	        HttpPost httpPost = new HttpPost(url);
	        setRequestConfig(httpPost);
	        if(null==params){
	        setPostParams(httpPost, params);
	        }else {
	        	setPostJson(httpPost, json);
	        }
	        CloseableHttpResponse response = null;
	        InputStream in = null;
	        JSONObject object = null;
	        try {
	            response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
	            HttpEntity entity = response.getEntity();
	            if (entity != null) {
	                in = entity.getContent();
	                String result = IOUtils.toString(in, "utf-8");
	                System.out.println("返回数据:"+result);
	                object = JSONObject.parseObject(result);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try{
	                if (in != null) in.close();
	                if (response != null) response.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return object;
	    }
	 
	    /**
	     * 关闭连接池
	     */
	    public static void closeConnectionPool(){
	        try {
	            httpClient.close();
	            manager.close();
	            monitorExecutor.shutdown();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    class Config{
	    	static final int httpConnectTimeout = 3000;
	    	static final int httpSocketTimeout = 3000;
	    	static final int httpMaxPoolSize = 100;
	    	static final int httpMonitorInterval = 3000;
	    	static final int httpIdelTimeout = 5000;
	    }
	    
	    
}
