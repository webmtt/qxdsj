package com.thinkgem.jeesite.modules.data.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.data.entity.*;
import com.thinkgem.jeesite.modules.data.service.*;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import com.thinkgem.jeesite.modules.sys.web.CacheCleanController;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/dataService")
public class DataServiceController extends BaseController {
  @Autowired private DataCategoryDefService dataCategoryDefService;
  @Autowired private CategoryDataReltService categoryDataReltService;
  @Autowired private DataDefService dataDefService;
  @Autowired private DataFtpDefService dataFtpDefService;
  @Autowired private DataLinksService dataLinksService;
  @Autowired private DataReferDefService dataReferDefService;
  @Autowired private DataClassDicService dataClassDicService;
  @Autowired private ComparasService comparasService;
  @Autowired private CacheCleanController cacheCleanController;

  /**
   * 获取数据服务列表并在展示页面
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/list")
  public String dataServiceList(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    Comparas comparas = comparasService.getComparas("imgServerUrl");
    List<DataCategoryDef> list = dataCategoryDefService.findAll();
    model.addAttribute("dataCategoryDefs", list);
    model.addAttribute("imgServiceUrl", comparas.getStringvalue());
    return "modules/data/dataServiceList";
  }

  /**
   * 排序功能-大小类排序
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sortCategory")
  public String sortCategory(
      String pid,
      String id,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    // 小类的排序
    Comparas comparas = comparasService.getComparas("imgServerUrl");
    model.addAttribute("imgServiceUrl", comparas.getStringvalue());
    if (pid != null && !"".equals(pid)) {
      List<DataCategory> dataCategoryDefs =
          dataCategoryDefService.getDataCategoryList(Integer.valueOf(id));
      model.addAttribute("list", dataCategoryDefs);
      return "modules/data/dataSortList";
    } else { // 大类的排序
      List<DataCategory> dataCategoryDefs = dataCategoryDefService.getDataCategoryList(0);
      model.addAttribute("list", dataCategoryDefs);
      return "modules/data/dataSortList";
    }
  }

  /**
   * 排序功能-数据列表排序
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sortDataDef")
  public String sortDataDef(
      String categoryid,
      String pid,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    List<Object[]> list = categoryDataReltService.findCategoryListSort(categoryid);
    DataCategory dataCategoryDef =
        dataCategoryDefService.getDataCategoryById(Integer.valueOf(categoryid));
    model.addAttribute("dataCategoryDef", dataCategoryDef);
    model.addAttribute("categoryid", categoryid);
    model.addAttribute("pid", pid);
    model.addAttribute("list", list);
    return "modules/data/dataDefSortList";
  }

  @RequestMapping(value = "/showPage")
  public String showPage(HttpServletRequest request, HttpServletResponse response, Model model) {
    String url = "/mgrsite/static/images/sjfw2.jpg";
    model.addAttribute("url", url);
    return "modules/data/dataServicePage";
  }

  @RequestMapping(value = "/showPage1")
  public String showPage1(HttpServletRequest request, HttpServletResponse response, Model model) {
    String url = "/mgrsite/static/images/sjfw1.jpg";
    model.addAttribute("url", url);
    return "modules/data/dataServicePage";
  }

  @RequestMapping(value = "/showPage3")
  public String showPage3(HttpServletRequest request, HttpServletResponse response, Model model) {
    String url = "/mgrsite/static/images/sjfw3.jpg";
    model.addAttribute("url", url);
    return "modules/data/dataServicePage";
  }

  /**
   * 获取数据服务列表
   *
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/dataDefList")
  public String dataDefList(
      DataDef dataDef,
      String chnname,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    Page<DataDef> page =
        dataDefService.getSearchResultByKey(chnname, new Page<DataDef>(request, response));
    model.addAttribute("page", page);
    model.addAttribute("dataDef", dataDef);
    model.addAttribute("chnname", chnname);
    return "modules/data/dataDefList";
  }

  /**
   * 查看类型下的资料、链接引用文献信息,Ftp下载
   *
   * @return
   */
  @RequestMapping("/getDataDetail")
  public String getDataDetail(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    String categoryid = request.getParameter("categoryid"); // 获得类型id
    String pid = request.getParameter("pid"); // 类型中文名
    String pageNo = request.getParameter("pageNo");
    String pageSize = request.getParameter("pageSize");
    String dataDefName = request.getParameter("dataDefName");
    if (pageNo == null || "".equals(pageNo)) {
      pageNo = "1";
    }
    if (pageSize == null || "".equals(pageSize)) {
      pageSize = "10";
    }
    if (dataDefName != null && !"".equals(dataDefName)) {
      try {
        dataDefName = new String(dataDefName.getBytes("UTF-8"), "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    if(pid==null||"".equals(pid)){
      pid="0";
    }
//    // pid不为空的情况，直接传值挑战
//    if (pid != null && !"".equals(pid)) {

      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList(0);
      model.addAttribute("plist", plist);
      // 小类
//      if ("17".equals(pid)) {
//        categoryid = "17";
//      }
//      List<DataCategory> clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
//      model.addAttribute("clist", clist);
      // 根据类型id在关联表中查到对应资料Id
      List<Object[]> dataDefList = new ArrayList<Object[]>();
      // 得到资料列表
      List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
      // List<DataLinks> linkList=new ArrayList<DataLinks>();
      // List<DataReferDef> referList=new ArrayList<DataReferDef>();
      List<DataFtpDef> dataftpList = new ArrayList<DataFtpDef>();
      Page dataDefPage = new Page();
      if(categoryid == null ||"".equals(categoryid)){
        categoryid="1";
      }
      if (categoryid != null && !"".equals(categoryid)) {
        dataDefPage =
            dataDefService.findDataDefByCodes(
                categoryid,
                dataDefName,
                new Page<DataDef>(Integer.parseInt(pageNo), Integer.parseInt(pageSize)));

        dataDefList = dataDefPage.getList();

        for (Object[] dd : dataDefList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("dataCode", dd[0]);
          map.put("chnName", dd[1]);
          map.put("chnDecription", dd[2]);
//          /*
//          //得到相关链接
//          linkList=dataLinksService.findDataLinksByDataCode(dd[0].toString());
//          map.put("linkList", linkList);
//          //得到相关文献
//          referList=dataReferDefService.findDataReferDefByDataCode(dd[0].toString());
//          map.put("referList", referList);
//          */
          map.put("serviceMode", dd[3]);
          map.put("orderno", dd[4]);
          map.put("invalid", dd[5]);
//          //          // 得到Ftp下载信息
//          //          if (dd[3] != null && Integer.valueOf(dd[3].toString()) == 4) {
//          //            dataftpList = dataFtpDefService.findDataFtpDefByCode(dd[0].toString());
//          //            if (dataftpList != null && dataftpList.size() > 0) {
//          //              map.put("dataftp", dataftpList.get(0));
//          //            }
//          //          }
//          /*
//          if(linkList.size()>referList.size()){
//          	map.put("colspan", linkList.size());
//          }else if(linkList.size()<referList.size()){
//          	map.put("colspan", referList.size());
//          }else{
//          	map.put("colspan", 1);
//          }
//          */
          resultList.add(map);
        }
      }
      DataCategory dc = dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
      model.addAttribute("page", dataDefPage);
      model.addAttribute("CategoryDataRelts", resultList);
      model.addAttribute("CategoryName", dc.getChnname());
      model.addAttribute("pid", pid);
      model.addAttribute("categoryid", categoryid);
      model.addAttribute("dataDefName", dataDefName);
      return "modules/data/dataReferList";
//    } else { // 没有传值，本页面默认值
//      // 大类
//      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList(0);
//      pid = String.valueOf(plist.get(0).getCategoryid());
//      model.addAttribute("plist", plist);
//      // 小类
//      List<DataCategory> clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
//      if ("17".equals(pid)) {
//        categoryid = "17";
//      } else {
//        if (clist.size() == 0) {
//          clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(17));
//          categoryid = "17";
//          pid = "17";
//        } else {
//          categoryid = String.valueOf(clist.get(0).getCategoryid());
//        }
//      }
//
//      model.addAttribute("clist", clist);
//      // 根据类型id在关联表中查到对应资料Id
//      List<Object[]> dataDefList = new ArrayList<Object[]>();
//      // 得到资料列表
//      List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
//      // List<DataLinks> linkList=new ArrayList<DataLinks>();
//      // List<DataReferDef> referList=new ArrayList<DataReferDef>();
//      List<DataFtpDef> dataftpList = new ArrayList<DataFtpDef>();
//      Page dataDefPage = new Page();
//      if (categoryid != null && !"".equals(categoryid)) {
//        dataDefPage =
//            dataDefService.findDataDefByCodes(
//                categoryid,
//                dataDefName,
//                new Page<DataDef>(Integer.parseInt(pageNo), Integer.parseInt(pageSize)));
//        dataDefList = dataDefPage.getList();
//        for (Object[] dd : dataDefList) {
//          Map<String, Object> map = new HashMap<String, Object>();
//          map.put("dataCode", dd[0]);
//          map.put("chnName", dd[1]);
//          map.put("chnDecription", dd[2]);
//          /*
//          //得到相关链接
//          linkList=dataLinksService.findDataLinksByDataCode(dd[0].toString());
//          map.put("linkList", linkList);
//          //得到相关文献
//          referList=dataReferDefService.findDataReferDefByDataCode(dd[0].toString());
//          map.put("referList", referList);
//          */
//          map.put("serviceMode", dd[3]);
//          map.put("orderno", dd[4]);
//          map.put("invalid", dd[5]);
//
//          // 得到Ftp下载信息
//          //          if (dd[3] != null && Integer.valueOf(dd[3].toString()) == 4) {
//          //            dataftpList = dataFtpDefService.findDataFtpDefByCode(dd[0].toString());
//          //            if (dataftpList != null && dataftpList.size() > 0) {
//          //              map.put("dataftp", dataftpList.get(0));
//          //            }
//          //          }
//          /*
//          if(linkList.size()>referList.size()){
//          	map.put("colspan", linkList.size());
//          }else if(linkList.size()<referList.size()){
//          	map.put("colspan", referList.size());
//          }else{
//          	map.put("colspan", 1);
//          }
//          */
//          resultList.add(map);
//        }
//      }
//      DataCategory dc = dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
//      model.addAttribute("page", dataDefPage);
//      model.addAttribute("CategoryDataRelts", resultList);
//      model.addAttribute("CategoryName", dc.getChnname());
//      model.addAttribute("pid", pid);
//      model.addAttribute("categoryid", categoryid);
//      model.addAttribute("dataDefName", dataDefName);
//      return "modules/data/dataReferList";
//    }
  }

  /**
   * 查看类型下的资料、链接引用文献信息,Ftp下载
   *
   * @return
   */
  @RequestMapping("/getDataChose")
  public String getDataChose(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    String categoryid = request.getParameter("categoryid"); // 获得类型id
    String pcategoryid = request.getParameter("pcategoryid");
    String pid = request.getParameter("pid"); // 类型中文名
    String ppid = request.getParameter("ppid");
    String pageNo = request.getParameter("pageNo");
    String pageSize = request.getParameter("pageSize");
    String dataDefName = request.getParameter("dataDefName");
    String chName = request.getParameter("chName");
    if (pageNo == null || "".equals(pageNo)) {
      pageNo = "1";
    }
    if (pageSize == null || "".equals(pageSize)) {
      pageSize = "20";
    }
    //        if (dataDefName != null && !"".equals(dataDefName)) {
    //            try {
    //                dataDefName = new String(dataDefName.getBytes("ISO8859-1"), "UTF-8");
    //            } catch (UnsupportedEncodingException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //        if (chName != null && !"".equals(chName)) {
    //            try {
    //                chName = new String(chName.getBytes("ISO8859-1"), "UTF-8");
    //            } catch (UnsupportedEncodingException e) {
    //                e.printStackTrace();
    //            }
    //        }
    model.addAttribute("chName", chName);
    model.addAttribute("pcategoryid", pcategoryid);
    model.addAttribute("ppid", ppid);
    DataCategory parent = dataCategoryDefService.getDataCategoryById(Integer.parseInt(ppid));
    if (parent != null) {
      model.addAttribute("parentName", parent.getChnname());
    }
    // pid不为空的情况，直接传值挑战
    if (pid != null && !"".equals(pid)) {
      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList(0);
      model.addAttribute("plist", plist);
      // 小类
      if ("17".equals(pid)) {
        categoryid = "17";
      }
      List<DataCategory> clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
      model.addAttribute("clist", clist);
      // 根据类型id在关联表中查到对应资料Id
      List<Object[]> dataDefList = new ArrayList<Object[]>();
      // 得到资料列表
      List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
      List<DataLinks> linkList = new ArrayList<DataLinks>();
      List<DataReferDef> referList = new ArrayList<DataReferDef>();
      List<DataFtpDef> dataftpList = new ArrayList<DataFtpDef>();
      Page dataDefPage = new Page();
      if (categoryid != null && !"".equals(categoryid)) {
        dataDefPage =
            dataDefService.findDataDefByCodes(
                categoryid,
                dataDefName,
                new Page<DataDef>(Integer.parseInt(pageNo), Integer.parseInt(pageSize)));
        dataDefList = dataDefPage.getList();
        for (Object[] dd : dataDefList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("dataCode", dd[0]);
          map.put("chnName", dd[1]);
          map.put("chnDecription", dd[2]);
          resultList.add(map);
        }
      }
      DataCategory dc = dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
      model.addAttribute("page", dataDefPage);
      model.addAttribute("CategoryDataRelts", resultList);
      model.addAttribute("CategoryName", dc.getChnname());
      model.addAttribute("pid", pid);
      model.addAttribute("categoryid", categoryid);
      model.addAttribute("dataDefName", dataDefName);
      return "modules/data/dataChoseList";
    } else { // 没有传值，本页面默认值
      // 大类
      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList(0);
      pid = String.valueOf(plist.get(0).getCategoryid());
      model.addAttribute("plist", plist);
      // 小类
      List<DataCategory> clist = dataCategoryDefService.getDataCategoryList(Integer.valueOf(pid));
      if ("17".equals(pid)) {
        categoryid = "17";
      } else {
        if(clist.size()!=0) {
          categoryid = String.valueOf(clist.get(0).getCategoryid());
        }
      }
      model.addAttribute("clist", clist);
      // 根据类型id在关联表中查到对应资料Id
      List<Object[]> dataDefList = new ArrayList<Object[]>();
      // 得到资料列表
      List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
      List<DataLinks> linkList = new ArrayList<DataLinks>();
      List<DataReferDef> referList = new ArrayList<DataReferDef>();
      List<DataFtpDef> dataftpList = new ArrayList<DataFtpDef>();
      Page dataDefPage = new Page();
      if (categoryid != null && !"".equals(categoryid)) {
        dataDefPage =
            dataDefService.findDataDefByCodes(
                categoryid,
                dataDefName,
                new Page<DataDef>(Integer.parseInt(pageNo), Integer.parseInt(pageSize)));
        dataDefList = dataDefPage.getList();
        for (Object[] dd : dataDefList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("dataCode", dd[0]);
          map.put("chnName", dd[1]);
          map.put("chnDecription", dd[2]);
          resultList.add(map);
        }
      }
      DataCategory dc = dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
      model.addAttribute("page", dataDefPage);
      model.addAttribute("CategoryDataRelts", resultList);
      model.addAttribute("CategoryName", dc.getChnname());
      model.addAttribute("pid", pid);
      model.addAttribute("categoryid", categoryid);
      model.addAttribute("dataDefName", dataDefName);
      return "modules/data/dataChoseList";
    }
  }

  /**
   * 根据大类获取子类列表
   *
   * @param DataClass
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/getDataSubClassList")
  public void getDataSubClassList(
      String DataClass, HttpServletRequest request, HttpServletResponse response, Model model) {
    // 根据大类型获取子类的列表
    List<DataCategory> clist =
        dataCategoryDefService.getDataCategoryList(Integer.valueOf(DataClass));
    String json = JsonMapper.toJsonString(clist);
    renderText(json, response);
  }

  /**
   * 查看类型下的资料、链接引用文献信息,Ftp下载
   *
   * @return
   */
  @RequestMapping("/getDataDetailByNo")
  public String getDataDetailByNo(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    String categoryid = request.getParameter("categoryid"); // 获得类型id
    String pid = request.getParameter("pid"); // 类型中文名
    String dataCode = request.getParameter("dataCode");
    Integer orderNo = Integer.valueOf(request.getParameter("orderNo"));
    String pageNo = request.getParameter("pageNo");
    String pageSize = request.getParameter("pageSize");
    String dataDefName = request.getParameter("dataDefName");
    if (pageNo == null || "".equals(pageNo)) {
      pageNo = "1";
    }
    if (pageSize == null || "".equals(pageSize)) {
      pageSize = "10";
    }
    //        if (dataDefName != null && !"".equals(dataDefName)) {
    //            try {
    //                dataDefName = new String(dataDefName.getBytes("ISO8859-1"), "UTF-8");
    //            } catch (UnsupportedEncodingException e) {
    //                e.printStackTrace();
    //            }
    //        }
    // 根据类型id在关联表中查到对应资料Id
    //		StringBuffer ids=new StringBuffer();

    List<CategoryDataRelt> list =
        categoryDataReltService.findCategoryDataReltByDataCodeid(
            dataCode, Integer.valueOf(categoryid));
    int tempNo = list.get(0).getOrderno();
    if (list.size() > 0) {
      CategoryDataRelt categoryDataRelt = list.get(0);
      categoryDataRelt.setOrderno(orderNo);
      categoryDataReltService.save(categoryDataRelt);
    }
    List<CategoryDataRelt> vateReltList = categoryDataReltService.findCategoryList(categoryid);
    Boolean flag = false;
    for (CategoryDataRelt cdr : vateReltList) {
      if (cdr.getOrderno() >= orderNo && !dataCode.equals(cdr.getDatacode())) {
        flag = true;
        cdr.setOrderno(cdr.getOrderno() + 1);
        categoryDataReltService.save(cdr);
      }
    }
    List<CategoryDataRelt> vateReltList2 = categoryDataReltService.findCategoryList(categoryid);
    for (int i = 0; i < vateReltList2.size(); i++) {
      CategoryDataRelt cdr = vateReltList2.get(i);
      cdr.setOrderno(i + 1);
      categoryDataReltService.save(cdr);
    }
//    cacheCleanController.cleanCache();
    List<Object[]> dataDefList = new ArrayList<Object[]>();
    // 得到资料列表
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    List<DataLinks> linkList = new ArrayList<DataLinks>();
    List<DataReferDef> referList = new ArrayList<DataReferDef>();
    List<DataFtpDef> dataftpList = new ArrayList<DataFtpDef>();
    Page dataDefPage = new Page();
    if (categoryid != null && !"".equals(categoryid)) {
      dataDefPage =
          dataDefService.findDataDefByCodes(
              categoryid,
              dataDefName,
              new Page<DataDef>(Integer.parseInt(pageNo), Integer.parseInt(pageSize)));
      dataDefList = dataDefPage.getList();
      for (Object[] dd : dataDefList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dataCode", dd[0]);
        map.put("chnName", dd[1]);
        map.put("chnDecription", dd[2]);
        // 得到相关链接
        linkList = dataLinksService.findDataLinksByDataCode(dd[0].toString());
        map.put("linkList", linkList);
        // 得到相关文献
        referList = dataReferDefService.findDataReferDefByDataCode(dd[0].toString());
        map.put("referList", referList);
        map.put("serviceMode", dd[3]);
        map.put("orderno", dd[4]);
        map.put("invalid", dd[5]);
        // 得到Ftp下载信息
        if (dd[3] != null && Integer.valueOf(dd[3].toString()) == 4) {
          dataftpList = dataFtpDefService.findDataFtpDefByCode(dd[0].toString());
          if (dataftpList != null && dataftpList.size() > 0) {
            map.put("dataftp", dataftpList.get(0));
          }
        }
        if (linkList.size() > referList.size()) {
          map.put("colspan", linkList.size());
        } else if (linkList.size() < referList.size()) {
          map.put("colspan", referList.size());
        } else {
          map.put("colspan", 1);
        }
        resultList.add(map);
      }
    }
    DataCategory dc = dataCategoryDefService.getDataCategoryById(Integer.parseInt(categoryid));
    DataCategory parent = dataCategoryDefService.getDataCategoryById(Integer.parseInt(pid));
    model.addAttribute("page", dataDefPage);
    model.addAttribute("CategoryDataRelts", resultList);
    model.addAttribute("CategoryName", dc.getChnname());
    model.addAttribute("parentName", parent.getChnname());
    model.addAttribute("pid", pid);
    model.addAttribute("categoryid", categoryid);
    model.addAttribute("dataDefName", dataDefName);
    return "modules/data/dataReferList";
  }

  /**
   * 跳转至编辑資料界面
   *
   * @param request
   * @param response
   * @param model
   * @param dataDef
   * @return
   */
  @RequestMapping(value = "/addDataDef")
  public String addDataDef(
      HttpServletRequest request, HttpServletResponse response, Model model, DataDef dataDef) {
    String categoryid = request.getParameter("categoryid");
    String pid = request.getParameter("pid");
    String chName = request.getParameter("chName");
    String pageType = request.getParameter("pageType");
    String imgUrl = (String) comparasService.getComparasByKey("imgServerUrl");
    //        if (chName != null && !"".equals(chName)) {
    //            try {
    //                chName = new String(chName.getBytes("ISO-8859-1"), "UTF-8");
    //            } catch (UnsupportedEncodingException e) {
    //                e.printStackTrace();
    //            }
    //        }
    List<DataClassDic> diclist = dataClassDicService.getDicList();
    DataCategoryDef dd = new DataCategoryDef();
//    pid="0";
//    if (pid != null && !"".equals(pid)) {
//      dd = dataCategoryDefService.findDataCategoryDefById(Integer.parseInt(pid));
//    }
    model.addAttribute("categoryid", categoryid);
    model.addAttribute("chName", chName);
    model.addAttribute("dataDic", diclist);
    model.addAttribute("imgServerUrl", imgUrl);
    model.addAttribute("pid", pid);
    model.addAttribute("pidName", dd.getChnname());
    model.addAttribute("pageType", pageType);
    return "modules/data/dataDefAdd";
  }

  /**
   * 获得资料信息并跳转至资料编辑页面
   *
   * @param request
   * @param response
   * @param model
   * @param datacode
   * @param pid
   * @return
   */
  @RequestMapping(value = {"edit"})
  public String detail(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      String datacode,
      String categoryid,
      String pid) {
    DataCategoryDef dataCategoryDef = new DataCategoryDef();
    String imgUrl = (String) comparasService.getComparasByKey("imgServerUrl");
    dataCategoryDef =
        dataCategoryDefService.findDataCategoryDefByIdUnique(Integer.valueOf(categoryid));
    String mUSICLinkUrl = (String) comparasService.getComparasByKey("MUSICLinkUrl");
    DataDef dataDef = dataDefService.findDataDefByDataCode(datacode);
    List<CategoryDataRelt> categoryDataRelts =
        categoryDataReltService.findCategoryDataReltByDataCodeid(
            dataDef.getDatacode(), Integer.valueOf(categoryid));
    if (categoryDataRelts.size() > 0) {
      dataDef.setOrderno(categoryDataRelts.get(0).getOrderno());
    }
    // 真正排序字段

    List<DataReferDef> dataReferList = dataReferDefService.findDataReferDefByDataCode(datacode);
    List<DataLinks> dataLinksList = dataLinksService.findDataLinksByDataCode(datacode);
    List<DataLinks> dataLinksList0 = new ArrayList();
    List<DataLinks> dataLinksList1 = new ArrayList();
    List<DataLinks> dataLinksList2 = new ArrayList();
    List<DataLinks> dataLinksList3 = new ArrayList();
    List<DataLinks> dataLinksList4 = new ArrayList();
    List<DataLinks> dataLinksList5 = new ArrayList();
    List<DataLinks> dataLinksList6 = new ArrayList();
    for (DataLinks dataLinks : dataLinksList) {
      if (dataLinks.getLinktype() == 0) {
        dataLinksList0.add(dataLinks);
      } else if (dataLinks.getLinktype() == 1) {
        dataLinksList1.add(dataLinks);
      } else if (dataLinks.getLinktype() == 2) {
        dataLinksList2.add(dataLinks);
      } else if (dataLinks.getLinktype() == 3) {
        dataLinksList3.add(dataLinks);
      } else if (dataLinks.getLinktype() == 4) {
        dataLinksList4.add(dataLinks);
      } else if (dataLinks.getLinktype() == 5) {
        dataLinksList5.add(dataLinks);
      } else if (dataLinks.getLinktype() == 6) {
        dataLinksList6.add(dataLinks);
      }
    }

    //    String ftpMapURL = (String) comparasService.getComparasByKey("ftpMapURL");
    //    if (ftpMapURL != null) {
    //      String[] arr = ftpMapURL.split(";");
    //      DataFtpDef dataFtpDef = dataDef.getDataFtpDef();
    //      if (dataFtpDef != null) {
    //        for (int i = 0; i < arr.length; i++) {
    //          String[] subarr = arr[i].split(",");
    //          for (int j = 0; j < subarr.length; j++) {
    //            if (subarr.length > 1) {
    //              if (dataFtpDef.getFtpurl01() != null) {
    //                dataFtpDef.setFtpurl01(dataFtpDef.getFtpurl01().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl02() != null) {
    //                dataFtpDef.setFtpurl02(dataFtpDef.getFtpurl02().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl03() != null) {
    //                dataFtpDef.setFtpurl03(dataFtpDef.getFtpurl03().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl04() != null) {
    //                dataFtpDef.setFtpurl04(dataFtpDef.getFtpurl04().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl05() != null) {
    //                dataFtpDef.setFtpurl05(dataFtpDef.getFtpurl05().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl06() != null) {
    //                dataFtpDef.setFtpurl06(dataFtpDef.getFtpurl06().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl07() != null) {
    //                dataFtpDef.setFtpurl07(dataFtpDef.getFtpurl07().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl08() != null) {
    //                dataFtpDef.setFtpurl08(dataFtpDef.getFtpurl08().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl09() != null) {
    //                dataFtpDef.setFtpurl09(dataFtpDef.getFtpurl09().replace(subarr[0],
    // subarr[1]));
    //              }
    //              if (dataFtpDef.getFtpurl10() != null) {
    //                dataFtpDef.setFtpurl10(dataFtpDef.getFtpurl10().replace(subarr[0],
    // subarr[1]));
    //              }
    //            }
    //          }
    //          dataDef.setDataFtpDef(dataFtpDef);
    //        }
    //      }
    //    }
    List<DataClassDic> diclist = dataClassDicService.getDicList();
    model.addAttribute("dataCategoryDef", dataCategoryDef);
    model.addAttribute("dataDef", dataDef);
    model.addAttribute("imgUrl", imgUrl);
    model.addAttribute("dataReferList", dataReferList);
    model.addAttribute("dataLinksList0", dataLinksList0);
    model.addAttribute("dataLinksList1", dataLinksList1);
    model.addAttribute("dataLinksList2", dataLinksList2);
    model.addAttribute("dataLinksList3", dataLinksList3);
    model.addAttribute("dataLinksList4", dataLinksList4);
    model.addAttribute("dataLinksList5", dataLinksList5);
    model.addAttribute("dataLinksList6", dataLinksList6);
    model.addAttribute("mUSICLinkUrl", mUSICLinkUrl);
    model.addAttribute("pid", pid);
    model.addAttribute("categoryid", categoryid);
    model.addAttribute("dataDic", diclist);
    return "modules/data/dataDetail";
  }

  /**
   * 保存资料信息
   *
   * @param dataDef
   * @param request
   * @param response
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/saveDataDef")
  public String saveDataDef(
      DataDef dataDef,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {

    String chndescription = request.getParameter("chndescription");
    String pid = request.getParameter("pid");
    String categoryid = request.getParameter("categoryid");
    dataDef.setChndescription(chndescription);
    if (dataDef.getInvalid() == null) {
      dataDef.setInvalid(0);
    }
    if (null == dataDef.getUserrankid()) {
      // 用户默认级别未国家级用户
      dataDef.setUserrankid(2);
    }
    if (null == dataDef.getFtpaccesstype()) {
      dataDef.setFtpaccesstype(0);
      dataDef.setIsoffline(0);
      dataDef.setTimezone(0);
    }
    dataDef.setWestlon(StringEscapeUtils.unescapeHtml(dataDef.getWestlon()));
    dataDef.setEastlon(StringEscapeUtils.unescapeHtml(dataDef.getEastlon()));
    dataDef.setNorthlat(StringEscapeUtils.unescapeHtml(dataDef.getNorthlat()));
    dataDef.setSouthlat(StringEscapeUtils.unescapeHtml(dataDef.getSouthlat()));
    String parentId = request.getParameter("treeNodeId");
    CategoryDataRelt cdr = new CategoryDataRelt();
    List<CategoryDataRelt> categoryDataRelts = new ArrayList<CategoryDataRelt>();
    String parentNo = "";
    cdr = new CategoryDataRelt();
    if (parentId != null && parentId.length() != 0) {
      parentNo = parentId;
      categoryDataRelts =
          categoryDataReltService.findCategoryDataReltById(Integer.valueOf(parentId));
    } else {
      //			parentNo=pid;
      parentNo = categoryid;
      categoryDataRelts =
          categoryDataReltService.findCategoryDataReltById(Integer.valueOf(categoryid));
    }
    cdr.setOrderno(dataDef.getOrderno());
    cdr.setCategoryid(Integer.parseInt(parentNo));
    cdr.setDatacode(dataDef.getDatacode());
    int id = categoryDataReltService.getId();
    cdr.setId(id + 1);
    try {
      dataDef.setServicemode(8);
      dataDef.setInsertmode(0);
      dataDef.setDatasourcecode("CMISS");
      dataDef.setIsincludesub(0);
      dataDefService.save(dataDef);
      categoryDataReltService.save(cdr);
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存成功");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "保存失败");
      e1.printStackTrace();
    }
    DataCategory dc = dataCategoryDefService.getDataCategoryById(Integer.parseInt(parentNo));
    return "redirect:"
        + "/dataService/getDataDetail?categoryid="
        + parentNo
        + "&pid="
        + dc.getParentid();
  }

  /**
   * 保存资料信息
   *
   * @param dataDef
   * @param request
   * @param response
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/updateDataDef")
  public String updateDataDef(
      DataDef dataDef,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    String chndescription = request.getParameter("chndescription");
    String pid = request.getParameter("pid");
    String categoryid = request.getParameter("categoryid");
    List<CategoryDataRelt> categoryDataRelts =
        categoryDataReltService.findCategoryDataReltByDataCodeid(
            dataDef.getDatacode(), Integer.valueOf(categoryid));
    dataDef.setWestlon(StringEscapeUtils.unescapeHtml(dataDef.getWestlon()));
    dataDef.setEastlon(StringEscapeUtils.unescapeHtml(dataDef.getEastlon()));
    dataDef.setNorthlat(StringEscapeUtils.unescapeHtml(dataDef.getNorthlat()));
    dataDef.setSouthlat(StringEscapeUtils.unescapeHtml(dataDef.getSouthlat()));
    dataDef.setChndescription(StringEscapeUtils.unescapeHtml(chndescription));
    try {
      Integer rankid = dataDef.getUserrankid();
      if (null == rankid) {
        // 默认国家级用户权限
        dataDef.setUserrankid(2);
      }
      //默认music接口
      dataDef.setDatasourcecode("CMISS");
      dataDef.setFtpaccesstype(0);
      //默认music接口
      dataDef.setServicemode(8);
      dataDef.setIsincludesub(0);
      dataDef.setIsoffline(0);
      dataDef.setTimezone(0);
      dataDefService.save(dataDef);
      if (categoryDataRelts.size() > 0) {
        CategoryDataRelt cdr = new CategoryDataRelt();
        cdr = categoryDataRelts.get(0);
        cdr.setOrderno(dataDef.getOrderno());
        categoryDataReltService.save(cdr);
      }
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存成功");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "保存失败");
      e1.printStackTrace();
    }
    return "redirect:" + "/dataService/getDataDetail?categoryid=" + categoryid + "&pid=" + pid;
  }

  /**
   * 保存类型信息
   *
   * @param dataCategoryDef
   * @param request
   * @param response
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/updateDataCategory")
  public String updateDataCategory(
      DataCategoryDef dataCategoryDef,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    DataCategory dc = new DataCategory();
    String chndescription = request.getParameter("chndescription");
    String engdescription = request.getParameter("engdescription");
    String pid = request.getParameter("pid");
    // 判断大类 0和子类 1
    String type = request.getParameter("type");
    if ("0".equals(type)) {
      dc.setCategorylayer(1);
      dc.setParentid(0);
    } else if ("1".equals(type)) {
      dc.setCategorylayer(2);
      dc.setParentid(Integer.valueOf(pid));
    }
    dc.setCategoryid(dataCategoryDef.getCategoryid());
    dc.setChndescription(chndescription);
    dc.setChnname(dataCategoryDef.getChnname());
    dc.setDatacount(dataCategoryDef.getDatacount());
    dc.setEngdescription(engdescription);
    dc.setEngname(dataCategoryDef.getEngname());
    dc.setIconurl(dataCategoryDef.getIconurl());
    dc.setImagechntitle(dataCategoryDef.getImagechntitle());
    dc.setImageengtitle(dataCategoryDef.getImageengtitle());
    dc.setImageurl(dataCategoryDef.getImageurl());
    dc.setInvalid(0);
    dc.setLargeiconurl(dataCategoryDef.getLargeiconurl());
    dc.setLinkurl(dataCategoryDef.getLinkurl());
    dc.setMiddleiconurl(dataCategoryDef.getMiddleiconurl());
    dc.setShortchnname(dataCategoryDef.getShortchnname());
    dc.setShortengname(dataCategoryDef.getShortengname());
    if (dc.getParentid() == 0) {
      dc.setShowtype(0);
    } else {
      dc.setShowtype(1);
    }
    dc.setShowuserrankid(0);
    dc.setTemplatefile(dataCategoryDef.getTemplatefile());
    dc.setOrderno(dataCategoryDef.getOrderno());
    dc.setTimeseq(dataCategoryDef.getTimeseq());
    try {
      dataCategoryDefService.saveDc(dc);
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存成功");
    } catch (Exception e) {
      addMessage(redirectAttributes, "保存失败");
      e.printStackTrace();
    }
    return "redirect:" + "/dataService/list";
  }

  /**
   * 添加-保存类型信息
   *
   * @param dataCategoryDef
   * @param request
   * @param response
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "/saveDataCategory")
  public String saveDataCategory(
      DataCategoryDef dataCategoryDef,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    DataCategory dc = new DataCategory();
    String chndescription = request.getParameter("chndescription");
    String engdescription = request.getParameter("engdescription");
    String pid = request.getParameter("pid");
    // 判断大类 0和子类 1
    String type = request.getParameter("type");
    if ("0".equals(type)) {
      dc.setCategorylayer(1);
      dc.setParentid(0);
    } else if ("1".equals(type)) {
      dc.setCategorylayer(2);
      dc.setParentid(Integer.valueOf(pid));
    }
    //    if (null != dataCategoryDef.getCategoryid()) {
    //      dc.setCategoryid(dataCategoryDef.getCategoryid());
    //    } else {
    //      int id = categoryDataReltService.getId();
    //      dc.setCategoryid(id + 1);
    //    }
    dc.setChndescription(chndescription);
    dc.setChnname(dataCategoryDef.getChnname());
    dc.setDatacount(dataCategoryDef.getDatacount());
    dc.setEngdescription(engdescription);
    dc.setEngname(dataCategoryDef.getEngname());
    dc.setIconurl(dataCategoryDef.getIconurl());
    dc.setImagechntitle(dataCategoryDef.getImagechntitle());
    dc.setImageengtitle(dataCategoryDef.getImageengtitle());
    dc.setImageurl(dataCategoryDef.getImageurl());
    dc.setInvalid(0);
    dc.setLargeiconurl(dataCategoryDef.getLargeiconurl());
    dc.setLinkurl(dataCategoryDef.getLinkurl());
    dc.setMiddleiconurl(dataCategoryDef.getMiddleiconurl());
    dc.setShortchnname(dataCategoryDef.getShortchnname());
    dc.setShortengname(dataCategoryDef.getShortengname());
    if (dc.getParentid() == 0) {
      dc.setShowtype(0);
    } else {
      dc.setShowtype(1);
    }
    dc.setShowuserrankid(0);
    dc.setTemplatefile(dataCategoryDef.getTemplatefile());
    dc.setOrderno(dataCategoryDef.getOrderno());
    try {
      dataCategoryDefService.saveDc(dc);
      //      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存成功");
    } catch (Exception e) {
      addMessage(redirectAttributes, "保存失败");
      e.printStackTrace();
    }
    return "redirect:" + "/dataService/list";
  }

  /**
   * 获得资料类型树
   *
   * @param extId
   * @param paramMap
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/getdataServiceTreeList")
  @ResponseBody
  public List<Map<String, Object>> getdataServiceTreeList(
      @RequestParam(required = false) Long extId,
      Map<String, Object> paramMap,
      HttpServletRequest request,
      HttpServletResponse response) {
    response.setContentType("application/json; charset=UTF-8");
    List<Map<String, Object>> mapList = Lists.newArrayList();
    List<DataCategoryDef> dataCategoryDefs = dataCategoryDefService.findAllOne();
    for (DataCategoryDef dataCategoryDef : dataCategoryDefs) {
      Map<String, Object> map = Maps.newHashMap();
      map.put("id", dataCategoryDef.getCategoryid());
      map.put("pId", dataCategoryDef.getParent().getCategoryid());
      map.put("name", dataCategoryDef.getChnname());
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
   * @param dataCategoryDef
   * @return
   */
  @RequestMapping(value = "/addDataCategoryDef")
  public String addDataCategoryDef(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      DataCategoryDef dataCategoryDef) {
    String categoryid = request.getParameter("categoryid");
    String pid = request.getParameter("pid");
    // 编辑根据categoryid和pid，直接无新增，categoryid不为空，则修改，pid为空则添加
    String imgServerUrl = (String) comparasService.getComparasByKey("imgServerUrl");
    DataCategoryDef parent = null;
    String pname = "";
    // 编辑
    if (categoryid != null && !"".equals(categoryid)) {
      dataCategoryDef =
          dataCategoryDefService.findDataCategoryDefById(Integer.parseInt(categoryid));
      if ("0".equals(pid)) {
        model.addAttribute("type", 0);
      } else {
        model.addAttribute("type", 1);
      }
      // 选择大类
      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList2(0);
      model.addAttribute("plist", plist);
      model.addAttribute("categoryid", categoryid);
      model.addAttribute("pid", pid);
      model.addAttribute("pname", pname);
      model.addAttribute("dataCategoryDef", dataCategoryDef);
      model.addAttribute("imgServerUrl", imgServerUrl);
      return "modules/data/dataCategoryEdit";
    } else if (pid != null && !"".equals(pid)) {
      // 添加子类
      parent = dataCategoryDefService.findDataCategoryDefById(Integer.parseInt(pid));
      if (parent != null) {
        pname = parent.getChnname();
      }
      model.addAttribute("type", 1);
      // 选择大类
      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList2(0);
      model.addAttribute("plist", plist);
      model.addAttribute("categoryid", categoryid);
      model.addAttribute("pid", pid);
      model.addAttribute("pname", pname);
      model.addAttribute("dataCategoryDef", dataCategoryDef);
      model.addAttribute("imgServerUrl", imgServerUrl);
      return "modules/data/dataCategoryAdd2";
    } else {
      // 新增
      model.addAttribute("type", 0);
      // 选择大类
      List<DataCategory> plist = dataCategoryDefService.getDataCategoryList2(0);
      model.addAttribute("plist", plist);
      model.addAttribute("categoryid", categoryid);
      model.addAttribute("pid", pid);
      model.addAttribute("pname", pname);
      model.addAttribute("dataCategoryDef", dataCategoryDef);
      model.addAttribute("imgServerUrl", imgServerUrl);
      return "modules/data/dataCategoryAdd";
    }
  }

  /**
   * 删除类型及类型下的所有资料
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping("/deleteCategory")
  public String deleteCategory(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    String pid = request.getParameter("pid");
    String id = request.getParameter("id");
    if ("0".equals(pid)) {
      try {
        DataCategory dataCategory = dataCategoryDefService.getDataCategoryById(Integer.valueOf(id));
        Integer invalid = dataCategory.getInvalid();
        if (invalid == 0) {
          dataCategory.setInvalid(1);
        } else if (invalid == 1) {
          dataCategory.setInvalid(0);
        }
        dataCategoryDefService.saveDc(dataCategory);
        // dataCategoryDefService.delDataCategoryDef(id);
//        cacheCleanController.cleanCache();
        addMessage(redirectAttributes, "更新成功");
      } catch (Exception e) {
        addMessage(redirectAttributes, "更新失败");
        e.printStackTrace();
      }
    } else {
      try {
        DataCategory dataCategory = dataCategoryDefService.getDataCategoryById(Integer.valueOf(id));
        Integer invalid = dataCategory.getInvalid();
        if (invalid == 0) {
          dataCategory.setInvalid(1);
        } else if (invalid == 1) {
          dataCategory.setInvalid(0);
        }
        dataCategoryDefService.saveDc(dataCategory);
//        cacheCleanController.cleanCache();
        addMessage(redirectAttributes, "更新成功");
      } catch (Exception e) {
        addMessage(redirectAttributes, "更新失败");
        e.printStackTrace();
      }
    }
    return "redirect:" + "/dataService/list";
  }
  /*
      @RequestMapping("/deleteCategory")
    	public String deleteCategory(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
        	String pid=request.getParameter("pid");
    		String id=request.getParameter("id");
    		List<CategoryDataRelt> cateList=new ArrayList<CategoryDataRelt>();//所有子类型与资料关联
    		List<DataCategoryDef> cateGoryList=new ArrayList<DataCategoryDef>();//存放所有子类型
    		boolean flag=false;
    		if("0".equals(pid)){
    			cateGoryList=dataCategoryDefService.findDataReferByPid(id);
  //  				for(DataCategoryDef dcd:cateGoryList){
  //  					cateList=categoryDataReltService.findCategoryListBycateId(dcd.getCategoryid());
  //  					for(CategoryDataRelt gfdr:cateList){
  //  						try {
  //  							dataFtpDefService.delDataFtpDef(gfdr.getDatacode());//删除Ftp相关信息
  //  							dataReferDefService.delDataReferByCode(gfdr.getDatacode());//删除引用文献
  //  							dataLinksService.delDataLinkByCode(gfdr.getDatacode());//删除关联
  //  							dataDefService.delDataDef(gfdr.getDatacode());
  ////  							categoryDataReltService.delCategoryDataRelt(gfdr.getDatacode(), gfdr.getCategoryid());
  //  						} catch (Exception e) {
  //  							addMessage(redirectAttributes, "删除失败");
  //  							e.printStackTrace();
  //  						}
  //  					}
  //  					try {
  //  						dataCategoryDefService.delDataCategoryDef(String.valueOf(dcd.getCategoryid()));
  //  					} catch (Exception e) {
  //  						addMessage(redirectAttributes, "删除失败");
  //  						e.printStackTrace();
  //  					}
  //  				}
    				try {
    					dataCategoryDefService.delDataCategoryDef(id);
    					cacheCleanController.cleanCache();
    					addMessage(redirectAttributes, "删除成功");
    				} catch (Exception e) {
    					addMessage(redirectAttributes, "删除失败");
    					e.printStackTrace();
    				}
    		}else{
  //  				cateList=categoryDataReltService.findCategoryListBycateId(Integer.parseInt(id));
  //  				for(CategoryDataRelt gfdr:cateList){
  //  					try {
  //  						//dataFtpDefService.delDataFtpDef(gfdr.getDatacode());//删除Ftp相关信息
  //  						//dataLinksService.delDataLinkByCode(gfdr.getDatacode());//删除关联
  //  						//dataReferDefService.delDataReferByCode(gfdr.getDatacode());//删除引用文献
  //  						//dataDefService.delDataDef(gfdr.getDatacode());
  ////  						categoryDataReltService.delCategoryDataRelt(gfdr.getDatacode(), gfdr.getCategoryid());
  //  					} catch (Exception e) {
  //  						addMessage(redirectAttributes, "删除失败");
  //  						e.printStackTrace();
  //  					}
  //  				}
    				try {
    					dataCategoryDefService.delDataCategoryDef(id);
    					cacheCleanController.cleanCache();
    					addMessage(redirectAttributes, "删除成功");
    				} catch (Exception e) {
    					addMessage(redirectAttributes, "删除失败");
    					e.printStackTrace();
    				}
    		}
    		return "redirect:"+"/dataService/list";
    	}
    	*/

  /**
   * 上传图片文件到服务器
   *
   * @param request
   * @param response
   */
  @RequestMapping("/imgUpload")
  public void uploadImg(HttpServletRequest request, HttpServletResponse response) {
    String imgDiv = request.getParameter("imgDiv");
    String dataclasscode = request.getParameter("dataclasscode");
    String datacode = request.getParameter("datacode");
    String imgServerPath = (String) comparasService.getComparasByKey("imgServerPath"); // /space/pic
    //    	String
    // imgServerUrl=(String)comparasService.getComparasByKey("imgServerUrl");//10.1.64.154/pic
    String dataPath = (String) comparasService.getComparasByKey("dataPath");
    //    	String imgServerPath="E:/data";
    //    	String dataPath="";
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    Map<String, MultipartFile> fliemap = multipartRequest.getFileMap();
    CommonsMultipartFile file = (CommonsMultipartFile) fliemap.get(imgDiv);
    String dataUrl = "";
    String path = "";
    String localpath = "";
    String OriginalFilename = file.getOriginalFilename();
    if ("imageurl1".equals(imgDiv) || "iconImg".equals(imgDiv) || "imageurl".equals(imgDiv)) {
      dataUrl = "/img";
    } else if ("iconurl2".equals(imgDiv)) {
      dataUrl = "/icon";
    } else if ("largeiconurl3".equals(imgDiv)) {
      dataUrl = "/iconX";
    } else if ("middleiconurl4".equals(imgDiv)) {
      dataUrl = "/iconS";
    } else if ("imageurl2".equals(imgDiv)) {
      dataUrl = "/" + dataclasscode.trim() + "/" + datacode.trim();
    }
    path = imgServerPath +"/"+ dataPath +"/"+ dataUrl + "/" + OriginalFilename; // 拼成上传路径
    localpath = "/" + "/"+ dataPath +"/" + dataUrl + "/" + OriginalFilename;
    Map<String, Object> map = new HashMap<String, Object>();
    if (!"".equals(dataUrl) && null != dataUrl) {
      // 上传文件不存在则创建文件夹
      File dir = new File(imgServerPath + "/"+ dataPath +"/" + dataUrl);
      if (!dir.exists()) {
        //				dir.mkdir();
        dir.mkdirs();
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
        map.put("returnCode", 0);
        map.put("imgUrl", localpath);
        map.put("imgDiv", imgDiv);
      } catch (FileNotFoundException e) {
        map.put("returnCode", 1);
        map.put("imgUrl", localpath);
        map.put("imgDiv", imgDiv);
        this.logger.error("路径错误" + e.getMessage());
        e.printStackTrace();
      } catch (IOException e) {
        map.put("returnCode", 1);
        map.put("imgUrl", localpath);
        map.put("imgDiv", imgDiv);
        this.logger.error("上传失败" + e.getMessage());
        e.printStackTrace();
      }
    }
    renderText(JsonMapper.toJsonString(map), response);
  }

  /**
   * 检查资料类型是否已存在
   *
   * @param request
   * @param response
   * @param datacode
   */
  /*    @RequestMapping(value="/checkDataCode")
  public void checkDataCode(HttpServletRequest request,HttpServletResponse response,String datacode,String id){
  	String message="";
  	DataDef dataDef=dataDefService.findDataDefByDataCode(datacode,pid);
  	if(dataDef==null){
  		message="ok";
  	}else{
  		message="hasdatacode";
  	}
  	renderText(message, response);
  }*/
  @RequestMapping(value = "/checkDataCode")
  public void checkDataCode(
      HttpServletRequest request, HttpServletResponse response, String datacode, String id) {
    String message = "";
    //    	DataDef dataDef=dataDefService.findDataDefByDataCode(datacode,pid);
    boolean flag = true;
    //    	CategoryDataRelt cater=categoryDataReltService.findCategoryDataReltByDataCode(datacode,
    // id);
    List<CategoryDataRelt> calList = categoryDataReltService.findCategoryList(id);
    for (CategoryDataRelt rt : calList) {
      String upperCode = datacode.toUpperCase();
      if (upperCode.equals(rt.getDatacode().toUpperCase())) {
        flag = false;
      }
    }
    if (flag) {
      message = "ok";
    } else {
      message = "hasdatacode";
    }
    renderText(message, response);
  }

  /**
   * 删除资料及相关链接和引用文档
   *
   * @param request
   * @param response
   * @param redirectAttributes
   * @return
   */
  /*
    @RequestMapping("/deleteDataDef")
    public String deleteDataDef(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
    	String categoryid=request.getParameter("categoryid");//获得类型id
  String pid=request.getParameter("pid");//类型中文名
  String dataCode=request.getParameter("dataCode");
  try {
  	dataFtpDefService.delDataFtpDef(dataCode);
  	dataLinksService.delDataLinkByCode(dataCode);
  	dataReferDefService.delDataReferByCode(dataCode);
  	dataDefService.delDataDef(dataCode);
  	addMessage(redirectAttributes, "删除成功");
  } catch (Exception e) {
  	addMessage(redirectAttributes, "删除失败");
  	e.printStackTrace();
  }
    	return "redirect:"+"/dataService/getDataDetail?pid="+pid+"&categoryid="+categoryid;
    }
    */

  /**
   * 删除资料及相关链接和引用文档
   *
   * @param request
   * @param response
   * @param redirectAttributes
   * @return
   */
  @RequestMapping("/deleteDataDef")
  public String deleteDataDef(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    String categoryid = request.getParameter("categoryid"); // 获得类型id
    String pid = request.getParameter("pid"); // 类型中文名
    String dataCode = request.getParameter("dataCode");
    try {
      // dataFtpDefService.delDataFtpDef(dataCode);
      // dataLinksService.delDataLinkByCode(dataCode);
      // dataReferDefService.delDataReferByCode(dataCode);
      // dataDefService.delDataDef(dataCode);
      DataDef dataDef = dataDefService.findDataDefByDataCode(dataCode);
      if (dataDef.getInvalid() == 0) {
        dataDef.setInvalid(1);
        dataDefService.save(dataDef);
      } else if (dataDef.getInvalid() == 1) {
        dataDef.setInvalid(0);
        dataDefService.save(dataDef);
      }
      addMessage(redirectAttributes, "更新成功");
    } catch (Exception e) {
      addMessage(redirectAttributes, "更新失败");
      e.printStackTrace();
    }
    return "redirect:" + "/dataService/getDataDetail?pid=" + pid + "&categoryid=" + categoryid;
  }

  /**
   * ckeditor上传图片的方法
   *
   * @param upload
   * @param request
   * @param response
   * @throws IOException
   */
  @RequestMapping("/imgUplad")
  public void imgUplaod(
      @RequestParam("upload") MultipartFile upload,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    request.setCharacterEncoding("utf-8");
    response.setContentType("charset=utf-8");
    PrintWriter out = response.getWriter();
    String filePath = "";
    String fileURL = "";
    String imgServerPath = ((String) comparasService.getComparasByKey("imgServerPath")).trim();
    String noticePath = ((String) comparasService.getComparasByKey("dataPath")).trim();
    //		String imgServerPath="E:/";
    //		String noticePath="data/";
    String datacode = request.getParameter("datacode");
    String dataclasscode = request.getParameter("dataclasscode");
    String imgServerUrl = ((String) comparasService.getComparasByKey("imgServerUrl")).trim();
    String callback = request.getParameter("CKEditorFuncNum");
    String contentType = upload.getContentType();
    String extName = FileUtils.getExtensionName(upload.getOriginalFilename());
    String docId = UUID.randomUUID().toString(); // 将文件名改成用guid表示
    filePath =
        imgServerPath
            + noticePath
            + "/"
            + dataclasscode.trim()
            + "/"
            + datacode.trim()
            + "/"
            + docId
            + "."
            + extName;
    fileURL =
        (imgServerUrl
                + "/"
                + noticePath
                + "/"
                + dataclasscode.trim()
                + "/"
                + datacode.trim()
                + "/"
                + docId
                + "."
                + extName)
            .replace("\\", "/");
    if (!upload.isEmpty()) {
      try {
        if ("image/pjpeg".equals(contentType)
            || ("image/jpeg".equals(contentType))
            || "image/png".equals(contentType)
            || "image/x-png".equals(contentType)
            || "image/gif".equals(contentType)
            || "image/bmp".equals(contentType)) {
          File dir =
              new File(
                  imgServerPath
                      + noticePath
                      + "/"
                      + dataclasscode.trim()
                      + "/"
                      + datacode.trim()
                      + "/");
          if (!dir.exists()) {
            dir.mkdirs();
            System.out.println(
                "dirs created: "
                    + imgServerPath
                    + noticePath
                    + "/"
                    + dataclasscode.trim()
                    + "/"
                    + datacode.trim()
                    + "/");
          }
          InputStream stream = upload.getInputStream(); // 把文件读入

          ByteArrayOutputStream baos = new ByteArrayOutputStream();

          OutputStream bos = new FileOutputStream(filePath);

          // 建立一个上传文件的输出流
          int bytesRead = 0;
          byte[] buffer = new byte[8192];
          while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead); // 将文件写入服务器
          }
          bos.close();
          stream.close();
        } else {
          out.println("<script type=\"text/javascript\">");
          out.println(
              "window.parent.CKEDITOR.tools.callFunction("
                  + callback
                  + ",'','文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
          out.println("</script>");
          return;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
      //			PropertiesLoader propertiesLoader=new PropertiesLoader("configuation.properties");
      //			String httpPath=propertiesLoader.getProperty("imgPosition")+upload.getOriginalFilename();
      // 从公共参数表中获取路径
      //	        Object obj = comparasDao.getComparasByKey("imgPosition");
      //	        if(obj!=null&&!"".equals(obj)){
      //		        String imgPosition= obj.toString();
      //		        fileURL=fileURL.replace("\\", "/");
      out.println("<script type=\"text/javascript\">");
      out.println(
          "window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + fileURL + "','')");
      out.println("</script>");
      //	        }
    }
  }

  /**
   * 相关链接上传文档
   *
   * @param request
   * @param response
   */
  @RequestMapping("/docUpload")
  public void docUpload(HttpServletRequest request, HttpServletResponse response) {
    String dataCode = request.getParameter("dataCode");
    String imgServerPath = (String) comparasService.getComparasByKey("imgServerPath");
    String imgServerUrl = ((String) comparasService.getComparasByKey("imgServerUrl")).trim();
    //    	String imgServerPath="E:/data/";
    String docDefUrl = (String) comparasService.getComparasByKey("dataPath");
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    Map<String, MultipartFile> fliemap = multipartRequest.getFileMap();
    CommonsMultipartFile file = (CommonsMultipartFile) fliemap.get("docBox");
    long l = file.getSize();
    int maxUploadSize = Integer.valueOf(Global.getConfig("web.maxUploadSize")).intValue();
    Map<String, Object> map = new HashMap<String, Object>();
    if (l > maxUploadSize) {
      map.put("linkURL", "");
      map.put("linkName", "");
      map.put("message", "文件大小超过" + maxUploadSize / 1024 / 1024 + "M，请联系管理员！");
    } else {
      String docId = UUID.randomUUID().toString(); // 将文件名改成用guid表示
      String OriginalFilename = file.getOriginalFilename();
      int index = OriginalFilename.lastIndexOf(".");
      String fileName = OriginalFilename.substring(0, index);
      String last = OriginalFilename.substring(index + 1, OriginalFilename.length());
      if (isContainsChinese(OriginalFilename)) {
        OriginalFilename = docId + "." + last;
      }
      DataDef dd = dataDefService.findDataDefByDataCode(dataCode);
      String path =
          imgServerPath
              + docDefUrl
              + "/"
              + dd.getDataclasscode().trim()
              + "/"
              + dd.getDatacode()
              + "/"
              + OriginalFilename;
      String localpath =
          imgServerUrl
              + "/"
              + docDefUrl
              + "/"
              + dd.getDataclasscode().trim()
              + "/"
              + dd.getDatacode()
              + "/"
              + OriginalFilename;
      File dir =
          new File(
              imgServerPath
                  + "/"
                  + docDefUrl
                  + "/"
                  + dd.getDataclasscode().trim()
                  + "/"
                  + dd.getDatacode()
                  + "/");
      if (!dir.exists()) {
        dir.mkdirs();
      }
      OutputStream os;
      try {
        os = new FileOutputStream(path.trim());
        InputStream is = file.getInputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
          os.write(buffer, 0, bytesRead); // 将文件写入服务器
        }
        os.close();
        is.close();
        map.put("linkURL", localpath.trim());
        map.put("linkName", fileName);
        map.put("message", "上传成功");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    renderText(JsonMapper.toJsonString(map), response);
  }

  public static boolean isContainsChinese(String str) {
    String regEx = "[\u4e00-\u9fa5]";
    Pattern pat = Pattern.compile(regEx);
    Matcher matcher = pat.matcher(str);
    boolean flg = false;
    if (matcher.find()) {
      flg = true;
    }
    return flg;
  }

  /** 批量修改菜单排序 */
  @RequestMapping(value = "updateSort")
  public String updateSort(String[] ids, RedirectAttributes redirectAttributes) {
    try {
      int len = ids.length;
      DataCategoryDef[] dataCategoryDef = new DataCategoryDef[len];

      for (int i = 0; i < len; i++) {
        dataCategoryDef[i] =
            dataCategoryDefService.findDataCategoryDefByIdUnique(Integer.valueOf(ids[i]));
        dataCategoryDef[i].setOrderno(i);
        dataCategoryDefService.save(dataCategoryDef[i]);
      }
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存菜单排序成功!");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "保存菜单排序失败!");
      e1.printStackTrace();
    }
    return "redirect:" + "/dataService/list";
  }

  /** 批量修改菜单排序 dataDef表 */
  @RequestMapping(value = "dataDefSort")
  public String dataDefSort(
      String[] ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    String categoryid = request.getParameter("categoryid"); // 获得类型id
    String pid = request.getParameter("pid"); // 类型中文名
    try {
      int len = ids.length;
      CategoryDataRelt[] categoryDataRelt = new CategoryDataRelt[len];

      for (int i = 0; i < len; i++) {
        categoryDataRelt[i] = categoryDataReltService.findDataReltById(Integer.valueOf(ids[i]));
        categoryDataRelt[i].setOrderno(i);
        categoryDataReltService.save(categoryDataRelt[i]);
      }
//      cacheCleanController.cleanCache();
      addMessage(redirectAttributes, "保存菜单排序成功!");
    } catch (Exception e1) {
      addMessage(redirectAttributes, "保存菜单排序失败!");
      e1.printStackTrace();
    }

    return "redirect:" + "/dataService/getDataDetail?categoryid=" + categoryid + "&pid=" + pid;
  }

  @RequestMapping(value = "addCategoryDataRelt")
  public String addCategoryDataRelt(
      String[] codes, String pcategoryid, String cpid, RedirectAttributes redirectAttributes) {
    try {
      int len = codes.length;
      List<Object[]> list = categoryDataReltService.findCategoryListById(pcategoryid);
      Boolean flag = false;
      String name = "";
      for (int i = 0; i < len; i++) {
        for (int j = 0; j < list.size(); j++) {
          if (codes[i].equals(list.get(j)[0])) {
            flag = true;
            name = list.get(j)[1].toString();
          }
          if (flag) {
            break;
          }
        }
      }
      if (!"".equals(name)) {
        addMessage(redirectAttributes, name + "添加失败，该资料已经存在！");
        return "redirect:"
            + "/dataService/getDataDetail?pid="
            + cpid
            + "&categoryid="
            + pcategoryid;
      } else {
        for (int i = 0; i < len; i++) {
          CategoryDataRelt categoryDataRelt = new CategoryDataRelt();
          categoryDataRelt.setOrderno(0);
          categoryDataRelt.setCategoryid(Integer.valueOf(pcategoryid));
          categoryDataRelt.setDatacode(codes[i]);
          categoryDataRelt.setId(categoryDataReltService.getId() + 1);
          categoryDataReltService.save(categoryDataRelt);
        }
//        cacheCleanController.cleanCache();
        addMessage(redirectAttributes, "添加成功!");
      }
    } catch (Exception e1) {
      addMessage(redirectAttributes, "添加失败!");
      e1.printStackTrace();
    }
    return "redirect:" + "/dataService/getDataDetail?pid=" + cpid + "&categoryid=" + pcategoryid;
  }

  @RequestMapping("/upload")
  public void upload(
      String uploadFileName,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      RedirectAttributes redirectAttributes)
      throws IOException {
    request.setCharacterEncoding("utf-8");
    response.setContentType("charset=utf-8");
    //String local = "d:";
    // 绝对路径的/space/pic/
    String imgServerPath = (String)comparasService.getComparasByKey("imgServerPath");
    String docDefUrl = (String)comparasService.getComparasByKey("dataPath");
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
    Map<String, MultipartFile> fliemap = multipartRequest.getFileMap();
    CommonsMultipartFile upload = (CommonsMultipartFile)fliemap.get("upload");
    String docId = "";
    String pathString = "";
    docId = UUID.randomUUID().toString();
    String filename = docId + uploadFileName.substring(uploadFileName.lastIndexOf("."), uploadFileName.length());
    String temp =  imgServerPath + docDefUrl + "/"+docId+"/";
    OutputStream fos = null;
    InputStream fis = null;
    File file = new File(temp);
    String message = "";
    String context = "";
    try {
    	try {
    		if (!file.exists()) {
    			file.mkdirs();
    		}
    		// 建立文件输出流
    		pathString = temp + filename;
    		fos = new FileOutputStream(pathString);
    		// 建立文件上传流
    		fis = upload.getInputStream();
    		byte[] buffer = new byte[8192];
    		int len = 0;
    		while ((len = fis.read(buffer)) > 0) {
    			fos.write(buffer, 0, len);
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		close(fos, fis);
    	}
    	try {
    		String context2 = doc2Html(docId, temp + filename, temp);
    	   	org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(context2);
    		Element body = doc.body();
    		context=body.html();
    		//System.out.println(context);
    		//context=StringEscapeUtils.escapeHtml(context);
    		//context=org.springframework.web.util.HtmlUtils.htmlEscape(context);
    	} catch (TransformerException e) {
    		e.printStackTrace();
    	} catch (ParserConfigurationException e) {
    		e.printStackTrace();
    	}
    	File imgfile = new File(pathString);
    	if (file.exists()) {
    		imgfile.delete();
    	}
    	message = "0";
    } catch (Exception e) {
    	message = "1";
    }
    Map map=new HashMap();
    map.put("msg", context);
    map.put("message", message);
    String json=JsonMapper.toJsonString(map);
    renderText(json, response);
  }

  public String doc2Html(final String id, String fileName, String temp)
      throws TransformerException, IOException, ParserConfigurationException {
    final String imgServerPath = ((String) comparasService.getComparasByKey("imgServerUrl")).trim();
    final String docDefUrl = (String) comparasService.getComparasByKey("dataPath");
    File imageDir = new File(temp);
    if (!imageDir.isDirectory()) {
      imageDir.mkdirs();
    }
    HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
    WordToHtmlConverter wordToHtmlConverter =
        new WordToHtmlConverter(
            DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
    wordToHtmlConverter.setPicturesManager(
        new PicturesManager() {
          public String savePicture(
              byte[] content,
              PictureType pictureType,
              String suggestedName,
              float widthInches,
              float heightInches) {
            return imgServerPath + "/" + docDefUrl + "/" + id + "/" + id + suggestedName;
          }
        });
    wordToHtmlConverter.processDocument(wordDocument);
    // 淇濆瓨鍥剧墖
    List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
    if (pics != null) {
      for (int i = 0; i < pics.size(); i++) {
        Picture pic = (Picture) pics.get(i);
        try {
          String filename = temp + id + pic.suggestFullFileName();
          // String filename = noticePath+id+pic.suggestFullFileName();
          pic.writeImageContent(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
    Document htmlDocument = wordToHtmlConverter.getDocument();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DOMSource domSource = new DOMSource(htmlDocument);
    StreamResult streamResult = new StreamResult(out);

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer serializer = tf.newTransformer();
    serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
    serializer.setOutputProperty(OutputKeys.METHOD, "html");
    serializer.transform(domSource, streamResult);
    out.close();
    return new String(out.toByteArray());
  }

  private void close(OutputStream fos, InputStream fis) {
    if (fis != null) {
      try {
        fis.close();
      } catch (IOException e) {
        System.out.println("FileInputStream关闭失败");
        e.printStackTrace();
      }
    }
    if (fos != null) {
      try {
        fos.close();
      } catch (IOException e) {
        System.out.println("FileOutputStream关闭失败");
        e.printStackTrace();
      }
    }
  }

  /**
   * 批量导入资料种类
   */
  @RequestMapping(value = "/excelUpload")
  public String excelUpload(@RequestParam("file") CommonsMultipartFile file){
    try {
      InputStream in = file.getInputStream();
      dataCategoryDefService.upload(in,file);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "modules/data/dataServiceList";
  }

  /**
   * 批量导入数据资料
   */
  @RequestMapping(value = "/dataExcelUpload")
  public void dataUpload(@RequestParam("file") CommonsMultipartFile file){
    try {
      InputStream in = file.getInputStream();
      dataDefService.upload(in,file);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
