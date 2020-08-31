/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.BpsJobPlan;
import com.thinkgem.jeesite.modules.distribute.service.*;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "dt/bpsJobPlan")
public class BpsJobPlanController extends BaseController {

	@Autowired
	private DdsSearchCondInfoService ddsSearchCondInfoService;
	@Autowired
	private DdsDataDefService ddsDataDefService;
	@Autowired
	private DdsFtpInfoService ddsFtpInfoService;
	@Autowired
	private DdsPushConfInfoService ddsPushConfInfoService;
	@Autowired
	private BpsJobPlanService bpsJobPlanService;
	@Resource
	private ComparasDao comparasDao;
	@RequestMapping(value = "jlist")
	public String jlist(String jobName,Model model,HttpServletResponse response,HttpServletRequest request) {
		Page<BpsJobPlan> page = bpsJobPlanService.getBpsJobPlanPage(new Page<BpsJobPlan>(request, response),jobName);
        model.addAttribute("page", page);
        model.addAttribute("jobName", jobName);
		return "modules/distribute/BpsJobPlanList";
	}
	@RequestMapping(value = "add")
	public String add(BpsJobPlan bpsJobPlan,Model model,HttpServletResponse response,HttpServletRequest request) {
		model.addAttribute("bpsJobPlan", bpsJobPlan);
		return "modules/distribute/BpsJobPlanAdd";
	}
	@RequestMapping(value = "edit")
	public String edit(String jobName, Model model,HttpServletResponse response,HttpServletRequest request) {
		BpsJobPlan bpsJobPlan=bpsJobPlanService.getBpsJobPlan(jobName);
		model.addAttribute("bpsJobPlan", bpsJobPlan);
		model.addAttribute("jobName", jobName);
		return "modules/distribute/BpsJobPlanEdit";
	}
	@RequestMapping(value = "delete")
	public String delete(String jobName, Model model,HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		try{
			bpsJobPlanService.delete(jobName);
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/bpsJobPlan/jlist";
	}
	@RequestMapping(value = "SaveBpsJobPlan")
	public String SaveBpsJobPlan(BpsJobPlan bpsJobPlan, Model model,HttpServletResponse response,
			HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String jobName=bpsJobPlan.getJobName();
		List<BpsJobPlan> list=bpsJobPlanService.getBpsJobPlanList(jobName);
		BpsJobPlan bpsJobPlan2=new BpsJobPlan();
		if(list.size()>0){
			bpsJobPlan2=list.get(0);
			bpsJobPlan2.setUpdated(new Date());
			bpsJobPlan2.setDescription(bpsJobPlan.getDescription());
			bpsJobPlan2.setJobParameter(bpsJobPlan.getJobParameter());
			bpsJobPlan2.setTriGgerPolicy(bpsJobPlan.getTriGgerPolicy());
		}else{
			bpsJobPlan2.setJobName(bpsJobPlan.getJobName());
			bpsJobPlan2.setCreated(new Date());
			bpsJobPlan2.setDescription(bpsJobPlan.getDescription());
			bpsJobPlan2.setJobParameter(bpsJobPlan.getJobParameter());
			bpsJobPlan2.setTriGgerPolicy(bpsJobPlan.getTriGgerPolicy());
			bpsJobPlan2.setInvalid(0);
			bpsJobPlan2.setJobGroup("Group1");
			bpsJobPlan2.setClassName("com.ultra.dataDS.main.InsertSearchRecord");
			bpsJobPlan2.setTriGgerPriority(0);
			bpsJobPlan2.setRetryCount(0);
			bpsJobPlan2.setTreatasDeadTimeOut(1);
			bpsJobPlan2.setTreatasDeadTimeOutUnit("hour");
			bpsJobPlan2.setQuartzCluster("diaodu");
		}
		try{
			bpsJobPlanService.saveJobPlan(bpsJobPlan2);
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/bpsJobPlan/jlist";
		
	}

	@RequestMapping(value = "checkJobName")
	public void checkJobName(String jobName, HttpServletResponse response) {
		List<BpsJobPlan> list = bpsJobPlanService.getBpsJobPlanList(jobName);
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
