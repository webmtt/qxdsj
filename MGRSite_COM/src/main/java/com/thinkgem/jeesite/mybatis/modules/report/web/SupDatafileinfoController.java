/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.web;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupDatafileinfo;
import com.thinkgem.jeesite.mybatis.modules.report.service.*;
import org.apache.lucene.util.RamUsageEstimator;
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
import java.util.*;

/**
 * 报表数据文件管理Controller
 *
 * @author yang.kq
 * @version 2019-11-06
 */
@Controller
@RequestMapping(value = "/report/supDatafileinfo")
public class SupDatafileinfoController extends BaseController {

  @Autowired
  private SupDatafileinfoService supDatafileinfoService;
  @Autowired
  private ReportLogServices reportLogServices;

  @ModelAttribute
  public SupDatafileinfo get(@RequestParam(required = false) String id) {
    SupDatafileinfo entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = supDatafileinfoService.get(id);
    }
    if (entity == null) {
      entity = new SupDatafileinfo();
    }
    return entity;
  }

  @RequestMapping(value = {"/list", ""})
  public String list(
      SupDatafileinfo supDatafileinfo,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    Page<SupDatafileinfo> page =
        supDatafileinfoService.findPage(
            new Page<SupDatafileinfo>(request, response), supDatafileinfo);
    model.addAttribute("page", page);
    return "modules/report/supDatafileinfoList";
  }
  
  @RequestMapping(value = "/queryData")
  @ResponseBody
  public String queryData(HttpServletRequest request, HttpServletResponse response) {
    String stations = request.getParameter("stations");
    String projectID = request.getParameter("project");
    String queryType = request.getParameter("queryType");
    String starTime = request.getParameter("startime");
    String endTime = request.getParameter("endtime");
    String queryData = request.getParameter("queryData");
    String type = request.getParameter("type");
    String headInfo = request.getParameter("headInfo");
    Integer pageno=Integer.parseInt(request.getParameter("pageno"));
    Integer pagesize=Integer.parseInt(request.getParameter("pagesize"));
   String remoteIP=StringUtils.getRemoteAddr(request);
    List<Map<String, Object>> list = null;
    String dataType=null;
    long total=0;
    if (null != queryType) {
      dataType = queryType;
      Map<String,Object> resultData=supDatafileinfoService.getQueryData(stations,projectID,starTime,endTime,queryData,dataType,type,pageno,pagesize);
      list= (List<Map<String, Object>>) resultData.get("dataList");
      total= (long) resultData.get("count");
    }
    String result = null;
    ReportLogInfo rlog=new ReportLogInfo();
    rlog.setId(UUID.randomUUID()+"");
    rlog.setAddr(remoteIP);
    rlog.setDataNum(RamUsageEstimator.humanSizeOf(list));
    rlog.setDataType(dataType);
    rlog.setStationInfo(stations);
    rlog.setTime(DateUtils.getDateTime());
    if ("1".equals(type)) {
      Map<String,Object> resultMap=new HashMap<>();
      Page page=new Page(pageno,pagesize,total,list);
      Page<Object> page1=page.setList(list);
      resultMap.put("pageinfo",page1.toString());
      resultMap.put("datainfo",page);
      result = JSON.toJSONString(resultMap);
      rlog.setOperitorType("2");
    } else {
      supDatafileinfoService.exportData(response, headInfo, list,dataType);
      rlog.setOperitorType("3");
    }
    reportLogServices.insertLog(rlog);
    return result;
  }

  @RequestMapping(value = "/getDictForFileType")
  @ResponseBody
  public String getDictForFileType(HttpServletRequest request, HttpServletResponse response) {
    String type = request.getParameter("type");
    String result = DictUtils.getDictListJson(type);
    return result;
  }

  @RequestMapping(value = "/loadData")
  @ResponseBody
  public void loadData(HttpServletRequest request, HttpServletResponse response) {
    String path = request.getParameter("path");
    String fileName = request.getParameter("filename");
    //    String result = DictUtils.getDictListJson(type);

  }

  @RequestMapping(value = "/form")
  public String form(SupDatafileinfo supDatafileinfo, Model model) {
    model.addAttribute("supDatafileinfo", supDatafileinfo);
    return "modules/report/supDatafileinfoForm";
  }

  @RequestMapping(value = "/queryNew")
  public String queryNew(Model model) {
    Page page=new Page();
    page.setPageSize(30);
    model.addAttribute("page", page);
    return "modules/report/supDataQueryListNew";
  }
    @RequestMapping(value = "/query")
    public String query(Model model) {
        Page page=new Page();
        page.setPageSize(30);
        model.addAttribute("page", page);
        return "modules/report/supDataQueryList";
    }
  @RequestMapping(value = "/save")
  public String save(
          SupDatafileinfo supDatafileinfo, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, supDatafileinfo)) {
      return form(supDatafileinfo, model);
    }
    supDatafileinfoService.save(supDatafileinfo);
    addMessage(redirectAttributes, "保存文件成功");
    return "redirect:/report/supDatafileinfo/list";
  }

  @RequestMapping(value = "/delete")
  public String delete(SupDatafileinfo supDatafileinfo, RedirectAttributes redirectAttributes) {
    supDatafileinfoService.delete(supDatafileinfo);
    addMessage(redirectAttributes, "删除文件成功");

    return "redirect:/report/supDatafileinfo/list";
  }

  @RequestMapping(value ="/fileUpload")
  @ResponseBody
  public String fileUpload(HttpServletRequest request, HttpServletResponse response) {
    String path = supDatafileinfoService.fileUpload(request);
    return path;
  }
}
