/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interf.dao;


import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import com.thinkgem.jeesite.mybatis.common.persistence.TreeDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 树结构生成DAO接口
 * @author zhaoxiaojun
 * @version 2019-12-05
 */
@MyBatisDao
public interface SysInterfaceDao extends TreeDao<SysInterface> {

    public List<SysInterface> findAllList();

    void deleteById(@Param("id") String id);

    void insertInter(SysInterface sysInterface);
}