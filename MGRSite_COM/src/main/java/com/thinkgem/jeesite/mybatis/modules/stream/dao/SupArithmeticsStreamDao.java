/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.stream.dao;

import com.thinkgem.jeesite.mybatis.modules.stream.entity.SupArithmeticsStream;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

/**
 * 算法池基本信息DAO接口
 * @author yck
 * @version 2019-12-03
 */
@MyBatisDao
public interface SupArithmeticsStreamDao extends CrudDao<SupArithmeticsStream> {
	public void updateAriState(SupArithmeticsStream supArithmeticsStream);
    public void updateStreamStatus(SupArithmeticsStream supArithmeticsStream);
    public SupArithmeticsStream findObjectByAirName(@Param("streamName") String streamName);
}