package com.nmpiesat.idata.products.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nmpiesat.idata.products.entity.ProDataindex;
import com.nmpiesat.idata.products.entity.UploadProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProDataindexDAO {

    List<UploadProduct> getProductsFile();

    @DS("slave")
    int statisticsUnits(@Param("id") String id);

    @DS("slave")
    List<String> getUnit();

    @DS("slave")
    List<ProDataindex> getMoreVersionUT(@Param("unitList") String[] unitList, @Param("typesList") String[] typesList,@Param("productList") String[] productList);

    @DS("slave")
    List<ProDataindex> getAlltypsFile(@Param("typscode") String typscode);

    @DS("slave")
    List<ProDataindex> getAllProductcodeFile(@Param("productcode") String productcode);

    @DS("slave")
    List<Map<String, Object>> getSubType();

    @DS("slave")
    List<Map<String, Object>> getAllSub(@Param("upType") String upType);

    Map<String, Object> getProductRepert();
}