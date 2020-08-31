/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.stream.web;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.stream.entity.SupArithmeticsStream;
import com.thinkgem.jeesite.mybatis.modules.stream.service.SupArithmeticsStreamService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 算法池基本信息Controller
 * @author yck
 * @version 2019-12-03
 */
@Controller
@RequestMapping(value = "/stream/supArithmeticsStreamAPI")
public class SupArithmeticsStreamAPIController extends BaseController {

	@Autowired
	private SupArithmeticsStreamService supArithmeticsStreamService;


	@ModelAttribute
	public SupArithmeticsStream get(@RequestParam(required=false) String id) {
		SupArithmeticsStream entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supArithmeticsStreamService.get(id);
		}
		if (entity == null){
			entity = new SupArithmeticsStream();
		}
		return entity;
	}

    /**
     * 算法池查看
     * @param supArithmeticsStream
     * @param request
     * @param response
     * @param model
     * @return
     */
//	@RequiresPermissions("stream:supArithmeticsStreamAPI:view")
	@RequestMapping(value = {"/list", ""})
	public String list(SupArithmeticsStream supArithmeticsStream, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupArithmeticsStream> page = supArithmeticsStreamService.findPages(new Page<SupArithmeticsStream>(request, response), supArithmeticsStream);
		model.addAttribute("page", page);
		return "modules/stream/supArithmeticsStreamAPIList";
	}
}