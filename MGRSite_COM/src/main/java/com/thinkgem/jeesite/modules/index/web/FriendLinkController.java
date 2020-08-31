package com.thinkgem.jeesite.modules.index.web;

import java.util.ArrayList;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.FriendLink;
import com.thinkgem.jeesite.modules.index.entity.PortalDataCategoryDef;
import com.thinkgem.jeesite.modules.index.entity.PortalMenu;
import com.thinkgem.jeesite.modules.index.entity.PortalMenuDef;
import com.thinkgem.jeesite.modules.index.service.FriendLinkService;
import com.thinkgem.jeesite.modules.index.service.PortalMenuDefService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;




@Controller
@RequestMapping(value="/friendLink")
public class FriendLinkController extends BaseController{
	@Autowired
	private FriendLinkService friendLinkService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private ComparasService comparasService;

	
	@RequestMapping(value="/list")
	public String friendLinkList(FriendLink friendLink,String linktype,HttpServletRequest request,HttpServletResponse response,Model model){
		if(linktype==null||"".equals(linktype)){
			linktype="RelationLinkNew,DomesticDep,InternationUnit,SpecialSite";
		}
		List<FriendLink> list=friendLinkService.getFriendLinkByLinkType(linktype);
		model.addAttribute("list", list);
		model.addAttribute("linktype", linktype);
		model.addAttribute("friendLink", friendLink);
		return "modules/index/FriendLinkList";
	}
	@RequestMapping(value="/edit")
	public String edit(String id,HttpServletRequest request, HttpServletResponse response, Model model){
		FriendLink friendLink=friendLinkService.getFriendLink(Integer.valueOf(id));
		model.addAttribute("friendLink", friendLink);
		return "modules/index/FriendLinkEdit";	
	}
	@RequestMapping(value="/add")
	public String add(FriendLink friendLink,HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("friendLink", friendLink);
		return "modules/index/FriendLinkAdd";	
	}
	@RequestMapping(value="/view")
	public String view(String id,HttpServletRequest request, HttpServletResponse response, Model model){
		FriendLink friendLink=friendLinkService.getFriendLink(Integer.valueOf(id));
		model.addAttribute("friendLink", friendLink);
		return "modules/index/FriendLinkView";	
	}
	@RequestMapping(value="/delete")
	public String delete(String id,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			friendLinkService.deleteFriendLinkById(id);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e1) {
			addMessage(redirectAttributes,  "删除失败");
			e1.printStackTrace();
		}
		return "redirect:"+"/friendLink/list";	
	}
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/save")
	public String save(FriendLink friendLink,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			friendLinkService.save(friendLink);
//			cacheCleanController.cleanCache();
			addMessage(redirectAttributes, "保存成功");	
		} catch (Exception e1) {
			addMessage(redirectAttributes,  "保存失败");	
			e1.printStackTrace();
		}
		return "redirect:"+"/friendLink/list";	
	}
	/**
	 * 批量修改菜单排序
	 */
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	FriendLink[] friendLink = new FriendLink[len];
    	try {
    		for (int i = 0; i < len; i++) {
    			friendLink[i] = friendLinkService.getFriendLink(Integer.valueOf(ids[i]));
    			friendLink[i].setOrderno(sorts[i]);
    			friendLinkService.save(friendLink[i]);
        	}
//        	cacheCleanController.cleanCache();
        	addMessage(redirectAttributes, "保存菜单排序成功!");
    	} catch (Exception e1) {
    		addMessage(redirectAttributes, "保存菜单排序失败!");
			e1.printStackTrace();
		}
    	return "redirect:"+"/friendLink/list";	
	}
}
