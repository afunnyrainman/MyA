package com.example.user.mya.Utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 自定义网路通讯类
 */
public class MyNetClient {
	/**
	 * 单一实例网络工具
	 */
	private static MyNetClient client;
	/**
	 * http客户端
	 */
	private HttpClient httpClient;

	private MyNetClient() {
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

	}

	public synchronized static MyNetClient getInstance() {

		if (client == null)
			client = new MyNetClient();
		return client;
	}

	public static String basePath ="http://www.duanwenxue.com";

	/**
	 * doGet提交
	 * 
	 * @param url
	 *            提交地址
	 * @return 服务器返回结果
	 */
	public String doGet(String url, Map<String, String> params) {

		/*
		 * int requestCode=this.NetIsOnLine(url); if(200!=requestCode){ return
		 * null; }
		 */
		// 拼接参数 得到枚举器
		Iterator<Entry<String, String>> iter = params.entrySet().iterator();
		StringBuilder builder = new StringBuilder();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();// 得到下一个对象
			if (entry == null)
				break;
			String key = entry.getKey();// 关键字
			String value = entry.getValue();// 值
			// 拼接
			builder.append(key).append("=").append(value).append("&");
		}
		// 创建请求对象地址
		HttpGet get = new HttpGet(basePath
				+ url
				+ "&"
				+ builder.toString().substring(0,
						builder.toString().length() - 1));

		try {

			// 得到服务器输出
			HttpResponse response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() != 200) {
				params.clear();
				params = null;
				return null;
			}
			params.clear();
			params = null;
			return EntityUtils.toString(response.getEntity(), "UTF-8");

		} catch (Exception e) {
			params.clear();
			params = null;
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * get请求不带参数
	 * 
	 * @param url
	 * @return
	 */
	public String doGet(String url) {
		/*
		 * int requestCode=this.NetIsOnLine(url); if(200!=requestCode){ return
		 * null; }
		 */
		HttpGet get = new HttpGet(basePath + url);
		System.out.println("执行了" + basePath + url);
		try {
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 提交post表单
	 * 
	 * @param url
	 *            请求地址
	 *            ,注意类型是List范型NameValuePair
	 * @return
	 */
	public String doPost(String url, List<NameValuePair> params) {
		/*
		 * int requestCode=this.NetIsOnLine(url); if(200!=requestCode){ return
		 * null; }
		 */
		HttpPost post = new HttpPost(basePath + url);// 因为get请求的内容需要拼接地址,post的内容在表单中
		try {

			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 提交post表单 无参数的
	 * 
	 * @param url
	 *            请求地址
	 *            ,注意类型是List范型NameValuePair
	 * @return
	 */
	public String doPost1(String url) {
		/*
		 * int requestCode=this.NetIsOnLine(url); if(200!=requestCode){ return
		 * null; }
		 */
		HttpPost post = new HttpPost(basePath + url);// 因为get请求的内容需要拼接地址,post的内容在表单中
		Log.e("dxq", basePath + url + "url");
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param savePath
	 *            视频保存路径
	 * @param Filename
	 *            文件名
	 * @param url
	 *            视频下载路径
	 * @return
	 */
	public File getFile(String savePath, String Filename, String url) {
		File saveFile = null;
		try {
			HttpPost request = new HttpPost(basePath + url);
			HttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException("下载错误,"
						+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				int fileSize = 0;
				int downSize = 0;

				HttpEntity httpEntity = response.getEntity();
				fileSize = (int) httpEntity.getContentLength();
				if (fileSize <= 0)
					throw new RuntimeException("文件长度为零!");

				saveFile = new File(savePath);
				if (!saveFile.isDirectory())
					saveFile.mkdirs();

				InputStream iStream = httpEntity.getContent();
				FileOutputStream fOut = new FileOutputStream(new File(savePath
						+ "//" + Filename));
				try {
					byte[] buffer = new byte[1024 * 8];
					int readLen = -1;
					while ((readLen = iStream.read(buffer)) != -1) {

						fOut.write(buffer, 0, readLen);
						downSize += readLen;
						// 这里添家进度完成通知

						int percent = Math.round((downSize * 100 / fileSize));
						System.out.println("percent=======" + percent);

					}
					fOut.flush();

				} finally {
					iStream.close();
					fOut.close();
				}

			}

			return saveFile;
		} catch (Exception e) {

			e.getStackTrace();
			return null;
		}
	}

}
