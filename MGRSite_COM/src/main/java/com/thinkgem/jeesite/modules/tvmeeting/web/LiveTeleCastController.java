/*
 * @(#)LiveTeleCastController.java 2016年1月27日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.RandomGUID;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.tvmeeting.entity.DocDef;
import com.thinkgem.jeesite.modules.tvmeeting.entity.PlanDefine;
import com.thinkgem.jeesite.modules.tvmeeting.service.DocDefService;
import com.thinkgem.jeesite.modules.tvmeeting.service.LiveTeleCastService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年1月27日
 */
@Controller
@RequestMapping(value = "tvmeeting/meetingarrange")
public class LiveTeleCastController extends BaseController {
  @Autowired private LiveTeleCastService lcService;
  @Resource private ComparasDao comparasDao;
  @Autowired private DocDefService docDefService;

  @RequestMapping("/teleList")
  public String TeleList(
      @RequestParam Map<String, Object> paramMap,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      Model model) {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
    String begin = (String) paramMap.get("begin");

    try {
      if (begin != null && !"".equals(begin)) {
        model.addAttribute("begin", begin);
        begin = sf1.format(sf.parse(begin));
      } else {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        begin = sf1.format(c.getTime());
      }
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
    String end = (String) paramMap.get("end");
    try {
      if (end != null && !"".equals(end)) {
        model.addAttribute("end", end);
        end = sf1.format(sf.parse(end));
      } else {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        end = sf1.format(c.getTime());
      }
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
    Page<PlanDefine> page =
        lcService.findByPage(new Page<PlanDefine>(request, response), begin, end);
    List<PlanDefine> list = page.getList();
    String beginTime = "";
    String endTime = "";
    Date date = null;
    for (PlanDefine pd : list) {
      try {
        date = sf1.parse(pd.getMeetingDate());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      pd.setMeetingDate(sf.format(date));
      beginTime = pd.getBeginTime().substring(0, 2) + ":" + pd.getBeginTime().substring(2, 4);
      endTime = pd.getEndTime().substring(0, 2) + ":" + pd.getEndTime().substring(2, 4);
      pd.setBeginTime(beginTime);
      pd.setEndTime(endTime);
    }
    page.setList(list);
    try {
      model.addAttribute("begin", sf.format(sf1.parse(begin)));
      model.addAttribute("end", sf.format(sf1.parse(end)));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    model.addAttribute("page", page);
    return "modules/tvmeeting/TeleList";
  }

  @RequiresPermissions("tvmeeting:meeting:edit")
  @RequestMapping("/teleAdd")
  public String TeleAdd(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      PlanDefine planDefine) {
    int maxUploadSize = Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
    model.addAttribute("maxUploadSize", maxUploadSize / 1024 / 1024);
    model.addAttribute("planDefine", planDefine);
    return "modules/tvmeeting/TeleAdd";
  }

  @RequestMapping("/saveAttach")
  public void saveAttach(
      String id, HttpServletRequest request, HttpServletResponse response, Model model) {
    String imgServerPath = (String) comparasDao.getComparasByKey("docServerPath");
    String docDefUrl = (String) comparasDao.getComparasByKey("meetingPath");
    //		String imgServerPath = "D://";
    //		String docDefUrl = "test/";
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    Map<String, MultipartFile> fliemap = multipartRequest.getFileMap();
    CommonsMultipartFile file = (CommonsMultipartFile) fliemap.get("file");
    long l = file.getSize();
    int maxUploadSize = Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
    Map<String, Object> map = new HashMap<String, Object>();
    DocDef docDef = new DocDef();
    List<DocDef> list = new ArrayList<DocDef>();
    if (l > maxUploadSize) {
      map.put("id", id);
      map.put("message", "文件大小超过" + maxUploadSize / 1024 / 1024 + "M，请联系管理员！");
    } else {
      String docId = UUID.randomUUID().toString(); // 将文件名改成用guid表示
      String OriginalFilename = file.getOriginalFilename();
      this.logger.info("OriginalFilename:" + OriginalFilename);
      int index = OriginalFilename.lastIndexOf(".");
      String fileName = OriginalFilename.substring(0, index);
      String last = OriginalFilename.substring(index + 1, OriginalFilename.length());
      String path = imgServerPath + docDefUrl + docId + "." + last; // 拼成上传路径
      String localpath = "/" + docDefUrl + docId + "." + last;
      boolean flag = false;
      if (!"".equals(docDefUrl) && null != docDefUrl) {
        // 上传文件不存在则创建文件夹
        File dir = new File(docDefUrl);
        if (!dir.exists()) {
          dir.mkdir();
        }
        OutputStream os;
        try {
          os = new FileOutputStream(path);
          InputStream is = file.getInputStream();
          int bytesRead = 0;
          byte[] buffer = new byte[8192];
          while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead); // 将文件写入服务器
          }
          os.close();
          is.close();
          flag = true;
        } catch (FileNotFoundException e) {
          flag = false;
          this.logger.error("路径错误" + e.getMessage());
          e.printStackTrace();
        } catch (IOException e) {
          flag = false;
          this.logger.error("上传失败" + e.getMessage());
          e.printStackTrace();
        }
      }
      docDef.setId(UUID.randomUUID().toString());
      if ("".equals(id) || null == id) {
        id = UUID.randomUUID().toString();
      }
      docDef.setDocId(id);
      docDef.setDocName(fileName);
      docDef.setDocUrl(localpath);
      docDef.setInvalid(0);
      list = docDefService.findDocDefList(id);
      int num = list.size();
      docDef.setOrderNo(num + 1);
      Date d = new Date(System.currentTimeMillis());
      String ip = UserUtils.getUser().getLoginIp();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String uploadDate = sdf.format(d);
      this.logger.info("uploadDate:" + uploadDate);
      docDef.setCreated(d);
      docDef.setCreatedBy(ip);
      docDef.setUploadDate(uploadDate);
      if (flag) {
        docDefService.save(docDef);
        list = docDefService.findDocDefList(id);
        map.put("list", list);
        map.put("id", id);
        map.put("message", "上传成功");
      } else {
        list.clear();
        map.put("list", list);
        map.put("id", id);
        map.put("message", "上传失败");
      }
    }

    String json = JsonMapper.toJsonString(map);
    renderText(json, response);
  }
  /**
   * 删除附件
   *
   * @param request
   * @param response
   * @param redirectAttributes
   */
  @RequestMapping("/delById")
  public void delAttach(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    String Id = request.getParameter("ID");
    String docUrl = request.getParameter("docUrl");
    String imgServerPath = (String) comparasDao.getComparasByKey("docServerPath");
    boolean flag = false;
    File file = new File(imgServerPath + "/" + docUrl);
    try {
      file.delete();
      flag = true;
    } catch (Exception e) {
      flag = false;
      e.printStackTrace();
    }
    Map<String, Object> m = new HashMap<String, Object>();
    if (flag) {
      try {
        docDefService.deleteById(Id);
        flag = true;
        if (flag) {
          m.put("ID", Id);
          m.put("flag", "true");
        } else {
          m.put("ID", Id);
          m.put("flag", "false");
        }

      } catch (Exception e) {
        flag = false;
        e.printStackTrace();
      }
    } else {
      m.put("ID", Id);
      m.put("flag", "false");
    }
    String json = JsonMapper.toJsonString(m);
    renderText(json, response);
  }

  @RequestMapping("/teleSave")
  public String TeleAdd(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      PlanDefine planDefine)
      throws ParseException {
    String MeetingDate = request.getParameter("MeetingDate");
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
    Date date = null;
    try {
      date = sf.parse(MeetingDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    MeetingDate = sf1.format(date);
    System.out.println("MeetingDate:" + MeetingDate);
    String BeginTime = request.getParameter("BeginTime");
    BeginTime = BeginTime.replace(":", "");
    String EndTime = request.getParameter("EndTime");
    EndTime = EndTime.replace(":", "");
    Integer b = Integer.parseInt(BeginTime);
    System.out.println(b);
    Integer e = Integer.parseInt(EndTime);
    System.out.println(e);
    Date d = new Date(System.currentTimeMillis());
    String[] MeetRanges = request.getParameterValues("MeetRange");
    StringBuffer sb = new StringBuffer();
    List<PlanDefine> exitList = lcService.findBeginTime(MeetingDate);
    System.out.println("exitList:" + exitList);
    for (int i = 0; i < MeetRanges.length; i++) {
      sb.append(MeetRanges[i]).append(",");
    }
    String userName = UserUtils.getUser().getLoginIp();
    if (planDefine.getMeetingId() == null || "".equals(planDefine.getMeetingId())) {
      planDefine.setMeetingId(new RandomGUID().toString());
      planDefine.setMeetRange(sb.toString().substring(0, sb.toString().length() - 1));
      planDefine.setMeetingDate(MeetingDate);
      planDefine.setBeginTime(BeginTime);
      planDefine.setEndTime(EndTime);
      planDefine.setCreated(d);
      planDefine.setCreatedBy(userName);
    }
    // List<PlanDefine> exitList=lcService.findBeginTime(MeetingDate);
    //  System.out.println("exitList:"+exitList);
    Boolean flag = false;
    for (PlanDefine p : exitList) {
      System.out.println("p:" + p);
      if (Integer.parseInt(p.getEndTime()) < b || Integer.parseInt(p.getBeginTime()) > e) {

      } else {
        flag = true;
      }
    }
    if (flag == false) {
      lcService.saveTele(planDefine);
      addMessage(redirectAttributes, "保存成功");
      return "redirect:" + "/tvmeeting/meetingarrange/teleList";
    } else {
      addMessage(redirectAttributes, "保存失败:以上时段已存在会商信息");
      return "redirect:" + "/tvmeeting/meetingarrange/teleAdd";
    }
  }

  @RequiresPermissions("tvmeeting:meeting:edit")
  @RequestMapping("/updateSave")
  public String TeleupdateSave(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      Model model,
      PlanDefine planDefine) {

    String MeetingId = planDefine.getMeetingId();
    PlanDefine pd = lcService.getTelById(MeetingId);

    String MeetingDate = request.getParameter("MeetingDate");
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
    Date date = null;
    try {
      date = sf.parse(MeetingDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    MeetingDate = sf1.format(date);
    String BeginTime = request.getParameter("BeginTime");
    BeginTime = BeginTime.replace(":", "");
    String EndTime = request.getParameter("EndTime");
    EndTime = EndTime.replace(":", "");
    Date d = new Date(System.currentTimeMillis());
    String UpdatedBy = UserUtils.getUser().getLoginIp();
    pd.setMeetingDate(MeetingDate);
    pd.setBeginTime(BeginTime);
    pd.setEndTime(EndTime);
    pd.setMeetTitle(planDefine.getMeetTitle());
    pd.setContent(planDefine.getContent());
    pd.setContacts(planDefine.getContacts());
    pd.setContactWay(planDefine.getContactWay());
    pd.setMeetArea(planDefine.getMeetArea());
    pd.setMeetLevel(planDefine.getMeetLevel());
    pd.setMeetTheme(planDefine.getMeetTheme());
    pd.setMeetType(planDefine.getMeetType());
    pd.setIsDouble(planDefine.getIsDouble());
    pd.setOrganizationUni(planDefine.getOrganizationUni());
    String[] MeetRanges = request.getParameterValues("MeetRange");
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < MeetRanges.length; i++) {
      sb.append(MeetRanges[i]).append(",");
    }
    pd.setMeetRange(sb.toString().substring(0, sb.toString().length() - 1));
    pd.setUpdated(d);
    pd.setUpdatedBy(UpdatedBy);
    lcService.saveTele(pd);
    addMessage(redirectAttributes, "保存成功");
    return "redirect:" + "/tvmeeting/meetingarrange/teleList";
  }

  @RequiresPermissions("tvmeeting:meeting:edit")
  @RequestMapping("/teleDel")
  public String TeleDelete(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      Model model) {
    String MeetingId = request.getParameter("MeetingId");
    String begin = request.getParameter("begin");
    String end = request.getParameter("end");
    lcService.delTele(MeetingId);

    addMessage(redirectAttributes, "删除成功");
    // return "redirect:"+"/tvmeeting/meetingarrange/teleList";
    return "redirect:"
        + "/tvmeeting/meetingarrange/teleList?begin="
        + begin
        + "&"
        + "end="
        + end
        + "";
  }

  @RequestMapping("/teleUpdate")
  public String TeleUpdate(HttpServletRequest request, HttpServletResponse response, Model model) {
    int maxUploadSize = Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
    this.logger.info("maxUploadSize:" + maxUploadSize);
    model.addAttribute("maxUploadSize", maxUploadSize / 1024 / 1024);
    String MeetingId = request.getParameter("MeetingId");
    PlanDefine pd = lcService.getTelById(MeetingId);
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
    Date date = null;
    try {
      date = sf1.parse(pd.getMeetingDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    pd.setMeetingDate(sf.format(date));
    List<DocDef> list = new ArrayList<DocDef>();
    list = docDefService.findDocDefList(MeetingId);
    String beginTime = pd.getBeginTime().substring(0, 2) + ":" + pd.getBeginTime().substring(2, 4);
    String endTime = pd.getEndTime().substring(0, 2) + ":" + pd.getEndTime().substring(2, 4);
    pd.setBeginTime(beginTime);
    pd.setEndTime(endTime);
    model.addAttribute("tele", pd);
    model.addAttribute("chk_value", pd.getMeetRange());
    model.addAttribute("list", list);
    return "modules/tvmeeting/TeleEdit";
  }

  @RequestMapping("/telebyId")
  public void TelebyCondition(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    String MeetingDate = request.getParameter("MeetingDate");
    String BeginTime = request.getParameter("BeginTime");
    MeetingDate = MeetingDate.replace("-", "");
    BeginTime = BeginTime.replace(":", "");
    String MeetingId = MeetingDate + BeginTime;
    boolean flag = false;
    List<PlanDefine> list = lcService.selectByCondition(MeetingId);
    if (list.size() == 0) {
      flag = true;
    }
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("flag", flag);
    String json = JsonMapper.toJsonString(m);
    renderText(json, response);
  }

  @RequiresPermissions("tvmeeting:meeting:edit")
  @RequestMapping("/teleBatchAdd")
  public String teleBatchAdd(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      Model model,
      PlanDefine planDefine) {

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    String nowDay = sf.format(c.getTime());
    c.add(Calendar.DAY_OF_MONTH, 30);
    String afterMonth = sf.format(c.getTime());
    model.addAttribute("nowDay", nowDay);
    model.addAttribute("afterMonth", afterMonth);
    model.addAttribute("beginTime", "08:00");
    model.addAttribute("endTime", "08:30");
    model.addAttribute("Content", "中央台早间会商");
    model.addAttribute("planDefine", planDefine);
    return "modules/tvmeeting/TeleBatchAdd";
  }

  @RequestMapping("/teleBatchAddSave")
  public String teleBatchAddSave(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes,
      Model model) {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
    String BeginDate = request.getParameter("BeginDate");
    String endDate = request.getParameter("endDate");
    long i = 0;
    try {
      i = (sf.parse(endDate).getTime() - sf.parse(BeginDate).getTime()) / (1000 * 3600 * 24);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    String BeginTime = request.getParameter("BeginTime");
    BeginTime = BeginTime.replace(":", "");
    String EndTime = request.getParameter("EndTime");
    EndTime = EndTime.replace(":", "");
    String Content = request.getParameter("Content");
    String MeetTitle = request.getParameter("MeetTitle");
    // this.logger.info("MeetTitle:"+MeetTitle);
    String Contacts = request.getParameter("Contacts");
    // this.logger.info("Contacts:"+Contacts);
    String MeetLevel = request.getParameter("MeetLevel");
    String MeetType = request.getParameter("MeetType");
    String MeetTheme = request.getParameter("MeetTheme");
    String MeetArea = request.getParameter("MeetArea");
    String ContactWay = request.getParameter("ContactWay");
    String[] MeetRanges = request.getParameterValues("MeetRange");
    StringBuffer sb = new StringBuffer();
    for (int j = 0; j < MeetRanges.length; j++) {
      sb.append(MeetRanges[j]).append(",");
    }
    String IsDouble = request.getParameter("IsDouble");

    // this.logger.info("ContactWay:"+ContactWay);
    String OrganizationUni = request.getParameter("OrganizationUni");
    // this.logger.info("OrganizationUni:"+OrganizationUni);
    String[] dates = new String[Integer.parseInt(String.valueOf(i)) + 1];
    String day = "";
    List<PlanDefine> pList = new ArrayList<PlanDefine>();
    PlanDefine pd;
    // String MeetingId="";
    String userName = UserUtils.getUser().getLoginIp();
    for (int j = 0; j < i + 1; j++) {
      pd = new PlanDefine();
      Calendar c = Calendar.getInstance();
      try {
        c.setTime(sf.parse(endDate));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      c.add(Calendar.DAY_OF_MONTH, -j);
      day = sf1.format(c.getTime());
      // MeetingId=day+BeginTime;
      pd.setMeetingId(new RandomGUID().toString());
      pd.setMeetingDate(day);
      pd.setBeginTime(BeginTime);
      pd.setEndTime(EndTime);
      pd.setContent(Content);
      pd.setContactWay(ContactWay);
      pd.setMeetTitle(MeetTitle);
      pd.setContacts(Contacts);
      pd.setOrganizationUni(OrganizationUni);
      pd.setIsDouble(Integer.parseInt(IsDouble));
      pd.setMeetArea(Integer.parseInt(MeetArea));
      pd.setMeetLevel(Integer.parseInt(MeetLevel));
      pd.setMeetRange(sb.toString().substring(0, sb.toString().length() - 1));
      pd.setMeetTheme(Integer.parseInt(MeetTheme));
      pd.setMeetType(Integer.parseInt(MeetType));
      pd.setCreated(new Date(System.currentTimeMillis()));
      pd.setCreatedBy(userName);
      dates[j] = day;
      pList.add(pd);
    }
    String str = "";
    for (int k = 0; k < (Integer.parseInt(String.valueOf(i)) + 1); k++) {
      if (k < (Integer.parseInt(String.valueOf(i)))) {
        str += "'" + dates[k] + "',";
      } else {
        str += "'" + dates[k] + "'";
      }
    }
    List<PlanDefine> exitList = lcService.getTeleByDateandTime(str, BeginTime, EndTime);
    if (exitList.size() > 0) {
      String datestr = "";
      int l = 1;
      for (PlanDefine p : exitList) {
        try {
          if (l < exitList.size()) {
            datestr += "'" + sf.format(sf1.parse(p.getMeetingDate())) + "',";
          } else {
            datestr += "'" + sf.format(sf1.parse(p.getMeetingDate())) + "'";
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
        l++;
      }
      addMessage(redirectAttributes, "保存失败:(" + datestr + ")以上日期已存在会商信息");
      return "redirect:" + "/tvmeeting/meetingarrange/teleBatchAdd";
    } else {
      lcService.saveBatchTele(pList);
      // for(PlanDefine plan:pList){
      //	lcService.saveTele(plan);
    }
    addMessage(redirectAttributes, "批量保存成功");
    return "redirect:" + "/tvmeeting/meetingarrange/teleList";
  }
}
