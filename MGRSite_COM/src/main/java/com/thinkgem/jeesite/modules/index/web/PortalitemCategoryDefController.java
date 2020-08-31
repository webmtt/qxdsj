package com.thinkgem.jeesite.modules.index.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategory;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategoryDef;
import com.thinkgem.jeesite.modules.index.service.PortalitemCategoryDefService;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;




@Controller
@RequestMapping(value="/portalitemCategoryDef")
public class PortalitemCategoryDefController extends BaseController{
	@Autowired
	private PortalitemCategoryDefService portalitemCategoryDefService;
	@Autowired
	private CacheCleanController cacheCleanController;

	/**
	 *获取气象业务列表并在展示页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String portalitemCategoryDefList(HttpServletRequest request,HttpServletResponse response,Model model){
		List<PortalitemCategoryDef> list=new ArrayList<PortalitemCategoryDef>();
		List<PortalitemCategoryDef> pcds=portalitemCategoryDefService.findAll();
		PortalitemCategoryDef.sortList(list, pcds, 0);
		model.addAttribute("pcds", list);
		return "modules/index/portalitemCategoryDefList";
	}

	/**
	 * 跳转到添加子类型页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	public String toAddportalitemCategoryDef(HttpServletRequest request, HttpServletResponse response, Model model){
    	Integer pid=Integer.valueOf(request.getParameter("pid"));
    	Integer funcitemid=Integer.valueOf(request.getParameter("funcitemid"));
    	PortalitemCategoryDef parent=null;
    	String pname="";
    	if(pid!=null&&!"".equals(pid)){
    		parent =portalitemCategoryDefService.findPortalitemCategoryDefById(funcitemid);
    		if(parent!=null){
				pname=parent.getChnname();
			}	
		}
    	model.addAttribute("funcitemid", funcitemid);
    	model.addAttribute("pid", pid);
    	model.addAttribute("pname", pname);
    	
		return "modules/index/portalitemCategoryDefAdd";
	}
	/**
	 * 添加子类型
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/save")
	public String saveportalitemCategoryDef(PortalitemCategoryDef portalitemCategoryDef,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		PortalitemCategory pc=new PortalitemCategory();
		
		String parent=request.getParameter("treeNodeId");//是否选择树节点
		Integer parentidNo=portalitemCategoryDef.getParent().getFuncitemid();
		
		if(parent==null||"".equals(parent)){//不选择
			parentidNo=Integer.valueOf(request.getParameter("funcitemid"));
		}else{//选择
			parentidNo=Integer.valueOf(parent);
		}
		pc.setParentid(parentidNo);
		
		//
		//Integer funcitemid=portalitemCategoryDefService.getPortalitemCategoryDefById(parentidNo);
		//通过parentidNo,新添加的节点layer值为0？？？、
		Integer layer=portalitemCategoryDefService.getPortalitemCategoryDefLayerByPid(parentidNo);
		if(layer==0){
			layer=3;
		}
//		if(funcitemid==0){//新添加节点
//			funcitemid=parentidNo*100;
//			layer=3;
//		}
//		pc.setFuncitemid(funcitemid+1);
		
		pc.setLayer(layer);
		
		String chnname=request.getParameter("chnname");
		String shortchnname=request.getParameter("shortchnname");
		String chndescription=request.getParameter("chndescription");
		
		Integer isopen=Integer.valueOf(request.getParameter("isopen"));
		String linkurl=request.getParameter("linkurl");
		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		
		pc.setChnname(chnname);
		pc.setShortchnname(shortchnname);
		pc.setChndescription(chndescription);
		
		pc.setShowtype(1);//数据库中存值为1
		pc.setInvalid(0);
		
		pc.setIsopen(isopen);
		pc.setLinkurl(linkurl);
		pc.setOrderno(orderno);
		
		try {
			portalitemCategoryDefService.savePc(pc);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes,  "保存失败");	
			e1.printStackTrace();
		}
		
		return "redirect:"+"/portalitemCategoryDef/list";	
	}
	
	/**
	 * 跳转至编辑类型界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
    @RequestMapping(value="/toEdit")
	public String toEditportalitemCategoryDef(HttpServletRequest request, HttpServletResponse response, Model model){
    	Integer funcitemid=Integer.valueOf(request.getParameter("funcitemid"));
    	Integer pid=Integer.valueOf(request.getParameter("pid"));
    	
    	PortalitemCategoryDef portalitemCategoryDef=portalitemCategoryDefService.findPortalitemCategoryDefById(funcitemid);   	  		
    	PortalitemCategoryDef parent=portalitemCategoryDefService.findPortalitemCategoryDefById(pid); 
    	String pname=parent.getChnname();
    	
		model.addAttribute("funcitemid", funcitemid);
    	model.addAttribute("pid", pid);
    	model.addAttribute("pname", pname);
    	model.addAttribute("portalitemCategoryDef", portalitemCategoryDef);
		return "modules/index/portalitemCategoryDefEdit";
	}
	
    /**
	 * 获得类型树
	 * @param extId
	 * @param paramMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/getTreeList")
	@ResponseBody
	public List<Map<String, Object>> getTreeList(@RequestParam(required=false) Long extId, Map<String, Object> paramMap,
			HttpServletRequest request, HttpServletResponse response){
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<PortalitemCategoryDef> pcds=portalitemCategoryDefService.findAll();
		for(PortalitemCategoryDef portalitemCategoryDef:pcds){
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", portalitemCategoryDef.getFuncitemid());
			map.put("pId",portalitemCategoryDef.getParent().getFuncitemid());
			map.put("name", portalitemCategoryDef.getChnname());
			mapList.add(map);
		}
		return mapList;
	}
    
    /**
	 * 编辑类型界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
    @RequestMapping(value="/edit")
	public String editportalitemCategoryDef(PortalitemCategoryDef portalitemCategoryDef,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){

    	PortalitemCategory pc=new PortalitemCategory();
		
		String parent=request.getParameter("treeNodeId");//是否选择树节点
		Integer parentidNo=portalitemCategoryDef.getParent().getFuncitemid();
		
		if(parent==null||"".equals(parent)){//不选择
			parentidNo=Integer.valueOf(request.getParameter("parent.funcitemid"));
		}else{//选择
			parentidNo=Integer.valueOf(parent);
		}
		pc.setParentid(parentidNo);
		
		pc.setFuncitemid(portalitemCategoryDef.getFuncitemid());
		
		Integer layer=portalitemCategoryDefService.getPortalitemCategoryDefLayerByPid(parentidNo);
		
//		if(portalitemCategoryDef.getFuncitemid()==null||"".equals(portalitemCategoryDef.getFuncitemid())){
//			Integer funcitemid=portalitemCategoryDefService.getPortalitemCategoryDefById(parentidNo);
//			if(funcitemid==0){//新添加节点
//				funcitemid=parentidNo*100;
//			}
//			pc.setFuncitemid(funcitemid+1);
//		}else{
//			pc.setFuncitemid(portalitemCategoryDef.getFuncitemid());
//		}
		
		pc.setLayer(layer);
		
		String chnname=request.getParameter("chnname");
		String shortchnname=request.getParameter("shortchnname");
		String chndescription=request.getParameter("chndescription");
		
		Integer isopen=Integer.valueOf(request.getParameter("isopen"));
		String linkurl=request.getParameter("linkurl");
		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
		
		pc.setChnname(chnname);
		pc.setShortchnname(shortchnname);
		pc.setChndescription(chndescription);
		
		pc.setShowtype(1);//数据库中存值为1
		pc.setInvalid(0);
		
		pc.setIsopen(isopen);
		pc.setLinkurl(linkurl);
		pc.setOrderno(orderno);
		
		try {
			portalitemCategoryDefService.savePc(pc);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "修改成功");
		} catch (Exception e1) {
			addMessage(redirectAttributes,  "修改失败");	
			e1.printStackTrace();
		}
		
		return "redirect:"+"/portalitemCategoryDef/list";
    	
    	
	}
	
    /**
	 * 查看详情
	 * @return
	 */
	@RequestMapping("/getDetail")
	public String getDetail(HttpServletRequest request,HttpServletResponse response,Model model){
		Integer funcitemid=Integer.valueOf(request.getParameter("funcitemid"));
		PortalitemCategoryDef portalitemCategoryDef=portalitemCategoryDefService.findPortalitemCategoryDefById(funcitemid);
		model.addAttribute("funcitemid", funcitemid);
    	model.addAttribute("portalitemCategoryDef", portalitemCategoryDef);
		return "modules/index/portalitemCategoryDefDetail";
	}
	
	@RequestMapping("/delete")
	public String deletePortalitemCategoryDef(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		Integer funcitemid=Integer.valueOf(request.getParameter("funcitemid"));
    	Integer pid=Integer.valueOf(request.getParameter("pid"));
    	PortalitemCategoryDef pcd=portalitemCategoryDefService.findPortalitemCategoryDefById(funcitemid);
    	Integer layer=pcd.getLayer();
		try {
			if(layer==1){//有两级节点
	    		List<PortalitemCategory> pclist= portalitemCategoryDefService.findByPid(funcitemid);
	    		for(int i=0;i<pclist.size();i++){
	    			Integer fid=pclist.get(i).getFuncitemid();
	    			List<PortalitemCategory> pclist1= portalitemCategoryDefService.findByPid(fid);
	    			for(int j=0;j<pclist1.size();j++){
	    				Integer fid1=pclist1.get(j).getFuncitemid();
	    				portalitemCategoryDefService.delPortalitemCategoryDefById(fid1);
	    			}
	    		}
	    		portalitemCategoryDefService.delPortalitemCategoryDefByPid(funcitemid);
    			portalitemCategoryDefService.delPortalitemCategoryDefById(funcitemid);
	    	}else if(layer==2){//有一级节点
	    		portalitemCategoryDefService.delPortalitemCategoryDefById(funcitemid);
	    		portalitemCategoryDefService.delPortalitemCategoryDefByPid(funcitemid);
	    	}
			else if(layer==3){//没有下级节点
	    		portalitemCategoryDefService.delPortalitemCategoryDefById(funcitemid);
	    	}
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes, "删除失败");	
			e1.printStackTrace();
		}
    	
		
    	return "redirect:"+"/portalitemCategoryDef/list";	
	}

	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	PortalitemCategoryDef[] portalitemCategoryDef = new PortalitemCategoryDef[len];
    	try {
    		for (int i = 0; i < len; i++) {
        		portalitemCategoryDef[i] = portalitemCategoryDefService.findPortalitemCategoryDefById(Integer.valueOf(ids[i]));
        		portalitemCategoryDef[i].setOrderno(sorts[i]);
        		portalitemCategoryDefService.save(portalitemCategoryDef[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
		return "redirect:"+"/portalitemCategoryDef/list";
	}

}
