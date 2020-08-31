package com.thinkgem.jeesite.modules.data.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataDef;
import com.thinkgem.jeesite.modules.data.entity.DataSearchDef2;
import com.thinkgem.jeesite.modules.data.entity.SearchInterfaceDef;
import com.thinkgem.jeesite.modules.data.service.DataDefService;
import com.thinkgem.jeesite.modules.data.service.DataSearchDefService2;
import com.thinkgem.jeesite.modules.data.service.SearchInterfaceDefService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;


@Controller
@RequestMapping(value = "/searchInterface")
public class SearchInterfaceController extends BaseController {
	@Autowired
	private SearchInterfaceDefService searchInterfaceDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private DataSearchDefService2 dataSearchDefService2;
	@Autowired
	private DataDefService dataDefService;
	
	@RequestMapping(value = "/searchInterfaceList")
	public String searchInterfaceList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, 
			Model model,String interfacesetcode,String datacode) {
		DataSearchDef2 dataSearchDef=dataSearchDefService2.getDataSearchDefByDataCode(datacode);
		if(dataSearchDef.getInterfacesetcode()==null||"".equals(dataSearchDef.getInterfacesetcode())){
			model.addAttribute("status",0);
		}else{
			model.addAttribute("status",1);
			model.addAttribute("interfacesetcode",dataSearchDef.getInterfacesetcode());
		}
		List<Object[]> namelist=searchInterfaceDefService.getTypename();
		if(namelist.size()>0){
			if(interfacesetcode!=null&&!"".equals(interfacesetcode)){
				model.addAttribute("interfacesetcode",interfacesetcode);
			}else{
				interfacesetcode=namelist.get(0)[0].toString();
				model.addAttribute("interfacesetcode",interfacesetcode);
			}
		}
		List<SearchInterfaceDef> list=searchInterfaceDefService.getSearchInterfaceDefListByCode(interfacesetcode);
		model.addAttribute("datacode",datacode);
		model.addAttribute("namelist",namelist);
		model.addAttribute("list",list);
		return "modules/data/searchInterfaceList";
	}
	/**
	 * 接口选择页面
	 * @param paramMap
	 * @param request
	 * @param response
	 * @param model
	 * @param interfacesetcode
	 * @param chnname
	 * @param datacode
	 * @return
	 */
	@RequestMapping(value = "/searchInterfaceCoList")
	public String searchInterfaceCoList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, 
			Model model,String interfacesetcode,String chnname,String datacode) {
		DataSearchDef2 dataSearchDef=dataSearchDefService2.getDataSearchDefByDataCode(datacode);
		if(dataSearchDef.getParentdatacode()==null||"".equals(dataSearchDef.getParentdatacode())){
			DataDef dataDef=dataDefService.getDataDefByCode(dataSearchDef.getDatacode());
			model.addAttribute("name",dataDef.getChnname());
		}else{
			model.addAttribute("name",dataSearchDef.getDatachnname());
		}
		if(dataSearchDef.getInterfacesetcode()==null||"".equals(dataSearchDef.getInterfacesetcode())){
			model.addAttribute("status",0);
		}else{
			model.addAttribute("status",1);
			model.addAttribute("interfacesetcode",dataSearchDef.getInterfacesetcode());
		}
		List<Object[]> namelist=searchInterfaceDefService.getTypename();
		if(namelist.size()>0){
			if(interfacesetcode!=null&&!"".equals(interfacesetcode)){
				model.addAttribute("interfacesetcode",interfacesetcode);
			}else{
				interfacesetcode=namelist.get(0)[0].toString();
				model.addAttribute("interfacesetcode",interfacesetcode);
			}
		}
		List<SearchInterfaceDef> list=searchInterfaceDefService.getSearchInterfaceDefListByCode(interfacesetcode);
		model.addAttribute("datacode",datacode);
		model.addAttribute("namelist",namelist);
		model.addAttribute("list",list);
		model.addAttribute("chnname",chnname);
		return "modules/data/searchInterfaceList2";
	}
	//跳转到添加页面
	@RequestMapping("/addInterface")
	public String addInterface(HttpServletRequest request,HttpServletResponse response,Model model,
			String datacode,String type,String interfacesetcode){
		//新增接口
		if("0".equals(type)){
			List<Object[]> namelist=searchInterfaceDefService.getTypename();
			if(namelist.size()>0){
				if(interfacesetcode!=null&&!"".equals(interfacesetcode)){
					model.addAttribute("interfacesetcode",interfacesetcode);
				}else{
					interfacesetcode=namelist.get(0)[0].toString();
					model.addAttribute("interfacesetcode",interfacesetcode);
				}
			}
			model.addAttribute("namelist",namelist);
			model.addAttribute("datacode",datacode);
			return "modules/data/interfaceDefAdd";		
		}else if("1".equals(type)){//新增接口类型
			model.addAttribute("datacode",datacode);
			return "modules/data/interfaceDefAdd2";		
		}
		return type;
	}
	//跳转到添加页面
	@RequestMapping("/editInterface")
	public String editInterface(HttpServletRequest request,HttpServletResponse response,
			Model model,String id,String datacode){
		SearchInterfaceDef searchInterfaceDef=searchInterfaceDefService.getSearchInterface(Integer.valueOf(id));
		List<Object[]> namelist=searchInterfaceDefService.getTypename();
		model.addAttribute("searchInterfaceDef",searchInterfaceDef);
		model.addAttribute("datacode",datacode);
		model.addAttribute("namelist",namelist);
		return "modules/data/interfaceDefAdd";		
	}
	//删除接口
	@RequestMapping("/deleteInterface")
	public String deleteInterface(HttpServletRequest request,HttpServletResponse response,
			Model model,String id,RedirectAttributes redirectAttributes,String datacode){
		SearchInterfaceDef searchInterfaceDef=searchInterfaceDefService.getSearchInterface(Integer.valueOf(id));
		try {
			if(searchInterfaceDef.getInvalid()==0){
				searchInterfaceDef.setInvalid(1);
			}else if(searchInterfaceDef.getInvalid()==1){
				searchInterfaceDef.setInvalid(0);
			}
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "更新成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "更新失败");
		}
		return "redirect:"+"/searchInterface/searchInterfaceList?datacode="+datacode;
	}
	//绑定接口
		@RequestMapping("/searchInterfaceBind")
		public String searchInterfaceBind(HttpServletRequest request,HttpServletResponse response,
				Model model,String interfacesetcode,RedirectAttributes redirectAttributes,String datacode){
			try {
				dataSearchDefService2.updateDataSearchDefByDataCode(datacode,interfacesetcode);
//				cacheCleanController.cleanCache();
				addMessage(redirectAttributes, "绑定成功");
			} catch (Exception e) {
				addMessage(redirectAttributes, "绑定失败");
			}
			return "redirect:"+"/searchInterface/searchInterfaceList?datacode="+datacode;
		}
	
	@RequestMapping("/saveInterface")
	public String saveInterface(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,
			Model model,String id,RedirectAttributes redirectAttributes){
		String searchcodelist=(String)paramMap.get("searchcodelist");
		String interfacename=(String)paramMap.get("interfacename");
		String interfacedesc=(String)paramMap.get("interfacedesc");
		String optioncodelist=(String)paramMap.get("optioncodelist");
		String interfacesetcode=(String)paramMap.get("interfacesetcode");
		String datacode=(String)paramMap.get("datacode");
		SearchInterfaceDef searchInterfaceDef=new SearchInterfaceDef();
		searchInterfaceDef.setSearchcodelist(searchcodelist);
		searchInterfaceDef.setInterfacename(interfacename);
		searchInterfaceDef.setInterfacedesc(interfacedesc);
		searchInterfaceDef.setOptioncodelist(optioncodelist);
		searchInterfaceDef.setInterfacesetcode(interfacesetcode);
		List<SearchInterfaceDef> list=searchInterfaceDefService.getSearchInterfaceDefListByCode(interfacesetcode);
		if(list.size()>0){
			searchInterfaceDef.setTypeName(list.get(0).getTypeName());
		}
		searchInterfaceDef.setInvalid(0);
		Integer maxid=searchInterfaceDefService.getMaxId();
		searchInterfaceDef.setId(maxid+1);
		try {
			searchInterfaceDefService.saveSearchInterface(searchInterfaceDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			// TODO: handle exception
		}
		
		return "redirect:"+"/searchInterface/searchInterfaceList?datacode="+datacode+"&interfacesetcode="+interfacesetcode;		
	}
	@RequestMapping("/saveInterface2")
	public String saveInterface2(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request,HttpServletResponse response,
			Model model,String id,RedirectAttributes redirectAttributes){
		String searchcodelist=(String)paramMap.get("searchcodelist");
		String interfacename=(String)paramMap.get("interfacename");
		String interfacedesc=(String)paramMap.get("interfacedesc");
		String optioncodelist=(String)paramMap.get("optioncodelist");
		String interfacesetcode=(String)paramMap.get("interfacesetcode");
		String datacode=(String)paramMap.get("datacode");
		String typeName=(String)paramMap.get("typeName");
		SearchInterfaceDef searchInterfaceDef=new SearchInterfaceDef();
		searchInterfaceDef.setSearchcodelist(searchcodelist);
		searchInterfaceDef.setInterfacename(interfacename);
		searchInterfaceDef.setInterfacedesc(interfacedesc);
		searchInterfaceDef.setOptioncodelist(optioncodelist);
		searchInterfaceDef.setInterfacesetcode(interfacesetcode);
		searchInterfaceDef.setInvalid(0);
		searchInterfaceDef.setTypeName(typeName);
		Integer maxid=searchInterfaceDefService.getMaxId();
		searchInterfaceDef.setId(maxid+1);
		try {
			searchInterfaceDefService.saveSearchInterface(searchInterfaceDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			// TODO: handle exception
		}
		
		return "redirect:"+"/searchInterface/searchInterfaceList?datacode="+datacode+"&interfacesetcode="+interfacesetcode;		
	}
	@RequestMapping("/saveUpdateInterface")
	public String saveUpdateInterface(@RequestParam
			Map<String, Object> paramMap,SearchInterfaceDef searchInterfaceDef,HttpServletRequest request,HttpServletResponse response,
			Model model,RedirectAttributes redirectAttributes){
		SearchInterfaceDef searchInterfaceDef2=new SearchInterfaceDef();
		if(searchInterfaceDef.getId()!=null){
			searchInterfaceDef2=searchInterfaceDefService.getSearchInterface(searchInterfaceDef.getId());
		}
		searchInterfaceDef2.setSearchcodelist(searchInterfaceDef.getSearchcodelist());
		searchInterfaceDef2.setInterfacename(searchInterfaceDef.getInterfacename());
		searchInterfaceDef2.setInterfacedesc(searchInterfaceDef.getInterfacedesc());
		searchInterfaceDef2.setOptioncodelist(searchInterfaceDef.getOptioncodelist());
		searchInterfaceDef2.setInterfacesetcode(searchInterfaceDef.getInterfacesetcode());
		searchInterfaceDef2.setInvalid(searchInterfaceDef.getInvalid());
		try {
			searchInterfaceDefService.saveSearchInterface(searchInterfaceDef2);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		} catch (Exception e) {
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存失败");
			// TODO: handle exception
		}
		String datacode=(String)paramMap.get("datacode");
		return "redirect:"+"/searchInterface/searchInterfaceList?datacode="+datacode;	
	}
	
	@RequestMapping(value="/checkCode")
	public void checkCode(String interfacesetcode,HttpServletRequest request,HttpServletResponse response,Model model){
		List list=searchInterfaceDefService.getSearchInterfaceDefListByCode(interfacesetcode);
		String status="0";
		if(list.size()>0){
			status="1";
		}else{
			status="0";
		}
		Map map=new HashMap();
		map.put("status", status);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping(value="/InterfaceShow")
	public void InterfaceShow(String datacode,HttpServletRequest request,HttpServletResponse response,Model model){
		String status="0";
		String interfacesetcode="";
		DataSearchDef2 dataSearchDef=dataSearchDefService2.getDataSearchDefByDataCode(datacode);
		interfacesetcode=dataSearchDef.getInterfacesetcode();
		if(interfacesetcode==null||"".equals(interfacesetcode)){
			status="0";
		}else{
			status="1";
		}
		List<SearchInterfaceDef> list=searchInterfaceDefService.getSearchInterfaceDefListByCode(interfacesetcode);
		String name="";
		if(list.size()>0){
			name=list.get(0).getTypeName();
		}
		Map map=new HashMap();
		map.put("status", status);
		map.put("name", name);
		//map.put("namelist", namelist);
		map.put("interfacesetcode", interfacesetcode);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping(value="/InterfaceBind")
	public void InterfaceBind(String interfacesetcode,String datacode,
			HttpServletRequest request,HttpServletResponse response,Model model){
		String status="0";
		DataSearchDef2 dataSearchDef=dataSearchDefService2.getDataSearchDefByDataCode(datacode);
		dataSearchDef.setInterfacesetcode(interfacesetcode);
		try {
			dataSearchDefService2.saveDataSearchDef(dataSearchDef);
//			cacheCleanController.cleanCache();
			status="1";
		} catch (Exception e) {
			
		}
		Map map=new HashMap();
		map.put("status", status);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
}

