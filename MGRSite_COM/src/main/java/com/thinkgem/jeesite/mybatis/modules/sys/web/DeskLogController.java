/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.sys.entity.DeskLog;
import com.thinkgem.jeesite.mybatis.modules.sys.entity.Log;
import com.thinkgem.jeesite.mybatis.modules.sys.service.DeskLogService;

/**
 * 前台日志Controller
 * @author ThinkGem
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "/sys/desklog")
public class DeskLogController extends BaseController {

	@Autowired
	private DeskLogService deskLogService;
	
	@RequiresPermissions("sys:desklog:view")
	@RequestMapping(value = {"list", ""})
	public String list(DeskLog deskLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DeskLog> page = deskLogService.findPage(new Page<DeskLog>(request, response), deskLog); 
        model.addAttribute("page", page);
		return "modules/sys/deskLogList";
	}

}
