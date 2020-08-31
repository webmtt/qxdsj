/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.service;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.modules.report.cache.FileDataCacheUtils;
import com.thinkgem.jeesite.mybatis.modules.report.dao.SupSurveystationDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupSurveystation;
import com.thinkgem.jeesite.mybatis.modules.report.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表查询参数配置Service
 *
 * @author yang.kq
 * @version 2019-11-06
 */
@Service
@Transactional(readOnly = true)
public class SupSurveystationService extends CrudService<SupSurveystationDao, SupSurveystation> {
 @Autowired
 private SupSurveystationDao dao;
  @Override
  public SupSurveystation get(String id) {
    return super.get(id);
  }

  @Override
  public List<SupSurveystation> findList(SupSurveystation supSurveystation) {
    List<SupSurveystation> list = dao.findList(supSurveystation);
    return list;
  }
    public List<Map<String,Object>> getStationTree(SupSurveystation supSurveystation){
        List<SupSurveystation> list = dao.findList(supSurveystation);
        //地市
        List<String> plist=new ArrayList<>();
        //区县
        List<String> clist=new ArrayList<>();

        Map<String,Object> resultMap=new HashMap<>();
        //结果集
        List<Map<String,Object>> result=new ArrayList<>();
        for (SupSurveystation sss : list) {
            String cities=sss.getCities();
            String contry=sss.getCounty();
            if(!plist.contains(cities)){
                plist.add(cities);
            }
            if(!clist.contains(contry)){
                clist.add(contry);
            }
        }
        for(int i=0,size=plist.size();i<size;i++){
            String cities=plist.get(i);
            resultMap=new HashMap<>();
            resultMap.put("id", i);
            resultMap.put("name", cities);
            resultMap.put("pid","");
            result.add(resultMap);
            for(int j=0,length=clist.size();j<length;j++){
                String contry=clist.get(j);
                boolean flag=false;
                for(int k=0,length1=list.size();k<length1;k++){
                    SupSurveystation s=list.get(k);
                    if(cities.equals(s.getCities())&&contry.equals(s.getCounty())){
                        if(!flag) {
                            resultMap = new HashMap<>();
                            resultMap.put("id", i + "#" + j);
                            resultMap.put("name", contry);
                            resultMap.put("pid", i);
                            result.add(resultMap);
                            flag=true;
                        }
                        resultMap=new HashMap<>();
                        resultMap.put("id", s.getStationNum());
                        resultMap.put("name", s.getStationName());
                        resultMap.put("pid",i+"#"+j);
                        result.add(resultMap);
                        FileDataCacheUtils.stationCache.put(s.getStationNum(), s.getStationName());
                    }
                }
            }
        }
        return result;
    }
    public SupSurveystation findStation(String stationnum){
      SupSurveystation surveystation=new SupSurveystation();
      long num=Long.parseLong(stationnum);
      List<SupSurveystation> list=dao.findListById(num);
      if(list==null||list.size()==0){
          return null;
      }else{
          return list.get(0);
      }

    }

  /**
   * 查询分页数据
   * @param page 分页对象
   * @param supSurveystation
   * @return
   */
  @Override
  public Page<SupSurveystation> findPage(Page<SupSurveystation> page, SupSurveystation supSurveystation) {
    supSurveystation.setPage(page);
    page.setList(dao.findList(supSurveystation));
    return page;
  }
  /**
   * 保存数据（插入或更新）
   * @param supSurveystation
   */
  @Override
  @Transactional(readOnly = false)
  public void save(SupSurveystation supSurveystation) {
      if (supSurveystation.getIsNewRecord()){
        supSurveystation.preInsert();
        dao.insert(supSurveystation);
      }else{
        supSurveystation.preUpdate();
        dao.update(supSurveystation);
      }
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(SupSurveystation supSurveystation) {
    dao.delete(supSurveystation);
  }

    /**
     * 上传文件
     * @param in
     * @param file
     */
    @Transactional(readOnly = false)
  public void upload(InputStream in, MultipartFile file){
      try {
          DecimalFormat decimalFormat2 = new DecimalFormat(".00");
          DecimalFormat decimalFormat1 = new DecimalFormat(".0");
          List<List<Object>> listob = ExcelUtil.getBankListByExcel(in,file.getOriginalFilename());
          List<SupSurveystation> supSurveystationList = new ArrayList<SupSurveystation>();
          for (int i = 0; i< listob.size(); i++){
              List<Object> ob = listob.get(i) ;
              SupSurveystation supSurveystation = new SupSurveystation();
              supSurveystation.preInsert();
              //台站号
//              supSurveystation.setStationNum(Long.valueOf(String.valueOf(ob.get(0))));
              List<SupSurveystation> list=dao.findListById(Long.valueOf(String.valueOf(ob.get(0))));
              if(list.size()!=0){
                  continue;
              }
              //台站名称
              supSurveystation.setStationName(String.valueOf(ob.get(1)));
              //省
              supSurveystation.setProvinces(String.valueOf(ob.get(2)));
              //市
              supSurveystation.setCities(String.valueOf(ob.get(3)));
              //区
              supSurveystation.setCounty(String.valueOf(ob.get(4)));
              //纬度
              supSurveystation.setWd(decimalFormat2.format(Float.parseFloat(String.valueOf(ob.get(5))))+"");
              //经度
              supSurveystation.setJd(decimalFormat2.format(Float.parseFloat(String.valueOf(ob.get(6))))+"");
              //测站高度
              supSurveystation.setViewHeight(decimalFormat1.format(Float.parseFloat(String.valueOf(ob.get(7))))+"");
              supSurveystationList.add(supSurveystation);
          }
          if(supSurveystationList.size()!=0) {
              dao.insertInfoBatch(supSurveystationList);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

}

