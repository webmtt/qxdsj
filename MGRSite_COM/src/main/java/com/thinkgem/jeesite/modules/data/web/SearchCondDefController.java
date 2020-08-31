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
import com.thinkgem.jeesite.modules.data.entity.SearchCondDef;
import com.thinkgem.jeesite.modules.data.service.SearchCondDefService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;


@Controller
@RequestMapping(value = "/searchCondDef")
public class SearchCondDefController extends BaseController {
	@Autowired
	private SearchCondDefService searchCondDefService;
	@Autowired
	private CacheCleanController cacheCleanController;

	@RequestMapping(value = "/list")
	public String searchCondDefList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,String dataCode,Integer sid,Integer fid) {
		String searchgroupcode=(String)paramMap.get("searchgroupcode");
		Page<SearchCondDef> page=null;
		if(null!=searchgroupcode &&!"".equals(searchgroupcode)){
			page=searchCondDefService.getByPage(new Page<SearchCondDef>(request,response),searchgroupcode);	
			
		}else{
			page=searchCondDefService.getByPage(new Page<SearchCondDef>(request,response));	
		}
		model.addAttribute("searchgroupcode",searchgroupcode);
		model.addAttribute("page",page);
		return "modules/data/searchCondDefList";
	}
	//跳转到添加页面
	@RequestMapping("/toAdd")
	public String searchCondDefAdd(HttpServletRequest request,HttpServletResponse response,Model model){
		return "modules/data/searchCondDefAdd";		
	}
	
	// 添加-保存
	@RequestMapping("/save")
	public String searchCondDefSave(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		SearchCondDef searchCondDef=new SearchCondDef();
		//id
		Integer maxId=searchCondDefService.getMaxId();
		searchCondDef.setId(maxId+1);		
		//其他字段
		extractMethod(request,model, searchCondDef);
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondDef/list";
	}
	//修改
	//跳转到添加页面
	@RequestMapping("/toEdit")
	public String searchCondDefToEdit(HttpServletRequest request,HttpServletResponse response,Model model){
		String id=request.getParameter("id");
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(id));
		model.addAttribute("searchCondDef",searchCondDef);
		return "modules/data/searchCondDefEdit";		
	}	
	
	@RequestMapping("/edit")
	public String searchCondDefEdit(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		SearchCondDef searchCondDef=new SearchCondDef();
		//id值不变
		String id=request.getParameter("id");
		searchCondDef.setId(Integer.valueOf(id));
		//其他字段
		extractMethod(request,model,searchCondDef);
		
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/SearchCondDef/list";		
	}
	private void extractMethod(HttpServletRequest request,Model model, SearchCondDef searchCondDef) {
		String searchgroupcode=request.getParameter("searchgroupcode");
		String searchcode=request.getParameter("searchcode");
		String engname=request.getParameter("engname");
		String chnname=request.getParameter("chnname");
		String searchtype=request.getParameter("searchtype");
		String searchsubtype=request.getParameter("searchsubtype");
		String datasourcetype=request.getParameter("datasourcetype");
		String searchattach=request.getParameter("searchattach");
		String searchattach2=request.getParameter("searchattach2");
		Integer isoptional=Integer.valueOf(request.getParameter("isoptional"));
		Integer isvaluelimit=Integer.valueOf(request.getParameter("isvaluelimit"));
		String valuelimit=request.getParameter("valuelimit");
		String valuelimitunit=request.getParameter("valuelimitunit");
		Integer begindatetype=Integer.valueOf(request.getParameter("begindatetype"));
		String begindate=request.getParameter("begindate");
		Integer enddatetype=Integer.valueOf(request.getParameter("enddatetype"));
		String enddate=request.getParameter("enddate");
		Integer minvalue=Integer.valueOf(request.getParameter("minvalue"));
		Integer maxvalue=Integer.valueOf(request.getParameter("maxvalue"));
		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		Integer invalid=Integer.valueOf(request.getParameter("invalid"));
		String  defaultvalue=request.getParameter("defaultvalue");
		Integer ishidden=Integer.valueOf(request.getParameter("ishidden"));
		String attachsearchcode=request.getParameter("attachsearchcode");
		String optiondefault=request.getParameter("optiondefault");
		Integer groupdefaultsearch=Integer.valueOf(request.getParameter("groupdefaultsearch"));
		String searchsubcode=request.getParameter("searchsubcode");
		
		searchCondDef.setAttachsearchcode(attachsearchcode);
		searchCondDef.setBegindate(begindate);
		searchCondDef.setBegindatetype(begindatetype);
		searchCondDef.setChnname(chnname);
		searchCondDef.setDatasourcetype(datasourcetype);
		searchCondDef.setDefaultvalue(defaultvalue);
		searchCondDef.setEnddate(enddate);
		searchCondDef.setEnddatetype(enddatetype);
		searchCondDef.setEngname(engname);
		searchCondDef.setGroupdefaultsearch(groupdefaultsearch);
		searchCondDef.setInvalid(invalid);
		searchCondDef.setIshidden(ishidden);
		searchCondDef.setIsoptional(isoptional);
		searchCondDef.setIsvaluelimit(isvaluelimit);
		searchCondDef.setMaxvalue(maxvalue);
		searchCondDef.setMinvalue(minvalue);
		searchCondDef.setOptiondefault(optiondefault);
		searchCondDef.setOrderno(orderno);
		searchCondDef.setSearchattach(searchattach);
		searchCondDef.setSearchattach2(searchattach2);
		searchCondDef.setSearchcode(searchcode);
		searchCondDef.setSearchgroupcode(searchgroupcode);
		searchCondDef.setSearchsubcode(searchsubcode);
		searchCondDef.setSearchsubtype(searchsubtype);
		searchCondDef.setSearchtype(searchtype);
		searchCondDef.setValuelimit(valuelimit);
		searchCondDef.setValuelimitunit(valuelimitunit);
		//created
		String created=request.getParameter("created");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null!=created||!"".equals(created)){
			model.addAttribute("created", created);
			try {
				searchCondDef.setCreated(sdf.parse(created));
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
			searchCondDef.setCreated(c.getTime());
		}		
		
	}	
	//删除
	@RequestMapping("/delete")
	public String searchCondDefDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String id=request.getParameter("id");
		try{
			searchCondDefService.delSearchCondDefById(Integer.valueOf(id));
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
			e.printStackTrace();
		}						
		return "redirect:"+"/searchCondDef/list";	
	}
	
}







