package com.thinkgem.jeesite.modules.data.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.DataSourceDef;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataSourceDefDao extends BaseDao<DataSourceDef> {
  @Override
  public List<DataSourceDef> findAll() {
    return this.find("from DataSourceDef where invalid = '0'");
  }

  public DataSourceDef findDataSourceDefByDsaccesscode(String dsaccesscode) {
    List<DataSourceDef> list =
        this.find(
            "from DataSourceDef where invalid = '0' and dsaccesscode = :p1",
            new Parameter(dsaccesscode));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
