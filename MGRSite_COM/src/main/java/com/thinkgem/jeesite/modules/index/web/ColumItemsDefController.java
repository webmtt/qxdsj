/*
 * @(#)ColumItemsDefController.java 2016-8-25
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.ColumnItemsDef;
import com.thinkgem.jeesite.modules.index.service.ColumnItemsDefService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.tvmeeting.entity.DocDef;
import com.thinkgem.jeesite.modules.tvmeeting.entity.PlanDefine;
import com.thinkgem.jeesite.modules.tvmeeting.service.DocDefService;
import com.thinkgem.jeesite.modules.tvmeeting.service.LiveTeleCastService;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-8-25
 */
@Controller
@RequestMapping(value = "column/columnarrange")
public class ColumItemsDefController extends BaseController {
	@Autowired
	private ColumnItemsDefService colService;
	@Resource
	private ComparasDao comparasDao;
	@Autowired
	private DocDefService docDefService;
	@RequestMapping("/columnList")
	public String ColumItemsList(
			String area,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		
	    area=request.getParameter("area");
		model.addAttribute("area", area);
	   Page<ColumnItemsDef> page=colService.getByPage(new Page<ColumnItemsDef>(request,response),area);
	model.addAttribute("page",page);
		return "modules/column/ColumnList";		
	}
	@RequestMapping("/columnAdd")
	public String ColumnAdd(HttpServletRequest request,HttpServletResponse response,Model model,ColumnItemsDef column){
		int maxUploadSize=Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
		String area=request.getParameter("area");
		column.setInvalid(0);
		model.addAttribute("maxUploadSize",maxUploadSize/1024/1024);
		model.addAttribute("column",column);
		model.addAttribute("area",area);
		return "modules/column/ColumnAdd";		
	}	
	
	@RequestMapping("/teleDell")
	public String TeleDeletel(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
			Model model){
		String columnItemID=request.getParameter("columnItemID");
		Integer colid=Integer.parseInt(columnItemID);
		String area=request.getParameter("area");
		colService.DelCol(colid, area);
		addMessage(redirectAttributes, "删除成功");
		cleanSuccess();
		//return "redirect:"+"/tvmeeting/meetingarrange/teleList";
		return "redirect:"+"/column/columnarrange/columnList?area="+area+"";		
	}
	
	@RequestMapping("/teleDel")
	public String TeleDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
			Model model){
		String columnItemID=request.getParameter("columnItemID");
		Integer colid=Integer.parseInt(columnItemID);
		String area=request.getParameter("area");
		colService.DelColl(colid);
		addMessage(redirectAttributes, "删除成功");
		cleanSuccess();
		//return "redirect:"+"/tvmeeting/meetingarrange/teleList";
		return "redirect:"+"/column/columnarrange/columnList?area="+area+"";		
	}
	@RequestMapping("/Savecolumn")
	public String SaveColumn(HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		String columnItemID=request.getParameter("columnItemIDs");
		Integer id=Integer.parseInt(columnItemID);
		ColumnItemsDef column=colService.findByColumn(id);
		
		String chnName=request.getParameter("chnName");
		String layerDescription=request.getParameter("layerDescription");
		String columnImageURL=request.getParameter("columnImageURL");
		String linkURL=request.getParameter("linkURL");
		String invalid=request.getParameter("invalid");
		String area=request.getParameter("areaItems");
		String arr=","+area+",";
		String orderNo=request.getParameter("orderNo");
		String columnType=request.getParameter("columnType");
		Integer showType=1;
		column.setAreaItem(arr);
		column.setLayerDescription(layerDescription);
		column.setInvalid(Integer.parseInt(invalid));
		colService.SaveCol(column);
		colService.updataCol(chnName, linkURL, showType, Integer.parseInt(orderNo), columnType, id);
		cleanSuccess();
		addMessage( redirectAttributes, "保存成功");
		return "redirect:"+"/column/columnarrange/columnList?area="+area+"";	
		
	}
	@RequestMapping("/saveAttach")  
	public void saveAttach(
			HttpServletRequest request, HttpServletResponse response, Model model) {
		 String imgServerPath=(String)comparasDao.getComparasByKey("docColumnPath");
		 String docDefUrl=(String)comparasDao.getComparasByKey("columnPath");
//		String imgServerPath = "D://";
//		String docDefUrl = "test/";
		 String area = request.getParameter("area");
		MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
		Map<String,MultipartFile> fliemap=multipartRequest.getFileMap();
		CommonsMultipartFile file = (CommonsMultipartFile)fliemap.get("file");	
		long l=file.getSize();
		int maxUploadSize=Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
		Map<String, Object> map = new HashMap<String, Object>();
		ColumnItemsDef column=new ColumnItemsDef();
		
		
		ColumnItemsDef list=new ColumnItemsDef();
		
		
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
			   
			
			    column.setColumnImageURL(localpath);
			    column.setChnName("1111");
			    column.setLinkURL("1111");
			    column.setShowType(1);
			    column.setOrderNo(1);
			    column.setInvalid(1);
			    column.setColumnType("1111");
			    column.setAreaItem(","+area+",");
			    colService.SaveCol(column);
			    cleanSuccess();
				if (flag) {
					
					//docDefService.save(docDef);
					//list = docDefService.findDocDefList(id);
					list=colService.findByColumn(column.getColumnItemID());
					map.put("list", list);
					map.put("fileName", fileName);
					map.put("id",column.getColumnItemID());
					map.put("message", "上传成功");
					map.put("area",area);
					
				} else {
					
					map.put("list",list);
					map.put("fileName", fileName);
					map.put("id", column.getColumnItemID());
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
		 String imgServerPath=(String)comparasDao.getComparasByKey("docColumnPath");
			
		boolean flag=false;
	File file=new File(imgServerPath+"/"+docUrl);
		try {
		//file.delete();
		colService.DelById(Integer.parseInt(docId));
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
