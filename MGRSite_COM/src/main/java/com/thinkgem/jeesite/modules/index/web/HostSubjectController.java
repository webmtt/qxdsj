/*
 * @(#)HostSubjectController.java 2016-8-25
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.entity.HostSubject;
import com.thinkgem.jeesite.modules.index.service.HostSubjectService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.tvmeeting.service.DocDefService;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-8-25
 */

@Controller
@RequestMapping(value = "hotsubject/hotsubjectarrange")
public class HostSubjectController extends  BaseController{
	@Autowired
	private HostSubjectService hsbService;
	@Resource
	private ComparasDao comparasDao;
	@Autowired
	private DocDefService docDefService;
	@RequestMapping("/hotsubjectList")
	public String HostsubjectList(@RequestParam
			String area,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		area=request.getParameter("area");
		List<HostSubject> lists=hsbService.find(area);
		model.addAttribute("lists", lists);
		model.addAttribute("area", area);
		return "modules/hotsubject/HotsubjectList";		
	}
	
	@RequestMapping("/hotsubjectAdd")
	public String ColumnAdd(HttpServletRequest request,HttpServletResponse response,Model model,HostSubject host){
		int maxUploadSize=Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
		String area=request.getParameter("area");
		model.addAttribute("maxUploadSize",maxUploadSize/1024/1024);
		model.addAttribute("host",host);
		model.addAttribute("area",area);
		return "modules/hotsubject/HotsubjectAdd";		
	}	
	@RequestMapping("/teleDel")
	public String TeleDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
			Model model){
		String area=request.getParameter("area");
		
		String id=request.getParameter("id");
		hsbService.DelHost(Integer.parseInt(id),area);
		cleanSuccess();
		addMessage(redirectAttributes, "删除成功");
		//return "redirect:"+"/tvmeeting/meetingarrange/teleList";
		return "redirect:"+"/hotsubject/hotsubjectarrange/hotsubjectList?area="+area+"";		
	}
	@RequestMapping("/hotsubjectSave")
	public String SaveColumn(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		String id=request.getParameter("ids");
		Integer Id=Integer.parseInt(id);
		HostSubject host=hsbService.findByHost(Id);
		
		String orderno=request.getParameter("orderno");
		String title=request.getParameter("title");
		String linkurl=request.getParameter("linkurl");
		
		String area=request.getParameter("areaItems");
		String arr=","+area+",";
		
		Integer invalid=0;
		host.setAreaItem(arr);
		host.setOrderno(Integer.parseInt(orderno));
		host.setTitle(title);
		host.setInvalid(invalid);
		host.setLinkurl(linkurl);
		hsbService.SaveHost(host);
		hsbService.updateHost(Integer.parseInt(orderno), 0, Id);
		cleanSuccess();
		addMessage( redirectAttributes, "保存成功");
		return "redirect:"+"/hotsubject/hotsubjectarrange/hotsubjectList?area="+area+"";	
		
	}
	
	@RequestMapping("/saveAttach")  
	public void saveAttach(
			HttpServletRequest request, HttpServletResponse response, Model model) {
		 String imgServerPath=(String)comparasDao.getComparasByKey("docHostPath");
		 String docDefUrl=(String)comparasDao.getComparasByKey("hostPath");
//		String imgServerPath = "D://";
//		String docDefUrl = "test/";
		 String area = request.getParameter("area");
		MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
		Map<String,MultipartFile> fliemap=multipartRequest.getFileMap();
		CommonsMultipartFile file = (CommonsMultipartFile)fliemap.get("file");	
		long l=file.getSize();
		int maxUploadSize=Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
		Map<String, Object> map = new HashMap<String, Object>();
		//ColumnItemsDef column=new ColumnItemsDef();
		HostSubject host=new HostSubject();
		
		//ColumnItemsDef list=new ColumnItemsDef();
		HostSubject list=new HostSubject();
		
		//DocDef docDef = new DocDef();
		//List<DocDef> list = new ArrayList<DocDef>();
		if(l>maxUploadSize){
			map.put("area", area);
			map.put("message", "文件大小超过"+maxUploadSize/1024/1024+"M，请联系管理员！");	
		}else{			
			
			String OriginalFilename = file.getOriginalFilename();
			this.logger.info("OriginalFilename:"+OriginalFilename);
			int index = OriginalFilename.lastIndexOf(".");
			String fileName = OriginalFilename.substring(0, index);
			String last = OriginalFilename.substring(index + 1, OriginalFilename.length());
			String path = imgServerPath + docDefUrl + fileName + "." + last;// 拼成上传路径
			String localpath="/"+docDefUrl + fileName + "." + last;
			boolean flag = false;
			if (!"".equals(docDefUrl) && null != docDefUrl) {
				// 上传文件不存在则创建文件夹
				File dir = new File(docDefUrl);
				if (!dir.exists()) {
					dir.mkdir();
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
					flag = true;
				} catch (FileNotFoundException e) {
					flag = false;
					this.logger.error("路径错误"+e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					flag = false;
					this.logger.error("上传失败"+e.getMessage());
					e.printStackTrace();
				}
			}
			   
			
			  host.setImageurl(localpath);
			  host.setOrderno(1);
			  host.setInvalid(0);
			  host.setAreaItem(","+area+",");
			  hsbService.SaveHost(host);
			  cleanSuccess();
			    
//			    column.setColumnImageURL(localpath);
//			    column.setChnName("1111");
//			    column.setLinkURL("1111");
//			    column.setShowType(1);
//			    column.setOrderNo(1);
//			    column.setInvalid(1);
//			    column.setColumnType("1111");
//			    column.setAreaItem(","+area+",");
//			    colService.SaveCol(column);
				if (flag) {
					
					//docDefService.save(docDef);
					//list = docDefService.findDocDefList(id);
					//list=colService.findByColumn(column.getColumnItemID());
			     	list=	hsbService.findByHost(host.getId());
					map.put("list", list);
					map.put("fileName", fileName);
					map.put("id",host.getId());
					map.put("message", "上传成功");
					map.put("area",area);
					
				} else {
					
					map.put("list",list);
					map.put("fileName", fileName);
					map.put("id", host.getId());
					map.put("message", "上传失败");
					map.put("area",area);
				
				}
		}
	
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	
	@RequestMapping("/delById")
	public void delAttach(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		String docId=request.getParameter("docId");
		
		String docUrl=request.getParameter("docUrl");
		 String imgServerPath=(String)comparasDao.getComparasByKey("docHostPath");
			
		boolean flag=false;
	File file=new File(imgServerPath+"/"+docUrl);
		try {
		//file.delete();
		hsbService.DelById(Integer.parseInt(docId));
		cleanSuccess();
		flag=true;
	} catch (Exception e) {
		flag=false;
		e.printStackTrace();
		}
		Map<String,Object> m=new HashMap<String, Object>();
		if(flag){
			try {
			
				flag=true;
				if(flag){
					m.put("ID", docId);
					m.put("flag", "true");
				}else{
					m.put("ID", docId);
					m.put("flag", "false");
				}
				
			} catch (Exception e) {
				flag=false;
				e.printStackTrace();
			}
		}else{
			m.put("ID", docId);
			m.put("flag", "false");
		}
		String json=JsonMapper.toJsonString(m);
		renderText(json, response);
	}
	public String cleanSuccess(){
		String message="";
		String cachePath=(String)comparasDao.getComparasByKey("cacheCleanUrl");
		System.out.println(cachePath);
		if(cachePath.contains(";"))
		{
			String[] urls=cachePath.split(";");
			for(String url: urls)
			{
				if(visitUrl(url).equals("1"));
				message="清理成功 ";
			}
		}
		else
		{
			message= visitUrl(cachePath).equals("1")? "清理成功":"";
		}
		 
	    return message;	 
    }
	public String visitUrl(String cachePath)
	{
		URL postUrl;
		try {
			postUrl = new URL(cachePath);
			 HttpURLConnection connection = (HttpURLConnection) postUrl
		                .openConnection();
		        // Output to the connection. Default is
		        // false, set to true because post
		        // method must write something to the
		        // connection
		        // 设置是否向connection输出，因为这个是post请求，参数要放在
		        // http正文内，因此需要设为true
		        connection.setDoOutput(true);
		        // Read from the connection. Default is true.
		        connection.setDoInput(true);
		        // Set the post method. Default is GET
		        connection.setRequestMethod("POST");
		        // Post cannot use caches
		        // Post 请求不能使用缓存
		        connection.setUseCaches(false);
		        // This method takes effects to
		        // every instances of this class.
		        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
		        // connection.setFollowRedirects(true);
		 
		        // This methods only
		        // takes effacts to this
		        // instance.
		        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
		        connection.setInstanceFollowRedirects(true);
		        // Set the content type to urlencoded,
		        // because we will write
		        // some URL-encoded content to the
		        // connection. Settings above must be set before connect!
		        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
		        // 进行编码
		        connection.setRequestProperty("Content-Type",
		                "application/x-www-form-urlencoded");
		        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		        // 要注意的是connection.getOutputStream会隐含的进行connect。
		        connection.connect();
		        DataOutputStream out = new DataOutputStream(connection
		                .getOutputStream());
		        // The URL-encoded contend
		        // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
		        String content = "key=j0r53nmbbd78x7m1pqml06u2&type=1&toemail=cngolon@gmail.com" + "&activatecode=" + URLEncoder.encode("中国聚龙", "utf-8");
		        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
		        out.writeBytes(content); 
		        out.flush();
		        out.close(); // flush and close
		        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//设置编码,否则中文乱码
		        String line="";
		        String message="";
		        while ((line = reader.readLine()) != null){
		            //line = new String(line.getBytes(), "utf-8");
		            System.out.println(line);
		            message+=line;
		        }
		       
		        reader.close();
		        connection.disconnect();
		        System.out.println(message);
		        
		        return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
