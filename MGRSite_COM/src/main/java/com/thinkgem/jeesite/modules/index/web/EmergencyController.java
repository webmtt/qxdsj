package com.thinkgem.jeesite.modules.index.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.Emergency;
import com.thinkgem.jeesite.modules.index.service.EmergencyService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;


@Controller
@RequestMapping(value="/emergencyController")
public class EmergencyController extends BaseController{

	@Autowired
	private ComparasService comparasService;
	@Autowired
	private EmergencyService emergencyService;
	@RequestMapping("/energencyList")
	public String getEmergencyNotice(String province,String nation, Model model,HttpServletRequest request,HttpServletResponse response){
		String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		for(Emergency e:provinceList){
			e.setNoticeLevel("省级");
		}
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		for(Emergency e:naticeList){
			e.setNoticeLevel("国家级");
		}
		//国家级应急list倒排序
		Collections.reverse(naticeList);
		List<Emergency> all=new ArrayList<Emergency>();
		/*if(province!=null&!"".equals(province)&(nation==null|"".equalsIgnoreCase(nation))){
			all.addAll(provinceList);
		}else if(nation!=null&!"".equals(nation)&(province==null|"".equalsIgnoreCase(province))){
			all.addAll(naticeList);
		}else if((nation==null|"".equals(nation))&(province==null|"".equalsIgnoreCase(province))){
			all.addAll(all);
		}else if(province!=null&!"".equals(province)&nation!=null&!"".equals(nation)){
			all.addAll(naticeList);
			all.addAll(provinceList);			
		}*/
		all.addAll(naticeList);
		all.addAll(provinceList);
		model.addAttribute("all", all);
		
		return "modules/index/emergencyList";
	}
	@RequestMapping("/jsonList")
	public void getEmergencyNoticeJsonList(String province,String nation, Model model,HttpServletRequest request,HttpServletResponse response){
		String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		for(Emergency e:provinceList){
			e.setNoticeLevel("省级");
		}
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		for(Emergency e:naticeList){
			e.setNoticeLevel("国家级");
		}
		//国家级应急list倒排序
		Collections.reverse(naticeList);
		List<Emergency> all=new ArrayList<Emergency>();
		if(province!=null&!"".equals(province)&(nation==null|"".equalsIgnoreCase(nation))){
			all.addAll(provinceList);
		}else if(nation!=null&!"".equals(nation)&(province==null|"".equalsIgnoreCase(province))){
			all.addAll(naticeList);
		}else if((nation==null|"".equals(nation))&(province==null|"".equalsIgnoreCase(province))){
			all.addAll(all);
		}else if(province!=null&!"".equals(province)&nation!=null&!"".equals(nation)){
			all.addAll(naticeList);
			all.addAll(provinceList);			
		}
		//model.addAttribute("all", all);
		
		String json=JsonMapper.toJsonString(all);
		renderText(json, response);
	}
	@RequestMapping("/naticeList")
	public String naticeList(Model model,HttpServletRequest request,HttpServletResponse response){
		String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		for(Emergency e:provinceList){
			e.setNoticeLevel("省级");
		}
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		for(Emergency e:naticeList){
			e.setNoticeLevel("国家级");
		}
		//国家级应急list倒排序
		Collections.reverse(naticeList);
		List<Emergency> all=new ArrayList<Emergency>();	
		all.addAll(naticeList);
		model.addAttribute("all", all);
		
		return "modules/index/emergencynaticeList";
	}
	@RequestMapping("/provinceList")
	public String provinceList(Model model,HttpServletRequest request,HttpServletResponse response){
		String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		for(Emergency e:provinceList){
			e.setNoticeLevel("省级");
		}
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		for(Emergency e:naticeList){
			e.setNoticeLevel("国家级");
		}
		//国家级应急list倒排序
		Collections.reverse(naticeList);
		List<Emergency> all=new ArrayList<Emergency>();	
		all.addAll(provinceList);
		model.addAttribute("all", all);
		
		return "modules/index/emergencyprovinceList";
	}
	@RequestMapping("/getProvence")
	public String getEmergencyInfo( Model model,HttpServletRequest request,HttpServletResponse response){
		/*String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		//国家级应急list倒排序
		Collections.reverse(naticeList);*/
		List<Emergency> all=  comparasService.getStringValue("yjProvinceRuler");
		Collections.reverse(all);
		model.addAttribute("all", all);
		return "modules/index/emergencyProvence";
	
	}
	@RequestMapping("/provinceAdd")
	public  String provinceAdd(HttpServletRequest request,
			HttpServletResponse response,Model model,String keyid,
			String[] province,String[] provinceCode,RedirectAttributes redirectAttributes) throws IOException {
		/*request.setCharacterEncoding("utf-8");  
		response.setContentType("text/html;charset=utf-8");*/
		List<Emergency> list = new ArrayList<Emergency>();
		String str = "";
		if(province.length!=0&&provinceCode.length!=0){
			for( int i=0;i<province.length;i++){
				Emergency eg = new Emergency();
				eg.setProvince(province[i].trim());
				eg.setProvinceCode(provinceCode[i].trim());
				list.add(eg);
			}
		}
		String strs = "";
		for(Emergency e:list){
			strs += e.getProvince()+"##"+e.getProvinceCode()+",";
		}
		str = strs.substring(0,strs.length()-1);
		comparasService.updateComparasById("yjProvinceRuler", str);
		return "redirect:"+"/emergencyController/getProvence";
		
	}
	
	@RequestMapping("/provinceCheck")
	public void provinceCheck(
			HttpServletRequest request,HttpServletResponse response){
		List<Emergency> all=  comparasService.getStringValue("yjProvinceRuler");
		String prov = request.getParameter("prov");
		String pcode = request.getParameter("pcode");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("code", 1);
		for(Emergency e:all){
			if(prov.equals(e.getProvince())){
				map.put("code", 2);
			}
			if(pcode.equals(e.getProvinceCode())){
				map.put("code", 3);
			}
		}
		renderText(JsonMapper.toJsonString(map), response);
			
	}
	@RequestMapping("/getType")
	public String getType( Model model,HttpServletRequest request,HttpServletResponse response){
		/*String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		//国家级应急list倒排序
		Collections.reverse(naticeList);*/
		List<Emergency> all=  comparasService.getStringValue("yjTypeRuler");
		Collections.reverse(all);
		model.addAttribute("all", all);
		return "modules/index/emergencyType";
	}
	
	@RequestMapping("/typeAdd")
	public  String typeAdd(HttpServletRequest request,
			HttpServletResponse response,Model model,String keyid,
			String[] type,String[] typeCode,RedirectAttributes redirectAttributes) throws IOException {
		/*request.setCharacterEncoding("utf-8");  
		response.setContentType("text/html;charset=utf-8");*/
		List<Emergency> list = new ArrayList<Emergency>();
		String str = "";
		if(type.length!=0&&typeCode.length!=0){
			for( int i=0;i<type.length;i++){
				Emergency eg = new Emergency();
				eg.setType(type[i].trim());
				eg.setTypeCode(typeCode[i].trim());
				list.add(eg);
			}
		}
		String strs = "";
		for(Emergency e:list){
			strs += e.getType()+"##"+e.getTypeCode()+",";
		}
		str = strs.substring(0,strs.length()-1);
		comparasService.updateComparasById("yjTypeRuler", str);
		return "redirect:"+"/emergencyController/getType";
		
	}
	
	@RequestMapping("/typeCheck")
	public void typeCheck(
			HttpServletRequest request,HttpServletResponse response){
		List<Emergency> all=  comparasService.getStringValue("yjTypeRuler");
		String prov = request.getParameter("prov");
		String pcode = request.getParameter("pcode");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("code", 1);
		for(Emergency e:all){
			if(prov.equals(e.getType())){
				map.put("code", 2);
			}
			if(pcode.equals(e.getTypeCode())){
				map.put("code", 3);
			}
		}
		renderText(JsonMapper.toJsonString(map), response);
			
	}
	@RequestMapping("/getLevel")
	public String getLevel( Model model,HttpServletRequest request,HttpServletResponse response){
		/*String provinceURL=comparasService.get("provinceURL").getStringvalue();
		String nationURL=comparasService.get("nationURL").getStringvalue();	
		Integer emergencyAnalysisMode=comparasService.get("emergencyAnalysisMode").getIntvalue();	
		//省级应急
		List<Emergency> provinceList=emergencyService.getEmergencyInfo(provinceURL, emergencyAnalysisMode);
		//省级应急list倒排序
		Collections.reverse(provinceList);
		//国家级应急
		List<Emergency> naticeList=emergencyService.getEmergencyInfo2(nationURL);
		//国家级应急list倒排序
		Collections.reverse(naticeList);*/
		List<Emergency> all=  comparasService.getStringValue("yjLevelRuler");
		Collections.reverse(all);
		model.addAttribute("all", all);
		return "modules/index/emergencyLevel";
	}
	@RequestMapping("/levelAdd")
	public  String levelAdd(HttpServletRequest request,
			HttpServletResponse response,Model model,String keyid,
			String[] level,String[] levelCode,RedirectAttributes redirectAttributes) throws IOException {
		/*request.setCharacterEncoding("utf-8");  
		response.setContentType("text/html;charset=utf-8");*/
		List<Emergency> list = new ArrayList<Emergency>();
		String str = "";
		if(level.length!=0&&levelCode.length!=0){
			for( int i=0;i<level.length;i++){
				Emergency eg = new Emergency();
				eg.setLevel(level[i].trim());
				eg.setLevelCode(levelCode[i].trim());
				list.add(eg);
			}
		}
		String strs = "";
		for(Emergency e:list){
			strs += e.getLevel()+"##"+e.getLevelCode()+",";
		}
		str = strs.substring(0,strs.length()-1);
		comparasService.updateComparasById("yjLevelRuler", str);
		return "redirect:"+"/emergencyController/getLevel";	
	}
	
	@RequestMapping("/levelCheck")
	public void level(
			HttpServletRequest request,HttpServletResponse response){
		List<Emergency> all=  comparasService.getStringValue("yjLevelRuler");
		String prov = request.getParameter("prov");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("code", 1);
		for(Emergency e:all){
			if(prov.equals(e.getLevel())){
				map.put("code", 2);
			}
		}
		renderText(JsonMapper.toJsonString(map), response);
			
	}
	@RequestMapping("/test")
	public String test(){
		return "modules/index/test";
	}
	
}
