package com.thinkgem.jeesite.modules.index.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.index.entity.PortalDataCategoryDef;
import com.thinkgem.jeesite.modules.index.entity.PortalImageProductDef;
import com.thinkgem.jeesite.modules.index.entity.PortalImageRull;
import com.thinkgem.jeesite.modules.index.entity.PortalImageRullModel;
import com.thinkgem.jeesite.modules.index.service.PortalDataCategoryDefService;
import com.thinkgem.jeesite.modules.index.service.PortalImageProductDefService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;




@Controller
@RequestMapping(value="/portalImage")
public class PortalImageProductController extends BaseController{
	@Autowired
	private PortalImageProductDefService portalImageProductDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private ComparasService comparasService;
	protected Logger logger = LoggerFactory.getLogger(PortalImageProductController.class);
	/**
	 *获取数据下载服务列表并在展示页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/portalImagePorductList")
	public String dataServiceList(HttpServletRequest request,HttpServletResponse response,
			PortalImageProductDef portalImageProductDef,String id,Model model){
		Comparas comparas=comparasService.getComparas("imgServerUrl");
		List<PortalImageProductDef> list=portalImageProductDefService.findAll();
		model.addAttribute("imgServiceUrl", comparas.getStringvalue());	
		model.addAttribute("list", list);		
		model.addAttribute("portalImageProductDef", portalImageProductDef);
		return "modules/index/portalImagePorductList";
	}
	@RequestMapping(value="/getTypeList")
	public String getTypeList(HttpServletRequest request,HttpServletResponse response,
			PortalImageProductDef portalImageProductDef,String id,Model model){
		List<PortalImageRullModel> list=portalImageProductDefService.getAllType();
		String chk_value="";
		for(int i=0;i<list.size();i++){
			if(list.get(i).getInvalid()==0){
				chk_value+=list.get(i).getType()+",";
			}
			
		}
		model.addAttribute("chk_value", chk_value);		
		model.addAttribute("list", list);		
		return "modules/index/portalImageRullList";
	}
	/**
	 * 根据类型获得列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getProductList")
	public String getProductList(HttpServletRequest request,HttpServletResponse response,
			String type,Model model){
		List<Object[]> list=portalImageProductDefService.getProductList(type);
		model.addAttribute("list", list);	
		model.addAttribute("types", type);	
		return "modules/index/portalImagePorductList";
	}
	@RequestMapping(value="/publicPortalImage")
	public String publicPortalImage(HttpServletRequest request,HttpServletResponse response,
			String type,Model model, RedirectAttributes redirectAttributes){
		try {
			portalImageProductDefService.updateAll();
			portalImageProductDefService.updatePortalImageRull(type);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "发布成功!");
		} catch (Exception e) {
			// TODO: handle exception
			addMessage(redirectAttributes, "发布失败!");
		}
		return "redirect:"+"/portalImage/getTypeList";
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
	@RequestMapping(value="/editPortalImage")
	public String editPortalData(String type,HttpServletRequest request,HttpServletResponse response,String id,Model model){
		PortalImageProductDef portalImageProductDef =portalImageProductDefService.getPortalImageById(Integer.valueOf(id));
		String imgServerUrl =(String)comparasService.getComparasByKey("imgServerUrl");
		model.addAttribute("imgServerUrl", imgServerUrl);
		model.addAttribute("type", type);
		model.addAttribute("portalImageProductDef", portalImageProductDef);
		//0标识放在图片服务器
		if(portalImageProductDef.getIsStatic()==0){
			return "modules/index/portalImageEdit2";
		}else{
			return "modules/index/portalImageEdit";
		}
		
	}
	/**
     * 上传图片文件到服务器
     * @param request
     * @param response
     */
    @RequestMapping(value="/imgUpload")
    public void uploadImg(HttpServletRequest request,HttpServletResponse response){
    	String imgDiv=request.getParameter("imgDiv");
    	String id=request.getParameter("id");
    	//String imgServerUrl =(String)comparasService.getComparasByKey("imgServerUrl");
    	//图片服务器的路径
    	String imgServerPath=(String)comparasService.getComparasByKey("imgServerPath");// /space/pic/
    	MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
		Map<String,MultipartFile> fliemap=multipartRequest.getFileMap();
		CommonsMultipartFile file = (CommonsMultipartFile)fliemap.get(imgDiv);
		String OriginalFilename = file.getOriginalFilename();
		Date date=new Date();
		String time=Math.floor(date.getTime()/1000)+"";
		OriginalFilename=OriginalFilename.replace(OriginalFilename.substring(0, OriginalFilename.lastIndexOf(".")), time+"_"+id);
		String dataUrl="indeximage/zyyw";
		
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
	 * 查看
	 * @param request
	 * @param response
	 * @param portalDataCategoryDef
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/viewPortalImage")
	public String viewPortalImage(String type,HttpServletRequest request,HttpServletResponse response,String id,Model model){
		PortalImageProductDef portalImageProductDef =portalImageProductDefService.getPortalImageById(Integer.valueOf(id));
		model.addAttribute("portalImageProductDef", portalImageProductDef);
		model.addAttribute("type", type);
		return "modules/index/portalImageView";
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
	@RequestMapping(value="/savePortalImage")
	public String savePortalImage(HttpServletRequest request,HttpServletResponse response,
			PortalImageProductDef portalImageProductDef,String id,Model model,RedirectAttributes redirectAttributes){
		String type=request.getParameter("type");
		try {
			portalImageProductDef.setTitle(StringEscapeUtils.unescapeHtml(portalImageProductDef.getTitle()));
			portalImageProductDefService.savePortalImage(portalImageProductDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes, "保存失败");	
			e1.printStackTrace();
		}		
		
		 return "redirect:"+"/portalImage/getProductList?type="+type;
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value="/updateSort")
	public String updateSort(String[] ids, Integer[] sorts,String type, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	PortalImageRull[] portalImageRull = new PortalImageRull[len];
    	try {
    		for (int i = 0; i < len; i++) {
    			portalImageRull[i] = portalImageProductDefService.getPortalImageRull(ids[i]);
    			portalImageRull[i].setOrderNo(sorts[i]);
    			portalImageProductDefService.savePortalImageRull(portalImageRull[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/portalImage/getProductList?type="+type;
	}
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value="/publish")
	public String publish(String[] ids, Integer[] startTimes,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		User currentUser = UserUtils.getUser();
		String logginName = currentUser.getLoginName();
		logger.error("PortalImageProductController-------logginName"+logginName+";Operation area:publish;"+"currentTime:"+new Date());
		String types = request.getParameter("types");
		String[] type=types.split(",");
		int len = ids.length;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	try {
    		for (int i = 0; i < len; i++) {
    			List<PortalImageRull> list=portalImageProductDefService.getPortalImageRullListByType(ids[i]);
    			for(int j=0;j<list.size();j++){
    				PortalImageRull pr=list.get(j);
    				if("Rainstorm".equals(pr.getType())){
    					String Rainstorm = request.getParameter("Rainstorm");
    					pr.setArea(Rainstorm);
    				}else if("Typhoon".equals(pr.getType())){
    					String Typhoon = request.getParameter("Typhoon");
    					pr.setArea(Typhoon);
    				}
    				if(startTimes[i]!=null){
    					pr.setStartTime(sdf.parse(startTimes[i].toString()));
    					portalImageProductDefService.savePortalImageRull(pr);
    				}else{
    					pr.setStartTime(null);
    					portalImageProductDefService.savePortalImageRull(pr);
    				}
    				
    			}
        	}
    		portalImageProductDefService.updateAll();
    		for (int i = 0; i < type.length; i++) {
    			portalImageProductDefService.updatePortalImageRull(type[i]);
        	}
//        	cacheCleanController.cleanCache();
        	List<PortalImageRull> list=portalImageProductDefService.getPortalImageRullList();
        	for(int i=0;i<list.size();i++){
        		CacheMapUtil.removeCache2("idata_", list.get(i).getId()+list.get(i).getTid()+"_datacode");
        	}
        	addMessage(redirectAttributes, "发布成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "发布失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/portalImage/getTypeList";
	}
}
