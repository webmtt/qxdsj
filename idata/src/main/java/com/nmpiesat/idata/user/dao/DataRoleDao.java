package com.nmpiesat.idata.user.dao;

import com.nmpiesat.idata.user.entity.DataRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色-Dao
 *
 * @author RainingTime
 * @version 1.0 2018年7月5日
 */
@Repository
public interface DataRoleDao {
  List<DataRole> findAll();
}
