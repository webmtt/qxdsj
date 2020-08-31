package com.nmpiesat.idata.products.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nmpiesat.idata.products.entity.Newproducts;
import com.nmpiesat.idata.products.entity.ProDataindex;
import com.nmpiesat.idata.products.entity.ProductLog;
import com.nmpiesat.idata.products.entity.ProductesConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ProductesDao {

    ProductesConfig selectByPrimaryKey(String id);

    void insert(ProductesConfig productesConfig);

    void updateByPrimaryKey(ProductesConfig productesConfig);

    List<Map> getConfig();

    @DS("slave")
    List<Map<String,Object>> getUnitTypes(@Param("alerteunitList") String[] alerteunitList, @Param("alertetypeList") String[] alertetypeList,@Param("alerteproductList") String[] alerteproductList);

    @DS("slave")
    List<Map<String,Object>> getProduct(@Param("forecastunitList") String[] forecastunitList,@Param("forecasttypeList") String[] forecasttypeList,@Param("forecastproductList") String[] forecastproductList);

    ProductesConfig getProductByUrl(String url);

    @DS("slave")
    void insertLog(ProductLog productLog);

    @DS("oracle")
    void updateOracle(@Param("id") String id);

    @DS("slave")
    List<Map<String, Object>> getFactor();

    @DS("slave")
    List<Map<String, Object>> getBmpProductInfo();

    @DS("slave")
    List<String> getType(@Param("products") String[] products);

    @DS("slave")
    List<Map<String, Object>> getAllUnitTypes(@Param("type") String type,@Param("products") String[] products);

    @DS("slave")
    Map<String, Object> getTypeName(@Param("typeCode") String typeCode);
}
