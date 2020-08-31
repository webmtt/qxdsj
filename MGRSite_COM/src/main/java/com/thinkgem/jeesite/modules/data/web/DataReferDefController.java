package com.thinkgem.jeesite.modules.data.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.data.entity.DataLinks;
import com.thinkgem.jeesite.modules.data.entity.DataReferDef;
import com.thinkgem.jeesite.modules.data.service.CategoryDataReltService;
import com.thinkgem.jeesite.modules.data.service.DataCategoryDefService;
import com.thinkgem.jeesite.modules.data.service.DataDefService;
import com.thinkgem.jeesite.modules.data.service.DataReferDefService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;
/**
 * 
 * 描述：使用文献
 *
 * @author cuijt
 * @version 1.0 2017-2-20
 */
@Controller
@RequestMapping(value="/dataReferDef")
public class DataReferDefController extends BaseController{
	@Resource
	private DataReferDefService dataReferDefService;
	@Resource
	private DataDefService dataDefService;
	@Resource
	private DataCategoryDefService dataCategoryDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private CategoryDataReltService categoryDataReltService;
	@RequestMapping("/dataReferDefList")
	public String dataReferDefList(HttpServletRequest request,HttpServletResponse response,Model model){						
		String datacode=request.getParameter("dataCode");	
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String dataDefName=request.getParameter("dataDefName");
		//pid不为空的情况，直接传值挑战
		if(pid!=null&&!"".equals(pid)){
			//类型选择   大类小类
			List<DataCategory> plist=dataCategoryDefService.getDataCategoryList(0);
			model.addAttribute("plist",plist);
			List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
			model.addAttribute("clist",clist);
			if("17".equals(pid)){
				categoryid="17";
			}
			List<Object[]> dlist=categoryDataReltService.findCategoryListById(String.valueOf(categoryid));
			model.addAttribute("dlist",dlist);
			Page<Object[]> page =				
					dataReferDefService.findPageByName(new Page<Object[]>(request, response), dataDefName,categoryid, datacode);	
			DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
			DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
			model.addAttribute("datacode",datacode);
			model.addAttribute("categoryid",categoryid);
			model.addAttribute("categoryName", dc.getChnname());
			model.addAttribute("pid", pid);
			model.addAttribute("pidName", pdc.getChnname());
			model.addAttribute("page", page);
			model.addAttribute("dataDefName", dataDefName);
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
					dataReferDefService.findPageByName(new Page<Object[]>(request, response), dataDefName,categoryid,datacode);		
			DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
			DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
			model.addAttribute("datacode",datacode);
			model.addAttribute("categoryid",categoryid);
			model.addAttribute("categoryName", dc.getChnname());
			model.addAttribute("pid", pid);
			model.addAttribute("pidName", pdc.getChnname());
			model.addAttribute("page", page);
			model.addAttribute("dataDefName", dataDefName);
		}
		return "modules/data/dataReferDefList";
	}
	/**
	 * 根据大类获取子类列表
	 * @param DataClass
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getDataSubClassList")
	public void getDataSubClassList(String DataClass,HttpServletRequest request,HttpServletResponse response,Model model){
		//根据大类型获取子类的列表
		List<DataCategory> clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(DataClass));
		String json=JsonMapper.toJsonString(clist);
		renderText(json, response);
	}
	/**
	 *排序功能-数据列表排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortDataRefer")
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
		List<DataReferDef> list=dataReferDefService.findDataReferDefByDataCode(dataCode);
		model.addAttribute("list", list);
		return "modules/data/dataReferSortList";
	}
	/**
	 * 获取添加页面
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request,HttpServletResponse response,DataReferDef dataReferDef,Model model){
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
		return "modules/data/dataReferDefAdd";			
	}
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public String save(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){		
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String pageType=request.getParameter("pageType");
		String refername = request.getParameter("refername");		
		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		DataReferDef dataReferDef=new DataReferDef();
		dataReferDef.setDatacode(datacode);
		dataReferDef.setRefername(refername);
		dataReferDef.setOrderno(orderno);
		dataReferDef.setInvalid(0);
		dataReferDefService.saveDataReferDefService(dataReferDef);
//		cacheCleanController.cleanCache();
		addMessage(redirectAttributes, "保存成功");
		String url="";
		if("0".equals(pageType)){
			url="redirect:"+"/dataService/getDataDetail?categoryid="+categoryid+"&pid="+pid;
		}else if("1".equals(pageType)){
			url="redirect:"+"/dataReferDef/dataReferDefList?categoryid="+categoryid+"&pid="+pid+"&dataCode="+datacode;
		}
		return url;
	}
	
	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/dataReferDefUpdate")
	public String dataReferDefUpdate(HttpServletRequest request,HttpServletResponse response,Model model){	
		Integer referid=Integer.valueOf(request.getParameter("referid"));
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		DataReferDef dataReferDef=dataReferDefService.findDataReferDefByID(referid);		
//		DataReferDef dataReferDef=dataReferDefService.findDataReferDefByDataCode(datacode).get(0);
		DataDef dd=dataDefService.findDataDefByDataCode(datacode);
		DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
		DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
		model.addAttribute("dataReferDef", dataReferDef);
		model.addAttribute("dataCode", datacode);
		model.addAttribute("categoryid", categoryid);
		model.addAttribute("categoryName", dc.getChnname());
		model.addAttribute("dataDefName", dd.getChnname());
		model.addAttribute("pid", pid);
		model.addAttribute("pidName", pdc.getChnname());
		return "modules/data/dataReferDefEdit";	
	}
	
	/**
	 * 修改保存
	 * 根据id修改
	 */
	@RequestMapping("/update")
	public String updateSave(DataReferDef dataReferDef,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		Integer referid=Integer.valueOf(request.getParameter("referid"));
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		String refername = request.getParameter("refername");	
		String invalid = request.getParameter("invalid");	
		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		dataReferDef.setReferid(referid);
		dataReferDef.setDatacode(datacode);
		dataReferDef.setRefername(refername);
		dataReferDef.setOrderno(orderno);
		dataReferDef.setInvalid(Integer.valueOf(invalid));
//		BaseDao封装的save()方法，要求id是String类型的，这里id是Integer类型，类型转换异常
		dataReferDefService.saveDataReferDefService(dataReferDef);
//		cacheCleanController.cleanCache();
//		dataReferDefService.saveUpdate(referid, datacode, refername, orderno, invalid);
		return "redirect:"+"/dataReferDef/dataReferDefList?dataCode="+datacode+"&categoryid="+categoryid+"&pid="+pid;		
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
		Integer referid=Integer.valueOf(request.getParameter("referid"));
		try {			
			dataReferDefService.delDataReferDefService(referid);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");
		} catch (NumberFormatException e) {
			addMessage(redirectAttributes, "删除失败");
			e.printStackTrace();
		}
		return "redirect:"+"/dataReferDef/dataReferDefList?dataCode="+datacode+"&categoryid="+categoryid+"&pid="+pid;		
	}
	@RequestMapping("/delete")
	public String deleteById(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){				
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		Integer referid=Integer.valueOf(request.getParameter("referid"));
		try {	
			DataReferDef dataReferDef=dataReferDefService.findDataReferDefByID(referid);
			if(dataReferDef.getInvalid()==0){
				dataReferDef.setInvalid(1);
			}else if(dataReferDef.getInvalid()==1){
				dataReferDef.setInvalid(0);
			}
			dataReferDefService.saveDataReferDefService(dataReferDef);
			//dataReferDefService.delDataReferDefService(referid);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "更新成功");
		} catch (NumberFormatException e) {
			addMessage(redirectAttributes, "更新失败");
			e.printStackTrace();
		}
		return "redirect:"+"/dataReferDef/dataReferDefList?dataCode="+datacode+"&categoryid="+categoryid+"&pid="+pid;		
	}
	/**
	 * 批量相关链接
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids,String categoryid,String pid,String dataCode, RedirectAttributes redirectAttributes) {
		try {
		int len = ids.length;
		DataReferDef[] dataReferDef = new DataReferDef[len];
    	
    		for (int i = 0; i < len; i++) {
    			dataReferDef[i] = dataReferDefService.findDataReferDefByID(Integer.valueOf(ids[i]));
    			dataReferDef[i].setOrderno(i);
    			dataReferDefService.saveDataReferDefService(dataReferDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/dataReferDef/dataReferDefList?pid="+pid+"&categoryid="+categoryid+"&datacode="+dataCode;
	}

	/**
	 * 批量导入
	 */
	@RequestMapping(value = "/excelUpload")
	public void excelUpload(@RequestParam("file") CommonsMultipartFile file){
		try {
			InputStream in = file.getInputStream();
			dataReferDefService.upload(in,file);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
