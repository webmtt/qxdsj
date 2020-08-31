package com.thinkgem.jeesite.mybatis.modules.report.service;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.common.utils.StringUtils;
import com.thinkgem.jeesite.mybatis.modules.report.cache.FileDataCacheUtils;
import com.thinkgem.jeesite.mybatis.modules.report.dao.SupDatafileinfoDao;
import com.thinkgem.jeesite.mybatis.modules.report.dao.SupSurveystationDao;
import com.thinkgem.jeesite.mybatis.modules.report.dao.XuGuSearchDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupDatafileinfo;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupSurveystation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.thinkgem.jeesite.mybatis.modules.report.util.DataExportUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表数据文件管理Service
 *
 * @author yang.kq
 * @version 2019-11-06
 */
@Service
@Transactional(readOnly = true)
public class SupDatafileinfoService extends CrudService<SupDatafileinfoDao, SupDatafileinfo> {
  @Autowired
  private SupDatafileinfoDao dao;
  @Resource
  private XuGuSearchDao xuguDao;
  @Autowired
  private SupSurveystationDao daoStation;
  @Override
  public SupDatafileinfo get(String id) {
    return dao.get(id);
  }

  @Override
  public List<SupDatafileinfo> findList(SupDatafileinfo supDatafileinfo) {
    return dao.findList(supDatafileinfo);
  }

  @Override
  public Page<SupDatafileinfo> findPage(
          Page<SupDatafileinfo> page, SupDatafileinfo supDatafileinfo) {
    supDatafileinfo.setPage(page);
    page.setList(dao.findList(supDatafileinfo));
    return page;
  }

  @Override
  @Transactional(readOnly = false)
  public void save(SupDatafileinfo supDatafileinfo) {
    supDatafileinfo.setState(1);
    if (supDatafileinfo.getIsNewRecord()){
      supDatafileinfo.preInsert();
      dao.insert(supDatafileinfo);
    }else{
      supDatafileinfo.preUpdate();
      dao.update(supDatafileinfo);
    }
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(SupDatafileinfo supDatafileinfo) {
    dao.delete(supDatafileinfo);
  }
  /**
   * 文件上传
   *
   * @param request
   * @return
   */
  public String fileUpload(HttpServletRequest request) {
    String realPath = request.getSession().getServletContext().getRealPath("/");
    realPath = realPath.replaceAll("\\\\", "/");
    String path = realPath + "fileUpload/";
    try {
      // 判断文件夹是否存在
      File fileFolder = new File(path);
      if (!fileFolder.exists()) {
        fileFolder.mkdirs();
      }
      long startTime = System.currentTimeMillis();
      // 将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
      CommonsMultipartResolver multipartResolver =
          new CommonsMultipartResolver(request.getSession().getServletContext());
      // 检查form中是否有enctype="multipart/form-data"
      if (multipartResolver.isMultipart(request)) {
        // 将request变成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 获取multiRequest 中所有的文件名
        Iterator iter = multiRequest.getFileNames();

        while (iter.hasNext()) {

          // 一次遍历所有文件
          MultipartFile file = multiRequest.getFile(iter.next().toString());
          if (file != null) {
            String filePath = path + file.getOriginalFilename();
            // 上传
            file.transferTo(new File(filePath));
          }
        }
      }
      long endTime = System.currentTimeMillis();
      System.out.println("Spring方法的运行时间：" + String.valueOf(endTime - startTime) + "ms");
    } catch (Exception e) {
      path = null;
      logger.debug("文件上传失败");
    }
    return path;
  }

  /**
   * 数据导出成excel
   *
   * @param response
   * @param headInfo-表头
   * @param list-导出数据
   */
  public void exportData(
      HttpServletResponse response,
      String headInfo,
      List<Map<String, Object>> list,
      String dataType) {
    List<String> headcolumns = (List<String>) JSON.parse(headInfo);
//    String fileName = DictUtils.getDictLabel(filetype, "file_type", null);
    String datatype=null;
    if ("2".equals(dataType)) { // 日
      datatype="日值";
    } else if ("4".equals(dataType)){// 月
      datatype="月值";
    }else if("3".equals(dataType)){//旬
      datatype= "旬值";
    }else if("1".equals(dataType)) { // 定时
      datatype="定时值";
    }  else if ("5".equals(dataType)) { // 年
      datatype="年值/跨月值";
    }

//    String projectName=FileDataCacheUtils.projectCache.get(projectID);
    String fileName = "文件"+datatype+ "数据";
  DataExportUtil.exportToExcel(response, list, fileName, headcolumns);
  }

  public Map<String, Object> getQueryData(
          String stations,
          String projectId,
          String starTime,
          String endTime,
          String queryData,
          String dataType,String type,int pageno,int pagesize){
    Map<String, Object> map = null;
    String[] station = stations.split(",");
    List<String> stats = StringUtils.stringToList(station);
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    String [] arr=queryData.split(",");
    List<String> queryDatas = StringUtils.stringToList(arr);
//    String[]projects=projectId.split(",");
    long countTotal=0;
//    for(int i=0;i<projects.length;i++){
//        String proId=projects[i];
//        String projectName=FileDataCacheUtils.projectCache.get(proId);
    //    if(projectName.contains("日照")){
    //      dataType="6";
    //    }

        Map<String,Object> resultMAP = xuguDao.getQueryDataList( stats, starTime, endTime, queryDatas, dataType, type, pageno, pagesize);

         countTotal+= (long) resultMAP.get("countTotal");
        List<Map<String, Object>> list1= (List<Map<String, Object>>) resultMAP.get("dataList");

        Map<String,Object> dataMap=null;
        for (int k = 0, m = list1.size(); k < m; k++) {
          map = new LinkedHashMap<String, Object>();
          dataMap=list1.get(k);
          Long num = Long.parseLong(dataMap.get("V01300")+"");
          map.put("stationId", num);
          String stationName=FileDataCacheUtils.stationCache.get(num);
          if(stationName==null){
           List<SupSurveystation> stationList= daoStation.findListById(num);
           stationName=stationList.get(0).getStationName();
          }
          map.put("station", stationName);
//          map.put("project", projectName);
          Object hourvalue=dataMap.get("V04004");
          Object dayvalue=dataMap.get("V04003");
          Object monthvalue=dataMap.get("V04002");
          Object yearvalue=dataMap.get("V04001");
          Object meadowvalue=dataMap.get("V04290");
          if(yearvalue!=null){
            map.put("yearvalue", yearvalue);
          }
          if(monthvalue!=null){
            map.put("monthvalue", monthvalue);
          }
          if(dayvalue!=null){
            map.put("dayvalue", dayvalue);
          }
          if(hourvalue!=null){
            map.put("hourvalue", hourvalue);
          }
          if(meadowvalue!=null){
            map.put("meadowvalue", meadowvalue);

          }
          for (int j = 0, length = queryDatas.size(); j < length; j++) {
            String param=queryDatas.get(j);
            map.put("column" + j, dataMap.get(param));
          }

          list.add(map);
        }
//    }
    Map<String,Object> resultData=new HashMap<>();
    resultData.put("dataList",list);
    resultData.put("count",countTotal);
    return resultData;
  }
}
