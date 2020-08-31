package com.thinkgem.jeesite.modules.sys.dao;


import com.thinkgem.jeesite.modules.sys.entity.Userinterface;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface UserInterDao {

    public boolean findById(@Param("id") String id);

    public void saveUserinter(@Param("id") String id);

    void saveUserinterface(Userinterface userinterface);

    void deltetUserinterface(@Param("dataroleId") String dataroleId);
}
