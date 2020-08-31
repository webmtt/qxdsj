/*
 * @(#)PortalPicShowController.java 2016-8-25
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;

import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.PortalPicShowDef;
import com.thinkgem.jeesite.modules.index.service.PortalPicShowService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.tvmeeting.service.DocDefService;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-8-25
 */
@Controller
@RequestMapping(value = "portalpicshow/portalpicshowarrange")
public class PortalPicShowController extends BaseController{
	@Autowired
	private PortalPicShowService ppsService;
	@Resource
	private ComparasDao comparasDao;
	@Autowired
	private DocDefService docDefService;
	@RequestMapping("/portalpicshowList")
	public String FunitemsList(@RequestParam
			String area,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		
		
		  area=request.getParameter("area");
			List<PortalPicShowDef> lists=ppsService.find(area);
			model.addAttribute("lists", lists);
			model.addAttribute("area", area);
		
		return "modules/portalpicshow/PortalpicshowList";		
	}
	
	@RequestMapping("/portalpicshowAdd")
	public String PortalpicshowAdd(HttpServletRequest request,HttpServletResponse response,Model model,PortalPicShowDef portal){
		int maxUploadSize=Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
		String area=request.getParameter("area");
		model.addAttribute("maxUploadSize",maxUploadSize/1024/1024);
		model.addAttribute("portal",portal);
		model.addAttribute("area",area);
		return "modules/portalpicshow/PortalpicshowAdd";		
	}	
	
	
	@RequestMapping("/teleDel")
	public String TeleDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
			Model model){
		String area=request.getParameter("area");
		String picCode=request.getParameter("picCode");
		ppsService.Delpps(picCode, area);
		cleanSuccess();
		addMessage(redirectAttributes, "删除成功");
		//return "redirect:"+"/tvmeeting/meetingarrange/teleList";
		return "redirect:"+"/portalpicshow/portalpicshowarrange/portalpicshowList?area="+area+"";		
	}
	
	@RequestMapping("/Savepps")
	public String SaveColumn(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
//		String columnItemID=request.getParameter("columnItemIDs");
//		Integer id=Integer.parseInt(columnItemID);
//		//ColumnItemsDef column=colService.findByColumn(id);
//	PortalPicShowDef pps=ppsService.findBypps(columnItemID);
		
		PortalPicShowDef pps=new PortalPicShowDef();
	    String picCode=request.getParameter("picCode");
		String orderNo=request.getParameter("sort");
		String chnName=request.getParameter("chnName");
		String invalid=request.getParameter("invalid");
		String linkURL=request.getParameter("linkURL");
		
		String area=request.getParameter("areaItems");
		String arr=","+area+",";
				pps.setPicCode(picCode);
				pps.setSort(Integer.valueOf(orderNo));
				pps.setChnName(chnName);
				pps.setInvalid(Integer.parseInt(invalid));
				pps.setLinkURL(linkURL);
				pps.setAreaItem(arr);
				ppsService.savepps(pps);
				cleanSuccess();
				addMessage( redirectAttributes, "保存成功");
				return "redirect:"+"/portalpicshow/portalpicshowarrange/portalpicshowList?area="+area+"";	
		
	}
	public String cleanSuccess(){
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
	public String visitUrl(String cachePath)
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
	
	
}
	
