package com.thinkgem.jeesite.mybatis.modules.industry.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.modules.products.entity.UploadProduct;
import com.thinkgem.jeesite.modules.sys.utils.ImageUploadUtil;
import com.thinkgem.jeesite.mybatis.modules.industry.entity.IndustryApplication;
import com.thinkgem.jeesite.mybatis.modules.industry.service.IndustryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;


/**
 * 行业应用服务Controller
 * @author songyan
 * @version 2020-02-26
 */
@Controller
@RequestMapping(value = "/industry/industryApplication")
public class IndustryApplicationController extends BaseController {

	@Autowired
	private IndustryApplicationService industryApplicationService;
	
	@ModelAttribute
	public IndustryApplication get(@RequestParam(required=false) String id) {
		IndustryApplication entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = industryApplicationService.get(id);
		}
		if (entity == null){
			entity = new IndustryApplication();
		}
		return entity;
	}
	
	//@RequiresPermissions("industry:industryApplication:view")
	@RequestMapping(value = {"list", ""})
	public String list(IndustryApplication industryApplication, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IndustryApplication> page = industryApplicationService.findPage(new Page<IndustryApplication>(request, response), industryApplication); 
		model.addAttribute("page", page);
		return "modules/industry/industryApplicationList";
	}

	//@RequiresPermissions("industry:industryApplication:view")
	@RequestMapping(value = "form")
	public String form(IndustryApplication industryApplication, Model model) {
		model.addAttribute("industryApplication", industryApplication);
		return "modules/industry/industryApplicationForm";
	}

	//@RequiresPermissions("industry:industryApplication:edit")
	@RequestMapping(value = "save")
	public String save(IndustryApplication industryApplication, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, industryApplication)){
			return form(industryApplication, model);
		}
		industryApplicationService.save(industryApplication);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:/industry/industryApplication/list";
	}
	
	//@RequiresPermissions("industry:industryApplication:edit")
	@RequestMapping(value = "delete")
	public String delete(IndustryApplication industryApplication, RedirectAttributes redirectAttributes) {
		industryApplicationService.delete(industryApplication);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:/industry/industryApplication/list";
	}

	@RequestMapping(value = "/upload")
	public void upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,HttpServletResponse response){
		try{
			JSONObject json = new JSONObject();
			String fileName = file.getOriginalFilename();// 获取到上传文件的名字
			InputStream inputStream = file.getInputStream();//转换成输入流的方式
			String host = Global.getConfig("img_addr");
			int port = Integer.parseInt(Global.getConfig("img_port"));
			String userName = Global.getConfig("img_name");
			String password =Global.getConfig("img_password");
			String remoteFile=Global.getConfig("img_url")+"/data/img";
			String url = "/data/img/"+fileName;
			ImageUploadUtil.uploadFile(host,port,userName,password,remoteFile,inputStream,fileName);
			String id = UUID.randomUUID().toString().replace("-","");
			List<IndustryApplication> list = industryApplicationService.getAllUploadIndustry();
			if(list.size()!=0){
				int index = 0;
				for (int i = 0; i <list.size() ; i++) {
					if(url.equals(list.get(i).getImageurl())) {
						index+=1;
					}
				}
				if (index==0){
					addMessage(redirectAttributes, "上传文件成功");
					json.put("url",url);
					renderText(JsonMapper.toJsonString(json), response);
				}else{
					addMessage(redirectAttributes, "文件已存在");
				}
			}else{
				addMessage(redirectAttributes, "上传文件成功");
				json.put("url",url);
				renderText(JsonMapper.toJsonString(json), response);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}