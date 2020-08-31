/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupReportsearchconf;
import com.thinkgem.jeesite.mybatis.modules.report.service.SupReportsearchconfService;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.SupSubjectdef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表查询参数配置Controller
 * @author yang.kq
 * @version 2019-11-05
 */
@Controller
@RequestMapping(value = "/report/supReportsearchconf")
public class SupReportsearchconfController extends BaseController {

	@Autowired
	private SupReportsearchconfService supReportsearchconfService;
	
	@ModelAttribute
	public SupReportsearchconf get(@RequestParam(required=false) String id) {
		SupReportsearchconf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supReportsearchconfService.get(id);
		}
		if (entity == null){
			entity = new SupReportsearchconf();
		}
		return entity;
	}

	@RequestMapping(value = {"/list", ""})
	public String list(SupReportsearchconf supReportsearchconf, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SupReportsearchconf> list = Lists.newArrayList();
		List<SupReportsearchconf> sourcelist = supReportsearchconfService.findAllConfig();
		SupReportsearchconf.sortList(list, sourcelist, SupReportsearchconf.getRootId(), true);
		model.addAttribute("list", list);
		return "modules/report/supReportsearchconfList";
	}
	@ResponseBody
	@RequestMapping(value = {"/getListByDataType"})
	public List<Map<String,Object>> getListByDataType(String dataType) {
		List<SupReportsearchconf> list = supReportsearchconfService.findConfigById("1",null);
		List<Map<String,Object>> result=new ArrayList<>();
		Map<String,Object> map=null;

		for(SupReportsearchconf s:list){
		    map=new HashMap<>();
		    map.put("id", s.getId());
            map.put("name",s.getParamName() );
            map.put("pid", "");
            boolean flag=false;
			List<SupReportsearchconf> clist = supReportsearchconfService.findConfigById(s.getId(),dataType);
            for(SupReportsearchconf t:clist){
				if(!"".equals(t.getParamCode())) {
				    if(!flag) {
                        result.add(map);
                        flag=true;
                    }
					map = new HashMap<>();
					map.put("id", t.getParamCode());
					map.put("name", t.getParamName());
					map.put("pid", s.getId());
					result.add(map);
				}
            }
		}
		return result;
	}
	@RequestMapping(value = "/form")
	public String form(SupReportsearchconf supReportsearchconf, Model model) {
		if (supReportsearchconf.getParent()==null||supReportsearchconf.getParent().getId()==null){
			supReportsearchconf.setParent(new SupReportsearchconf(SupReportsearchconf.getRootId()));
		}
		supReportsearchconf.setParent(supReportsearchconfService.get(supReportsearchconf.getParent().getId()));
		model.addAttribute("supReportsearchconf", supReportsearchconf);
		return "modules/report/supReportsearchconfForm";
	}

	@RequestMapping(value = "/save")
	public String save(SupReportsearchconf supReportsearchconf, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, supReportsearchconf)){
			return form(supReportsearchconf, model);
		}
		supReportsearchconfService.save(supReportsearchconf);
		addMessage(redirectAttributes, "保存参数成功");
		return "redirect:/report/supReportsearchconf/list";
	}

	@RequestMapping(value = "/delete")
	public String delete(SupReportsearchconf supReportsearchconf, RedirectAttributes redirectAttributes) {
		supReportsearchconfService.delete(supReportsearchconf);
		addMessage(redirectAttributes, "删除参数成功");
		return "redirect:/report/supReportsearchconf/list";
	}

	@ResponseBody
	@RequestMapping(value = "/treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SupReportsearchconf> list=new ArrayList<>();
		List<SupReportsearchconf> sourcelist = supReportsearchconfService.findAllConfig();
		list.add(sourcelist.get(0));
		SupReportsearchconf.sortList(list, sourcelist, SupReportsearchconf.getRootId(), true);
		for (int i=0; i<list.size(); i++){
			SupReportsearchconf e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent().getId());
				map.put("name", e.getParamName());
				mapList.add(map);
		}
		return mapList;
	}
	@ResponseBody
	@RequestMapping(value = "/getProject")
	public List<SupReportsearchconf> getProject(String parentId,String param_type) {
		List<SupReportsearchconf> list = supReportsearchconfService.findConfigById(parentId,param_type);
		return list;
	}
	/**
	 * 批量导入
	 */
	@RequestMapping(value = "/excelUpload")
	public String excelUpload(@RequestParam("file") CommonsMultipartFile file){
		try {
			InputStream in = file.getInputStream();
			supReportsearchconfService.upload(in,file);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "modules/report/supReportsearchconfList";
	}
}