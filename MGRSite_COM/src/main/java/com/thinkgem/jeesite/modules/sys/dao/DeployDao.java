package com.thinkgem.jeesite.modules.sys.dao;


import com.thinkgem.jeesite.modules.sys.entity.Deploy;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface DeployDao {
    public List<Deploy> getById(@Param("id") String id);

    void insertDeploys(Deploy deploy);

    void updateDeploys(Deploy deploy);

    List<String> getALLUrl(@Param("id") String id);
}
