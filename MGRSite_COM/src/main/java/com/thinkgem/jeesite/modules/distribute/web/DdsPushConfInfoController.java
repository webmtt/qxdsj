/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.distribute.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushConfInfo;
import com.thinkgem.jeesite.modules.distribute.service.DdsDataDefService;
import com.thinkgem.jeesite.modules.distribute.service.DdsFtpInfoService;
import com.thinkgem.jeesite.modules.distribute.service.DdsPushConfInfoService;
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
import java.util.Date;

/**
 * 菜单Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "dt/ddsPushConfInfo")
public class DdsPushConfInfoController extends BaseController {

	@Autowired
	private DdsSearchCondInfoService ddsSearchCondInfoService;
	@Autowired
	private DdsDataDefService ddsDataDefService;
	@Autowired
	private DdsFtpInfoService ddsFtpInfoService;
	@Autowired
	private DdsPushConfInfoService ddsPushConfInfoService;
	/*@Autowired
	private BpsJobPlanService bpsJobPlanService;*/
	@Resource
	private ComparasDao comparasDao;
	@RequestMapping(value = "plist")
	public String plist(DdsPushConfInfo ddsPushConfInfo,Model model,HttpServletResponse response,HttpServletRequest request) {
		Page<DdsPushConfInfo> page = ddsPushConfInfoService.getDdsPushConfInfoPage(
				new Page<DdsPushConfInfo>(request, response),ddsPushConfInfo);
        model.addAttribute("page", page);
		return "modules/distribute/DdsPushConfInfoList";
	}
	
	@RequestMapping(value = "addDdsPushConfInfo")
	public String addDdsPushConfInfo(String[] ids,HttpServletResponse response,HttpServletRequest request
			,RedirectAttributes redirectAttributes) {
		int len = ids.length;
		String dataId=request.getParameter("dataId");
    	try {
    		ddsPushConfInfoService.updateByDataId(dataId);
    		for (int i = 0; i < len; i++) {
    			DdsPushConfInfo ddsPushConfInfo=new DdsPushConfInfo();
    			ddsPushConfInfo.setInvalid(0);
    			ddsPushConfInfo.setOrderNo(0);
    			ddsPushConfInfo.setDataId(dataId);
    			ddsPushConfInfo.setFtpId(ids[i]);
    			int maxid=ddsPushConfInfoService.getMaxId();
    			ddsPushConfInfo.setPushId(maxid+1);
    			ddsPushConfInfo.setCreated(new Date());
    			ddsPushConfInfoService.save(ddsPushConfInfo);
        	}
    		addMessage(redirectAttributes, "保存成功");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存失败");
			e1.printStackTrace();
		}
		
		return "redirect:"+"/dt/ddsFtpInfo/flist2?dataId="+dataId;
	}
}
