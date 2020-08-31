/*
 * @(#)DataFtpDefController.java 2016年10月9日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.Encodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.data.entity.DataFtpDef;
import com.thinkgem.jeesite.modules.data.entity.DataLinks;
import com.thinkgem.jeesite.modules.data.service.CategoryDataReltService;
import com.thinkgem.jeesite.modules.data.service.DataCategoryDefService;
import com.thinkgem.jeesite.modules.data.service.DataDefService;
import com.thinkgem.jeesite.modules.data.service.DataFtpDefService;
import com.thinkgem.jeesite.modules.data.service.DataReferDefService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;

import fr.opensagres.xdocreport.document.json.JSONObject;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Controller
@RequestMapping("/dataFtpDef")
public class DataFtpDefController extends BaseController{
	@Autowired
	private DataFtpDefService dataFtpDefService;
	@Autowired
	private DataDefService dataDefService;
	@Autowired
	private DataCategoryDefService dataCategoryDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private CategoryDataReltService categoryDataReltService;
	/**
	 *获取FTP下载的列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/dataFtpList")
	public String dataFtpList(HttpServletRequest request,HttpServletResponse response,Model model){
		String datacode = request.getParameter("dataCode");
		String categoryid = request.getParameter("categoryid");
		String pid = request.getParameter("pid");
		// pid不为空的情况，直接传值挑战
		if(datacode!=null&&!"".equals(datacode)){
			 List<DataFtpDef> list= dataFtpDefService.findDataFtpDefByCode(datacode);
			 if(list.size()==0){
				 model.addAttribute("status", 1);
			 }else{
				 model.addAttribute("status", 0);
			 }
		}else{
			model.addAttribute("status", 1);
		}
		if (pid != null && !"".equals(pid)) {
			// 类型选择 大类小类
			List<DataCategory> plist = dataCategoryDefService.getDataCategoryList(0);
			model.addAttribute("plist", plist);
			List<DataCategory> clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
			model.addAttribute("clist", clist);
			if ("17".equals(pid)) {
				categoryid = "17";
			}
			List<Object[]> dlist = categoryDataReltService.findFtpListById(String.valueOf(categoryid));
			model.addAttribute("dlist", dlist);
			Page<Object[]> page = dataFtpDefService.findPageByName(new Page<Object[]>(request, response),
					 categoryid, datacode);
			model.addAttribute("datacode", datacode);
			model.addAttribute("categoryid", categoryid);
			model.addAttribute("pid", pid);
			model.addAttribute("page", page);
		} else {
			List<DataCategory> plist = dataCategoryDefService.getDataCategoryList(0);
			pid = String.valueOf(plist.get(0).getCategoryid());
			model.addAttribute("plist", plist);
			// 小类
			List<DataCategory> clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
			if ("17".equals(pid)) {
				categoryid = "17";
			} else {
				//categoryid = String.valueOf(clist.get(0).getCategoryid());
				if(clist.size()==0){
					clist=dataCategoryDefService.getDataCategoryList(Integer.valueOf(17));
					categoryid="17";
					pid="17";
				}else{
					categoryid=String.valueOf(clist.get(0).getCategoryid());
				}
			}
			model.addAttribute("clist", clist);
			List<Object[]> dlist = categoryDataReltService.findFtpListById(String.valueOf(categoryid));
			model.addAttribute("dlist", dlist);
			Page<Object[]> page = dataFtpDefService.findPageByName(new Page<Object[]>(request, response),
					 categoryid, datacode);
			model.addAttribute("datacode", datacode);
			model.addAttribute("categoryid", categoryid);
			model.addAttribute("pid", pid);
			model.addAttribute("page", page);
			}
		return "modules/data/dataFtpListDef";
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
	 * 跳转至添加页面
	 * @param response
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/dataFtpAdd")
	public String dataFtpAdd(HttpServletResponse response,HttpServletRequest request,Model model){
		String dataCode=request.getParameter("dataCode");
		String pid=request.getParameter("pid");
		String categoryId=request.getParameter("categoryId");
		String pageType=request.getParameter("pageType");
		DataDef dd=dataDefService.getDataDefByCode(dataCode);
		DataCategory dc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryId));
		DataCategory pdc=dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
		List<DataFtpDef> list=dataFtpDefService.findDataFtpDefByCode(dataCode);
		String[] ftpUrls=null;
		String[] ftpNames=null;
		DataFtpDef dataFtpDef=null;
		List<Map<String,Object>> dataFtpList=new ArrayList<Map<String,Object>>();
		if(list!=null&&list.size()>0){
			dataFtpDef=list.get(0);
			ftpUrls=dataFtpDef.getFtpurls().split(";");
			ftpNames=dataFtpDef.getFtpnames().split(";");
			for(int i=1;i<=ftpUrls.length;i++){
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("ftpUrls", ftpUrls[i-1]);
				map.put("ftpName", ftpNames[i-1]);
				if(i==1){
					map.put("ftpUtl", dataFtpDef.getFtpurl01());					
				}else if(i==2){
					map.put("ftpUtl", dataFtpDef.getFtpurl02());
				}else if(i==3){
					map.put("ftpUtl", dataFtpDef.getFtpurl03());
				}else if(i==4){
					map.put("ftpUtl", dataFtpDef.getFtpurl04());
				}else if(i==5){
					map.put("ftpUtl", dataFtpDef.getFtpurl05());
				}else if(i==6){
					map.put("ftpUtl", dataFtpDef.getFtpurl06());
				}else if(i==7){
					map.put("ftpUtl", dataFtpDef.getFtpurl07());
				}else if(i==8){
					map.put("ftpUtl", dataFtpDef.getFtpurl08());
				}else if(i==9){
					map.put("ftpUtl", dataFtpDef.getFtpurl09());
				}else{
					map.put("ftpUtl", dataFtpDef.getFtpurl10());
				}
				dataFtpList.add(map);
			}
			model.addAttribute("invalid", dataFtpDef.getInvalid());
		}else{
			model.addAttribute("invalid", 1);
		}
		model.addAttribute("dataCode", dataCode);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("pid", pid);
		model.addAttribute("pidName", pdc.getChnname());
		model.addAttribute("categoryName", dc.getChnname());
		model.addAttribute("dataDefName", dd.getChnname());
		model.addAttribute("pageType", pageType);
		model.addAttribute("dataFtpList", dataFtpList);
		return "modules/data/dataFtpDefEdit";
	}
	/**
	 * 保存信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/save")
	public void save(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String dataCode=request.getParameter("dataCode");
		String pid=request.getParameter("pid");
		String categoryId=request.getParameter("categoryId");
		String pageType=request.getParameter("pageType");
		String jsonStr=request.getParameter("jsonStr");
		boolean flag=false;
		JSONObject jsonObject=new JSONObject(jsonStr);
		List<JSONObject> list=(List<JSONObject>)jsonObject.get("list");
		String ftpUrls="";
		String ftpNames="";
		DataFtpDef df=new DataFtpDef();
		int i=1;
		String name="";
		String url="";
		for(JSONObject o:list){
			/*if((o.getString("FTPURLs")!=null&&!"".equals(o.getString("FTPURLs")))
					&&(o.getString("FTPNames")!=null&&!"".equals(o.getString("FTPNames")))
					&&(o.getString("FTPURL")!=null&&!"".equals(o.getString("FTPURL")))){*/
			if((o.getString("FTPNames")!=null&&!"".equals(o.getString("FTPNames")))
					&&(o.getString("FTPURL")!=null&&!"".equals(o.getString("FTPURL")))){
//				url=o.getString("FTPURLs");
				name=o.getString("FTPNames");
				try {
					String encodeType=Encodes.getEncoding(name);
					if("ISO-8859-1".equals(encodeType)){
						name=new String(name.getBytes("ISO-8859-1"),"UTF-8");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if(i<10){
					ftpUrls+="FTPurl0"+i+";";						
				}else{
					ftpUrls+="FTPurl"+i+";";	
				}
				ftpNames+=name+";";
				if(i==1){
					df.setFtpurl01(o.getString("FTPURL"));
				}else if(i==2){
					df.setFtpurl02(o.getString("FTPURL"));
				}else if(i==3){
					df.setFtpurl03(o.getString("FTPURL"));
				}else if(i==4){
					df.setFtpurl04(o.getString("FTPURL"));
				}else if(i==5){
					df.setFtpurl05(o.getString("FTPURL"));
				}else if(i==6){
					df.setFtpurl06(o.getString("FTPURL"));
				}else if(i==7){
					df.setFtpurl07(o.getString("FTPURL"));
				}else if(i==8){
					df.setFtpurl08(o.getString("FTPURL"));
				}else if(i==9){
					df.setFtpurl09(o.getString("FTPURL"));
				}else{
					df.setFtpurl10(o.getString("FTPURL"));
				}
				i++;
			}
		}
		df.setDatacode(dataCode);
		int nameIndex=ftpNames.lastIndexOf(";");
		int UrlsIndex=ftpUrls.lastIndexOf(";");
		if(nameIndex>-1&&nameIndex==(ftpNames.length()-1)){
			ftpNames=ftpNames.substring(0, nameIndex);
		}
		if(UrlsIndex>-1&&UrlsIndex==(ftpUrls.length()-1)){
			ftpUrls=ftpUrls.substring(0, UrlsIndex);
		}
		df.setFtpnames(ftpNames);
		df.setFtpurls(ftpUrls);
		df.setInvalid(0);
		try {
			if((ftpNames==null||"".equals(ftpNames))&&(ftpUrls==null||"".equals(ftpUrls))){
				dataFtpDefService.delDataFtpDef(dataCode);
			}else{
				dataFtpDefService.updatedDataDef(df);				
			}
//			cacheCleanController.cleanCache();
			flag=true;
		} catch (Exception e) {
			flag=false;			
			e.printStackTrace();
		}
		Map map=new HashMap();
		
		if(flag){
			map.put("status", "保存成功");
			map.put("dataCode", dataCode);
		}else{
			map.put("status", "保存失败");
			map.put("dataCode", dataCode);
		}
		renderText(JsonMapper.toJsonString(map), response);
	}
	@RequestMapping("/delete")
	public String deleteById(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){		
		
		String datacode=request.getParameter("datacode");
		String categoryid=request.getParameter("categoryid");
		String pid=request.getParameter("pid");
		try {
			List<DataFtpDef> list=dataFtpDefService.findDataFtpDefByCode(datacode);
			if(list.size()>0){
				DataFtpDef dataFtpDef=list.get(0);
				if(dataFtpDef.getInvalid()==0){
					dataFtpDef.setInvalid(1);
				}else if(dataFtpDef.getInvalid()==1){
					dataFtpDef.setInvalid(0);
				}
				dataFtpDefService.updatedDataDef(dataFtpDef);
			}
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "更新成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "更新失败");
			e.printStackTrace();
		}		
		/*model.addAttribute("datacode", datacode);
		addMessage(redirectAttributes, "删除成功");*/
		return "redirect:"+"/dataFtpDef/dataFtpList?dataCode="+datacode+"&categoryid="+categoryid+"&pid="+pid;	
	}
}
