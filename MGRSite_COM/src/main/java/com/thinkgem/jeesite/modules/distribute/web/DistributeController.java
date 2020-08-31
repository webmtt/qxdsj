/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.DdsDataDef;
import com.thinkgem.jeesite.modules.distribute.service.DdsDataDefService;
import com.thinkgem.jeesite.modules.distribute.service.DdsFtpInfoService;
import com.thinkgem.jeesite.modules.distribute.service.DdsSearchCondInfoService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import org.apache.commons.lang.StringEscapeUtils;
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
@RequestMapping(value = "dt/distribute")
public class DistributeController extends BaseController {

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
	@RequestMapping(value = {"list", ""})
	public String list(DdsDataDef ddsDataDef,String dataId,String hostName,Model model,HttpServletResponse response,HttpServletRequest request) {
		if(hostName==null || "".equals(hostName)){
			hostName = "all";
		}
		Page<DdsDataDef> page = ddsDataDefService.getDdsDataDefPage(
				new Page<DdsDataDef>(request, response),ddsDataDef,dataId,hostName);
		List<DdsDataDef> list=page.getList();
		for(int i=0;i<list.size();i++){
			DdsDataDef DdsDataDef=list.get(i);
			DdsDataDef.setNameFormat(StringEscapeUtils.escapeHtml(DdsDataDef.getNameFormat()));
		}
		List<String> dList = ddsDataDefService.findHostName();
		model.addAttribute("dList", dList);
        model.addAttribute("page", page);
        model.addAttribute("dataId", dataId);
        model.addAttribute("hostN", hostName);
		return "modules/distribute/DdsDataDefList";
	}
	@RequestMapping(value = "add")
	public String add(DdsDataDef ddsDataDef, Model model,HttpServletResponse response,HttpServletRequest request) {
		model.addAttribute("ddsDataDef", ddsDataDef);
		return "modules/distribute/DdsDataDefAdd";
	}
	@RequestMapping(value = "edit")
	public String edit(String Id, Model model,HttpServletResponse response,HttpServletRequest request) {
		DdsDataDef ddsDataDef=ddsDataDefService.getDdsDataDef(Integer.valueOf(Id));
		model.addAttribute("ddsDataDef", ddsDataDef);
		return "modules/distribute/DdsDataDefEdit";
	}
	@RequestMapping(value = "checkJobName")
	public void checkJobName(String dataId, HttpServletResponse response) {
		List<DdsDataDef> list = ddsDataDefService.getDdsDataDefList(dataId);
		Map map = new HashMap();
		if (list.size() == 0) {
			map.put("status", 0);
		} else {
			map.put("status", 1);
		}
		String json = JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	//delete
	@RequestMapping(value = "delete")
	public String delete(String Id, Model model,HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		try{
			ddsDataDefService.delete(Integer.valueOf(Id));
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/distribute/list";
	}
	@RequestMapping(value = "SaveDdsDataDef")
	public String SaveDdsDataDef(DdsDataDef ddsDataDef, Model model,HttpServletResponse response,
			HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String nameFormat=StringEscapeUtils.unescapeHtml(ddsDataDef.getNameFormat());
		ddsDataDef.setNameFormat(nameFormat);
		ddsDataDef.setId(ddsDataDefService.findMaxId()+1);
		ddsDataDef.setInvalid(1);
		ddsDataDef.setIsRedo(1);
		try{
			ddsDataDefService.save(ddsDataDef);
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/distribute/list";
		
	}
	
	@RequestMapping(value = "UpdateDdsDataDef")
	public String UpdateDdsDataDef(DdsDataDef ddsDataDef, Model model,HttpServletResponse response,
			HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String nameFormat=StringEscapeUtils.unescapeHtml(ddsDataDef.getNameFormat());
		ddsDataDef.setNameFormat(nameFormat);

		try{
			 int n = ddsDataDefService.updateDdsDataDef(ddsDataDef);
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/distribute/list";
		
	}
	
	@RequestMapping(value = "test")
	public String test(){
		return "modules/distribute/test";
	}
	
	@RequestMapping(value = "testSortTable")
	public String sortTable(){
		return "modules/distribute/testSortTable";
	}
	
}
