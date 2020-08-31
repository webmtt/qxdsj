/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.service;

import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.modules.report.cache.FileDataCacheUtils;
import com.thinkgem.jeesite.mybatis.modules.report.dao.SupReportsearchconfDao;
import com.thinkgem.jeesite.mybatis.modules.report.dao.SupReportsearchconfUploadDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupReportsearchconf;
import com.thinkgem.jeesite.mybatis.modules.report.util.ExcelUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报表查询参数配置Service
 *
 * @author yang.kq
 * @version 2019-11-05
 */
@Service
@Transactional(readOnly = true)
public class SupReportsearchconfService
        extends CrudService<SupReportsearchconfDao, SupReportsearchconf> {
  @Autowired
  private SupReportsearchconfDao dao;
  @Autowired
  private SupReportsearchconfUploadDao supReportsearchconfUploadDao;
  @Override
  @Transactional(readOnly = false)
  public SupReportsearchconf get(String id) {
    return dao.get(id);
  }

  @Override
  public List<SupReportsearchconf> findList(SupReportsearchconf supReportsearchconf) {
    return dao.findList(supReportsearchconf);
  }

  @Override
  public Page<SupReportsearchconf> findPage(
          Page<SupReportsearchconf> page, SupReportsearchconf supReportsearchconf) {
    supReportsearchconf.setPage(page);
    page.setList(dao.findList(supReportsearchconf));
    return page;
  }

  @Override
  @Transactional(readOnly = false)
  public void save(SupReportsearchconf supReportsearchconf) {
    if (supReportsearchconf.getIsNewRecord()){
      supReportsearchconf.preInsert();
      dao.insert(supReportsearchconf);
    }else{
      supReportsearchconf.preUpdate();
      dao.update(supReportsearchconf);
    }
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(SupReportsearchconf supReportsearchconf) {
    dao.delete(supReportsearchconf);
  }

  public List<SupReportsearchconf> findAllConfig() {
    return dao.findList(new SupReportsearchconf());
  }
  public List<SupReportsearchconf> getListByDataType(String dataType) {
    return dao.getListByDataType(dataType);
  }

  public List<SupReportsearchconf> findConfigById(String parentId,String param_type) {
    SupReportsearchconf t = new SupReportsearchconf();
    t.setId(parentId);
    SupReportsearchconf srt = new SupReportsearchconf();
    srt.setParent(t);
    if(param_type!=null&&(!"".equals(param_type))) {
      srt.setParamType(param_type);
    }
    List<SupReportsearchconf> list = dao.findListById(srt);
    if ("1".equals(parentId)) { // 项目标识
      for (SupReportsearchconf src : list) {
        String id = src.getId();
        if (null == FileDataCacheUtils.projectCache.get(id)) {
          FileDataCacheUtils.projectCache.put(id, src.getParamName());
        }
      }
    }

    return list;
  }
  /**
   * 批量导入
   */
  @Transactional(readOnly = false)
  public void upload(InputStream in, MultipartFile file){
    try {
      List<List<Object>> listob = ExcelUtil.getBankListByExcel(in, file.getOriginalFilename());
      List<SupReportsearchconf> list=new ArrayList<>();
      for(int i = 0;i < listob.size();i++){
        List<Object> ob = listob.get(i);
        SupReportsearchconf supReportsearchconf = new SupReportsearchconf();
        User user = UserUtils.getUser();
        supReportsearchconf.setCreateBy(user);
        supReportsearchconf.setUpdateBy(user);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        supReportsearchconf.setCreateDate(Timestamp.valueOf(simpleDate.format(new Date())));
        supReportsearchconf.setUpdateDate(Timestamp.valueOf(simpleDate.format(new Date())));
        supReportsearchconf.setId(ob.get(0)+"");
        supReportsearchconf.setParamName(ob.get(1)+"");
        String paramtype=null;
        if("日值".equals(ob.get(3)+"")){
          paramtype="2";
        }else if("定时值".equals(ob.get(3)+"")){
          paramtype="1";
        }else if("旬值".equals(ob.get(3)+"")){
          paramtype="3";
        }else if("月值".equals(ob.get(3)+"")){
          paramtype="4";
        }else if("年值".equals(ob.get(3)+"")){
          paramtype="5";
        }else{
          paramtype=ob.get(3)+"";
        }
        supReportsearchconf.setParamType(paramtype);
        supReportsearchconf.setParent(new SupReportsearchconf(""+ob.get(4)));
        if(ob.size()==6){
          supReportsearchconf.setRemarks(ob.get(5)+"");
        }
        list.add(supReportsearchconf);
      }
      if(list.size()>0){
        for(SupReportsearchconf sup:list){
          dao.insert(sup);
        }
      }
//      supReportsearchconfUploadDao.upload(listob);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
