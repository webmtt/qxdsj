/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.arithmetic.dao;


import com.thinkgem.jeesite.mybatis.modules.arithmetic.entity.SupArithmeticsPackage;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

/**
 * 算法信息管理DAO接口
 * @author yang.kq
 * @version 2019-11-22
 */
@MyBatisDao
public interface SupArithmeticsPackageDao extends CrudDao<SupArithmeticsPackage> {
    public SupArithmeticsPackage findListByName(SupArithmeticsPackage supArithmeticsPackage);
    public int selectCounts(SupArithmeticsPackage supArithmeticsPackage);
    public int updateAriState(SupArithmeticsPackage supArithmeticsPackage);
    public SupArithmeticsPackage findObjectByAirName(@Param("ariName") String ariName);
}