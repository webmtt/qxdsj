package com.thinkgem.jeesite.modules.index.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.FuncItmesDef;
import com.thinkgem.jeesite.modules.index.entity.FuncItmes;
import com.thinkgem.jeesite.modules.index.service.FuncItmesDefService;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;

@Controller
@RequestMapping(value="/funcItmesDef")
public class FuncItmesDefController extends BaseController {
	
	@Autowired
	private FuncItmesDefService funcItmesDefService;
	@Autowired
	private ComparasService comparasService;
	@Autowired
	private CacheCleanController cacheCleanController;
	
	/**
	 * 获取栏目列表
	 * @param funcItmesDef
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String funcItmesDefList(FuncItmes funcItmes, HttpServletRequest request, HttpServletResponse response, Model model){
		String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
		
		List<FuncItmes> fis = funcItmesDefService.findFuncItmesAll();
		List<FuncItmes> list = new ArrayList<FuncItmes>();
		FuncItmes.sortList(list, fis, 0);
		for (FuncItmes item : list) {
			item.setLinkUrl(item.getLinkUrl().replace("<ctx>", prexUrl));
		}
		//parentID为空或找不到上级
		List<FuncItmes> noParentList = fis;
		noParentList.removeAll(list);
		for (FuncItmes item : noParentList) {
			findChild(item, fis, item.getFuncItemID());
			item.setLinkUrl(item.getLinkUrl().replace("<ctx>", prexUrl));
		}
		model.addAttribute("list", list);
		model.addAttribute("noParentList", noParentList);
		model.addAttribute("FuncItmes", funcItmes);
		
		return "modules/index/funcItmesDefList";
	}
	
	/**
	 * parentID为空或找不到上级：是否有下级，有下级isHasChild设为1
	 * */
	private void findChild(FuncItmes item, List<FuncItmes> list, Integer id){
		for (FuncItmes funcItmes : list) {
			if(funcItmes.getParent()!=null && id.equals(funcItmes.getParent().getFuncItemID())){
				item.setIsHasChild(1);
			}
		}
	}
	
	
	/**
	 * 模糊查询-分页
	 * @param funcItmesDef
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/search")
	public String funcItmesDefsearchPage(FuncItmesDef funcItmesDef, HttpServletRequest request, HttpServletResponse response, Model model){
		String CHNName = request.getParameter("CHNName");
		
		if(CHNName!=null && !"".equals(CHNName)){
			String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
			Page<FuncItmesDef> page = funcItmesDefService.searchByPage(new Page<FuncItmesDef>(request, response), CHNName);
			for (FuncItmesDef fid : page.getList()) {
				fid.setLinkUrl(fid.getLinkUrl().replace("<ctx>", prexUrl));
			}
			model.addAttribute("page", page);
			
			return "modules/index/funcItmesDefSearchPage";
		}else{
			return "redirect:"+"/funcItmesDef/list";
		}
		
		/*String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
		Page<FuncItmesDef> page = funcItmesDefService.getByPage(new Page<FuncItmesDef>(request, response), CHNName);
		List<FuncItmesDef> listAll = funcItmesDefService.findAll();
		for (FuncItmesDef fid : page.getList()) {
			String parentIDs = "";
			if(fid.getParentID()!= null && !"".equals(fid.getParentID())){
				List<String> parentNameList = new ArrayList<String>();
				//parentNameList.add(fid.getCHNName());
				findParentIDs(parentNameList, listAll, fid.getParentID());
				if(parentNameList.size() < 1){
					parentNameList.add(fid.getCHNName());
				}
				Collections.reverse(parentNameList);
				for (String name : parentNameList) {
					parentIDs += name + " > ";
				}
				parentIDs = parentIDs.substring(0, parentIDs.lastIndexOf(" > "));
			}
			fid.setParentIDs(parentIDs);
			fid.setLinkUrl(fid.getLinkUrl().replace("<ctx>", prexUrl));
		}
		
		model.addAttribute("page", page);*/
		
	}
	
	/**
	 * 根据子节点获取所有父节点
	 * */
	private void findParentIDs(List<String> parentNameList, List<FuncItmesDef> list, Integer parentID){
		for (FuncItmesDef funcItmesDef : list) {
			if(parentID!= null && parentID.equals(funcItmesDef.getFuncItemID())){
				parentNameList.add(funcItmesDef.getCHNName());
				findParentIDs(parentNameList, list, funcItmesDef.getParentID());
				break;
			}
		}
	}
	
	/**
	 * 跳转至编辑类型界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toEdit")
	public String toEditFuncItmesDef(HttpServletRequest request, HttpServletResponse response, Model model){
		Integer funcitemid = Integer.valueOf(request.getParameter("funcitemid"));
    	Integer pid = null;
    	try {
    		pid = Integer.valueOf(request.getParameter("pid"));
		} catch (Exception e) {
			logger.info("ParentID is null");
		}
    	
    	String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
    	FuncItmes funcItmesDef = funcItmesDefService.findFuncItmesByFuncID(funcitemid);
    	funcItmesDef.setLinkUrl(funcItmesDef.getLinkUrl().replace("<ctx>", prexUrl));
    	String pname = "";
    	if(pid != null && pid != 0){
    		FuncItmes parent = funcItmesDef.getParent();
    		try {
    			pname = parent.getCHNName();
			} catch (Exception e) {
				logger.info("No row with the given identifier exists");
			}
    	}
    	model.addAttribute("funcitemid", funcitemid);
    	model.addAttribute("pid", pid);
    	model.addAttribute("pname", pname);
    	model.addAttribute("funcItmesDef", funcItmesDef);
    	
    	return "modules/index/funcItmesDefEdit";
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
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		List<FuncItmes> list = new ArrayList<FuncItmes>();
		List<FuncItmes> fids = funcItmesDefService.findFuncItmesAll();
		FuncItmes.sortList(list, fids, 0);
		for (FuncItmes funcItmesDef : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", funcItmesDef.getFuncItemID());
			map.put("pId", funcItmesDef.getParent().getFuncItemID());
			map.put("name", funcItmesDef.getCHNName());
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
	public String editFuncItmesDef(FuncItmes funcItmes, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes){
		try {
			FuncItmesDef fid = funcItmesDefService.findFuncItmesDefByFuncID(funcItmes.getFuncItemID());
			String parentId = request.getParameter("treeNodeId");//是否选择树节点
			if(parentId!=null && !"".equals(parentId)){//选择树节点
				fid.setParentID(Integer.valueOf(parentId));
				Integer orderNo = funcItmesDefService.getMaxOrderNo(Integer.valueOf(parentId));
				fid.setOrderNo(orderNo+1);
				Integer layer = funcItmesDefService.getLayer(Integer.valueOf(parentId));
				fid.setLayer(layer+1);
			}
			String CHNName = request.getParameter("CHNName");
			String shortCHNName = request.getParameter("shortCHNName");
			String CHNDescription = request.getParameter("CHNDescription");
			String keyWord = request.getParameter("keyWord");
			Integer orderNo = Integer.valueOf(request.getParameter("orderNo"));
			String linkUrl = request.getParameter("linkUrl");
			String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
			if(linkUrl.startsWith(prexUrl)){
				linkUrl = linkUrl.replaceFirst(prexUrl, "<ctx>");
			}
			fid.setCHNName(CHNName);
			fid.setShortCHNName(shortCHNName);
			fid.setCHNDescription(CHNDescription);
			fid.setKeyWord(keyWord);
			fid.setOrderNo(orderNo);
			fid.setLinkUrl(linkUrl);
			funcItmesDefService.updateFuncItmesDef(fid);
			cacheCleanController.cacheclean();
			addMessage(redirectAttributes, "修改成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "修改失败");
			e.printStackTrace();
		}
		
		return "redirect:"+"/funcItmesDef/getDetail?funcitemid="+funcItmes.getFuncItemID();
	}
	
	/**
	 * 设为无效/有效
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/invalidOrNot")
	public String invalidOrNotFuncItmesDef(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model){
		Integer funcitemid = Integer.valueOf(request.getParameter("funcitemid"));
		
		try {
			FuncItmesDef fid = funcItmesDefService.findFuncItmesDefByFuncID(funcitemid);
			if(fid.getInvalid() == 0){
				fid.setInvalid(1);
			}else{
				fid.setInvalid(0);
			}
			funcItmesDefService.updateFuncItmesDef(fid);
			cacheCleanController.cacheclean();
			addMessage(redirectAttributes, "设置成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "设置失败");
			e.printStackTrace();
		}
		
		return "redirect:"+"/funcItmesDef/list";
	}
	
	/**
	 * 查看详情
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getDetail")
	public String getDetail(HttpServletRequest request,HttpServletResponse response,Model model){
		Integer funcitemid = Integer.valueOf(request.getParameter("funcitemid"));
		
		String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
		//FuncItmesDef fid = funcItmesDefService.findFuncItmesDefByFuncID(funcitemid);
		FuncItmes fid = funcItmesDefService.findFuncItmesByFuncID(funcitemid);
		Integer pid = null;
		try {
			pid = fid.getParent().getFuncItemID();
		} catch (Exception e) {
			logger.info("ParentID is null");
		}
		fid.setLinkUrl(fid.getLinkUrl().replace("<ctx>", prexUrl));
		model.addAttribute("funcitemid", funcitemid);
		model.addAttribute("pid", pid);
       	model.addAttribute("funcItmesDef", fid);
		
		return "modules/index/funcItmesDefDetail";
	}
	
	/**
	 * 跳转到添加子类型页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	public String toAddFuncItmesDef(HttpServletRequest request, HttpServletResponse response, Model model){
		Integer funcitemid = Integer.valueOf(request.getParameter("funcitemid"));
    	Integer pid = null;
    	try {
    		pid = Integer.valueOf(request.getParameter("pid"));
		} catch (Exception e) {
			logger.info("ParentID is null");
		}
    	
    	FuncItmesDef fid = funcItmesDefService.findFuncItmesDefByFuncID(funcitemid);
    	String pname = fid.getCHNName();
    	
    	model.addAttribute("funcitemid", funcitemid);
    	model.addAttribute("pid", pid);
    	model.addAttribute("pname", pname);
		
		return "modules/index/funcItmesDefAdd";
	}
	
	/**
	 * 添加子类型
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/save")
	public String saveFuncItmesDef(FuncItmes funcItmes, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes){
		FuncItmesDef fid = new FuncItmesDef();
		
		Integer funcitemid = Integer.valueOf(request.getParameter("funcitemid"));
		String parentId = request.getParameter("treeNodeId");//是否选择树节点
		if(parentId!=null && !"".equals(parentId)){//选择树节点
			fid.setParentID(Integer.valueOf(parentId));
			Integer funcItemID = funcItmesDefService.getMaxFuncItemID(Integer.valueOf(parentId));
			fid.setFuncItemID(funcItemID+1);
			Integer orderNo = funcItmesDefService.getMaxOrderNo(Integer.valueOf(parentId));
			fid.setOrderNo(orderNo+1);
			Integer layer = funcItmesDefService.getLayer(Integer.valueOf(parentId));
			fid.setLayer(layer+1);
		}else{//未选择树节点
			fid.setParentID(funcitemid);
			Integer funcItemID = funcItmesDefService.getMaxFuncItemID(funcitemid);
			fid.setFuncItemID(funcItemID+1);
			Integer orderNo = funcItmesDefService.getMaxOrderNo(funcitemid);
			fid.setOrderNo(orderNo+1);
			Integer layer = funcItmesDefService.getLayer(funcitemid);
			fid.setLayer(layer+1);
		}
		String CHNName = request.getParameter("CHNName");
		String shortCHNName = request.getParameter("shortCHNName");
		String CHNDescription = request.getParameter("CHNDescription");
		String keyWord = request.getParameter("keyWord");
		String linkUrl = request.getParameter("linkUrl");
		String prexUrl = (String)comparasService.getComparasByKey("ctx_test");
		if(linkUrl.startsWith(prexUrl)){
			linkUrl = linkUrl.replaceFirst(prexUrl, "<ctx>");
		}
		fid.setCHNName(CHNName);
		fid.setShortCHNName(shortCHNName);
		fid.setCHNDescription(CHNDescription);
		fid.setLinkUrl(linkUrl);
		fid.setKeyWord(keyWord);
		fid.setInvalid(0);
		fid.setShowType(1);
		fid.setIsNosearch(0);
		fid.setIsSitemap(1);
		
		try {
			funcItmesDefService.saveFuncItmesDef(fid);
			cacheCleanController.cacheclean();
			addMessage(redirectAttributes, "保存成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存失败");
			e.printStackTrace();
		}
		
		return "redirect:"+"/funcItmesDef/list";
	}
	
	/**
	 * 跳转至排序界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toSortOrder")
	public String toSortOrderFuncItmesDef(HttpServletRequest request, HttpServletResponse response, Model model){
		Integer funcitemid = Integer.valueOf(request.getParameter("funcitemid"));
		List<FuncItmesDef> list = funcItmesDefService.findFuncItmesDefByParentID(funcitemid);
		if(list!=null && list.size()>0){
			model.addAttribute("count", list.size());
		}else{
			model.addAttribute("count", 0);
		}
		model.addAttribute("list", list);
		model.addAttribute("funcitemid", funcitemid);
		
		return "modules/index/funcItmesDefSortOrder";
	}
	
	/**
	 * 排序
	 */
	@RequestMapping(value = "sortOrder")
	public String sortOrder(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		int len = ids.length;
		FuncItmesDef[] fid = new FuncItmesDef[len];
		try {
			for (int i = 0; i < len; i++) {
				fid[i] = funcItmesDefService.findFuncItmesDefByFuncID(Integer.valueOf(ids[i]));
				fid[i].setOrderNo(sorts[i]);
				funcItmesDefService.updateFuncItmesDef(fid[i]);
			}
			cacheCleanController.cacheclean();
			addMessage(redirectAttributes, "栏目排序成功!");
		} catch (Exception e) {
			addMessage(redirectAttributes, "栏目排序失败!");
			e.printStackTrace();
		}
		
		return "redirect:"+"/funcItmesDef/list";
	}
	
	/**
	 * 批量修改菜单排序-分页中批量排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		int len = ids.length;
		FuncItmesDef[] fid = new FuncItmesDef[len];
		try {
			for (int i = 0; i < len; i++) {
				fid[i] = funcItmesDefService.findFuncItmesDefByFuncID(Integer.valueOf(ids[i]));
				fid[i].setOrderNo(sorts[i]);
				funcItmesDefService.updateFuncItmesDef(fid[i]);
			}
			cacheCleanController.cacheclean();
			addMessage(redirectAttributes, "保存菜单排序成功!");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存菜单排序失败!");
			e.printStackTrace();
		}
		
		return "redirect:"+"/funcItmesDef/list";
	}
	
}
