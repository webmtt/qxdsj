package com.thinkgem.jeesite.modules.data.service;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.UserDataRole.entity.ZTreeInfo;
import com.thinkgem.jeesite.modules.data.dao.DataCategoryDAO;
import com.thinkgem.jeesite.modules.data.dao.DataCategoryDefDao;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.mybatis.modules.report.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataCategoryDefService extends BaseService {
  @Autowired private DataCategoryDefDao dataCategoryDefDao;
  @Autowired private DataCategoryDAO dataCategoryDao;
  @Autowired private ComparasDao comparasDao;

  public List<DataCategoryDef> findAll() {
    List<DataCategoryDef> list = new ArrayList<DataCategoryDef>();
    List<DataCategoryDef> dataCategoryDefs = dataCategoryDefDao.findAll();
    DataCategoryDef.sortList(list, dataCategoryDefs, 0);
    return list;
  }

  public DataCategoryDef getId(Integer categoryid) {
    return dataCategoryDefDao.get(categoryid);
  }

  public List<DataCategoryDef> findAllOne() {
    return dataCategoryDefDao.findAllOne();
  }

  public List<DataCategoryDef> findDataCategoryDef() {
    String imgUrl = (String) comparasDao.getComparasByKey("imgServerUrl");
    List<DataCategoryDef> list = this.dataCategoryDefDao.findAll();
    List<DataCategoryDef> dataCategoryDefList = new ArrayList();
    for (DataCategoryDef dataCategoryDef : list) {
      dataCategoryDef.setLargeiconurl(imgUrl + dataCategoryDef.getLargeiconurl());
      /*if(dataCategoryDef.getParentid() == 0){
      	//子级list
      	List<DataCategoryDef> dataCategoryDefListSub = new ArrayList();
      	for(DataCategoryDef dataCategoryDefSub : list){
      		if(dataCategoryDefSub.getParentid() == dataCategoryDef.getCategoryid()){
      			dataCategoryDefListSub.add(dataCategoryDefSub);
      		}
      	}
      	dataCategoryDef.setList(dataCategoryDefListSub);

      	dataCategoryDefList.add(dataCategoryDef);
      }*/
    }
    return dataCategoryDefList;
  }

  public String findDataCategoryDefTop() {
    String imgUrl = (String) comparasDao.getComparasByKey("imgServerUrl");
    List<DataCategoryDef> list = this.dataCategoryDefDao.findDataCategoryDefTop();
    for (DataCategoryDef dataCategoryDef : list) {
      dataCategoryDef.setIconurl(imgUrl + dataCategoryDef.getIconurl());
    }
    return JsonMapper.toJsonString(list);
  }

  public DataCategoryDef findDataCategoryDefById(int id) {
    List<DataCategoryDef> list = this.dataCategoryDefDao.findDataCategoryDefById(id);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public DataCategoryDef findDataCategoryDefByIdUnique(int id) {
    // String imgUrl = (String)comparasDao.getComparasByKey("imgServerUrl");
    DataCategoryDef dataCategoryDef = this.dataCategoryDefDao.findDataCategoryDefByIdUnique(id);
    // dataCategoryDef.setImageurl(imgUrl+dataCategoryDef.getImageurl());
    return dataCategoryDef;
  }

  @Transactional
  public void save(DataCategoryDef dataCategoryDef) {
    dataCategoryDefDao.save(dataCategoryDef);
  }

  @Transactional
  public void save2(DataCategoryDef dataCategoryDef) {
    dataCategoryDefDao.save(dataCategoryDef);
  }

  public void delDataCategoryDef(String id) {
    dataCategoryDefDao.delDataCategoryDef(id);
  }

  public Integer getDataCategoryDefId(Integer pid) {
    return dataCategoryDefDao.getDataCategoryDefId(pid);
  }

  public List<DataCategoryDef> findDataReferByPid(String pid) {
    return dataCategoryDefDao.findDataReferByPid(pid);
  }

  @Transactional
  public void saveDc(DataCategory dc) {
    dataCategoryDao.save(dc);
    //    dataCategoryDao.saveData(dc);
  }

  public DataCategory getDataCategoryById(Integer categoryid) {
    return dataCategoryDao.getDataCategoryById(categoryid);
  }

  public List<DataCategory> getDataCategoryList(Integer parentid) {
    return dataCategoryDao.getDataCategoryList(parentid);
  }

  public List<DataCategory> getDataCategoryList2(Integer parentid) {
    return dataCategoryDao.getDataCategoryList2(parentid);
  }

  public List<Object[]> getDataEleSetCode(int categoryid) {
    return dataCategoryDefDao.getDataEleSetCode(categoryid);
  }

  public List<Object[]> getDataSearchSetCode(int categoryid) {
    return dataCategoryDefDao.getDataSearchSetCode(categoryid);
  }

  public List<Object[]> getelesetcodeList(String elesetcode) {
    return dataCategoryDefDao.getelesetcodeList(elesetcode);
  }

  public List<Object[]> getDataSearchSetList(String searchsetcode) {
    return dataCategoryDefDao.getDataSearchSetList(searchsetcode);
  }
  public  List<Object[]> findDataCategory(){
    List<Object[]> list=dataCategoryDefDao.findDataCategory();
    return list;
  }

  /**
   * 批量导入
   */
  @Transactional(readOnly = false)
  public void upload(InputStream in, MultipartFile file){
    try {
      List<List<Object>> listob = ExcelUtil.getBankListByExcel(in, file.getOriginalFilename());
      dataCategoryDao.upload(listob);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
