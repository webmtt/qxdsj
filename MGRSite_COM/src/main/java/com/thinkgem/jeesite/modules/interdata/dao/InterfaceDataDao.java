/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interdata.dao;


import com.thinkgem.jeesite.modules.interdata.entity.InterDeploy;
import com.thinkgem.jeesite.modules.interdata.entity.InterfaceData;
import com.thinkgem.jeesite.modules.interdata.entity.InterfaceDefine;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API接口展示DAO接口
 * @author zhaoxiaojun
 * @version 2019-12-06
 */
@MyBatisDao
public interface InterfaceDataDao extends CrudDao<InterfaceData> {


    public List<InterfaceData> getAllInter();

    List<InterfaceDefine> getInterface(String id);

    List<Map<String, Object>> projectcdinter(String id);
    List<Map<String, Object>> projectcdinterall();

    String projectcdinterName(String custom_api_id);

    List<Map<String, Object>> interElement(String id);

    Map<String, Object> interElementName(String param_id);

    List<Map<String, Object>> getFcstLevel();

    List<Map<String, Object>> getNetCodes();

    List<Map<String, Object>> getSoilDepths();

    List<Map<String, Object>> getAdminCodes();

    List<Map<String, Object>> getFcstEle(String id);

    List<Map<String, Object>> getElements(String id);

    List<Map<String, Object>> getInterElement(@Param("inters") String[] inters);

    int addInterValue(InterDeploy interDeploy);

    int updateInterValue(InterDeploy interDeploy);

    List<InterDeploy> getAllInterValue();
}