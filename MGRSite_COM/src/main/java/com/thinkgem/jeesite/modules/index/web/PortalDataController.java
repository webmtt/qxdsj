package com.thinkgem.jeesite.modules.index.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.index.entity.PortalDataCategoryDef;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategoryDef;
import com.thinkgem.jeesite.modules.index.service.PortalDataCategoryDefService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;




@Controller
@RequestMapping(value="/portalData")
public class PortalDataController extends BaseController{
	@Autowired
	private PortalDataCategoryDefService portalDataCategoryDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private ComparasService comparasService;
	/**
	 *获取数据下载服务列表并在展示页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/portalDataCategoryList")
	public String dataServiceList(HttpServletRequest request,HttpServletResponse response,
			PortalDataCategoryDef portalDataCategoryDef,String id,Model model){
		Comparas comparas=comparasService.getComparas("imgServerUrl");
		List<PortalDataCategoryDef> list=new ArrayList<PortalDataCategoryDef>();
		List<PortalDataCategoryDef> PortalDataCategoryDefs=portalDataCategoryDefService.findAll();
		PortalDataCategoryDef.sortList(list, PortalDataCategoryDefs, 0);
		model.addAttribute("PortalDataCategoryDefs", list);
		model.addAttribute("imgServiceUrl", comparas.getStringvalue());		
		model.addAttribute("portalDataCategoryDef", portalDataCategoryDef);
		return "modules/index/portalDataCategoryList";
	}
	/**
	 * 新增
	 * @param request
	 * @param response
	 * @param portalDataCategoryDef
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addPortalData")
	public String addPortalData(HttpServletRequest request,HttpServletResponse response,
			PortalDataCategoryDef portalDataCategoryDef,String pid,Model model){
		if(pid!=null&&!"".equals(pid)){
			PortalDataCategoryDef portalDataCategoryDef2=portalDataCategoryDefService.getPortalDataById(Integer.valueOf(pid));
			portalDataCategoryDef.setParent(portalDataCategoryDef2);
		}
		model.addAttribute("portalDataCategoryDef", portalDataCategoryDef);
		return "modules/index/portalDataCategoryAdd";
	}
	/**
	 * 编辑
	 * @param request
	 * @param response
	 * @param portalDataCategoryDef
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editPortalData")
	public String editPortalData(HttpServletRequest request,HttpServletResponse response,
			PortalDataCategoryDef portalDataCategoryDef,String id,Model model){
		portalDataCategoryDef=portalDataCategoryDefService.getPortalDataById(Integer.valueOf(id));
		model.addAttribute("pid", portalDataCategoryDef.getParent().getId());
		String imgUrl=(String)comparasService.getComparasByKey("imgServerUrl");
		model.addAttribute("imgUrl", imgUrl);
		model.addAttribute("portalDataCategoryDef", portalDataCategoryDef);
		return "modules/index/portalDataCategoryEdit";
	}
	/**
	 * 查看
	 * @param request
	 * @param response
	 * @param portalDataCategoryDef
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getDataDetail")
	public String getDataDetail(HttpServletRequest request,HttpServletResponse response,
			PortalDataCategoryDef portalDataCategoryDef,String id,Model model){
		portalDataCategoryDef=portalDataCategoryDefService.getPortalDataById(Integer.valueOf(id));
		model.addAttribute("portalDataCategoryDef", portalDataCategoryDef);
		return "modules/index/portalDataCategoryView";
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param portalDataCategoryDef
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/deletePortalData")
	public String deletePortalData(HttpServletRequest request,HttpServletResponse response,
			PortalDataCategoryDef portalDataCategoryDef,String pid,String id,Model model,RedirectAttributes redirectAttributes){
		try {
			if("0".equals(pid)){
				 portalDataCategoryDefService.deletePortalDataById(id);
				 portalDataCategoryDefService.deletePortalDataByPid(id);
			 }else{
				 portalDataCategoryDefService.deletePortalDataById(id);
			 }
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes, "删除失败");	
			e1.printStackTrace();
		}		
		 
		 return "redirect:"+"/portalData/portalDataCategoryList?";
	}
	/**
	 * 保存
	 * @param request
	 * @param response
	 * @param portalDataCategoryDef
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/savePortalData")
	public String savePortalData(HttpServletRequest request,HttpServletResponse response,
			PortalDataCategoryDef portalDataCategoryDef,String id,Model model,RedirectAttributes redirectAttributes){
		
		String parentId=request.getParameter("treeNodeId");
		if(parentId!=null&&!"".equals(parentId)){
			if("0".equals(parentId)){
				PortalDataCategoryDef portalDataCategoryDef2=portalDataCategoryDefService.getPortalDataById(Integer.valueOf(parentId));
				portalDataCategoryDef.setParent(portalDataCategoryDef2);
				portalDataCategoryDef.setLayer(1);
			}else{
				PortalDataCategoryDef portalDataCategoryDef2=portalDataCategoryDefService.getPortalDataById(Integer.valueOf(parentId));
				portalDataCategoryDef.setParent(portalDataCategoryDef2);
				portalDataCategoryDef.setLayer(2);
			}
			
		}
		try {
			portalDataCategoryDef.setChnDescription(StringEscapeUtils.unescapeHtml(portalDataCategoryDef.getChnDescription()));
			portalDataCategoryDefService.savePortalData(portalDataCategoryDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes, "保存失败");	
			e1.printStackTrace();
		}		
		
		 return "redirect:"+"/portalData/portalDataCategoryList?";
	}
	 /**
     * 上传图片文件到服务器
     * @param request
     * @param response
     */
    @RequestMapping("/imgUpload")
    public void uploadImg(HttpServletRequest request,HttpServletResponse response){
    	String imgDiv=request.getParameter("imgDiv");
    	String imgServerPath=(String)comparasService.getComparasByKey("imgServerPath");///space/pic
    	MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
		Map<String,MultipartFile> fliemap=multipartRequest.getFileMap();
		CommonsMultipartFile file = (CommonsMultipartFile)fliemap.get(imgDiv);
		String dataUrl="";
		String path="";
		String localpath="";
		String OriginalFilename = file.getOriginalFilename();
		if("imageUrl1".equals(imgDiv)){
			dataUrl="indeximage/sjxz";
		}
		path = imgServerPath +dataUrl+"/" + OriginalFilename;// 拼成上传路径
		localpath="/"+dataUrl+"/" + OriginalFilename;
		Map<String,Object> map=new HashMap<String, Object>();
		if (!"".equals(dataUrl) && null != dataUrl) {
			// 上传文件不存在则创建文件夹
			File dir = new File(imgServerPath+dataUrl);
			if (!dir.exists()) {
//				dir.mkdir();
				dir.mkdirs();
			}
			OutputStream os;
			try {
				os = new FileOutputStream(path);
				InputStream is = file.getInputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);// 将文件写入服务器
				}
				os.close();
				is.close();
				map.put("returnCode", 0);
				map.put("imgUrl", localpath);
				map.put("imgDiv", imgDiv);
			} catch (FileNotFoundException e) {
				map.put("returnCode", 1);
				map.put("imgUrl", localpath);
				map.put("imgDiv", imgDiv);
				this.logger.error("路径错误"+e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				map.put("returnCode", 1);
				map.put("imgUrl", localpath);
				map.put("imgDiv", imgDiv);
				this.logger.error("上传失败"+e.getMessage());
				e.printStackTrace();
			}
		}
		renderText(JsonMapper.toJsonString(map), response);
    }
	/**
	 * 获得资料类型树
	 * @param extId
	 * @param paramMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/getPortalDataTreeList")
	@ResponseBody
	public List<Map<String, Object>> getPortalDataTreeList(@RequestParam(required=false) Long extId, Map<String, Object> paramMap,
			HttpServletRequest request, HttpServletResponse response){
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<PortalDataCategoryDef> PortalDataCategoryDefs=portalDataCategoryDefService.findDataTwo();
		for(PortalDataCategoryDef dataCategoryDef:PortalDataCategoryDefs){
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", dataCategoryDef.getId());
			if(dataCategoryDef.getParent() == null){
				map.put("pId", -1);
			}else{
				map.put("pId", dataCategoryDef.getParent().getId());
			}
			map.put("name", dataCategoryDef.getChnName());
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	PortalDataCategoryDef[] portalDataCategoryDef = new PortalDataCategoryDef[len];
    	try {
    		for (int i = 0; i < len; i++) {
    			portalDataCategoryDef[i] = portalDataCategoryDefService.getPortalDataById(Integer.valueOf(ids[i]));
    			portalDataCategoryDef[i].setOrderNo(sorts[i]);
    			portalDataCategoryDefService.savePortalData(portalDataCategoryDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/portalData/portalDataCategoryList";
	}
}
