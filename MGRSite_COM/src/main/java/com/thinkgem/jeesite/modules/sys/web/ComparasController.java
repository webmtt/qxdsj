package com.thinkgem.jeesite.modules.sys.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;

@Controller
@RequestMapping(value = "sys/comparas")
public class ComparasController extends BaseController{
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}
	
	
	@Autowired
	ComparasService comparasService;
	
	@Resource(name = "comparasService")
	public void setComparasService(ComparasService comparasService) {
		this.comparasService = comparasService;
	}
	

	@RequestMapping(value = {"list",""})
	public String list(@RequestParam
			Map<String, Object> paramMap,Comparas comparas, HttpServletRequest request,HttpServletResponse response, Model model) {
		String description=request.getParameter("description");
		
		Page<Comparas> page = comparasService.findComparas(description,new Page<Comparas>(request, response), comparas);
		model.addAttribute("comparas", comparas);
		model.addAttribute("page", page);
		
		return "modules/sys/comparas/comparasList";
	}
	
	@RequestMapping(value = "form")
	public String form(Comparas comparas,HttpServletRequest request,HttpServletResponse response, Model model) {
		Comparas comparas1=new Comparas();
		String keyid=comparas.getKeyid();
		if(keyid!=null&&!"".equals(keyid.toString())){
			comparas1=comparasService.getComparas(keyid);
		}
		model.addAttribute("comparas", comparas1);
		return "modules/sys/comparas/comparasform";
		
	}
	@RequestMapping(value = "checkKeyId")
	@ResponseBody
	public String checkKeyId(String keyid) {

			Comparas comparas1=comparasService.getComparas(keyid);
		if(comparas1==null){
			return "1";
		}else {
			return "2";
		}
	}

	@RequestMapping(value = "add")
	public String add(Comparas comparas,HttpServletRequest request,HttpServletResponse response, Model model) {
		return "modules/sys/comparas/comparasadd";
		
	}
	@RequestMapping(value = "/save")
	public String saveComparas(Comparas comparas, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		String keyid=comparas.getKeyid();
		String name=comparas.getName();
		String type=comparas.getType();
		String description=comparas.getDescription();
		String stringvalue=comparas.getStringvalue();
		Integer booleanvalue=comparas.getBooleanvalue();
		Integer intvalue=comparas.getIntvalue();
		comparas.setKeyid(keyid);
		comparas.setCreated(new Date());
		
		if("".equals(comparas.getName())||comparas.getName()==null){
			comparas.setName(null);
		}else{
			comparas.setName(name);
		}
		if("".equals(comparas.getType())||comparas.getType()==null){
			comparas.setType(null);
		}else{
			comparas.setType(String.valueOf(type));
		}
		if("".equals(comparas.getDescription())||comparas.getDescription()==null){
			comparas.setDescription(null);
		}else{
			comparas.setDescription(description);
		}
		if("".equals(comparas.getStringvalue())||comparas.getStringvalue()==null){
			comparas.setStringvalue(null);
		}else{
			comparas.setStringvalue(stringvalue);
		}
		if("".equals(comparas.getBooleanvalue())||comparas.getBooleanvalue()==null){
			comparas.setBooleanvalue(null);
		}else{
			comparas.setBooleanvalue(booleanvalue);
		}
		if("".equals(comparas.getIntvalue())||comparas.getIntvalue()==null){
			comparas.setIntvalue(null);
		}else{
			comparas.setIntvalue(intvalue);
		}
		comparas.setInvalid(0);//暂时写死，不清楚这个字段的意思
		comparasService.save(comparas);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+"/sys/comparas/list";
		
	}
	
	
}
