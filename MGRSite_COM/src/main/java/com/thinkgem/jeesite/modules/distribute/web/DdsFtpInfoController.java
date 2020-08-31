/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.DdsFtpInfo;
import com.thinkgem.jeesite.modules.distribute.service.DdsDataDefService;
import com.thinkgem.jeesite.modules.distribute.service.DdsFtpInfoService;
import com.thinkgem.jeesite.modules.distribute.service.DdsSearchCondInfoService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "dt/ddsFtpInfo")
public class DdsFtpInfoController extends BaseController {

	@Autowired
	private DdsSearchCondInfoService ddsSearchCondInfoService;
	@Autowired
	private DdsDataDefService ddsDataDefService;
	@Autowired
	private DdsFtpInfoService ddsFtpInfoService;
	//@Autowired
	//private DdsPushConfInfoService ddsPushConfInfoService;
	//@Autowired
	//private BpsJobPlanService bpsJobPlanService;
	@Resource
	private ComparasDao comparasDao;
	@RequestMapping(value = "flist")
	public String flist(DdsFtpInfo ddsFtpInfo, Model model, HttpServletResponse response, HttpServletRequest request) {
		List<DdsFtpInfo> list = ddsFtpInfoService.findDdsFtpInfoList();
        model.addAttribute("list", list);
		return "modules/distribute/DdsFTPInfoList";
	}
	//查询配置下的list列表
	@RequestMapping(value = "flist2")
	public String flist2(DdsFtpInfo ddsFtpInfo, String dataId, Model model, HttpServletResponse response, HttpServletRequest request) {
		List<DdsFtpInfo> list = ddsFtpInfoService.findDdsFtpList(dataId);
        model.addAttribute("list", list);
        model.addAttribute("dataId", dataId);
		return "modules/distribute/DdsFtpInfoList2";
	}
	//查询配置下的list列表
		@RequestMapping(value = "flist3")
		public String flist3(DdsFtpInfo ddsFtpInfo, String dataId, Model model, HttpServletResponse response, HttpServletRequest request) {
			List<DdsFtpInfo> list = ddsFtpInfoService.findDdsFtpInfoList();
			List<DdsFtpInfo> templist = ddsFtpInfoService.findDdsFtpList(dataId);
			String chk_value="";
			for(int i=0;i<templist.size();i++){
				chk_value+=templist.get(i).getFtpId()+"#";
			}
			model.addAttribute("chk_value", chk_value);
	        model.addAttribute("list", list);
	        model.addAttribute("dataId", dataId);
			return "modules/distribute/DdsFtpInfoList3";
		}
	@RequestMapping(value = "add")
	public String add(DdsFtpInfo ddsFtpInfo, Model model, HttpServletResponse response, HttpServletRequest request) {
		model.addAttribute("ddsFtpInfo", ddsFtpInfo);
		return "modules/distribute/DdsFtpInfoAdd";
	}
	@RequestMapping(value = "edit")
	public String edit(String ftpId, Model model,HttpServletResponse response,HttpServletRequest request) {
		DdsFtpInfo ddsFtpInfo=ddsFtpInfoService.getDdsFtpInfo(ftpId);
		model.addAttribute("ddsFtpInfo", ddsFtpInfo);
		model.addAttribute("ftpId", ftpId);
		return "modules/distribute/DdsFtpInfoEdit";
	}
	@RequestMapping(value = "delete")
	public String delete(String ftpId, Model model,HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		try{
			ddsFtpInfoService.delete(ftpId);
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/ddsFtpInfo/flist";
	}
	@RequestMapping(value = "SaveDdsFtpInfo")
	public String SaveDdsFtpInfo(DdsFtpInfo ddsFtpInfo, Model model, HttpServletResponse response,
                                 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		 List<DdsFtpInfo> list=ddsFtpInfoService.findDdsFtpInfoList(ddsFtpInfo.getFtpId());
		try{
			 if(list.size()==0){
				 ddsFtpInfo.setOrderNo(0);
				 ddsFtpInfo.setInvalid(0);
				 ddsFtpInfoService.save(ddsFtpInfo);
			 }else{
				 //更新
				 DdsFtpInfo ddsFtpInfo2=new DdsFtpInfo();
				 ddsFtpInfo2=list.get(0);
				 ddsFtpInfo2.setFtpId(ddsFtpInfo.getFtpId());
				 ddsFtpInfo2.setFtpIp(ddsFtpInfo.getFtpIp());
				 ddsFtpInfo2.setFtpPort(ddsFtpInfo.getFtpPort());
				 ddsFtpInfo2.setFtpPwd(ddsFtpInfo.getFtpPwd());
				 ddsFtpInfo2.setFtpUser(ddsFtpInfo.getFtpUser());
				 ddsFtpInfoService.save(ddsFtpInfo2);
			 }
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/ddsFtpInfo/flist";
		
	}

	@RequestMapping(value = "checkJobName")
	public void checkJobName(String ftpId, HttpServletResponse response) {
		List<DdsFtpInfo> list = ddsFtpInfoService.findDdsFtpInfoList(ftpId);
		Map map = new HashMap();
		if (list.size() == 0) {
			map.put("status", 0);
		} else {
			map.put("status", 1);
		}
		String json = JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	
}
