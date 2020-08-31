/*
 * @(#)CdrController.java 2016-10-8
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.Cdrex;
import com.thinkgem.jeesite.modules.data.service.CdrexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-10-8
 */
@Controller
@RequestMapping(value = "cdr/cdrarrange")
public class CdrController extends BaseController {
	@Autowired
	private CdrexService CdrexService;
	@RequestMapping("/CdrList")
	public String CdrList(
			HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		Page<Cdrex> page= CdrexService.getByPage(new Page<Cdrex>(request, response));
		  
		
		   model.addAttribute("page", page);
		    
//			List<DataReferDef> list =page.getList();	
		
		
		String begin=request.getParameter("pageNo");
		if(begin==null||begin==""){
			begin="1";
		}
		
		List<Cdrex> lists=CdrexService.find(begin);
		model.addAttribute("lists", lists);
		 
		
		return "modules/Cdr/CdrList";		
	}
}
