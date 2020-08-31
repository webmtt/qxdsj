package com.thinkgem.jeesite.modules.products.dao;

import com.thinkgem.jeesite.modules.products.entity.BmpProductinfoWithBLOBs;
import com.thinkgem.jeesite.modules.products.entity.Newproducts;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface BmpProductinfoDAO {
    int insert(BmpProductinfoWithBLOBs record);

    int insertSelective(BmpProductinfoWithBLOBs record);

    List<Map<String,Object>> getAllUnit(Newproducts newproducts);

    List<Map<String,Object>> getAllUnitType(Newproducts newproducts);

    List<Map<String,Object>> getAllUnitProduct(Newproducts newproducts);

    List<Map<String,Object>> getUnitSum(@Param("unit") String unit, @Param("dataType") String dataType);

    List<Map<String, Object>> getUnitSumForTimes(@Param("unit") String unit, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> getUnitCount(@Param("unit") String unit, @Param("productname") String productname);

    List<Map<String, Object>> getToken();

    List<String> getNewProduct(@Param("tokenscode") String tokenscode);
}