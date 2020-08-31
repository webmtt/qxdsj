package com.nmpiesat.idata.products.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nmpiesat.idata.products.entity.BmpProductinfoWithBLOBs;
import com.nmpiesat.idata.products.entity.ProDataindex;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@DS("slave")
public interface BmpProductinfoDAO {

    @DS("master")
    List<Map<String,Object>> getUnitSum(@Param("unit") String unit,@Param("dataType") String dataType);

    List<Map<String, Object>> getUnitSumForTimes(@Param("unit") String unit,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Map<String, Object>> getUnitCount(@Param("unit") String unit, @Param("productname") String productname);

    List<ProDataindex> getMorePhotoUT(@Param("num") int num, @Param("photoTypeList") String[] photoTypeList, @Param("unitList")String[] unitList, @Param("typesList") String[] typesList,@Param("productList") String[] productList);

    List<ProDataindex> getMenuPhotoUT(@Param("unitList")String[] unitList, @Param("typesList") String[] typesList,@Param("productList") String[] productList,@Param("photoTypeList") String[] photoTypeList);

    String getCounts(@Param("unit") String unit, @Param("productname") String productname);
}