package com.thinkgem.jeesite.modules.index.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.index.entity.PortalMenu;
import com.thinkgem.jeesite.modules.index.entity.PortalMenuDef;
import com.thinkgem.jeesite.modules.index.service.PortalMenuDefService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/portalMenuDef")
public class PortalMenuDefController extends BaseController {
  @Autowired private PortalMenuDefService portalMenuDefService;
  @Autowired private CacheCleanController cacheCleanController;
  @Autowired private ComparasService comparasService;

  @RequestMapping(value = "/list")
  public String portalMenuDefList(
      PortalMenuDef portalMenuDef,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    Comparas comparas = comparasService.getComparas("imgServerUrl");
    List<PortalMenuDef> list = new ArrayList<PortalMenuDef>();
    List<PortalMenuDef> pmds = portalMenuDefService.findAll();
    PortalMenuDef.sortList(list, pmds, 0);
    model.addAttribute("imgServiceUrl", comparas.getStringvalue());
    model.addAttribute("pmds", list);
    model.addAttribute("portalMenuDef", portalMenuDef);
    return "modules/index/portalMenuDefList";
  }

  /**
   * 跳转到添加子类型页面
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/toAdd")
  public String toAddPortalMenuDef(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    Integer pid = Integer.valueOf(request.getParameter("pid"));
    Integer menuid = Integer.valueOf(request.getParameter("menuid"));
    PortalMenuDef parent = null;
    String pname = "";
    if (pid != null && !"".equals(pid)) {
      parent = portalMenuDefService.findPortalMenuDefById(menuid);
      if (parent != null) {
        pname = parent.getChnname();
      }
    }
    model.addAttribute("menuid", menuid);
    model.addAttribute("pid", pid);
    model.addAttribute("pname", pname);

    return "modules/index/portalMenuDefAdd";
  }

  /**
   * 添加子类型
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/save")
  public String savePortalMenuDef(
      PortalMenuDef portalMenuDef,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      RedirectAttributes redirectAttributes) {
    PortalMenu pm = new PortalMenu();

    String parentId = request.getParameter("treeNodeId");
    // menuid  新增的话 直接当前最大menuid加1

    if (parentId != null && !"".equals(parentId)) { // 选择树节点
      pm.setParentid(Integer.valueOf(parentId));
      // menuid
      Integer menuid = portalMenuDefService.getMaxMid(Integer.valueOf(parentId));
      pm.setMenuid(menuid + 1);
      // orderno
      Integer orderno = portalMenuDefService.getMaxOrderno(Integer.valueOf(parentId));
      pm.setOrderno(orderno + 1);
      // showType字段
      if ("0".equals(parentId)) {
        pm.setShowtype(0);
      } else {
        pm.setShowtype(1);
      }
    } else { // 未选择树节点
      pm.setParentid(Integer.valueOf(request.getParameter("menuid")));
      // menuid
      Integer menuid =
          portalMenuDefService.getMaxMid(Integer.valueOf(request.getParameter("menuid")));
      pm.setMenuid(menuid + 1);
      // orderno
      Integer orderno =
          portalMenuDefService.getMaxOrderno(Integer.valueOf(request.getParameter("menuid")));
      pm.setOrderno(orderno + 1);
      // showType字段
      if ("0".equals(request.getParameter("menuid"))) {
        pm.setShowtype(0);
      } else {
        pm.setShowtype(1);
      }
    }
    pm.setLayer(1); // layer值全为1
    String chnname = request.getParameter("chnname");
    String linkurl = request.getParameter("linkurl");
    //		Integer orderno=Integer.valueOf(request.getParameter("orderno"));
    String menucode = request.getParameter("menucode");
    pm.setChnname(chnname);
    pm.setLinkurl(linkurl);
    //		pm.setOrderno(orderno);
    pm.setInvalid(0);
    pm.setMenucode(menucode);
    try {
      portalMenuDefService.savePm(pm);
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存成功");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "保存失败");
      e1.printStackTrace();
    }
    return "redirect:" + "/portalMenuDef/list";
  }

  /**
   * 获得类型树
   *
   * @param extId
   * @param paramMap
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/getTreeList")
  @ResponseBody
  public List<Map<String, Object>> getTreeList(
      @RequestParam(required = false) Long extId,
      Map<String, Object> paramMap,
      HttpServletRequest request,
      HttpServletResponse response) {
    response.setContentType("application/json; charset=UTF-8");
    List<Map<String, Object>> mapList = Lists.newArrayList();
    List<PortalMenuDef> pmds = portalMenuDefService.findAll();
    for (PortalMenuDef portalMenuDef : pmds) {
      Map<String, Object> map = Maps.newHashMap();
      map.put("id", portalMenuDef.getMenuid());
      map.put("pId", portalMenuDef.getParent().getMenuid());
      map.put("name", portalMenuDef.getChnname());
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 跳转至编辑类型界面
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/toEdit")
  public String toEditPortalMenuDef(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    Integer menuid = Integer.valueOf(request.getParameter("menuid"));
    Integer pid = Integer.valueOf(request.getParameter("pid"));

    PortalMenuDef portalMenuDef = portalMenuDefService.findPortalMenuDefById(menuid);
    String pname = "";
    if (pid != 0) {
      PortalMenuDef parent = portalMenuDef.getParent();
      pname = parent.getChnname();
    }

    model.addAttribute("menuid", menuid);
    model.addAttribute("pid", pid);
    model.addAttribute("pname", pname);
    model.addAttribute("portalMenuDef", portalMenuDef);
    return "modules/index/portalMenuDefEdit";
  }

  /**
   * 编辑类型界面
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/edit")
  public String editPortalMenuDef(
      PortalMenuDef portalMenuDef,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      RedirectAttributes redirectAttributes) {
    try {
      PortalMenu pm = new PortalMenu();

      String parentId = request.getParameter("treeNodeId"); // 是否选择树节点
      if (parentId == null || "".equals(parentId)) { // 不选择
        pm.setParentid(Integer.valueOf(request.getParameter("parent.menuid")));
        if ("0".equals(request.getParameter("parent.menuid"))) {
          pm.setShowtype(0);
        } else {
          pm.setShowtype(1);
        }
      } else { // 选择
        pm.setParentid(Integer.valueOf(parentId));
        if ("0".equals(parentId)) { // showType字段
          pm.setShowtype(0);
        } else {
          pm.setShowtype(1);
        }
      }
      pm.setMenuid(portalMenuDef.getMenuid()); // menuid
      pm.setLayer(1); // layer全是1
      String chnname = request.getParameter("chnname");
      String linkurl = request.getParameter("linkurl");
      //   	Integer orderno=Integer.valueOf(request.getParameter("orderno"));
      String menucode = request.getParameter("menucode");

      pm.setChnname(chnname);
      pm.setLinkurl(linkurl);
      //   	pm.setOrderno(orderno);
      pm.setMenucode(menucode);
      pm.setInvalid(portalMenuDef.getInvalid());
      pm.setOrderno(portalMenuDef.getOrderno());

      portalMenuDefService.savePm(pm);
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "修改成功");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "修改失败");
      e1.printStackTrace();
    }
    return "redirect:" + "/portalMenuDef/list";
  }

  /**
   * 查看详情
   *
   * @return
   */
  @RequestMapping("/getDetail")
  public String getDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
    Integer menuid = Integer.valueOf(request.getParameter("menuid"));
    PortalMenuDef portalMenuDef = portalMenuDefService.findPortalMenuDefById(menuid);
    model.addAttribute("menuid", menuid);
    model.addAttribute("portalMenuDef", portalMenuDef);
    return "modules/index/portalMenuDefDetail";
  }

  @RequestMapping("/delete")
  public String deletePortalMenuDef(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      Model model) {
    Integer menuid = Integer.valueOf(request.getParameter("menuid"));
    Integer pid = Integer.valueOf(request.getParameter("pid"));

    try {
      if ("0".equals(pid)) {
        portalMenuDefService.deletePortalDataById(menuid);
        portalMenuDefService.deletePortalDataByPid(menuid);
      } else {
        portalMenuDefService.deletePortalDataById(menuid);
      }
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "删除成功");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "删除失败");
      e1.printStackTrace();
    }
    return "redirect:" + "/portalMenuDef/list";
  }
  /** 批量修改菜单排序 */
  @RequestMapping(value = "updateSort")
  public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    int len = ids.length;
    PortalMenuDef[] portalMenuDef = new PortalMenuDef[len];
    try {
      for (int i = 0; i < len; i++) {
        portalMenuDef[i] = portalMenuDefService.findPortalMenuDefById(Integer.valueOf(ids[i]));
        portalMenuDef[i].setOrderno(sorts[i]);
        portalMenuDefService.savePm2(portalMenuDef[i]);
      }
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存菜单排序成功!");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "保存菜单排序失败!");
      e1.printStackTrace();
    }
    return "redirect:" + "/portalMenuDef/list";
  }
}
