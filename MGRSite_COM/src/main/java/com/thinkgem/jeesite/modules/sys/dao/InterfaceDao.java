package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.modules.interf.entity.SysInterface;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;

import java.util.List;

@MyBatisDao
public interface InterfaceDao extends CrudDao<SysInterface> {


        public List<SysInterface> findByParentIdsLike(SysInterface menu);

        public List<SysInterface> findByUserId(SysInterface menu);

        public int updateParentIds(SysInterface menu);

        public int updateSort(SysInterface menu);
}
