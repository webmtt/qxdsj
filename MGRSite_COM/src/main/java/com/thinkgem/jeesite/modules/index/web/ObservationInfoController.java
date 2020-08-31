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
import com.thinkgem.jeesite.modules.index.entity.ObservationInfo;
import com.thinkgem.jeesite.modules.index.service.ObservationInfoService;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;




@Controller
@RequestMapping(value="/observationInfo")
public class ObservationInfoController extends BaseController{
	@Autowired
	private ObservationInfoService observationInfoService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private ComparasService comparasService;

	
	@RequestMapping(value="/list")
	public String observationInfoList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,Model model){
		String chnname=(String)paramMap.get("chnname");
		List<ObservationInfo> list=null;
		if(null!=chnname &&!"".equals(chnname)){
			try {
				chnname=new String(chnname.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			list=observationInfoService.getByChnname(chnname);	
			model.addAttribute("chnname",chnname);
		}else{
			list =observationInfoService.findAll();	
		}
		model.addAttribute("list", list);
		return "modules/index/observationInfoList";
	}

	/**
	 * 跳转至编辑类型界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
    @RequestMapping(value="/toEdit")
	public String toEditObservationInfo(HttpServletRequest request, HttpServletResponse response, Model model){
    	String id=request.getParameter("id");
    	ObservationInfo observationInfo=observationInfoService.findObservationInfoById(id);   	  		
    	String imgServerUrl =(String)comparasService.getComparasByKey("imgServerUrl");
    	model.addAttribute("id", id);
    	model.addAttribute("imgServerUrl", imgServerUrl);
    	model.addAttribute("observationInfo", observationInfo);
		return "modules/index/observationInfoEdit";
	}
    
    @RequestMapping(value="/edit")
	public String editObservationInfo(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		 ObservationInfo observationInfo=new ObservationInfo();
		
		 String id=request.getParameter("id");
		 observationInfo.setId(id);
		 
		 String chnname=request.getParameter("chnname");
		 String shortchnname=request.getParameter("shortchnname");
		 String imgurl=request.getParameter("imgurl");
		 String linkurl=request.getParameter("linkurl");
		 Integer invalid=Integer.valueOf(request.getParameter("invalid"));
		 Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		 Integer procount=Integer.valueOf(request.getParameter("procount"));
		 Integer pastdate=Integer.valueOf(request.getParameter("pastdate"));
		
		 observationInfo.setChnname(chnname);
		 observationInfo.setShortchnname(shortchnname);
		 observationInfo.setImgurl(imgurl);
		 observationInfo.setLinkurl(linkurl);
		 observationInfo.setInvalid(invalid);
		 observationInfo.setOrderno(orderno);
		 observationInfo.setProcount(procount);
		 observationInfo.setPastdate(pastdate);
		
		 String created=request.getParameter("created");
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if(null!=created &&!"".equals(created)){
			model.addAttribute("created", created);
			try {
				observationInfo.setCreated(sdf.parse(created));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			Calendar c=Calendar.getInstance();
			try {
				observationInfo.setCreated(sdf.parse(sdf.format(c.getTime())));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		
		try{
			observationInfoService.saveUpdate(observationInfo);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}			
		
		return "redirect:"+"/observationInfo/list?id="+id;	
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
		
		String dataUrl="indeximage/gcyw";///indeximage/gcyw/tankong.png
		
		String path = imgServerPath +dataUrl+"/" + OriginalFilename;// 拼成上传路径
//		String path = imgServerUrl +dataUrl+"/" + OriginalFilename;// 拼成上传路径
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
    
	@RequestMapping(value="/delete")
	public String deleteObservationInfo(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		
		 String id=request.getParameter("id");
    	
		try {
			observationInfoService.deleteObservationInfo(id);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes, "删除失败");	
			e1.printStackTrace();
		}
    	return "redirect:"+"/observationInfo/list";	
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value="/updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	ObservationInfo[] observationInfo = new ObservationInfo[len];
    	try {
    		for (int i = 0; i < len; i++) {
    			observationInfo[i] = observationInfoService.findObservationInfoById(ids[i]);
    			observationInfo[i].setOrderno(sorts[i]);
    			observationInfoService.saveObservationInfoData(observationInfo[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/observationInfo/list";
	}
	
    
}
