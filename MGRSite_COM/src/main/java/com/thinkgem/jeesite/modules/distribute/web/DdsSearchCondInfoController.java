/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchCondInfo;
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
import java.util.Date;
import java.util.List;

/**
 * 菜单Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "dt/ddsSearchCondInfo")
public class DdsSearchCondInfoController extends BaseController {

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
	
	@RequestMapping(value = "slist")
	public String slist(String dataId,Model model,HttpServletResponse response,HttpServletRequest request) {
		List<DdsSearchCondInfo> list=ddsSearchCondInfoService.findDdsSearchCondInfoList(dataId);
		model.addAttribute("list", list);
		model.addAttribute("dataId", dataId);
		return "modules/distribute/DdsSearchCondInfoList";
	}
	@RequestMapping(value = "add")
	public String add(DdsSearchCondInfo ddsSearchCondInfo, String dataId, Model model, HttpServletResponse response, HttpServletRequest request) {
		model.addAttribute("ddsSearchCondInfo", ddsSearchCondInfo);
		model.addAttribute("dataId", dataId);
		return "modules/distribute/DdsSearchCondInfoAdd";
	}
	@RequestMapping(value = "edit")
	public String edit(String id,String dataId, Model model,HttpServletResponse response,HttpServletRequest request) {
		DdsSearchCondInfo ddsSearchCondInfo=ddsSearchCondInfoService.getDdsSearchCondInfo(Integer.valueOf(id));
		model.addAttribute("ddsSearchCondInfo", ddsSearchCondInfo);
		model.addAttribute("dataId", dataId);
		model.addAttribute("id", id);
		return "modules/distribute/DdsSearchCondInfoEdit";
	}
	@RequestMapping(value = "delete")
	public String delete(String id,String dataId, Model model,HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		try{
			ddsSearchCondInfoService.delete(Integer.valueOf(id));
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/ddsSearchCondInfo/slist?dataId="+dataId;
	}
	@RequestMapping(value = "SaveDdsSearchCondInfo")
	public String SaveDdsFtpInfo(DdsSearchCondInfo ddsSearchCondInfo, Model model, HttpServletResponse response,
                                 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Integer id=ddsSearchCondInfo.getId();
		String dataId=request.getParameter("dataId");
		try{
			if(id!=null){
				DdsSearchCondInfo ddsSearchCondInfo2=ddsSearchCondInfoService.getDdsSearchCondInfo(id);
				ddsSearchCondInfo2.setUpdated(new Date());
				ddsSearchCondInfo2.setOrderNo(ddsSearchCondInfo.getOrderNo());
				ddsSearchCondInfo2.setSearchCond(StringEscapeUtils.unescapeHtml(ddsSearchCondInfo.getSearchCond()));
				ddsSearchCondInfo2.setDataId(dataId);
				ddsSearchCondInfoService.save(ddsSearchCondInfo2);
			}else{
				int temp=ddsSearchCondInfoService.getMaxId();
				ddsSearchCondInfo.setId(temp+1);
				ddsSearchCondInfo.setInvalid(0);
				ddsSearchCondInfo.setCreated(new Date());
				ddsSearchCondInfo.setDataId(dataId);
				ddsSearchCondInfo.setSearchCond(StringEscapeUtils.unescapeHtml(ddsSearchCondInfo.getSearchCond()));
				ddsSearchCondInfoService.save(ddsSearchCondInfo);
			}
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}				
		return "redirect:"+"/dt/ddsSearchCondInfo/slist?dataId="+dataId;
		
	}
}
