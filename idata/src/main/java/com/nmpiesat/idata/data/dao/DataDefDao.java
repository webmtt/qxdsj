package com.nmpiesat.idata.data.dao;

import com.nmpiesat.idata.data.entity.CategoryDataRelt;
import com.nmpiesat.idata.data.entity.DataDef;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataDefDao {

    Integer findCount(List<String> value);

    List<DataDef> findlist(@Param("list") List<String> list, @Param("beginNum") int beginNum, @Param("endNum") int endNum);

    DataDef findDataDefByDataCode(String dataCode);

    List<DataDef> finlistByCateId(int categoryid);

    List<DataDef> finlistByCateName(String name);
}
