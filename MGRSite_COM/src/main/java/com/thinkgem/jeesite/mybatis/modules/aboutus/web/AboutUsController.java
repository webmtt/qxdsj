/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.aboutus.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.mybatis.modules.aboutus.entity.AboutUs;
import com.thinkgem.jeesite.mybatis.modules.aboutus.service.AboutUsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;


/**
 * 关于我们信息维护Controller
 * @author songyan
 * @version 2020-02-27
 */
@Controller
@RequestMapping(value = "/aboutus/aboutUs")
public class AboutUsController extends BaseController {

	@Autowired
	private AboutUsService aboutUsService;
	
	@ModelAttribute
	public AboutUs get(@RequestParam(required=false) String id) {
		AboutUs entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = aboutUsService.get(id);
		}
		if (entity == null){
			entity = new AboutUs();
		}
		return entity;
	}
	
	//@RequiresPermissions("aboutus:aboutUs:view")
	@RequestMapping(value = {"list", ""})
	public String list(AboutUs aboutUs, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AboutUs> page = aboutUsService.findPage(new Page<AboutUs>(request, response), aboutUs); 
		model.addAttribute("page", page);
		return "modules/aboutus/aboutUsList";
	}

	//@RequiresPermissions("aboutus:aboutUs:view")
	@RequestMapping(value = "form")
	public String form(AboutUs aboutUs, Model model) {
		model.addAttribute("aboutUs", aboutUs);
		return "modules/aboutus/aboutUsForm";
	}

	//@RequiresPermissions("aboutus:aboutUs:edit")
	@RequestMapping(value = "save")
	public String save(AboutUs aboutUs, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, aboutUs)){
			return form(aboutUs, model);
		}
		aboutUsService.save(aboutUs);
		addMessage(redirectAttributes, "保存关于我们成功");
		return "redirect:/aboutus/aboutUs/list";
	}
	
	//@RequiresPermissions("aboutus:aboutUs:edit")
	@RequestMapping(value = "delete")
	public String delete(AboutUs aboutUs, RedirectAttributes redirectAttributes) {
		aboutUsService.delete(aboutUs);
		addMessage(redirectAttributes, "删除关于我们成功");
		return "redirect:/aboutus/aboutUs/list";
	}

}