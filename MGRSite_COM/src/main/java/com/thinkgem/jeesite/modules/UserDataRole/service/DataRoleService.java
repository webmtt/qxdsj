package com.thinkgem.jeesite.modules.UserDataRole.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.UserDataRole.dao.DataroleDao;
import com.thinkgem.jeesite.modules.UserDataRole.dao.DataroleLimitsDao;
import com.thinkgem.jeesite.modules.UserDataRole.entity.DataRolelimits;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 数据角色维护-service
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/5 10:02
 */
@Service
public class DataRoleService {
  @Autowired private DataroleDao drDao;
  @Autowired private DataroleLimitsDao drlDao;

  public Page<Datarole> getDataroleList(String idOrName, String pageNo, String pageSize) {
    int No = Integer.parseInt(pageNo);
    int Size = Integer.parseInt(pageSize);
    Page<Datarole> page = null;
    if (null == idOrName) {
      page = drDao.getDataRoleList(No, Size);
    } else {
      page = drDao.getDataRoleListBy(No, Size, idOrName);
    }

    return page;
  }

  public int getMaxOrderNo() {
    return drDao.getMaxOrderNo();
  }
  public Datarole getdataroleByIdOrName(String dataroleid, String name) {
    Datarole datarole = null;
    if (null != dataroleid && (!"".equals(dataroleid))) {
      datarole = drDao.getDataroleById(dataroleid);
    } else {
      datarole = drDao.getDataroleByName(name);
    }
    return datarole;
  }

  public boolean save(Datarole datarole, String nodes) {

    Datarole datarole1 = drDao.getDataroleById(datarole.getDataroleId());
    if (datarole1 != null) {
      drDao.updateRole(datarole);
      drlDao.delDataroleLimitsById(datarole.getDataroleId());
    } else {
      datarole.setDataroleId(UUID.randomUUID().toString());
      drDao.insertRole(datarole);
    }
   String[] array=nodes.split(",");
    List<DataRolelimits> list = new ArrayList<DataRolelimits>();
    DataRolelimits drl = null;
    for (int i = 0, length = array.length; i < length; i++) {
      drl = new DataRolelimits();
      String dataid =array[i];
      drl.setRoledataId(datarole.getDataroleId());
      drl.setDataId(dataid);
      list.add(drl);
    }
    drlDao.batchSave(list);
    return true;
  }

  public void delDataroleById(String dataroleid) {
    drlDao.delDataroleLimitsById(dataroleid);
    drDao.delDataroleById(dataroleid);
  }

  public List<DataRolelimits> getdataroleLimitsById(String dataroleid) {
    List<Object[]> list = drlDao.getdataroleLimitsById(dataroleid);
    List<DataRolelimits> returnList = new ArrayList<>();
    DataRolelimits dl = null;
    for (Object[] t : list) {
      dl = new DataRolelimits();
      dl.setDataId(t[0] + "");
      dl.setRoledataId(t[1] + "");
      returnList.add(dl);
    }
    return returnList;
  }

  public List<Datarole> findAll() {
    List<Datarole> list = drDao.findAll();
    return list;
  }

  public List<Datarole> getdataroleByUserId(String userid) {
    List<Object[]> list = drDao.getdataroleByUserId(userid);
    if(list==null){
      return null;
    }
    List<Datarole> result = new ArrayList<Datarole>();
    Datarole dr = null;
    for (Object[] t : list) {
      dr = new Datarole();
      dr.setDataroleId(t[0] + "");
      dr.setDataroleName(t[1] + "");
      result.add(dr);
    }
    return result;
  }
}
