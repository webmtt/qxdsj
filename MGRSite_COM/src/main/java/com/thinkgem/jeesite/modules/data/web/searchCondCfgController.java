package com.thinkgem.jeesite.modules.data.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.EleSetElementDef;
import com.thinkgem.jeesite.modules.data.entity.SearchCondCfg;
import com.thinkgem.jeesite.modules.data.entity.SearchCondDef;
import com.thinkgem.jeesite.modules.data.entity.SearchCondItems;
import com.thinkgem.jeesite.modules.data.service.SearchCondCfgService;
import com.thinkgem.jeesite.modules.data.service.SearchCondDefService;
import com.thinkgem.jeesite.modules.data.service.SearchCondItemsService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;


@Controller
@RequestMapping(value = "/searchCondCfg")
public class searchCondCfgController extends BaseController {
	@Autowired
	private SearchCondCfgService searchCondCfgService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private SearchCondItemsService searchCondItemsService;
	@Autowired
	private SearchCondDefService searchCondDefService;
	
	@RequestMapping(value = "/list")
	public String searchCondCfgList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,String dataCode,Integer sid,Integer fid) {
		String searchconfigcode=(String)paramMap.get("searchconfigcode");
		Page<SearchCondCfg> page=null;
		if(null!=searchconfigcode &&!"".equals(searchconfigcode)){
			page=searchCondCfgService.getByPage(new Page<SearchCondCfg>(request,response),searchconfigcode);	
			
		}else{
			page=searchCondCfgService.getByPage(new Page<SearchCondCfg>(request,response));	
		}
		model.addAttribute("searchconfigcode",searchconfigcode);
		model.addAttribute("page",page);
		return "modules/data/searchCondCfgList";
	}
	/**
	 * 通用字典表的选定
	 * @param paramMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SearchCondItemslist")
	public String SearchCondItemslist(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model) {
		String itemtype=(String)paramMap.get("itemtype");
		//条件的id
		String pid=(String)paramMap.get("pid");
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(pid));
		model.addAttribute("pid",pid);
		model.addAttribute("defaultvalue",searchCondDef.getDefaultvalue());
		//作为显示用
		model.addAttribute("name",searchCondDef.getChnname());
		//判断是否有绑定
		model.addAttribute("searchAttach",searchCondDef.getSearchattach());
		List<SearchCondCfg> list1=searchCondCfgService.getSearchCondCfgListByScc(searchCondDef.getSearchattach());
		if(list1.size()==0){
			model.addAttribute("name2","无");
		}else{
			model.addAttribute("name2",list1.get(0).getChndescription());
		}
		
		List<SearchCondCfg> alist=searchCondCfgService.getSearchCondCfgList();
		model.addAttribute("alist",alist);
		//根据类型获取
		if(itemtype==null||"".equals(itemtype)){
			if(searchCondDef.getSearchattach()==null||"".equals(searchCondDef.getSearchattach())){
				itemtype=alist.get(0).getSearchconfigcode();
			}else{
				itemtype=searchCondDef.getSearchattach();
			}
		}
		List<SearchCondItems> list=searchCondItemsService.getSearchCondList(itemtype);
		model.addAttribute("list",list);
		model.addAttribute("itemtype",itemtype);
		return "modules/data/searchCondItemsList";
	}
	/**
	 * 通字典表的维护 
	 * @param paramMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SearchCondItemslist2")
	public String SearchCondItemslist2(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model) {
		String itemtype=(String)paramMap.get("itemtype");
		//条件的id
		String pid=(String)paramMap.get("pid");
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(pid));
		model.addAttribute("pid",pid);
		model.addAttribute("defaultvalue",searchCondDef.getDefaultvalue());
		//作为显示用
		model.addAttribute("name",searchCondDef.getChnname());
		//判断是否有绑定
		model.addAttribute("searchAttach",searchCondDef.getSearchattach());
		List<SearchCondCfg> list1=searchCondCfgService.getSearchCondCfgListByScc(searchCondDef.getSearchattach());
		if(list1.size()==0){
			model.addAttribute("name2","无");
		}else{
			model.addAttribute("name2",list1.get(0).getChndescription());
		}
		
		List<SearchCondCfg> alist=searchCondCfgService.getSearchCondCfgList();
		model.addAttribute("alist",alist);
		//根据类型获取
		if(itemtype==null||"".equals(itemtype)){
			if(searchCondDef.getSearchattach()==null||"".equals(searchCondDef.getSearchattach())){
				itemtype=alist.get(0).getSearchconfigcode();
			}else{
				itemtype=searchCondDef.getSearchattach();
			}
		}
		List<SearchCondItems> list=searchCondItemsService.getSearchCondList(itemtype);
		model.addAttribute("list",list);
		model.addAttribute("itemtype",itemtype);
		return "modules/data/searchCondItemsList2";
	}
	/**
	 * 绑定字典表数据
	 * @param searchCondCfg
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/searchCondCfgBind")
	public String searchCondCfgBind(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		String pid=request.getParameter("pid");
		String itemtype=request.getParameter("itemtype");
		String defaultvalue=request.getParameter("defaultvalue");
		
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(pid));
		searchCondDef.setDefaultvalue(defaultvalue);
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;	
	}
	/**
	 * 绑定字典表数据
	 * @param searchCondCfg
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/searchCondCfgSave")
	public String searchCondCfgSave(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		String pid=request.getParameter("ppid");
		String itemtype=request.getParameter("itemtype");
		String defaultvalue=request.getParameter("defaultvalue");
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(pid));
		searchCondDef.setDefaultvalue(defaultvalue);
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			model.addAttribute("status",0);
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			model.addAttribute("status",1);
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}
		return "modules/data/blankClose";	
		//return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;	
	}
	
	/**
	 * 选定配置
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/searchCondCfgBindC")
	public String searchCondCfgBindC(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		String pid=request.getParameter("pid");
		String itemtype=request.getParameter("itemtype");
		
		SearchCondDef searchCondDef=searchCondDefService.getSearchCondDefById(Integer.valueOf(pid));
		searchCondDef.setSearchattach(itemtype);
		try{
			searchCondDefService.saveSearchCondDef(searchCondDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "选择成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "选择失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;	
	}
	
	//添加通配类型
	@RequestMapping("/toAdd")
	public String searchCondCfgAdd(SearchCondCfg searchCondCfg,HttpServletRequest request,HttpServletResponse response,
			Model model,String itemtype,String pid){
		model.addAttribute("pid",pid);
		model.addAttribute("itemtype",itemtype);
		model.addAttribute("searchCondCfg",searchCondCfg);
		return "modules/data/searchCondEdit";		
	}
	//条件通配详情新增
	@RequestMapping("/AddSearchCondItems")
	public String AddSearchCondItems(String itemtype,HttpServletRequest request,HttpServletResponse response,Model model){
		String pid=request.getParameter("pid");
		model.addAttribute("itemtype",itemtype);
		model.addAttribute("pid",pid);
		return "modules/data/searchCondItemsAdd";		
	}
	//条件通配详情修改
	@RequestMapping("/updateSearchCondItems")
	public String updateSearchCondItems(String id,HttpServletRequest request,HttpServletResponse response,Model model){
		String pid=request.getParameter("pid");
		model.addAttribute("pid",pid);
		SearchCondItems searchCondItems=searchCondItemsService.getSearchCondItems(Integer.valueOf(id));
		model.addAttribute("searchCondItems",searchCondItems);
		return "modules/data/searchCondItemsEdit";		
	}
	//验证searchconfigcode是否存在 
	@RequestMapping("/checkCode")
	public void checkCode(String searchconfigcode,HttpServletRequest request,HttpServletResponse response,Model model){
		List<SearchCondCfg> list=searchCondCfgService.getSearchCondCfgListByScc(searchconfigcode);
		Map map=new HashMap();
		if(list.size()==0){
			map.put("status", 0);
		}else{
			map.put("status", 1);
		}	
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	// 添加-保存
	@RequestMapping("/saveSearchCondCfg")
	public String saveSearchCondCfg(SearchCondCfg searchCondCfg,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String pid=request.getParameter("pid");
		String searchconfigcode=request.getParameter("searchconfigcode");
		String chndescription=request.getParameter("chndescription");
		searchCondCfg.setSearchconfigcode(searchconfigcode);
		searchCondCfg.setDatasource("BMD_SearchCondItems");
		searchCondCfg.setCaptionfield("ItemCaption");
		searchCondCfg.setValuefield("ItemValue");
		searchCondCfg.setWherecond("ItemType='"+searchconfigcode+"' and Invalid=0");
		searchCondCfg.setOrderbycond("OrderNo");
		searchCondCfg.setInvalid(0);
		searchCondCfg.setIsdataspec(0);
		searchCondCfg.setChndescription(chndescription);
		searchCondCfg.setRowitemcount(0);
		try{
			searchCondCfgService.saveSearchCondCfg(searchCondCfg);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+searchconfigcode+"&pid="+pid;
	}
	/**
	 * 新增的保存方法
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveCondItems")
	public String saveCondItems(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String pid=request.getParameter("pid");
		SearchCondItems searchCondItems=new SearchCondItems();
		String itemtype=request.getParameter("itemtype");
		String itemcaption=request.getParameter("itemcaption");
		String itemvalue=request.getParameter("itemvalue");
		searchCondItems.setInvalid(0);
		searchCondItems.setItemtype(itemtype);
		searchCondItems.setItemcaption(itemcaption);
		searchCondItems.setItemvalue(itemvalue);
		searchCondItems.setId(searchCondItemsService.getMaxId()+1);
		searchCondItems.setOrderno(0);
		try{
			searchCondItemsService.saveSearchCondItems(searchCondItems);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;
	}
	@RequestMapping("/updateCondItems")
	public String updateCondItems(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String pid=request.getParameter("pid");
		SearchCondItems searchCondItems=new SearchCondItems();
		String itemtype=request.getParameter("itemtype");
		String itemcaption=request.getParameter("itemcaption");
		String invalid=request.getParameter("invalid");
		String itemvalue=request.getParameter("itemvalue");
		String id=request.getParameter("id");
		String orderno=request.getParameter("orderno");
		searchCondItems.setInvalid(Integer.valueOf(invalid));
		searchCondItems.setItemtype(itemtype);
		searchCondItems.setItemcaption(itemcaption);
		searchCondItems.setItemvalue(itemvalue);
		searchCondItems.setId(Integer.valueOf(id));
		searchCondItems.setOrderno(Integer.valueOf(orderno));
		try{
			searchCondItemsService.saveSearchCondItems(searchCondItems);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;
	}
	@RequestMapping("/deleteCondItems")
	public String delete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String pid=request.getParameter("pid");
		String id=request.getParameter("id");
		String invalid=request.getParameter("invalid");
		String itemtype=request.getParameter("itemtype");
		try{
			searchCondItemsService.deleteCondItems(Integer.valueOf(id),Integer.valueOf(invalid));
//			cacheCleanController.cleanCache();
			if(Integer.valueOf(invalid)==0){
				addMessage(redirectAttributes, "无效成功");
			}else{
				addMessage(redirectAttributes, "有效成功");
			}
		}catch (Exception e) {
			addMessage(redirectAttributes, "重置失败");
			e.printStackTrace();
		}						
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;	
	}
	
	//修改
	//跳转到添加页面
	@RequestMapping("/toEdit")
	public String searchCondCfgToEdit(HttpServletRequest request,HttpServletResponse response,Model model){
		String searchconfigcode=request.getParameter("searchconfigcode");
		SearchCondCfg searchCondCfg=searchCondCfgService.getSearchCondCfgByScc(searchconfigcode);
		model.addAttribute("searchCondCfg",searchCondCfg);
		return "modules/data/searchCondCfgEdit";		
	}	
	
	@RequestMapping("/edit")
	public String searchCondCfgEdit(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		SearchCondCfg searchCondCfg=new SearchCondCfg();
		
		//其他字段
		extractMethod(request,model,searchCondCfg);
		
		try{
			searchCondCfgService.saveSearchCondCfg(searchCondCfg);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}		
		return "redirect:"+"/searchCondCfg/list";		
	}
	private void extractMethod(HttpServletRequest request,Model model, SearchCondCfg searchCondCfg) {
		String searchconfigcode=request.getParameter("searchconfigcode");
		String datasource=request.getParameter("datasource");
		String chndescription=request.getParameter("chndescription");
		String engdescription=request.getParameter("engdescription");
		String captionfield=request.getParameter("captionfield");
		String valuefield=request.getParameter("valuefield");
		Integer isdataspec=Integer.valueOf(request.getParameter("isdataspec"));
		Integer rowitemcount=Integer.valueOf(request.getParameter("rowitemcount"));
		String wherecond=request.getParameter("wherecond");
		String orderbycond=request.getParameter("orderbycond");
		Integer invalid=Integer.valueOf(request.getParameter("invalid"));
		
		searchCondCfg.setCaptionfield(captionfield);
		searchCondCfg.setChndescription(chndescription);
		searchCondCfg.setDatasource(datasource);
		searchCondCfg.setInvalid(invalid);
		searchCondCfg.setEngdescription(engdescription);
		searchCondCfg.setIsdataspec(isdataspec);
		searchCondCfg.setOrderbycond(orderbycond);
		searchCondCfg.setRowitemcount(rowitemcount);
		searchCondCfg.setSearchconfigcode(searchconfigcode);
		searchCondCfg.setValuefield(valuefield);
		searchCondCfg.setWherecond(wherecond);
		
		//created
		String created=request.getParameter("created");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null!=created||!"".equals(created)){
			model.addAttribute("created", created);
			try {
				searchCondCfg.setCreated(sdf.parse(created));
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
			searchCondCfg.setCreated(c.getTime());
		}		
		
	}	
	//删除
	@RequestMapping("/delete")
	public String searchCondCfgDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		String searchconfigcode=request.getParameter("searchconfigcode");
		try{
			searchCondCfgService.delSearchCondCfgByScc(searchconfigcode);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
			e.printStackTrace();
		}						
		return "redirect:"+"/searchCondCfg/list";	
	}
	/**
	 * 批量修改菜单排序  dataDef表
	 */
	@RequestMapping(value = "searchCondCfgSort")
	public String searchCondCfgSort(@RequestParam
			Map<String, Object> paramMap,String[] ids, RedirectAttributes redirectAttributes) {
		String itemtype=(String)paramMap.get("itemtype");
		String pid=(String)paramMap.get("pid");
		try {
		int len = ids.length;
		SearchCondItems[] searchCondItems = new SearchCondItems[len];
    	
    		for (int i = 0; i < len; i++) {
    			searchCondItems[i] = searchCondItemsService.getSearchCondItems(Integer.valueOf(ids[i]));
    			searchCondItems[i].setOrderno(i);
    			searchCondItemsService.saveSearchCondItems(searchCondItems[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/searchCondCfg/SearchCondItemslist?itemtype="+itemtype+"&pid="+pid;	
	}
	/**
	 *排序功能-大小类排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortSearchCondItems")
	public String sortSearchCondItems(HttpServletRequest request,HttpServletResponse response,Model model,
			String itemtype,String pid){
		   List<SearchCondItems> list= searchCondItemsService.getSearchCondList(itemtype);
			model.addAttribute("itemtype",itemtype);
			model.addAttribute("pid",pid);
			model.addAttribute("list",list);
		return "modules/data/searchCondSortList";
		
	}
}







