/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.subject.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.modules.UserDataRole.entity.ZTreeInfo;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.SupSubjectdef;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.TableInfo;
import com.thinkgem.jeesite.mybatis.modules.subject.service.SupSubjectdefService;
import net.sf.json.JSONString;
import net.sf.json.groovy.GJson;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 专题产品Controller
 * @author yangkq
 * @version 2020-02-28
 */
@Controller
@RequestMapping(value = "/subject/supSubjectdef")
public class SupSubjectdefController extends BaseController {

	@Autowired
	private SupSubjectdefService supSubjectdefService;
	
	@ModelAttribute
	public SupSubjectdef get(@RequestParam(required=false) String id) {
		SupSubjectdef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supSubjectdefService.get(id);
		}
		if (entity == null){
			entity = new SupSubjectdef();
		}
		return entity;
	}

	@RequestMapping(value ="/list")
	public String list(SupSubjectdef supSubjectdef, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SupSubjectdef> list = Lists.newArrayList();
		List<SupSubjectdef> sourcelist = supSubjectdefService.findAllConfig();
		SupSubjectdef.sortList(list, sourcelist, SupSubjectdef.getRootId(), true);
		model.addAttribute("list", list);
		return "modules/subject/supSubjectdefList";
	}
	@RequestMapping(value ="/getSubjectInfo")
	@ResponseBody
	public List<ZTreeInfo> getSubjectInfo(SupSubjectdef supSubjectdef, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SupSubjectdef> list = Lists.newArrayList();
		List<SupSubjectdef> sourcelist = supSubjectdefService.findAllConfig();
		List<ZTreeInfo> rs=new ArrayList<>();
		ZTreeInfo zt=null;
		for(SupSubjectdef subject:sourcelist) {
			if (!"1".equals(subject.getId())){
				zt = new ZTreeInfo();
				zt.setId(subject.getId() + "");
				zt.setName(subject.getProductName());
				zt.setPid(subject.getParent().getId());
				rs.add(zt);
			}
		}
		return rs;
	}
	@RequestMapping(value ="/getColumn")
	@ResponseBody
	public List<TableInfo> getColumn(HttpServletRequest request, HttpServletResponse response, Model model) {
		String tablename=request.getParameter("tablename");
		List<TableInfo> list = supSubjectdefService.findTableInfo(tablename);
		return list;
	}
	@RequestMapping(value = "/form")
	public String form(SupSubjectdef supSubjectdef, Model model) {
		if (supSubjectdef.getParent()==null||supSubjectdef.getParent().getId()==null){
			supSubjectdef.setParent(new SupSubjectdef(supSubjectdef.getRootId()));
		}
		if(!"".equals(supSubjectdef.getProcode())){
			supSubjectdef.setOldProCode(supSubjectdef.getProcode());
		}
		if(!"".equals(supSubjectdef.getKind())){
			supSubjectdef.setOldKind(supSubjectdef.getKind());
		}
		supSubjectdef.setParent(supSubjectdefService.get(supSubjectdef.getParent().getId()));
		model.addAttribute("supSubjectdef", supSubjectdef);
		return "modules/subject/supSubjectdefForm";
	}
	@RequestMapping(value = "/updatepub")
	public String updatepub(SupSubjectdef supSubjectdef, RedirectAttributes redirectAttributes) {
		String mesg="";
		if("1".equals(supSubjectdef.getIspub())){
			supSubjectdef.setIspub("2");
		}else{
			supSubjectdef.setIspub("1");
		}
		addMessage(redirectAttributes, mesg);
		supSubjectdefService.updatepub(supSubjectdef);

		return "redirect:/subject/supSubjectdef/list";
	}
	@RequestMapping(value = "/save")
	public String save(SupSubjectdef supSubjectdef, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, supSubjectdef)){
			return form(supSubjectdef, model);
		}
		supSubjectdef.setIspub("1");
		supSubjectdefService.save(supSubjectdef);
		addMessage(redirectAttributes, "保存产品成功");
		return "redirect:/subject/supSubjectdef/list";
	}

	@RequestMapping(value = "/delete")
	public String delete(SupSubjectdef supSubjectdef, RedirectAttributes redirectAttributes) {
		supSubjectdefService.delete(supSubjectdef);
		addMessage(redirectAttributes, "删除产品成功");
		return "redirect:/subject/supSubjectdef/list";
	}
	@ResponseBody
	@RequestMapping(value = "/treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SupSubjectdef> list = supSubjectdefService.findAllConfig();
		for (int i=0; i<list.size(); i++){
			SupSubjectdef e = list.get(i);
			if(extId.equals(e.getParent().getId())){
				continue;
			}
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent().getId());
				map.put("name", e.getProductName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	@ResponseBody
	@RequestMapping(value = "/checkProcode")
	public boolean checkProcode(HttpServletRequest request, HttpServletResponse response) {
		String procode=request.getParameter("procode");
		String kind=request.getParameter("kind");
		boolean flag=supSubjectdefService.checkProcode(procode,kind);
		return flag;
	}
}