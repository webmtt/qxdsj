package com.nmpiesat.idata.user.dao;


import com.nmpiesat.idata.user.entity.DataRolelimits;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 数据角色权限
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 14:45
 */
@Repository
public interface DataRoleLimitsDao {

    List<DataRolelimits> getDataRoleLimitsById(String dataroleid);
}
