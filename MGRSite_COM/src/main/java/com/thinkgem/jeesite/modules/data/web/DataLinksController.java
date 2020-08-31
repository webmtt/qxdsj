package com.thinkgem.jeesite.modules.data.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.data.entity.DataLinks;
import com.thinkgem.jeesite.modules.data.service.CategoryDataReltService;
import com.thinkgem.jeesite.modules.data.service.DataCategoryDefService;
import com.thinkgem.jeesite.modules.data.service.DataDefService;
import com.thinkgem.jeesite.modules.data.service.DataLinksService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;

@Controller
@RequestMapping(value="/dataLinks")
public class DataLinksController extends BaseController{
	@Resource
	private DataLinksService dataLinksService;
	@Autowired
	private DataDefService dataDefService;
	@Resource
	private DataCategoryDefService dataCategoryDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private CategoryDataReltService categoryDataReltService;
	
	@RequestMapping("/dataLinksList")
	public String dataLinksList(HttpServletRequest request,HttpServletResponse response,Model model){		
				
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String dataDefName=request.getParameter("dataDefName");
		if(pid!=null&&!"".equals(pid)){
			List<DataCategory> plist=dataCategoryDefService.getDataCategoryList(0);
			model.addAttribute("plist",plist);
			List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
			model.addAttribute("clist",clist);
			List<Object[]> dlist=categoryDataReltService.findCategoryListById(String.valueOf(categoryid));
			model.addAttribute("dlist",dlist);
			Page<Object[]> page =				
					dataLinksService.findDataLinksPages(new Page<Object[]>(request, response), dataDefName, categoryid, datacode);		
			DataDef dd=dataDefService.findDataDefByDataCode(datacode);
			DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
			DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
			model.addAttribute("datacode",datacode);
			model.addAttribute("dataDefName", dataDefName);
			model.addAttribute("categoryid",categoryid);
			model.addAttribute("categoryName", dc.getChnname());
			model.addAttribute("pid", pid);
			model.addAttribute("pidName", pdc.getChnname());
			model.addAttribute("page", page);
		}else{
			List<DataCategory> plist=dataCategoryDefService.getDataCategoryList(0);
			pid=String.valueOf(plist.get(0).getCategoryid());
			model.addAttribute("plist",plist);
			//小类
			List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
			if("17".equals(pid)){
				categoryid="17";
			}else{
				//categoryid=String.valueOf(clist.get(0).getCategoryid());
				if(clist.size()==0){
					clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(17));
					categoryid="17";
					pid="17";
				}else{
					categoryid=String.valueOf(clist.get(0).getCategoryid());
				}
			}
			model.addAttribute("clist",clist);
			List<Object[]> dlist=categoryDataReltService.findCategoryListById(String.valueOf(categoryid));
			model.addAttribute("dlist",dlist);
			Page<Object[]> page =				
					dataLinksService.findDataLinksPages(new Page<Object[]>(request, response), dataDefName, categoryid, datacode);	
			DataDef dd=dataDefService.findDataDefByDataCode(datacode);
			DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
			DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
			model.addAttribute("datacode",datacode);
			model.addAttribute("dataDefName", dataDefName);
			model.addAttribute("categoryid",categoryid);
			model.addAttribute("categoryName", dc.getChnname());
			model.addAttribute("pid", pid);
			model.addAttribute("pidName", pdc.getChnname());
			model.addAttribute("page", page);
		}
		return "modules/data/dataLinksList";
	}
	
	/**
	 * 获取添加页面
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request,HttpServletResponse response,DataLinks dataLinks,Model model){
		String dataCode=request.getParameter("dataCode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String pageType=request.getParameter("pageType");
		DataDef dd=dataDefService.findDataDefByDataCode(dataCode);
		DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
		DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
		model.addAttribute("dataCode", dataCode);
		model.addAttribute("categoryid", categoryid);
		model.addAttribute("pid", pid);
		model.addAttribute("pidName", pdc.getChnname());
		model.addAttribute("categoryName", dc.getChnname());
		model.addAttribute("dataDefName", dd.getChnname());
		model.addAttribute("pageType", pageType);
		return "modules/data/dataLinksAdd";			
	}
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public String save(String linkUrlBox,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){		
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String pageType=request.getParameter("pageType");
		Integer linktype=Integer.valueOf(request.getParameter("linktype"));
		String linkname=request.getParameter("linkname");
		String linkurl=request.getParameter("linkurl");	
		String invalid=request.getParameter("invalid");	
		String orderno=request.getParameter("orderno");	
		DataLinks dataLinks=new DataLinks();
		dataLinks.setInvalid(Integer.valueOf(invalid));
		dataLinks.setOrderno(Integer.valueOf(orderno));
		dataLinks.setDatacode(datacode);
		dataLinks.setLinktype(linktype);
		dataLinks.setLinkname(linkname);
		if(linkUrlBox!=null&&!"".equals(linkUrlBox)){
			dataLinks.setLinkurl(linkUrlBox);			
		}else{
			dataLinks.setLinkurl(linkurl);
		}
		dataLinksService.saveDataLinksService(dataLinks);
//		cacheCleanController.cleanCache();
		addMessage(redirectAttributes, "保存成功");
		String url="";
		if("0".equals(pageType)){
			url= "redirect:"+"/dataService/getDataDetail?categoryid="+categoryid+"&pid="+pid;			
		}else if("1".equals(pageType)){
			url="redirect:"+"/dataLinks/dataLinksList?categoryid="+categoryid+"&pid="+pid+"&datacode="+datacode;
		}
		return url;	
	}
	
	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/dataLinksUpdate")
	public String dataLinksUpdate(HttpServletRequest request,HttpServletResponse response,Model model){
	
		Integer linkid=Integer.valueOf(request.getParameter("linkid"));
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		DataLinks dataLinks=dataLinksService.finddataLinksByID(linkid);		
		DataDef dd=dataDefService.findDataDefByDataCode(datacode);
		DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
		DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
		model.addAttribute("dataLinks", dataLinks);
		model.addAttribute("dataCode", datacode);
		model.addAttribute("categoryid", categoryid);
		model.addAttribute("categoryName", dc.getChnname());
		model.addAttribute("dataDefName", dd.getChnname());
		model.addAttribute("pid", pid);
		model.addAttribute("pidName", pdc.getChnname());
		return "modules/data/dataLinksEdit";	
	}
	
	/**
	 * 修改保存
	 * 根据id修改
	 */
	@RequestMapping("/update")
	public String updateSave(DataLinks dataLinks,String linkUrlBox,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		Integer linkid=Integer.valueOf(request.getParameter("linkid"));
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		Integer linktype=Integer.valueOf(request.getParameter("linktype"));
		String linkname=request.getParameter("linkname");
		String linkurl=request.getParameter("linkurl");	
		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		Integer invalid=Integer.valueOf(request.getParameter("invalid"));
		dataLinks.setDatacode(datacode);
		dataLinks.setLinktype(linktype);
		dataLinks.setLinkname(linkname);
		if(linkUrlBox==null||"".equals(linkUrlBox)){
			dataLinks.setLinkurl(linkurl);			
		}else{
			dataLinks.setLinkurl(linkUrlBox);
		}
		dataLinks.setOrderno(orderno);
		dataLinks.setInvalid(invalid);

//		BaseDao封装的save()方法，要求id是String类型的，这里id是Integer类型，类型转换异常
//		dataLinksService.savedataLinksService(dataLinks);
		
		dataLinksService.saveUpdate(linkid, datacode, linktype,linkname,linkurl, orderno, invalid);
//		cacheCleanController.cleanCache();
		return "redirect:"+"/dataLinks/dataLinksList?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid;		
	}	
	
	/**
	 * 删除
	 * 根据id删除
	 */
	@RequestMapping("/delete2")
	public String deleteById2(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){		
		
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		Integer linkid=Integer.valueOf(request.getParameter("linkid"));
		try {
			dataLinksService.deldataLinksService(linkid);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
			e.printStackTrace();
		}		
		/*model.addAttribute("datacode", datacode);
		addMessage(redirectAttributes, "删除成功");*/
		return "redirect:"+"/dataLinks/dataLinksList?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid;	
	}
	@RequestMapping("/delete")
	public String deleteById(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){		
		
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		Integer linkid=Integer.valueOf(request.getParameter("linkid"));
		try {
			DataLinks dataLinks=dataLinksService.finddataLinksByID(linkid);
			if(dataLinks.getInvalid()==0){
				dataLinks.setInvalid(1);
			}else if(dataLinks.getInvalid()==1){
				dataLinks.setInvalid(0);
			}
			//dataLinksService.deldataLinksService(linkid);
			dataLinksService.saveDataLinksService(dataLinks);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "更新成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "更新失败");
			e.printStackTrace();
		}		
		/*model.addAttribute("datacode", datacode);
		addMessage(redirectAttributes, "删除成功");*/
		return "redirect:"+"/dataLinks/dataLinksList?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid;	
	}
	/**
	 *排序功能-数据列表排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortDataLinks")
	public String sortDataLinks(String datacode,HttpServletRequest request,HttpServletResponse response,Model model){
		String dataCode=request.getParameter("dataCode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String pageType=request.getParameter("pageType");
		DataDef dd=dataDefService.findDataDefByDataCode(dataCode);
		model.addAttribute("dataCode", dataCode);
		model.addAttribute("categoryid", categoryid);
		model.addAttribute("pid", pid);
		model.addAttribute("dataDefName", dd.getChnname());
		model.addAttribute("pageType", pageType);
		List<DataLinks> list=dataLinksService.findDataLinksByDataCode2(dataCode);
		model.addAttribute("list", list);
		return "modules/data/dataLinkSortList";
	}
	/**
	 * 批量相关链接
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids,String categoryid,String pid,String dataCode, RedirectAttributes redirectAttributes) {
		try {
		int len = ids.length;
		DataLinks[] dataLinks = new DataLinks[len];
    	
    		for (int i = 0; i < len; i++) {
    			dataLinks[i] = dataLinksService.finddataLinksByID(Integer.valueOf(ids[i]));
    			dataLinks[i].setOrderno(i);
    			dataLinksService.saveDataLinksService(dataLinks[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/dataLinks/dataLinksList?pid="+pid+"&categoryid="+categoryid+"&datacode="+dataCode;
	}

	/**
	 * 批量导入
	 */
	@RequestMapping(value = "/excelUpload")
	public void excelUpload(@RequestParam("file") CommonsMultipartFile file){
		try {
			InputStream in = file.getInputStream();
			dataLinksService.upload(in,file);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
