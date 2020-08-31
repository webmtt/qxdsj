package com.thinkgem.jeesite.modules.sys.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
/**
 * 清除缓存的Controller
 * @author fengwanggang
 *
 */
@Controller
@RequestMapping(value = "sys")
public class CacheCleanController {
	@Resource
	private ComparasDao comparasDao;
	/*
	 * 开始进入页面
	 */
	@RequestMapping(value="cacheclean")
	public String cacheclean(){
		
	return "modules/sys/cacheClear";
	}
	/*
	 * 点击清理缓存时
	 */
	@RequestMapping(value="cleansuccess")
	@ResponseBody
	public String cleanSuccess(HttpServletRequest request){
		String message="";
		String cachePath=(String)comparasDao.getComparasByKey("cacheCleanUrl");
		System.out.println(cachePath);
		if(cachePath.contains(";"))
		{
			String[] urls=cachePath.split(";");
			for(String url: urls)
			{
				if(visitUrl(url).equals("1"));
				message="清理成功 ";
			}
		}
		else
		{
			message= visitUrl(cachePath).equals("1")? "清理成功":"";
		}
		 
	    return message;	 
    }
	
	private String visitUrl(String cachePath)
	{
		URL postUrl;
		try {
			postUrl = new URL(cachePath);
			 HttpURLConnection connection = (HttpURLConnection) postUrl
		                .openConnection();
		        // Output to the connection. Default is
		        // false, set to true because post
		        // method must write something to the
		        // connection
		        // 设置是否向connection输出，因为这个是post请求，参数要放在
		        // http正文内，因此需要设为true
		        connection.setDoOutput(true);
		        // Read from the connection. Default is true.
		        connection.setDoInput(true);
		        // Set the post method. Default is GET
		        connection.setRequestMethod("POST");
		        // Post cannot use caches
		        // Post 请求不能使用缓存
		        connection.setUseCaches(false);
		        // This method takes effects to
		        // every instances of this class.
		        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
		        // connection.setFollowRedirects(true);
		 
		        // This methods only
		        // takes effacts to this
		        // instance.
		        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
		        connection.setInstanceFollowRedirects(true);
		        // Set the content type to urlencoded,
		        // because we will write
		        // some URL-encoded content to the
		        // connection. Settings above must be set before connect!
		        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
		        // 进行编码
		        connection.setRequestProperty("Content-Type",
		                "application/x-www-form-urlencoded");
		        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		        // 要注意的是connection.getOutputStream会隐含的进行connect。
		        connection.connect();
		        DataOutputStream out = new DataOutputStream(connection
		                .getOutputStream());
		        // The URL-encoded contend
		        // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
		        String content = "key=j0r53nmbbd78x7m1pqml06u2&type=1&toemail=cngolon@gmail.com" + "&activatecode=" + URLEncoder.encode("中国聚龙", "utf-8");
		        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
		        out.writeBytes(content); 
		        out.flush();
		        out.close(); // flush and close
		        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//设置编码,否则中文乱码
		        String line="";
		        String message="";
		        while ((line = reader.readLine()) != null){
		            //line = new String(line.getBytes(), "utf-8");
		            System.out.println(line);
		            message+=line;
		        }
		       
		        reader.close();
		        connection.disconnect();
		        System.out.println(message);
		        
		        return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public String cleanCache()
	{
		String message="";
		String cachePath=(String)comparasDao.getComparasByKey("cacheCleanUrl");
		System.out.println(cachePath);
		if(cachePath.contains(";"))
		{
			String[] urls=cachePath.split(";");
			for(String url: urls)
			{
				if(visitUrl(url).equals("1"));
				message="清理成功 ";
			}
		}
		else
		{
			message= visitUrl(cachePath).equals("1")? "清理成功":"";
		}
		 
	    return message;	 
	}
}