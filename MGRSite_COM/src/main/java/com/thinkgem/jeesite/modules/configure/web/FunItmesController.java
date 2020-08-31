/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.configure.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.configure.entity.FunItmes;
import com.thinkgem.jeesite.modules.configure.service.FunItmesService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;

/**
 * 菜单Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "configure/funItmes")
public class FunItmesController extends BaseController {

	@Autowired
	private FunItmesService funItmesService;
	@Resource
	private ComparasDao comparasDao;
	@RequestMapping(value = {"list", ""})
	public String list(Model model,HttpServletResponse response,HttpServletRequest request) {
		String areaItem=request.getParameter("areaItem");
		List<FunItmes> list = Lists.newArrayList();
		List<FunItmes> sourcelist = funItmesService.findAllList(areaItem);
		FunItmes.sortList(list, sourcelist, 0);
		model.addAttribute("areaItem", areaItem);
        model.addAttribute("list", list);
        
		return "modules/configure/funItmesList";
	}

	@RequestMapping(value = "form")
	public String form(FunItmes funItmes, Model model,HttpServletResponse response,HttpServletRequest request) {
		String areaItem=request.getParameter("areaItem");
		model.addAttribute("areaItem", areaItem);
		if(funItmes.getParent()!=null&&funItmes.getParent().getId()!=null){
			FunItmes funItmes1=funItmesService.getFunItmes(funItmes.getParent().getId());
			funItmes.setParent(funItmes1);
			funItmes.setAreaItem(","+areaItem+",");
			model.addAttribute("funItmes", funItmes);
			return "modules/configure/funItmesForm";
		}else if(funItmes.getId()!=null){
			FunItmes funItmes1=funItmesService.getFunItmes(funItmes.getId());
			model.addAttribute("funItmes", funItmes1);
			return "modules/configure/funItmesForm";
		}else {
			
			FunItmes funItmestemp=new FunItmes();
			funItmestemp.setId(0);
			funItmestemp.setLayer(0);
			funItmestemp.setInvalid(0);
			//funItmestemp.setLink("");
			//funItmestemp.setOrderno(0);
			//funItmestemp.setShowType("0");
			funItmestemp.setName("顶级菜单");
			funItmes.setParent(funItmestemp);
			funItmes.setAreaItem(","+areaItem+",");
			model.addAttribute("funItmes", funItmes);
			return "modules/configure/funItmesForm";
		}
		
	}
	
	@RequestMapping(value = "save")
	public String save(FunItmes funItmes, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String areaItem=request.getParameter("areaItems");
		FunItmes funItmes1=funItmesService.getFunItmes(funItmes.getParent().getId());
		if(funItmes1==null){
			funItmes.setLayer(1);
		}else{
			funItmes.setLayer(funItmes1.getLayer()+1);
		}
		funItmes.setInvalid(0);
		funItmes.setIsnosearch(0);
		funItmes.setShortChnName(funItmes.getName());
		funItmesService.saveFunItems(funItmes);
		cleanSuccess();
		addMessage(redirectAttributes, "保存菜单'" + funItmes.getName() + "'成功");
		return "redirect:"+"/configure/funItmes/list?areaItem="+areaItem;
	}
	
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String areaItem=request.getParameter("areaItem");
		List<FunItmes> list = Lists.newArrayList();
		List<FunItmes> sourcelist = funItmesService.findAllList(areaItem);
		Boolean flag=true;
		FunItmes funItmes=new FunItmes();
		for(int i=0;i<sourcelist.size();i++){
			FunItmes funItmestemp=sourcelist.get(i);
			if(funItmestemp.getParent()!=null&&id.equals(String.valueOf(funItmestemp.getParent().getId()))){
				flag=false;
			} 
			if(id.equals(String.valueOf(funItmestemp.getId()))){
				funItmes=funItmestemp;
			}
		}
		if(flag){
			cleanSuccess();
			funItmes.setAreaItem(funItmes.getAreaItem().replace(","+areaItem+",", ""));
			funItmesService.deleteFunItems(list);
			addMessage(redirectAttributes, "删除菜单'" + funItmes.getName() + "'成功");
		}else{
			addMessage(redirectAttributes, "删除菜单'" + funItmes.getName() + "'失败！请先删除下级菜单");
		}
		
		return "redirect:"+"/configure/funItmes/list?areaItem="+areaItem;
	}
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String areaItem=request.getParameter("areaItem");
    	int len = ids.length;
    	FunItmes[] funItmes = new FunItmes[len];
    	for (int i = 0; i < len; i++) {
    		funItmes[i] = funItmesService.getFunItmes(Integer.valueOf(ids[i]));
    		funItmes[i].setOrderno(sorts[i]);
    		funItmesService.saveFunItems(funItmes[i]);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:"+"/configure/funItmes/list?areaItem="+areaItem;
	}
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) String areaItem,HttpServletResponse response,FunItmes funItmes) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		List<FunItmes> list = funItmesService.findAllList(areaItem);
		
		for (int i=0; i<list.size(); i++){
			FunItmes e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()))){
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParent()!=null?e.getParent().getId():0);
			map.put("name", e.getName());
			mapList.add(map);
			}
		}
		return mapList;
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
