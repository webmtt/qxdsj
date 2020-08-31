package com.thinkgem.jeesite.modules.UserDataRole.web;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.UserDataRole.entity.DataRolelimits;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import com.thinkgem.jeesite.modules.UserDataRole.entity.ZTreeInfo;
import com.thinkgem.jeesite.modules.UserDataRole.service.DataRoleService;
import com.thinkgem.jeesite.modules.UserDataRole.service.UserAndDataRoleService;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;
import com.thinkgem.jeesite.modules.Users.service.UserInfoService;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.data.service.DataCategoryDefService;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.SupSubjectdef;
import com.thinkgem.jeesite.mybatis.modules.subject.service.SupSubjectdefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据角色维护
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/5 10:01
 */
@Controller
@RequestMapping(value = "dataRole")
public class DataRoleController extends BaseController {
  @Autowired private DataRoleService dataRoleService;
  @Autowired private DataCategoryDefService dataCategoryDefService;
  @Autowired
  private SupSubjectdefService supSubjectdefService;
//  @Autowired private UserAndDataRoleService udrService;
  @Autowired private UserInfoService uiService;

  @RequestMapping(value = "/list")
  public String DataRoleList(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    // 编码或名称
    String idOrName = (String) request.getParameter("idOrName");
    String pageNo = (String) request.getParameter("pageNo");
    String pageSize = (String) request.getParameter("pageSize");

    if (pageNo == null || "".equals(pageNo) || pageSize == null || "".equals(pageSize)) {
      pageNo = "1";
      pageSize = "20";
    }
    Page<Datarole> page = dataRoleService.getDataroleList(idOrName, pageNo, pageSize);
    model.addAttribute("page", page);
    model.addAttribute("idOrName", idOrName);
    return "modules/userdatarole/dataroleList";
  }

  @RequestMapping(value = "/getDataRoleInfo")
  @ResponseBody
  public String getDataRoleInfo(HttpServletRequest request, HttpServletResponse response) {
    // 编码或名称
    List<Datarole> list = dataRoleService.findAll();
    String result = JSON.toJSONString(list);
    return result;
  }

  @RequestMapping(value = "/addDataRole")
  public String addDataRole(
      Datarole datarole, HttpServletRequest request, HttpServletResponse response, Model model) {
    int orderNo=dataRoleService.getMaxOrderNo();
    datarole.setOrderNo(orderNo+1);
    model.addAttribute("dataRole", datarole);
    return "modules/userdatarole/dataroleAdd";
  }

  @RequestMapping(value = "/updateDataRole")
  public String updateDataRole(
      Datarole datarole, HttpServletRequest request, HttpServletResponse response, Model model) {
    // 编码
    String dataroleid = (String) request.getParameter("id");
    if (dataroleid != null && !"".equals(dataroleid)) {
      datarole = dataRoleService.getdataroleByIdOrName(dataroleid, null);
    }
    model.addAttribute("dataRole", datarole);
    return "modules/userdatarole/dataroleForm";
  }

  @RequestMapping(value = "/viewDataRole")
  public String viewDataRole(
      Datarole datarole, HttpServletRequest request, HttpServletResponse response, Model model) {
    // 编码
    String dataroleid = (String) request.getParameter("id");
    if (dataroleid != null && !"".equals(dataroleid)) {
      datarole = dataRoleService.getdataroleByIdOrName(dataroleid, null);
    }
    model.addAttribute("dataRole", datarole);
    return "modules/userdatarole/dataroleView";
  }

  @RequestMapping(value = "/getselectData")
  @ResponseBody
  public String getSelectDataInfo(HttpServletRequest request, HttpServletResponse response) {
    String dataroleid = request.getParameter("dataroleid");
    List<DataRolelimits> list = dataRoleService.getdataroleLimitsById(dataroleid);
    String result = JSON.toJSONString(list);
    return result;
  }

  @RequestMapping(value = "/getselectDataView")
  @ResponseBody
  public String getselectDataView(HttpServletRequest request, HttpServletResponse response) {
    String userid = request.getParameter("userid");
    String dataroleid = request.getParameter("dataroleid");
    List<DataRolelimits> listSelect = dataRoleService.getdataroleLimitsById(dataroleid);
    List<String> listSelectDataId = new ArrayList<>();
    for (DataRolelimits drl : listSelect) {
      listSelectDataId.add(drl.getDataId());
    }
    List<SupSubjectdef> sourcelist = supSubjectdefService.findAllConfig();
    List<ZTreeInfo> rs = new ArrayList<ZTreeInfo>();
    ZTreeInfo zt = null;
    for (SupSubjectdef d : sourcelist) {
      zt = new ZTreeInfo();
      if (!listSelectDataId.contains(d.getId() + "")) {
        continue;
      }
      zt.setId(d.getId() + "");
      zt.setName(d.getProductName());
      zt.setPid(d.getParent().getId()+"");
      rs.add(zt);
    }

    String result = JSON.toJSONString(rs);
    return result;
  }

  @RequestMapping(value = "/getSelectRole")
  @ResponseBody
  public String getSelectRole(HttpServletRequest request, HttpServletResponse response) {
    String userid = request.getParameter("userid");
    List<Datarole> list = dataRoleService.getdataroleByUserId(userid);
    String result = JSON.toJSONString(list);
    return result;
  }

  @RequestMapping(value = "/getDataInfo")
  @ResponseBody
  public String getDataInfo(HttpServletRequest request, HttpServletResponse response) {
    List<DataCategoryDef> list = dataCategoryDefService.findAll();
    // 获取数据集
    List<Object[]> listdata = dataCategoryDefService.findDataCategory();
    List<ZTreeInfo> rs = new ArrayList<ZTreeInfo>();
    ZTreeInfo zt = null;
    for (DataCategoryDef d : list) {
      zt = new ZTreeInfo();
      zt.setId(d.getCategoryid() + "");
      zt.setName(d.getChnname());
      zt.setPid(d.getParent().getCategoryid()+"");
      rs.add(zt);
    }
    for (Object[] t : listdata) {
      zt = new ZTreeInfo();
      zt.setId(t[0] + "");
      zt.setName(t[1] + "");
      zt.setPid(t[2] + "");
      rs.add(zt);
    }
    String result = JSON.toJSONString(rs);
    return result;
  }

  @RequestMapping(value = "/save")
  public String save(Datarole datarole, HttpServletRequest request, HttpServletResponse response,  Model model,RedirectAttributes redirectAttribute) {
    String nodes = request.getParameter("nodes");
    String dataid = request.getParameter("dataroleId");
    String dataroleName = request.getParameter("dataroleName");
    String orderNo = request.getParameter("orderNo");
    String descriptionChn = request.getParameter("descriptionChn");
    datarole.setDataroleId(dataid);
    datarole.setDataroleName(dataroleName);
    datarole.setOrderNo(Integer.parseInt(orderNo));
    datarole.setDescriptionChn(descriptionChn);
    boolean isOK = dataRoleService.save(datarole, nodes);
    if (isOK) {
      addMessage(redirectAttribute, "添加成功");
      return "redirect:" + "/dataRole/list";
    } else {
      model.addAttribute("message", "提交失败！");
      return "modules/userdatarole/dataroleForm";
    }
  }

  @RequestMapping(value = "/delete")
  public String delete(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttribute) {
    // 编码
    String dataroleid = (String) request.getParameter("id");
    List<UserInfo> list=uiService.getUserByDataRoleId(dataroleid);
    if(list.size()==0) {
      dataRoleService.delDataroleById(dataroleid);
      addMessage(redirectAttribute, "删除成功");
    }else{
      addMessage(redirectAttribute, "删除失败，无法删除用户绑定的角色");
    }
    return "redirect:" + "/dataRole/list";
  }

  @RequestMapping(value = "/checkNameOrId")
  @ResponseBody
  public String checkNameOrId(HttpServletRequest request, HttpServletResponse response) {
    // 编码
    String dataroleid = request.getParameter("id");
    // 名称
    String datarolename = request.getParameter("name");
    Datarole datarole = dataRoleService.getdataroleByIdOrName(dataroleid, datarolename);
    if (datarole != null) {
      return "1";
    } else {
      return "0";
    }
  }
}
