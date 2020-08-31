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
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.EleSetEleGroupDef;
import com.thinkgem.jeesite.modules.data.entity.EleSetElementDef;
import com.thinkgem.jeesite.modules.data.entity.SearchCondDef;
import com.thinkgem.jeesite.modules.data.entity.SearchSetDef;
import com.thinkgem.jeesite.modules.data.service.EleSetEleGroupDefService;
import com.thinkgem.jeesite.modules.data.service.EleSetElementDefService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;


@Controller
@RequestMapping(value = "/eleSetEleGroup")
public class EleSetGroupController extends BaseController {
	@Autowired
	private EleSetEleGroupDefService eleSetEleGroupDefService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private EleSetElementDefService eleSetElementDefService;
	
	@RequestMapping(value = "/list")
	public String searchCondCfgList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,String elesetcode,String elegroupcode) {
		
		
		List<EleSetEleGroupDef> list=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
		return "modules/data/searchCondCfgList";
	}
	/**
	 * 要素型 的要素分组-
	 * @param paramMap
	 * @param request
	 * @param response
	 * @param model
	 * @param elesetcode
	 * @param elegroupcode
	 * @return
	 */
	@RequestMapping(value = "/elementList")
	public String elementList(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,
			String elesetcode,String elegroupcode) {
		if(elegroupcode!=null&&!"".equals(elegroupcode)){
			List<EleSetElementDef> list=eleSetElementDefService.getListByelesetcode(elesetcode,elegroupcode);
			model.addAttribute("elesetcode",elesetcode);
			model.addAttribute("elegroupcode",elegroupcode);
			model.addAttribute("list",list);
		}else{
			List<EleSetElementDef> list=eleSetElementDefService.getListByelesetcode2(elesetcode);
			model.addAttribute("elesetcode",elesetcode);
			model.addAttribute("list",list);
		}
		return "modules/data/elementList";
	}
	/**
	 * 根据elesetcode去查询检索条件集合
	 * @param datacode
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/eleSetEleGroupDef")
	public void eleSetEleGroupDef(String elesetcode,HttpServletRequest request,HttpServletResponse response,Model model){
		List<EleSetEleGroupDef> clist=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
		 List<EleSetElementDef> flist=eleSetElementDefService.getListByelesetcode(elesetcode);
		 Map map=new HashMap();
		 map.put("clist", clist);
		 map.put("flist", flist);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	/**
	 * 根据elesetcode去查询检索条件集合-文件型
	 * @param datacode
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/eleSetEleGroupDef2")
	public void eleSetEleGroupDef2(String elesetcode,HttpServletRequest request,HttpServletResponse response,Model model){
		 List<EleSetElementDef> flist=eleSetElementDefService.getListByelesetcode(elesetcode);
		 Map map=new HashMap();
		 map.put("flist", flist);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping(value = "/addEleGroup")
	public String addEleGroup(HttpServletRequest request, HttpServletResponse response, Model model,String elesetcode) {
		model.addAttribute("elesetcode",elesetcode);
		return "modules/data/eleGroupDefAdd";
	}
	@RequestMapping(value = "/editEleGroup")
	public String editEleGroup(HttpServletRequest request, HttpServletResponse response, Model model,String elesetcode,String id) {
		
		EleSetEleGroupDef eleSetEleGroupDef=eleSetEleGroupDefService.getEleSetEleGroupDef(Integer.valueOf(id));
		model.addAttribute("elesetcode",elesetcode);
		model.addAttribute("eleSetEleGroupDef",eleSetEleGroupDef);
		return "modules/data/eleGroupDefEdit";
	}
	@RequestMapping(value = "/addElementDef")
	public String addElementDef(HttpServletRequest request, HttpServletResponse response, Model model,
			String elesetcode,String elegroupcode) {
		model.addAttribute("elegroupcode",elegroupcode);
		model.addAttribute("elesetcode",elesetcode);
		return "modules/data/elementDefAdd";
	}
	@RequestMapping(value = "/updateElementDef")
	public String updateElementDef(HttpServletRequest request, HttpServletResponse response, Model model,
			String elesetcode,String elegroupcode,String id) {
		EleSetElementDef eleSetElementDef=eleSetElementDefService.getEleSetElementDefById(Integer.valueOf(id));
		model.addAttribute("eleSetElementDef",eleSetElementDef);
		model.addAttribute("elegroupcode",elegroupcode);
		model.addAttribute("elesetcode",elesetcode);
		return "modules/data/elementDefEdit";
	}
	@RequestMapping(value = "/deleteElementDef")
	public String deleteElementDef(HttpServletRequest request, HttpServletResponse response, Model model,
			String elesetcode,String elegroupcode,String id,String invalid,RedirectAttributes redirectAttributes) {
		if(elegroupcode!=null&&!"".equals(elegroupcode)){
			try {
				eleSetElementDefService.deleteElementDef(Integer.valueOf(id),Integer.valueOf(invalid));
				addMessage(redirectAttributes, "重置成功");
			} catch (Exception e) {
				addMessage(redirectAttributes, "重置失败");
			}
			model.addAttribute("elegroupcode",elegroupcode);
			model.addAttribute("elesetcode",elesetcode);
			return "redirect:"+"/eleSetEleGroup/elementList?elesetcode="+elesetcode+"&elegroupcode="+elegroupcode;	
		}else{
			try {
				eleSetElementDefService.deleteElementDef(Integer.valueOf(id),Integer.valueOf(invalid));
				addMessage(redirectAttributes, "重置成功");
			} catch (Exception e) {
				addMessage(redirectAttributes, "重置失败");
			}
			model.addAttribute("elegroupcode",elegroupcode);
			model.addAttribute("elesetcode",elesetcode);
			return "redirect:"+"/eleSetEleGroup/elementList?elesetcode="+elesetcode+"&elegroupcode="+elegroupcode;	
		}
	}
	@RequestMapping(value = "/deleteEleGroup")
	public void deleteEleGroup(HttpServletRequest request, HttpServletResponse response, Model model,String id,String elesetcode) {
		Map map=new HashMap();
		EleSetEleGroupDef eleSetEleGroupDef=eleSetEleGroupDefService.getEleSetEleGroupDef(Integer.valueOf(id));
		try{
			if(eleSetEleGroupDef.getInvalid()==0){
				eleSetEleGroupDef.setInvalid(1);
			}else if(eleSetEleGroupDef.getInvalid()==1){
				eleSetEleGroupDef.setInvalid(0);
			}
			eleSetEleGroupDefService.saveEleSetEleGroupDef(eleSetEleGroupDef);
//			cacheCleanController.cleanCache();
			 map.put("status", 0);
		}catch (Exception e) {
			 map.put("status", 1);
		}
		List<EleSetEleGroupDef> clist=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
		List<EleSetElementDef> flist=eleSetElementDefService.getListByelesetcode(elesetcode);
		map.put("clist", clist);
		map.put("flist", flist);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	
	}
	
	@RequestMapping(value = "/saveEleGroup")
	public void saveEleGroup(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model) {
		String elesetcode=(String)paramMap.get("elesetcode");
		String chnname=(String)paramMap.get("chnname");
		EleSetEleGroupDef eleSetEleGroupDef=new EleSetEleGroupDef();
		eleSetEleGroupDef.setId(eleSetEleGroupDefService.getMaxId()+1);
		//根据elesetcode去查询
		List<EleSetEleGroupDef> list=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
		if(list.size()>0){
			String elegroupcode= eleSetEleGroupDefService.getMaxElegroupcode(elesetcode);
			eleSetEleGroupDef.setElegroupcode(getNextGroupCode(elegroupcode));
		}else{
			eleSetEleGroupDef.setElegroupcode("G0001");
		}
		
		eleSetEleGroupDef.setElesetcode(elesetcode);
		eleSetEleGroupDef.setChnname(chnname);
		eleSetEleGroupDef.setInvalid(0);
		eleSetEleGroupDef.setOrderno(0);
		Map map=new HashMap();
		try{
			eleSetEleGroupDefService.saveEleSetEleGroupDef(eleSetEleGroupDef);
//			cacheCleanController.cleanCache();
			 map.put("status", 0);
		}catch (Exception e) {
			 map.put("status", 1);
		}
//		List<EleSetEleGroupDef> clist=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
//		List<EleSetElementDef> flist=eleSetElementDefService.getListByelesetcode(elesetcode);
//		map.put("clist", clist);
//		map.put("flist", flist);
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping(value = "/updateEleGroup")
	public void updateEleGroup(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model) {
		String elesetcode=(String)paramMap.get("elesetcode");
		String chnname=(String)paramMap.get("chnname");
		String id=(String)paramMap.get("id");
		//根据elesetcode去查询
		List<EleSetEleGroupDef> list=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
		EleSetEleGroupDef eleSetEleGroupDef=eleSetEleGroupDefService.getEleSetEleGroupDef(Integer.valueOf(id));
		eleSetEleGroupDef.setElesetcode(elesetcode);
		eleSetEleGroupDef.setChnname(chnname);
		Map map=new HashMap();
		try{
			eleSetEleGroupDefService.saveEleSetEleGroupDef(eleSetEleGroupDef);
//			cacheCleanController.cleanCache();
			 map.put("status", 0);
		}catch (Exception e) {
			 map.put("status", 1);
		}
		String json=JsonMapper.toJsonString(map);
		renderText(json, response);
	}
	@RequestMapping(value = "/saveElement")
	public String saveElement(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		String elesetcode=(String)paramMap.get("elesetcode");
		String elegroupcode=(String)paramMap.get("elegroupcode");
		String uelecode=(String)paramMap.get("uelecode");
		String celecode=(String)paramMap.get("celecode");
		String elementname=(String)paramMap.get("elementname");
		String datatype=(String)paramMap.get("datatype");
		String dataunit=(String)paramMap.get("dataunit");
		String isoptional=(String)paramMap.get("isoptional");
		String isfilter=(String)paramMap.get("isfilter");
		String isseleted=(String)paramMap.get("isseleted");
		String isqc=(String)paramMap.get("isqc");
		EleSetElementDef eleSetElementDef=new EleSetElementDef();
		eleSetElementDef.setElesetcode(elesetcode);
		eleSetElementDef.setElegroupcode(elegroupcode);
		eleSetElementDef.setUelecode(uelecode);
		eleSetElementDef.setCelecode(celecode);
		eleSetElementDef.setElementname(elementname);
		eleSetElementDef.setDatatype(datatype);
		eleSetElementDef.setDataunit(dataunit);
		eleSetElementDef.setIsoptional(Integer.valueOf(isoptional));
		eleSetElementDef.setIsfilter(Integer.valueOf(isfilter));
		eleSetElementDef.setIsseleted(Integer.valueOf(isseleted));
		eleSetElementDef.setIsqc(Integer.valueOf(isqc));
		eleSetElementDef.setOrderno(0);
		eleSetElementDef.setInvalid(0);
		eleSetElementDef.setIshidden(0);
		eleSetElementDef.setId(eleSetElementDefService.getMaxId()+1);
		try{
			eleSetElementDefService.saveEleSetElementDef(eleSetElementDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		
		return "redirect:"+"/eleSetEleGroup/elementList?elesetcode="+elesetcode+"&elegroupcode="+elegroupcode;	
	}
	@RequestMapping(value = "/saveUpdateElement")
	public String saveUpdateElement(@RequestParam
			Map<String, Object> paramMap,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		String id=(String)paramMap.get("id");
		EleSetElementDef eleSetElementDef=eleSetElementDefService.getEleSetElementDefById(Integer.valueOf(id));
		String elesetcode=(String)paramMap.get("elesetcode");
		String elegroupcode=(String)paramMap.get("elegroupcode");
		String uelecode=(String)paramMap.get("uelecode");
		String celecode=(String)paramMap.get("celecode");
		String elementname=(String)paramMap.get("elementname");
		String datatype=(String)paramMap.get("datatype");
		String dataunit=(String)paramMap.get("dataunit");
		String isoptional=(String)paramMap.get("isoptional");
		String isfilter=(String)paramMap.get("isfilter");
		String isseleted=(String)paramMap.get("isseleted");
		String isqc=(String)paramMap.get("isqc");
		eleSetElementDef.setElesetcode(elesetcode);
		eleSetElementDef.setElegroupcode(elegroupcode);
		eleSetElementDef.setUelecode(uelecode);
		eleSetElementDef.setCelecode(celecode);
		eleSetElementDef.setElementname(elementname);
		eleSetElementDef.setDatatype(datatype);
		eleSetElementDef.setDataunit(dataunit);
		eleSetElementDef.setIsoptional(Integer.valueOf(isoptional));
		eleSetElementDef.setIsfilter(Integer.valueOf(isfilter));
		eleSetElementDef.setIsseleted(Integer.valueOf(isseleted));
		eleSetElementDef.setIsqc(Integer.valueOf(isqc));
		try{
			eleSetElementDefService.saveEleSetElementDef(eleSetElementDef);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}		
		
		return "redirect:"+"/eleSetEleGroup/elementList?elesetcode="+elesetcode+"&elegroupcode="+elegroupcode;	
	}
	private String getNextGroupCode(String elegroupcode){
		elegroupcode=elegroupcode.substring(1,5);
		Integer next=Integer.valueOf(elegroupcode)+1;
		if(String.valueOf(next).length()==1){
			elegroupcode="G000"+String.valueOf(next);
		}else if(String.valueOf(next).length()==2){
			elegroupcode="G00"+String.valueOf(next);
		}else if(String.valueOf(next).length()==3){
			elegroupcode="G0"+String.valueOf(next);
		}else if(String.valueOf(next).length()==4){
			elegroupcode="G"+String.valueOf(next);
		}
		return elegroupcode;
	}
	/**
	 * 批量修改菜单排序  dataDef表
	 */
	@RequestMapping(value = "elementSort")
	public String elementSort(@RequestParam
			Map<String, Object> paramMap,String[] ids, RedirectAttributes redirectAttributes) {
		String elesetcode=(String)paramMap.get("elesetcode");
		String elegroupcode=(String)paramMap.get("elegroupcode");
		try {
		int len = ids.length;
		EleSetElementDef[] eleSetElementDef = new EleSetElementDef[len];
    	
    		for (int i = 0; i < len; i++) {
    			eleSetElementDef[i] = eleSetElementDefService.getEleSetElementDefById(Integer.valueOf(ids[i]));
    			eleSetElementDef[i].setOrderno(i);
    			eleSetElementDefService.saveEleSetElementDef(eleSetElementDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/eleSetEleGroup/elementList?elesetcode="+elesetcode+"&elegroupcode="+elegroupcode;	
	}
	/**
	 *排序功能-大小类排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortElement")
	public String sortCategory(HttpServletRequest request,HttpServletResponse response,Model model,
			String elesetcode,String elegroupcode){
		if(elegroupcode!=null&&!"".equals(elegroupcode)){
			List<EleSetElementDef> list=eleSetElementDefService.getListByelesetcode(elesetcode,elegroupcode);
			model.addAttribute("elesetcode",elesetcode);
			model.addAttribute("elegroupcode",elegroupcode);
			model.addAttribute("list",list);
		}else{
			List<EleSetElementDef> list=eleSetElementDefService.getListByelesetcode2(elesetcode);
			model.addAttribute("elesetcode",elesetcode);
			model.addAttribute("list",list);
		}
		return "modules/data/elementSortList";
		
	}
	/**
	 *排序功能-大小类排序
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/sortElementGroup")
	public String sortElementGroup(HttpServletRequest request,HttpServletResponse response,Model model,
			String elesetcode){
		List<EleSetEleGroupDef> list=eleSetEleGroupDefService.getListByelesetcode(elesetcode);
		model.addAttribute("list",list);
		model.addAttribute("elesetcode",elesetcode);
		return "modules/data/elementGpSortList";
		
	}
	/**
	 * 批量修改菜单排序  dataDef表
	 */
	@RequestMapping(value = "elementGpSort")
	public String elementGpSort(@RequestParam
			Map<String, Object> paramMap,String[] ids, RedirectAttributes redirectAttributes) {
		String elesetcode=(String)paramMap.get("elesetcode");
		try {
		int len = ids.length;
		EleSetEleGroupDef[] eleSetEleGroupDef = new EleSetEleGroupDef[len];
    	
    		for (int i = 0; i < len; i++) {
    			eleSetEleGroupDef[i] = eleSetEleGroupDefService.getEleSetEleGroupDef(Integer.valueOf(ids[i]));
    			eleSetEleGroupDef[i].setOrderno(i);
    			eleSetEleGroupDefService.saveEleSetEleGroupDef(eleSetEleGroupDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/eleSetEleGroup/sortElementGroup?elesetcode="+elesetcode;	
	}
}

