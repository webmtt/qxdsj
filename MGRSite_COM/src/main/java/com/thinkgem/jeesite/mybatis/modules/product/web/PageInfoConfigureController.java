
package com.thinkgem.jeesite.mybatis.modules.product.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.product.entity.PageInfoConfigure;
import com.thinkgem.jeesite.mybatis.modules.product.service.PageInfoConfigureService;

/**
 * 产品库页面配置Controller
 * @author yang.kq
 * @version 2019-11-04
 */
@Controller
@RequestMapping(value = "/product/pageInfoConfigure")
public class PageInfoConfigureController extends BaseController {

	@Autowired
	private PageInfoConfigureService pageInfoConfigureService;
	
	@ModelAttribute
	public PageInfoConfigure get(@RequestParam(required=false) String id) {
		PageInfoConfigure entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pageInfoConfigureService.get(id);
		}
		if (entity == null){
			entity = new PageInfoConfigure();
		}
		return entity;
	}

	@RequestMapping(value = {"/list", ""})
	public String list(PageInfoConfigure pageInfoConfigure, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PageInfoConfigure> page = pageInfoConfigureService.findPage(new Page<PageInfoConfigure>(request, response), pageInfoConfigure); 
		model.addAttribute("page", page);
		return "modules/product/pageInfoConfigureList";
	}

	@RequestMapping(value = "/form")
	public String form(PageInfoConfigure pageInfoConfigure, Model model) {
		model.addAttribute("pageInfoConfigure", pageInfoConfigure);
		return "modules/product/pageInfoConfigureForm";
	}

	@RequestMapping(value = "/save")
	public String save(PageInfoConfigure pageInfoConfigure, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pageInfoConfigure)){
			return form(pageInfoConfigure, model);
		}
		pageInfoConfigureService.save(pageInfoConfigure);
		addMessage(redirectAttributes, "保存页面成功");
		return "redirect:/product/pageInfoConfigure/list";
	}

	@RequestMapping(value = "/delete")
	public String delete(PageInfoConfigure pageInfoConfigure, RedirectAttributes redirectAttributes) {
		pageInfoConfigureService.delete(pageInfoConfigure);
		addMessage(redirectAttributes, "删除页面成功");
		return "redirect:/product/pageInfoConfigure/list";
	}

}