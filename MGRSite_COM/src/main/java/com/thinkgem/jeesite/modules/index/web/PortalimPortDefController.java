package com.thinkgem.jeesite.modules.index.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.PortalimPortDef;
import com.thinkgem.jeesite.modules.index.service.PortalimPortDefService;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;





@Controller
@RequestMapping(value="/portalimPortDef")
public class PortalimPortDefController extends BaseController{
	@Autowired
	private PortalimPortDefService portalimPortDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private ComparasService comparasService;
	
	/**
	 * 查询
	 * 按chnname查询
	 *获取数据下载服务列表并在展示页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/portalimPortDefList")
	public String portalimPortDefList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		
		String chnname=(String)paramMap.get("chnname");
		
		
//		Page<PortalimPortDef> page=null;
//		if(null!=chnname &&!"".equals(chnname)){
//			try {
//				chnname=new String(chnname.getBytes("ISO-8859-1"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			page=portalimPortDefService.getByPage(new Page<PortalimPortDef>(request,response),chnname);	
//			model.addAttribute("chnname",chnname);
//		}else{
//			page =portalimPortDefService.findPortalimPortDefPage(new Page<PortalimPortDef>(request, response));	
//		}
//		model.addAttribute("page", page);
		List<PortalimPortDef> list=null;
		if(null!=chnname &&!"".equals(chnname)){
			try {
				chnname=new String(chnname.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			list=portalimPortDefService.getByChnname(chnname);	
			model.addAttribute("chnname",chnname);
		}else{
			list =portalimPortDefService.findAll();	
		}
		
		model.addAttribute("list", list);
		return "modules/index/portalimPortDefList";
		
	}
	
	
	@RequestMapping(value="/reverseData")
	public String getReverseData(String ids,HttpServletRequest request,HttpServletResponse response,Model model){
		
//		Integer orderno1=Integer.valueOf(request.getParameter("orderno1"));
//		Integer orderno2=Integer.valueOf(request.getParameter("orderno2"));
		String[] id = ids.split(",");
		Integer id1=Integer.valueOf(id[0]);
		Integer id2=Integer.valueOf(id[1]);
		PortalimPortDef PortalimPortDef1=portalimPortDefService.getPortalimPortDef(id1);
		PortalimPortDef PortalimPortDef2=portalimPortDefService.getPortalimPortDef(id1);
		int orderno1=PortalimPortDef1.getOrderno();
		int orderno2=PortalimPortDef2.getOrderno();
		PortalimPortDef1.setOrderno(orderno2);
		PortalimPortDef2.setOrderno(orderno1);
		this.portalimPortDefService.saveppd(PortalimPortDef1);
		this.portalimPortDefService.saveppd(PortalimPortDef2);
		List<PortalimPortDef> list=portalimPortDefService.getReverseData(id1,id2);
		model.addAttribute("list", list);
		return "redirect:"+"/portalimPortDef/portalimPortDefList";
	}
	
	
	@RequestMapping(value="/toAdd")
	public String toAddportalimPortDef(HttpServletRequest request,HttpServletResponse response,Model model){
		String imgServerUrl =(String)comparasService.getComparasByKey("imgServerUrl");
		model.addAttribute("imgServerUrl", imgServerUrl);
		return "modules/index/portalimPortDefAdd";
	}
	
	@RequestMapping(value="/save")
	public String saveportalimPortDef(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
				
		 PortalimPortDef portalimPortDef=new PortalimPortDef();
		 Integer recommenditemid=portalimPortDefService.getMaxId().getRecommenditemid();
		 Integer orderno =portalimPortDefService.getMaxId().getOrderno();
		 portalimPortDef.setRecommenditemid(recommenditemid+1);
		 portalimPortDef.setOrderno(orderno+1);
		 portalimPortDef.setShowtype(1);
		 
		 String chnname=request.getParameter("chnname");
		 String layerdescription=request.getParameter("layerdescription");
		 String iconurl=request.getParameter("iconurl");
		 String linkurl=request.getParameter("linkurl");
		
		 String created=request.getParameter("created");
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 portalimPortDef.setCreated(new Date());
		 portalimPortDef.setChnname(chnname);
		 portalimPortDef.setLayerdescription(layerdescription);
		 portalimPortDef.setIconurl(iconurl);
		 portalimPortDef.setLinkurl(linkurl);
		 portalimPortDef.setInvalid(0);
		 try{
			 portalimPortDefService.saveppd(portalimPortDef);
//			 cacheCleanController.cleanCache();
			 addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
				addMessage(redirectAttributes, "保存失败");
				e.printStackTrace();
		}	
		 
		return "redirect:"+"/portalimPortDef/portalimPortDefList";	
	}
	
	@RequestMapping(value="/delete")
	public String deleteportalimPortDef(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		Integer recommenditemid=Integer.valueOf(request.getParameter("recommenditemid"));
		
		try {
			portalimPortDefService.delppd(recommenditemid);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes, "删除失败");	
			e1.printStackTrace();
		}	
		
		
		
		return "redirect:"+"/portalimPortDef/portalimPortDefList?recommenditemid="+recommenditemid;	
	}
				
	@RequestMapping(value="/toEdit")
	public String toEditportalimPortDef(HttpServletRequest request,HttpServletResponse response,Model model){
		Integer recommenditemid=Integer.valueOf(request.getParameter("recommenditemid"));
		PortalimPortDef portalimPortDef=portalimPortDefService.findPortalimPortDefById(recommenditemid);   	  		
		String imgServerUrl =(String)comparasService.getComparasByKey("imgServerUrl");
		model.addAttribute("imgServerUrl", imgServerUrl);
		model.addAttribute("recommenditemid", recommenditemid);
		model.addAttribute("portalimPortDef", portalimPortDef);
		return "modules/index/portalimPortDefEdit";
	}
	
	@RequestMapping(value="/edit")
	public String editportalimPortDef(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		 
		
		 Integer recommenditemid=Integer.valueOf(request.getParameter("recommenditemid"));
		 PortalimPortDef portalimPortDef=portalimPortDefService.findPortalimPortDefById(recommenditemid);
		 portalimPortDef.setRecommenditemid(recommenditemid);
		
		 portalimPortDef.setShowtype(1);
		 
		 String chnname=request.getParameter("chnName");
		 String layerdescription=request.getParameter("layerdescription");
		 String iconurl=request.getParameter("iconurl");
		 String linkurl=request.getParameter("linkurl");
		 Integer invalid=Integer.valueOf(request.getParameter("invalid"));
		 Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		 portalimPortDef.setChnname(chnname);
		 portalimPortDef.setLayerdescription(layerdescription);
		 portalimPortDef.setIconurl(iconurl);
		 portalimPortDef.setLinkurl(linkurl);
		 portalimPortDef.setInvalid(invalid);
		 portalimPortDef.setOrderno(orderno);
		 portalimPortDef.setUpdated(new Date());
		try{
			portalimPortDefService.saveUpdate(portalimPortDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}			
		
		return "redirect:"+"/portalimPortDef/portalimPortDefList?recommenditemid="+recommenditemid;	
	}
	
	
	/**
     * 上传图片文件到服务器
     * @param request
     * @param response
     */
    @RequestMapping(value="/imgUpload")
    public void uploadImg(HttpServletRequest request,HttpServletResponse response){
    	String imgDiv=request.getParameter("imgDiv");
    	//String imgServerUrl =(String)comparasService.getComparasByKey("imgServerUrl");
    	//图片服务器的路径
    	String imgServerPath=(String)comparasService.getComparasByKey("imgServerPath");// /space/pic/
    	MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
		Map<String,MultipartFile> fliemap=multipartRequest.getFileMap();
		CommonsMultipartFile file = (CommonsMultipartFile)fliemap.get(imgDiv);
		String OriginalFilename = file.getOriginalFilename();
		
		String dataUrl="indeximage/zdlmdz";
		
		String path = imgServerPath +dataUrl+"/" + OriginalFilename;// 拼成上传路径
		String localpath="/"+dataUrl+"/"+OriginalFilename;
		
		Map<String,Object> map=new HashMap<String, Object>();
		if (!"".equals(dataUrl) && null != dataUrl) {
			// 上传文件不存在则创建文件夹
			File dir = new File(imgServerPath+dataUrl);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			OutputStream os;
			try {
				os = new FileOutputStream(path);
				InputStream is = file.getInputStream();
				int len = 0;
				byte[] buffer = new byte[8192];
				while ((len = is.read(buffer, 0, 8192)) !=-1) {
					os.write(buffer, 0, len);// 将文件写入服务器
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
	 * 批量修改菜单排序
	 */
	@RequestMapping(value="/updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	PortalimPortDef[] portalimPortDef = new PortalimPortDef[len];
    	try {
    		for (int i = 0; i < len; i++) {
    			portalimPortDef[i] = portalimPortDefService.findPortalimPortDefById(Integer.valueOf(ids[i]));
    			portalimPortDef[i].setOrderno(sorts[i]);
    			portalimPortDefService.saveppd(portalimPortDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/portalimPortDef/portalimPortDefList";
	}
}
