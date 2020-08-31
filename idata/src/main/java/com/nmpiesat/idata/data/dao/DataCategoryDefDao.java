package com.nmpiesat.idata.data.dao;

import com.nmpiesat.idata.data.entity.DataCateDef;
import com.nmpiesat.idata.data.entity.DataCategoryDef;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataCategoryDefDao  {
    List<DataCategoryDef> findByUserId(String userid,String keyword);

    List<DataCategoryDef> findAll();

    List<DataCategoryDef> findDataCategoryDefById(int id);

    List<DataCategoryDef> findDataCategoryDefByUserId(String userid, int id);

    DataCategoryDef findDataCategoryDefByIdUnique(int id);

    List<DataCateDef> findDataCategory();

    List<DataCategoryDef> findDataTypeCount();
}
