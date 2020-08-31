/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.musicuser.web;

import com.thinkgem.jeesite.modules.Users.entity.OrgInfo;
import com.thinkgem.jeesite.modules.Users.service.OrgInfoService;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.common.web.BaseController;
import com.thinkgem.jeesite.mybatis.modules.musicuser.entity.MusicInfo;
import com.thinkgem.jeesite.mybatis.modules.musicuser.service.MusicInfoService;
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
import java.util.Iterator;
import java.util.List;

/**
 * music用户维护Controller
 *
 * @author yang.kq
 * @version 2020-01-20
 */
@Controller
@RequestMapping(value = "/musicuser/musicInfo")
public class MusicInfoController extends BaseController {

  @Autowired private MusicInfoService musicInfoService;
  @Autowired private OrgInfoService oiService;

  @ModelAttribute
  public MusicInfo get(@RequestParam(required = false) String id) {
    MusicInfo entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = musicInfoService.get(id);
    }
    if (entity == null) {
      entity = new MusicInfo();
    }
    return entity;
  }

  @RequestMapping(value = {"/list", ""})
  public String list(
      MusicInfo musicInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<MusicInfo> page =
        musicInfoService.findPage(new Page<MusicInfo>(request, response), musicInfo);
    model.addAttribute("page", page);
    return "modules/musicuser/musicInfoList";
  }

  @RequestMapping(value = "/form")
  public String form(MusicInfo musicInfo, Model model) {
    List<OrgInfo> oneList = oiService.getOrgListBypId("0");
    List<MusicInfo> list = musicInfoService.findList(musicInfo);
    List<String> orgExist = new ArrayList<String>();
    for (MusicInfo mi : list) {
      orgExist.add(mi.getOrgid());
    }
    String org1 = musicInfo.getOrgid();
    Iterator<OrgInfo> it = oneList.iterator();
    while (it.hasNext()) {
      OrgInfo org = (OrgInfo) it.next();
      String orgid = org.getId();
      if (orgExist.contains(orgid) && (!orgid.equals(org1))) {
        it.remove();
      }
    }
    model.addAttribute("musicInfo", musicInfo);
    model.addAttribute("oneList", oneList);
    return "modules/musicuser/musicInfoForm";
  }

  @RequestMapping(value = "/view")
  public String view(MusicInfo musicInfo, Model model) {
    List<OrgInfo> oneList = oiService.getOrgListBypId("0");
    Iterator<OrgInfo> it = oneList.iterator();
    String org1=musicInfo.getOrgid();
    while (it.hasNext()) {
      OrgInfo org = (OrgInfo) it.next();
      String orgid = org.getId();
      if (!orgid.equals(org1)) {
        it.remove();
      }
    }
    model.addAttribute("musicInfo", musicInfo);
    model.addAttribute("orgname", oneList.get(0).getName());
    return "modules/musicuser/musicInfoView";
  }

  @RequestMapping(value = "/save")
  public String save(MusicInfo musicInfo, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, musicInfo)) {
      return form(musicInfo, model);
    }
    musicInfoService.save(musicInfo);
    addMessage(redirectAttributes, "保存用户成功");
    return "redirect:/musicuser/musicInfo/list";
  }

  @RequestMapping(value = "/delete")
  public String delete(MusicInfo musicInfo, RedirectAttributes redirectAttributes) {
    musicInfoService.delete(musicInfo);
    addMessage(redirectAttributes, "删除用户成功");
    return "redirect:/musicuser/musicInfo/list";
  }

  @RequestMapping(value = "/checkUserName")
  @ResponseBody
  public boolean checkUserName(String name, String orgid) {
    boolean flag = musicInfoService.checkUserName(name);
    return flag;
  }
}
