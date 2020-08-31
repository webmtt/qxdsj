/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.web;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ExcelBean;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupSurveystation;
import com.thinkgem.jeesite.mybatis.modules.report.service.SupSurveystationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 报表查询参数配置Controller
 * @author yang.kq
 * @version 2019-11-06
 */
@Controller
@RequestMapping(value = "/report/supSurveystation")
public class SupSurveystationController extends BaseController {

	@Autowired
	private SupSurveystationService supSurveystationService;

	@ModelAttribute
	public SupSurveystation get(@RequestParam(required=false) String id) {
		SupSurveystation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supSurveystationService.get(id);
		}
		if (entity == null){
			entity = new SupSurveystation();
		}
		return entity;
	}

	@RequestMapping(value = {"/list", ""})
	public String list(SupSurveystation supSurveystation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupSurveystation> page = supSurveystationService.findPage(new Page<SupSurveystation>(request, response), supSurveystation);
		model.addAttribute("page", page);
		return "modules/report/supSurveystationList";
	}
	@RequestMapping(value = "/checkStationNum")
	@ResponseBody
	public String checkStationNum(String stationnum) {

		SupSurveystation supSurveystation=supSurveystationService.findStation(stationnum);
		if(supSurveystation==null){
			return "1";
		}else{
			return "2";
		}
	}
	@RequestMapping(value = "/form")
	public String form(SupSurveystation supSurveystation, Model model) {
		model.addAttribute("supSurveystation", supSurveystation);
		return "modules/report/supSurveystationForm";
	}

	@RequestMapping(value = "/save")
	public String save(SupSurveystation supSurveystation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, supSurveystation)){
			return form(supSurveystation, model);
		}
		supSurveystationService.save(supSurveystation);
		addMessage(redirectAttributes, "保存参数成功");
		return "redirect:/report/supSurveystation/list";
	}

	@RequestMapping(value = "/delete")
	public String delete(SupSurveystation supSurveystation, RedirectAttributes redirectAttributes) {
		supSurveystationService.delete(supSurveystation);
		addMessage(redirectAttributes, "删除参数成功");
		return "redirect:/report/supSurveystation/list";
	}
	@ResponseBody
	@RequestMapping(value = "/getStation")
	public  List<Map<String,Object>> getStation(SupSurveystation supSurveystation,Model model, RedirectAttributes redirectAttributes) {
		List<Map<String,Object>> list= supSurveystationService.getStationTree(supSurveystation);
		return list;
	}

	@RequestMapping(value = "/excelUpload")
	public String excelUpload(@RequestParam("file")CommonsMultipartFile file){
		try {
			InputStream in = file.getInputStream();
			supSurveystationService.upload(in,file);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "modules/report/supSurveystationList";
	}

	@RequestMapping(value = "download")
    public void download(HttpServletRequest request,HttpServletResponse response,@RequestParam("fileName") String fileName){
		try {
			String realPath = request.getSession().getServletContext().getRealPath("WEB-INF/download/") + fileName;
			realPath=realPath.replaceAll("\\\\","/");
			File file = new File(realPath);
			if (!file.exists()) {
				response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
				response.getWriter().print("<html><body><script type='text/javascript'>alert('您要下载的资源已被删除！');</script></body></html>");
				response.getWriter().close();
				System.out.println("您要下载的资源已被删除！！");
				return;
			}
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/vnd.ms-excel");
			//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
//        response.setContentType("multipart/form-data");
			// 读取要下载的文件，保存到文件输入流
			FileInputStream in = new FileInputStream(realPath);
			// 创建输出流
			OutputStream out= response.getOutputStream();
			// 创建缓冲区
			byte buffer[] = new byte[1024];
			int len = 0;
			//循环将输入流中的内容读取到缓冲区当中
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			//关闭文件输入流
			if(in!=null) {
				in.close();
			}
			if(out!=null) {
				// 关闭输出流
				out.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

    }
}