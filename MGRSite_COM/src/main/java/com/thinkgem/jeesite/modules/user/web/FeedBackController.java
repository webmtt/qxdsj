/*
 * @(#)FeedBackController.java 2016年5月30日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.user.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.user.entity.TargetUnit;
import com.thinkgem.jeesite.modules.user.entity.UserFeedBack;
import com.thinkgem.jeesite.modules.user.service.FeedBackService;
import com.thinkgem.jeesite.modules.user.service.TargetUnitService;



/**
 * 描述：反馈
 *
 * @author Administrator
 * @version 1.0 2016年5月30日
 */
@Controller
@RequestMapping(value = "feed")
public class FeedBackController extends BaseController{
	@Autowired
	private FeedBackService feedBackService;
	@Autowired
	private TargetUnitService targetUnitService;
	@Resource
	private ComparasDao comparasDao;
	/**
	 * 跳转至添加反馈信息页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request, HttpServletResponse response,Model model,UserFeedBack userFeedBack,String fDStatus){			
		String orderBy = request.getParameter("orderBy");
		User currentUser = UserUtils.getUser();
		String logginName = currentUser.getLoginName();
		TargetUnit TargetUnit=this.targetUnitService.findTargetByloginName(logginName);
		if(fDStatus==null||"".equals(fDStatus)){
			fDStatus="0";
			if(TargetUnit!=null){
				userFeedBack.setUnitId(Integer.valueOf(TargetUnit.getId()));
			}
		}
		if(TargetUnit!=null){
	    	model.addAttribute("TargetUnitId", TargetUnit.getId());
		}else{
	    	model.addAttribute("TargetUnitId", 0);
		}
		Page<UserFeedBack> page = feedBackService.findUserFeedBackPage(
				new Page<UserFeedBack>(request, response), userFeedBack, orderBy,fDStatus);
		    model.addAttribute("userFeedBack", userFeedBack);
		    model.addAttribute("fDStatus", fDStatus);
	        model.addAttribute("page", page);
	        return "modules/user/feedBackList";				
	}
	/**
	 * 获得反馈信息列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/form")
	public String form(HttpServletRequest request, HttpServletResponse response,Model model,UserFeedBack userFeedBack){
		String id=userFeedBack.getId();
		UserFeedBack userFeedBack1=feedBackService.getUserFeedBack(id);
		model.addAttribute("userFeedBack", userFeedBack1);
		return "modules/user/feedBackForm";
	}
	/**
	 * 查看反馈信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/view")
	public String view(HttpServletRequest request, HttpServletResponse response,Model model,UserFeedBack userFeedBack){
		String id=userFeedBack.getId();
		UserFeedBack userFeedBack1=feedBackService.getUserFeedBack(id);
		model.addAttribute("userFeedBack", userFeedBack1);
		return "modules/user/feedBackView";
	}
	/**
	 * 获得反馈信息列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/save")
	public String save(HttpServletRequest request, HttpServletResponse response,Model model,UserFeedBack userFeedBack, RedirectAttributes redirectAttributes){
		String id=userFeedBack.getId();
		String respContext=request.getParameter("respContext");
		UserFeedBack userFeedBack1=feedBackService.getUserFeedBack(id);
		userFeedBack1.setRespContext(respContext);
		//已经回复
		userFeedBack1.setfDStatus(1);
		User currentUser = UserUtils.getUser();
		String logginName = currentUser.getLoginName();
		userFeedBack1.setRespUserID(logginName);
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		userFeedBack1.setRespTime(sdf.format(date));
		feedBackService.save(userFeedBack1);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+"/feed/list";
	}
	/**
	 * 删除已完成指定反馈信息
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/delFeedById")
	public String delFeedBackById(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		String id=request.getParameter("id");
		//String userId=request.getParameter("userId");
		try {
			feedBackService.delFeedBackById(id);
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
			e.printStackTrace();
		}
		return "redirect:"+"/feed/list";
	}
	/**
	 * 修改反馈信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editFeed")
	public String editFeedBack(HttpServletRequest request, HttpServletResponse response,Model model){
		String id=request.getParameter("id");
		String userId=request.getParameter("userId");
		UserFeedBack uf=feedBackService.getFeedBackByIDS(id,userId);
		String fdContext=uf.getfDContext();
		String resContext=uf.getRespContext();
		if(fdContext!=null){
			fdContext=fdContext.replaceAll("<p>", "");
			fdContext=fdContext.replaceAll("</p>", "");
			uf.setfDContext(fdContext.trim());			
		}
		if(resContext!=null){
			resContext=resContext.replaceAll("<p>", "");
			resContext=resContext.replaceAll("</p>", "");
			uf.setRespContext(resContext.trim());			
		}
		model.addAttribute("feedBack", uf);
		return "modules/data/feedBackEdit";
	}
	/**
	 * 保存修改的反馈信息
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/update")
	public String updateFeedBackInfo(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
	 String title=request.getParameter("fdTitle");
	 String context=request.getParameter("fdContext");
	 String id=request.getParameter("Id");
	 String userId=request.getParameter("userId");
	try {
		context=new String(context.getBytes("ISO-8859-1"),"UTF-8");
		title=new String(title.getBytes("ISO-8859-1"),"UTF-8");
	} catch (UnsupportedEncodingException e1) {
		e1.printStackTrace();
	}
		 try {
			 feedBackService.updateFeedBackInfo(title,context,id,userId);
			 addMessage(redirectAttributes, "保存成功");
		 } catch (Exception e) {
			 addMessage(redirectAttributes, "保存失败");
			 e.printStackTrace();
		 }		 		
		 return "redirect:"+"/sys/comparas/list";
	}
	
	/**
	 * 上传内容中的图片
	 * @param upload
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/imgUplad")
	public void imgUplaod(@RequestParam("upload")
	MultipartFile upload, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//将文件存到/user/feedback目录中,以反馈的id作为文件夹，该反馈的图片都存到该目录下
		request.setCharacterEncoding("utf-8");
		response.setContentType("charset=utf-8");
		String id=request.getParameter("id");
		PrintWriter out = response.getWriter();
		String filePath="";
		String fileURL="";
		String imgServerPath=((String)comparasDao.getComparasByKey("imgServerPath")).trim(); 
		String userPath=((String)comparasDao.getComparasByKey("userPath")).trim();  
		String imgServerUrl=((String)comparasDao.getComparasByKey("imgServerUrl")).trim();  
		
		String callback=request.getParameter("CKEditorFuncNum"); 
		String contentType = upload.getContentType();
		String extName=FileUtils.getExtensionName(upload.getOriginalFilename());
		 
		String docId = UUID.randomUUID().toString();// 将文件名改成用guid表示
		filePath= imgServerPath+userPath+id+"/"+docId+"."+extName;
		fileURL=(imgServerUrl+"/"+userPath+id+"/"+docId+"."+extName).replace("\\", "/");
		if (!upload.isEmpty()) {
			try {
				if ("image/pjpeg".equals(contentType) || ("image/jpeg".equals(contentType)) || "image/png".equals(contentType) || "image/x-png".equals(contentType) || "image/gif".equals(contentType) || "image/bmp".equals(contentType)) {
					File dir = new File(imgServerPath+userPath+id+"/");
					if (!dir.exists()) { 
						dir.mkdirs();
						System.out.println("dirs created: "+imgServerPath+userPath+id+"/");
					}
					InputStream stream = upload.getInputStream();// 把文件读入
					  
					   ByteArrayOutputStream baos = new ByteArrayOutputStream();
					   
					   OutputStream bos = new FileOutputStream(filePath);
					   
					   // 建立一个上传文件的输出流
					   int bytesRead = 0;
					   byte[] buffer = new byte[8192];
					   while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					    bos.write(buffer, 0, bytesRead);// 将文件写入服务器
					   }
					   bos.close();
					   stream.close();  
				} else {
					out.println("<script type=\"text/javascript\">");
					out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'','文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
					out.println("</script>");
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		        fileURL=(imgServerUrl+"/"+userPath+id+"/"+docId+"."+extName).replace("\\", "/");
		        out.println("<script type=\"text/javascript\">");
		        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback+ ",'" + fileURL+"','')");
		        out.println("</script>");
		}
	} 
}
