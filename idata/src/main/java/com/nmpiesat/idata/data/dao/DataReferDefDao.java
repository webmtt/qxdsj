package com.nmpiesat.idata.data.dao;

import com.nmpiesat.idata.data.entity.DataReferDef;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DataReferDefDao {
    List<DataReferDef> findDataReferDefByDataCode(String dataCode);
}
