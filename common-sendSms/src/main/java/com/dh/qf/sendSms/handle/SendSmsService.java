package com.dh.qf.sendSms.handle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dh.common.restful.DHRestful;
import com.dh.common.restful.entity.DHRequest;
import com.dh.common.restful.entity.DHResponse;
import com.dh.common.utils.GsonUtil;
import com.dh.qf.enums.ParamKeyEnum;

import sun.misc.BASE64Encoder;

@Service("sendSms")
public class SendSmsService {
	private Logger logger = LoggerFactory.getLogger(SendSmsService.class);

	@Resource
	private DHRestful dhRestful;

	public DHResponse handle(DHRequest request) throws Exception {
		DHResponse response = new DHResponse("000000", "成功");
		Map<String, String> reqMap = GsonUtil.toMap(request.getJsonParam());
		String mobiles = reqMap.get("mobiles");
		String content = reqMap.get("content");
		Map<String, String> smsMap = GsonUtil.toMap(dhRestful.getParam(ParamKeyEnum.SEND_SMS_CHANNEL_INFO.getCode()));
		String url = smsMap.get("url");
		String sign = smsMap.get("sign");
		String userid = smsMap.get("userid");
		String password = smsMap.get("password");
		String isUse = smsMap.get("isUse");
		String timespan = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String pwd = getMD5(password + timespan);
		String msgfmt = "gbk";
		content = sign + content;
		StringBuilder sendData = new StringBuilder();

		BASE64Encoder encoder = new BASE64Encoder();
		String content_base64 = encoder.encode(content.getBytes(msgfmt));

		sendData.append("userid=").append(userid).append("&pwd=").append(pwd).append("&timespan=").append(timespan)
				.append("&mobile=").append(mobiles).append("&content=").append(content_base64);
		logger.info("发送短信请求参数{}", sendData.toString());
		// 发送短信
		String result = "0000";//模拟成功调用http ——>httpPost(url, sendData.toString(), msgfmt);
		logger.info("发送短信返回参数{}", result);
		if (StringUtils.isEmpty(result)) {
			response.setRetCode(DHResponse.ERROR);
			response.setRetMsg("短信发送失败");
			return response;
		}
		return response;
	}

	private String getMD5(String sourceStr) {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * http协议提交
	 */
	private String httpPost(String ulr, String data, String charset) {

		URL url = null;
		try {
			url = new URL(ulr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
			conn.setConnectTimeout(30 * 1000);
			conn.setReadTimeout(30 * 1000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream out = conn.getOutputStream();
			out.write(data.getBytes(charset));
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			StringBuilder response = new StringBuilder();
			String result;
			while (null != (result = in.readLine())) {
				response.append(result).append("\n");
			}
			in.close();
			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
