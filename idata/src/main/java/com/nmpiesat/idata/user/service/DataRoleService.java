package com.nmpiesat.idata.user.service;

import com.nmpiesat.idata.user.dao.DataRoleDao;
import com.nmpiesat.idata.user.dao.DataRoleLimitsDao;

import com.nmpiesat.idata.user.entity.DataRole;
import com.nmpiesat.idata.user.entity.DataRolelimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据角色维护-service
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/5 10:02
 */
@Service
public class DataRoleService {
  @Autowired
  private DataRoleDao drDao;
  @Autowired
  private DataRoleLimitsDao drlDao;

  public List<DataRole> findAll() {
    List<DataRole> list = drDao.findAll();
    return list;
  }
  public List<DataRolelimits> getDataRoleLimitsById(String dataroleid) {
    List<DataRolelimits> returnList = drlDao.getDataRoleLimitsById(dataroleid);
    return returnList;
  }

}
