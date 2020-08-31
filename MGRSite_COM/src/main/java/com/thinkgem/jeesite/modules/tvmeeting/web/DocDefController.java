/*
 * @(#)DocDefController.java 2016-1-27
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.tvmeeting.entity.DocDef;
import com.thinkgem.jeesite.modules.tvmeeting.service.DocDefService;

/**
 * 描述：
 *
 * @author huanglei
 * @version 1.0 2016-1-27
 */
@Controller
@RequestMapping(value="tvmeeting/techdoc")
public class DocDefController extends BaseController{
	@Autowired
	private DocDefService docDefService;
	@Autowired
	private ComparasDao comparasDao;

	@RequestMapping(value="add")
	public String add(Model model){
		model.addAttribute("flag", "add");
		return "modules/tvmeeting/DocDefAdd";
	}
	
	//上传文件
	@RequestMapping(value="/upload")
	public String upload(MultipartFile file,RedirectAttributes redirectAttributes,
			HttpServletResponse response,HttpServletRequest request, Model model) throws Exception {
		// 文件类型
		String docDefName=request.getParameter("docDefName");
		String type = file.getContentType();
		String fileName=file.getOriginalFilename();
		String docId=UUID.randomUUID().toString();
		long size=file.getSize();
//		System.out.println("size=========:"+size);
		String imgServerPath=(String)comparasDao.getComparasByKey("imgServerPath");
		String docDefUrl=(String)comparasDao.getComparasByKey("docDefUrl");
		//String docDefUrl = "C:\\Users\\huanglei\\Desktop\\m\\doc\\";
		if (docDefUrl != null && !"".equals(docDefUrl)) {
			if ("application/msword".equals(type) || "application/pdf".equals(type)
					|| "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(type)) {
					try {
						// 上传文件不存在则创建文件夹
						File dir = new File(docDefUrl);
						if (!dir.exists()) {
							dir.mkdir();
						}
						OutputStream os = new FileOutputStream(imgServerPath+docDefUrl + docId+"."+FileUtils.getExtensionName(fileName));
						InputStream is = file.getInputStream();
						int bytesRead = 0;
						byte[] buffer = new byte[8192];
						while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
							os.write(buffer, 0, bytesRead);// 将文件写入服务器
						}
						os.close();
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 日期格式
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String uploadDate = sdf.format(new Date());
					// 保存实体
					DocDef docDef = new DocDef();
					docDef.setDocId(docId);
					docDef.setDocName(new String(docDefName.getBytes("ISO-8859-1"), "utf-8"));
					if(fileName.lastIndexOf(".")!=-1){
						docDef.setDocUrl(docDefUrl + docId+"."+FileUtils.getExtensionName(fileName));
					}else{
						docDef.setDocUrl(docDefUrl+docId);
					}
					
					docDef.setUploadDate(uploadDate);
					docDef.setInvalid(0);
					docDef.setCreated(new Date());
					try {
						docDef.setCreatedBy(InetAddress.getLocalHost().getHostAddress());
						docDef.setUpdatedBy(InetAddress.getLocalHost().getHostAddress());
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					docDef.setUpdated(new Date());
					docDefService.save(docDef);
					addMessage(redirectAttributes, "添加" + new String(docDefName.getBytes("iso8859-1"), "utf-8") + "文档成功");
				}
			} else {
				model.addAttribute("flag", "add");
				model.addAttribute("errorInfo", "文件格式不正确（必须为word、pdf文件）");
				return "modules/tvmeeting/DocDefAdd";
			}
		return "redirect:" + "/tvmeeting/techdoc/";
	}
		
	
	
	//捕获上传文件过大问题
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex,HttpServletRequest request){
			Map<String,Object>	model = new HashMap<String,Object>();
			if(ex instanceof MaxUploadSizeExceededException){
				long fileSize=Long.parseLong(Global.getConfig("web.maxUploadSize"))/1024/1024;
				model.put("flag", "add");
				model.put("errorInfo", "文件不能超过"+String.valueOf(fileSize)+"M");
			}
		return new ModelAndView("modules/tvmeeting/DocDefAdd",(Map)model);
	}
	
	
	@RequestMapping(value="delete")
	public String delete(String docId,String docDefUrl){
		//删除实体类
		DocDef docDef=new DocDef();
		docDef.setDocId(docId);
		docDefService.deleteById(docId);
		
		//删除服务器上的文件
		File file=new File(docDefUrl);
		if(file.isFile() && file.exists()){
			file.delete();
		}		
		return "redirect:"+"/tvmeeting/techdoc/";
	}
	
	@RequestMapping(value="findDocDef")
	public String  findDocDef(String docId,Model model){
		DocDef docDef=docDefService.findDocDef(docId);
		String docName=docDef.getDocName();
		model.addAttribute("flag", "update");
		model.addAttribute("docName", docName);
		model.addAttribute("docId", docId);
		return "modules/tvmeeting/DocDefAdd";
	}
	
	@RequestMapping(value="update")
	public String update(String docName,String docId){	
		System.out.println(docName+"  "+docId);
		docDefService.update(docName,docId);
		return "redirect:"+"/tvmeeting/techdoc/";
	}
	
	@RequestMapping(value = {"list",""})
	public String list(DocDef docDef,HttpServletRequest request,HttpServletResponse response,Model model){		
		Page<DocDef> page = docDefService.findDocDefPage(new Page<DocDef>(request, response), docDef);
		model.addAttribute("page", page);
		model.addAttribute("docDef", docDef);
		return "modules/tvmeeting/docDefList";
	}
}
