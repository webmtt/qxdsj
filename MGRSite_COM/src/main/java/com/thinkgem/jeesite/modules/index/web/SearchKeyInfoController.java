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
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.FriendLink;
import com.thinkgem.jeesite.modules.index.entity.PortalDataCategoryDef;
import com.thinkgem.jeesite.modules.index.entity.PortalMenu;
import com.thinkgem.jeesite.modules.index.entity.PortalMenuDef;
import com.thinkgem.jeesite.modules.index.entity.SearchKeyInfo;
import com.thinkgem.jeesite.modules.index.service.FriendLinkService;
import com.thinkgem.jeesite.modules.index.service.PortalMenuDefService;
import com.thinkgem.jeesite.modules.index.service.SearckKeyInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;




@Controller
@RequestMapping(value="/searchKey")
public class SearchKeyInfoController extends BaseController{
	@Autowired
	private SearckKeyInfoService searckKeyInfoService;
	@Autowired
	private CacheCleanController cacheCleanController;
	@Autowired
	private ComparasService comparasService;

	
	@RequestMapping(value="/list")
	public String friendLinkList(HttpServletRequest request,HttpServletResponse response,Model model,@RequestParam Map<String, Object> paramMap){
		String pageNo=(String)paramMap.get("pageNo");
		String pageSize=(String)paramMap.get("pageSize");
		String orderBy = request.getParameter("orderBy");
		String searchKeyWord = request.getParameter("searchKeyWord");
		if(orderBy == null || orderBy.equals("")){
			orderBy = "searchsumnum DESC ";
		}
		String orderByNullsLast = orderBy != null ? (orderBy.contains("DESC") ? orderBy + " NULLS LAST" : orderBy) : null;
		if(pageNo==null||"".equals(pageNo)||pageSize==null||"".equals(pageSize)){
			pageNo="1";
			pageSize="20";
		}
		int no=Integer.parseInt(pageNo);
		int size=Integer.parseInt(pageSize);
		Page<SearchKeyInfo> searchKeyPage = searckKeyInfoService.getSearckKeyInfoList(no, size, orderByNullsLast,searchKeyWord);
		model.addAttribute("page", searchKeyPage);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("searchKeyWord", searchKeyWord);
		return "modules/index/searchKeyList";
	}
	
	
	
	
	
}
