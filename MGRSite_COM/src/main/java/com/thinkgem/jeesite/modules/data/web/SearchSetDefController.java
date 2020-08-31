package com.thinkgem.jeesite.modules.data.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.SearchSetDef;
import com.thinkgem.jeesite.modules.data.service.SearchSetDefService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;


@Controller
@RequestMapping(value = "/searchSetDef")
public class SearchSetDefController extends BaseController {
	@Autowired
	private SearchSetDefService searchSetDefService;
	@Autowired
	private CacheCleanController cacheCleanController;

	@RequestMapping(value = "/list")
	public String searchSetDefList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,String dataCode,Integer sid,Integer fid) {
		String searchsetcode=(String)paramMap.get("searchsetcode");
		Page<SearchSetDef> page=null;
		if(null!=searchsetcode &&!"".equals(searchsetcode)){
			page=searchSetDefService.getByPage(new Page<SearchSetDef>(request,response),searchsetcode);	
			
		}else{
			page=searchSetDefService.getByPage(new Page<SearchSetDef>(request,response));	
		}
		model.addAttribute("searchsetcode",searchsetcode);
		model.addAttribute("page",page);
		return "modules/data/searchSetDefList";
	}
	//跳转到添加页面
	@RequestMapping("/toAdd")
	public String searchSetDefAdd(HttpServletRequest request,HttpServletResponse response,Model model){
		return "modules/data/searchSetDefAdd";		
	}
	
	// 添加-保存
	@RequestMapping("/save")
	public String searchSetDefSave(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		SearchSetDef searchSetDef=new SearchSetDef();
		
		String searchsetcode=request.getParameter("searchsetcode");
		String searchgroupcode=request.getParameter("searchgroupcode");
		String chnname=request.getParameter("chnname");
		String engname=request.getParameter("engname");
		String isoptional=request.getParameter("isoptional");
		String orderno=request.getParameter("orderno");
		String invalid=request.getParameter("invalid");
		
		String created=request.getParameter("created");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(null!=created||!"".equals(created)){
			model.addAttribute("created", created);
			try {
				searchSetDef.setCreated(sdf.parse(created));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			Calendar c=Calendar.getInstance();
			try {
				c.setTime(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			searchSetDef.setCreated(c.getTime());
		}
		
		searchSetDef.setChnname(chnname);
		searchSetDef.setEngname(engname);
		searchSetDef.setInvalid(Integer.valueOf(invalid));
		searchSetDef.setIsoptional(Integer.valueOf(isoptional));
		searchSetDef.setOrderno(Integer.valueOf(orderno));
		searchSetDef.setSearchgroupcode(searchgroupcode);
		searchSetDef.setSearchsetcode(searchsetcode);
		//id
		Integer maxId=searchSetDefService.getMaxId();
		searchSetDef.setId(maxId+1);
		try{
			searchSetDefService.saveSearchSetDef(searchSetDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchSetDef/list";
	}
	//修改
	//跳转到添加页面
	@RequestMapping("/toEdit")
	public String searchSetDefToEdit(HttpServletRequest request,HttpServletResponse response,Model model){
		String id=request.getParameter("id");
		SearchSetDef searchSetDef=searchSetDefService.getsearchSetDefById(Integer.valueOf(id));
		model.addAttribute("searchSetDef",searchSetDef);
		return "modules/data/searchSetDefEdit";		
	}	
	
	@RequestMapping("/edit")
	public String searchSetDefEdit(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		SearchSetDef searchSetDef=new SearchSetDef();
		
		//id值不变
		String id=request.getParameter("id");
		searchSetDef.setId(Integer.valueOf(id));
		
		String searchsetcode=request.getParameter("searchsetcode");
		String searchgroupcode=request.getParameter("searchgroupcode");
		String chnname=request.getParameter("chnname");
		String engname=request.getParameter("engname");
		String isoptional=request.getParameter("isoptional");
		String orderno=request.getParameter("orderno");
		String invalid=request.getParameter("invalid");
		
		String created=request.getParameter("created");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(null!=created||!"".equals(created)){
			model.addAttribute("created", created);
			try {
				searchSetDef.setCreated(sdf.parse(created));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			Calendar c=Calendar.getInstance();
			try {
				c.setTime(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			searchSetDef.setCreated(c.getTime());
		}
		
		searchSetDef.setChnname(chnname);
		searchSetDef.setEngname(engname);
		searchSetDef.setInvalid(Integer.valueOf(invalid));
		searchSetDef.setIsoptional(Integer.valueOf(isoptional));
		searchSetDef.setOrderno(Integer.valueOf(orderno));
		searchSetDef.setSearchgroupcode(searchgroupcode);
		searchSetDef.setSearchsetcode(searchsetcode);
		
		try{
			searchSetDefService.saveSearchSetDef(searchSetDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchSetDef/list";		
	}	
	//删除
	@RequestMapping("/delete")
	public String searchSetDefDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String id=request.getParameter("id");
		try{
			searchSetDefService.delSearchSetDefById(Integer.valueOf(id));
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
			e.printStackTrace();
		}						
		return "redirect:"+"/searchSetDef/list";	
	}
	
}







